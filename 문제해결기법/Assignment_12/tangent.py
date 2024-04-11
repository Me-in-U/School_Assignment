from functools import cmp_to_key
import matplotlib.pyplot as plt
from scipy.spatial import ConvexHull

# 볼록 껍질 계산 함수


def calculate_convex_hull(points):
    hull = ConvexHull(points)
    hull_points = [points[i] for i in hull.vertices]
    return hull_points


# 새로운 입력 데이터
new_data = """
48
-4578 20594
-4578 18253
-12588 18253
-12588 10
-3430 10
-3430 19486
-2336 19486
-2336 6892
0 6892
0 2259
10196 2259
10196 10283
0 10283
0 8071
-1152 8071
-1152 15988
0 15988
0 13755
10196 13755
10196 17124
-1152 17124
-1152 19486
5643 19486
5643 21773
10196 21773
10196 36608
3316 36608
3316 21773
4515 21773
4515 20594
2253 20594
2253 29685
-2336 29685
-2336 30847
-1152 30847
-1152 36608
-11422 36608
-11422 30847
-3430 30847
-3430 27456
-12588 27456
-12588 21773
0 21773
0 27456
-2336 27456
-2336 28559
1080 28559
1080 20594
48
-4578 -16024
-4578 -18365
-12588 -18365
-12588 -36608
-3430 -36608
-3430 -17132
-2336 -17132
-2336 -29726
0 -29726
0 -34359
10196 -34359
10196 -26335
0 -26335
0 -28547
-1152 -28547
-1152 -20630
0 -20630
0 -22863
10196 -22863
10196 -19494
-1152 -19494
-1152 -17132
5643 -17132
5643 -14845
10196 -14845
10196 -10
3316 -10
3316 -14845
4515 -14845
4515 -16024
2253 -16024
2253 -6933
-2336 -6933
-2336 -5771
-1152 -5771
-1152 -10
-11422 -10
-11422 -5771
-3430 -5771
-3430 -9162
-12588 -9162
-12588 -14845
0 -14845
0 -9162
-2336 -9162
-2336 -8059
1080 -8059
1080 -16024
"""

# 입력 데이터를 줄 단위로 나누기
new_lines = new_data.split("\n")

# 첫 번째 다각형
n1 = int(new_lines[0])  # 첫 번째 다각형의 점의 수
new_polygon_a = [list(map(int, line.split())) for line in new_lines[1:n1+1]]

# 두 번째 다각형
n2_start_line = n1 + 1
n2 = int(new_lines[n2_start_line])  # 두 번째 다각형의 점의 수
new_polygon_b = [list(map(int, line.split()))
                 for line in new_lines[n2_start_line+1:n2_start_line+1+n2]]
# determines the quadrant of a point (used in compare())


def quad(p):
    if p[0] >= 0 and p[1] >= 0:
        return 1
    if p[0] <= 0 and p[1] >= 0:
        return 2
    if p[0] <= 0 and p[1] <= 0:
        return 3
    return 4

# Checks whether the line is crossing the polygon


def orientation(a, b, c):
    res = (b[1] - a[1]) * (c[0] - b[0]) - (c[1] - b[1]) * (b[0] - a[0])
    if res == 0:
        return 0
    if res > 0:
        return 1
    return -1

# compare function for sorting


def compare(p1, q1):
    p = [p1[0] - mid[0], p1[1] - mid[1]]
    q = [q1[0] - mid[0], q1[1] - mid[1]]
    one = quad(p)
    two = quad(q)

    if one != two:
        return 1 if one > two else -1
    return -1 if p[1] * q[0] < q[1] * p[0] else 1
# 수정된 findUpperTangent 함수로 상단 접선의 좌표를 반환


