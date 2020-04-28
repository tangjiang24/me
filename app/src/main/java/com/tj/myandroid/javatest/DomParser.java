package com.tj.myandroid.javatest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DomParser {

    public void  parseDom() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        String path = "C:\\Users\\19018090\\Desktop\\allTestFactoryMode.xml";
        Document document = builder.parse(new File(path));
        NodeList nodeList = document.getElementsByTagName("string");
        for(int i=0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            String name = element.getAttribute("name");
            String value = element.getFirstChild().getNodeValue();
            System.out.println("name="+name+"----value="+value);
        }
    }
}
