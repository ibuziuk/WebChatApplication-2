package chat.app.controller;

import chat.app.model.Message;
import chat.app.model.MessageStorage;
import chat.app.storage.XMLHistoryUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.PrintWriter;

import static chat.app.util.MessageUtil.*;
import static chat.app.util.ServletUtil.APPLICATION_JSON;
import static chat.app.util.ServletUtil.getMessageBody;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
        try {
            loadHistory();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter(TOKEN);

		if (token != null && !"".equals(token)) {
			int index = getIndex(token);
			String messages = formResponse(index);
			response.setContentType(APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.print(messages);
			out.flush();
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data = getMessageBody(request);

        JSONObject json = null;
        try {
            json = stringToJson(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Message message = jsonToMessage(json);
			MessageStorage.addMessage(message);
        try {
            XMLHistoryUtil.addData(message);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);

			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

	}


	/*protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPut");
		String data = ServletUtil.getMessageBody(request);
		logger.info(data);
		try {
			JSONObject json = stringToJson(data);
			Task task = jsonToTask(json);
			String id = task.getId();
			Task taskToUpdate = MessageStorage.getTaskById(id);
			if (taskToUpdate != null) {
				taskToUpdate.setDescription(task.getDescription());
				taskToUpdate.setDone(task.isDone());
				XMLHistoryUtil.updateData(taskToUpdate);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
			}
		} catch (ParseException | ParserConfigurationException | SAXException | TransformerException | XPathExpressionException e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}*/

	@SuppressWarnings("unchecked")
	private String formResponse(int index) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(MESSAGES, MessageStorage.getSubMessagesByIndex(index));
		jsonObject.put(TOKEN, getToken(MessageStorage.getSize()));
		return jsonObject.toJSONString();
	}

	private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
		if (XMLHistoryUtil.doesStorageExist()) {
			MessageStorage.addAll(XMLHistoryUtil.getTasks());
		} else {
			XMLHistoryUtil.createStorage();
		}
	}
	
	/*private void addStubData() throws ParserConfigurationException, TransformerException {
		Message[] stubMessages = {
				new Message("1", "Create markup", true),
				new Task("2", "Learn JavaScript", true),
				new Task("3", "Learn Java Servlet Technology", false), 
				new Task("4", "Write The Chat !", false), };
		TaskStorage.addAll(stubTasks);
		for (Task task : stubTasks) {
			try {
				XMLHistoryUtil.addData(task);
			} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
				logger.error(e);
			}
		}
	}*/

}
