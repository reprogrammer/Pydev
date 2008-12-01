// Autogenerated AST node
package org.python.pydev.parser.jython.ast;
import org.python.pydev.parser.jython.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class Name extends exprType implements expr_contextType {
    public String id;
    public int ctx;
    public boolean reserved;

    public Name(String id, int ctx, boolean reserved) {
        this.id = id;
        this.ctx = ctx;
        this.reserved = reserved;
    }

    public Name(String id, int ctx, boolean reserved, SimpleNode parent) {
        this(id, ctx, reserved);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Name[");
        sb.append("id=");
        sb.append(dumpThis(this.id));
        sb.append(", ");
        sb.append("ctx=");
        sb.append(dumpThis(this.ctx,
        expr_contextType.expr_contextTypeNames));
        sb.append(", ");
        sb.append("reserved=");
        sb.append(dumpThis(this.reserved));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(47, ostream);
        pickleThis(this.id, ostream);
        pickleThis(this.ctx, ostream);
        pickleThis(this.reserved, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitName(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
    }

}
