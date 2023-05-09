# !파일 open 'r', 'w'
input_txt = open("수치해석\LU_Decomposition\LU_in.txt", 'r')
output_txt = open("수치해석\LU_Decomposition\LU_out.txt", 'w', encoding="utf-8")
# !A,B입력
A = []
while (line := input_txt.readline().strip()):
    A.append(list(map(float, line.split())))
n = len(A)
B = [list(map(float, input_txt.readline().split())) for _ in range(n)]
input_txt.close()
# !L,U생성
L = [[0] * n for _ in range(n)]
U = A
for i in range(n): L[i][i] = 1
# !가우스 소거 이용해서 L,U 값 넣기
for i in range(n-1):
  for j in range(i+1, n):
    divider = U[j][i] / U[i][i]
    L[j][i] = divider
    for k in range(n):
      U[j][k] -= divider * U[i][k]
#!Ly = b
Y = [0] * n
for i in range(n):
    Y[i] = B[i][0] - sum(L[i][j] * Y[j] for j in range(i))
# !Ux = Y
X = [0] * n
for i in range(n-1, -1, -1):
    X[i] = (Y[i] - sum(U[i][j] * X[j] for j in range(i+1, n))) / U[i][i]
#!출력
str_L = [list(map(str, row)) for row in L]
str_U = [list(map(str, row)) for row in U]
max_length_L = max(len(s) for row in str_L for s in row)
max_length_U = max(len(s) for row in str_U for s in row)
format_str_L = f"{{:>{max_length_L}}}"
format_str_U = f"{{:>{max_length_U}}}"
last_print =  "L 행렬\n"
for i in range(n):
    last_print += " ".join([format_str_L.format(s) for s in str_L[i]]) + '\n'
last_print += "U 행렬\n"
for i in range(n):
    last_print += " ".join([format_str_U.format(s) for s in str_U[i]]) + '\n'
last_print += f"\ny {Y}\n\n"
last_print += '\n'.join([f"X{i+1} = {X[i]}" for i in range(n)])
output_txt.write(last_print)
output_txt.close()
print(last_print)