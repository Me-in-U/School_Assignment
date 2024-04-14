package 문제해결기법.Assignment_15;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class contour {
    public static class Rectangle {
        int x;
        int h;

        Rectangle(int x, int h) {
            this.x = x;
            this.h = h;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("contour.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_15\\2.inp"));
        String input = "";
        StringTokenizer st = null;
        StringBuilder sb = new StringBuilder();
        int testCase = 1;
        while ((input = br.readLine()) != null) {
            StringBuilder temp = new StringBuilder();
            PriorityQueue<Rectangle> pq = new PriorityQueue<>((o1, o2) -> o1.x == o2.x ? o2.h - o1.h : o1.x - o2.x);
            TreeMap<Integer, Integer> tm = new TreeMap<>((o1, o2) -> o2 - o1);
            while (true) {
                while (input != null && input.length() < 5)
                    input = br.readLine();
                if (input == null)
                    break;
                st = new StringTokenizer(input);
                int left_x = Integer.parseInt(st.nextToken());
                int height = Integer.parseInt(st.nextToken());
                int right_x = Integer.parseInt(st.nextToken());
                if (left_x == 0 && height == 0 && right_x == 0) {
                    break;
                }
                pq.add(new Rectangle(left_x, height));
                pq.add(new Rectangle(right_x, -height));
                input = br.readLine();
            }
            if (input == null)
                break;
            if (!pq.isEmpty()) {
                int maxX = -1;
                int maxH = 0;
                long volume = 0;
                tm.put(0, 1);
                while (!pq.isEmpty()) {
                    Rectangle rectangle = pq.poll();
                    if (rectangle.h > 0) {
                        tm.put(rectangle.h, tm.getOrDefault(rectangle.h, 0) + 1);
                    } else {
                        int value = tm.get(-rectangle.h);
                        if (value == 1) {
                            tm.remove(-rectangle.h);
                        } else {
                            tm.replace(-rectangle.h, value - 1);
                        }
                    }
                    int height = tm.firstKey();
                    if (maxX != rectangle.x && maxH != height) {
                        volume += (long) (rectangle.x - maxX) * maxH;
                        maxX = rectangle.x;
                        maxH = height;
                        if (maxH == 0) {
                            temp.append(volume).append(' ');
                            volume = 0;
                        }
                    }
                }
            }
            sb.append("Test Case #").append(testCase++).append(" : ").append(temp).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}