from functools import cmp_to_key
from turtle import color
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


def findInitialPoints(a, b, vertical=False):
    if vertical:
        ia = max(range(len(a)), key=lambda i: a[i][1])
        ib = min(range(len(b)), key=lambda i: b[i][1])
    else:
        ia = max(range(len(a)), key=lambda i: a[i][0])
        ib = min(range(len(b)), key=lambda i: b[i][0])
    return ia, ib


def findTangent(a, b, lower=False, vertical=False):
    n1, n2 = len(a), len(b)
    ia, ib = findInitialPoints(a, b, vertical)

    inda, indb = ia, ib
    done = False
    while not done:
        done = True

        # Adjust for A's tangent point
        while True:
            next_inda = (inda - 1 + n1) % n1 if lower else (inda + 1) % n1
            ori = orientation(b[indb], a[inda], a[next_inda])
            if ori == 0:  # Collinear
                # Break immediately if next point is not closer, no need to check distance
                break
            elif (ori < 0 if lower else ori > 0):
                inda = next_inda
                done = False
            else:
                break

        # Adjust for B's tangent point
        while True:
            next_indb = (indb + 1) % n2 if lower else (indb - 1 + n2) % n2
            ori = orientation(a[inda], b[indb], b[next_indb])
            if ori == 0:  # Collinear
                # Break immediately if next point is not closer, no need to check distance
                break
            elif (ori > 0 if lower else ori < 0):
                indb = next_indb
                done = False
            else:
                break

        # If both tangents are found, we can exit the loop
        if done:
            break

    return a[inda], b[indb]


# Plotting function
def plot_polygons_and_tangents(a, b, upper_tangent, lower_tangent, convex_hull_a, convex_hull_b, merged_polygon):
    plt.figure()
    a_x, a_y = zip(*a)
    b_x, b_y = zip(*b)

    plt.plot(a_x + a_x[:1], a_y + a_y[:1], 'b-', label='Polygon A')
    plt.fill(a_x, a_y, color='red', label='Polygon A', alpha=0.5)
    plt.plot(b_x + b_x[:1], b_y + b_y[:1], 'b-', label='Polygon B')
    plt.fill(b_x, b_y, color='orange', label='Polygon B', alpha=0.5)

    plt.plot(*zip(*convex_hull_a, convex_hull_a[0]), marker='o',
             linestyle='-', color='red', label='Convex Hull A')
    plt.plot(*zip(*convex_hull_b, convex_hull_b[0]), marker='o',
             linestyle='-', color='orange', label='Convex Hull B')

    plt.scatter(*zip(*a), c='blue')
    plt.scatter(*zip(*b), c='blue')

    plt.plot([upper_tangent[0][0], upper_tangent[1][0]], [
        upper_tangent[0][1], upper_tangent[1][1]], 'g--', label='Upper Tangent')
    plt.plot([lower_tangent[0][0], lower_tangent[1][0]], [
             lower_tangent[0][1], lower_tangent[1][1]], 'g--', label='Lower Tangent')

    x, y = zip(*merged_polygon)
    plt.plot(x + (x[0],), y + (y[0],), 'r-',
             color='black', label='Merged Polygon')
    plt.fill(x, y, color='grey', label='Merged Polygon', alpha=0.5)
    # plt.scatter(x, y, c='black')

    plt.legend()
    plt.axis('equal')
    plt.show()


def merge_polygons_with_tangents(a, b, upper_tangent, lower_tangent):
    # Find the start and end indices for the tangents on each polygon
    ua_idx = a.index(upper_tangent[0])  # Upper tangent index on A
    ub_idx = b.index(upper_tangent[1])  # Upper tangent index on B
    la_idx = a.index(lower_tangent[0])  # Lower tangent index on A
    lb_idx = b.index(lower_tangent[1])  # Lower tangent index on B

    # Initialize the merged polygon
    merged_polygon = []

    # Add points from polygon A between upper and lower tangents
    current_idx = ua_idx
    while True:
        merged_polygon.append(a[current_idx])
        if current_idx == la_idx:
            break
        current_idx = (current_idx - 1) % len(a)

    # Add points from polygon B between lower and upper tangents
    current_idx = lb_idx
    while True:
        merged_polygon.append(b[current_idx])
        if current_idx == ub_idx:
            break
        current_idx = (current_idx - 1) % len(b)

    # Return to the starting point to close the polygon
    merged_polygon.append(upper_tangent[0])

    return merged_polygon


def determine_position(a, b):
    # Calculate the centroids of the polygons
    centroid_a = [sum(x) / len(a) for x in zip(*a)]
    centroid_b = [sum(x) / len(b) for x in zip(*b)]

    # Determine vertical and horizontal positioning
    vertical = centroid_a[1] > centroid_b[1]
    left_of_b = centroid_a[0] < centroid_b[0]

    return vertical, left_of_b


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
4
1246 1246
-1246 1246
-1246 -1246
1246 -1246
3
1869 0
4362 0
1869 249
"""

polygons = parse_polygon_data(polygon_data)
a, b = polygons

# Main code to determine the position and decide if we need to swap polygons
vertical, left_of_b = determine_position(a, b)
print(vertical, left_of_b)
# Swap polygons if necessary to ensure 'a' is left of 'b' or 'a' is below 'b'
if not left_of_b:
    a, b = b, a

convex_hull_a = calculate_convex_hull(a)
convex_hull_b = calculate_convex_hull(b)

# Find the upper and lower tangents
upper_tangent = findTangent(
    convex_hull_a, convex_hull_b, lower=False, vertical=vertical)
lower_tangent = findTangent(
    convex_hull_a, convex_hull_b, lower=True, vertical=vertical)

# if vertical:
#     upper_tangent, lower_tangent = lower_tangent, upper_tangent

merged_polygon = merge_polygons_with_tangents(
    a, b, upper_tangent, lower_tangent)

print(polygon_area(merged_polygon))


plot_polygons_and_tangents(
    a, b, upper_tangent, lower_tangent, convex_hull_a, convex_hull_b, merged_polygon)
