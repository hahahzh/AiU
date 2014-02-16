/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XML工具类，存放通用的xml操作
 * @author kortide
 */
public abstract class XmlUtil {

    /**
     * 获取一个空白的dom对象
     * @return
     */
    public static Document getNewDocument() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            return doc;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * 从文件打开xml
     * @param file
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document openXmlFile(File file) throws IOException, ParserConfigurationException, SAXException {
            return  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
    }
    
    /**
     * 从输入流打开xml
     * @param file
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document openXmlFile(InputStream is) throws IOException, ParserConfigurationException, SAXException {
            return  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    }

    /**
     * 保存XML文件
     * @param dom
     * @param dest
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static void saveXMLFile(Document dom,File dest) throws TransformerConfigurationException, TransformerException{
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(dom),new StreamResult(dest));
    }
    
    public static String dom2String(Document doc) throws TransformerConfigurationException, TransformerException {
        StringWriter buffer = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),new StreamResult(buffer));
        return buffer.getBuffer().toString();
    }

}
