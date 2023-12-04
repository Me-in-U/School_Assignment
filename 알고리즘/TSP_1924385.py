import math


# !두 점 거리
def euclidean_distance(p1, p2):
    return math.sqrt((p1[0] - p2[0])**2 + (p1[1] - p2[1])**2)


# !기존 Kruskal algorithm
def kruskal(graph):
    def find_parent(vertex):
        if parent[vertex] == vertex:
            return vertex
        parent[vertex] = find_parent(parent[vertex])
        return parent[vertex]

    def union(x, y):
        root_x, root_y = find_parent(x), find_parent(y)
        if rank[root_x] < rank[root_y]:
            parent[root_x] = root_y
        elif rank[root_x] > rank[root_y]:
            parent[root_y] = root_x
        else:
            parent[root_y] = root_x
            rank[root_x] += 1

    L = sorted(graph, key=lambda x: x[2])
    T = []

    vertices = {vertex for edge in graph for vertex in edge[:2]}
    parent = {vertex: vertex for vertex in vertices}
    rank = {vertex: 0 for vertex in vertices}
    mst_cost = 0

    while len(T) < len(parent) - 1:
        u, v, w = L.pop(0)
        root_u, root_v = find_parent(u), find_parent(v)
        if root_u != root_v:
            T.append((ord(u) - ord('a'), ord(v) - ord('a'), w))
            union(root_u, root_v)
            mst_cost += w

    return mst_cost, T


# !DFS
def dfs_path(graph, start, visited=None, path=None):
    if visited is None:
        visited = set()
    if path is None:
        path = []
    path.append(start)
    visited.add(start)
    for neighbor in graph[start]:
        if neighbor not in visited:
            dfs_path(graph, neighbor, visited, path)
    return path


# !BFS
def bfs_path(graph, start):
    visited = set([start])
    queue = [start]
    path = []

    while queue:
        vertex = queue.pop(0)
        path.append(vertex)

        for neighbor in graph[vertex]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

    path.append(start)  # Return to start
    return path


# !거리계산
def calc_distance(path, points_dict):
    total_distance = 0
    for i in range(len(path) - 1):
        total_distance += euclidean_distance(
            points_dict[path[i]], points_dict[path[i + 1]])
    return total_distance


# !Input
points = [
    ('A', 0, 3),
    ('B', 7, 5),
    ('C', 6, 0),
    ('D', 4, 3),
    ('E', 1, 0),
    ('F', 5, 3),
    ('H', 4, 1),
    ('G', 2, 2)
]

# !초기 point의 좌표값으로 거리 가중치 그래프 생성
graph = []
for i in range(len(points)):
    for j in range(i + 1, len(points)):
        distance = euclidean_distance(points[i][1:], points[j][1:])
        graph.append((points[i][0], points[j][0], distance))

# !Kruskal algorithm 실행
mst_cost, mst_edges = kruskal(graph)
print("MST Cost : {:.2f}".format(mst_cost))

# !Kruskal algorithm 결과로 mst 그래프 생성
mst_graph = {}
for u, v, w in mst_edges:
    u_char = chr(u + ord('a'))
    v_char = chr(v + ord('a'))
    if u_char not in mst_graph:
        mst_graph[u_char] = []
    if v_char not in mst_graph:
        mst_graph[v_char] = []
    mst_graph[u_char].append(v_char)
    mst_graph[v_char].append(u_char)
print(mst_graph)
print()

points_dict = {point[0]: point[1:] for point in points}

# !TSP DFS, BFS 실행
tsp_dfs_path = dfs_path(mst_graph, 'A')
tsp_dfs_path.append('A')
tsp_bfs_path = bfs_path(mst_graph, 'A')

# !거리 계산
total_distance_dfs = calc_distance(
    tsp_dfs_path, points_dict)
total_distance_bfs = calc_distance(
    tsp_bfs_path, points_dict)

# !Output
print("TSP DFS Path :", tsp_dfs_path)
print("Total Distance : {:.2f}\n".format(total_distance_dfs))

print("TSP BFS Path :", tsp_bfs_path)
print("Total Distance : {:.2f}\n".format(total_distance_bfs))
