package com.tp.holdem.model.message;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	private MessageType messageType;
	private ServerMessage response;

	public static Message from(MessageType type, ServerMessage response) {
		return new Message(type, response);
	}

	public static Message simple(MessageType type) {
		return new Message(type, null);
	}

	public Option<ServerMessage> getResponse() {
		return Option.of(response);
	}

	public <T extends ServerMessage> T instance(Class<T> clazz) {
		return getResponse()
				.filter(response -> messageType.clazz().isInstance(response))
				.map(clazz::cast)
				.getOrElseThrow(() -> new IllegalArgumentException("Message response is invalid"));
	}
}
