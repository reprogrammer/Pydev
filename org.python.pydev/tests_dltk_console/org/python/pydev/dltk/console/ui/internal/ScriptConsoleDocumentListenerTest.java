package org.python.pydev.dltk.console.ui.internal;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.python.pydev.core.docutils.StringUtils;
import org.python.pydev.dltk.console.InterpreterResponse;
import org.python.pydev.dltk.console.ScriptConsoleHistory;
import org.python.pydev.dltk.console.ScriptConsolePrompt;
import org.python.pydev.dltk.console.ui.IConsoleStyleProvider;
import org.python.pydev.editor.TestIndentPrefs;
import org.python.pydev.editor.autoedit.PyAutoIndentStrategy;

public class ScriptConsoleDocumentListenerTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
    public void testConsoleListener() throws Exception {
        final Document doc = new Document();
        final List<String> commandsHandled = new ArrayList<String>();

        ScriptConsolePrompt prompt = new ScriptConsolePrompt(">>> ", "... ");
        ScriptConsoleDocumentListener listener = new ScriptConsoleDocumentListener(new IScriptConsoleViewer2ForDocumentListener(){

            public IDocument getDocument() {
                return doc;
            }

            public IConsoleStyleProvider getStyleProvider() {
                return null;
            }

            public void revealEndOfDocument() {
                //do nothing
            }

            public void setCaretPosition(int length) {
                //do nothing
            }
        }, 
            
        new ICommandHandler(){

            public InterpreterResponse handleCommand(String userInput) throws Exception {
                commandsHandled.add(userInput);
                return new InterpreterResponse("", "", false, false);
            }
        }, 
            
        prompt, new ScriptConsoleHistory());
        
        PyAutoIndentStrategy strategy = listener.getIndentStrategy();
        strategy.setIndentPrefs(new TestIndentPrefs(true, 4));
        listener.setDocument(doc);
        
        
        doc.replace(0, 0, ">>> class A:");
        doc.replace(doc.getLength(), 0, "\n");
        assertEquals(StringUtils.format(">>> class A:%s>>>     ", listener.getDelimeter()), doc.get());
        doc.replace(doc.getLength(), 0, "def m1");
        doc.replace(doc.getLength(), 0, "(");
        assertEquals(StringUtils.format(">>> class A:%s>>>     def m1(self):", listener.getDelimeter()), doc.get());
    }
}