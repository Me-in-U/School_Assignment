timeit("hoare_partition(arr.copy(), 0, len(arr.copy()) - 1, 0)",
                      globals=globals(), number=repeat_time)
print(f'Center Pivot : {time0:.6f} seconds')
time1 = timeit.timeit("hoare_partition(arr.copy(), 0, len(arr.copy()) - 1, 1)",
                      globals=globals(), number=repeat_time)
print(f'Median of three : {time1:.6f} seconds')
time2 = timeit.timeit("hoare_partition(arr.copy(), 0, len(arr.copy()) - 1, 2)",
                      globals=globals(), number=repeat_time)
print(f'Median of medians : {time2:.6f} seconds')