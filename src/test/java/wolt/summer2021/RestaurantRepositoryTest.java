package wolt.summer2021;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import wolt.summer2021.module.Restaurant.RestaurantRepository;
import wolt.summer2021.module.Restaurant.RestaurantService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantRepositoryTest {

	@Autowired private RestaurantRepository restaurantRepository;
	@Autowired private RestaurantService restaurantService;
		
	
	@Test
	@DisplayName("Restaurant repository is not empty")
	void checkRestaurantRepository() throws Exception {	
		
		assertNotNull(restaurantRepository);
	}
	
	
	
}
