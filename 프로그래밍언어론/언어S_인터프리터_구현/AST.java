package 프로그래밍언어론.언어S_인터프리터_구현;

// AST.java
// AST for S
import java.util.*;

abstract class Command {
    // Command = Decl | Function | Stmt
    Type type = Type.UNDEF;

    public void display(int l) {
    }
}

class Indent {
    public static void display(int level, String s) {
        String tab = "";
        if (level >= 1) {
            // for (int i = 0; i < level; i++)
            // tab = tab + " ";
            // !Note: 다른 방식으로 표현
            for (int i = 0; i < level - 1; i++)
                tab = tab + " ";
            for (int i = 0; i < 1; i++)
                tab = tab + "┗━";
        }

        System.out.println(tab + s);
    }
}

class Decls extends ArrayList<Decl> {
    // Decls = Decl*

    Decls() {
        super();
    }

    Decls(Decl d) {
        this.add(d);
    }

    public void display(int l) {
        Indent.display(l, "Decls");
        for (Decl d : this) {
            d.display(l + 1);
        }
    }
}

class Decl extends Command {
    // Decl = Type type; Identifier id
    Identifier id;
    Expr expr = null;
    int arraysize = 0;

    Decl(String s, Type t) {
        id = new Identifier(s);
        type = t;
    } // declaration

    Decl(String s, Type t, int n) {
        id = new Identifier(s);
        type = t;
        arraysize = n;
    } // array declaration

    Decl(String s, Type t, Expr e) {
        id = new Identifier(s);
        type = t;
        expr = e;
    } // declaration

    @Override
    public void display(int level) {
        Indent.display(level, "Decl");
        if (type != null) {
            type.display(level + 1);
        }
        id.display(level + 1);
        if (expr != null) {
            expr.display(level + 1);
        }
    }
}

class Functions extends ArrayList<Function> {
    // Functions = Function*
    Functions() {
        super();
    }

    Functions(Function f) {
        this.add(f);
    }

    public void display(int l) {
        Indent.display(l, "Functions");
        for (Function f : this) {
            f.display(l + 1);
        }
    }
}

class Function extends Command {
    // Function = Type type; Identifier id; Decls params; Stmt stmt
    Identifier id;
    Decls params;
    Stmt stmt;

    Function(String s, Type t) {
        id = new Identifier(s);
        type = t;
        params = null;
        stmt = null;
    }

    public String toString() {
        return id.toString() + params.toString();
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Function");
        type.display(level + 1);
        id.display(level + 1);
        if (params != null) {
            params.display(level + 1);
        }
        if (stmt != null) {
            stmt.display(level + 1);
        }
    }
}

class Type {
    // Type = int | bool | string | fun | array | except | void
    final static Type INT = new Type("int");
    final static Type BOOL = new Type("bool");
    final static Type STRING = new Type("string");
    final static Type VOID = new Type("void");
    final static Type FUN = new Type("fun");
    final static Type ARRAY = new Type("array");
    final static Type EXC = new Type("exc");
    final static Type RAISEDEXC = new Type("raisedexc");
    final static Type UNDEF = new Type("undef");
    final static Type ERROR = new Type("error");

    protected String id;

    protected Type(String s) {
        id = s;
    }

    public String toString() {
        return id;
    }

    public void display(int level) {
        Indent.display(level, "Type: " + id);
    }
}

class ProtoType extends Type {
    // defines the type of a function and its parameters
    Type result;
    Decls params;

    ProtoType(Type t, Decls ds) {
        super(t.id);
        result = t;
        params = ds;
    }
}

abstract class Stmt extends Command {
    // Stmt = Empty | Stmts | Assignment | If | While | Let | Read | Print
}

class Empty extends Stmt {

}

class Stmts extends Stmt {
    // Stmts = Stmt*
    public ArrayList<Stmt> stmts = new ArrayList<>();

    Stmts() {
        super();
    }

    Stmts(Stmt s) {
        stmts.add(s);
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Stmts");
        for (Stmt s : stmts) {
            s.display(level + 1);
        }
    }
}

