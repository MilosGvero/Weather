package weatherdata;


import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class WeatherResponse {

	@JsonDeserialize(using = SecondsToDateDeserializer.class)
	private Date dt;
	private String name;
	private MainResponse main;

	public MainResponse getMain() {
		return main;
	}

	public void setMain(MainResponse main) {
		this.main = main;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
