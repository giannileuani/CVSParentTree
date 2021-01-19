import java.io.*;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InputParser {

	public static void main(String args[]) {
		if (args.length>=1) {
			File f=new File(args[0]);
			if (f.exists() && f.isFile()) {
				InputParser ip=new InputParser();
				ip.process(f);
			} else {
				System.err.println("Need a valid input file");
			}
		} else {
			System.err.println("Need an input file");
		}
	}
	private Document xmlDoc;
	private XPath xPath;
	public InputParser() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			xmlDoc = docBuilder.newDocument();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			xPath = xPathfactory.newXPath();
		} catch (Exception err ) {
			err.printStackTrace();
		}
	}
	public void process(File f) {
		/*
		 * Holding place for incoming elements
		 */
		Vector<DataElement> dataElemList=new Vector<DataElement>();
		
		try {
			String line=null;
			/*
			 * Read from input file
			 */
			BufferedReader read=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			while((line=read.readLine())!=null) {
				String parts[] = line.split("\\|");
				for (String part:parts) {
					DataElement de=new DataElement(part);
					dataElemList.add(de);
				}
			}			
			read.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
		/*
		 * Verify we have the data pre-loaded
		 */
		for (DataElement de:dataElemList) {
			System.out.println(de);
		}
		/*
		 * Load data into an XML Document.
		 */
		for (DataElement de:dataElemList) {
			/*
			 * Create XML Element to append to XML Document
			 */
			Element elem = xmlDoc.createElement("DataElem");
			elem.setAttribute("node_id", de.node_id);
			elem.setAttribute("node_name", de.node_name);
			if ("null".equals(de.parent_id)) {
				xmlDoc.appendChild(elem);
			} else {
				try {
					XPathExpression expr = xPath.compile(String.format("//DataElem[@node_id=\"%s\"]/self::node()",de.parent_id));
					Element lookup = (Element)expr.evaluate(xmlDoc,  XPathConstants.NODE);
					lookup.appendChild(elem);
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		}
		System.out.println();
		/*
		 * XML Document populated, dump to System.out
		 */
		try {
			TransformerFactory tfactory = TransformerFactory.newInstance();
			tfactory.setAttribute("indent-number",new Integer(3));
			Transformer xtransform = tfactory.newTransformer();

			xtransform.setOutputProperty(OutputKeys.INDENT,"yes");
			xtransform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			Source src = new DOMSource(xmlDoc);
			Result res = new StreamResult(new OutputStreamWriter(System.out, "utf-8"));
			xtransform.transform(src, res);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	private class DataElement {
		public String parent_id;
		public String node_id;
		public String node_name;
		public DataElement(String part) {
			String bits[]=part.split(",");
			if (bits.length==3) {
				parent_id=bits[0];
				node_id=bits[1];
				node_name=bits[2];
			}
		}
		public String toString() {
			StringBuilder sb=new StringBuilder();
			sb.append(parent_id).append(',').append(node_id).append(',').append(node_name);
			return sb.toString();
		}
	}
}
