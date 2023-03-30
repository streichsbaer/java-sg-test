import java.util.Scanner;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


// Using path with input sanitization outside the function
// Currently false positive
public class Main {
    
    Response getImage(@javax.ws.rs.PathParam("image") String image) {
        String safe = FilenameUtils.getName(image);
        // ok: taint-backend-path-traversal-file
        File file = new File("resources/images/", safe);
        if (!file.exists()) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().entity(new FileInputStream(file)).build();
    }
}

// Using path with input sanitization inside the function
public class Main {
    public static void main() {
        Scanner in = new Scanner(System.in);
        String image = in.nextLine();
        getImage(image);
    }
    Response getImage(@javax.ws.rs.PathParam("image") String image) {
        String safePath = sanitize(image);
        // ok: taint-backend-path-traversal-file
        File file = new File("resources/images/", safePath);
        if (!file.exists()) {
            return Response.status(Status.NOT_FOUND).build();
        }
        // ok: taint-backend-path-traversal-file
        return Response.ok().entity(new FileInputStream(file)).build();
    }
}

// Using path without input validation
public class Main {
    public static void main() {
        Scanner in = new Scanner(System.in);
        String image = in.nextLine();
        getImage(image);
    }
    public Response getImage(@javax.ws.rs.PathParam("image") String image) {
        // ruleid: taint-backend-path-traversal-file
        File file = new File("resources/images/", image);
        if (!file.exists()) {
            return Response.status(Status.NOT_FOUND).build();
        }
        // ruleid: taint-backend-path-traversal-file
        return Response.ok().entity(new FileInputStream(file)).build();
    }
}