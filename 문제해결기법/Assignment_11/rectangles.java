import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class rectangles {
    private static TreeMap<Integer, Integer> perimeterMap = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("rectangles.out"));
        BufferedReader br = new BufferedReader(new FileReader("rectangles.inp"));
        StringBuilder sb = new StringBuilder();

        int t = Integer.parseInt(br.readLine());
        Deque<Integer> lengths = new ArrayDeque<>();
        int maxLength = 0;
        while (t-- > 0) {
            int givenLength = Integer.parseInt(br.readLine());
            lengths.add(givenLength);
            maxLength = Math.max(maxLength, givenLength);
        }
        generateRectangles(maxLength);
        while (!lengths.isEmpty()) {
            int count = getMaxRectangles(lengths.pollFirst());
            sb.append(count).append('\n');
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    private static void generateRectangles(int maxLength) {
        Set<String> checkedPairs = new HashSet<>();

        for (int m = 2; m < Math.sqrt(maxLength / 2.0); m++) {
            for (int n = 1; n < m; n++) {
                if ((m - n) % 2 == 1 && gcd(m, n) == 1) {
                    int a = m * m - n * n;
                    int b = 2 * m * n;
                    int min = Math.min(a, b);
                    int max = Math.max(a, b);
                    String key = min + ":" + max;
                    if (!checkedPairs.contains(key)) {
                        checkedPairs.add(key);
                        int perimeter = 2 * (min + max);
                        if (perimeter <= maxLength) {
                            perimeterMap.put(perimeter, perimeterMap.getOrDefault(perimeter, 0) + 1);
                        }
                    }
                }
            }
        }
    }

    private static int getMaxRectangles(int wireLength) {
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : perimeterMap.entrySet()) {
            if (wireLength >= entry.getKey()) {
                int key = entry.getKey();
                int value = entry.getValue();
                while (wireLength >= key && value > 0) {
                    wireLength -= entry.getKey();
                    count++;
                    value--;
                }
            } else {
                break;
            }
        }
        return count;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
