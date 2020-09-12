package me.shw.restapi.events;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.shw.restapi.common.RestDocsConfiguration;
import me.shw.restapi.common.TestDescription;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;

	@Autowired
	EventRepository eventRepository;
	
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
				.andExpect(jsonPath("free").value(false))
				.andExpect(jsonPath("offline").value(true))
				.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.query-events").exists())
				.andExpect(jsonPath("_links.update-event").exists())
				.andDo(document("create-event",
						links( //링크 문서화
								linkWithRel("self").description("link to self"),
								linkWithRel("query-events").description("link to query events"),
								linkWithRel("update-event").description("link to update an existing event"),
								linkWithRel("profile").description("link to profle")
						),
						requestHeaders( //요청 헤더 문서화 
								headerWithName(HttpHeaders.ACCEPT).description("accept header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
						),
						requestFields( //요청 필드 문서화
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("date time of begin of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
						responseHeaders( //응답 헤더 문서화
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields( //응답 본문 문서화
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("date time of begin of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events list"),
                                fieldWithPath("_links.update-event.href").description("link to update an existing event"),
                                fieldWithPath("_links.profile.href").description("link to profle")
                        )
						
						
				))
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
		
		mockMvc.perform(post("/api/events")
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
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("content[0].objectName").exists())
				.andExpect(jsonPath("content[0].field").exists())
				.andExpect(jsonPath("content[0].defaultMessage").exists())
				.andExpect(jsonPath("content[0].code").exists())
//				.andExpect(jsonPath("$[0].rejectedValue").exists())
				.andExpect(jsonPath("_links.index").exists())
					;
		
		
	}
	
	@Test
	@DisplayName("30개 이벤트를 10개씩 두번째 페이지 조회하기")
	public void queryEvents() throws Exception {
		//givne
		IntStream.range(0, 30).forEach(i ->{
			this.generateEvent(i);
		});
		
		//when & then
		this.mockMvc.perform(get("/api/events")
						.param("page", "1")
						.param("size", "10")
						.param("sort", "name,DESC")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("page").exists())
				.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andDo(document("query-event"))
				;
	
	}

	
	@Test
	@DisplayName("기존 이벤트 하나 조회하기")
	public void getEvent() throws Exception {
		//given
		Event event =this.generateEvent(100);
	
		//when & then
		this.mockMvc.perform(get("/api/events/{id}",event.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").exists())
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andDo(document("get-an-event"))
				;
	}
	
	@Test
	@DisplayName("없는 이벤트조회 404 응답받기")
	public void getEvent404() throws Exception {
		//when & then
			this.mockMvc.perform(get("/api/events/7777"))
					.andExpect(status().isNotFound())
					;		
	}
	
	
	private Event generateEvent(int index) {
		
		Event event = Event.builder()
				.name("event"+ index)
				.description("test event")
				.build();
		
		return this.eventRepository.save(event);
		
	}

}
