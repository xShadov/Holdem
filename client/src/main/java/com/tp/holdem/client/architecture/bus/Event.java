package com.tp.holdem.client.architecture.bus;

import com.tp.holdem.client.architecture.model.event.EventType;
import com.tp.holdem.client.architecture.model.event.ServerResponse;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	private EventType eventType;
	private ServerResponse response;

	public static Event from(EventType type, ServerResponse response) {
		return new Event(type, response);
	}

	public static Event simple(EventType type) {
		return new Event(type, null);
	}

	public Option<ServerResponse> getResponse() {
		return Option.of(response);
	}

	public <T extends ServerResponse> T instance(Class<T> clazz) {
		return getResponse()
				.filter(response -> eventType.clazz().isInstance(response))
				.map(clazz::cast)
				.getOrElseThrow(() -> new IllegalArgumentException("Event response is invalid"));
	}
}
