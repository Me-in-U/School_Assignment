# 2X2 Matrix det 계산
def get_det2(a):
    return a[0][0]*a[1][1] - a[0][1]*a[1][0]

# 3X3 Matrix det 계산


def get_det3(a):
    return (a[0][0]*(a[1][1]*a[2][2] - a[1][2]*a[2][1])
            - a[0][1]*(a[1][0]*a[2][2] - a[1][2]*a[2][0])
            + a[0][2]*(a[1][0]*a[2][1] - a[1][1]*a[2][0]))


# 파일 open 'r', 'w'
input_txt = open("수치해석\Gaussian_elimination_method\LU_in.txt", 'r')
output_txt = open(
    "수치해석\Gaussian_elimination_method\LU_out.txt", 'w', encoding="utf-8")
last_print = str()
# A입력
A = list()
A.append(list(map(int, input_txt.readline().split())))
n = len(A[0])
for i in range(n-1):
    A.append(list(map(int, input_txt.readline().split())))
print("A행렬", A)
# B입력
B = list()
for i in range(n):
    B.append(list(map(int, input_txt.readline().split())))
print("B행렬", B)
# L생성,U생성
L = list()
U = list()
for i in range(n):
    L.append([0] * n)
    U.append([0] * n)
    L[i][i] = 1
for i in range(n):
    U[0][i] = A[0][i]


print("L행렬", L)
print("U행렬", U)
