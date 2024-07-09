##########################################################################
# BigData Analytics: BA_CSE418_lastname_id.py
##
# 과제1과 2에 대한 템플릿 코드
# [TODO]# 라고 표기된 지역에만 코드를 작성하시기 바랍니다.
##
# 학생 이름:김민규
# 학번:1924385


import sys
from pprint import pprint
from random import random, randint
import random
from collections import deque
from sys import getsizeof
import hashlib
# import resource

##########################################################################
##########################################################################
# 풀이: 아래 4가지 메소드를 주어진 메모리 변수에 각각 맞추어 구현하시기 바랍니다.
#
# 각 메소드는 amount를 100개 크기의 메모리 배열을 가진다.
# 메모리 배열의 변수명은 memory1a, memory1b, memory1c, memory1d 이다.
# 따라서, 주어진 메모리 배열 이외에 데이터 저장을 위한 새로운 변수선언은 금지한다
# 현재 메모리의 크기는 8,000을 넘지 않도록 설계되어 있음.

MEMORY_SIZE = 100  # do not edit

memory1a = deque([None] * MEMORY_SIZE, maxlen=MEMORY_SIZE)  # do not edit


def task1AReservoirSampling(element, returnResult=True):
    # [TODO]#
    # Reservoir Sampling 알고리즘 적용
    if None in memory1a:
        memory1a.append(element)
    else:
        j = randint(0, len(memory1a) - 1)
        if j < MEMORY_SIZE:
            memory1a[j] = element

    # when the stream is requesting the current result (e.g., sum )
    if returnResult:
        result = sum(x for x in memory1a if x is not None)
        # [TODO]#
        # any additional processing to return the result at this point
        return result
    else:  # no need to return a result
        pass


memory1b = deque([None] * MEMORY_SIZE, maxlen=MEMORY_SIZE)  # do not edit


def task2BDistinctAmount(element, returnResult=True):
    # [TODO]#

    def trailing_zeros(x):
        if x == 0:
            return 1
        count = 0
        while x & 1 == 0:
            count += 1
            x >>= 1
        return count

    def hash_function(x, seed):
        rng = random.Random(seed)
        h = hashlib.md5(f"{x}{rng.random()}".encode()).hexdigest()
        return int(h, 16)

    # Flajolet-Martin 알고리즘 적용
    for i in range(MEMORY_SIZE):
        h = hash_function(element, i)
        zeroes = trailing_zeros(h)
        if memory1b[i] is None:
            memory1b[i] = zeroes
        else:
            memory1b[i] = max(memory1b[i], zeroes)

    if returnResult:  # when the stream is requesting the current result
        avg_zeroes = sum(memory1b) / len(memory1b)
        result = 2 ** avg_zeroes
        # [TODO]#
        # any additional processing to return the result at this point
        return result
    else:  # no need to return a result
        pass


memory1c = deque([None] * MEMORY_SIZE, maxlen=MEMORY_SIZE)  # do not edit


def task3CMedian(element, returnResult=True):
    # [TODO]#
    if None in memory1c:
        for i in range(len(memory1c)):
            if memory1c[i] is None:
                memory1c[i] = element
                break
    else:
        j = randint(0, len(memory1c) - 1)
        memory1c[j] = element

    if returnResult:  # when the stream is requesting the current result
        valid_elements = [x for x in memory1c if x is not None]
        if not valid_elements:
            return 0

        valid_elements.sort()
        n = len(valid_elements)
        if n % 2 == 1:
            result = valid_elements[n // 2]
        else:
            result = (valid_elements[n // 2 - 1] + valid_elements[n // 2]) / 2

        # [TODO]#
        # any additional processing to return the result at this point
        return result
    else:  # no need to return a result
        pass


memory1d = deque([None] * MEMORY_SIZE, maxlen=MEMORY_SIZE)  # do not edit


def task4DMostFreqAmount(element, returnResult=True):
    # [TODO]#
    # process the element you may only use memory1a, storing at most 100
    found = False
    for i in range(0, len(memory1d), 2):
        if memory1d[i] == element:
            memory1d[i + 1] += 1
            found = True
            break

    if not found:
        if None in memory1d:
            for i in range(0, len(memory1d), 2):
                if memory1d[i] is None:
                    memory1d[i] = element
                    memory1d[i + 1] = 1
                    break
        else:
            min_index = min(range(0, len(
                memory1d), 2), key=lambda x: memory1d[x + 1] if memory1d[x + 1] is not None else float('inf'))
            memory1d[min_index] = element
            memory1d[min_index + 1] = 1

    if returnResult:  # when the stream is requesting the current result
        max_elem = max(range(0, len(memory1d), 2),
                       key=lambda x: memory1d[x + 1] if memory1d[x + 1] is not None else float('-inf'))
        result = memory1d[max_elem]
        # [TODO]#
        # any additional processing to return the result at this point
        return result
    else:  # no need to return a result
        pass


##########################################################################
##########################################################################
# MAIN 함수: 해당 코드는 파일로부터 스트림을 불러오고 각 작업에 대한 함수를 호출한다.
# 반환되는 결과의 출력은 자주 수행될 수 있다.
# 가능하면 아래의 코드를 수정하지 마시오
# 물론, 보너스 문제에 대해서는 수정이 가능하다.

def getMemorySize(l):  # returns sum of all element sizes
    return sum([getsizeof(e) for e in l])+getsizeof(l)


if __name__ == "__main__":  # [Uncomment peices to test]

    print("\n\nTESTING YOUR CODE\n")

    ###################
    # The main stream loop:
    print("\n\n*************************\n Beginning stream input \n*************************\n")
    filename = sys.argv[1]  # the data file to read into a stream
    printLines = frozenset([10**i for i in range(1, 20)]
                           )  # stores lines to print
    peakMem = 0  # tracks peak memory usage

    with open(filename, 'r') as infile:
        i = 0  # keeps track of lines read
        for line in infile:

            # remove \n and convert to int
            element = int(line.strip())
            i += 1
            # call tasks
            if i in printLines:  # print status at this point:
                result1a = task1AReservoirSampling(element, returnResult=True)
                result1b = task2BDistinctAmount(element, returnResult=True)
                result1c = task3CMedian(element, returnResult=True)
                result1d = task4DMostFreqAmount(element, returnResult=True)

                print(" Result at stream element # %d:" % i)
                print("   1A:  Sum value(by Reservoir Sampling): %d" %
                      int(result1a))
                print("   2B:                   Distinct values: %d" %
                      int(result1b))
                print("   3C:                            Median: %.2f" %
                      float(result1c))
                print("   4D:               Most frequent value: %d" %
                      int(result1d))
                print(" [current memory sizes: A: %d, B: %d, C: %d, D: %d]\n" % (getMemorySize(
                    memory1a), getMemorySize(memory1b), getMemorySize(memory1c), getMemorySize(memory1d)))

            else:  # just pass for stream processing
                result1a = task1AReservoirSampling(element, False)
                result1b = task2BDistinctAmount(element, False)
                result1c = task3CMedian(element, False)
                result1d = task4DMostFreqAmount(element, False)

            # memUsage = resource.getrusage(resource.RUSAGE_SELF).ru_maxrss
            # if memUsage > peakMem:
            #     peakMem = memUsage

    print("\n*******************************\n       Stream Terminated \n*******************************")
    print("(peak memory usage was: ", peakMem, ")")
