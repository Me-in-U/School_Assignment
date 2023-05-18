package 프로그래밍언어론.언어S_인터프리터_구현_배열추가;

// Parser for language S
public class Parser {
    Token token; // current token
    Lexer lexer;
    String funId = "";

    public Parser(Lexer scan) {
        lexer = scan;
        token = lexer.getToken(); // get the first token
    }

    private String match(Token t) {
        String value = token.value();
        if (token == t)
            token = lexer.getToken();
        else
            error(t);
        return value;
    }

    private void error(Token tok) {
        System.err.println("Syntax error: " + tok + " --> " + token);
        token = lexer.getToken();
    }

    private void error(String tok) {
        System.err.println("Syntax error: " + tok + " --> " + token);
        token = lexer.getToken();
    }

    public Command command() {
        // <command> -> <decl> | <function> | <stmt>
        if (isType()) {
            Decl d = decl();
            return d;
        }

        if (token == Token.FUN) {
            Function f = function();
            return f;
        }

        if (token != Token.EOF) {
            Stmt s = stmt();
            return s;
        }
        return null;
    }

    private Decls decls() {
        // <decls> -> {<decl>}
        Decls ds = new Decls();
        while (isType()) {
            Decl d = decl();
            ds.add(d);
        }
        return ds;
    }

    // Todo : 배열 구현을 위해 수정 o
    private Decl decl() {
        // <decl> -> <type> id [=<expr>];
        // 타입 type
        Type t = type();
        // 변수 이름 id
        String id = match(Token.ID);
        Decl d = null;
        // if 다음에 = 이 오면
        if (token == Token.ASSIGN) {
            match(Token.ASSIGN);
            Expr e = expr();
            d = new Decl(id, t, e);
        } else if (token == Token.LBRACKET) {
            match(Token.LBRACKET);
            String n = match(Token.NUMBER);
            match(Token.RBRACKET);
            d = new Decl(id, t, Integer.parseInt(n));
        } else
            d = new Decl(id, t);
        // 마지막에 ; 오는지
        if (token == Token.SEMICOLON) {
            match(Token.SEMICOLON);
        }
        return d;
    }

    private Functions functions() {
        // <decls> -> {<decl>}
        Functions fs = new Functions();
        while (token == Token.FUN) {
            Function f = function();
            fs.add(f);
        }
        return fs;
    }

    private Function function() {
        // <function> -> fun <type> id(<params>) <stmt>
        match(Token.FUN);
        Type t = type();
        String str = match(Token.ID);
        funId = str;
        Function f = new Function(str, t);
        match(Token.LPAREN);
        f.params = params();
        match(Token.RPAREN);
        Stmt s = stmt();
        f.stmt = s;
        return f;
    }

    private Decls params() {
        Decls params = new Decls();
        while (isType() || !isType()) {
            if (isType()) {
                Type t = type();
                Decl d = new Decl(match(Token.ID), t);
                params.add(d);
            } else {
                Decl d = new Decl(match(Token.ID), null);
                params.add(d);
            }
            if (token == Token.RPAREN) {
                break;
            } else if (token == Token.COMMA) {
                match(Token.COMMA);
            }
        }
        return params;
    }

    private Type type() {
        // <type> -> int | bool | void | string
        Type t = null;
        switch (token) {
            case INT:
                t = Type.INT;
                break;
            case BOOL:
                t = Type.BOOL;
                break;
            case VOID:
                t = Type.VOID;
                break;
            case STRING:
                t = Type.STRING;
                break;
            default:
                error("int | bool | void | string");
        }
        match(token);
        return t;
    }

    private Stmt stmt() {
        // <stmt> -> <block> | <assignment> | <ifStmt> | <whileStmt> | ...
        // -> read id; | print <expr>;
        Stmt s = new Empty();
        switch (token) {
            case SEMICOLON:
                match(token.SEMICOLON);
                return s;
            case LBRACE:
                match(Token.LBRACE);
                s = stmts();
                match(Token.RBRACE);
                return s;
            case IF: // if statement
                s = ifStmt();
                return s;
            case WHILE, FOR, DO: // while statement
                s = whileStmt();
                return s;
            case ID: // assignment
                s = assignment();
                return s;
            case LET: // let statement
                s = letStmt();
                return s;
            case READ: // read statement
                s = readStmt();
                return s;
            case PRINT: // print statment
                s = printStmt();
                return s;
            case RETURN: // return statement
                s = returnStmt();
                return s;
            default:
                error("Illegal stmt");
                return null;
        }
    }

    private Stmts stmts() {
        // <block> -> {<stmt>}
        Stmts ss = new Stmts(); // 빈 복합문 AST 생성
        while ((token != Token.RBRACE) && (token != Token.END))
            ss.stmts.add(stmt()); // 문장 파싱하고 그 AST를 복합문 AST에 추가
        return ss; // 복합문 AST 리턴
    }

