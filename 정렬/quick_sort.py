"""
@author: 김민규(1924385)
@date: 2023-09-23
@todo: quick sorting methods
"""

import pandas as pd
import timeit
import random


#!Hoare Partition Scheme(중간 인덱스)
def hoare_partition(arr, left, right):
    if left < right:
        pivot_index = hoare_partition_center_pivot(arr, left, right)
        hoare_partition(arr, left, pivot_index)
        hoare_partition(arr, pivot_index + 1, right)
    return arr


def hoare_partition_center_pivot(arr, left, right):
    pivot = arr[(left + right) // 2]  # 중간 인덱스를 피벗으로 설정
    while True:
        while arr[left] < pivot:
            left += 1
        while arr[right] > pivot:
            right -= 1
        if left >= right:
            return right
        arr[left], arr[right] = arr[right], arr[left]
        left += 1
        right -= 1


# !median_of_medians
def find_median(arr):
    arr.sort()  # 배열 정렬
    return arr[len(arr) // 2]  # 중앙값 반환


def median_of_medians(arr):
    if len(arr) <= 3:
        return find_median(arr)

    size = len(arr) // 3
    sub_arrays = [arr[i:i+size] for i in range(0, len(arr), size)]

    medians = [find_median(sub_array) for sub_array in sub_arrays]
    return find_median(medians)


def median_of_medians_pivot(arr):
    if len(arr) <= 1:
        return arr
    pivot = median_of_medians(arr)
    low = [x for x in arr if x < pivot]
    equal = [x for x in arr if x == pivot]
    high = [x for x in arr if x > pivot]
    return median_of_medians_pivot(low) + equal + median_of_medians_pivot(high)


# !median_of_three
def find_median_of_three(arr):
    left = arr[0]
    middle = arr[len(arr) // 2]
    right = arr[-1]
    # 세 값 중 중앙값 찾기
    return sorted([left, middle, right])[1]


def median_of_three_pivot(arr):
    if len(arr) <= 1:
        return arr  # 기본 케이스: 배열의 길이가 1 이하인 경우 배열 반환
    pivot = find_median_of_three(arr)  # 피벗 찾기
    left = [x for x in arr if x < pivot]  # 피벗보다 작은 요소
    middle = [x for x in arr if x == pivot]  # 피벗과 같은 요소
    right = [x for x in arr if x > pivot]  # 피벗보다 큰 요소
    # 재귀적으로 정렬 및 결합
    return median_of_three_pivot(left) + middle + median_of_three_pivot(right)


#! 랜덤 피벗
def random_pivot(arr):
    if len(arr) <= 1:
        return arr  # 기본 케이스: 배열의 길이가 1 이하인 경우 배열 반환
    pivot_index = random.randint(0, len(arr) - 1)  # 랜덤 피벗 인덱스 선택
    pivot = arr[pivot_index]  # 피벗 선택
    left = [x for x in arr if x < pivot]  # 피벗보다 작은 요소
    middle = [x for x in arr if x == pivot]  # 피벗과 같은 요소
    right = [x for x in arr if x > pivot]  # 피벗보다 큰 요소
    # 재귀적으로 정렬 및 결합
    return random_pivot(left) + middle + random_pivot(right)


# !그냥 중간 값
def center_pivot(arr):
    if len(arr) <= 1:
        return arr  # 기본 케이스: 배열의 길이가 0 또는 1인 경우 배열을 그대로 반환
    pivot = arr[len(arr) // 2]  # 피벗 선택 (여기서는 배열의 중간 요소)
    left = [x for x in arr if x < pivot]  # 피벗보다 작은 요소들
    middle = [x for x in arr if x == pivot]  # 피벗과 같은 요소들
    right = [x for x in arr if x > pivot]  # 피벗보다 큰 요소들
    # 재귀적으로 정렬하고 결합
    return center_pivot(left) + middle + center_pivot(right)


#!과제 엑셀 파일로 테스트
# file_path = '정렬/input_quick_sort.xlsx'
# data = pd.read_excel(file_path)
# arr = data.iloc[:, 0].tolist()

#!랜덤한 배열로 테스트
arr = [random.randint(0, 9999) for _ in range(100000)]

print(f'배열 크기 : {len(arr)}')
repeat_time = 2
print(f"시간 측정 : {repeat_time}회 반복 평균")

# !퀵 정렬(center pivot)의 실행 시간 측정
center_pivot_time = timeit.timeit(
    "center_pivot(arr.copy())", globals=globals(), number=repeat_time)
print(f"center_pivot: {center_pivot_time:.3f} seconds")

# !퀵 정렬(median of three pivot)의 실행 시간 측정
median_of_three_pivot_time = timeit.timeit(
    "median_of_three_pivot(arr.copy())", globals=globals(), number=repeat_time)
print(
    f"median_of_three_pivot: {median_of_three_pivot_time:.3f} seconds")

# !퀵 정렬(median of medians pivot)의 실행 시간 측정
median_of_medians_pivot_time = timeit.timeit(
    "median_of_medians_pivot(arr.copy())", globals=globals(), number=repeat_time)
print(
    f"median_of_medians_pivot: {median_of_medians_pivot_time:.3f} seconds")

# !퀵 정렬(random pivot)의 실행 시간 측정
random_pivot_time = timeit.timeit(
    "random_pivot(arr.copy())", globals=globals(), number=repeat_time)
print(
    f"random_pivot: {random_pivot_time:.3f} seconds")

# !퀵 정렬(Hoare Partition Scheme Center Pivot)의 실행 시간 측정
hoare_partition_time = timeit.timeit(
    "hoare_partition(arr.copy(), 0, len(arr.copy()) - 1)", globals=globals(), number=repeat_time)
print(
    f"hoare_partition(center_pivot): {hoare_partition_time:.3f} seconds")

arr_size = len(arr)
sorted_arr1 = center_pivot(arr.copy())
sorted_arr2 = median_of_three_pivot(arr.copy())
sorted_arr3 = median_of_medians_pivot(arr.copy())
sorted_arr4 = random_pivot(arr.copy())
sorted_arr5 = hoare_partition(arr.copy(), 0, len(arr.copy()) - 1)


def are_arrays_equal(*arrays):
    return all(all(a == b for b in arrays) for a in arrays)


is_equal = are_arrays_equal(
    sorted_arr1, sorted_arr2, sorted_arr3, sorted_arr4, sorted_arr5)
print("모두 같은가? : ", is_equal)

# print(sorted_arr1)
# print(sorted_arr2)
# print(sorted_arr3)
# print(sorted_arr4)
# print(sorted_arr5)
# print("Sorted array is:", sorted_arr1)
