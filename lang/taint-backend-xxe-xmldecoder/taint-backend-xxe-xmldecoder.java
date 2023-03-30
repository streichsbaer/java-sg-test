package testcode.xmldecoder;

import java.beans.XMLDecoder;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XmlDecodeUtil {

    public static void main(String[] args) {
        InputStream in = XmlDecodeUtil.class.getResourceAsStream("/testcode/xmldecoder/obj1.xml");
        XmlDecodeUtil.handleXml0(in);
    }

    public static Object handleXml(HttpServletRequest req, HttpServletResponse res) {
    // ruleid: taint-backend-xxe-xmldecoder
        XMLDecoder d = new XMLDecoder(req.getParam("in"));
        try {
            Object result = d.readObject(); //Deserialization happen here
            return result;
        }
        finally {
            d.close();
        }
    }

    // Not sure about this one actually, because it is not confirmed tainted.
    public static Object handleXml0(InputStream in) {
    // todoruleid: taint-backend-xxe-xmldecoder
        XMLDecoder d = new XMLDecoder(in);
        try {
            Object result = d.readObject(); //Deserialization happen here
            return result;
        }
        finally {
            d.close();
        }
    }


    // ok: taint-backend-xxe-xmldecoder
    public static Object handleXml1() {
        XMLDecoder d = new XMLDecoder("<safe>XML</safe>");
        try {
            Object result = d.readObject();
            return result;
        }
        finally {
            d.close();
        }
    }

    // ok: taint-backend-xxe-xmldecoder
    public static Object handleXml2() {
        String strXml = "<safe>XML</safe>";
        XMLDecoder d = new XMLDecoder(strXml);
        try {
            Object result = d.readObject();
            return result;
        }
        finally {
            d.close();
        }
    }

}