    private Let letStmt() {
        // <letStmt> -> let <decls> in <block> end
        match(Token.LET);// let 토큰 매치
        Decls ds = decls();// 변수 선언 파싱
        Functions fs = null;
        if (token == Token.FUN) {
            fs = functions();// fun 선언 파싱
        }
        match(Token.IN);// in 토큰 매치
        Stmts ss = stmts();// 본체 문장들 파싱
        match(Token.END);// end 토큰 매치
        match(Token.SEMICOLON);// 세미콜론 매치
        if (fs != null) {
            return new Let(ds, fs, ss);// AST 구성 및 리턴
        }
        return new Let(ds, ss);
    }

    private Read readStmt() {
        // <readStmt> -> read id;
        match(Token.READ);
        Identifier id = new Identifier(match(Token.ID));
        match(Token.SEMICOLON);
        return new Read(id);
    }

    private Print printStmt() {
        // <printStmt> -> print <expr>;
        match(Token.PRINT);
        Expr e = expr();
        match(Token.SEMICOLON);
        return new Print(e);
    }

    private Return returnStmt() {
        // <returnStmt> -> return <expr>;
        match(Token.RETURN);
        Expr e = expr();
        match(Token.SEMICOLON);
        return new Return(funId, e);
    }

    // Todo : 배열 구현을 위해 수정
    private Stmt assignment() {
        // <assignment> -> id = <expr>;
        Identifier id = new Identifier(match(Token.ID));

        if (token == Token.LPAREN) // function call
            return call(id);
        else if (token == Token.LBRACKET) {// array access
            match(Token.LBRACKET);
            Expr indexExpr = expr();
            match(Token.RBRACKET);
            Array array = new Array(id, indexExpr);
            match(Token.ASSIGN);
            Expr valueExpr = expr();
            if (token == Token.SEMICOLON)
                match(Token.SEMICOLON);
            return new Assignment(array, valueExpr);
        }
        match(Token.ASSIGN);
        Expr e = expr();
        if (token == Token.SEMICOLON)
            match(Token.SEMICOLON);
        return new Assignment(id, e);
    }

    private Call call(Identifier id) {
        // <call> -> id(<expr>{,<expr>});
        Exprs args;
        match(Token.LPAREN);
        args = arguments();
        match(Token.RPAREN);
        return new Call(id, args);
    }

    private If ifStmt() {
        // <ifStmt> -> if (<expr>) then <stmt> [else <stmt>]
        match(Token.IF);
        match(Token.LPAREN);
        Expr e = expr();
        match(Token.RPAREN);
        match(Token.THEN);
        Stmt s1 = stmt();
        Stmt s2 = new Empty();
        // else가 있는지 확인
        if (token == Token.ELSE) {
            match(Token.ELSE);
            s2 = stmt();
        }
        return new If(e, s1, s2);
    }

    private While whileStmt() {
        While w = null;
        // <whileStmt> -> while (<expr>) <stmt>
        if (token == Token.WHILE) {
            match(Token.WHILE);
            match(Token.LPAREN);
            Expr e = expr();
            match(Token.RPAREN);
            Stmt s = stmt();
            w = new While(e, s, 0);
        }
        // <whileStmt> -> do <stmt> while (<expr>)
        else if (token == Token.DO) {
            match(Token.DO);
            Stmt s = stmt();
            match(Token.WHILE);
            match(Token.LPAREN);
            Expr e = expr();
            match(Token.RPAREN);
            w = new While(e, s, 1);
        }
        // <whileStmt> -> for (<type> id = <expr>; <expr>; id = <expr>) <stmt>
        else if (token == Token.FOR) {
            match(Token.FOR);
            match(Token.LPAREN);
            Decl a = decl();
            Expr b = expr();
            match(Token.SEMICOLON);
            Identifier id = new Identifier(match(Token.ID));
            match(Token.ASSIGN);
            Expr e = expr();
            Stmt st2 = new Assignment(id, e);
            match(Token.RPAREN);
            Stmt st1 = stmt();
            w = new While(a, b, st1, st2);
        }
        return w;
    }

    private Expr expr() {
        // <expr> -> <bexp> {& <bexp> | '|'<bexp>} | !<expr> | true | false
        switch (token) {
            case NOT:
                Operator op = new Operator(match(token));
                Expr e = expr();
                return new Unary(op, e);
            case TRUE:
                match(Token.TRUE);
                return new Value(true);
            case FALSE:
                match(Token.FALSE);
                return new Value(false);
        }

        Expr e = bexp();

        // parse logical operations
        while (token == Token.AND || token == Token.OR) {
            Operator op = new Operator(match(token));
            Expr right = bexp();
            e = new Binary(op, e, right);
        }

        return e;
    }

