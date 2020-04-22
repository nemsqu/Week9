package com.example.week9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class TheaterCollection {

    ArrayList<Theater> listTheaters;
    private static NodeList nodeList;


    TheaterCollection() {
        listTheaters = new ArrayList<>();
        String id;
        String name;

        for(int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                id = element.getElementsByTagName("ID").item(0).getTextContent();
                name = element.getElementsByTagName("Name").item(0).getTextContent();
                /*if(name.contains(":")){
                    listTheaters.add(new Theater(id, name));
                }*/
                listTheaters.add(new Theater(id, name));
            }
        }
    }

    static void parseXML(){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("https://www.finnkino.fi/xml/TheatreAreas/");
            document.getDocumentElement().normalize();
            System.out.println("ROOT ELEMENT: " + document.getDocumentElement().getNodeName());

            nodeList = document.getDocumentElement().getElementsByTagName("TheatreArea");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("YAAAAAAS");
        }
    }


}
