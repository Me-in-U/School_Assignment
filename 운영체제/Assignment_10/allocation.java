package 운영체제.Assignment_10;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class allocation {
  protected static List<int[]> memory;
  protected static Queue<Process> readyQueue;
  protected static Queue<Process> readyQueueTemp;
  protected static PriorityQueue<ProcessEndTime> endQueue;
  private static int time;
  private static int n;
  private static int result;
  private static boolean lastAllocated;

  public static class Process implements Comparable<Process> {
    int id = -1;
    int requestTime;
    int runTime;
    int size;

    Process(int requestTime, int runTime, int size) {
      this.requestTime = requestTime;
      this.runTime = runTime;
      this.size = size;
    }

    @Override
    public int compareTo(Process o) {
      return this.requestTime - o.requestTime;
    }
  }

  public static class ProcessEndTime implements Comparable<ProcessEndTime> {
    int id;
    int endTime;

    ProcessEndTime(int id, int endTime) {
      this.id = id;
      this.endTime = endTime;
    }

    @Override
    public int compareTo(ProcessEndTime o) {
      return this.endTime - o.endTime;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_10/allocation.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_10/0.inp"));
    n = Integer.parseInt(br.readLine().trim());
    List<Process> processes = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      String[] data = br.readLine().trim().split(" ");
      processes.add(new Process(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])));
    }
    br.close();
    Collections.sort(processes);
    for (int i = 0; i < processes.size(); i++) {
      processes.get(i).id = i;
    }
    StringBuilder sb = new StringBuilder();
    List<Process> newProcesses = null;
    for (int i = 0; i < 3; i++) {
      if (i == 0) {
        System.out.println("First Fit");
      } else if (i == 1) {
        System.out.println("Best Fit");
      } else {
        System.out.println("Worst Fit");
      }
      System.out.println("INIT MEMORY : <0 -1 1000>");
      newProcesses = new LinkedList<>(processes);
      sb.append(MemoryAllocation(i, newProcesses)).append('\n');
    }
    bs.write(sb.toString().getBytes());
    bs.close();
  }

  public static int MemoryAllocation(int method, List<Process> processes) {
    endQueue = new PriorityQueue<>();
    readyQueue = new LinkedList<>();
    memory = new LinkedList<>();
    lastAllocated = false;
    result = 0;
    time = 0;
    memory.add(new int[] { 0, -1, 1000 });
    while (!lastAllocated) {
      MemoryReturn();
      readyQueueTemp = new LinkedList<>();
      while (!readyQueue.isEmpty()) {
        if (method == 0) {
          MemoryAllocation(ff_MemorySpaceCheck(readyQueue.peek().size), readyQueue.poll());
        } else if (method == 1) {
          MemoryAllocation(bf_MemorySpaceCheck(readyQueue.peek().size), readyQueue.poll());
        } else {
          MemoryAllocation(wf_MemorySpaceCheck(readyQueue.peek().size), readyQueue.poll());
        }
      }
      while (!processes.isEmpty()) {
        if (processes.get(0).requestTime == time) {
          if (method == 0) {
            MemoryAllocation(ff_MemorySpaceCheck(processes.get(0).size), processes.remove(0));
          } else if (method == 1) {
            MemoryAllocation(bf_MemorySpaceCheck(processes.get(0).size), processes.remove(0));
          } else {
            MemoryAllocation(wf_MemorySpaceCheck(processes.get(0).size), processes.remove(0));
          }
        } else {
          break;
        }
      }
      readyQueue = new LinkedList<>(readyQueueTemp);
      time++;
    }
    System.out.println(result);
    return result;
  }

  public static void printMemoryUsage() {
    System.out.print("Time[" + time + "] ");
    for (int i = 0; i < memory.size(); i++) {
      System.out.print("<" + memory.get(i)[0] + " " + memory.get(i)[1] + " " + memory.get(i)[2] + "> ");
    }
    System.out.println();
  }

  public static int ff_MemorySpaceCheck(int size) {
    for (int index = 0; index < memory.size(); index++) {
      if (memory.get(index)[1] == -1 && memory.get(index)[2] >= size) {
        return index;
      }
    }
    return -1;
  }

  public static int bf_MemorySpaceCheck(int size) {
    int minSpace = 1001;
    int minSpaceIndex = -1;
    for (int index = 0; index < memory.size(); index++) {
      if (memory.get(index)[1] == -1 && memory.get(index)[2] >= size) {
        if (memory.get(index)[2] < minSpace) {
          minSpace = memory.get(index)[2];
          minSpaceIndex = index;
        }
      }
    }
    return minSpaceIndex;
  }

  public static int wf_MemorySpaceCheck(int size) {
    int maxSpace = -1;
    int maxSpaceIndex = -1;
    for (int index = 0; index < memory.size(); index++) {
      if (memory.get(index)[1] == -1 && memory.get(index)[2] >= size) {
        if (memory.get(index)[2] > maxSpace) {
          maxSpace = memory.get(index)[2];
          maxSpaceIndex = index;
        }
      }
    }
    return maxSpaceIndex;
  }

  public static void MemoryAllocation(int index, Process process) {
    if (index != -1) {
      int[] subMemory = memory.remove(index);
      memory.add(index, new int[] { subMemory[0], process.id, process.size });
      if (subMemory[0] + process.size != 1000) {
        memory.add(index + 1, new int[] { subMemory[0] + process.size, -1, subMemory[2] - process.size });
      }
      endQueue.add(new ProcessEndTime(process.id, time + process.runTime));
      if (process.id == n - 1) {
        lastAllocated = true;
        result = subMemory[0];
      }
      System.out.print("ALC " + process.id + " ");
      printMemoryUsage();
    } else {
      readyQueueTemp.add(process);
    }
  }

  public static void MemoryReturn() {
    while (!endQueue.isEmpty()) {
      if (endQueue.peek().endTime <= time) {
        int removeId = endQueue.remove().id;
        int index = MemoryIndexCheck(removeId);
        memory.get(index)[1] = -1;
        if (memory.size() != 1) {
          if (index == 0) {
            if (memory.get(index + 1)[1] == -1)
              memory.get(index)[2] += memory.remove(index + 1)[2];
          } else if (index == memory.size() - 1) {
            if (memory.get(index - 1)[1] == -1)
              memory.get(index - 1)[2] += memory.remove(index)[2];
          } else {
            if (memory.get(index + 1)[1] == -1)
              memory.get(index)[2] += memory.remove(index + 1)[2];
            if (memory.get(index - 1)[1] == -1)
              memory.get(index - 1)[2] += memory.remove(index)[2];
          }
        }
        System.out.print("RTN " + removeId + " ");
        printMemoryUsage();
      } else {
        break;
      }
    }
  }

  public static int MemoryIndexCheck(int id) {
    for (int index = 0; index < memory.size(); index++)
      if (memory.get(index)[1] == id)
        return index;
    return -1;
  }
}