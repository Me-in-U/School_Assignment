from numpy import zeros, array
import matplotlib.pyplot as plt
def g(x):
    return 1.0/6.0*(x**2 + 5)

n = 15
x0 = 3.0
x = zeros(n)
E = zeros(n)
x[0] = x0
itl = range(1,16)
it = array(itl)

for i in range(n-1):
    x[i+1] = g(x[i])
    E[i] = ((x[i] - x[i-1]) / x[i]) * 100

#printing output
print("%5s %8s" % ('k', 'x'))
for k in range(n):
   # print("%4d %9.4f" % (k+1, x[k]))
    print("%4d %9.4f" % (k+1, E[k]))

plt.plot(it, E, 'ko-')
plt.xlabel('Iteration')
plt.ylabel('E')
plt.show()