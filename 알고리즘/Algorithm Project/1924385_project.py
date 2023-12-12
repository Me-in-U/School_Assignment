
import pandas as pd
from numpy.linalg import det
import numpy as np
import time


def greedy_selection(data, vector_count):
    selected_indices = []
    remaining_indices = set(range(data.shape[1]))
    max_volume = -np.inf
    best_index = None
    for _ in range(vector_count):
        for i in remaining_indices:
            test_indices = selected_indices + [i]
            test_matrix = data[:, test_indices]
            test_volume = np.sqrt(
                abs(np.linalg.det(np.dot(test_matrix.T, test_matrix))))
            if test_volume > max_volume:
                max_volume = test_volume
                best_index = i
        selected_indices.append(best_index)
        remaining_indices.remove(best_index)
    return selected_indices, max_volume


#! 파일 읽기
file_path = 'input.csv'
df = pd.read_csv(file_path, encoding='utf-8', header=None)
data = df.iloc[1:].values

vector_count = 20

#! Test1
start_time = time.time()

best_combination, max_volume = greedy_selection(data, vector_count)
selected_matrix = data[:, best_combination]

end_time = time.time()

running_time = (end_time - start_time) * 1e6
performance_metric = max_volume / running_time

with open('output.txt', 'w', encoding='utf-8') as f:
    f.write(f"최대 volume : {max_volume}\n")
    f.write(f"벡터 집합 : {best_combination}\n")
    f.write(f"running time: {running_time} microseconds\n")
    f.write(
        f"Performance Metric (Volume/Running Time): {performance_metric}\n")
print("Results saved to output.txt")
