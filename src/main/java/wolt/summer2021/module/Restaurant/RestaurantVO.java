package wolt.summer2021.module.Restaurant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RestaurantVO {

	private String title;

	// Max number of restaurant data per list is limited to 10
	@JsonInclude(Include.NON_NULL)
	private List<RestaurantData> restaurants = new ArrayList<RestaurantData>();
	@Data
	public static class RestaurantData {

		private String blurhash;

		@JsonProperty("launch_date")
		private LocalDate launchDate;

		private List<Double> location = new ArrayList<Double>();

		private String name;

		private boolean online;

		private double popularity;

	}
}
