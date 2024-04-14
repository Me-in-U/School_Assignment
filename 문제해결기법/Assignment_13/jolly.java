package 문제해결기법.Assignment_13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Arrays;

public class jolly {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("jolly.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_13\\2.inp"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (numbers[0] == 1) {
                sb.append("Jolly").append('\n');
                continue;
            }
            HashSet<Integer> diffs = new HashSet<>();
            for (int i = 2; i < numbers.length; i++) {
                int diff = Math.abs(numbers[i] - numbers[i - 1]);
                diffs.add(diff);
            }
            boolean isJolly = true;
            for (int i = 1; i < numbers.length - 1; i++) {
                if (!diffs.contains(i)) {
                    isJolly = false;
                    break;
                }
            }
            sb.append(isJolly ? "Jolly" : "Not Jolly").append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}
