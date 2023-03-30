class TrustAllManager implements X509TrustManager {

    // ruleid: insecure-implementation-trustmanager
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //Trust any client connecting (no certificate validation)
    }

    // ruleid: insecure-implementation-trustmanager
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //Trust any remote server (no certificate validation)
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // ruleid: insecure-implementation-trustmanager
        return null;
    }
}