// Class implements HostnameVerifier and returns true on verify
public class AllHosts implements HostnameVerifier {
    public boolean verify(final String hostname, final SSLSession session) {
        // ruleid: insecure-implementation-hostnameverifier
        return true;
    }
}

// Class does not implement HostnameVerifier
public class AllHosts {
    public boolean verify(final String hostname, final SSLSession session) {
        // ok: insecure-implementation-hostnameverifier
        return true;
    }
}