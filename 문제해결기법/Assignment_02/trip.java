package 문제해결기법.Assignment_02;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class trip {
    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("trip.out"));
        BufferedReader br = new BufferedReader(new FileReader("trip.inp"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            int n = Integer.parseInt(line);
            if (n == 0)
                break;
            double[] expenses = new double[n];
            double sum = 0;
            for (int i = 0; i < n; i++) {
                expenses[i] = Double.parseDouble(br.readLine());
                sum += expenses[i];
            }
            double average = sum / n;
            double diffUp = 0;
            double diffDown = 0;
            for (double expense : expenses) {
                double diff = expense - average;
                if (diff > 0) {
                    diffUp += Math.floor(diff * 100) / 100;
                } else {
                    diffDown += Math.floor(-diff * 100) / 100;
                }
            }
            double result = Math.max(diffUp, diffDown);
            sb.append(String.format("$%.2f", result)).append('\n');
        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
