package 문제해결기법;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder temp = new StringBuilder();
        int n = 12300;
        temp.append(String.valueOf(n)).reverse();
        n = Math.abs(Integer.parseInt(temp.toString()));
        System.out.println(n);
    }
}
