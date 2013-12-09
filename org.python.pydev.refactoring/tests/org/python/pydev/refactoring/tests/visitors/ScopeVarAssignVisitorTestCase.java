/* 
 * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler 
 */

package org.python.pydev.refactoring.tests.visitors;

import java.util.Iterator;

import org.eclipse.jface.text.Document;
import org.python.pydev.refactoring.ast.adapters.ModuleAdapter;
import org.python.pydev.refactoring.ast.adapters.SimpleAdapter;
import org.python.pydev.refactoring.ast.visitors.VisitorFactory;
import org.python.pydev.refactoring.ast.visitors.context.ScopeAssignedVisitor;
import org.python.pydev.refactoring.tests.adapter.PythonNatureStub;
import org.python.pydev.refactoring.tests.core.AbstractIOTestCase;

public class ScopeVarAssignVisitorTestCase extends AbstractIOTestCase {

	public ScopeVarAssignVisitorTestCase(String name) {
		super(name);
	}

	@Override
	public void runTest() throws Throwable {
		StringBuffer buffer = new StringBuffer();
		ModuleAdapter module = VisitorFactory.createModuleAdapter(null, null, new Document(getSource()), new PythonNatureStub());
		ScopeAssignedVisitor visitor = VisitorFactory.createContextVisitor(ScopeAssignedVisitor.class, module.getASTNode(), module, module);

		assertTrue(visitor.getAll().size() > 0);

		printAttributes(buffer, visitor);
		this.setTestGenerated(buffer.toString().trim());

		assertEquals(getExpected(), getGenerated());
	}

	private void printAttributes(StringBuffer buffer, ScopeAssignedVisitor scopeVisitor) {
		Iterator<SimpleAdapter> iter = scopeVisitor.iterator();
		buffer.append("# " + scopeVisitor.getAll().size() + "\n");
		while (iter.hasNext()) {
			SimpleAdapter adapter = iter.next();
			buffer.append("# " + adapter.getParentName() + " " + adapter.getName() + "\n");
		}
	}

	@Override
	public String getExpected() {
		return getResult();
	}

}