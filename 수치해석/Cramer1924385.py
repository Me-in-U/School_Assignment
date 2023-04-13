# 2X2 Matrix det 계산
def get_det2(a):
    return a[0][0]*a[1][1] - a[0][1]*a[1][0]

# 3X3 Matrix det 계산


def get_det3(a):
    return (a[0][0]*(a[1][1]*a[2][2] - a[1][2]*a[2][1])
            - a[0][1]*(a[1][0]*a[2][2] - a[1][2]*a[2][0])
            + a[0][2]*(a[1][0]*a[2][1] - a[1][1]*a[2][0]))

# 3X3 Matrix det 계산 ver.2
# def get_det3_ver2(a):
#     return (a[0][0] * get_det2([a[1][1:], a[2][1:]])
#             - a[0][1] * get_det2([a[1][::2], a[2][::2]])
#             + a[0][2] * get_det2([a[1][:3], a[2][:3]]))


# 파일 open 'r', 'w'
input_txt = open("cramer_in.txt", 'r')
output_txt = open("cramer_out.txt", 'w')
# A입력
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
last_print = str()
for index, value in enumerate(x):
    last_print += "x"+str(index+1)+" = "+str(value)+"\n"
output_txt.write(last_print)
output_txt.close()
