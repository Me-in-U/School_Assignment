package 운영체제.Assignment_03;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class batch {
  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("batch.out"));
    BufferedReader br = new BufferedReader(new FileReader("batch.inp"));
    StringBuilder sb = new StringBuilder();
    int timeTotal = 0;
    int timeIO = 0;
    int N = Integer.parseInt(br.readLine());
    for (int i = 0; i < N; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int k = 0;
      boolean isIO = false;
      while ((k = Integer.parseInt(st.nextToken())) != -1) {
        if (isIO) {
          timeIO += k;
        }
        timeTotal += k;
        isIO = !isIO;
      }
    }
    sb.append(timeIO + " " + timeTotal).append('\n');
    bs.write(sb.toString().getBytes());
    bs.close();
    br.close();
  }
}
