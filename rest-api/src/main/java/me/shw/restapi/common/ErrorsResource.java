package me.shw.restapi.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import me.shw.restapi.index.IndexController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors>{

	public ErrorsResource(Errors content, Link... links) {
		super(content, links);
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
		// TODO Auto-generated constructor stub
	}
	
	
}