class Assignment extends Stmt {
    // Assignment = Identifier id; Expr expr
    Identifier id;
    Array ar = null;
    Expr expr;

    Assignment(Identifier t, Expr e) {
        id = t;
        expr = e;
    }

    Assignment(Array a, Expr e) {
        ar = a;
        expr = e;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Assignment");
        id.display(level + 1);
        expr.display(level + 1);
    }
}

class If extends Stmt {
    // If = Expr expr; Stmt stmt1, stmt2;
    Expr expr;
    Stmt stmt1, stmt2;

    If(Expr t, Stmt tp) {
        expr = t;
        stmt1 = tp;
        stmt2 = new Empty();
    }

    If(Expr t, Stmt tp, Stmt ep) {
        expr = t;
        stmt1 = tp;
        stmt2 = ep;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "if");
        expr.display(level + 1);
        Indent.display(level, "then");
        stmt1.display(level + 1);
        if (!(stmt2 instanceof Empty)) {
            Indent.display(level, "else");
            stmt2.display(level + 1);
        }
    }
}

class While extends Stmt {
    // While = Expr expr; Stmt stmt;
    Decl decl = null;
    Expr expr;
    Stmt stmt;
    Stmt stmt2;

    While(Expr t, Stmt b) {
        expr = t;
        stmt = b;
    }

    While(Decl a, Expr b, Stmt c, Stmt d) {
        decl = a;
        expr = b;
        stmt = d;
        stmt2 = c;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "While");
        if (decl != null) {
            Indent.display(level + 1, "Initialization");
            decl.display(level + 2);
        }
        Indent.display(level + 1, "Condition");
        expr.display(level + 2);
        Indent.display(level + 1, "Do");
        stmt.display(level + 2);
        if (stmt2 != null) {
            Indent.display(level + 1, "Update");
            stmt2.display(level + 2);
        }
    }
}

class Let extends Stmt {
    // Let = Decls decls; Functions funs; Stmts stmts;
    Decls decls;
    Functions funs;
    Stmts stmts;

    Let(Decls ds, Stmts ss) {
        decls = ds;
        funs = null;
        stmts = ss;
    }

    Let(Decls ds, Functions fs, Stmts ss) {
        decls = ds;
        funs = fs;
        stmts = ss;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Let");
        decls.display(level + 1);
        if (funs != null) {
            funs.display(level + 1);
        }
        stmts.display(level + 1);
    }

}

class Read extends Stmt {
    // Read = Identifier id
    Identifier id;

    Read(Identifier v) {
        id = v;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Read ");
        id.display(level + 1);
    }
}

class Print extends Stmt {
    // Print = Expr expr
    Expr expr;

    Print(Expr e) {
        expr = e;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "PRINT");
        expr.display(level + 1);
    }
}

class Return extends Stmt {
    Identifier fid;
    Expr expr;

    Return(String s, Expr e) {
        fid = new Identifier(s);
        expr = e;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Return");
        expr.display(level + 1);
    }

}

class Try extends Stmt {
    // Try = Identifier id; Stmt stmt1; Stmt stmt2;
    Identifier eid;
    Stmt stmt1;
    Stmt stmt2;

    Try(Identifier id, Stmt s1, Stmt s2) {
        eid = id;
        stmt1 = s1;
        stmt2 = s2;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "TRY");
        eid.display(level + 1);
        stmt1.display(level + 1);
        Indent.display(level, "EXCEPT");
        stmt2.display(level + 1);
    }
}

class Raise extends Stmt {
    Identifier eid;

    Raise(Identifier id) {
        eid = id;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "RAISE");
        eid.display(level + 1);
    }
}

class Exprs extends ArrayList<Expr> {
    // Exprs = Expr*

    Exprs() {
        super();
    }

    public void display(int level) {
        Indent.display(level, "Exprs");
        for (Expr expr : this) {
            expr.display(level + 1);
        }
    }
}

abstract class Expr extends Stmt {
    // Expr = Identifier | Value | Binary | Unary | Call

}

