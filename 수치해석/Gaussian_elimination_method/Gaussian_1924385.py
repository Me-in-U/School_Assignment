def pivoting(p):
  for i in range(p,n):
   for j in range(i,n):
     if abs(A[j][i]) > abs(A[i][i]):
        A[i], A[j] = A[j],A[i]
        B[i], B[j] = B[j],B[i]

# !파일 open 'r', 'w'
input_txt = open("수치해석\Gaussian_elimination_method\gauss_in.txt", 'r')
output_txt = open(
    "수치해석\Gaussian_elimination_method\gauss_out.txt", 'w', encoding="utf-8")
# !A,B입력
A = []
while (line := input_txt.readline().strip()):
    A.append(list(map(float, line.split())))
n = len(A)
B = [list(map(float, input_txt.readline().split())) for _ in range(n)]
input_txt.close()
# !소거
for i in range(n-1):
  pivoting(i)
  for j in range(i+1, n):
    divider = A[j][i] / A[i][i]
    for k in range(n):
      A[j][k] -= divider * A[i][k]
    B[j][0] -= divider * B[i][0]
# !X계산
X = [0] * n
for i in range(n-1, -1, -1):
    X[i] = (B[i][0] - sum(A[i][j] * X[j] for j in range(i+1, n))) / A[i][i]
# !출력
last_print = "Gauss 소거법 완료 후 행렬(피봇팅 적용)\n"
str_list = [list(map(str, row)) for row in A]
max_length = max(len(s) for row in str_list for s in row)
format_stL = f"{{:>{max_length}}}"
for i in range(n):
    last_print += " ".join([format_stL.format(s) for s in str_list[i]]) + '\n'
last_print += '\n'
last_print += '\n'.join([f"X{i+1} = {X[i]}" for i in range(n)])
output_txt.write(last_print)
output_txt.close()
print(last_print)