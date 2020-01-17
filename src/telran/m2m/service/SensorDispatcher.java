package telran.m2m.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.m2m.dao.SensorsRepository;
import telran.m2m.dto.Sensor;

@EnableBinding(IDispatcher.class)
public class SensorDispatcher {
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	IDispatcher dispatcher;
	@Autowired
	SensorsRepository repository;

	@Value("${min_data_pressure:20}")
	private int minDataPressure;
	@Value("${max_data_pressure:70}")
	private int maxDataPressure;

	@Value("${min_dataPuls:45}")
	private int minDataPuls;
	@Value("${max_dataPuls:200}")
	private int maxDataPuls;

	@Value("${min_dataSugar:60}")
	private int minDataSugar;
	@Value("${max_dataSugar:150}")
	private int maxDataSugar;
	@Value("${period:60}")
	int period;

	List<Sensor> listSensors = new LinkedList<>();
	List<Sensor> sendList = new LinkedList<>();
	Instant current = null;

	@StreamListener(IDispatcher.INPUT)
	public void getSensorData(String sensorJson) throws JsonParseException, IOException, IOException {
		Sensor sensor = mapper.readValue(sensorJson, Sensor.class);
		int deltaPressure = sensor.getDataUBP() - sensor.getDataLBP();

		if (deltaPressure < minDataPressure || deltaPressure > maxDataPressure) {
			dispatcher.dangerPressure().send(MessageBuilder.withPayload(sensorJson).build());
		}

		if (sensor.getDataPuls() < minDataPuls || sensor.getDataPuls() > maxDataPuls) {
			dispatcher.dangerPuls().send(MessageBuilder.withPayload(sensorJson).build());
		}

		if (sensor.getDataSugar() < minDataSugar || sensor.getDataSugar() > maxDataSugar) {
			dispatcher.dangerSugar().send(MessageBuilder.withPayload(sensorJson).build());
		}
		listSensors.add(sensor);
		if (current == null) {
			current = Instant.now();
		} else {
			long currentPeriod = ChronoUnit.SECONDS.between(current, Instant.now());
			if (currentPeriod > period) {
				repository.saveAll(getListSensorsAVG(listSensors));
				listSensors.clear();
				sendList.clear();
				current = null;
			}
		}
	}

	private List<Sensor> getListSensorsAVG(List<Sensor> listSensors) {
		Map<Integer, Integer> mapUBP = new HashMap<>();
		Map<Integer, Integer> mapLBP = new HashMap<>();
		Map<Integer, Integer> mapPulse = new HashMap<>();
		Map<Integer, Integer> mapSugar = new HashMap<>();
		Map<Integer, Integer> mapCount = new HashMap<>();

		for (Sensor sensor : listSensors) {
			int id = sensor.getId();
			mapCount.merge(id, 1, Integer::sum);
			mapUBP.merge(id, sensor.getDataUBP(), Integer::sum);
			mapLBP.merge(id, sensor.getDataLBP(), Integer::sum);
			mapPulse.merge(id, sensor.getDataPuls(), Integer::sum);
			mapSugar.merge(id, sensor.getDataSugar(), Integer::sum);
		}
		Set<Integer> keys = mapCount.keySet();
		for (Integer key : keys) {
			Sensor sensor_avg = new Sensor(getStringTime(), key, (int) (mapUBP.get(key) / mapCount.get(key)),
					(int) (mapLBP.get(key) / mapCount.get(key)), (int) (mapPulse.get(key) / mapCount.get(key)),
					(int) (mapSugar.get(key) / mapCount.get(key)));
			sendList.add(sensor_avg);
		}
		return sendList;  

	}

	private String getStringTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS");
		return LocalDateTime.now().format(dtf);
	}

}
