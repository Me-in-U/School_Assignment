import pandas as pd
import numpy as np
from numpy.linalg import det
import random


def calculate_volume(matrix):
    # 행렬의 볼륨을 계산합니다. 볼륨은 행렬의 행렬식(determinant)의 절대값으로 정의됩니다.
    # return abs(det(matrix))
    return np.sqrt(np.linalg.det(np.dot(matrix, matrix.T)))


def sorted_heuristic_algorithm(data, vector_count, iterations=100):
    # 초기 벡터 세트 선택: 가장 큰 노름(norm)을 가진 벡터를 선택합니다.
    norms = np.linalg.norm(data, axis=0)
    initial_indices = np.argsort(norms)[-vector_count:]
    current_matrix = data[:, initial_indices]
    current_volume = calculate_volume(current_matrix)

    for _ in range(iterations):
        # 현재 세트에 포함되지 않은 각 벡터에 대해 볼륨 증가 잠재력 계산
        potential_increase = []
        for i in range(data.shape[1]):
            if i not in initial_indices:
                max_increase = 0
                for j in range(vector_count):
                    test_indices = initial_indices.copy()
                    test_indices[j] = i
                    test_matrix = data[:, test_indices]
                    test_volume = calculate_volume(test_matrix)
                    increase = test_volume - current_volume
                    max_increase = max(max_increase, increase)
                potential_increase.append((i, max_increase))
        # 잠재력이 높은 순으로 벡터 정렬
        potential_increase.sort(key=lambda x: x[1], reverse=True)

        # 가장 높은 잠재력을 가진 벡터로 현재 세트의 벡터를 교체 시도
        for i, increase in potential_increase:
            if increase > 0:
                for j in range(vector_count):
                    test_indices = initial_indices.copy()
                    test_indices[j] = i
                    test_matrix = data[:, test_indices]
                    test_volume = calculate_volume(test_matrix)
                    print(format_large_number(current_volume),
                          " <-> ", format_large_number(test_volume))
                    if test_volume > current_volume:
                        current_volume = test_volume
                        current_matrix = test_matrix
                        initial_indices = test_indices
                        break

    return current_volume, initial_indices


def improved_heuristic_algorithm(data, vector_count, iterations=100, patience=10):
    # 초기 벡터 세트를 노름 기반으로 선택
    norms = np.linalg.norm(data, axis=0)
    initial_indices = np.argsort(norms)[-vector_count:]
    current_matrix = data[:, initial_indices]
    current_volume = calculate_volume(current_matrix)

    no_improvement_count = 0

    for _ in range(iterations):
        if no_improvement_count >= patience:
            break

        # 잠재적 볼륨 증가량 계산
        potential_increase = []
        for i in range(data.shape[1]):
            if i not in initial_indices:
                max_increase = 0
                for j in range(vector_count):
                    test_indices = initial_indices.copy()
                    test_indices[j] = i
                    test_matrix = data[:, test_indices]
                    test_volume = calculate_volume(test_matrix)
                    increase = test_volume - current_volume
                    max_increase = max(max_increase, increase)
                potential_increase.append((i, max_increase))

        # 잠재력이 높은 순으로 벡터 정렬
        potential_increase.sort(key=lambda x: x[1], reverse=True)

        # 가장 높은 잠재력을 가진 벡터로 현재 세트의 벡터를 교체 시도
        for i, increase in potential_increase:
            if increase > 0:
                for j in range(vector_count):
                    test_indices = initial_indices.copy()
                    test_indices[j] = i
                    test_matrix = data[:, test_indices]
                    test_volume = calculate_volume(test_matrix)
                    print(format_large_number(current_volume), test_volume)
                    if test_volume > current_volume:
                        current_volume = test_volume
                        current_matrix = test_matrix
                        initial_indices = test_indices
                        no_improvement_count = 0
                        break
                else:
                    continue
                break
            else:
                no_improvement_count += 1

    return current_volume, initial_indices


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


