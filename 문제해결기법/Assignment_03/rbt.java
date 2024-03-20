package 문제해결기법.Assignment_03;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class rbt {
    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("trip.out"));
        BufferedReader br = new BufferedReader(new FileReader("trip.inp"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {

        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
