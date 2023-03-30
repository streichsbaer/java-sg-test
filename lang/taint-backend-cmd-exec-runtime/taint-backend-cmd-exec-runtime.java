import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Runtime;

// Call to exec with a non-constant string
public class Main{
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        Runtime r = Runtime.getRuntime();
        String input = request.getParam("cmd");

        // Insecure: java HelloWorld ". && touch shell"
        // ruleid: taint-backend-cmd-exec-runtime
        r.exec(new String[] {"sh", "-c", "ls " + input});
        
        // Insecure: java HelloWorld "touch shell"
        // ruleid: taint-backend-cmd-exec-runtime
        r.exec(new String[] {"sh", "-c", input});
        
        // Insecure: java HelloWorld "touch 4"
        // ruleid: taint-backend-cmd-exec-runtime
        Process proc = r.exec(input);
        
        // ok: taint-backend-cmd-exec-runtime
        Process proc = r.exec(new String[] {"sh", "-c", "id", input});
        
        // ok: taint-backend-cmd-exec-runtime
        Process proc = r.exec(new String[] {"id", input});
        
        // ruleid: taint-backend-cmd-exec-runtime
        r.loadLibrary(String.format("%s.dll", input));

        // ruleid: taint-backend-cmd-exec-runtime
        Process p = r.exec(request.getParameter("cmd"));
        
        String[] lol = {"sh", "-c", "ls " + input};
        // ruleid: taint-backend-cmd-exec-runtime
        Process p = r.exec(lol);
        
        // ok: taint-backend-cmd-exec-runtime
        r.exec("/bin/sh -c some_tool" + input);
        
        // ok: taint-backend-cmd-exec-runtime
        r.exec("ls -l . / " + input);

        // ok: taint-backend-cmd-exec-runtime
        r.exec(new String[] {"some_tool", "-t", "param1 param2 " + input});
        // ok: taint-backend-cmd-exec-runtime
        r.exec(new String[] {"some_tool", "-t", input});
    }
    
    public static void main2(HttpServletRequest request, HttpServletResponse response) {
        String input = request.getParam("cmd");

        // ruleid: taint-backend-cmd-exec-runtime
        Runtime.getRuntime().loadLibrary(String.format("%s.dll", input));
    }

    // Call to exec with a constant string
    public static void mainSecure(HttpServletRequest request, HttpServletResponse response) {
        Runtime r = Runtime.getRuntime();
        String input = request.getParam("cmd");
        input = "constant";
        // ok: taint-backend-cmd-exec-runtime
        r.exec("/bin/sh -c some_tool" + input);

        // ok: taint-backend-cmd-exec-runtime
        r.loadLibrary("lib.dll");
    }
}