package 문제해결기법.Assignment_35;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class path {
    public static class Node {
        int val;
        Node left;
        Node right;

        Node(int x) {
            val = x;
        }
    }

    protected static int preIndex;

    public static Node buildTree(int[] preOrder, int[] nodeValues, int inStart, int inEnd) {
        if (inStart > inEnd) {
            return null;
        }
        int rootValueIndex = preOrder[preIndex++];
        Node root = new Node(nodeValues[rootValueIndex]);
        if (inStart == inEnd) {
            return root;
        }
        root.left = buildTree(preOrder, nodeValues, inStart, rootValueIndex - 1);
        root.right = buildTree(preOrder, nodeValues, rootValueIndex + 1, inEnd);
        return root;
    }

    public static int findLTLMax(Node node) {
        if (node == null) {
            return Integer.MIN_VALUE;
        }
        if (node.left == null && node.right == null) {
            if (node.val > maxSum)
                maxSum = node.val;
            return node.val;
        }
        int left = findLTLMax(node.left);
        int right = findLTLMax(node.right);
        if (node.left != null && node.right != null) {
            int currentSum = left + right + node.val;
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }
        return (left > right) ? left + node.val
                : right + node.val;
    }

    protected static int maxSum;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_35\\path.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_35\\0.inp"));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int nodeCount = Integer.parseInt(br.readLine());
            int[] nodeValues = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] preOrder = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            Node root = buildTree(preOrder, nodeValues, 0, nodeCount - 1);
            preIndex = 0;
            maxSum = Integer.MIN_VALUE;
            findLTLMax(root);
            sb.append(maxSum).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }
}
