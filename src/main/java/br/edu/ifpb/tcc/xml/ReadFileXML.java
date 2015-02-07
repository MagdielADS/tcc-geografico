/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.tcc.xml;

import br.edu.ifpb.tcc.beans.ObjectSearchXML;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Magdiel Ildefonso
 */
public class ReadFileXML {
//    private String link;

    private final File filepath;
    private URL url;

    public ReadFileXML(String query, String latitude, String longitude) {
        try {
            this.url = new URL("http://api.wikimapia.org/?key=example&"
                    + "function=place.search&q=" + query + "&lat=" + latitude + "&lon=" + longitude + "&"
                    + "format=&pack=&language=pt&page=1&count=50&category=&"
                    + "categories_or=&categories_and=&distance=");
            System.out.println(this.url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ReadFileXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.filepath = new File(url.toString());
    }

    public ObjectSearchXML readFileReturnXMLObject() {
        ObjectSearchXML result = null;
        try {
            URLConnection urlConnection = url.openConnection();

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(url.openStream());
            doc.normalize();

            result = new ObjectSearchXML();
            if (doc.getElementsByTagName("title").getLength() > 0) {
                String title = doc.getElementsByTagName("title").item(0).getTextContent();
                result.setTitle(title);
            }

            if (doc.getElementsByTagName("description").getLength() > 0) {
                String description = doc.getElementsByTagName("description").item(0).getTextContent();
                result.setDescription(description);
            }

            if (doc.getElementsByTagName("polygon").getLength() > 0) {
                NodeList nodeListDatas = doc.getElementsByTagName("polygon");
                ArrayList<String> polygons = new ArrayList();
                for (int i = 0; i < nodeListDatas.getLength(); i++) {
                    polygons.add(nodeListDatas.item(i).getTextContent());
                }

                result.setPoints(polygons);
            }

        } catch (IOException ex) {
            Logger.getLogger(ReadFileXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ReadFileXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ReadFileXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
