package 문제해결기법.Assignment_03;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

enum COLOR {
    RED, BLACK
}

public class rbt {
    public static class Node {
        int val;
        COLOR color;
        Node left;
        Node right;
        Node parent;

        Node(int val) {
            this.val = val;
            parent = left = right = null;

            // Node is created during insertion
            // Node is red at insertion
            color = COLOR.RED;
        }

        // returns pointer to uncle
        Node uncle() {

            // If no parent or grandparent, then no uncle
            if (parent == null || parent.parent == null)
                return null;

            if (parent.isOnLeft())
                // uncle on right
                return parent.parent.right;
            else
                // uncle on left
                return parent.parent.left;
        }

        // check if node is left child of parent
        boolean isOnLeft() {
            return this == parent.left;
        }

        // returns pointer to sibling
        Node sibling() {

            // sibling null if no parent
            if (parent == null)
                return null;

            if (isOnLeft())
                return parent.right;

            return parent.left;
        }

        // moves node down and moves given node in its place
        void moveDown(Node nParent) {
            if (parent != null) {
                if (isOnLeft())
                    parent.left = nParent;
                else
                    parent.right = nParent;
            }
            nParent.parent = parent;
            parent = nParent;
        }

        boolean hasRedChild() {
            return (left != null && left.color == COLOR.RED) ||
                    (right != null && right.color == COLOR.RED);
        }
    }

    public static class RBTree {
        Node root;

        // left rotates the given node
        void leftRotate(Node x) {
            // new parent will be node's right child
            Node nParent = x.right;

            // update root if currentent node is root
            if (x == root)
                root = nParent;

            x.moveDown(nParent);

            // connect x with new parent's left element
            x.right = nParent.left;

            // connect new parent's left element with node
            // if it is not null
            if (nParent.left != null)
                nParent.left.parent = x;

            // connect new parent with x
            nParent.left = x;
        }

        void rightRotate(Node x) {

            // new parent will be node's left child
            Node nParent = x.left;

            // update root if currentent node is root
            if (x == root)
                root = nParent;

            x.moveDown(nParent);

            // connect x with new parent's right element
            x.left = nParent.right;

            // connect new parent's right element with node
            // if it is not null
            if (nParent.right != null)
                nParent.right.parent = x;

            // connect new parent with x
            nParent.right = x;
        }

        void swapColors(Node x1, Node x2) {
            COLOR temp = x1.color;
            x1.color = x2.color;
            x2.color = temp;
        }

        void swapValues(Node u, Node v) {
            int temp = u.val;
            u.val = v.val;
            v.val = temp;
        }

        // fix red red at given node
        void fixRedRed(Node x) {

            // if x is root color it black and return
            if (x == root) {
                x.color = COLOR.BLACK;
                return;
            }

            // initialize parent, grandparent, uncle
            Node parent = x.parent;
            Node grandparent = parent.parent;
            Node uncle = x.uncle();

            if (parent.color != COLOR.BLACK) {
                if (uncle != null && uncle.color == COLOR.RED) {

                    // uncle red, perform recoloring and recurse
                    parent.color = COLOR.BLACK;
                    uncle.color = COLOR.BLACK;
                    grandparent.color = COLOR.RED;
                    fixRedRed(grandparent);
                } else {
                    // Else perform LR, LL, RL, RR
                    if (parent.isOnLeft()) {
                        if (x.isOnLeft())
                            // for left right
                            swapColors(parent, grandparent);
                        else {
                            leftRotate(parent);
                            swapColors(x, grandparent);
                        }
                        // for left left and left right
                        rightRotate(grandparent);
                    } else {
                        if (x.isOnLeft()) {
                            // for right left
                            rightRotate(parent);
                            swapColors(x, grandparent);
                        } else
                            swapColors(parent, grandparent);

                        // for right right and right left
                        leftRotate(grandparent);
                    }
                }
            }
        }

