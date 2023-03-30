import org.apache.commons.mail.SimpleEmail;

// Send email without verification
public class Main {
    public static void main() {
        Email email = new SimpleEmail();
        email.setHostName("smtp.servermail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setFrom("user@gmail.com");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("foo@bar.com");
        // ruleid: missing-ssl-certificate-validation-simpleemail
        email.send();
    }
}

// Send email after verification
public class Main {
    public static void main() {
        Email email = new SimpleEmail();
        email.setHostName("smtp.servermail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setFrom("user@gmail.com");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("foo@bar.com");
        email.setSSLCheckServerIdentity(true);
        // ok: missing-ssl-certificate-validation-simpleemail
        email.send();
    }
}

// Send email before verification
public class Main {
    public static void main() {
        Email email = new SimpleEmail();
        email.setHostName("smtp.servermail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setFrom("user@gmail.com");
        email.setSubject("TestMail");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("foo@bar.com");
        // ruleid: missing-ssl-certificate-validation-simpleemail
        email.send();
        email.setSSLCheckServerIdentity(true);
    }
}