    private Expr bexp() {
        // <bexp> -> <aexp> [ (< | <= | > | >= | == | !=) <aexp> ]
        Expr e = aexp();

        // parse relational operations
        if (token == Token.LT || token == Token.LTEQ ||
                token == Token.GT || token == Token.GTEQ ||
                token == Token.EQUAL || token == Token.NOTEQ) {
            Operator op = new Operator(match(token));
            Expr right = aexp();
            e = new Binary(op, e, right);
        }

        return e;
    }

    private Expr aexp() {
        // <aexp> -> <term> { + <term> | - <term> }
        Expr e = term(); // 첫번째 항(term ) 파실
        while (token == Token.PLUS || token == Token.MINUS) {// + 혹은 -
            Operator op = new Operator(match(token));// 연산자 매치
            Expr t = term();// 다음 항(term) 파싱
            e = new Binary(op, e, t);// 수식 AST 구성
        }
        return e;// 수식 AST 리턴
    }

    private Expr term() {
        // <term> -> <factor> { * <factor> | / <factor>}
        Expr t = factor(); // 첫번째 인수(factor) 파싱
        while (token == Token.MULTIPLY || token == Token.DIVIDE) {// *혹은 /
            Operator op = new Operator(match(token));// 연산자 매치
            Expr f = factor();// 다음 인수(factor)파실
            t = new Binary(op, t, f); // 항의 AST 구성
        }
        return t;// 항의 AST 리턴
    }

    // Todo : 배열 구현을 위해 수정
    private Expr factor() {
        // <factor> -> [-](id | <call> | literal | '('<expr> ')')
        Operator op = null;
        if (token == Token.MINUS)
            op = new Operator(match(Token.MINUS));// 단항 - 연산자 매치

        Expr e = null;
        switch (token) {
            case ID:
                Identifier v = new Identifier(match(Token.ID));// 식별자 매치
                e = v;
                if (token == Token.LPAREN) { // function call
                    match(Token.LPAREN);
                    Call c = new Call(v, arguments());
                    match(Token.RPAREN);
                    e = c;
                } else if (token == Token.LBRACKET) { // Array access
                    match(Token.LBRACKET);
                    Expr indexExpr = expr();
                    match(Token.RBRACKET);
                    Array array = new Array(v, indexExpr);
                    e = array;
                }
                break;
            case NUMBER: // 정수 혹은 스트링 리터럴 파싱
            case STRLITERAL:
                e = literal();
                break;
            case LPAREN:
                match(Token.LPAREN);// 왼쪽 괄호 매치
                e = aexp();// 괄호 수식 파싱
                match(Token.RPAREN);// 오른쪽 괄호 매치
                break;
            default:
                error("Identifier | Literal");
        }

        if (op != null)
            return new Unary(op, e);// 단항 연산 AST 구성 및 리턴
        else
            return e;
    }

    private Exprs arguments() {
        // arguments -> [ <expr> {, <expr> } ]
        Exprs es = new Exprs();
        while (token != Token.RPAREN) {
            es.add(expr());
            if (token == Token.COMMA)
                match(Token.COMMA);
            else if (token != Token.RPAREN)
                error("Exprs");
        }
        return es;
    }

    private Value literal() {
        String s = null;
        switch (token) {
            case NUMBER:
                s = match(Token.NUMBER);
                return new Value(Integer.parseInt(s));
            case STRLITERAL:
                s = match(Token.STRLITERAL);
                return new Value(s);
        }
        throw new IllegalArgumentException("no literal");
    }

    private boolean isType() {
        switch (token) {
            case INT:
            case BOOL:
            case STRING:
                return true;
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        Parser parser;
        Command command = null;
        if (args.length == 0) {
            Lexer.interactive = true;
            // !파일로 입력하기
            String filename = "프로그래밍언어론/언어S_인터프리터_구현_배열추가/for.s";
            parser = new Parser(new Lexer(filename));
            System.out.println(filename);
            // !직접 입력하기
            // System.out.print(">> ");
            // parser = new Parser(new Lexer());
            do {
                if (parser.token == Token.EOF) {
                    parser.token = parser.lexer.getToken();
                }
                try {
                    command = parser.command();
                    if (command != null)
                        command.display(0); // display AST
                } catch (Exception e) {
                    System.err.println(e);
                }
                // !직접 입력할때 아래 주석 제거
                // System.out.print("\n>> ");
            } while (true);
        } else {
            System.out.println("Begin parsing... " + args[0]);
            parser = new Parser(new Lexer(args[0]));
            do {
                if (parser.token == Token.EOF) {
                    break;
                }
                try {
                    command = parser.command();
                    if (command != null)
                        command.display(0); // display AST
                } catch (Exception e) {
                    System.err.println(e);
                }
            } while (command != null);
        }
    } // main
} // Parser