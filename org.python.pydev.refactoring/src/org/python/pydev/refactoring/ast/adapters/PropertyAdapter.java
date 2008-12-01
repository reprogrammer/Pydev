/* 
 * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler
 * Copyright (C) 2007  Reto Schuettel, Robin Stocker
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 * 
 */

package org.python.pydev.refactoring.ast.adapters;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.parser.jython.ast.keywordType;

public class PropertyAdapter extends AbstractNodeAdapter<SimpleNode> {

    private Name getter;

    private Name setter;

    private Name delete;

    private SimpleNode doc;

    public PropertyAdapter(ModuleAdapter module, AbstractScopeNode<?> parent, SimpleNode node, String endLineDelim) {
        super(module, parent, node, endLineDelim);
        if (nodeHelper.isAssign(getASTNode())) {
            initByAssign();
        } else {
            // functiondef not supported yet
        }
    }

    private void initByAssign() {
        getter = null;
        setter = null;
        delete = null;
        doc = null;

        exprType[] args = getPropertyArgs();
        for (int i = 0; i < args.length; i++) {
            setMethod(args[i], i);
        }
        
        for (keywordType keyword : getValue().keywords) {
            setKeyword(keyword);
        }
        if (getter == null)
            getter = createNone();
        if (setter == null)
            setter = createNone();
        if (delete == null)
            delete = createNone();

    }

    private void setKeyword(keywordType kw) {
        if (nodeHelper.isFGet(kw)) {
            this.getter = (Name) kw.value;
        } else if (nodeHelper.isFSet(kw)) {
            this.setter = (Name) kw.value;
        } else if (nodeHelper.isFDel(kw)) {
            this.delete = (Name) kw.value;
        } else if (nodeHelper.isKeywordStr(kw)) {
            this.doc = kw;
        }
    }

    private Name createNone() {
        return new Name("None", Name.Param, false);
    }

    private void setMethod(exprType expr, int i) {
        if (nodeHelper.isStr(expr)) {
            doc = expr;
        } else if (nodeHelper.isName(expr)) {
            Name name = (Name) expr;
            switch (i) {
            case 0:
                getter = name;
                break;
            case 1:
                setter = name;
                break;
            case 2:
                delete = name;
                break;
            case 3:
                if (!(nodeHelper.isNone(name)))
                    doc = name;
                break;
            }
        }
    }

    @Override
    public String getName() {
        return nodeHelper.getName(getTarget());
    }

    public boolean isComplete() {
        return hasGetter() && hasSetter() && hasDelete() && hasDocString();
    }

    public boolean hasSetter() {
        return (!(nodeHelper.isNone(setter)));
    }

    public boolean hasDelete() {
        return (!(nodeHelper.isNone(delete)));
    }

    public boolean hasDocString() {
        return doc != null;
    }

    public boolean hasGetter() {
        return (!(nodeHelper.isNone(getter)));
    }

    private Assign getAssign() {
        return (Assign) getASTNode();
    }

    private exprType[] getPropertyArgs() {
        return getValue().args;
    }

    private Name getTarget() {
        return (Name) getAssign().targets[0];
    }

    private Call getValue() {
        return (Call) getAssign().value;
    }
}
