package 문제해결기법.Assignment_03;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class rbt {
    public static class Node {
        long data;
        Node parent;
        Node left;
        Node right;
        boolean color;
    }

    public static class RedBlackTree {
        Node root;
        Node nullLeaf;

        RedBlackTree() {
            nullLeaf = new Node();
            nullLeaf.left = null;
            nullLeaf.right = null;
            nullLeaf.color = false;
            root = nullLeaf;
        }

        public void deleteNode(long key) {
            Node z = searchTree(key);
            Node x;
            Node y = z;

            boolean yOriginalColor = y.color;
            if (z.left == nullLeaf) {
                x = z.right;
                replaceNode(z, z.right);
            } else if (z.right == nullLeaf) {
                x = z.left;
                replaceNode(z, z.left);
            } else {
                y = successor(z);
                yOriginalColor = y.color;
                x = y.right;
                if (y.parent == z) {
                    x.parent = y;
                } else {
                    replaceNode(y, y.right);
                    y.right = z.right;
                    y.right.parent = y;
                }
                replaceNode(z, y);
                y.left = z.left;
                y.left.parent = y;
                y.color = z.color;
            }

            if (yOriginalColor == false)
                deleteFixup(x);
        }

        public void deleteFixup(Node x) {
            Node w;
            while (x != root && x.color == false) {
                if (x == x.parent.left) {
                    w = x.parent.right;
                    if (w.color == true) {
                        w.color = false;
                        x.parent.color = true;
                        leftRotate(x.parent);
                        w = x.parent.right;
                    }
                    if (w.left.color == false && w.right.color == false) {
                        w.color = true;
                        x = x.parent;
                    } else {
                        if (w.right.color == false) {
                            w.left.color = false;
                            w.color = true;
                            rightRotate(w);
                            w = x.parent.right;
                        }
                        w.color = x.parent.color;
                        x.parent.color = false;
                        w.right.color = false;
                        leftRotate(x.parent);
                        x = root;
                    }
                } else {
                    w = x.parent.left;
                    if (w.color == true) {
                        w.color = false;
                        x.parent.color = true;
                        rightRotate(x.parent);
                        w = x.parent.left;
                    }
                    if (w.right.color == false) {
                        w.color = true;
                        x = x.parent;
                    } else {
                        if (w.left.color == false) {
                            w.right.color = false;
                            w.color = true;
                            leftRotate(w);
                            w = x.parent.left;
                        }
                        w.color = x.parent.color;
                        x.parent.color = false;
                        w.left.color = false;
                        rightRotate(x.parent);
                        x = root;
                    }
                }
            }
            x.color = false;
        }

        public void replaceNode(Node u, Node v) {
            if (u.parent == null)
                root = v;
            else if (u == u.parent.left)
                u.parent.left = v;
            else
                u.parent.right = v;
            v.parent = u.parent;
        }

        public void insert(long key) {
            Node node = new Node();
            node.parent = null;
            node.data = key;
            node.left = nullLeaf;
            node.right = nullLeaf;
            node.color = true;

            Node y = null;
            Node x = this.root;

            while (x != nullLeaf) {
                y = x;
                x = (node.data < x.data) ? x.left : x.right;
            }

            node.parent = y;
            if (y == null) {
                root = node;
            } else if (node.data < y.data) {
                y.left = node;
            } else {
                y.right = node;
            }

            if (node.parent == null) {
                node.color = false;
                return;
            }

            if (node.parent.parent == null) {
                return;
            }

            insertFixup(node);
        }

        public void insertFixup(Node x) {
            Node y;
            while (x != root && x.parent.color == true) {
                if (x.parent == x.parent.parent.right) {
                    y = x.parent.parent.left;
                    if (y.color == true) {
                        x.parent.color = false;
                        y.color = false;
                        x.parent.parent.color = true;
                        x = x.parent.parent;
                    } else {
                        if (x == x.parent.left) {
                            x = x.parent;
                            rightRotate(x);
                        }
                        x.parent.color = false;
                        x.parent.parent.color = true;
                        leftRotate(x.parent.parent);
                    }
                } else {
                    y = x.parent.parent.right;
                    if (y.color == true) {
                        x.parent.color = false;
                        y.color = false;
                        x.parent.parent.color = true;
                        x = x.parent.parent;
                    } else {
                        if (x == x.parent.right) {
                            x = x.parent;
                            leftRotate(x);
                        }
                        x.parent.color = false;
                        x.parent.parent.color = true;
                        rightRotate(x.parent.parent);
                    }
                }
            }
            root.color = false;
        }

        public Node successor(Node x) {
            if (x.right != nullLeaf) {
                Node minNode = x.right;
                while (minNode.left != nullLeaf) {
                    minNode = minNode.left;
                }
                return minNode;
            }

            Node y = x.parent;
            while (y != nullLeaf && x == y.right) {
                x = y;
                y = y.parent;
            }
            return y;
        }

        public void leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != nullLeaf) {
                y.left.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                this.root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.left = x;
            x.parent = y;
        }

        public void rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != nullLeaf) {
                y.right.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                this.root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
        }

        public Node searchTree(long key) {
            Node returnNode = this.root;
            while (returnNode != nullLeaf) {
                if (key < returnNode.data) {
                    returnNode = returnNode.left;
                } else if (returnNode.data < key) {
                    returnNode = returnNode.right;
                } else {
                    return returnNode;
                }
            }
            return nullLeaf;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("rbt.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_03\\2.inp"));
        StringBuilder sb = new StringBuilder();

        RedBlackTree bst = new RedBlackTree();
        String line;
        while ((line = br.readLine()) != null) {
            String[] commandKey = line.split(" ");
            char command = commandKey[0].charAt(0);
            long key = Long.parseLong(commandKey[1]);
            if (key < 0)
                break;
            if (command == 'i') {
                bst.insert(key);
            } else if (command == 'c') {
                String color = bst.searchTree(key).color ? "RED" : "BLACK";
                sb.append("color(").append(key).append("): ").append(color)
                        .append('\n');
            } else if (command == 'd') {
                bst.deleteNode(key);
            }
        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}