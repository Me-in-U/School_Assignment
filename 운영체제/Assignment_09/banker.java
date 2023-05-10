package 운영체제.Assignment_09;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class banker {
  protected static int[][] need;
  protected static int[][] allocation;
  protected static int[] available;
  public static int N;
  public static int M;
  public static StringBuilder sb = new StringBuilder();
  public static Queue<int[]> queue = new LinkedList<>();

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_09/banker.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_09/banker.inp"));
    StringTokenizer st = new StringTokenizer(br.readLine());
    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());
    st = new StringTokenizer(br.readLine());
    available = new int[M];
    for (int i = 0; i < M; i++) {
      available[i] = Integer.parseInt(st.nextToken());
    }
    br.readLine();
    need = new int[N][M];
    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < M; j++) {
        need[i][j] = Integer.parseInt(st.nextToken());
      }
    }
    br.readLine();
    allocation = new int[N][M];
    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < M; j++) {
        int data = Integer.parseInt(st.nextToken());
        allocation[i][j] = data;
        available[j] -= data;
        need[i][j] -= data;
      }
    }
    System.out.println();
    br.readLine();
    while (true) {
      st = new StringTokenizer(br.readLine());
      String requestOrRelease = st.nextToken();
      if (!requestOrRelease.equals("quit")) {
        int[] rorData = new int[M + 1];
        for (int i = 0; i < M + 1; i++) {
          rorData[i] = Integer.parseInt(st.nextToken());
        }
        if (requestOrRelease.equals("request")) {
          if (checkNeed(rorData)) {
            if (checkAvailable(rorData) && checkSafe(rorData)) {
              request(rorData);
            } else {
              queue.add(rorData);
            }
          }
        } else if (requestOrRelease.equals("release")) {
          release(rorData);
          checkQueue();
        }
        print();
      } else {
        break;
      }
    }
    bs.write(sb.toString().getBytes());
    bs.close();
  }

  public static boolean checkNeed(int[] rorData) {
    int processorNumber = rorData[0];
    for (int i = 0; i < M; i++) {
      if (need[processorNumber][i] < rorData[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean checkAvailable(int[] rorData) {
    for (int i = 0; i < M; i++) {
      if (available[i] < rorData[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean checkAvailable(int[] safeNeed, int[] safeAvailable) {
    for (int i = 0; i < M; i++) {
      if (safeAvailable[i] < safeNeed[i]) {
        return false;
      }
    }
    return true;
  }

  public static boolean checkSafe(int[] rorData) {
    int[] safeAvailable = Arrays.copyOf(available, M);
    int[][] safeNeed = new int[N][];
    int[][] safeAllocation = new int[N][];
    for (int i = 0; i < N; i++) {
      safeNeed[i] = need[i].clone();
      safeAllocation[i] = allocation[i].clone();
    }
    int processorNumber = rorData[0];
    for (int i = 0; i < M; i++) {
      safeAvailable[i] -= rorData[i + 1];
      safeNeed[processorNumber][i] -= rorData[i + 1];
      safeAllocation[processorNumber][i] += rorData[i + 1];
    }
    int count = 0;
    boolean[] done = new boolean[N];
    while (count < N) {
      boolean doneSomething = false;
      for (int i = 0; i < N; i++) {
        if (!done[i]) {
          if (checkAvailable(safeNeed[i], safeAvailable)) {
            for (int j = 0; j < M; j++) {
              safeAvailable[j] += safeAllocation[i][j];
            }
            done[i] = true;
            count++;
            doneSomething = true;
            break;
          }
        }
      }
      if (!doneSomething) {
        return false;
      }
    }
    return true;
  }

  public static void request(int[] rorData) {
    int processorNumber = rorData[0];
    for (int i = 0; i < M; i++) {
      available[i] -= rorData[i + 1];
      need[processorNumber][i] -= rorData[i + 1];
      allocation[processorNumber][i] += rorData[i + 1];
    }
  }

  public static void release(int[] rorData) {
    int processorNumber = rorData[0];
    for (int i = 0; i < M; i++) {
      available[i] += rorData[i + 1];
      need[processorNumber][i] += rorData[i + 1];
      allocation[processorNumber][i] -= rorData[i + 1];
    }
  }

  public static void print() {
    for (int i = 0; i < M; i++) {
      sb.append(available[i] + " ");
    }
    sb.append('\n');
  }

  public static void checkQueue() {
    Queue<int[]> temp = new LinkedList<>();
    while (!queue.isEmpty()) {
      int[] rorData = queue.poll();
      if (checkNeed(rorData)) {
        if (checkAvailable(rorData) && checkSafe(rorData)) {
          request(rorData);
        } else {
          temp.add(rorData);
        }
      }
    }
    queue = temp;
  }
}