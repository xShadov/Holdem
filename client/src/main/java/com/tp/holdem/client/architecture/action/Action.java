package com.tp.holdem.client.architecture.action;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {
	private ActionType actionType;
	private ActionRequest request;

	public static Action from(ActionType type, ActionRequest request) {
		return new Action(type, request);
	}

	public static Action simple(ActionType type) {
		return new Action(type, null);
	}

	public Option<ActionRequest> getRequest() {
		return Option.of(request);
	}

	public <T extends ActionRequest> T instance(Class<T> clazz) {
		return getRequest()
				.filter(req -> actionType.clazz()
						.exists(optionClazz -> optionClazz.isInstance(req))
				)
				.map(clazz::cast)
				.getOrElseThrow(() -> new IllegalArgumentException("Action request is invalid"));
	}
}
