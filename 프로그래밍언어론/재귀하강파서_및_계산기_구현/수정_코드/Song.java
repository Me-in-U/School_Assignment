import java.io.*;

class Calc_Song {
  int token;
  int pretoken;
  int value;
  int ch;
  int check;
  private PushbackInputStream input;
  final int NUMBER = 256;

  Calc_Song(PushbackInputStream is) {
    input = is;
  }

  int getToken() { /* tokens are characters */
    while (true) {
      try {
        ch = input.read();
        if (ch == ' ' || ch == '\t' || ch == '\r')
          ;
        else if (Character.isDigit(ch)) {
          value = number();
          input.unread(ch);
          return NUMBER;
        } else
          return ch;
      } catch (IOException e) {
        System.err.println(e);
      }
    }
  }

  void error() {
    System.out.printf("parse error : %d\n", ch);
    System.exit(1);
  }

  void match(int c) {
    if (token == c)
      token = getToken();
    else
      error();
  }

  void command() {
    /* command -> expr '\n' */
    int result = expr(); // Need to be changed
    // boolean b;

    if (result == 1 && check >= 1)
      System.out.println("True");
    else if (result == 1 && check == 0)
      System.out.println(result);
    else if (result == 0 && check >= 1)
      System.out.println("False");
    else if (result == 0 && check == 0)
      System.out.println(result);
    else if (token == '\n') /* end the parse and print the result */
      System.out.println(result);
    else
      error();
  }

  int expr() {
    /* <expr> → <bexp> {& <bexp> | ‘|’ <bexp>} | !<expr> | true | false */
    int result;
    int a = 0;
    if (token == '!') {
      match('!');
      return expr();
    } else if (token == 't') {
      match('t');
      match('r');
      match('u');
      match('e');
      return 1;
    } else if (token == 'f') {
      match('f');
      match('a');
      match('l');
      match('s');
      match('e');
      return 0;
    } else {
      result = bexp();
      while (token == '&') {
        match('&');
        a = bexp();
        if (result == 1 && a == 1)
          return 1;
        else
          return 0;
      }
      while (token == '|') {
        match('|');
        a = bexp();
        if (result == 1 || a == 1)
          return 1;
        else
          return 0;
      }
    }
    return result;
  }

  int bexp() {
    /* <bexp> → <aexp> [<relop> <aexp>] */
    int result = aexp();
    int a = 0;

    if (token == '=') {
      match('=');
      check += 1;
      if (token == '=') {
        match('=');
        a = aexp();
      }
      if (result == a)
        return 1;
      else
        return 0;
    } else if (token == '!') {
      match('!');
      check += 1;
      if (token == '=') {
        match('=');
      }
      a = aexp();
      if (result != a)
        return 1;
      else
        return 0;
    } else if (token == '>') {
      match('>');
      check += 1;

      if (token == '=') {
        match('=');
      }
      a = aexp();

      if (result >= a)
        return 1;
      else
        return 0;

    } else if (token == '<') {
      match('<');
      check += 1;

      if (token == '=') {
        match('=');
      }
      a = aexp();
      if (result >= a)
        return 1;
      else
        return 0;
    }
    return result;
  }

  int relop() {
    /* <relop> → '==' | '!=' | '<' | '>' | '<=' | '>=' */
    return 1;
  }

  int aexp() {
    /* <aexp> → <term> {+ <term> | - <term>} */
    int result = term();
    while (token == '+') {
      match('+');
      result += term();
    }
    while (token == '-') {
      match('-');
      result -= term();
    }
    return result;
  }

  int term() {
    /* <term> → <factor> {* <factor> | / <factor>} */
    int result = factor();
    while (token == '*') {
      match('*');
      result *= factor();
    }
    while (token == '/') {
      match('/');
      result /= factor();
    }
    return result;
  }

  int factor() {
    /* <factor> → [-] ( <number> | (<expr>) ) */
    int result = 0;
    if (token == '-') {
      match('-');
    }
    if (token == '(') {
      match('(');
      result = expr();
      match(')');
    } else if (token == NUMBER) {
      result = value;
      match(NUMBER); // token = getToken();
    }
    return result;
  }

  private int number() {
    /* number -> digit { digit } */
    int result = ch - '0';
    try {
      ch = input.read();
      while (Character.isDigit(ch)) {
        result = 10 * result + ch - '0';
        ch = input.read();
      }
    } catch (IOException e) {
      System.err.println(e);
    }
    return result;
  }

  void parse() {
    token = getToken(); // get the first token
    command(); // call the parsing command
  }

  public static void main(String args[]) {
    Calc_Song calc_song = new Calc_Song(new PushbackInputStream(System.in));
    while (true) {
      System.out.print(">> ");
      calc_song.parse();
    }
  }
}