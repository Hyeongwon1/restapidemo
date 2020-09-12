package me.shw.restapi.events;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import me.shw.restapi.common.ErrorsResource;

@Controller
@RequestMapping(value ="/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
	
//	@Autowired 
//	EventRepository EventRepository;
	
//	@PostMapping("/api/events")
	
	
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;
	
	
	public EventController(EventRepository eventRepository, ModelMapper modelMapper ,EventValidator eventValidator) {
		this.eventRepository =eventRepository;
		this.modelMapper = modelMapper;
		this.eventValidator = eventValidator;
	}
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,Errors errors) {
		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		Event newEvent =this.eventRepository.save(event);
		WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
		URI createdUri = selfLinkBuilder.toUri();
		EventResource eventResource = new EventResource(event);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
//		eventResource.add(selfLinkBuilder.withSelfRel());
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		eventResource.add(Link.of("/docs/index.html").withRel("profile"));
		return ResponseEntity.created(createdUri).body(eventResource);
		
	}
	
	@GetMapping
	public ResponseEntity queryEntity(Pageable pageable,PagedResourcesAssembler<Event> assembler) {
		Page<Event> page = this.eventRepository.findAll(pageable);
		var pagedResources = assembler.toModel(page,e -> new EventResource(e));
		pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
		return ResponseEntity.ok(pagedResources);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id) {
		
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
		return ResponseEntity.ok(eventResource);
		
		
		
		
	}

	private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}

}
