import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Run variable script
public class Main {
    public void runCustomTrigger(HttpServletRequest request, HttpServletResponse response) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        // ruleid: taint-backend-code-injection-scriptengine-eval
        engine.eval(request.getParameter("script"));
    }

    // Run static script
    public void runStaticTrigger() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        // ok: taint-backend-code-injection-scriptengine-eval
        engine.eval("4*2");
    }
}