def heap_sort():
    def down_heap(i, heap_size):
        left_child = 2 * i + 1  # i의 왼쪽 자식 노드
        right_child = 2 * i + 2  # i의 오른쪽 자식 노드
        bigger = i
        if left_child < heap_size and arr[left_child] > arr[bigger]:
            bigger = left_child
        if right_child < heap_size and arr[right_child] > arr[bigger]:
            bigger = right_child
        if bigger != i:
            arr[i], arr[bigger] = arr[bigger], arr[i]
            down_heap(bigger, heap_size)

    # input.txt 읽기
    with open("input.txt", 'r') as file:
        arr = [int(line.strip()) for line in file]

    # A의 숫자에 대해서 최대 힙을 구성
    n = len(arr)  # 힙의 크기를 조절하는 변수

    # BuildHeap()
    for i in range(n // 2 - 1, -1, -1):
        down_heap(i, n)

    # sort 진행
    for i in range(n - 1, 0, -1):
        arr[0], arr[i] = arr[i], arr[0]  # 루트와 힙의 마지막 노드 교환
        down_heap(0, i)

    # output.txt로
    with open("output.txt", 'w') as file:
        for number in arr:
            file.write(f"{number}\n")


heap_sort()
