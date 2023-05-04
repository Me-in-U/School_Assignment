from numpy import zeros
import matplotlib.pyplot as plt

m = 8
it = range(m)
x1j = zeros(m)
x2j = zeros(m)
x3j = zeros(m)
e1 = zeros(m)
e2 = zeros(m)
e3 = zeros(m)

print('%7s %9s %9s %9s' % ('k','x1','x2','x3'))
print('%7d %9.5f %9.5f %9.5f' % (0,x1j[0],x2j[0],x3j[0]))

for k in range(m-1):
    x1j[k+1] = (-1.0 + 2.0*x2j[k] - 3.0*x3j[k])/5.0
    x2j[k+1] = (2.0 + 3.0*x1j[k] - x3j[k])/9.0
    x3j[k+1] = (-3.0 + 2.0*x1j[k] - x2j[k])/7.0
    e1[k + 1] = (abs(x1j[k + 1] - abs(x1j[k])) / abs(x1j[k + 1])) * 100
    e2[k + 1] = (abs(x2j[k + 1] - abs(x2j[k])) / abs(x2j[k + 1])) * 100
    e3[k + 1] = (abs(x3j[k + 1] - abs(x3j[k])) / abs(x3j[k + 1])) * 100
    print('%7d %9.5f %9.5f %9.5f' % (k+1, e1[k+1], e2[k+1], e3[k+1]))

#for k in range(m-1):
  #  print('%9.5f %9.5f %9.5f' % (x1j[k], x2j[k], x3j[k]))

plt.plot(it, e1, 'ko-', label='x1')
plt.plot(it, e2, 'k--', label='x2')
plt.plot(it, e3, 'kv-', label='x3')
plt.legend()
plt.xlabel('iteration')
plt.ylabel('Approxiimate Solutions')
plt.show()