def findUpperTangent(a, b):
    global mid
    n1, n2 = len(a), len(b)
    mid = [0, 0]
    maxa, minb = float('-inf'), float('inf')

    for i in range(n1):
        maxa = max(maxa, a[i][0])
        mid[0] += a[i][0]
        mid[1] += a[i][1]
        a[i][0] *= n1
        a[i][1] *= n1
    a = sorted(a, key=cmp_to_key(compare))
    for i in range(n1):
        a[i][0] /= n1
        a[i][1] /= n1

    mid = [0, 0]
    for i in range(n2):
        minb = min(minb, b[i][0])
        mid[0] += b[i][0]
        mid[1] += b[i][1]
        b[i][0] *= n2
        b[i][1] *= n2
    b = sorted(b, key=cmp_to_key(compare))
    for i in range(n2):
        b[i][0] /= n2
        b[i][1] /= n2

    if minb < maxa:
        a, b = b, a
        n1, n2 = n2, n1

    ia, ib = 0, 0
    for i in range(1, n1):
        if a[i][0] > a[ia][0]:
            ia = i
    for i in range(1, n2):
        if b[i][0] < b[ib][0]:
            ib = i

    inda, indb = ia, ib
    done = 0
    while not done:
        done = 1
        while orientation(b[indb], a[inda], a[(inda+1) % n1]) >= 0:
            inda = (inda + 1) % n1
        while orientation(a[inda], b[indb], b[(n2+indb-1) % n2]) <= 0:
            indb = (n2 + indb - 1) % n2
            done = 0

    return (a[inda][0], a[inda][1], b[indb][0], b[indb][1])
# 수정된 findLowerTangent 함수로 하단 접선의 좌표를 반환


def findLowerTangent(a, b):
    global mid
    n1, n2 = len(a), len(b)
    mid = [0, 0]
    maxa, minb = float('-inf'), float('inf')

    for i in range(n1):
        maxa = max(maxa, a[i][0])
        mid[0] += a[i][0]
        mid[1] += a[i][1]
        a[i][0] *= n1
        a[i][1] *= n1
    a = sorted(a, key=cmp_to_key(compare))
    for i in range(n1):
        a[i][0] /= n1
        a[i][1] /= n1

    mid = [0, 0]
    for i in range(n2):
        minb = min(minb, b[i][0])
        mid[0] += b[i][0]
        mid[1] += b[i][1]
        b[i][0] *= n2
        b[i][1] *= n2
    b = sorted(b, key=cmp_to_key(compare))
    for i in range(n2):
        b[i][0] /= n2
        b[i][1] /= n2

    if minb < maxa:
        a, b = b, a
        n1, n2 = n2, n1

    ia, ib = 0, 0
    for i in range(1, n1):
        if a[i][0] < a[ia][0]:
            ia = i
    for i in range(1, n2):
        if b[i][0] > b[ib][0]:
            ib = i

    inda, indb = ia, ib
    done = 0
    while not done:
        done = 1
        while orientation(b[indb], a[inda], a[(inda-1+n1) % n1]) <= 0:
            inda = (inda - 1 + n1) % n1
        while orientation(a[inda], b[indb], b[(indb+1) % n2]) >= 0:
            indb = (indb + 1) % n2
            done = 0

    return (a[inda][0], a[inda][1], b[indb][0], b[indb][1])


# 두 다각형에 대한 볼록 껍질 계산
convex_hull_a = calculate_convex_hull(new_polygon_a)
convex_hull_b = calculate_convex_hull(new_polygon_b)

# 볼록 껍질을 사용하여 상단 접선 찾기
upper_tangent_convex_hull = findUpperTangent(convex_hull_a, convex_hull_b)
# 볼록 껍질을 사용하여 하단 접선 찾기
lower_tangent_convex_hull = findLowerTangent(convex_hull_a, convex_hull_b)

# 다각형과 상단 접선 시각화
plt.figure(figsize=(10, 6))
plt.plot(*zip(*new_polygon_a, new_polygon_a[0]), marker='o', label='Polygon A')
plt.plot(*zip(*new_polygon_b, new_polygon_b[0]), marker='o', label='Polygon B')
plt.plot(*zip(*convex_hull_a, convex_hull_a[0]), marker='o',
         linestyle='-', color='blue', label='Convex Hull A')
plt.plot(*zip(*convex_hull_b, convex_hull_b[0]), marker='o',
         linestyle='-', color='orange', label='Convex Hull B')

plt.plot([upper_tangent_convex_hull[0], upper_tangent_convex_hull[2]], [
         upper_tangent_convex_hull[1], upper_tangent_convex_hull[3]], 'r--', label='Upper Tangent')
plt.plot([lower_tangent_convex_hull[0], lower_tangent_convex_hull[2]], [
         lower_tangent_convex_hull[1], lower_tangent_convex_hull[3]], 'g--', label='Lower Tangent')
plt.fill(*zip(*new_polygon_a, new_polygon_a[0]), alpha=0.3)
plt.fill(*zip(*new_polygon_b, new_polygon_b[0]), alpha=0.3)
plt.legend()
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Upper Tangent of Two Polygons')
plt.show()
