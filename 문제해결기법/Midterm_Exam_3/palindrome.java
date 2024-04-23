import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class palindrome {

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("palindrome.out"));
        BufferedReader br = new BufferedReader(new FileReader("palindrome.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int n = Integer.parseInt(br.readLine());
            int index = 0;
            while (index < 1000 && !check(n)) {
                index++;
                StringBuilder temp = new StringBuilder().append(String.valueOf(n)).reverse();
                n = Math.abs(n - Integer.parseInt(temp.toString()));
            }
            sb.append(index == 1000 ? -1 : index).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    static boolean check(int number) {
        String s = String.valueOf(number);
        if (s.length() == 1)
            return true;
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
}