import random
import secrets

choice = ['가위', '바위', '보']
print("가위, 바위, 보 중 하나를 선택하세요 (게임을 종료하려면 '종료'를 입력하세요)")
while True:
    userSelected = input("가위, 바위, 보! :  ")
    if userSelected == '종료':
        print("게임을 종료합니다.")
        break
    if userSelected not in choice:
        print("잘못된 입력입니다.")
        continue
    random.seed(secrets.randbelow(1000000))
    randomSelected = random.choice(choice)
    if userSelected == randomSelected:
        print("비겼습니다!")
    elif (userSelected == "가위" and randomSelected == "보") or \
            (userSelected == "바위" and randomSelected == "가위") or \
            (userSelected == "보" and randomSelected == "바위"):
        print("이겼습니다!")
    else:
        print("졌습니다!")
