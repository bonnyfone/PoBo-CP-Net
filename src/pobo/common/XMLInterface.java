package pobo.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pobo.cpnet.CPNET;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;



/**
 *	Class that allow to read and Write XML file
 */
public class XMLInterface {
	public static boolean writeCPNET(CPNET cpnet, String filename) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.newDocument();
			
			Element root = xmlDoc.createElement("cpnet");
			root.setAttribute("name", cpnet.getName());
			Element v, e, t = null, r;
			for (int i = 0; i < cpnet.getVertex().length; i++) {
				v = xmlDoc.createElement("vertex");
				v.setAttribute("name", cpnet.getVertex()[i].getName());
				for (int j = 0; j < cpnet.getCpnet_data().getMatrix().length; j++) {
					if ((Integer)cpnet.getCpnet_data().getMatrix()[i][j] == 1) {
						e = xmlDoc.createElement("edge");
						e.setTextContent(cpnet.getVertex()[j].getName());
						v.appendChild(e);
					}
				}
				t = xmlDoc.createElement("table");
				List keys = new ArrayList(cpnet.getVertex()[i].getTable().keySet());
				for (int k = 0; k < keys.size(); k++) {
					r = xmlDoc.createElement("row");
				    r.setTextContent((String)keys.get(k) + cpnet.getVertex()[i].getTable().get(keys.get(k)).toString());
				    t.appendChild(r);
				}
				v.appendChild(t);
				root.appendChild(v);
			}
			xmlDoc.appendChild(root);
				
			FileOutputStream fos = new FileOutputStream(filename);
			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
			of.setIndent(1);
			of.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(fos,of);
			serializer.asDOMSerializer();
			serializer.serialize(xmlDoc.getDocumentElement() );
			fos.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static CPNET readCPNET(String filename) {
		CPNET cpnet = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(filename));
			Element root = doc.getDocumentElement();
			String cpnetName =root.getAttribute("name");
			
			NodeList list = root.getElementsByTagName("vertex");
			cpnet = new CPNET(list.getLength());
			cpnet.setName(cpnetName);
			for (int i = 0; i < list.getLength(); i++) {
	            cpnet.addVertex(list.item(i).getAttributes().getNamedItem("name").getTextContent());
	        }
			NodeList childList;
			for (int i = 0; i < list.getLength(); i++) {
				childList = list.item(i).getChildNodes();
				for (int j = 0; j < childList.getLength(); j++) {
					if (childList.item(j).getNodeName().equals("edge"))
						cpnet.addEdge((String)cpnet.getVertexName().get(i), childList.item(j).getTextContent());
				}
			}
			cpnet.generate();
			NodeList rows;
			String conf;
			int pref;
			for (int i = 0; i < list.getLength(); i++) {
				childList = list.item(i).getChildNodes();
				for (int j = 0; j < childList.getLength(); j++) {
					if (childList.item(j).getNodeName().equals("table")) {
						rows = childList.item(j).getChildNodes();
						for (int r = 0; r < rows.getLength(); r++) {
							if (rows.item(r).getNodeName().equals("row")) {
								conf = rows.item(r).getTextContent().substring(0, rows.item(r).getTextContent().length() - 1);
								pref = Integer.parseInt(rows.item(r).getTextContent().substring(rows.item(r).getTextContent().length() - 1));
								cpnet.setPreference((String)cpnet.getVertexName().get(i), conf, pref);
							}
						}
					}
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return cpnet;
	}
}
