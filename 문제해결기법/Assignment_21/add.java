package 문제해결기법.Assignment_21;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class add {
    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("add.out"));
        BufferedReader br = new BufferedReader(new FileReader("add.inp"));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        while (Integer.parseInt(br.readLine()) != 0) {
            long totalCost = 0;
            st = new StringTokenizer(br.readLine());
            PriorityQueue<Long> pq = new PriorityQueue<>();
            while (st.hasMoreTokens()) {
                pq.add(Long.parseLong(st.nextToken()));
            }
            while (pq.size() != 1) {
                long cost = pq.poll() + pq.poll();
                totalCost += cost;
                pq.add(cost);
            }
            sb.append(totalCost).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}