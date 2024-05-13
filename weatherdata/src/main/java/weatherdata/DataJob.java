package weatherdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@DisallowConcurrentExecution
public class DataJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(DataJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			processData();
		} catch (Exception e) {
			logger.error("Data job failed!", e);
		}

	}

	private void processData() throws Exception {
		logger.info("Data job started!");
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(
				"http://api.openweathermap.org/data/2.5/weather?q=Novi%20Sad&appid=5ce3ea215d939fdc8aacdfeac41aa7d6")
				.build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful()) {
				String responseData = response.body().string();
				ObjectMapper objectMapper = new ObjectMapper()
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
				objectMapper.registerModule(new JavaTimeModule());
				WeatherResponse entity = objectMapper.readValue(responseData, WeatherResponse.class);
				String connUrl = "jdbc:postgresql://host.docker.internal:5432/weatherdb?user=milos&password=milos";
				try (Connection conn = DriverManager.getConnection(connUrl)) {
					try (PreparedStatement stmt = conn.prepareStatement(
							"INSERT INTO weather_data (time, location, current_temperature) VALUES (?, ?, ?)")) {
						stmt.setTimestamp(1, new Timestamp(entity.getDt().getTime()));
						stmt.setString(2, entity.getName());
						stmt.setDouble(3, entity.getMain().getTemp());
						stmt.executeUpdate();
					}
				}
			} else {
				System.out.println("Failed to fetch weather data: " + response.code());
			}
		}
		logger.info("Data job completed!");
	}
}
