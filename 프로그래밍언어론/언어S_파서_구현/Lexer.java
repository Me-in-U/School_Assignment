package 프로그래밍언어론.언어S_파서_구현;

// Lexical analyzer for S

import java.io.*;

public class Lexer {
    private char ch = ' ';
    private BufferedReader input;
    private final char eolnCh = '\n';
    private final char eofCh = '\004';
    static boolean interactive = false;

    public Lexer(String fileName) { // source filename
        try {
            // System.out.println("input = new BufferedReader(new FileReader(" + fileName +
            // "))");
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }

    public Lexer() { // from standard input
        // System.out.println("input = new BufferedReader(new
        // InputStreamReader(System.in))");
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    private char getchar() { // Return next char
        int c = 0;
        try {
            c = input.read();
            if (c == -1)
                c = eofCh;
        } catch (IOException e) {
            System.err.println(e);
        }
        return (char) c;
    }

    public Token getToken() { // Return next token
        do {
            // 읽은 문자가 알파벳 문자
            if (Character.isLetter(ch)) { // ident or keyword
                String s = "";
                do {
                    s += ch;
                    ch = getchar();
                } while (Character.isLetter(ch) || Character.isDigit(ch));
                return Token.idORkeyword(s);
            }
            // 읽은 문자가 숫자 : 정수리터럴
            if (Character.isDigit(ch)) { // number
                String s = "";
                do {
                    s += ch;
                    ch = getchar();
                } while (Character.isDigit(ch));
                return Token.NUMBER.setValue(s);
            }
            // 나머지는 읽은 문자에 따라 연산자, 구분자 등을 인식하여 리턴
            switch (ch) {
                case ' ':
                case '\t':
                case '\r':
                    ch = getchar();
                    break;
                case eolnCh:
                    ch = getchar();
                    if (ch == '\r') // for Windows
                        ch = getchar(); // for Windows
                    if (ch == eolnCh && interactive)
                        return Token.EOF;
                    break;
                case '/': // divide
                    ch = getchar();
                    if (ch != '/')
                        return Token.DIVIDE;
                    do {
                        ch = getchar();
                    } while (ch != eolnCh);
                    ch = getchar();
                    break;
                case '\"': // string literal
                    String s = "";
                    while ((ch = getchar()) != '\"')
                        s += ch;
                    ch = getchar();
                    return Token.STRLITERAL.setValue(s);
                case eofCh:
                    return Token.EOF;
                case '+':
                    ch = getchar();
                    return Token.PLUS;
                case '-':
                    ch = getchar();
                    return Token.MINUS;
                case '*':
                    ch = getchar();
                    return Token.MULTIPLY;
                case '(':
                    ch = getchar();
                    return Token.LPAREN;
                case ')':
                    ch = getchar();
                    return Token.RPAREN;
                case '{':
                    ch = getchar();
                    return Token.LBRACE;
                case '}':
                    ch = getchar();
                    return Token.RBRACE;
                case '[':
                    ch = getchar();
                    return Token.LBRACKET;
                case ']':
                    ch = getchar();
                    return Token.RBRACKET;
                case ';':
                    ch = getchar();
                    return Token.SEMICOLON;
                case ',':
                    ch = getchar();
                    return Token.COMMA;
                case '&':
                    ch = getchar();
                    return Token.AND;
                case '|':
                    ch = getchar();
                    return Token.OR;
                case '=':
                    ch = getchar();
                    if (ch != '=')
                        return Token.ASSIGN;
                    else {
                        ch = getchar();
                        return Token.EQUAL;
                    }
                case '<':
                    ch = getchar();
                    if (ch != '=')
                        return Token.LT;
                    else {
                        ch = getchar();
                        return Token.LTEQ;
                    }
                case '>':
                    ch = getchar();
                    if (ch != '=')
                        return Token.GT;
                    else {
                        ch = getchar();
                        return Token.GTEQ;
                    }
                case '!':
                    ch = getchar();
                    if (ch != '=')
                        return Token.NOT;
                    else {
                        ch = getchar();
                        return Token.NOTEQ;
                    }
            } // switch
        } while (true);
    } // next

    public void error(String msg) {
        System.err.println("Error: " + msg);
        // System.exit(1);
    }

    public static void main(String[] args) {
        Lexer lexer;
        if (args.length == 0) {
            System.out.println("Lexer()");
            lexer = new Lexer();
        } else {
            System.out.println("new Lexer(args[0])");
            lexer = new Lexer(args[0]);
        }

        Token tok = lexer.getToken();
        while (tok != Token.EOF) {
            System.out.println(tok.toString());
            tok = lexer.getToken();
        }
    } // main
}