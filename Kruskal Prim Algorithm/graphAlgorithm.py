"""
@author: 김민규(1924385) - Dong-A University
@date: 2023-10-13~15
@todo: 주어진 그래프를 Kruskal, Prim algorithm으로 MST 완성
"""

import math


# kruskal Algorithm
def kruskal(graph):
    def find_parent(vertex):
        if parent[vertex] == vertex:
            return vertex
        parent[vertex] = find_parent(parent[vertex])
        return parent[vertex]

    def union(x, y):  # 두 vertex x, y가 속한 두 트리를 하나로 합치는 역할
        root_x, root_y = find_parent(x), find_parent(y)
        # 랭크가 높은 트리 아래에 랭크가 낮은 트리를 병합
        if rank[root_x] < rank[root_y]:
            parent[root_x] = root_y
        elif rank[root_x] > rank[root_y]:
            parent[root_y] = root_x
        else:
            parent[root_y] = root_x
            rank[root_x] += 1

    # 1. 가중치의 오름차순으로 간선들을 정렬: L = 정렬된 간선 리스트
    L = sorted(graph, key=lambda x: x[2])
    print(L)
    # 2. T=Φ // 트리 T를 초기화
    T = []

    # vertices set, 부모 vertex dict(초기값 본인), 랭크 dict 초기화(초기값 0)
    vertices = {vertex for edge in graph for vertex in edge[:2]}
    parent = {vertex: vertex for vertex in vertices}
    rank = {vertex: 0 for vertex in vertices}
    mst_cost = 0
    # 3. while (T의 간선 수 < n-1)
    while len(T) < len(parent) - 1:
        # 4. L에서 가장 작은 가중치를 가진 간선 e를 가져오고, e를 L에서 제거
        # e의 정보(정점 u,v와 가중치 w)
        u, v, w = L.pop(0)
        # 5. if (간선 e가 T에 추가되어 사이클을 만들지 않으면)
        # -> 두 vertex가 같은 vertex에 연결되어있는지 확인
        root_u, root_v = find_parent(u), find_parent(v)
        if root_u != root_v:
            # 6. e를 T에 추가
            T.append((ord(u) - ord('a'), ord(v) - ord('a'), w))
            union(root_u, root_v)
            mst_cost += w

    # 9. Return 트리 T, T는 MST
    return mst_cost, T


# Prim Algorithm
def prim(graph):
    # 1-6. 임의의 정점 p를 선택하고 D[p] = 0으로 설정
    # p에서 나가는 간선을 기반으로 D 초기화
    # p를 제외한 각 정점에 대해 D를 무한으로 초기화(∞)
    vertices = {vertex for edge in graph for vertex in edge[:2]}
    p = list(vertices)[0]  # 임의의 vertex p 선택(0번째)
    D = {vertex: math.inf for vertex in vertices}
    D[p] = 0
    # print(D)  # {'f': 0, 'a': inf, 'c': inf, 'e': inf, 'b': inf, 'd': inf}
    for u, v, w in graph:
        if u == p:
            D[v] = w
        elif v == p:
            D[u] = w
    # print(D)  # {'f': 0, 'a': inf, 'c': 1, 'e': 9, 'b': 2, 'd': 7}

    mst_cost = 0
    # 7. 트리 T를 정점 p로 초기화(임의의 시작점은 바로 추가)
    T = {p}
    # 8. T의 정점 수가 n보다 작은 동안 반복
    while len(T) < len(vertices):
        # 9. T에 없는 정점 중 D[v]가 최소인 정점 vmin 찾음
        vmin = min((D[v], v) for v in vertices if (v not in T))[1]
        mst_cost += D[vmin]
        # 9. T에 있는 정점 u와 vmin을 연결하는 간선을 T에 추가
        T.add(vmin)
        # 10-12. 새로 추가된 정점 vmin을 기반으로 D 업데이트
        for u, v, w in graph:
            if (u == vmin) and (v not in T) and (w < D[v]):
                D[v] = w
            elif (v == vmin) and (u not in T) and (w < D[u]):
                D[u] = w

    # 최소 신장 트리를 구성하는 간선 추출
    mst = [(ord(u) - ord('a'), ord(v) - ord('a'), w)
           for u, v, w in graph if ((u in T) and (v in T)) and ((D[v] == w) or (D[u] == w))]

    # 9. Return mst
    return mst_cost, mst


# 강의자료 15페이지에 있는 그래프
kruskal_graph = [
    ('d', 'f', 7),
    ('e', 'f', 9),
    ('b', 'd', 4),
    ('a', 'e', 4),
    ('b', 'f', 2),
    ('a', 'b', 8),
    ('d', 'e', 3),
    ('b', 'c', 1),
    ('c', 'f', 1),
    ('a', 'd', 2)
]
# 강의자료 31페이지에 있는 그래프
prim_graph = [
    ('b', 'f', 2),
    ('a', 'd', 2),
    ('a', 'e', 4),
    ('d', 'b', 4),
    ('c', 'f', 1),
    ('d', 'f', 7),
    ('e', 'd', 5),
    ('a', 'b', 3),
    ('e', 'f', 9),
    ('b', 'c', 1)
]

cost1, kruskal_MST = kruskal(kruskal_graph)
cost2, prim_MST = prim(prim_graph)

print("kruskal mst cost  :", cost1, )
print("kruskal mst graph :", kruskal_MST)
print("prim mst cost     :", cost2)
print("prim mst graph    :", prim_MST)
