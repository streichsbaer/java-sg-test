import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Use of variable in second parameter
public class Eval {
    public static void evaluateExpressionUnsafe(HttpServletRequest request, HttpServletResponse response) {
        String expression = request.getParamter("expression")
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        // ruleid: taint-backend-el-injection-expressionfactory
        ValueExpression vex = expressionFactory.createValueExpression(elContext, expression, String.class);
        return (String) vex.getValue(elContext);
    }

    public static void evaluateExpressionSafe(HttpServletRequest request, HttpServletResponse response) {
        String expression = request.getParamter("expression");
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        // ok: taint-backend-el-injection-expressionfactory
        ValueExpression vex = expressionFactory.createValueExpression(elContext, "expression", String.class);
        return (String) vex.getValue(elContext);
    }
}