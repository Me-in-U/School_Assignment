from numpy import array, linspace, zeros, sign, cos
import matplotlib.pyplot as plt


n = 5
e = zeros(n)
x = array([1.0, 1.7, 1.9449, 2.0021, 2.0121])
iter = array([0, 1, 2, 3, 4])


for i in range(n):
    e[i] = abs(((x[i] - x[i-1])/(x[i]))) * 100

# printing output
print("%5s %8s" % ('k', 'e'))
for k in range(n):
    #print("%5d %9.4f" % (k + 1, c[k]))
    if (k == 0):
        continue
    else:
        print("%5d %9.4f" % (k, e[k]))

plt.plot(iter, e, 'ko-')
plt.xlabel('x')
plt.ylabel('y')
plt.show()