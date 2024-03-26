import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class rbt {
    enum COLOR {
        RED,
        BLACK;
    }

    public static class Node {
        long data;
        Node parent;
        Node left;
        Node right;
        COLOR color;
    }

    public static class RedBlackTree {
        Node root;
        Node nullLeaf;

        RedBlackTree() {
            nullLeaf = new Node();
            nullLeaf.left = null;
            nullLeaf.right = null;
            nullLeaf.color = COLOR.BLACK;
            root = nullLeaf;
        }

        public void deleteNode(long key) {
            Node z = searchTree(key);
            if (z == nullLeaf) {
                return;
            }

            Node x = z;
            Node y = z;
            COLOR yOriginalColor = y.color;

            if (z.left == nullLeaf) {
                x = z.right;
                replaceNode(y, z.right);
            } else if (z.right == nullLeaf) {
                x = z.left;
                replaceNode(y, z.left);
            } else {
                y = minimum(z.right);
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

            if (yOriginalColor == COLOR.BLACK)
                deleteFixup(x);
        }

        private void deleteFixup(Node x) {
            Node w;
            while (x != root && x.color == COLOR.BLACK) {
                if (x == x.parent.left) {
                    w = x.parent.right;
                    if (w == nullLeaf) {
                        break;
                    }
                    if (w.color == COLOR.RED) {
                        w.color = COLOR.BLACK;
                        x.parent.color = COLOR.RED;
                        leftRotate(x.parent);
                        w = x.parent.right;
                    }
                    if (w.left.color == COLOR.BLACK && w.right.color == COLOR.BLACK) {
                        w.color = COLOR.RED;
                        x = x.parent;
                    } else {
                        if (w.right.color == COLOR.BLACK) {
                            w.left.color = COLOR.BLACK;
                            w.color = COLOR.RED;
                            rightRotate(w);
                            w = x.parent.right;
                        }
                        w.color = x.parent.color;
                        x.parent.color = COLOR.BLACK;
                        w.right.color = COLOR.BLACK;
                        leftRotate(x.parent);
                        x = root;
                    }
                } else {
                    w = x.parent.left;
                    if (w == nullLeaf) {
                        break;
                    }
                    if (w.color == COLOR.RED) {
                        w.color = COLOR.BLACK;
                        x.parent.color = COLOR.RED;
                        rightRotate(x.parent);
                        w = x.parent.left;
                    }
                    if (w.right.color == COLOR.BLACK && w.left.color == COLOR.BLACK) {
                        w.color = COLOR.RED;
                        x = x.parent;
                    } else {
                        if (w.left.color == COLOR.BLACK) {
                            w.right.color = COLOR.BLACK;
                            w.color = COLOR.RED;
                            leftRotate(w);
                            w = x.parent.left;
                        }
                        w.color = x.parent.color;
                        x.parent.color = COLOR.BLACK;
                        w.left.color = COLOR.BLACK;
                        rightRotate(x.parent);
                        x = root;
                    }
                }
            }
            x.color = COLOR.BLACK;
        }

        public void replaceNode(Node u, Node v) {
            if (u.parent == null)
                this.root = v;
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
            node.color = COLOR.RED;

            Node y = null;
            Node x = this.root;

            while (x != nullLeaf) {
                y = x;
                x = (node.data < x.data) ? x.left : x.right;
            }

            node.parent = y;
            if (y == null) {
                this.root = node;
            } else if (node.data < y.data) {
                y.left = node;
            } else {
                y.right = node;
            }

            if (node.parent == null) {
                node.color = COLOR.BLACK;
                return;
            }

            if (node.parent.parent == null) {
                return;
            }

            insertFixup(node);
        }

        public void insertFixup(Node x) {
            Node y;
            while (x != this.root && x.parent.color == COLOR.RED) {
                if (x.parent == x.parent.parent.right) {
                    y = x.parent.parent.left;
                    if (y.color == COLOR.RED) {
                        x.parent.color = COLOR.BLACK;
                        y.color = COLOR.BLACK;
                        x.parent.parent.color = COLOR.RED;
                        x = x.parent.parent;
                    } else {
                        if (x == x.parent.left) {
                            x = x.parent;
                            rightRotate(x);
                        }
                        x.parent.color = COLOR.BLACK;
                        x.parent.parent.color = COLOR.RED;
                        leftRotate(x.parent.parent);
                    }
                } else {
                    y = x.parent.parent.right;
                    if (y.color == COLOR.RED) {
                        x.parent.color = COLOR.BLACK;
                        y.color = COLOR.BLACK;
                        x.parent.parent.color = COLOR.RED;
                        x = x.parent.parent;
                    } else {
                        if (x == x.parent.right) {
                            x = x.parent;
                            leftRotate(x);
                        }
                        x.parent.color = COLOR.BLACK;
                        x.parent.parent.color = COLOR.RED;
                        rightRotate(x.parent.parent);
                    }
                }
            }
            this.root.color = COLOR.BLACK;
        }

        public Node successor(Node x) {
            if (x.right != nullLeaf) {
                return minimum(x.right);
            }

            Node y = x.parent;
            while (y != nullLeaf && x == y.right) {
                x = y;
                y = y.parent;
            }
            return y;
        }

        public Node minimum(Node node) {
            while (node.left != nullLeaf) {
                node = node.left;
            }
            return node;
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
        BufferedWriter bw = new BufferedWriter(new FileWriter("rbt.out"));
        BufferedReader br = new BufferedReader(new FileReader("rbt.inp"));
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
                COLOR color = bst.searchTree(key).color;
                sb.append("color(").append(key).append("): ").append(color)
                        .append('\n');
            } else if (command == 'd') {
                bst.deleteNode(key);
            }
        }
        bw.write(sb.toString().trim());
        bw.flush();
        bw.close();
        br.close();
    }
}