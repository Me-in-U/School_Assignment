package 문제해결기법.Assignment_14;

import java.io.*;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class crt2 {
    private static BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO))
            return new BigInteger[] { a, BigInteger.ONE, BigInteger.ZERO };
        BigInteger[] values = extendedGCD(b, a.mod(b));
        BigInteger gcd = values[0];
        BigInteger x = values[2];
        BigInteger y = values[1].subtract(a.divide(b).multiply(values[2]));
        return new BigInteger[] { gcd, x, y };
    }

    private static BigInteger[] mergeCongruences(BigInteger a1, BigInteger m1, BigInteger a2, BigInteger m2) {
        BigInteger[] gcdResult = extendedGCD(m1, m2);
        BigInteger gcd = gcdResult[0];
        BigInteger m1Inverse = gcdResult[1];

        if (!a1.subtract(a2).mod(gcd).equals(BigInteger.ZERO))
            throw new ArithmeticException("No solution exists for the given congruences.");

        BigInteger m = m1.divide(gcd).multiply(m2); // lcm(m1, m2)
        BigInteger x = a1.add(m1.multiply(m1Inverse.multiply(a2.subtract(a1).divide(gcd)).mod(m2.divide(gcd))));

        return new BigInteger[] { x.mod(m), m }; // Return the solution and new modulus
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_14\\1.inp"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("crt.out"));
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int k = Integer.parseInt(br.readLine());
            BigInteger a = BigInteger.ZERO;
            BigInteger m = BigInteger.ONE;
            boolean solutionExists = true;
            for (int i = 0; i < k; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                BigInteger a1 = new BigInteger(st.nextToken());
                BigInteger m1 = new BigInteger(st.nextToken());
                if (i == 0) {
                    a = a1;
                    m = m1;
                } else {
                    try {
                        BigInteger[] result = mergeCongruences(a, m, a1, m1);
                        a = result[0];
                        m = result[1];
                    } catch (ArithmeticException e) {
                        solutionExists = false;
                    }
                }
            }
            if (solutionExists) {
                bw.write(a.toString());
            } else {
                bw.write("-1");
            }
            bw.newLine();
        }
        br.close();
        bw.close();
    }
}
