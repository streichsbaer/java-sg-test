// Set no security authentication
public class Main {
    public static void main() {
        env = new Hashtable();
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        // ruleid: anonymous-ldap-bind
        dirContext = new InitialDirContext(env);
    }
}

// Set security authentication after
public class Main {
    public static void main() {
        env = new Hashtable();
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put(Context.SECURITY_AUTHENTICATION, validContext);
        // ok: anonymous-ldap-bind
        dirContext = new InitialDirContext(env);
    }
}