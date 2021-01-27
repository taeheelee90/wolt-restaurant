package wolt.summer2021.module;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import wolt.summer2021.module.Restaurant.RestaurantService;
import wolt.summer2021.module.Restaurant.RestaurantVO;
import wolt.summer2021.module.user.User;
import wolt.summer2021.module.user.UserService;

@RestController
@AllArgsConstructor
public class MainController {

	private final RestaurantService restaurantService;
	private final UserService userService;

	@GetMapping("/")
	public List<RestaurantVO> main(Model model) {
		User user = userService.getUserLocation();		
		List<RestaurantVO> discovery = restaurantService.restaurantsInMyArea(user);
		return discovery;

	}
}