class Call extends Expr {
    Identifier fid;
    Exprs args;

    Call(Identifier id, Exprs a) {
        fid = id;
        args = a;
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Call");
        fid.display(level + 1);
        args.display(level + 1);
    }
}

class Identifier extends Expr {
    // Identifier = String id
    private String id;

    Identifier(String s) {
        id = s;
    }

    public String toString() {
        return id;
    }

    public boolean equals(Object obj) {
        String s = ((Identifier) obj).id;
        return id.equals(s);
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Identifier: " + id.toString());
    }
}

class Array extends Expr {
    // Array = Identifier id; Expr expr
    Identifier id;
    Expr expr = null;

    Array(Identifier s, Expr e) {
        id = s;
        expr = e;
    }

    public String toString() {
        return id.toString();
    }

    public boolean equals(Object obj) {
        String s = ((Array) obj).id.toString();
        return id.equals(s);
    }
}

class Value extends Expr {
    // Value = int | bool | string | array | function
    protected boolean undef = true;
    Object value = null; // Type type;

    Value(Type t) {
        type = t;
        if (type == Type.INT)
            value = new Integer(0);
        if (type == Type.BOOL)
            value = new Boolean(false);
        if (type == Type.STRING)
            value = "";
        undef = false;
    }

    Value(Object v) {
        if (v instanceof Integer)
            type = Type.INT;
        if (v instanceof Boolean)
            type = Type.BOOL;
        if (v instanceof String)
            type = Type.STRING;
        if (v instanceof Function)
            type = Type.FUN;
        if (v instanceof Value[])
            type = Type.ARRAY;
        value = v;
        undef = false;
    }

    Value(boolean v) {
        value = v;
    } // value

    Object value() {
        return value;
    }

    int intValue() {
        if (value instanceof Integer)
            return ((Integer) value).intValue();
        else
            return 0;
    }

    boolean boolValue() {
        if (value instanceof Boolean)
            return ((Boolean) value).booleanValue();
        else
            return false;
    }

    String stringValue() {
        if (value instanceof String)
            return (String) value;
        else
            return "";
    }

    Function funValue() {
        if (value instanceof Function)
            return (Function) value;
        else
            return null;
    }

    Value[] arrValue() {
        if (value instanceof Value[])
            return (Value[]) value;
        else
            return null;
    }

    Type type() {
        return type;
    }

    public String toString() {
        // if (undef) return "undef";
        if (type == Type.INT)
            return "" + intValue();
        if (type == Type.BOOL)
            return "" + boolValue();
        if (type == Type.STRING)
            return "" + stringValue();
        if (type == Type.FUN)
            return "" + funValue();
        if (type == Type.ARRAY)
            return "" + arrValue();
        return "undef";
    }

    @Override
    public void display(int level) {
        Indent.display(level, "Value: " + value);
    }
}

class Binary extends Expr {
    // Binary = Operator op; Expr expr1; Expr expr2;
    Operator op;
    Expr expr1, expr2;

    Binary(Operator o, Expr e1, Expr e2) {
        op = o;
        expr1 = e1;
        expr2 = e2;
    } // binary

    @Override
    public void display(int level) {
        Indent.display(level, "Binary");
        Indent.display(level + 1, "Operator: " + op.val);
        expr1.display(level + 1);
        expr2.display(level + 1);
    }
}

class Unary extends Expr {
    // Unary = Operator op; Expr expr
    Operator op;
    Expr expr;

    Unary(Operator o, Expr e) {
        op = o; // (o.val == "-") ? new Operator("neg"): o;
        expr = e;
    } // unary

    @Override
    public void display(int level) {
        Indent.display(level, "Unary");
        Indent.display(level + 1, "Operator: " + op.val);
        expr.display(level + 1);
    }
}

class Operator {
    String val;

    Operator(String s) {
        val = s;
    }

    public String toString() {
        return val;
    }

    public boolean equals(Object obj) {
        return val.equals(obj);
    }
}
