package 문제해결기법.Assignment_01;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final int MAX = 1000000;
    private static int[] cycleLength = new int[MAX + 1];

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("3nplus1.out"));
        BufferedReader br = new BufferedReader(new FileReader("3nplus1.inp"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            String[] IJ = line.split(" ");
            int i = Integer.parseInt(IJ[0]);
            int j = Integer.parseInt(IJ[1]);
            sb.append(line)
                    .append(' ')
                    .append(findMaxCycleLength(i, j))
                    .append('\n');
        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }

    private static int findMaxCycleLength(int i, int j) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        int maxCycleLength = 0;
        for (int k = i; k <= j; k++) {
            int cycleLength = calculateCycleLength(k);
            if (cycleLength > maxCycleLength) {
                maxCycleLength = cycleLength;
            }
        }
        return maxCycleLength;
    }

    private static int calculateCycleLength(int n) {
        if (n < MAX && cycleLength[n] != 0) {
            return cycleLength[n];
        }
        int cycle = 1;
        int originalN = n;
        while (n != 1) {
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }
            cycle++;
            if (n < MAX && cycleLength[n] != 0) {
                cycle += cycleLength[n] - 1;
                break;
            }
        }
        if (originalN < MAX) {
            cycleLength[originalN] = cycle;
        }
        return cycle;
    }
}