package 문제해결기법.Assignment_26;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class service {

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_26\\service.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_26\\1.inp"));
        StringBuilder sb = new StringBuilder();

        bw.write(sb.toString());
        bw.close();
        br.close();
    }

}