import numpy as np
import heapq
import pandas as pd
import timeit

"""
@author: 김민규(1924385) - Dong-A University
@date: 2023-11-12
@todo: dijkstra, Floyd-Warshall speed test
"""

# !ppt 그래프
graph_data = """
논산,대전,3
천안,논산,4
포항,부산,5
원주,대구,7
대구,부산,9
천안,대전,10
대전,대구,10
서울,천안,12
논산,광주,13
서울,원주,15
광주,부산,15
대구,포항,19
원주,강릉,21
강릉,포항,25
"""


# Todo: 그래프 만들기
graph = {}
for line in graph_data.strip().split("\n"):
    parts = line.split(',')
    node1, node2, weight = parts[0], parts[1], int(parts[2])
    if node1 not in graph:
        graph[node1] = {}
    if node2 not in graph:
        graph[node2] = {}
    graph[node1][node2] = weight
    graph[node2][node1] = weight

nodes = sorted(graph.keys())
data = {node: [] for node in nodes}
for node in nodes:
    for adj_node in nodes:
        data[node].append(graph[node].get(adj_node, '-'))
graph_df = pd.DataFrame(data, index=nodes)
for i in range(graph_df.shape[0]):
    for j in range(i+1):
        graph_df.iloc[i, j] = ''


# Todo: dijkstra 만들기
def all_pairs_dijkstra(graph):
    all_distances = {}
    for node in graph:
        all_distances[node] = dijkstra(graph, node)
    distance_df = pd.DataFrame(all_distances, index=nodes, columns=nodes)
    for i in range(distance_df.shape[0]):
        for j in range(i+1):
            distance_df.iloc[i, j] = ''
    return distance_df.to_csv(sep='\t', index=True)


def dijkstra(graph, start):
    distances = {node: float('infinity') for node in graph}
    distances[start] = 0
    queue = [(0, start)]

    while queue:
        current_distance, current_node = heapq.heappop(queue)
        for neighbor, weight in graph[current_node].items():
            distance = current_distance + weight
            if distance < distances[neighbor]:
                distances[neighbor] = distance
                heapq.heappush(queue, (distance, neighbor))
    return distances


# Todo: dijkstra 만들기(pq미사용)
def all_pairs_dijkstra_no_priority_queue(graph):
    all_distances = {}
    for node in graph:
        all_distances[node] = dijkstra_no_priority_queue(graph, node)
    distance_df = pd.DataFrame(all_distances, index=nodes, columns=nodes)
    for i in range(distance_df.shape[0]):
        for j in range(i+1):
            distance_df.iloc[i, j] = ''
    return distance_df.to_csv(sep='\t', index=True)


def dijkstra_no_priority_queue(graph, start):
    distances = {node: float('infinity') for node in graph}
    distances[start] = 0
    unvisited = set(graph.keys())

    while unvisited:
        current_node = min(unvisited, key=lambda node: distances[node])
        unvisited.remove(current_node)
        for neighbor, weight in graph[current_node].items():
            distance = distances[current_node] + weight
            if distance < distances[neighbor]:
                distances[neighbor] = distance

    return distances


# Todo: Floyd-Warshall 만들기
def floyd_warshall(graph):
    num_nodes = len(graph)
    node_indices = {node: i for i, node in enumerate(nodes)}
    distance_matrix = np.full((num_nodes, num_nodes), float('infinity'))

    for i, node1 in enumerate(nodes):
        for node2, dist in graph[node1].items():
            j = node_indices[node2]
            distance_matrix[i][j] = dist
        distance_matrix[i][i] = 0

    for k in range(num_nodes):
        for i in range(num_nodes):
            for j in range(num_nodes):
                distance_matrix[i][j] = min(
                    distance_matrix[i][k] + distance_matrix[k][j], distance_matrix[i][j])

    distance_df = pd.DataFrame(distance_matrix, index=nodes, columns=nodes)
    for i in range(distance_df.shape[0]):
        for j in range(i+1):
            distance_df.iloc[i, j] = ''
    return distance_df.to_csv(sep='\t', index=True)


#!출력
print("입력된 그래프")
print(graph_df.to_csv(sep='\t', index=True))
print()

repeat = 3000
print("Dijkstra(with PQ)")
print(all_pairs_dijkstra(graph))
time1 = timeit.timeit("all_pairs_dijkstra(graph)",
                      globals=globals(), number=repeat)
print(f'{repeat}회 반복 소요시간: {time1:.4f} seconds')
print()

print("Dijkstra(without PQ)")
print(all_pairs_dijkstra_no_priority_queue(graph))
time1 = timeit.timeit("all_pairs_dijkstra_no_priority_queue(graph)",
                      globals=globals(), number=repeat)
print(f'{repeat}회 반복 소요시간: {time1:.4f} seconds')
print()


print("Floyd-Warshall")
print(floyd_warshall(graph))
time2 = timeit.timeit("floyd_warshall(graph)",
                      globals=globals(), number=repeat)
print(f'{repeat}회 반복 소요시간: {time2:.4f} seconds')
