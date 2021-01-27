package wolt.summer2021.module.Restaurant;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Restaurant {

	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

	private String blurhash;

	private LocalDate launchDate;

	private double longitude;

	private double latitude;

	private String name;

	private boolean online;

	private double popularity;

	public boolean opendFourMonthsAgo() {
		return this.launchDate.isBefore(LocalDate.now().minusMonths(4));
	}
}
