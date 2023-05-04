from numpy import zeros, array, cos, sin
import matplotlib.pyplot as plt

def f(x):
    return 3*x - cos(x) - 1
def df(x):
    return 3 + sin(x)

n = 7
x0 = 0.0
x = zeros(n)
E = zeros(n)
E2 = zeros(n)
x[0] = x0
itl = range(0,7)
it = array(itl)

for k in range(n-1):
    x[k+1] = x[k] - f(x[k]) / df(x[k])
    E[k] = ((x[k] - x[k - 1]) / x[k]) * 100

#printing output
print("%5s %8s" % ('k', 'x'))
for k in range(n-1):
    #print("%4d %9.4f" % (k+1, x[k]))
    print("%4d %9.4f" % (k + 1, E[k+1]))

for j in range(n-1):
    E2[j] = E[j+1]

plt.plot(it, E2, 'ko-')
plt.xlabel('Iteration')
plt.ylabel('x')
plt.show()