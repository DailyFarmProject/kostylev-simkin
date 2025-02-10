package telran.daily_farm.location.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class LocationIqResponse {
	
	private String lat;
	private String lon;
	private Map<String, String> address;
	private double importance;

}
