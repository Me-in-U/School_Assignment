import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class dish {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("dish.out"));
        BufferedReader br = new BufferedReader(new FileReader("dish.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int N = Integer.parseInt(br.readLine());
            String dishes = br.readLine();
            int height = 10;
            for (int i = 1; i < N; i++)
                height += (dishes.charAt(i - 1) == dishes.charAt(i)) ? 5 : 10;
            sb.append(height).append('\n');
        }
        bw.write(sb.toString().trim());
        bw.flush();
        bw.close();
        br.close();
    }
}