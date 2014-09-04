package plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mujava.app.XMLErrorHandler;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigReader {
	private static String file;
	private static ConfigReader instance = null;
	private HashMap<String, String> configValues;
	private Document doc = null;
	
	public static ConfigReader newInstance(String file) {
		if (instance == null || ConfigReader.file != file) {
			instance = new ConfigReader(file);
		}
		return instance;
	}
	
	private ConfigReader(String file) {
		ConfigReader.file = file;
		reload();
	}
	
	public void reload() {
		clean();
		load();
	}
	
	private void clean() {
		this.configValues = new HashMap<String, String>();
	}
	
	private void load() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			dbf.setNamespaceAware(true);
		    dbf.setValidating(true);
		    OutputStreamWriter errorWriter = new OutputStreamWriter(System.err,"UTF-8");
		    db.setErrorHandler(new XMLErrorHandler (new PrintWriter(errorWriter, true)));
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			InputStream xmlDoc = new FileInputStream(new File(ConfigReader.file));
			System.out.println("Parsing : " + ConfigReader.file + '\n');
			doc = db.parse(xmlDoc);
			parse(doc);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void parse(Document d) {
		NodeList childs = d.getElementsByTagName("value");
		for (int c = 0; c < childs.getLength(); c++) {
			String key = childs.item(c).getAttributes().getNamedItem("key").getNodeValue();
			
			String value = childs.item(c).getAttributes().getNamedItem("value").getNodeValue();
			
			this.configValues.put(key, value);
		}
	}
	
	public String getValue(String key) {
		return this.configValues.get(key);
	}
}
