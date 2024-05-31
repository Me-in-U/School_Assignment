package 문제해결기법.Assignment_34;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class double1 {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_34\\double.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_34\\1.inp"));
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(br.readLine());
        while (n-- > 0) {
            String input = br.readLine();
            String[] number = new String[2];
            char c = ' ';
            BigDecimal bd1 = BigDecimal.ZERO;
            BigDecimal bd2 = BigDecimal.ZERO;
            for (int i = 1; i < input.length(); i++) {
                c = input.charAt(i);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    number[0] = input.substring(0, i - 1).trim();
                    number[1] = input.substring(i + 2, input.length()).trim();
                    bd1 = new BigDecimal(number[0]);
                    bd2 = new BigDecimal(number[1]);
                    break;
                }
            }
            BigDecimal result = BigDecimal.ZERO;
            if (c == '+') {
                result = bd1.add(bd2);
            } else if (c == '-') {
                result = bd1.subtract(bd2);
            } else if (c == '*') {
                result = bd1.multiply(bd2);
            } else if (c == '/') {
                result = bd1.divideToIntegralValue(bd2);
            }
            sb.append(result.stripTrailingZeros().toPlainString()).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}
