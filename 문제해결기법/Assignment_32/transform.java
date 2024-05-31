package 문제해결기법.Assignment_35;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Deque;
import java.util.ArrayDeque;

public class transform {
    public static class Node {
        int val;
        Node left;
        Node right;
        boolean leftDone = false;

        Node(int val) {
            this.val = val;
        }
    }

    public static class Tree {
        private int[] inorder;
        private int[] inorderIndexMap;
        private int preorderIndex;

        Tree() {
        }

        public Node buildTreeFromInorder(int[] inorder) {
            this.inorder = inorder;
            this.inorderIndexMap = new int[inorder.length + 1];
            this.preorderIndex = 0;
            for (int i = 0; i < inorder.length; i++)
                inorderIndexMap[inorder[i]] = i;
            return buildSubTree(0, inorder.length - 1);
        }

        private Node buildSubTree(int left, int right) {
            if (left > right)
                return null;
            int rootValue = preorderIndex + 1;
            Node root = new Node(rootValue);
            preorderIndex++;
            int inorderIndex = inorderIndexMap[rootValue];
            root.left = buildSubTree(left, inorderIndex - 1);
            root.right = buildSubTree(inorderIndex + 1, right);
            return root;
        }

        public Node buildTreeFromBracket(String s) {
            Deque<Node> stack = new ArrayDeque<>();
            Node root = null;
            Node current = null;
            int val = 1;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') {
                    Node newNode = new Node(val++);
                    if (current != null) {
                        if (!current.leftDone) {
                            current.left = newNode;
                        } else {
                            current.right = newNode;
                        }
                        stack.push(newNode);
                    }
                    current = newNode;
                    if (root == null) {
                        root = current;
                        stack.push(root);
                    }
                } else if (c == ')') {
                    if (!stack.isEmpty()) {
                        current = stack.pop();
                        current.leftDone = true;
                    }
                }
            }
            return root;
        }

        public void printBracket(Node root, StringBuilder sb) {
            if (root == null)
                return;
            sb.append("(");
            if (root.left != null)
                printBracket(root.left, sb);
            sb.append(")");
            if (root.right != null)
                printBracket(root.right, sb);
        }

        public void printInorder(Node root, StringBuilder sb) {
            if (root == null)
                return;
            printInorder(root.left, sb);
            sb.append(root.val).append(" ");
            printInorder(root.right, sb);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_32\\transform.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_32\\1.inp"));
        StringBuilder finalSb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());
        for (int t = 0; t < T; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            Tree tree = new Tree();
            StringBuilder tempSb = new StringBuilder();
            if (k == 0) {
                int[] inorder = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                Node root = tree.buildTreeFromInorder(inorder);
                tree.printBracket(root, tempSb);
                finalSb.append(n).append(' ').append(tempSb.toString()).append('\n');
            } else {
                String bracket = br.readLine();
                Node root = tree.buildTreeFromBracket(bracket);
                tree.printInorder(root, tempSb);
                finalSb.append(n).append(' ').append(tempSb.toString()).append('\n');
            }
        }
        bw.write(finalSb.toString());
        bw.close();
        br.close();
    }
}
