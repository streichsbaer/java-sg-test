package testcode.script.ognl;

import com.opensymphony.xwork2.ognl.OgnlReflectionProvider;

import javax.management.ReflectionException;
import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;

public class OgnlReflectionProviderSample {

    public void unsafeOgnlReflectionProvider(String input, OgnlReflectionProvider reflectionProvider, Class type) throws IntrospectionException, ReflectionException {
        // ruleid: variable-query-ognl
        reflectionProvider.getGetMethod(type, input);
    }

    public void unsafeOgnlReflectionProvider1(String input, ReflectionProvider reflectionProvider) throws IntrospectionException, ReflectionException {
        // ruleid: variable-query-ognl
        reflectionProvider.getValue(input, null, null);
    }

    public void unsafeOgnlReflectionProvider2(String input, OgnlUtil reflectionProvider) throws IntrospectionException, ReflectionException {
        // ruleid: variable-query-ognl
        reflectionProvider.setValue(input, null, null,null);
    }

    public void unsafeOgnlReflectionProvider3(String input, OgnlTextParser reflectionProvider) throws IntrospectionException, ReflectionException {
        // ruleid: variable-query-ognl
        reflectionProvider.evaluate( input );
    }

    public void safeOgnlReflectionProvider1(OgnlReflectionProvider reflectionProvider, Class type) throws IntrospectionException, ReflectionException {
        String input = "thisissafe";
        // ok: variable-query-ognl
        reflectionProvider.getGetMethod(type, input);
    }

    public void safeOgnlReflectionProvider2(OgnlReflectionProvider reflectionProvider, Class type) throws IntrospectionException, ReflectionException {
        // ok: variable-query-ognl
        reflectionProvider.getField(type, "thisissafe");
    }

}


// String concatenation used
public class Main {
    public void getUserProperty(String property) {
        OgnlUtil ognlutil = new OgnlUtil();
        // todoruleid: variable-query-ognl
        return ognlUtil.getValue("user."+property, ctx, root, String.class);
    }
}

// Standard arbitrary execution
public class Main {
    public void execute(final ActionInvocation invocation) throws Exception {
        if (this.namespace == null) {
            this.namespace = invocation.getProxy().getNamespace();
        }
        final OgnlValueStack stack = ActionContext.getContext().getValueStack();
        TextParseUtil textParse = new TextParseUtil();
        // ruleid: variable-query-ognl
        final String finalNamespace = textParse.translateVariables(this.namespace, stack);
        final String finalActionName = TextParseUtil.translateVariables(this.actionName, stack);
        if (this.isInChainHistory(finalNamespace, finalActionName)) {
            throw new XworkException("infinite recursion detected");
        }
    }
}

// Execution of fixed string
public class Main {
    public void execute(final ActionInvocation invocation) throws Exception {
        if (this.namespace == null) {
            this.namespace = invocation.getProxy().getNamespace();
        }
        final OgnlValueStack stack = ActionContext.getContext().getValueStack();
        TextParseUtil textParse = new TextParseUtil();
        // ok: variable-query-ognl
        final String finalNamespace = textParse.translateVariables("this.namespace", stack);
        final String finalActionName = TextParseUtil.translateVariables(this.actionName, stack);
        if (this.isInChainHistory(finalNamespace, finalActionName)) {
            throw new XworkException("infinite recursion detected");
        }
    }
}