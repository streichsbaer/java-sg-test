package com.example.javaservletdemo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getParameter("urlRedirect");
        badRedirect1(resp, url);
    }

    private void badRedirect1(HttpServletResponse resp, String url) throws IOException {
        if (url != null) {
            // ruleid: url-rewriting-httpservletresponse
            resp.sendRedirect(resp.encodeRedirectURL(url));
        }
    }

    public void badRedirect2(HttpServletResponse resp, String url) throws IOException {
        if (url != null) {
            // ruleid: url-rewriting-httpservletresponse
            resp.sendRedirect(resp.encodeRedirectUrl(url));
        }
    }

    private void badRedirect3(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        // ruleid: url-rewriting-httpservletresponse
        writer.print("<a href=\"" + resp.encodeURL("index.jsp") + "\">Index</a>");
    }

    private void badRedirect4(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        // ruleid: url-rewriting-httpservletresponse
        writer.print("<a href=\"" + resp.encodeURL("index.jsp") + "\">Index</a>");
    }

    public void badRedirect5(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        // ruleid: url-rewriting-httpservletresponse
        writer.print("<a href=\"" + resp.encodeURL(resp.getHeader("Host")) + "\">Index</a>");
    }

    public void badRedirect6(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        // ruleid: url-rewriting-httpservletresponse
        writer.print("<a href=\"" + resp.encodeUrl(resp.getHeader("Host")) + "\">Index</a>");
    }

    private void goodRedirect7(HttpServletResponse resp, String url) throws IOException {
        if (url != null) {
            // ok: url-rewriting-httpservletresponse
            resp.sendRedirect(url);
        }
    }
}