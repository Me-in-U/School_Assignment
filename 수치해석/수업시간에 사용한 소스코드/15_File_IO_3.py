filename = 'test_data.txt'
result_filename = 'out.txt'

with open(filename, encoding='ascii') as file_object:
    lines = file_object.readlines()
    file_object.close()


with open(result_filename, 'w') as output_object:
    output_object.writelines(lines)
    output_object.close()

#Cramer의 출력 결과를 파일로 저장
from numpy import array
from numpy.linalg import det

a = array([[4,1,-1],[3,-1,2],[-1,2,3]])
b = array([-2,1,1])
a1 = array([[-2,1,-1],[1,-1,2],[1,2,3]])
a2 = array([[4,-2,-1],[3,1,2],[-1,1,3]])
a3 = array([[4,1,-2],[3,-1,1],[-1,2,1]])
detA = det(a)
detA1 = det(a1)
detA2 = det(a2)
detA3 = det(a3)
print('|A| = ',detA, '|A1| = ',detA1,'|A2| = ',detA2,'|A3| = ',detA3)
x1 = detA1/detA
x2 = detA2/detA
x3 = detA3/detA
result = ['x1 = ', x1, 'x2 = ', x2, 'x3 =', x3]
sresult = str(result)
print('x1 = ',x1,'x2 = ',x2,'x3 =',x3) #결과 출력


result_filename2 = "out2.txt"
with open(result_filename2, 'w') as output_object2:
   output_object2.writelines(sresult)

   #for i in range(0,3):
   #    output_object2.write(result[i])



  #  output_object2.write(result)
  #  output_object2.close()