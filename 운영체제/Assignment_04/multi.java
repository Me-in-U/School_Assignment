package 운영체제.Assignment_04;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class multi {
  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_04/multi.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_04/1.inp"));
    int N = Integer.parseInt(br.readLine());
    LinkedList<Deque<Integer>> programList = new LinkedList<>();
    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      Deque<Integer> processTimeData = new ArrayDeque<>();
      processTimeData.add(0);
      processTimeData.add(0);
      while (st.hasMoreTokens()) {
        processTimeData.add(Integer.parseInt(st.nextToken()));
      }
      programList.add(processTimeData);
    }
    int timeIdle = 0;
    int timeNow = 0;
    int processNum = 0;
    int programs = programList.size();
    while (processNum < programs) {
      Deque<Integer> processTimeData = programList.remove(processNum);
      int isIO = processTimeData.poll();
      int processEndTime = processTimeData.poll();
      if (isIO == 1) {
        if (processEndTime <= timeNow) {
          isIO = 0;
        } else {
          processTimeData.addFirst(processEndTime);
          processTimeData.addFirst(isIO);
          programList.add(processNum, processTimeData);
          processNum++;
          if (processNum == programs) {
            timeNow++;
            timeIdle++;
            processNum = 0;
          }
          continue;
        }
      }
      if (isIO == 0) {
        int cpuTime = processTimeData.poll();
        int ioTime = -1;
        if (cpuTime != -1) {
          ioTime = processTimeData.poll();
          processEndTime = timeNow + cpuTime;
          timeNow += cpuTime;
        }
        if (cpuTime != -1 && ioTime != -1 && processTimeData.size() > 1) {
          processEndTime += ioTime;
          isIO = 1;
          processTimeData.addFirst(processEndTime);
          processTimeData.addFirst(isIO);
          programList.add(processNum, processTimeData);
        } else if (ioTime != -1 && processTimeData.size() == 1 && programList.isEmpty()) {
          timeNow += ioTime;
          break;
        }
        processNum = 0;
      }
      programs = programList.size();
    }
    System.out.println(timeIdle + " " + timeNow);
    StringBuilder sb = new StringBuilder();
    sb.append(timeIdle + " " + timeNow);
    bs.write(sb.toString().getBytes());
    bs.close();
    br.close();
  }
}
