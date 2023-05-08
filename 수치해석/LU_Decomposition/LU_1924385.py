# !2X2 Matrix det 계산
def get_det2(a):
    return a[0][0]*a[1][1] - a[0][1]*a[1][0]

# !3X3 Matrix det 계산
def get_det3(a):
    return (a[0][0]*(a[1][1]*a[2][2] - a[1][2]*a[2][1])
            - a[0][1]*(a[1][0]*a[2][2] - a[1][2]*a[2][0])
            + a[0][2]*(a[1][0]*a[2][1] - a[1][1]*a[2][0]))

# !파일 open 'r', 'w'
input_txt = open("수치해석\LU_Decomposition\LU_in.txt", 'r')
output_txt = open("수치해석\LU_Decomposition\LU_out.txt", 'w', encoding="utf-8")
# !A입력
A = list()
while True:
    line = input_txt.readline()
    if line == "\n":
        break
    A.append(list(map(int, line.split())))
print("A행렬", A)
n = len(A[0])
# !B입력
B = list()
for i in range(n):
    B.append(list(map(int, input_txt.readline().split())))
print("B행렬", B)
# !L생성,U생성
L = list()
U = list()
for i in range(n):
    L.append([0] * n)
    U.append([0] * n)
    L[i][i] = 1
for i in range(n):
    U[0][i] = A[0][i]
    
    
    
#!출력
last_print = str()
last_print += "A        B\n"
for i in range(n):
    for j in range(n):
        last_print += str(A[i][j]) + " "
    last_print += "  "+ str(B[i][0])+"\n"
last_print += "\nL        U\n"
for i in range(n):
    for j in range(n):
        last_print += str(L[i][j]) + " "
    last_print += "   "
    for j in range(n):
        last_print += str(U[i][j]) + " "
    last_print += "\n"
output_txt.write(last_print)
print("L행렬", L)
print("U행렬", U)
