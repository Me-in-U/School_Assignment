# 데이터와 수치해석

## 1. 평균, 중앙값, 분산, 표준편차 구하기
![양윤상_1724443](https://user-images.githubusercontent.com/46489446/119249008-4b8c2700-bbd0-11eb-9677-aba78ad3fe1c.png)


## 2. 회귀법
- 관측된 데이터가 있을때 독립변수와 종속변수가 존재한다면 그 둘 사이의 관계를 가장 적절하게 표현할 수 있는 '직선' 또는 '곡선'을 나타내는 표현법


### 1) 최소 자승법(Least square method)
  - 회귀법 중 가장 널리 쓰임
  - 관측된 데이터와 회귀 직선(곡선)의 오차를 최소화 하도록 회귀 직선(곡선)의 형태를 정함
  - 회귀 직선 구하기
  
<img src="https://user-images.githubusercontent.com/46489446/119249325-9870fd00-bbd2-11eb-9f72-4b283a399e82.png" alt="drawing" width="500"/>

  1. 가장 근접한 직선(적합 방정식) 3개 그어보기

  - f(x) = 2x-1
  - f(x) = x+0.5
  - f(x) = 0.55x+1.5
  2. 직선 3개 중 어느 직선이 데이터 경향성을 가장 잘 표현하는지 오차를 계산한다.
  - 오차 = 참 값 - 적합방정식의 y값
  - ε = y - (ax+b)
  - 위 오차계산식을 통해 오차값 3개를 구한다. ε1, ε2, ε3
  3. 회귀법은 전체 오차의 값이 최소가 되도록 하는 미지수 a와 b를 구하는 문제이므로 오차들을 합하여 계산한다.
  - 단, 오차간 부호기 다를 수 있어 오차간의 상쇄 등의 이유로 왜곡 될 수 있다. (ex. ε1 = -1, ε2= 0 , ε3 = 1, ε1 + ε2 + ε3 = 0 ,오차가 0? X )
  - 즉 제곱하여 더한다. ε^2 =  ε1^2 +  ε2^2 +  ε3^2
  - 위의 수식에서 오차를 최소화 시키는 미지수(계수) a와 b를 찾기 위해 a 또는 b 에 대한 편미분을 통해 상대 변수의 영향력을 삭제해준다.
  - 또한 제곱을 없애 직선식으로 표현 가능하다.
  - 하나의 변수에 대한 해에 근접한 극한 값을 구한다.
<img width="500" alt="스크린샷 2021-05-23 오후 3 29 05" src="https://user-images.githubusercontent.com/46489446/119250530-a0816a80-bbdb-11eb-94a2-dd33963682c9.png">

### 2) 최소 자승법의 일반화

  - 회귀 직선을 임의로 f(x) = ax+b로 둔다
  - 회귀 직선을 주어진 점들과의 오차가 가장 작은 직선으로 하기 위해 a와 b값을 구하기 위한 식을 세운다.
  - <img width="247" alt="스크린샷 2021-05-30 오후 2 06 50" src="https://user-images.githubusercontent.com/46489446/120092762-49861300-c150-11eb-8a3a-ecd96cb86fd6.png">
  - <img width="378" alt="스크린샷 2021-05-30 오후 2 07 15" src="https://user-images.githubusercontent.com/46489446/120092766-59055c00-c150-11eb-9208-2d27dc0d3c51.png">
  - 위 식 2개 출처 : [네이버 캐스트](https://terms.naver.com/entry.naver?docId=3569970&cid=58944&categoryId=58970)
  - 주어진 점들을 통해서 y=ax+b를 구한다.
<img width="500" alt="1" src="https://user-images.githubusercontent.com/46489446/120092671-bf3daf00-c14f-11eb-8b60-24112d831001.png">

### 3) 다항식 회귀법
  - 데이터의 성향이 선형적이지 않은 경우 사용되는 비선형 회귀법 중 하나
    * 다항식 함수
    * 지수/로그함수
    * 삼각 함수
  - 2차 방정식 ax^2+bx+c가 있을때 전체 오차는 y-ax^2+bx+c이고 마찬가지로 오차를 제곱하여 계산한다.
  - a,b,c에 대해 각각 편미분 한 다음 equal 0 을 계산하면 아래와 같은 식을 도출해낼 수 있다.
<img width="500" alt="스크린샷 2021-06-08 오후 6 30 34" src="https://user-images.githubusercontent.com/46489446/121161100-9f149b00-c887-11eb-9268-cbf59d0f3a65.png">
  - 각각 시그마 값을 계산해서 연립방정식을 계산하여 다항식을 뽑아낸다.
<img width="500" alt="스크린샷 2021-06-08 오후 6 33 52" src="https://user-images.githubusercontent.com/46489446/121161565-15190200-c888-11eb-8cbc-0c0ad5ac3dba.png">
  - 지수 함수의 경우 양변에 자연 로그를 씌운 후 lnf(x) 를 Y로 치환하고 lnb를 B로 치환하여 계산한 그래프는 아래와 같다.
<img width="500" alt="스크린샷 2021-06-08 오후 10 18 58" src="https://user-images.githubusercontent.com/46489446/121192077-874d0f00-c8a7-11eb-99fd-b7399efc91a2.png">


## 3. 보간법
- 관측된 데이터를 가지고 알려지지 않은 새로운 데이터를 얻기 위함
- i) 데이터가 존재하지 않는 지점에서 정보를 얻고 싶을 경우
    - 1,3,8,9초에 대한 데이터가 존재할때 2초에 대한 데이터를 구하라
- ii) 함수가 매우 복잡해서 대수적 처리가 힘든 경우
      - 함수의 값을 근사하게 따라갈 수 있는 보다 간단한 함수들을 구성
      - 간단하게 계산
      - 수치적분

