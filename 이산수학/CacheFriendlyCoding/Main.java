import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    long beforeTime = 0;
    long afterTime = 0;
    int size = 10000;
    int repeat = 50;
    int sum = 0;
    // 코드입력
    beforeTime = System.currentTimeMillis();
    int[][] arr = new int[size][size];
    afterTime = System.currentTimeMillis();
    System.out.println("배열 생성 : " + (afterTime - beforeTime) + "ms 소요");
    System.out.println("배열 크기 : " + size + " * " + size + '\n');

    System.out.println("Cache-Friendly O");
    for (int k = 0; k < repeat; k++) {
      beforeTime = System.currentTimeMillis();
      for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1; j++) {
          arr[i][j] += arr[i][j + 1];
        }
      }
      afterTime = System.currentTimeMillis();
      sum += (afterTime - beforeTime);
      System.out.print((afterTime - beforeTime) + "ms | ");
    }
    System.out.println('\n' + "평균 : " + (sum / repeat) + "ms" + '\n');

    sum = 0;
    System.out.println("Cache-Friendly X");
    for (int k = 0; k < repeat; k++) {
      beforeTime = System.currentTimeMillis();
      for (int j = 0; j < size - 1; j++) {
        for (int i = 0; i < size - 1; i++) {
          arr[i][j] += arr[i + 1][j];
        }
      }
      afterTime = System.currentTimeMillis();
      sum += (afterTime - beforeTime);
      System.out.print((afterTime - beforeTime) + "ms | ");
    }
    System.out.println('\n' + "평균 : " + (sum / repeat) + "ms");
  }
}
