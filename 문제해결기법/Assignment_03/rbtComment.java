import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class rbtComment {
    enum COLOR {
        RED,
        BLACK;
    }

    class Node {
        int data;
        Node parent;
        Node left;
        Node right;
        COLOR color;
    }

    private Node root;
    private Node nullLeaf;

    public rbtComment() {
        nullLeaf = new Node();
        nullLeaf.left = null;
        nullLeaf.right = null;
        nullLeaf.color = COLOR.BLACK;
        root = nullLeaf;
    }

    // !삭제
    public void deleteNode(int key) {
        // ! 조건 : 삭제하려는 키는 항상 존재
        Node z = searchTree(key);
        Node x;
        Node y = z;

        COLOR yOriginalColor = y.color;
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

        if (yOriginalColor == COLOR.BLACK)
            deleteFixup(x);
    }

    private void deleteFixup(Node x) {
        Node w; // x 의 형제노드
        while (x != root && x.color == COLOR.BLACK) {
            if (x == x.parent.left) {
                w = x.parent.right;
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
                if (w.color == COLOR.RED) {
                    w.color = COLOR.BLACK;
                    x.parent.color = COLOR.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == COLOR.BLACK) {
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

    private void replaceNode(Node u, Node v) {
        if (u.parent == null)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;
        v.parent = u.parent;
    }

    // ! 삽입
    public void insert(int key) {
        Node node = new Node();
        node.parent = null;
        node.data = key;
        node.left = nullLeaf;
        node.right = nullLeaf;
        node.color = COLOR.RED; // !새로운 노드는 빨강

        Node y = null;
        Node x = this.root;

        while (x != nullLeaf) {
            y = x;
            x = (node.data < x.data) ? x.left : x.right;
        }

        // y는 x의 부모
        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.left = node;
        } else {
            y.right = node;
        }

        // 새 노드가 루트면 검은색으로 바꾸고 리턴
        if (node.parent == null) {
            node.color = COLOR.BLACK;
            return;
        }

        // grandparent가 null이면 그냥 리턴
        if (node.parent.parent == null) {
            return;
        }

        insertFixup(node);
    }

    private void insertFixup(Node x) {
        Node y; // 삼촌 노드
        while (x != root && x.parent.color == COLOR.RED) {
            if (x.parent == x.parent.parent.right) {
                y = x.parent.parent.left; // 왼쪽 삼촌
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
                y = x.parent.parent.right; // 오른쪽 삼촌
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
        root.color = COLOR.BLACK;
    }

    public Node successor(Node x) {
        // Case 1
        // 오른쪽 subtree가 null이 아니면
        // 오른쪽 subtree의 최솟값 노드
        if (x.right != nullLeaf) {
            Node minNode = x.right;
            while (minNode.left != nullLeaf) {
                minNode = minNode.left;
            }
            return minNode;
        }

        // Case 2
        // x의 가장 가까운 조상노드(ancestor) 중에서
        // 그 조상 노드의 left child 가 또한 x의 조상노드인 경우이다.
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

    // ! 검색
    public Node searchTree(int key) {
        Node returnNode = this.root;
        while (returnNode.data != key) {
            if (key < returnNode.data) {
                returnNode = returnNode.left;
            } else if (returnNode.data < key) {
                returnNode = returnNode.right;
            }
        }
        return returnNode;
    }

    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("rbt.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_03\\1.inp"));
        StringBuilder sb = new StringBuilder();

        rbtComment bst = new rbtComment();
        while (true) {
            String[] line = br.readLine().split(" ");
            char command = line[0].charAt(0);
            int key = Integer.parseInt(line[1]);
            if (key < 0)
                break;
            switch (command) {
                case 'i': // 삽입
                    bst.insert(key);
                    break;
                case 'c': // 색 확인
                    sb.append("color(").append(key).append("): ").append(bst.searchTree(key).color).append('\n');
                    break;
                case 'd': // 삭제
                    bst.deleteNode(key);
                    break;
                default:
            }
        }
        bs.write(sb.toString().getBytes());
        bs.close();
        br.close();
    }
}