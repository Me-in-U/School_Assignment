package 문제해결기법.Assignment_14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.math.BigInteger;

public class crt {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("crt.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_14\\0.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int k = Integer.parseInt(br.readLine());
            BigInteger n = BigInteger.ONE;
            BigInteger[] ai = new BigInteger[k];
            BigInteger[] ni = new BigInteger[k];
            for (int i = 0; i < k; i++) {
                st = new StringTokenizer(br.readLine());
                ai[i] = new BigInteger(st.nextToken());
                ni[i] = new BigInteger(st.nextToken());
                n = n.multiply(ni[i]);
            }
            System.out.println(n.toString());
            BigInteger[] Ni = new BigInteger[k];
            BigInteger[] xi = new BigInteger[k];
            for (int i = 0; i < k; i++) {
                Ni[i] = n.divide(ni[i]);
                if (!Ni[i].gcd(ni[i]).equals(BigInteger.ONE)) {
                    throw new ArithmeticException("Ni[i] and ni[i] are not coprime.");
                }
                xi[i] = Ni[i].modInverse(ni[i]); // 역원을 계산합니다.
            }
            BigInteger xBar = BigInteger.ZERO;
            for (int i = 0; i < k; i++) {
                xBar = xBar.add(ai[i].multiply(Ni[i]).multiply(xi[i]));
            }
            System.out.println(xBar.mod(n));
            sb.append(xBar.mod(n)).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}