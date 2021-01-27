package wolt.summer2021.module.user;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	@Autowired
	private final HttpServletRequest request;
	private final RestTemplate restTemplate;

	final String IP_RENDER_API = "http://ip-api.com/json/{query}?fields=lat,lon";

	public User getUserLocation() {

		// Get user IP address
		String userIP = "94.237.113.214";// request.getRemoteAddr();

		// Covert user IP address to location
		String IP_RENDER_API = "http://ip-api.com/json/" + userIP + "?fields=lat,lon";

		// No HttpEntity explicitly required to use ip-api.com
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);

		User user = restTemplate.exchange(IP_RENDER_API, HttpMethod.GET, entity, User.class).getBody();

		return user;
	}
}
