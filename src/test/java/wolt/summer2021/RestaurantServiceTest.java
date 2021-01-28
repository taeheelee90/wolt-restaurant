package wolt.summer2021;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import wolt.summer2021.module.Restaurant.Restaurant;
import wolt.summer2021.module.Restaurant.RestaurantService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantServiceTest {

	@Autowired
	private RestaurantService restaurantService;
	double lat = 60.1709;
	double lon = 24.941;
	double notInHelLat = 10; // field to test location not in Helsinki

	@Test
	@DisplayName("Popular restaurnts list - valid coordinates")
	void checkPopularList() {
		List<Restaurant> popularList = restaurantService.popularList(lon, lat);
		assertNotNull(popularList);
		assertTrue(popularList.size() <= 10);
		assertTrue(checkDistanceOfAllRestaurants(popularList));
		for (int i = 0; i < popularList.size() - 1; i++) {
			int j = i + 1;
			assertTrue(popularList.get(i).getPopularity() > popularList.get(j).getPopularity());
		}
	}

	@Test
	@DisplayName("Popular restaurnts list - invalid coordinates")
	void checkPopularList_inValidLat() {
		List<Restaurant> popularList = restaurantService.popularList(lon, notInHelLat);
		assertTrue(popularList.isEmpty());
		assertTrue(popularList.size() == 0);
		restaurantService.restaurantsInMyArea(notInHelLat, lon).getSections().isEmpty();
	}

	@Test
	@DisplayName("New restaurants list - valid coordinates")
	void checkNewList() {
		List<Restaurant> newList = restaurantService.newList(lon, lat);
		assertNotNull(newList);
		assertTrue(newList.size() <= 10);
		assertTrue(checkDistanceOfAllRestaurants(newList));
		for (Restaurant r : newList) {
			assertTrue(!r.opendFourMonthsAgo());
		}
		for (int i = 0; i < newList.size() - 1; i++) {
			int j = i + 1;
			assertTrue(newList.get(i).getLaunchDate().isAfter(newList.get(j).getLaunchDate()));
		}
	}

	@Test
	@DisplayName("New restaurnts list - invalid coordinates")
	void checkNewList_inValidLat() {
		List<Restaurant> newList = restaurantService.newList(lon, notInHelLat);
		assertTrue(newList.isEmpty());
		assertTrue(newList.size() == 0);
		restaurantService.restaurantsInMyArea(notInHelLat, lon).getSections().isEmpty();
	}

	@Test
	@DisplayName("Nearby restaurants list - valid coordinates")
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

	@Test
	@DisplayName("Nearby restaurnts list - invalid coordinates")
	void checkNearByList_inValidLat() {
		List<Restaurant> nearByList = restaurantService.nearByList(lon, notInHelLat);
		assertTrue(nearByList.isEmpty());
		assertTrue(nearByList.size() == 0);
		assertTrue(restaurantService.restaurantsInMyArea(notInHelLat, lon).getSections().isEmpty());
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
