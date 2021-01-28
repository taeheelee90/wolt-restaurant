package wolt.summer2021;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import wolt.summer2021.module.Restaurant.Restaurant;
import wolt.summer2021.module.Restaurant.RestaurantRepository;
import wolt.summer2021.module.Restaurant.RestaurantService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantServiceTest {

	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private RestaurantService restaurantService;
	double lat;
	double lon;

	@BeforeEach()
	void beforeEach() throws IOException {
		restaurantService.initRestaurantData();
		lat = 60.1709;
		lon = 24.941;
	}

	@Test
	@DisplayName("Restaurant repository is not empty")
	void checkRestaurantRepository() throws Exception {
		assertNotNull(restaurantRepository);
	}

	@Test
	@DisplayName("Popular restaurnts list")
	void checkPopularList() {
		List<Restaurant> popularList = restaurantService.popularList(lon, lat);
		assertNotNull(popularList);
		assertTrue(popularList.size() <= 10);
		assertTrue(checkDistanceOfAllRestaurants(popularList));
	}

	@Test
	@DisplayName("New restaurants list")
	void checkNewList() {
		List<Restaurant> newList = restaurantService.newList(lon, lat);
		assertNotNull(newList);
		assertTrue(newList.size() <= 10);
		assertTrue(checkDistanceOfAllRestaurants(newList));
		for (Restaurant r : newList) {
			assertTrue(!r.opendFourMonthsAgo());
		}
	}

	@Test
	@DisplayName("Nearby restaurants list")
	void checkNearByList() {
		List<Restaurant> nearByList = restaurantService.nearByList(lon, lat);
		assertNotNull(nearByList);
		assertTrue(nearByList.size() <= 10);
		assertTrue(checkDistanceOfAllRestaurants(nearByList));

		for (int i = 0; i < nearByList.size() - 1; i++) {
			int j = i + 1;

			double distanceI = restaurantService.calcDistance(lon, lat, nearByList.get(i).getLongitude(),
					nearByList.get(i).getLatitude());
			double distanceJ = restaurantService.calcDistance(lon, lat, nearByList.get(j).getLongitude(),
					nearByList.get(j).getLatitude());

			assertTrue(distanceI < distanceJ);
		}
	}

	private boolean checkDistanceOfAllRestaurants(List<Restaurant> list) {
		boolean result = true;
		for (Restaurant r : list) {
			double distance = restaurantService.calcDistance(lon, lat, r.getLongitude(), r.getLatitude());
			if (distance > 1.5)
				result = false;
		}
		return result;
	}

}
