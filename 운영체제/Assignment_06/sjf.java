package 운영체제.Assignment_06;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class sjf {
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
      int thisCPU = this.cpuIO.peek();
      int oCPU = o.cpuIO.peek();
      if (thisCPU == oCPU) {
        if (this.readyQueueInTime == o.readyQueueInTime) {
          return this.ID - o.ID;
        }
        return this.readyQueueInTime - o.readyQueueInTime;
      }
      return thisCPU - oCPU;
    }
  }

  public static class ProcessOut implements Comparable<ProcessOut> {
    Process process;

    ProcessOut(Process process) {
      this.process = process;
    }

    @Override
    public int compareTo(ProcessOut o) {
      int thisTime = this.process.readyQueueInTime;
      int oTime = o.process.readyQueueInTime;
      return thisTime - oTime;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_06/sjf.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_06/2.inp"));
    int N = Integer.parseInt(br.readLine());
    int idleTime = 0;
    int nowTime = 0;
    int[] endTime = new int[N];
    PriorityQueue<Process> readyQueue = new PriorityQueue<>();
    PriorityQueue<ProcessOut> notReadyQueue = new PriorityQueue<>();
    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int createTime = Integer.parseInt(st.nextToken());
      LinkedList<Integer> cpuIO = new LinkedList<>();
      while (st.hasMoreTokens()) {
        cpuIO.add(Integer.parseInt(st.nextToken()));
      }
      notReadyQueue.add(new ProcessOut(new Process(i, createTime, cpuIO)));
    }
    while (!readyQueue.isEmpty() || !notReadyQueue.isEmpty()) {
      while (!notReadyQueue.isEmpty() && notReadyQueue.peek().process.readyQueueInTime <= nowTime) {
        readyQueue.add(notReadyQueue.poll().process);
      }
      if (readyQueue.isEmpty()) {
        idleTime++;
        nowTime++;
        continue;
      }
      Process process = readyQueue.poll();
      if (process.readyQueueInTime > nowTime) {
        idleTime += process.readyQueueInTime - nowTime;
        nowTime = process.readyQueueInTime;
      }
      int cpuTime = process.cpuIO.poll();
      int ioTime = process.cpuIO.poll();
      process.readyQueueInTime = nowTime + cpuTime + ioTime;
      nowTime += cpuTime;
      if (process.cpuIO.size() > 1) {
        notReadyQueue.add(new ProcessOut(process));
      } else {
        if (ioTime == -1) {
          endTime[process.ID] = process.readyQueueInTime + 1;
        } else {
          endTime[process.ID] = process.readyQueueInTime;
        }
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