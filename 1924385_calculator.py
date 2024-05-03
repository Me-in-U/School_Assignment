def gugudan(dan):
    print(f"--- {dan}단 ---")
    for i in range(1, 10):
        print(f"{dan} x {i} = {dan*i}")
    print()


while (True):
    oper = (int)(input("숫자를 선택하세요(1: 한 단, 2. 구구단 전체, 0: 종료): "))
    if oper == 1:
        dan = int(input("출력하고 싶은 단을 입력하세요(1-9): "))
        if 1 <= dan <= 9:
            gugudan(dan)
        else:
            print("1에서 9 사이의 숫자를 입력해주세요.")
    elif oper == 2:
        for dan in range(1, 10):
            gugudan(dan)
    elif oper == 0:
        print("종료되었습니다.")
        break
    else:
        print("잘못된 입력입니다. 다시 입력해주세요.")

    while True:
        oper = input("계속 하시겠습니까?(y / n): ").lower()
        if oper == 'y':
            break
        elif oper == 'n':
            print("프로그램을 종료합니다.")
            exit()
        else:
            print("잘못된 입력입니다. 'y' 또는 'n'을 입력해주세요.")
