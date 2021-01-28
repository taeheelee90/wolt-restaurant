package wolt.summer2021;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import wolt.summer2021.module.Restaurant.RestaurantService;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	RestaurantService restaurantServie;
	double lat = 60.1709;
	double lon = 24.941;

	@Test
	@DisplayName("Display Test")
	void show_discovery() throws Exception {

		mockMvc.perform(get("/discovery?lat=" + lat + "&lon=" + lon))
				.andExpect(status().isOk());
		
		assertNotNull(restaurantServie.restaurantsInMyArea(lat, lon));
	}
}
