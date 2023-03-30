import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Usage of string concatenation 
public class Main {
    public static void main (HttpServletRequest req, HttpServletResponse resp) {
        InitialLdapContext context = new InitialLdapContext();
        // ruleid: taint-backend-ldap-injection-search
        NamingEnumeration<SearchResult> answers = context.search("dc=People,dc=example,dc=com",
        "(uid=" + req.getParam("username") + ")", ctrls);
    }

    public static void mainSecure (HttpServletRequest req, HttpServletResponse resp) {
        InitialLdapContext context = new InitialLdapContext();
        String username = req.getParam("username");
        if(StringUtils.isAlphanumeric(username)) {
            // todook: taint-backend-ldap-injection-search
            NamingEnumeration<SearchResult> answers = context.search("dc=People,dc=example,dc=com",
            "(uid=" + username + ")", ctrls);
        }
    }
}