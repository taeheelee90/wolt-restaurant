package wolt.summer2021.infra;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	// Used in UserService: convert IP to coordinate
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// Used in RestaurantService: RestaurantVO to Restaurant
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}
}
