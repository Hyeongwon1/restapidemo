package me.shw.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	
	@MockBean
	EventRepository eventRepository;
	
	
	@Test
	public void createEvent() throws Exception {
		Event event = Event.builder()
				.name("Spring")
				.description("Rest Api dev with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020,9,01,22,10))
				.closeEnrollmentDateTime(LocalDateTime.of(2020,9,02,22,10))
				.beginEventDateTime(LocalDateTime.of(2020,9,03,22,10))
				.endEventDateTime(LocalDateTime.of(2020,9,03,22,10))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("동작구")
				.build();
		event.setId(10);
		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").exists());
	
	}
	
	

}
