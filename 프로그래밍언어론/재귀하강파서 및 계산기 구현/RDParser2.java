
import java.io.*;

/**
 * Recursive Descent Parser of simple expression
 * 
 * EBNF of our grammar
 * <command> -> <expr>\n
 * <expr> -> <bexp> {& <bexp> | '|' <bexp>} | !<expr> | true | false
 * <bexp> -> <aexp> [<relop> <aexp>]
 * <relop> -> '==' | '!=' | '<' | '>' | '<=' | '>='
 * <aexp> -> <term> {+ <term> | - <term>}
 * <term> -> <factor> {* <factor> | / <factor>}
 * <factor> -> [-] ( <number> | (<expr>) )
 * <number> -> <digit> {<digit>}
 * <digit> -> 0|1|2|3|4|5|6|7|8|9
 * 
 * 
 * @author Jangwu Jo
 *
 */
public class RDParser2 {

  int token;
  int ch;
  private PushbackInputStream input;

  RDParser2(PushbackInputStream is) {
    input = is;
  }

  void error() {
    System.out.printf("syntax error : DEX[%d] CHAR[%c]%n", token, token);
    System.exit(1);
  }

  void command() {
    /* command -> expr '\n' */
    expr();
    if (token == '\n') /* end the parse and print the result */
      System.out.println("good syntax");
    else
      error();
  }

  void match(int c) {
    if (token == c) {
      token = getToken();
    } else {
      error();
    }
  }

  void expr() {
    /* <expr> → <bexp> {& <bexp> | ‘|’ <bexp>} | !<expr> | true | false */
    if (token == '!') {
      match('!');
      expr();
    } else if (token == 't' || token == 'f') {
      if (token == 't') {
        match('t');
        match('r');
        match('u');
        match('e');
      } else {
        match('f');
        match('a');
        match('l');
        match('s');
        match('e');
      }
    } else {
      bexp();
      while (token == '&' || token == '|') {
        if (token == '&') {
          match('&');
        } else {
          match('|');
        }
        bexp();
      }
    }
  }

  void bexp() {
    /* <bexp> → <aexp> [<relop> <aexp>] */
    aexp();
    if (token == '=' || token == '!' || token == '<' || token == '>') {
      relop();
      aexp();
    }
  }

  void relop() {
    /* <relop> → '==' | '!=' | '<' | '>' | '<=' | '>=' */
    switch (token) {
      case '=':
        match('=');
        match('=');
        break;
      case '!':
        match('!');
        match('=');
        break;
      case '<':
        match('<');
        if (token == '=')
          match('=');
        break;
      case '>':
        match('>');
        if (token == '=')
          match('=');
        break;
      default:
        error();
    }
  }

  void aexp() {
    /* <aexp> → <term> {+ <term> | - <term>} */
    term();
    while (token == '+' || token == '-') {
      if (token == '+') {
        match('+');
      } else {
        match('-');
      }
      term();
    }
  }

  void term() {
    /* <term> → <factor> {* <factor> | / <factor>} */
    factor();
    while (token == '*' || token == '/') {
      if (token == '*') {
        match('*');
      } else {
        match('/');
      }
      factor();
    }
  }

  void factor() {
    /* <factor> → [-] ( <number> | (<expr>) ) */
    if (token == '-') {
      match('-');
    }
    if (token == '(') {
      match('(');
      expr();
      match(')');
    } else {
      number();
    }
  }

  void number() {
    /* <number> → <digit> {<digit>} */
    digit();
    while (Character.isDigit(token)) {
      digit();
    }
  }

  void digit() {
    /* <digit> -> 0|1|...|9 */
    if (Character.isDigit(token)) {
      match(token);
    } else {
      error();
    }
  }

  int getToken() {
    while (true) {
      try {
        ch = input.read();
        if (!(ch == ' ' || ch == '\t' || ch == '\r')) {
          return ch;
        }
      } catch (IOException e) {
        System.err.println(e);
      }
    }
  }

  void parse() {
    token = getToken(); // get the first character
    command(); // call the parsing command
  }

  public static void main(String[] args) {
    RDParser2 parser = new RDParser2(new PushbackInputStream(System.in));
    while (true) {
      System.out.print(">> ");
      parser.parse();
    }
  }
}