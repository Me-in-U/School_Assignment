def calculate_cycle_length(n, cycle_length):
    if n < len(cycle_length) and cycle_length[n] != 0:
        return cycle_length[n]
    original_n = n
    cycle = 1
    while n != 1:
        if n % 2 == 0:
            n = n // 2
        else:
            n = 3 * n + 1
        cycle += 1
        if n < len(cycle_length) and cycle_length[n] != 0:
            cycle += cycle_length[n] - 1
            break
    if original_n < len(cycle_length):
        cycle_length[original_n] = cycle
    return cycle


def find_max_cycle_length(i, j, cycle_length):
    if i > j:
        i, j = j, i
    max_cycle_length = 0
    for k in range(i, j + 1):
        cycle_len = calculate_cycle_length(k, cycle_length)
        if cycle_len > max_cycle_length:
            max_cycle_length = cycle_len
    return max_cycle_length


MAX = 1000000
cycle_length = [0] * (MAX + 1)
with open("3nplus1.inp", "r") as inp_file, open("3nplus1.out", "w") as out_file:
    for line in inp_file:
        i, j = map(int, line.split())
        max_cycle = find_max_cycle_length(i, j, cycle_length)
        out_file.write(f"{i} {j} {max_cycle}\n")
