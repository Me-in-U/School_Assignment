def evenAdd(L):
    return sum(x for x in L if x % 2 == 0)

def evenAvg(total, L):
    count = evenCount(total, L)
    return total / count if count != 0 else 0

def evenCount(total, L):
    return len([x for x in L if x % 2 == 0])

if __name__ == '__main__':
    print("모듈 테스트")
    test_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    total = evenAdd(test_list)
    avg = evenAvg(total, test_list)
    count = evenCount(total, test_list)
    print(f"짝수총합: {total}, 평균: {avg}, 짝수개수: {count}")