# !유전 알고리즘
def create_initial_population(data, pop_size, vector_count):
    population = []
    for _ in range(pop_size):
        indices = random.sample(range(data.shape[1]), vector_count)
        population.append(indices)
    return population


def evaluate_population(data, population):
    fitness = []
    for individual in population:
        matrix = data[:, individual]
        fitness.append(calculate_volume(matrix))
    return fitness


def select_parents(population, fitness, num_parents):
    parents = []
    for _ in range(num_parents):
        max_fitness_idx = np.argmax(fitness)
        parents.append(population[max_fitness_idx])
        fitness[max_fitness_idx] = -1
    return parents


def crossover(parents, offspring_size):
    offspring = []
    for _ in range(offspring_size[0]):
        # 랜덤한 교차점 선택
        crossover_point = random.randint(1, offspring_size[1]-1)
        parent1_idx = random.randint(0, len(parents) - 1)
        parent2_idx = random.randint(0, len(parents) - 1)
        offspring.append(
            parents[parent1_idx][:crossover_point] + parents[parent2_idx][crossover_point:])
    return offspring


def mutate(offspring_crossover, mutation_rate, data_shape):
    for idx in range(len(offspring_crossover)):
        if random.uniform(0, 1) < mutation_rate:
            # 여러 점에서 돌연변이 발생
            for _ in range(random.randint(1, len(offspring_crossover[idx]) // 2)):
                mutation_idx = random.randint(0, data_shape[1] - 1)
                offspring_crossover[idx][random.randint(
                    0, len(offspring_crossover[idx]) - 1)] = mutation_idx
    return offspring_crossover


def genetic_algorithm(data, vector_count, pop_size, num_parents, max_generations, mutation_rate):
    population = create_initial_population(data, pop_size, vector_count)
    for generation in range(max_generations):
        fitness = evaluate_population(data, population)
        parents = select_parents(population, fitness, num_parents)
        offspring_crossover = crossover(
            parents, (pop_size - num_parents, vector_count))
        offspring_mutation = mutate(
            offspring_crossover, mutation_rate, data.shape)
        population[0:num_parents] = parents
        population[num_parents:] = offspring_mutation
    return population[np.argmax(evaluate_population(data, population))]


# 데이터를 읽는 코드
# 실제 파일 경로로 바꿔주세요.
file_path = 'C:/Users/LoewllZoe/Documents/GitHub/SchoolAssignment/알고리즘/Algorithm Project/input.csv'
df = pd.read_csv(file_path, header=None)

# 첫 번째 행은 인덱스라고 가정하고, 이를 제외한 나머지 데이터를 사용합니다.
data = df.iloc[1:].values

# 알고리즘 매개변수
vector_count = 20  # 벡터의 수 (20차원 벡터라고 가정)
iterations = 300  # 휴리스틱 알고리즘의 반복 횟수

# !휴리스틱 알고리즘 적용
max_volume, best_combination = sorted_heuristic_algorithm(
    data, vector_count, iterations)
print("최대 볼륨:", format_large_number(max_volume))
print("최적의 벡터 조합 인덱스:", best_combination)


# !유전 알고리즘 매개변수
pop_size = 1000  # 인구 크기 증가
num_parents = 200  # 부모의 수 증가
max_generations = 2000  # 세대 수 증가
mutation_rate = 0.3  # 돌연변이 확률 증가

# # 최적의 조합 찾기
# best_combination = genetic_algorithm(
#     data, vector_count, pop_size, num_parents, max_generations, mutation_rate)
# print("최적의 벡터 조합 인덱스:", best_combination)
# # 최적의 조합으로 볼륨 계산
# best_matrix = data[:, best_combination]
# best_volume = calculate_volume(best_matrix)
# print("최적의 조합으로 형성된 행렬의 볼륨:", format_large_number(best_volume))
