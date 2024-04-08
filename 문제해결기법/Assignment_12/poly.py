# 필요한 라이브러리를 불러옵니다.
import matplotlib.pyplot as plt

# 파일에 저장된 두 다각형의 좌표를 읽습니다.
# 임시로 여기서 문자열로 좌표를 정의하지만, 실제로는 파일에서 읽어옵니다.

polygon_data = """
10
-12 50
0 23
14 7
6 8
10 -3
22 5
20 0
33 5
25 20
12 25
8
100 20
120 30
135 76
111 45
121 99
131 100
120 110
100 100
"""

# 문자열 데이터를 처리하여 두 다각형의 좌표 목록을 생성합니다.
lines = polygon_data.strip().split("\n")
polygons = []
current_polygon = []

for line in lines:
    if line.isdigit():  # 새로운 다각형의 시작
        if current_polygon:  # 현재 다각형이 비어있지 않다면 polygons 리스트에 추가
            polygons.append(current_polygon)
            current_polygon = []
    else:
        # 현재 다각형에 좌표 추가
        x, y = map(int, line.split())
        current_polygon.append((x, y))

# 마지막 다각형 추가
if current_polygon:
    polygons.append(current_polygon)

# 처음 점과 마지막 점을 이어주기 위해, 각 다각형의 좌표 리스트에 첫 번째 좌표를 다시 추가합니다.
# 이렇게 하면 plt.plot() 함수가 자동으로 처음 점과 마지막 점을 이어줍니다.

plt.figure(figsize=(10, 5))

for polygon in polygons:
    # 처음 점을 마지막에 다시 추가
    polygon_closed = polygon + [polygon[0]]
    y, x = zip(*polygon_closed)  # 세로(x)와 가로(y)의 역할을 바꿉니다.
    plt.plot(x, y, marker='o')
    plt.fill(x, y, alpha=0.2)

plt.axis('equal')
plt.gca().invert_yaxis()  # y축을 반전시킵니다.
plt.show()
