package me.shw.restapi.index;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import me.shw.restapi.events.EventController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class IndexController {
	
	@GetMapping("/")
	public RepresentationModel index() {
		var index = new RepresentationModel();
		index.add(linkTo(EventController.class).withRel("events"));
		return index;
	}
	

}
