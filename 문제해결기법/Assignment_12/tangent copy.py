from functools import cmp_to_key
import matplotlib.pyplot as plt
from scipy.spatial import ConvexHull

# 볼록 껍질 계산 함수


def calculate_convex_hull(points):
    hull = ConvexHull(points)
    hull_points = [points[i] for i in hull.vertices]
    return hull_points


# Parsing input data
def parse_polygon_data(data):
    lines = data.strip().split('\n')
    polygons = []
    i = 0
    while i < len(lines):
        n = int(lines[i])
        i += 1
        poly = []
        for j in range(n):
            x, y = map(int, lines[i+j].split())
            poly.append([x, y])
        polygons.append(poly)
        i += n
    return polygons


mid = [0, 0]


def quad(p):
    if p[0] >= 0 and p[1] >= 0:
        return 1
    if p[0] <= 0 and p[1] >= 0:
        return 2
    if p[0] <= 0 and p[1] <= 0:
        return 3
    return 4


def orientation(a, b, c):
    res = (b[1] - a[1]) * (c[0] - b[0]) - (c[1] - b[1]) * (b[0] - a[0])
    return 0 if res == 0 else (1 if res > 0 else -1)


def compare(p1, q1):
    p = [p1[0] - mid[0], p1[1] - mid[1]]
    q = [q1[0] - mid[0], q1[1] - mid[1]]
    one, two = quad(p), quad(q)
    if one != two:
        return -1 if one < two else 1
    return -1 if p[1] * q[0] < q[1] * p[0] else 1


def preparePolygon(poly, n):
    global mid
    mid = [0, 0]
    extreme = float('-inf') if poly == a else float('inf')
    for i in range(n):
        extreme = max(extreme, poly[i][0]) if poly == a else min(
            extreme, poly[i][0])
        mid[0] += poly[i][0]
        mid[1] += poly[i][1]
        poly[i][0] *= n
        poly[i][1] *= n
    poly.sort(key=cmp_to_key(compare))
    for i in range(n):
        poly[i][0] /= n
        poly[i][1] /= n


def findTangent(a, b, lower=False):
    global mid
    n1, n2 = len(a), len(b)
    preparePolygon(a, n1)
    preparePolygon(b, n2)

    if isAPolygonRightOfB(a, b):
        a, b = b, a  # Ensure polygon 'a' is to the left of 'b'
        n1, n2 = n2, n1

    ia, ib = findLeftmostPoints(a, b)
    inda, indb = ia, ib
    done = False
    while not done:
        done = True
        if lower:
            while orientation(b[indb], a[inda], a[(inda - 1 + n1) % n1]) <= 0:
                inda = (inda - 1 + n1) % n1
            while orientation(a[inda], b[indb], b[(indb + 1) % n2]) >= 0:
                indb = (indb + 1) % n2
                done = False
        else:
            while orientation(b[indb], a[inda], a[(inda + 1) % n1]) >= 0:
                inda = (inda + 1) % n1
            while orientation(a[inda], b[indb], b[(indb - 1 + n2) % n2]) <= 0:
                indb = (indb - 1 + n2) % n2
                done = False

    return a[inda], b[indb]


def isAPolygonRightOfB(a, b):
    maxa = max(point[0] for point in a)
    minb = min(point[0] for point in b)
    return minb < maxa


def findLeftmostPoints(a, b):
    ia = max(range(len(a)), key=lambda i: a[i][0])
    ib = min(range(len(b)), key=lambda i: b[i][0])
    return ia, ib

# Plotting function


def plot_polygons_and_tangents(a, b, upper_tangent, lower_tangent, convex_hull_a, convex_hull_b):
    plt.figure()
    a_x, a_y = zip(*a)
    b_x, b_y = zip(*b)

    plt.plot(a_x + a_x[:1], a_y + a_y[:1], 'b-', label='Polygon A')
    plt.plot(b_x + b_x[:1], b_y + b_y[:1], 'g-', label='Polygon B')
    plt.plot([upper_tangent[0][0], upper_tangent[1][0]], [
             upper_tangent[0][1], upper_tangent[1][1]], 'r--', label='Upper Tangent')
    plt.plot([lower_tangent[0][0], lower_tangent[1][0]], [
             lower_tangent[0][1], lower_tangent[1][1]], 'y--', label='Lower Tangent')

    plt.plot(*zip(*convex_hull_a, convex_hull_a[0]), marker='o',
             linestyle='-', color='blue', label='Convex Hull A')
    plt.plot(*zip(*convex_hull_b, convex_hull_b[0]), marker='o',
             linestyle='-', color='orange', label='Convex Hull B')
    plt.scatter(*zip(*a), c='blue')
    plt.scatter(*zip(*b), c='green')
    plt.legend()
    plt.axis('equal')
    plt.show()


# Main code
polygon_data = """
5
4 5
4 7
8 9
2 8
2 7
4
6 2
6 4
4 4
4 2
"""

polygons = parse_polygon_data(polygon_data)
a, b = polygons

# 두 다각형에 대한 볼록 껍질 계산
convex_hull_a = calculate_convex_hull(a)
convex_hull_b = calculate_convex_hull(b)

upper_tangent = findTangent(convex_hull_a, convex_hull_b, lower=False)
lower_tangent = findTangent(convex_hull_a, convex_hull_b, lower=True)

plot_polygons_and_tangents(
    a, b, upper_tangent, lower_tangent, convex_hull_a, convex_hull_b)
