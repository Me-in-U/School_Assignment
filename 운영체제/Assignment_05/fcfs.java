package 운영체제.Assignment_05;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class fcfs {
  public static class Process implements Comparable<Process> {
    int ID;
    int readyQueueInTime;
    LinkedList<Integer> cpuIO;

    Process(int id, int readyQueueInTime, LinkedList<Integer> cpuIO) {
      this.ID = id;
      this.readyQueueInTime = readyQueueInTime;
      this.cpuIO = cpuIO;
    }

    @Override
    public int compareTo(Process o) {
      if (this.readyQueueInTime == o.readyQueueInTime) {
        return this.ID - o.ID;
      }
      return this.readyQueueInTime - o.readyQueueInTime;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("fcfs.out"));
    BufferedReader br = new BufferedReader(new FileReader("fcfs.inp"));
    int N = Integer.parseInt(br.readLine());
    int idleTime = 0;
    int nowTime = 0;
    int[] endTime = new int[N];
    PriorityQueue<Process> readyQueue = new PriorityQueue<>();
    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int createTime = Integer.parseInt(st.nextToken());
      LinkedList<Integer> cpuIO = new LinkedList<>();
      while (st.hasMoreTokens()) {
        cpuIO.add(Integer.parseInt(st.nextToken()));
      }
      readyQueue.add(new Process(i, createTime, cpuIO));
    }
    while (!readyQueue.isEmpty()) {
      Process process = readyQueue.poll();
      if (process.readyQueueInTime <= nowTime) {
        int cpuTime = process.cpuIO.poll();
        int ioTime = process.cpuIO.poll();
        process.readyQueueInTime = nowTime + cpuTime + ioTime;
        nowTime += cpuTime;
        if (process.cpuIO.size() > 1) {
          readyQueue.add(process);
        } else {
          if (ioTime == -1) {
            endTime[process.ID] = process.readyQueueInTime + 1;
          } else {
            endTime[process.ID] = process.readyQueueInTime;
          }
        }
      } else {
        idleTime++;
        nowTime++;
        readyQueue.add(process);
      }
    }
    StringBuilder sb = new StringBuilder();
    sb.append(idleTime).append('\n');
    for (int i = 0; i < N; i++) {
      sb.append(endTime[i]).append('\n');
    }
    bs.write(sb.toString().getBytes());
    bs.close();
    br.close();
  }
}