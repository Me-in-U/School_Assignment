from numpy import zeros, array
import matplotlib.pyplot as plt
def f(x):
    return -x**2 + 6.0*x - 5.0
def df(x):
    return -2.0*x + 6.0

n = 7
x0 = -2.0
x = zeros(n)
x[0] = x0
itl = range(0,7)
it = array(itl)

for k in range(n-1):
    x[k+1] = x[k] - f(x[k]) / df(x[k])

#printing output
print("%5s %8s" % ('k', 'x'))
for k in range(n):
    print("%4d %9.4f" % (k+1, x[k]))

plt.plot(it, x, 'ko-')
plt.xlabel('Iteration')
plt.ylabel('x')
plt.show() 