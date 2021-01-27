package wolt.summer2021.module.Restaurant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	List<Restaurant> findByOnlineOrderByPopularityDesc(boolean online);
	
	List<Restaurant> findByOnlineOrderByLaunchDateDesc(boolean online);

	List<Restaurant> findByOnline(boolean online);

}
