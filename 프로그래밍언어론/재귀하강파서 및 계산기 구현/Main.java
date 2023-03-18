import java.io.*;

class Main {
    public static PushbackInputStream input;

    public static class Calc {
        int token;
        int value;
        int ch;
        boolean isBoolean = false;
        static final int NUMBER = 256;
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
            // 에러가 났으면 이미 멈췄겠지만,,
            System.out.printf("syntax error : DEX[%d] CHAR[%c]%n", token, token);
        }

        void match(int c) {
            if (token == c)
                token = getToken();
            else
                error();
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
            } else
                error();
        }

        int expr() {
            /* <expr> → <bexp> {& <bexp> | ‘|’ <bexp>} | !<expr> | true | false */
            int resultBefore;
            if (token == '!') {
                match('!');
                resultBefore = NOT;
                resultBefore = (expr() == TRUE) ? FALSE : TRUE;
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
            /* <factor> → [-] ( <number> | (<aexp>) ) */
            /* <aexp> -> <expr> */
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
            if (isMinus) {
                return -1 * result;
            } else {
                return result;
            }
        }

        void parse() {
            token = getToken(); // get the first token
            command(); // call the parsing command
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        RDParser syntaxCheck = new RDParser();
        Calc calc = new Calc();
        while (true) {
            System.out.print(">> ");
            String inputString = br.readLine() + '\n';
            if (inputString.equals("end")) {
                break;
            }
            input = new PushbackInputStream(new ByteArrayInputStream(inputString.getBytes()));
            syntaxCheck.parse();
            input = new PushbackInputStream(new ByteArrayInputStream(inputString.getBytes()));
            calc.parse();
            calc.isBoolean = false;
        }
    }

    public static class RDParser {

        int token, ch;

        void error() {
            System.out.printf("syntax error : DEX[%d] CHAR[%c]%n", token, token);
            System.exit(1);
        }

        void command() {
            /* command -> expr '\n' */
            expr();
            if (token == '\n') /* end the parse and print the result */ {
                // System.out.println("good syntax");
            } else
                error();
        }

        void match(int c) {
            if (token == c)
                token = getToken();
            else
                error();
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
                    term();
                } else {
                    match('-');
                    term();
                }
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
            if (Character.isDigit(token))
                match(token);
            else
                error();
        }

        int getToken() {
            while (true) {
                try {
                    ch = input.read();
                    // System.out.println("token: " + ch);
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
    }

}