package org.python.pydev.editor.codecompletion;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.python.pydev.core.ICallback;
import org.python.pydev.core.TestDependent;
import org.python.pydev.core.log.Log;
import org.python.pydev.core.structure.CompletionRecursionException;
import org.python.pydev.editor.codecompletion.revisited.CodeCompletionTestsBase;
import org.python.pydev.editor.codecompletion.revisited.modules.CompiledModule;

public class PythonCompletionZipsTest extends CodeCompletionTestsBase {

    public static void main(String[] args) {
        try {
            PythonCompletionZipsTest builtins = new PythonCompletionZipsTest();
            builtins.setUp();
            builtins.testZip();
            builtins.tearDown();

            junit.textui.TestRunner.run(PythonCompletionZipsTest.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * @see TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        CompiledModule.COMPILED_MODULES_ENABLED = false;
        
        //add the zip and the egg files here...
        this.restorePythonPath(
                TestDependent.GetCompletePythonLib(true)+"|"+
                TestDependent.TEST_PYSRC_LOC+"myzipmodule.zip"+"|"+
                TestDependent.TEST_PYSRC_LOC+"myeggmodule.egg", 
                false);
        codeCompletion = new PyCodeCompletion();
        PyCodeCompletion.onCompletionRecursionException = new ICallback<Object, CompletionRecursionException>() {

            public Object call(CompletionRecursionException e) {
                throw new RuntimeException("Recursion error:" + Log.getExceptionStr(e));
            }

        };
    }

    /*
     * @see TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        CompiledModule.COMPILED_MODULES_ENABLED = true;
        super.tearDown();
        PyCodeCompletion.onCompletionRecursionException = null;
    }

    

    public void testZip() throws Exception{
        String s = 
            "import myzipmodule\n" +
            "myzipmodule.";
        
        requestCompl(s, s.length(), -1, new String[]{"MyZipClass"});
    }
    
    
    public void testEgg() throws Exception{
        String s = 
            "import myeggmodule\n" +
            "myeggmodule.";
        
        requestCompl(s, s.length(), -1, new String[]{"MyEggClass"});
    }

}
