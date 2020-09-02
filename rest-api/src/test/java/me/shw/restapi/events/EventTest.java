package me.shw.restapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EventTest {
	
	@Test
	public void builder() {
		Event event = Event.builder()
						.name("spring rest api")
						.description("rest api development with spring")
						.build();
		assertThat(event).isNotNull();
	}
	
	@Test
	public void javaBean() {
		
		Event event = new Event();
		String name = "Event";
		event.setName(name);
		String description = "Spring";
		event.setDescription(description);
				
				
		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(description);
		
	}
	
	   
	

}
