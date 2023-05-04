from numpy import array, linspace, zeros, sign, cos
import matplotlib.pyplot as plt


n = 6
e = zeros(n)
c = zeros(n)
x = array([1.6, 1.33, 1.34, 1.331, 1.33, 1.33])
iter = array([0, 1, 2, 3, 4, 5])
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