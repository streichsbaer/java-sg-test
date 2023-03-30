package com.example.demo;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javax.xml.stream.XMLInputFactory;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    @XmlRootElement(name = "book")
    @XmlType(propOrder = {"name"})
    static class Book {
        private String name;

        @XmlElement(name = "name")
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        unmarshall05(req.getParameter("content"));
    }

    public Book unmarshall(HttpServletRequest req, HttpServletResponse res) {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            FileReader reader = new FileReader(req.getParameter("filename"));
            // ruleid: taint-backend-xxe-jaxb-unmarshaller
            Book book = (Book)context.createUnmarshaller().unmarshal(reader);
            return book;
        }
        catch (Exception e) { return null; }
    }

    public Book unmarshall01(HttpServletRequest req, HttpServletResponse res) {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            Part part = req.getPart("filecontent");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // ruleid: taint-backend-xxe-jaxb-unmarshaller
            return (Book)unmarshaller.unmarshal(part.getInputStream());
        }
        catch (Exception e) { return null; }
    }

    public Book unmarshall02(HttpServletRequest req, HttpServletResponse res) {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // ruleid: taint-backend-xxe-jaxb-unmarshaller
            return (Book)unmarshaller.unmarshal(new File(req.getParameter("filename")));
        }
        catch (Exception e) { return null; }
    }

    public Book unmarshall03(HttpServletRequest req, HttpServletResponse res) {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // ruleid: taint-backend-xxe-jaxb-unmarshaller
            return (Book)unmarshaller.unmarshal(new URL(req.getParameter("url")));
        }
        catch (Exception e) { return null; }
    }

    public Book unmarshall04(HttpServletRequest req, HttpServletResponse res) {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            String xml = req.getParameter("xmlcontent");
            // ruleid: taint-backend-xxe-jaxb-unmarshaller
            return (Book)unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) { return null; }
    }

    public Book unmarshall05(String xmlContent) {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // ruleid: taint-backend-xxe-jaxb-unmarshaller
            return (Book)unmarshaller.unmarshal(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) { return null; }
    }

    public Book unmarshall05() {
        try {
            JAXBContext context = JAXBContext.newInstance(Book.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // ok: taint-backend-xxe-jaxb-unmarshaller
            return (Book)unmarshaller.unmarshal(new ByteArrayInputStream("<book><name>safe</name></book>".getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) { return null; }
    }

    public class Comment {
      private String user;
      private String dateTime;
      private String text;
    }

    // This test is from 
    // https://github.com/guardrails-test/WebGoat-SemgrepTest/blob/main/src/main/java/org/owasp/webgoat/lessons/xxe/CommentsCache.java#L96
    protected Comment parseXml(String xml) throws JAXBException, XMLStreamException {
    var jc = JAXBContext.newInstance(Comment.class);
    var xif = XMLInputFactory.newInstance();

    if (webSession.isSecurityEnabled()) {
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
      xif.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
    }

    // ruleid: taint-backend-xxe-jaxb-unmarshaller
    var xsr = xif.createXMLStreamReader(new StringReader(xml));

    var unmarshaller = jc.createUnmarshaller();
    return (Comment) unmarshaller.unmarshal(xsr);
  }
}