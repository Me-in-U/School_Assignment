package 프로그래밍언어론.언어S_파서_구현;

// Token.java
import java.lang.Enum;

enum Token {
    // 타입
    BOOL("bool"), TRUE("true"), FALSE("false"), VOID("void"),
    CHAR("char"), INT("int"), FLOAT("float"), STRING("string"),
    // if
    IF("if"), ELSE("else"), THEN("then"),
    // let in end
    LET("let"), IN("in"), END("end"),
    // while
    WHILE("while"), DO("do"),
    // etc
    RETURN("return"), FUN("fun"),
    READ("read"), PRINT("print"), FOR("for"),
    // 구분자
    LBRACE("{"), RBRACE("}"), LBRACKET("["), RBRACKET("]"),
    LPAREN("("), RPAREN(")"), SEMICOLON(";"), COMMA(","), EOF("<<EOF>>"),
    // 연산자
    ASSIGN("="), EQUAL("=="), LT("<"), LTEQ("<="), GT(">"),
    GTEQ(">="), NOT("!"), NOTEQ("!="), PLUS("+"), MINUS("-"),
    MULTIPLY("*"), DIVIDE("/"), AND("&"), OR("|"),
    // ""
    ID(""), NUMBER(""), STRLITERAL("");

    private String value;

    private Token(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public Token setValue(String v) {
        this.value = v;
        return this;
    }

    public static Token idORkeyword(String name) {
        for (Token token : Token.values()) {
            if (token.value().equals(name))
                return token;
            if (token == Token.EOF)
                break;
        }
        return ID.setValue(name);
    } // keyword or ID
}