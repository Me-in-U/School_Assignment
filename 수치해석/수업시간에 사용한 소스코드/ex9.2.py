from numpy import array, linspace, zeros, sign, cos
import matplotlib.pyplot as plt


def f(x):
    return -x ** 2 - 6.0 * x - 5.0

a = -2.0
b = 3.0

#x = array([1, 2, 3, 4, 5, 6, 7])
x = array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15])
n = len(x)
c = zeros(n)

for i in range(n):
    c[i] = (a + b) / 2.0
    if sign(f(c[i])) == sign(f(a)):
        a = c[i]
    else:
        b = c[i]
# printing output
print("%5s %8s" % ('k', 'c'))
for k in range(n):
    print("%5d %9.4f" % (k + 1, c[k]))

plt.plot(x, c, 'ko-')
plt.xlabel('x')
plt.ylabel('y')
plt.show() 