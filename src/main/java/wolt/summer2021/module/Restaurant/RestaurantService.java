package wolt.summer2021.module.Restaurant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import wolt.summer2021.module.Restaurant.RestaurantVO.RestaurantData;
import wolt.summer2021.module.user.User;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

	private final ObjectMapper objectMapper;
	private final ModelMapper modelMapper;
	private final RestaurantRepository repository;

	// Automatically save restaurant data from 'restaurants.json' to DB
	@PostConstruct
	public void initRestaurantData() throws IOException {
		// 1. Read data from file
		File resource = new ClassPathResource("restaurants.json").getFile();
		String restaurants = new String(Files.readAllBytes(resource.toPath()));

		// 2. Deserialize JSON content to RestaurantVO class
		RestaurantVO restaurantVO = objectMapper.readValue(restaurants, RestaurantVO.class);

		// 3. Save restaurant data to DB
		RestaurantData[] restaurantData = restaurantVO.getRestaurants();
		for (RestaurantData data : restaurantData) {
			Restaurant restaurant = modelMapper.map(data, Restaurant.class);
			restaurant.setLongitude(data.getLocation().get(0));
			restaurant.setLatitude(data.getLocation().get(1));
			repository.save(restaurant);
		}
	}

	// Return final discovery
	public Section restaurantsInMyArea(User user) {
		// 1. Make 3 different lists
		List<Restaurant> popularRestaurants = popularList(user);
		List<Restaurant> newRestaurants = newList(user);
		List<Restaurant> nearByRestaurants = nearByList(user);

		// 2. Combine lists
		List<RestaurantVO> discovery = combineLists(popularRestaurants, newRestaurants, nearByRestaurants);
		
		// 3. Format combined lists
		Section discoverySection = new Section();
		discoverySection.setSections(discovery);
		
		return discoverySection;
	}


	// Return 10 popular restaurants in descending order 
	public List<Restaurant> popularList(User user) {
		final double userLon = user.getLon();
		final double userLat = user.getLat();
		List<Restaurant> restaurants = repository.findByOnlineOrderByPopularityDesc(true);
		restaurants = removeTooFarRestaurants(restaurants, userLon, userLat);

		if (restaurants.size() > 10) {
			restaurants = limitSizeToTen(restaurants);
		}
		return restaurants;
	}

	// Return 10 newest restaurants in descending order
	public List<Restaurant> newList(User user) {
		final double userLon = user.getLon();
		final double userLat = user.getLat();
		List<Restaurant> restaurants = repository.findByOnlineOrderByLaunchDateDesc(true);
		restaurants = removeTooFarRestaurants(restaurants, userLon, userLat);

		// Remove restaurants launched more than 4 months ago.
		restaurants.removeIf(r -> r.opendFourMonthsAgo());

		if (restaurants.size() > 10) {
			restaurants = limitSizeToTen(restaurants);
		}
		return restaurants;
	}

	// Return 10 closest restaurants in ascending order
	public List<Restaurant> nearByList(User user) {
		final double userLon = user.getLon();
		final double userLat = user.getLat();
		List<Restaurant> restaurants = repository.findByOnline(true);
		restaurants = removeTooFarRestaurants(restaurants, userLon, userLat);

		if (restaurants.size() > 10) {
			restaurants = limitSizeToTen(restaurants);
		}

		restaurants = orderByDistance(restaurants, userLon, userLat);

		return restaurants;
	}

	// Order NearBy Restaurants by distance
	private List<Restaurant> orderByDistance(List<Restaurant> restaurants, double userLon, double userLat) {
	
		for (int i = 0; i < restaurants.size() - 1; i++) {
			for (int j = 1; j < restaurants.size(); j++) {
				double d1 = calcDistance(userLon, userLat, restaurants.get(j - 1).getLongitude(),
						restaurants.get(j - 1).getLatitude());
				double d2 = calcDistance(userLon, userLat, restaurants.get(j).getLongitude(),
						restaurants.get(j).getLatitude());
				{
					if (d1 > d2) {
						Collections.swap(restaurants, j - 1, j);

					}
				}
			}
		}
		return restaurants;
	}

	// Limit the size of list to 10 restaurants
	private List<Restaurant> limitSizeToTen(List<Restaurant> restaurants) {
		List<Restaurant> toRemove = new ArrayList<>();
		for (Restaurant r : restaurants) {
			if (restaurants.indexOf(r) >= 10) {
				toRemove.add(r);
			}
		}
		restaurants.removeAll(toRemove);
		return restaurants;
	}

	// Remove restaurants located too far from user
	private List<Restaurant> removeTooFarRestaurants(List<Restaurant> restaurants, double userLon, double userLat) {
		List<Restaurant> toRemove = new ArrayList<>();

		for (Restaurant r : restaurants) {
			if (calcDistance(userLon, userLat, r.getLongitude(), r.getLatitude()) >= 1.5) {
				toRemove.add(r);
			}
		}
		restaurants.removeAll(toRemove);

		return restaurants;
	}

	// Calculate distance between User location and Restaurant
	public double calcDistance(double userLong, double userLat, double restaurantLong, double restaurantLat) {

		userLat = Math.toRadians(userLat);
		userLong = Math.toRadians(userLong);
		restaurantLat = Math.toRadians(restaurantLat);
		restaurantLong = Math.toRadians(restaurantLong);

		double earthRadius = 6371.01; // for KM
		return earthRadius * Math.acos(Math.sin(userLat) * Math.sin(restaurantLat)
				+ Math.cos(userLat) * Math.cos(restaurantLat) * Math.cos(userLong - restaurantLong));
	}
	

	// Combine all three lists to one list
	private List<RestaurantVO> combineLists(List<Restaurant> popularRestaurants, List<Restaurant> newRestaurants,
			List<Restaurant> nearByRestaurants) {
		
		List<RestaurantVO> discovery = new ArrayList<>();
		
		RestaurantVO vo1 = new RestaurantVO();
		RestaurantVO vo2 = new RestaurantVO();
		RestaurantVO vo3 = new RestaurantVO();
		
		vo1.setTitle("Popular Restaurants");
		mapRestaurantsToData(popularRestaurants, vo1);		
		discovery.add(vo1);
		
		vo2.setTitle("New Restaurants");
		mapRestaurantsToData(newRestaurants, vo2);
		discovery.add(vo2);

		
		vo3.setTitle("Nearby Restaurants");
		mapRestaurantsToData(nearByRestaurants, vo3);
		discovery.add(vo3);
		
		return discovery;
	}

	// Map Restaurants to RestaurantData
	private void mapRestaurantsToData(List<Restaurant> restaurants, RestaurantVO vo) {
		for (Restaurant r : restaurants) {
			RestaurantData data = modelMapper.map(r, RestaurantData.class);
			data.getLocation().add(r.getLongitude());
			data.getLocation().add(r.getLatitude());
			vo.getRestaurants()[restaurants.indexOf(r)] = data;
		}		
	}
}
