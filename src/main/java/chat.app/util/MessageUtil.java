package chat.app.util;

import chat.app.model.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class MessageUtil {
	public static final String TOKEN = "token";
	public static final String MESSAGES = "tasks";
	private static final String TN = "TN";
	private static final String EN = "EN";
	private static final String ID = "id";
	private static final String USER = "description";
	private static final String TEXT = "done";

	private MessageUtil() {
	}

	public static String getToken(int index) {
		Integer number = index * 8 + 11;
		return TN + number + EN;
	}

	public static int getIndex(String token) {
		return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
	}

	public static JSONObject stringToJson(String data) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(data.trim());
	}

	public static Message jsonToMessage(JSONObject json) {
		Object id = json.get(ID);
		Object user = json.get(USER);
		Object text = json.get(TEXT);

		if (id != null && user != null && text != null) {
			return new Message((String) user, (String) text, (String) id);
		}
		return null;
	}
}