### 1) 선형보간법
  - 데이터 사이의 관계가 선형적이다.
  - 데이터의 한점과 다른 한 점 사이를 연결하는 선 -> 직선의 방정식과 유사
  - f(x) = f(x0) + (f(x1)-f(x0)/x1-x0)*(x-x0)
<img width="500" alt="스크린샷 2021-06-09 오전 1 23 30" src="https://user-images.githubusercontent.com/46489446/121222224-4f52c580-c8c1-11eb-9f24-ce963bbf00fd.png">
<img width="526" alt="스크린샷 2021-06-09 오전 1 24 52" src="https://user-images.githubusercontent.com/46489446/121222423-7f9a6400-c8c1-11eb-835c-7eab1b7dcdb9.png">
  - 선형 보간법은 구간의 크기를 작게 할 수록 보간값이 실제에 근사하다.
  - 함수에 대한 보간을 수행할 때에는 먼저 구간의 지점들을 규정하고 함수값을 구한 후 보간하는 방식을 사용한다.

### 2) 다항식 보간법
  - 실제 데이터에 근접한 관계를 부여하고자 할때 다항식을 이용한다.
  - Newton 다항식 보간법
  - 관측된 데이터에 대한 체계적인 보간함수 제공
  - 선형보간법부터 고차원 함수를 사용하는 보간까지 체계적 수행을 위한 방법
  - 차수에 따라 보간함수가 설정
  - f(x) = a0 + a1(x-x0) + a2(x-x0)(x-x1) + a3(x-x0)(x-x1)(x-x2) + ...
<img width="500" alt="스크린샷 2021-06-09 오전 1 27 38" src="https://user-images.githubusercontent.com/46489446/121222864-e1f36480-c8c1-11eb-8e46-c1f4ef12dd3b.png">
  - 선형 보간법에 비해 훨씬 근사한 보간법이다.


### 3) Lagrange 보간법
  - 다항식 보간법의 한 종류
  - 보간함수 계수를 규정해서 제공한다.(장점)
  - 데이터가 많은경우 보간함수 차수가 크게 늘어난다. (단점)
  - 즉 다량의 데이터 보간에는 비효율적이다.
  - f(x) = ~ L0(x)f(x0) + L1(x)f(x1) + L2(x)f(x2) + ...
  - Lj(x) = (x-x0)(x-x1)(x-x2)... / (xj-x0)(xj-x1)(xj-x2)... 
  - 분모가 0이 되지 않게 주의 (j !== n)
<img width="500" alt="스크린샷 2021-06-09 오전 11 41 07" src="https://user-images.githubusercontent.com/46489446/121284289-95d30f00-c917-11eb-8653-7218f45d2176.png">
  - 라그랑지 다항식은 일반적으로 다량의 데이터에 대한 보간보다는 특별한 경우에 국한
  - 유한 요소법의 형상함수 등을 정의할때 널리 사용
<img width="500" alt="스크린샷 2021-06-09 오전 11 46 10" src="https://user-images.githubusercontent.com/46489446/121284738-4b05c700-c918-11eb-94c3-18d717451e4b.png">
  - Newton-Cotes 적분 공식을 유도할때 유용하게 사용