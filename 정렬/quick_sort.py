"""
@author: 김민규(1924385)
@date: 2023-09-23
@todo: quick sorting methods
"""

import pandas as pd
import timeit
import random


# !median_of_medians
def find_median(arr):
    arr.sort()  # 배열 정렬
    return arr[len(arr) // 2]  # 중앙값 반환


def median_of_medians(arr):
    # 배열의 길이가 3 이하인 경우, 중앙값 반환
    if len(arr) <= 3:
        return find_median(arr)

    # 배열을 3등분
    size = len(arr) // 3
    left = arr[:size]
    middle = arr[size:2*size]
    right = arr[2*size:]

    # 각 부분의 가장 왼쪽, 중간, 가장 오른쪽 값 중 중앙값 찾기
    medians = [
        find_median([left[0], left[len(left) // 2], left[-1]]),
        find_median([middle[0], middle[len(middle) // 2], middle[-1]]),
        find_median([right[0], right[len(right) // 2], right[-1]])
    ]

    # 세 중앙값 중 중앙값을 피벗으로 반환
    return find_median(medians)


def quick_sort_median_of_medians_pivot(arr):
    # 기본 케이스: 배열의 길이가 1 이하인 경우 배열 반환
    if len(arr) <= 1:
        return arr
    pivot = median_of_medians(arr)  # 피벗 찾기
    low = [x for x in arr if x < pivot]  # 피벗보다 작은 요소
    high = [x for x in arr if x > pivot]  # 피벗보다 큰 요소
    # 재귀적으로 정렬 및 결합
    return quick_sort_median_of_medians_pivot(low) + [pivot] + quick_sort_median_of_medians_pivot(high)


# !median_of_three
def find_median_of_three(arr):
    left = arr[0]
    middle = arr[len(arr) // 2]
    right = arr[-1]
    # 세 값 중 중앙값 찾기
    return sorted([left, middle, right])[1]


def quick_sort_median_of_three_pivot(arr):
    if len(arr) <= 1:
        return arr  # 기본 케이스: 배열의 길이가 1 이하인 경우 배열 반환
    pivot = find_median_of_three(arr)  # 피벗 찾기
    left = [x for x in arr if x < pivot]  # 피벗보다 작은 요소
    middle = [x for x in arr if x == pivot]  # 피벗과 같은 요소
    right = [x for x in arr if x > pivot]  # 피벗보다 큰 요소
    # 재귀적으로 정렬 및 결합
    return quick_sort_median_of_three_pivot(left) + middle + quick_sort_median_of_three_pivot(right)


#! 랜덤 피벗
def quick_sort_random_pivot(arr):
    if len(arr) <= 1:
        return arr  # 기본 케이스: 배열의 길이가 1 이하인 경우 배열 반환
    pivot_index = random.randint(0, len(arr) - 1)  # 랜덤 피벗 인덱스 선택
    pivot = arr[pivot_index]  # 피벗 선택
    left = [x for x in arr if x < pivot]  # 피벗보다 작은 요소
    middle = [x for x in arr if x == pivot]  # 피벗과 같은 요소
    right = [x for x in arr if x > pivot]  # 피벗보다 큰 요소
    # 재귀적으로 정렬 및 결합
    return quick_sort_random_pivot(left) + middle + quick_sort_random_pivot(right)


# !그냥 중간 값
def quick_sort_center_pivot(arr):
    if len(arr) <= 1:
        return arr  # 기본 케이스: 배열의 길이가 0 또는 1인 경우 배열을 그대로 반환
    pivot = arr[len(arr) // 2]  # 피벗 선택 (여기서는 배열의 중간 요소)
    left = [x for x in arr if x < pivot]  # 피벗보다 작은 요소들
    middle = [x for x in arr if x == pivot]  # 피벗과 같은 요소들
    right = [x for x in arr if x > pivot]  # 피벗보다 큰 요소들
    # 재귀적으로 정렬하고 결합
    return quick_sort_center_pivot(left) + middle + quick_sort_center_pivot(right)


#!과제 엑셀 파일로 테스트
file_path = '정렬/input_quick_sort.xlsx'
data = pd.read_excel(file_path)
arr = data.iloc[:, 0].tolist()

#!랜덤한 배열로 테스트
# arr = [random.randint(0, 9999) for _ in range(5000)]

# !퀵 정렬(center pivot)의 실행 시간 측정
quick_sort_center_pivot_time = timeit.timeit(
    "quick_sort_center_pivot(arr.copy())", setup="from __main__ import quick_sort_center_pivot, arr", number=1000)
print(f"quick_sort_center_pivot: {quick_sort_center_pivot_time:.3f} seconds")

# !퀵 정렬(median of three pivot)의 실행 시간 측정
quick_sort_median_of_three_pivot_time = timeit.timeit(
    "quick_sort_median_of_three_pivot(arr.copy())", setup="from __main__ import quick_sort_median_of_three_pivot, arr", number=1000)
print(
    f"quick_sort_median_of_three_pivot: {quick_sort_median_of_three_pivot_time:.3f} seconds")

# !퀵 정렬(median of medians pivot)의 실행 시간 측정
quick_sort_median_of_medians_pivot_time = timeit.timeit(
    "quick_sort_median_of_medians_pivot(arr.copy())", setup="from __main__ import quick_sort_median_of_medians_pivot, arr", number=1000)
print(
    f"quick_sort_median_of_medians_pivot: {quick_sort_median_of_medians_pivot_time:.3f} seconds")

# !퀵 정렬(random pivot)의 실행 시간 측정
quick_sort_random_pivot_time = timeit.timeit(
    "quick_sort_random_pivot(arr.copy())", setup="from __main__ import quick_sort_random_pivot, arr", number=1000)
print(
    f"quick_sort_random_pivot: {quick_sort_random_pivot_time:.3f} seconds")


def are_arrays_equal(*arrays):
    return all(all(a == b for b in arrays) for a in arrays)


arr_size = len(arr)
sorted_arr1 = quick_sort_center_pivot(arr.copy())
sorted_arr2 = quick_sort_median_of_three_pivot(arr.copy())
sorted_arr3 = quick_sort_median_of_medians_pivot(arr.copy())
sorted_arr4 = quick_sort_random_pivot(arr.copy())
is_equal = are_arrays_equal(sorted_arr1, sorted_arr2, sorted_arr3, sorted_arr4)
print("모두 같은가? : ", is_equal)


# print("Sorted array is:", sorted_arr1)
