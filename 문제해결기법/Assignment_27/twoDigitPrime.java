package 문제해결기법.Assignment_27;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class twoDigitPrime {
    protected static boolean[] isPrime;
    protected static boolean[] isTwoDigitPrime;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_27\\twoDigitPrime.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_27\\0.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        isPrime = new boolean[100];
        isTwoDigitPrime = new boolean[100001];
        for (int i = 1; i < 100; i++)
            isPrime[i] = isPrime(i);
        for (int i = 11; i <= 100000; i++)
            isTwoDigitPrime[i] = isTwoDigitPrimeCheck(i);
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int count = 0;
            for (int i = from; i <= to; i++)
                if (isTwoDigitPrime[i])
                    count++;
            sb.append(count).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    public static boolean isPrime(int num) {
        if (num <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++)
            if (num % i == 0)
                return false;
        return true;
    }

    public static boolean isTwoDigitPrimeCheck(int num) {
        String number = String.valueOf(num);
        for (int l = 0; l < number.length() - 1; l++) {
            for (int r = l + 1; r < number.length(); r++) {
                int twoDigit1 = ((number.charAt(l) - '0') * 10) + (number.charAt(r) - '0');
                int twoDigit2 = ((number.charAt(r) - '0') * 10) + (number.charAt(l) - '0');
                if (twoDigit1 > 10 && isPrime[twoDigit1])
                    return true;
                if (twoDigit2 > 10 && isPrime[twoDigit2])
                    return true;
            }
        }
        return false;
    }
}