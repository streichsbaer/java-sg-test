package testcode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnvalidatedRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("urlRedirect");
        unvalidatedRedirect1(resp, url);
    }

    private void unvalidatedRedirect1(HttpServletResponse resp, String url) throws IOException {
        if (url != null) {
            // todoruleid: taint-backend-url-redirect-httpservletresponse
            resp.sendRedirect(url);
        }
    }

    public void unvalidatedRedirect2(HttpServletResponse resp, String url) {
        if (url != null) {
            // todoruleid: taint-backend-url-redirect-httpservletresponse
            resp.addHeader("Location", url);
        }
    }

    private void unvalidatedRedirect3(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // ruleid: taint-backend-url-redirect-httpservletresponse
        resp.sendRedirect(req.getParameter("urlRedirect"));
    }

    public void unvalidatedRedirect4(HttpServletRequest req, HttpServletResponse resp) {
        String url = req.getParameter("urlRedirect");
        // ruleid: taint-backend-url-redirect-httpservletresponse
        resp.addHeader("locAtion", url);
    }

    public void falsePositiveRedirect1(HttpServletResponse resp) throws IOException {
        String url = "/Home";
        if (url != null) {
            // ok: taint-backend-url-redirect-httpservletresponse
            resp.sendRedirect(url);
        }
    }

    public void falsePositiveRedirect2(HttpServletResponse resp) {
        // ok: taint-backend-url-redirect-httpservletresponse
        resp.addHeader("Location", "/login.jsp");
    }
    public void falsePositiveRedirect3(HttpServletRequest request, HttpServletResponse response) {
        // ok: taint-backend-url-redirect-httpservletresponse
        response.sendRedirect(request.getContextPath()+"/vulnerability/SendMessage.jsp?status=<b style='color:green'>* Message successfully sent *</b>");
    }

    private void falsePositiveRedirect3(HttpServletResponse resp, String url) throws IOException {
        if (url != null) {
            url = "static Strings are irrelevant";
            // ok: taint-backend-url-redirect-httpservletresponse
            resp.sendRedirect(url);
        }
    }
}
