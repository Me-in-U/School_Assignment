package 문제해결기법.Assignment_03.tree;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class rbt {
    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("rbt.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_03\\1.inp"));
        StringBuilder sb = new StringBuilder();

        RedBlackTree3 tree = new RedBlackTree3();

        while (true) {
            String[] line = br.readLine().split(" ");
            char command = line[0].charAt(0);
            int key = Integer.parseInt(line[1]);
            System.out.println(Arrays.toString(line));
            if (key < 0)
                break;
            switch (command) {
                case 'i': // Insert operation
                    tree.insertNode(key);
                    break;
                case 'c': // Color check operation
                    Node node = tree.searchNode(key);
                    String color = node.color ? "Red" : "Black";
                    sb.append("color(").append(key).append(") : ").append(color).append('\n');
                    break;
                case 'd': // Delete operation
                    tree.deleteNode(key);
                    break;
                default:
            }
        }
        System.out.print(sb.toString());
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}
