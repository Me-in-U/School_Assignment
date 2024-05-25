import random as rd
from pyMyEven import evenAdd, evenAvg, evenCount

# 1. random(): 0~1 미만 사이의 실수를 생성
def generate_random_float():
    return rd.random()

# 2. randrange(a, b, c): 지정한 범위 내의 정수를 반환
def generate_random_range(start, end, step):
    return rd.randrange(start, end, step)

# 3. randint(a, b): 지정한 범위 내의 정수를 반환
def generate_random_int(start, end):
    return rd.randint(start, end)

# 4. shuffle(seq): 주어진 시퀀스(seq) 리스트의 요소를 랜덤하게 섞음
def shuffle_list(seq):
    rd.shuffle(seq)
    return seq

# 5. choice(seq): seq 시퀀스 내의 임의의 요소를 선택
def choose_random_element(seq):
    return rd.choice(seq)

# 6. sample(seq, n): seq에서 n개의 요소를 임의로 선택
def sample_random_elements(seq, n):
    return rd.sample(seq, n)

# 1부터 100까지의 정수 중에서 임의로 10개를 선택하여 리스트 생성
seq = rd.sample(range(1, 101), 10)
print(f"임의로 선택된 리스트: {seq}")

# 각 함수들을 사용하여 결과 출력
random_float = generate_random_float()
print(f"0과 1 사이의 실수: {random_float}")

random_range = generate_random_range(1, 10, 2)
print(f"1부터 9까지 2씩 증가하는 정수 중 하나: {random_range}")

random_int = generate_random_int(1, 10)
print(f"1부터 10까지의 정수 중 하나: {random_int}")

# seq 리스트를 섞음
shuffled_list = shuffle_list(seq[:])
print(f"섞인 리스트: {shuffled_list}")

random_choice = choose_random_element(shuffled_list)
print(f"리스트에서 임의로 선택된 요소: {random_choice}")

random_sample = sample_random_elements(shuffled_list, 3)
print(f"리스트에서 임의로 선택된 3개의 요소: {random_sample}")

# 짝수만 골라서 짝수의 합, 평균, 개수를 출력
total = evenAdd(shuffled_list)
avg = evenAvg(total, shuffled_list)
count = evenCount(total, shuffled_list)

print(f"짝수의 합: {total}")
print(f"짝수의 평균: {avg}")
print(f"짝수의 개수: {count}")
