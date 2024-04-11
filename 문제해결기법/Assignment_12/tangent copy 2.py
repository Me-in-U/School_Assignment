from functools import cmp_to_key
from turtle import color, width
import matplotlib.pyplot as plt
from scipy.spatial import ConvexHull


# Calculate the convex hull of a set of points
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


def orientation(a, b, c):
    res = (b[1] - a[1]) * (c[0] - b[0]) - (c[1] - b[1]) * (b[0] - a[0])
    if res == 0:
        return 0
    return 1 if res > 0 else -1


def findInitialPoints(a, b, a_is_above_b=False):
    if a_is_above_b:
        ia = min(range(len(a)), key=lambda i: a[i][1])
        ib = max(range(len(b)), key=lambda i: b[i][1])
    else:
        ia = max(range(len(a)), key=lambda i: a[i][0])
        ib = min(range(len(b)), key=lambda i: b[i][0])
    return ia, ib


def findTangent(a, b, lower=False, a_is_above_b=False):
    n1, n2 = len(a), len(b)
    init_a, init_b = findInitialPoints(a, b, a_is_above_b)

    index_a, index_b = init_a, init_b
    done = False
    while not done:
        done = True

        # Adjust for A's tangent point
        while True:
            next_inda = (
                index_a - 1 + n1) % n1 if lower else (index_a + 1) % n1
            ori = orientation(b[index_b], a[index_a], a[next_inda])
            if ori == 0:  # Collinear
                # Break immediately if next point is not closer, no need to check distance
                break
            elif (ori <= 0 if lower else ori >= 0):
                index_a = next_inda
                done = False
            else:
                break

        # Adjust for B's tangent point
        while True:
            next_indb = (
                index_b + 1) % n2 if lower else (index_b - 1 + n2) % n2
            ori = orientation(a[index_a], b[index_b], b[next_indb])
            if ori == 0:  # Collinear
                # Break immediately if next point is not closer, no need to check distance
                break
            elif (ori >= 0 if lower else ori <= 0):
                index_b = next_indb
                done = False
            else:
                break

        # If both tangents are found, we can exit the loop
        if done:
            break

    return a[index_a], b[index_b]


# Plotting function
def plot_polygons_and_tangents(a, b, upper_tangent, lower_tangent, convex_hull_a, convex_hull_b, merged_polygon):
    plt.figure()
    a_x, a_y = zip(*a)
    b_x, b_y = zip(*b)

    plt.plot(a_x + a_x[:1], a_y + a_y[:1], color='blue',
             linestyle='-', label='Polygon A')
    plt.fill(a_x, a_y, color='red', label='Polygon A', alpha=0.5)
    plt.plot(b_x + b_x[:1], b_y + b_y[:1],  color='blue',
             linestyle='-', label='Polygon B')
    plt.fill(b_x, b_y, color='orange', label='Polygon B', alpha=0.5)

    plt.plot(*zip(*convex_hull_a, convex_hull_a[0]), marker='o',
             linestyle='-', color='red', label='Convex Hull A')
    plt.plot(*zip(*convex_hull_b, convex_hull_b[0]), marker='o',
             linestyle='-', color='orange', label='Convex Hull B')

    plt.scatter(*zip(*a), c='blue')
    plt.scatter(*zip(*b), c='blue')

    plt.plot([upper_tangent[0][0], upper_tangent[1][0]], [
        upper_tangent[0][1], upper_tangent[1][1]], color='green',
        linestyle='-', label='Upper Tangent')
    plt.plot([lower_tangent[0][0], lower_tangent[1][0]], [
             lower_tangent[0][1], lower_tangent[1][1]], color='green',
             linestyle=':', label='Lower Tangent')

    x, y = zip(*merged_polygon)
    plt.plot(x + x[:1], y + y[:1], color='black',
             linestyle='-.', label='Merged Polygon')
    plt.fill(x, y, color='grey', label='Merged Polygon', alpha=0.5)
    # plt.scatter(x, y, c='black')

    plt.legend()
    plt.axis('equal')
    plt.show()


def merge_polygons_with_tangents(a, b, upper_tangent, lower_tangent):
    # Find the start and end indices for the tangents on each polygon
    upper_a_idx = a.index(upper_tangent[0])  # Upper tangent index on A
    upper_b_idx = b.index(upper_tangent[1])  # Upper tangent index on B

    lower_a_idx = a.index(lower_tangent[0])  # Lower tangent index on A
    lower_b_idx = b.index(lower_tangent[1])  # Lower tangent index on B

    # Initialize the merged polygon
    merged_polygon = []

    # Add points from polygon A between upper and lower tangents
    current_idx = upper_a_idx
    while True:
        merged_polygon.append(a[current_idx])
        if current_idx == lower_a_idx:
            break
        current_idx = (current_idx + 1) % len(a)

    # Add points from polygon B between lower and upper tangents
    current_idx = lower_b_idx
    while True:
        merged_polygon.append(b[current_idx])
        if current_idx == upper_b_idx:
            break
        current_idx = (current_idx + 1) % len(b)

    return merged_polygon


def determine_position(a, b):
    # Calculate the centroids of the polygons
    centroid_a = [sum(x) / len(a) for x in zip(*a)]
    centroid_b = [sum(x) / len(b) for x in zip(*b)]

    # Determine vertical and horizontal positioning
    a_is_above_b = centroid_a[1] > centroid_b[1]
    a_is_left_of_b = centroid_a[0] < centroid_b[0]

    return a_is_above_b, a_is_left_of_b


def polygon_area(points):
    N = len(points)
    area = 0
    j = N - 1
    for i in range(N):
        area += (points[i][0] * points[j][1]) - (points[i][1] * points[j][0])
        j = i
    area /= 2
    return abs(area)


# Main code
polygon_data = """
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

polygons = parse_polygon_data(polygon_data)
a, b = polygons
convex_hull_a, convex_hull_b = calculate_convex_hull(
    a), calculate_convex_hull(b)

a_is_above_b, a_is_left_of_b = determine_position(a, b)
print("1", a_is_above_b, a_is_left_of_b)
# if not a_is_above_b:
#     a, b = b, a
#     convex_hull_a, convex_hull_b = convex_hull_b, convex_hull_a
#     a_is_above_b, a_is_left_of_b = determine_position(a, b)
#     print("2", a_is_above_b, a_is_left_of_b)
# if not a_is_above_b and not a_is_left_of_b:
#     a, b = b, a
#     convex_hull_a, convex_hull_b = convex_hull_b, convex_hull_a
#     a_is_above_b, a_is_left_of_b = determine_position(a, b)
#     print("3", a_is_above_b, a_is_left_of_b)

# Find the upper and lower tangents
upper_tangent = findTangent(
    convex_hull_a, convex_hull_b, lower=False, a_is_above_b=a_is_above_b)
lower_tangent = findTangent(
    convex_hull_a, convex_hull_b, lower=True, a_is_above_b=a_is_above_b)
merged_polygon = merge_polygons_with_tangents(
    a, b, upper_tangent, lower_tangent)
print("poly 1 : ", a)
print("poly 2 : ", b)
print("Hull 1 :", convex_hull_a)
print("Hull 2 :", convex_hull_b)
print("Tangent Upper : ", upper_tangent)
print("Tangent Lower : ", lower_tangent)
print("merged_polygon: ", merged_polygon)
print(polygon_area(merged_polygon) - polygon_area(a) - polygon_area(b))


plot_polygons_and_tangents(
    a, b, upper_tangent, lower_tangent, convex_hull_a, convex_hull_b, merged_polygon)
