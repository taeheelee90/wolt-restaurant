package wolt.summer2021.module;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import wolt.summer2021.module.Restaurant.RestaurantService;
import wolt.summer2021.module.Restaurant.Section;

@RestController
@AllArgsConstructor
public class MainController {

	private final RestaurantService restaurantService;
	
	@GetMapping("/discovery")
	public Section main(@RequestParam double lat , @RequestParam double lon) {			
		Section discovery = restaurantService.restaurantsInMyArea(lat, lon);
		return discovery;
	}
}
