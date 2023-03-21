
import java.io.*;

class Calc {
    int token;
    int value;
    int ch;
    boolean isBoolean = false;
    private PushbackInputStream input;
    static final int NUMBER = 256; // number
    static final int EQ = 257; // equals
    static final int NEQ = 258; // not equals
    static final int ST = 259; // smaller than
    static final int BT = 260; // bigger than
    static final int SEQT = 261; // smaller and equals than
    static final int BEQT = 262; // bigger and equals than
    static final int NOT = 263;
    static final int AND = 264;
    static final int OR = 265;
    static final int TRUE = 266;
    static final int FALSE = 267;

    Calc(PushbackInputStream is) {
        input = is;
    }

    public static void main(String[] args) {
        Calc calc = new Calc(new PushbackInputStream(System.in));
        while (true) {
            System.out.print(">> ");
            calc.parse();
            calc.isBoolean = false;
        }
    }

    void match(int c) {
        if (token == c) {
            token = getToken();
        } else {
            error();
        }
    }

    void parse() {
        token = getToken(); // get the first token
        command(); // call the parsing command
    }

    int getToken() { /* tokens are characters */
        while (true) {
            try {
                ch = input.read();
                if (ch == ' ' || ch == '\t' || ch == '\r') {
                    continue;
                } else if (Character.isDigit(ch)) {
                    value = number();
                    input.unread(ch);
                    return NUMBER;
                } else {
                    return ch;
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    void command() {
        /* command -> expr '\n' */
        int result = expr();
        if (token == '\n') /* end the parse and print the result */ {
            if (!isBoolean) {
                System.out.println(result);
            } else {
                if (result == TRUE) {
                    System.out.println("true");
                } else {
                    System.out.println("false");
                }
            }
        } else {
            error();
        }
    }

    int expr() {
        /* <expr> → <bexp> {& <bexp> | ‘|’ <bexp>} | !<expr> | true | false */
        int resultBefore;
        if (token == '!') {
            match('!');
            isBoolean = true;
            resultBefore = NOT;
            int resultAfter = expr();
            if (resultAfter == TRUE) {
                resultBefore = FALSE;
            } else if (resultAfter == FALSE) {
                resultBefore = TRUE;
            } else { // !숫자만
                resultBefore = FALSE;
            }
        } else if (token == 't' || token == 'f') {
            isBoolean = true;
            if (token == 't') {
                match('t');
                match('r');
                match('u');
                match('e');
                resultBefore = TRUE;
            } else {
                match('f');
                match('a');
                match('l');
                match('s');
                match('e');
                resultBefore = FALSE;
            }
        } else {
            resultBefore = bexp();
            while (token == '&' || token == '|') {
                int saveToken = token;
                if (token == '&') {
                    match('&');
                } else {
                    match('|');
                }
                int resultAfter = bexp();
                if (saveToken == '&') {
                    resultBefore = (resultAfter == FALSE || resultBefore == FALSE) ? FALSE : TRUE;
                } else {
                    resultBefore = (resultAfter == FALSE && resultBefore == FALSE) ? FALSE : TRUE;
                }
            }
        }
        return resultBefore;
    }

    int bexp() {
        /* <bexp> → <aexp> [<relop> <aexp>] */
        int resultBefore = aexp();
        if (token == '=' || token == '!' || token == '<' || token == '>') {
            isBoolean = true;
            int savedToken = relop();
            int resultAfter = aexp();
            if (savedToken == EQ) {
                resultBefore = (resultBefore == resultAfter) ? TRUE : FALSE;
            } else if (savedToken == NEQ) {
                resultBefore = (resultBefore != resultAfter) ? TRUE : FALSE;
            } else if (savedToken == ST) {
                resultBefore = (resultBefore < resultAfter) ? TRUE : FALSE;
            } else if (savedToken == BT) {
                resultBefore = (resultBefore > resultAfter) ? TRUE : FALSE;
            } else if (savedToken == SEQT) {
                resultBefore = (resultBefore <= resultAfter) ? TRUE : FALSE;
            } else if (savedToken == BEQT) {
                resultBefore = (resultBefore >= resultAfter) ? TRUE : FALSE;
            }
        }
        return resultBefore;
    }

    int relop() {
        /* <relop> → '==' | '!=' | '<' | '>' | '<=' | '>=' */
        switch (token) {
            case '=':
                match('=');
                match('=');
                return EQ;
            case '!':
                match('!');
                match('=');
                return NEQ;
            case '<':
                match('<');
                if (token == '=') {
                    match('=');
                    return SEQT;
                }
                return ST;
            case '>':
                match('>');
                if (token == '=') {
                    match('=');
                    return BEQT;
                }
                return BT;
            default:
                error();
                return 0;
        }
    }

    int aexp() {
        /* <aexp> → <term> {+ <term> | - <term>} */
        int result = term();
        while (token == '+' || token == '-') {
            if (token == '+') {
                match('+');
                result += term();
            } else {
                match('-');
                result -= term();
            }
        }
        return result;
    }

    int term() {
        /* <term> → <factor> {* <factor> | / <factor>} */
        int result = factor();
        while (token == '*' || token == '/') {
            if (token == '*') {
                match('*');
                result *= factor();
            } else {
                match('/');
                result /= factor();
            }
        }
        return result;
    }

    int factor() {
        /* <factor> → [-] ( <number> | (<expr>) ) */
        int result = 0;
        boolean isMinus = false;
        if (token == '-') {
            match('-');
            isMinus = true;
        }
        if (token == NUMBER) {
            result = value;
            match(NUMBER); // token = getToken();
        } else if (token == '(') {
            match('(');
            result = expr();
            match(')');
        } else {
            error();
        }
        return isMinus ? (-1 * result) : result;
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

    void error() {
        System.out.printf("syntax error : DEX[%d] CHAR[%c]%n", token, token);
        System.exit(1);
    }
}