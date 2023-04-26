package 운영체제.Assignment_07;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class srtf {
  public static class Process implements Comparable<Process> {
    int ID;
    int timeWaited;
    int timeCpu;

    Process(int id, int timeCpu) {
      this.ID = id;
      this.timeCpu = timeCpu;
      this.timeWaited = 0;
    }

    @Override
    public int compareTo(Process o) {
      return this.timeCpu - o.timeCpu;
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
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_07/out.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_07/2.inp"));
    int N = Integer.parseInt(br.readLine());
    ArrayList<Process> readyQueue = new ArrayList<>();
    PriorityQueue<NotReady> notReady = new PriorityQueue<>();
    int[] processWaitTimes = new int[N];
    StringTokenizer st = null;
    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(br.readLine());
      int timeArrived = Integer.parseInt(st.nextToken());
      int timeCpu = Integer.parseInt(st.nextToken());
      notReady.add(new NotReady(timeArrived, new Process(i, timeCpu)));
    }
    br.close();
    int time = 0;
    while (!readyQueue.isEmpty() || !notReady.isEmpty()) {
      while (!notReady.isEmpty()) {
        if (notReady.peek().timeArrived <= time)
          readyQueue.add(notReady.poll().process);
        else
          break;
      }
      if (!readyQueue.isEmpty()) {
        Collections.sort(readyQueue);
        readyQueue.get(0).timeCpu--;
        for (int i = 1; i < readyQueue.size(); i++) {
          readyQueue.get(i).timeWaited++;
        }
        if (readyQueue.get(0).timeCpu == 0) {
          processWaitTimes[readyQueue.get(0).ID] = readyQueue.get(0).timeWaited;
          readyQueue.remove(0);
        }
      }
      time++;
    }
    int sum = 0;
    for (int i = 0; i < N; i++) {
      sum += processWaitTimes[i];
    }
    bs.write(String.valueOf(sum).getBytes());
    bs.close();
  }
}