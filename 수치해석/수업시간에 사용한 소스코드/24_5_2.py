#%matplotlib inline
from numpy import array
import matplotlib.pyplot as plt 

height = array([ 8.2, 8.1, 9.1, 6.3, 7.6, 8.1, 6.5, 5.5, 9.1, 8.3, 2.2, 3.3, 4.4, 4.5, 4.6, 4.7, 12.5, 14.6, 18.9])

plt.hist(height, color='yellow', label='bins=10')
plt.hist(height, bins=30, color='black', label='bins=30')
plt.xlabel('Height')
plt.ylabel('Frequency')
plt.legend()
plt.show()
