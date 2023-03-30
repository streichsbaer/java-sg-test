import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

// Retobj flag set to true
public class Main {
    public static void main(String query, String filter, String[] attributes) {
        DirContext ctx = new InitialDirContext();

        ctx.search(query, filter,
                // ruleid: ldap-entry-poisoning-object-returning-search
                new SearchControls(2, 0, 0, attributes,
                true, //Enable object deserialization if bound in directory
                true));

        // ruleid: ldap-entry-poisoning-object-returning-search
        SearchControls sc =  new SearchControls(1, 0, 0, attributes,
                false, //Enable object deserialization if bound in directory
                true);
        sc.setReturningObjFlag(true);
        ctx.search(query, filter, sc);
    }
    
    // Retobj flag set to false
    public static void main2(String query, String filter, String[] attributes) {
        DirContext ctx = new InitialDirContext();

        ctx.search(query, filter,
                // ok: ldap-entry-poisoning-object-returning-search
                new SearchControls(2, 0, 0, attributes,
                false,
                true));
    }
}



