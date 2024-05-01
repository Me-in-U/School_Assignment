package 문제해결기법.Assignment_23;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class rectangles {
    static class RectangleEvent implements Comparable<RectangleEvent> {
        int x;
        int yUp;
        int yDown;
        int chk;

        public RectangleEvent(int x, int yUp, int yDown, int chk) {
            this.x = x;
            this.yUp = yUp;
            this.yDown = yDown;
            this.chk = chk;
        }

        @Override
        public int compareTo(RectangleEvent o) {
            if (this.x == o.x)
                return this.chk - o.chk;
            return this.x - o.x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("rectangles.out"));
        BufferedReader br = new BufferedReader(new FileReader("rectangles.inp"));
        StringTokenizer st = null;
        int n = Integer.parseInt(br.readLine());
        List<RectangleEvent> events = new ArrayList<>();
        int[] sweep = new int[20001];
        while (n-- > 0) {
            st = new StringTokenizer(br.readLine());
            int xStart = Integer.parseInt(st.nextToken()) + 10000;
            int yUp = Integer.parseInt(st.nextToken()) + 10000;
            int yStart = Integer.parseInt(st.nextToken()) + 10000;
            int yDown = Integer.parseInt(st.nextToken()) + 10000;
            events.add(new RectangleEvent(xStart, yUp, yDown, 1));
            events.add(new RectangleEvent(yStart, yUp, yDown, -1));
        }
        Collections.sort(events);
        int area = 0;
        int x = 0;
        for (RectangleEvent event : events) {
            int count = 0;
            for (int i = 0; i < 20001; i++)
                if (sweep[i] > 0)
                    count++;
            area += count * (event.x - x);
            for (int i = event.yUp; i < event.yDown; i++) {
                if (event.chk == 1)
                    sweep[i]++;
                else
                    sweep[i]--;
            }
            x = event.x;
        }
        bw.write(String.valueOf(area));
        bw.close();
        br.close();
    }
}