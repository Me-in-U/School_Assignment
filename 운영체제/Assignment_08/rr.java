package 운영체제.Assignment_08;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class rr {
  public static class Process {
    int ID;
    int timeWaited;
    LinkedList<Integer> cpuIO;

    Process(int id, LinkedList<Integer> cpuIO) {
      this.ID = id;
      this.cpuIO = cpuIO;
      this.timeWaited = 0;
    }
  }

  public static class NotReady implements Comparable<NotReady> {
    int timeArrived;
    Process process;

    NotReady(int timeArrived, Process process) {
      this.timeArrived = timeArrived;
      this.process = process;
    }

    @Override
    public int compareTo(NotReady o) {
      if (this.timeArrived == o.timeArrived) {
        return this.process.ID - o.process.ID;
      }
      return this.timeArrived - o.timeArrived;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_08/out.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_08/2.inp"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int q = Integer.parseInt(st.nextToken());
    ArrayList<Process> readyQueue = new ArrayList<>();
    PriorityQueue<NotReady> notReady = new PriorityQueue<>();
    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(br.readLine());
      int timeArrived = Integer.parseInt(st.nextToken());
      LinkedList<Integer> cpuIO = new LinkedList<>();
      while (st.hasMoreTokens()) {
        cpuIO.add(Integer.parseInt(st.nextToken()));
      }
      notReady.add(new NotReady(timeArrived, new Process(i, cpuIO)));
    }
    br.close();
    int[] processEndTimes = new int[N];
    int idleTime = 0;
    int time = 0;
    while (!readyQueue.isEmpty() || !notReady.isEmpty()) {
      while (!notReady.isEmpty()) {
        if (notReady.peek().timeArrived <= time)
          readyQueue.add(notReady.poll().process);
        else
          break;
      }
      if (!readyQueue.isEmpty()) {
        int cpuTime = readyQueue.get(0).cpuIO.remove(0);
        if (cpuTime <= q) {
          time += cpuTime;
          int ioTime = readyQueue.get(0).cpuIO.remove(0);
          if (ioTime == -1 || readyQueue.get(0).cpuIO.get(0) == -1) {
            if (ioTime != -1) {
              processEndTimes[readyQueue.remove(0).ID] = time + ioTime;
            } else {
              processEndTimes[readyQueue.remove(0).ID] = time;
            }
          } else {
            notReady.add(new NotReady(time + ioTime, readyQueue.remove(0)));
          }
        } else {
          time += q;
          readyQueue.get(0).cpuIO.add(0, cpuTime - q);
          notReady.add(new NotReady(time, readyQueue.remove(0)));
        }
      } else {
        idleTime++;
        time++;
      }
    }
    StringBuilder sb = new StringBuilder();
    sb.append(idleTime).append('\n');
    for (int i = 0; i < N; i++) {
      sb.append(processEndTimes[i]).append('\n');
    }
    bs.write(sb.toString().getBytes());
    bs.close();
  }
}