import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class multi4 {

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("multi4.out"));
        BufferedReader br = new BufferedReader(new FileReader("multi4.inp"));
        StringBuilder sb = new StringBuilder();
        int N = Integer.parseInt(br.readLine());
        while (N-- > 0) {
            int n = Integer.parseInt(br.readLine());
            List<Long> minus = new ArrayList<>();
            List<Long> plus = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                Long a = Long.parseLong(st.nextToken());
                if (a < 0) {
                    minus.add(a);
                } else {
                    plus.add(a);
                }
            }
            Collections.sort(minus);
            Collections.sort(plus, Collections.reverseOrder());
            int ms = minus.size();
            int ps = plus.size();
            List<Long> values = new ArrayList<>();
            if (minus.size() >= 4) {
                values.add(minus.get(0) * minus.get(1) * minus.get(2) * minus.get(3));
                values.add(minus.get(ms - 1) * minus.get(ms - 2) * minus.get(ms - 3) * minus.get(ms - 4));
            }
            if (minus.size() >= 3 && plus.size() >= 1) {
                values.add(minus.get(0) * minus.get(1) * minus.get(2) * plus.get(0));
                values.add(minus.get(ms - 1) * minus.get(ms - 2) * minus.get(ms - 3) * plus.get(ps - 1));
            }
            if (minus.size() >= 2 && plus.size() >= 2) {
                values.add(minus.get(0) * minus.get(1) * plus.get(0) * plus.get(1));
                values.add(minus.get(ms - 1) * minus.get(ms - 2) * plus.get(ps - 1) * plus.get(ps - 2));
            }
            if (minus.size() >= 1 && plus.size() >= 3) {
                values.add(minus.get(0) * plus.get(0) * plus.get(1) * plus.get(2));
                values.add(minus.get(ms - 1) * plus.get(ps - 3) * plus.get(ps - 2) * plus.get(ps - 3));
            }
            if (plus.size() >= 4) {
                values.add(plus.get(0) * plus.get(1) * plus.get(2) * plus.get(3));
                values.add(plus.get(ps - 1) * plus.get(ps - 2) * plus.get(ps - 3) * plus.get(ps - 4));
            }
            Collections.sort(values);
            sb.append(values.get(0)).append(' ').append(values.get(values.size() - 1)).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}