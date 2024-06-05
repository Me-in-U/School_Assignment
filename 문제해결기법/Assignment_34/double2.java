package 문제해결기법.Assignment_34;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;

public class double2 {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_34\\double.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_34\\0.inp"));
        int n = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while (n-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            BigDecimal bd1 = new BigDecimal(st.nextToken());
            char c = st.nextToken().charAt(0);
            BigDecimal bd2 = new BigDecimal(st.nextToken());
            BigDecimal result = BigDecimal.ZERO;
            if (c == '+') {
                result = bd1.add(bd2);
            } else if (c == '-') {
                result = bd1.subtract(bd2);
            } else if (c == '*') {
                result = bd1.multiply(bd2);
            } else if (c == '/') {
                result = bd1.divideAndRemainder(bd2)[0];
            }
            sb.append(result.stripTrailingZeros().toPlainString()).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}
