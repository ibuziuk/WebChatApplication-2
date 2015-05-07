package chat.app.storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import chat.app.model.Message;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class XMLHistoryUtil {
	private static final String STORAGE_LOCATION = System.getProperty("user.home") +  File.separator + "history.xml"; // history.xml will be located in the home directory
	private static final String MESSAGES = "messages";
	private static final String MESSAGE = "message";
	private static final String ID = "id";
	private static final String USER = "user";
	private static final String TEXT = "text";

	private XMLHistoryUtil() {
	}

	public static synchronized void createStorage() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(MESSAGES);
		doc.appendChild(rootElement);

		Transformer transformer = getTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
		transformer.transform(source, result);
	}

	public static synchronized void addData(Message message) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(STORAGE_LOCATION);
		document.getDocumentElement().normalize();
		
		Element root = document.getDocumentElement();

		Element messageElement = document.createElement(MESSAGE);
		root.appendChild(messageElement);

		messageElement.setAttribute(ID, message.getId());

		Element user = document.createElement(USER);
		user.appendChild(document.createTextNode(message.getUsername()));
		messageElement.appendChild(user);

		Element text = document.createElement(TEXT);
		text.appendChild(document.createTextNode(message.getText()));
		messageElement.appendChild(text);

		DOMSource source = new DOMSource(document);

		Transformer transformer = getTransformer();

		StreamResult result = new StreamResult(STORAGE_LOCATION);
		transformer.transform(source, result);
	}

	/*public static synchronized void updateData(Task task) throws ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(STORAGE_LOCATION);
		document.getDocumentElement().normalize();
		Node taskToUpdate = getNodeById(document, task.getId());

		if (taskToUpdate != null) {

			NodeList childNodes = taskToUpdate.getChildNodes();

			for (int i = 0; i < childNodes.getLength(); i++) {

				Node node = childNodes.item(i);

				if (DESCRIPTION.equals(node.getNodeName())) {
					node.setTextContent(task.getDescription());
				}

				if (DONE.equals(node.getNodeName())) {
					node.setTextContent(Boolean.toString(task.isDone()));
				}

			}

			Transformer transformer = getTransformer();

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(STORAGE_LOCATION));
			transformer.transform(source, result);
		} else {
			throw new NullPointerException();
		}
	}*/

	public static synchronized boolean doesStorageExist() {
		File file = new File(STORAGE_LOCATION);
		return file.exists();
	}

	public static synchronized List<Message> getTasks() throws SAXException, IOException, ParserConfigurationException {
		List<Message> tasks = new ArrayList<Message>();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(STORAGE_LOCATION);
		document.getDocumentElement().normalize();
		Element root = document.getDocumentElement();
		NodeList messageList = root.getElementsByTagName(MESSAGE);
		for (int i = 0; i < messageList.getLength(); i++) {
			Element messageElement = (Element) messageList.item(i);
			String id = messageElement.getAttribute(ID);
			String user = messageElement.getElementsByTagName(USER).item(0).getTextContent();
            String text = messageElement.getElementsByTagName(TEXT).item(0).getTextContent();
			tasks.add(new Message(user, text, id));
		}
		return tasks;
	}

	/*public static synchronized int getStorageSize() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(STORAGE_LOCATION);
		document.getDocumentElement().normalize();
		Element root = document.getDocumentElement(); // Root <tasks> element
		return root.getElementsByTagName(TASK).getLength();
	}*/

	/*private static Node getNodeById(Document doc, String id) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//" + TASK + "[@id='" + id + "']");
		return (Node) expr.evaluate(doc, XPathConstants.NODE);
	}*/

	private static Transformer getTransformer() throws TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// Formatting XML properly
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		return transformer;
	}

}
