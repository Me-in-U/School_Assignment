package 문제해결기법.Assignment_14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.math.BigInteger;

public class crt {

    private static BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO))
            return new BigInteger[] { a, BigInteger.ONE, BigInteger.ZERO };
        BigInteger[] values = extendedGCD(b, a.mod(b));
        BigInteger gcd = values[0];
        BigInteger x = values[2];
        BigInteger y = values[1].subtract(a.divide(b).multiply(values[2]));
        return new BigInteger[] { gcd, x, y };
    }

    private static BigInteger[] mergeCongruences(BigInteger r1, BigInteger m1, BigInteger r2, BigInteger m2) {
        BigInteger[] gcdResult = extendedGCD(m1, m2);
        BigInteger gcd = gcdResult[0];
        BigInteger m1Inverse = gcdResult[1];
        if (!r1.subtract(r2).mod(gcd).equals(BigInteger.ZERO))
            throw new ArithmeticException();
        BigInteger m = m1.divide(gcd).multiply(m2);
        BigInteger x = r1.add(m1.multiply(m1Inverse.multiply(r2.subtract(r1).divide(gcd)).mod(m2.divide(gcd))));
        return new BigInteger[] { x.mod(m), m };
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_14\\2.inp"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("crt.out"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int k = Integer.parseInt(br.readLine());
            BigInteger r = BigInteger.ZERO;
            BigInteger m = BigInteger.ONE;
            boolean solutionExists = true;
            for (int i = 0; i < k; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                BigInteger r1 = new BigInteger(st.nextToken());
                BigInteger m1 = new BigInteger(st.nextToken());
                if (i == 0) {
                    r = r1;
                    m = m1;
                } else if (solutionExists) {
                    try {
                        BigInteger[] result = mergeCongruences(r, m, r1, m1);
                        r = result[0];
                        m = result[1];
                    } catch (ArithmeticException e) {
                        solutionExists = false;
                    }
                }
            }
            sb.append(solutionExists ? r.toString() : -1).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
