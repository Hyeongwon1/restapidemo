package me.shw.restapi.events;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class EventResource extends EntityModel<Event>{

	public EventResource(Event event, Link... links) {
		super(event, links);
		add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
		// TODO Auto-generated constructor stub
	}
	
	
	
}
