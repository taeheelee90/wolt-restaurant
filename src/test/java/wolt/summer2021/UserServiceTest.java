package wolt.summer2021;

import static org.junit.Assert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import wolt.summer2021.module.user.User;
import wolt.summer2021.module.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired UserService userService;
	
	@Test
	@DisplayName("User location is acquired.")
	void getUserLocationTest() {
		User user = userService.getUserLocation();
		double lat = user.getLat();
		double lon = user.getLon();
		assertNotNull(user);
		assertNotNull(lat);
		assertNotNull(lon);
	}
}
