package me.shw.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.shw.restapi.common.TestDescription;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;

	@Test
	@TestDescription("정상이벤트 생성 테스트")
	public void createEvent() throws Exception {
		EventDto event = EventDto.builder()
				
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
//				.free(true)
//				.offline(false)
//				.eventStatus(EventStatus.PUBLISHED)
				.build();
//		event.setId(10);
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
				.andDo(print()) 
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").exists())
				.andExpect(header().exists(HttpHeaders.LOCATION))
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
				.andExpect(jsonPath("id").value(Matchers.not(100)))
				.andExpect(jsonPath("free").value(Matchers.not(true)))
				.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT))
				;
	
	}
	@Test
	@TestDescription("입력할수없는 값 이벤트 생성 테스트")
	public void createEvent_Bad_Request() throws Exception {
		Event event = Event.builder()
				.id(100)
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
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build();
//		event.setId(10);
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
				.andDo(print()) 
				.andExpect(status().isBadRequest())
				;
	
	}
	
	@Test
	@TestDescription("입력값 비어있을 떄 이벤트 생성 테스트")
	public void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDto eventDto = EventDto.builder().build();
		this.mockMvc.perform(post("/api/events")
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(eventDto)))
				.andExpect(status().isBadRequest())
					;
		
		
	}
	
	@Test
	@TestDescription("입력값 잘못되었을 때 이벤트 생성 테스트")
	public void createEvent_Bad_Request_Wrong_Input() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("Rest Api dev with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020,9,04,22,10))
				.closeEnrollmentDateTime(LocalDateTime.of(2020,9,03,22,10))
				.beginEventDateTime(LocalDateTime.of(2020,9,02,22,10))
				.endEventDateTime(LocalDateTime.of(2020,9,01,22,10))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("동작구")
				.build();
		this.mockMvc.perform(post("/api/events")
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(eventDto)))
				.andExpect(status().isBadRequest())
					;
		
		
	}
	
	

}
