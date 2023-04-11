# |이 코드는 크래머의 규칙을 이용하여 선형방정식을 푸는 코드입니다.
# |
# |좋은 점:
# |- 코드가 간결하고 이해하기 쉽습니다.
# |- 함수를 이용하여 중복되는 코드를 줄였습니다.
# |- 파일 입출력을 이용하여 입력과 출력을 처리합니다.
# |
# |나쁜 점:
# |- 변수명이 짧아서 의미 파악이 어렵습니다. 변수명을 더 구체적으로 작성하면 코드의 가독성이 높아질 것입니다.
# |- 입력 파일의 형식이 고정되어 있어서 다른 형식의 입력 파일을 처리할 수 없습니다. 예외 처리를 추가하면 더욱 안정적인 코드가 될 것입니다.
# |


def get_det2(a):
    return a[0][0]*a[1][1] - a[0][1]*a[1][0]


def get_det3(a):
    return a[0][0]*(a[1][1]*a[2][2] - a[1][2]*a[2][1]) - a[0][1]*(a[1][0]*a[2][2] - a[1][2]*a[2][0]) + a[0][2]*(a[1][0]*a[2][1] - a[1][1]*a[2][0])


# A입력
input_txt = open("cramer_in.txt", 'r')
A = list()
A.append(list(map(int, input_txt.readline().split())))
A.append(list(map(int, input_txt.readline().split())))
n = len(A[0])
if n == 3:
    A.append(list(map(int, input_txt.readline().split())))
# B입력
B = list()
B.append(int(input_txt.readline()))
B.append(int(input_txt.readline()))
if n == 3:
    B.append(int(input_txt.readline()))
input_txt.close()
# det(A) 계산
if n == 2:
    det_A = get_det2(A)
else:
    det_A = get_det3(A)
# det(A1) 계산
if n == 2:
    det_A1 = get_det2([[B[0], A[0][1]],
                       [B[1], A[1][1]]])
else:
    det_A1 = get_det3([[B[0], A[0][1], A[0][2]],
                       [B[1], A[1][1], A[1][2]],
                       [B[2], A[2][1], A[2][2]]])
# det(A2) 계산
if n == 2:
    det_A2 = get_det2([[A[0][0], B[0]],
                      [A[1][0], B[1]]])
else:
    det_A2 = get_det3([[A[0][0], B[0], A[0][2]],
                       [A[1][0], B[1], A[1][2]],
                       [A[2][0], B[2], A[2][2]]])
# det(A3) 계산
if n == 3:
    det_A3 = get_det3([[A[0][0], A[0][1], B[0]],
                       [A[1][0], A[1][1], B[1]],
                       [A[2][0], A[2][1], B[2]]])
# x계산
x = list()
x.append(det_A1/det_A)
x.append(det_A2/det_A)
if n == 3:
    x.append(det_A3/det_A)
# 출력
output_txt = open("cramer_out.txt", 'w')
last_print = str()
for index, value in enumerate(x):
    last_print += "x"+str(index+1)+" = "+str(value)+"\n"
output_txt.write(last_print)
output_txt.close()
