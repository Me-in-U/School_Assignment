from numpy import array

filename = 'test_data.txt'
with open(filename) as file_object:
    lines = file_object.read()

print(lines)