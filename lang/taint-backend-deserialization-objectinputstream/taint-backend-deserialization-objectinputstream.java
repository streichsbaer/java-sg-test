import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Base64;

@RestController
@AssignmentHints({"insecure-deserialization.hints.1", "insecure-deserialization.hints.2", "insecure-deserialization.hints.3"})
public class InsecureDeserializationTask extends AssignmentEndpoint {

    public Object deserializeObject(InputStream receivedFile) throws IOException, ClassNotFoundException {
        // ruleid: taint-backend-deserialization-objectinputstream
        ObjectInputStream in = new ObjectInputStream(receivedFile);
        return in.readObject();
    }

    public static Object fromString(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        // todoruleid:taint-backend-deserialization-objectinputstream
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public void secured(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // ok: taint-backend-deserialization-objectinputstream
            ObjectInputStream ois = new ObjectInputStream(new SerialKiller(request.getParam("blub")));
            Object o = ois.readObject();
        } catch (Exception e) {
            return failed(this).feedback("insecure-deserialization.invalidversion").build();
        }
    }
    
    @PostMapping("/InsecureDeserialization/task/2")
    @ResponseBody
    public AttackResult completed2(@RequestParam String token) throws IOException {
        String b64token;
        b64token = token.replace('-', '+').replace('_', '/');
        // ruleid:taint-backend-deserialization-objectinputstream
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(b64token)))){
            Object o = ois.readObject();
        } catch (Exception e) {
            return failed(this).feedback("insecure-deserialization.invalidversion").build();
        }
        return success(this).build();
    }

    @PostMapping("/InsecureDeserialization/task")
    @ResponseBody
    public AttackResult completed(@RequestParam String token) throws IOException {
        String b64token;
        long before;
        long after;
        int delay;

        b64token = token.replace('-', '+').replace('_', '/');

        try {
            // ruleid:taint-backend-deserialization-objectinputstream
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(b64token)));
            before = System.currentTimeMillis();
            Object o = ois.readObject();
            if (!(o instanceof VulnerableTaskHolder)) {
                if (o instanceof String) {
                    return failed(this).feedback("insecure-deserialization.stringobject").build();
                }
                return failed(this).feedback("insecure-deserialization.wrongobject").build();
            }
            after = System.currentTimeMillis();
        } catch (InvalidClassException e) {
            return failed(this).feedback("insecure-deserialization.invalidversion").build();
        } catch (IllegalArgumentException e) {
            return failed(this).feedback("insecure-deserialization.expired").build();
        } catch (Exception e) {
            return failed(this).feedback("insecure-deserialization.invalidversion").build();
        }

        delay = (int) (after - before);
        if (delay > 7000) {
            return failed(this).build();
        }
        if (delay < 3000) {
            return failed(this).build();
        }
        return success(this).build();
    }
}
