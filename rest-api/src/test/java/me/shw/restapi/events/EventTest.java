package me.shw.restapi.events;

import static org.assertj.core.api.Assertions.assertThat;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

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
	
	@ParameterizedTest
	@CsvSource({
		"0,0,true",
		"100,0,false",
		"0,100,false"
		
	})
	public void testFree(int basePrice, int maxPrice, boolean isFree) {
		//given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build()
				;
		//When
		event.update();

		//then
		assertThat(event.isFree()).isEqualTo(isFree);
				
	}
	
	@ParameterizedTest
	@MethodSource("prametersForTestoffline")
	public void testoffline(String location, boolean isOffline) {
		//given
		Event event = Event.builder()
					.location(location)
					.build()
					;
		//When
		event.update();

		//then
		assertThat(event.isOffline()).isEqualTo(isOffline);
	}
	
	static Object [] prametersForTestoffline() {
		return new Object[] {
			new Object[] {"강남",true},
			new Object[] {null,false},
			new Object[] {"  ",false}
		};
	}
}