        // find node that do not have a left child
        // in the subtree of the given node
        Node successor(Node x) {
            Node temp = x;
            while (temp.left != null)
                temp = temp.left;
            return temp;
        }

        // find node that replaces a deleted node in BST
        Node BSTreplace(Node x) {
            // when node have 2 children
            if (x.left != null && x.right != null)
                return successor(x.right);

            // when leaf
            if (x.left == null && x.right == null)
                return null;

            // when single child
            if (x.left != null)
                return x.left;
            else
                return x.right;
        }

        // deletes the given node
        void deleteNode(Node v) {
            Node u = BSTreplace(v);
            // True when u and v are both black
            boolean uvBlack = ((u == null || u.color == COLOR.BLACK) && (v.color == COLOR.BLACK));
            Node parent = v.parent;

            if (u == null) {
                // u is NULL therefore v is leaf
                if (v == root)
                    // v is root, making root null
                    root = null;
                else {
                    if (uvBlack)
                        // u and v both black
                        // v is leaf, fix double black at v
                        fixDoubleBlack(v);

                    // u or v is red
                    else if (v.sibling() != null)
                        // sibling is not null, make it red
                        v.sibling().color = COLOR.RED;

                    // delete v from the tree
                    if (v.isOnLeft())
                        parent.left = null;
                    else
                        parent.right = null;
                }
                return;
            }

            if (v.left == null || v.right == null) {
                // v has 1 child
                if (v == root) {
                    // v is root, assign the value of u to v, and delete u
                    v.val = u.val;
                    v.left = v.right = null;
                    // delete u;
                } else {
                    // Detach v from tree and move u up
                    if (v.isOnLeft())
                        parent.left = u;
                    else
                        parent.right = u;

                    u.parent = parent;

                    if (uvBlack)
                        // u and v both black, fix double black at u
                        fixDoubleBlack(u);
                    else
                        // u or v red, color u black
                        u.color = COLOR.BLACK;
                }
                return;
            }

            // v has 2 children, swap values with successor and recurse
            swapValues(u, v);
            deleteNode(u);
        }

        void fixDoubleBlack(Node x) {
            // Reached root
            if (x == root)
                return;

            Node sibling = x.sibling();
            Node parent = x.parent;

            if (sibling == null)
                // No sibling, double black pushed up
                fixDoubleBlack(parent);
            else {
                if (sibling.color == COLOR.RED) {
                    // sibling red
                    parent.color = COLOR.RED;
                    sibling.color = COLOR.BLACK;

                    if (sibling.isOnLeft())
                        // right case
                        rightRotate(parent);
                    else
                        // right case
                        leftRotate(parent);

                    fixDoubleBlack(x);
                } else {
                    // Sibling black
                    if (sibling.hasRedChild()) {
                        // at least 1 red children
                        if (sibling.left != null && sibling.left.color == COLOR.RED) {
                            if (sibling.isOnLeft()) {
                                // left left
                                sibling.left.color = sibling.color;
                                sibling.color = parent.color;
                                rightRotate(parent);
                            } else {
                                // right right
                                sibling.left.color = parent.color;
                                rightRotate(sibling);
                                leftRotate(parent);
                            }
                        } else {
                            if (sibling.isOnLeft()) {
                                // left right
                                sibling.right.color = parent.color;
                                leftRotate(sibling);
                                rightRotate(parent);
                            } else {
                                // right right
                                sibling.right.color = sibling.color;
                                sibling.color = parent.color;
                                leftRotate(parent);
                            }
                        }
                        parent.color = COLOR.BLACK;
                    } else {
                        // 2 black children
                        sibling.color = COLOR.RED;
                        if (parent.color == COLOR.BLACK)
                            fixDoubleBlack(parent);
                        else
                            parent.color = COLOR.BLACK;
                    }
                }
            }
        }

