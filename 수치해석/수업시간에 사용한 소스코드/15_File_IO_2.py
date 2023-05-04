from numpy import array

filename = 'test_data.txt'
with open(filename, encoding='ascii') as file_object:
    lines = file_object.readlines()
#lines2 = int(lines)
Mat = []
Mat2 = Mat

for line in lines:
    Mat.append(line.split())
    #split() 함수는 문자열을 특정 기준으로 나눔, 인자가 없는 경우 띄어쓰기, 엔터 구분
    # append() append는 덧붙인다는 뜻으로 괄호( ) 안에 값을 입력하면 새로운 요소를 array 맨 끝에 객체로 추가한다.
    # 요소를 추가할 때는 객체로 추가하게 되는데, 입력한 값이 리스트와 같은 반복 가능한 iterable 자료형이더라도 객체로 저장

for idx_i, val_i in enumerate(Mat):
    #print(idx_i, val_i)
    for idx_j, val_j in enumerate(val_i):
        print(idx_i, idx_j, val_j)
        Mat2[idx_i][idx_j] = int(Mat[idx_i][idx_j])


A = array(Mat2)
#a = array([[4, 1, -1],[3, -1, 2],[-1, 2, 3]])
print(Mat2)
#print(a)