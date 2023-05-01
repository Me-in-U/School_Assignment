class Node:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None


class BST:
    def __init__(self):
        self.root = None

    # 노트삽입
    def insert(self, key):
        new_node = Node(key)
        # root node가 없으면 바로 root에 바로 삽입
        if self.root is None:
            self.root = new_node
        # root node가 있으면 root 부터 key값을 비교하며 탐색 후 삽입
        else:
            node = self.root
            while True:
                if key < node.key:
                    if node.left is None:
                        node.left = new_node
                        break
                    else:
                        node = node.left
                else:  # key > node.key
                    if node.right is None:
                        node.right = new_node
                        break
                    else:
                        node = node.right

    def delete(self, key):
        # 수정된 root로 대체
        self.root = self.delete_node(self.root, key)

    def delete_node(self, node, key):
        # node 재구성
        # ★삭제할 노드가 포함된 subtree를 재구성☆
        # ★삭제할 노드가 없으면 subtree를 재구성이 필요없음☆
        if node is None:
            return None
        if node.key > key:
            node.left = self.delete_node(node.left, key)
            return node
        elif node.key < key:
            node.right = self.delete_node(node.right, key)
            return node
        # 삭제할 노드를 찾았을때 이 이후 다시 재구성
        elif node.key == key:
            # 삭제할 노드가 자식이 없다면 Node
            if (node.left is None) & (node.right is None):
                return None
            # 삭제할 노드의 left가 없다면, 삭제할노드 = 삭제할 노드의 right
            elif node.left is None:
                return node.right
            # 삭제할 노드의 right가 없다면, 삭제할노드 = 삭제할 노드의 left
            elif node.right is None:
                return node.left
            # ★삭제할 노드의 자식이 둘 다 있다면☆
            else:
                # 삭제할 노드의 오른쪽 subtree에서 가장 작은 key의 노드의 대체
                min_node = self.get_min(node.right)
                node.key = min_node.key
                # 삭제된 이후 min_node.key을 삭제할 숫자로 하고 다시 delete_node 재귀 시작
                node.right = self.delete_node(node.right, min_node.key)
                return node

    # node의 가장 왼쪽 node return
    def get_min(self, node):
        while node.left is not None:
            node = node.left
        return node

    # 경로 출력
    # "R" 출력이후 검색할 root부터 key를 계속 탐색
    def get_path(self, key):
        global last_print
        last_print += "R"
        node = self.root
        while node.key is not key:
            if key < node.key:
                last_print += "0"
                node = node.left
            elif key > node.key:
                last_print += "1"
                node = node.right
        last_print += "\n"


input_txt = open("bst_input.txt", 'r')
output_txt = open("bst_output.txt", 'w')
last_print = str()

# 첫째 줄은 Test Case의 개수 𝑡 를 나타낸다. 1 ≤ 𝑡 ≤ 10)
t = int(input_txt.readline())
for case in range(t):
    # tree 생성
    bst = BST()

    # 첫째 줄은 삽입할 키의 개수 𝑖 가 표기된다. (2 ≤ 𝑖 ≤ 50)
    i = int(input_txt.readline())

    # 둘째 줄은 삽입할 𝑖개의 키가 삽입 순서대로 스페이스로 구분되어 표기된다.
    keys = list(map(int, input_txt.readline().split()))
    for key in keys:
        bst.insert(key)

    # 셋째 줄은 검색할 키의 개수 𝑠1 가 표기된다. (1 ≤ 𝑠1 ≤ 𝑖)
    s1 = int(input_txt.readline())
    # 넷째 줄은 검색할 𝑠1개의 키가 검색 순서대로 스페이스로 구분되어 표기된다.
    search_list = list(map(int, input_txt.readline().split()))
    for key in search_list:
        bst.get_path(key)

    # 다섯째 줄은 삭제할 키의 개수 𝑑가 표기된다. (1 ≤ 𝑑 < 𝑖)
    d = int(input_txt.readline())
    # 여섯째 줄은 삭제할 𝑑개의 키가 삭제 순서대로 스페이스로 구분되어 표기된다
    delete_list = list(map(int, input_txt.readline().split()))
    for key in delete_list:
        bst.delete(key)

    # 일곱째 줄은 검색할 키의 개수 𝑠2 가 표기된다. (1 ≤ 𝑠2 ≤ 𝑖 − 𝑑)
    s2 = int(input_txt.readline())
    # 여덟째 줄은 검색할 𝑠2 개의 키가 검색 순서대로 스페이스로 구분되어 표기된다
    search_list = list(map(int, input_txt.readline().split()))
    for key in search_list:
        bst.get_path(key)

output_txt.write(last_print)
input_txt.close()
output_txt.close()