        // prints level order for given node
        void levelOrder(Node x) {
            if (x == null)
                return;

            // queue for level order
            Queue<Node> q = new LinkedList<>();
            Node current;

            q.add(x);

            while (!q.isEmpty()) {
                current = q.poll();
                // print node value
                System.out.print(current.val + "(" + current.color + ") ");

                // push children to queue
                if (current.left != null)
                    q.add(current.left);
                if (current.right != null)
                    q.add(current.right);
            }
        }

        // prints inorder recursively
        void inorder(Node x) {
            if (x == null)
                return;

            inorder(x.left);
            System.out.print(x.val + "(" + x.color + ") ");
            inorder(x.right);
        }

        // constructor
        // initialize root
        RBTree() {
            root = null;
        }

        Node getRoot() {
            return root;
        }

        // searches for given value
        // if found returns the node (used for delete)
        // else returns the last node while traversing (used in insert)
        Node search(int n) {
            Node temp = root;
            while (temp != null) {
                if (n < temp.val) {
                    if (temp.left == null)
                        break;
                    else
                        temp = temp.left;
                } else if (n == temp.val) {
                    break;
                } else {
                    if (temp.right == null)
                        break;
                    else
                        temp = temp.right;
                }
            }

            return temp;
        }

        // inserts the given value to tree
        void insert(int n) {
            Node newNode = new Node(n);
            if (root == null) {
                // when root is null
                // simply insert value at root
                newNode.color = COLOR.BLACK;
                root = newNode;
            } else {
                Node temp = search(n);

                // return if value already exists
                if (temp.val == n)
                    return;

                // if value is not found, search returns the node
                // where the value is to be inserted

                // connect new node to correct node
                newNode.parent = temp;

                if (n < temp.val)
                    temp.left = newNode;
                else
                    temp.right = newNode;

                // fix red red violation if exists
                fixRedRed(newNode);
            }
        }

        // utility function that deletes the node with given value
        void deleteByVal(int n) {
            if (root == null)
                return;

            Node v = search(n), u;

            if (v.val != n) {
                System.out.println("No node found to delete with value: " + n);
                return;
            }

            deleteNode(v);
        }

        // prints inorder of the tree
        void printInOrder() {
            System.out.print("Inorder: ");
            if (root == null)
                System.out.println("Tree is empty");
            else
                inorder(root);
            System.out.println();
        }

        // prints level order of the tree
        void printLevelOrder() {
            System.out.print("Level order: ");
            if (root == null)
                System.out.println("Tree is empty");
            else
                levelOrder(root);
            System.out.println();
        }

        // RBTree 클래스 내에 추가
        void printTree(Node root, String prefix, boolean isLeft) {
            if (root != null) {
                System.out.println(prefix + (isLeft ? "|-- " : "\\-- ") + root.val + "(" + root.color + ")");
                // 재귀적으로 왼쪽 자식을 먼저 출력
                printTree(root.left, prefix + (isLeft ? "|   " : "    "), true);
                // 재귀적으로 오른쪽 자식을 출력
                printTree(root.right, prefix + (isLeft ? "|   " : "    "), false);
            }
        }

        // 트리 전체를 출력하기 위한 메서드
        void printTree() {
            printTree(this.root, "", true);
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("rbt.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_03\\0.inp"));
        StringBuilder sb = new StringBuilder();

        RBTree tree = new RBTree();

        while (true) {
            String[] line = br.readLine().split(" ");
            char command = line[0].charAt(0);
            int key = Integer.parseInt(line[1]);
            System.out.println(Arrays.toString(line));
            if (key < 0)
                break;
            switch (command) {
                case 'i': // Insert operation
                    tree.insert(key);
                    // tree.printInOrder();
                    // tree.printLevelOrder();
                    tree.printTree();
                    break;
                case 'c': // Color check operation
                    Node node = tree.search(key);
                    COLOR color = node.color;
                    sb.append("color(").append(key).append(") : ").append(color).append('\n');
                    break;
                case 'd': // Delete operation
                    tree.deleteByVal(key);
                    // tree.printInOrder();
                    // tree.printLevelOrder();
                    tree.printTree();
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
