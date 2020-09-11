package me.shw.restapi.events;


import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class EventResource extends RepresentationModel<EventResource>{
	
	@JsonUnwrapped
	private Event event;

	public EventResource(Event event) {
		super();
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
	
	
	
	
	
}
