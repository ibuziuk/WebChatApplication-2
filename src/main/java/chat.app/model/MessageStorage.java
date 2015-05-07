package chat.app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MessageStorage {
	private static final List<Message> CONTAINER = Collections.synchronizedList(new ArrayList<Message>());

	private MessageStorage() {
	}

	public static List<Message> getStorage() {
		return CONTAINER;
	}

	public static void addMessage(Message message) {
		CONTAINER.add(message);
	}

	public static void addAll(Message[] messages) {
		CONTAINER.addAll(Arrays.asList(messages));
	}
	
	public static void addAll(List<Message> messages) {
		CONTAINER.addAll(messages);
	}

	public static int getSize() {
		return CONTAINER.size();
	}

	public static List<Message> getSubMessagesByIndex(int index) {
		return CONTAINER.subList(index, CONTAINER.size());
	}

	public static Message getMessageById(String id) {
		for (Message message : CONTAINER) {
			if (message.getId().equals(id)) {
				return message;
			}
		}
		return null;
	}

}
