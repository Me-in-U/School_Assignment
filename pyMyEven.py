
import random
import math


def trailing_zeros(x):
    """ Counting number of trailing zeros 
    in the binary representation of x."""
    if x == 0:
        return 1
    count = 0
    while x & 1 == 0:
        count += 1
        x >>= 1
    return count


def flajolet_martin(dataset, k):
    """Number of distinct elements using
    the Flajolet-Martin Algorithm."""
    max_zeros = 0
    for i in range(k):
        hash_vals = [trailing_zeros(random.choice(dataset))
                     for _ in range(len(dataset))]
        max_zeros = max(max_zeros, max(hash_vals))

    return 2 ** max_zeros


# Example
dataset = [2,
           10,
           34,
           1,
           4,
           7,
           8,
           6,
           3,
           144]
dist_num = flajolet_martin(dataset, 10)
print("Estimated number of distinct elements:", dist_num)
