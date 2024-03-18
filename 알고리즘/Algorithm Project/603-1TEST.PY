import pandas as pd
import numpy as np


def load_data(file_path):
    """ CSV 파일에서 데이터를 로드하는 함수 """
    data = pd.read_csv(file_path, header=None)
    # 첫 번째 행(인덱스) 제거
    return data.values[1:]


def select_vectors_with_angle_heuristic(vectors, num_vectors):
    # 초기 벡터 선택
    selected_indices = [np.random.choice(range(len(vectors)))]
    selected_vectors = [vectors[selected_indices[0]]]

    while len(selected_vectors) < num_vectors:
        max_angle = 0
        max_index = -1

        for i, vector in enumerate(vectors):
            if i not in selected_indices:
                # 현재 선택된 벡터들과의 각도 계산
                angles_to_selected = [calculate_angle(
                    vector, v) for v in selected_vectors]
                min_angle_to_selected = min(angles_to_selected)
                if min_angle_to_selected > max_angle:
                    max_angle = min_angle_to_selected
                    max_index = i

        selected_indices.append(max_index)
        selected_vectors.append(vectors[max_index])

    return np.array(selected_vectors)


def calculate_angle(u, v):
    """ 두 벡터 간의 각도를 계산하는 함수 """
    dot_product = np.dot(u, v)
    norm_u = np.linalg.norm(u)
    norm_v = np.linalg.norm(v)
    cos_angle = dot_product / (norm_u * norm_v)
    angle = np.arccos(cos_angle)
    return angle


def calculate_volume(matrix):
    return np.sqrt(np.linalg.det(np.dot(matrix, matrix.T)))


def format_large_number(num):
    units = ["조", "억", "만", ""]
    divisors = [1e12, 1e8, 1e4, 1]

    parts = []
    for unit, divisor in zip(units, divisors):
        part = int(num // divisor)
        if part > 0:
            parts.append(f"{part}{unit}")
            num -= part * divisor

    return ' '.join(parts) if parts else '0'


# 파일 경로 설정
# 여기서 파일 경로를 설정해야 합니다.
file_path = 'C:/Users/LoewllZoe/Documents/GitHub/SchoolAssignment/알고리즘/Algorithm Project/input.csv'

# 데이터 로드
vectors = load_data(file_path)

# 벡터 선택 및 볼륨 계산
selected_matrix = select_vectors_with_angle_heuristic(vectors, 20)
volume = calculate_volume(selected_matrix)

print("선택된 벡터의 볼륨:", format_large_number(volume))
