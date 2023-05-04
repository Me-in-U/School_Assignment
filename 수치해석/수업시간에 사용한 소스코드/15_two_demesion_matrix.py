iter_letters = iter(['A', 'B', 'C'])
print(next(iter_letters))
print(next(iter_letters))
print(next(iter_letters))

enumerate_letters = enumerate(['A', 'B', 'C'])
print(next(enumerate_letters))
print(next(enumerate_letters))
print(next(enumerate_letters))



matrix = [['A', 'B', 'C'], ['D', 'E', 'F'], ['G', 'H', 'I']]


""" 일반적인 for 형태
for r in range(len(matrix)):
    for c in range(len(matrix[r])):
        print(r, c, matrix[r][c])
"""

""" 파이썬 스타일
for r, row in enumerate(matrix):
    for c, letter in enumerate(row):
        print(r, c, letter)
"""

