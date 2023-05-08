# !2X2 Matrix det 계산
def get_det2(a):
    return a[0][0]*a[1][1] - a[0][1]*a[1][0]

# !3X3 Matrix det 계산
def get_det3(a):
    return (a[0][0]*(a[1][1]*a[2][2] - a[1][2]*a[2][1])
            - a[0][1]*(a[1][0]*a[2][2] - a[1][2]*a[2][0])
            + a[0][2]*(a[1][0]*a[2][1] - a[1][1]*a[2][0]))

# !파일 open 'r', 'w'
input_txt = open("수치해석\Gaussian_elimination_method\gauss_in.txt", 'r')
output_txt = open(
    "수치해석\Gaussian_elimination_method\gauss_out.txt", 'w', encoding="utf-8")
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
#!소거
for i in range(n-1):
  for j in range(i+1, n):
    divider = A[j][i] / A[i][i]
    for k in range(n):
      A[j][k] -= divider * A[i][k]
    B[j][0] -= divider * B[i][0]
#!X계산
X = list()
for i in range(n):
    X.append([0] * n)
for i in range(n-1, 0, -1):
  
#!출력
last_print = str()
last_print += "A                 B\n"
for i in range(n):
    for j in range(n):
        last_print += str(A[i][j]) + " "
    last_print += "         "+ str(B[i][0])+"\n"
output_txt.write(last_print)
