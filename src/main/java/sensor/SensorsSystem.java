package sensor;

import java.util.HashMap;
import java.util.Map;

public class SensorsSystem {
	
	private Map <String, Sensor> sensors = new HashMap<String, Sensor>();
	
	private void initSensors(Map <String, Sensor> sensors) {
		sensors.put("Temperature", newSensor("Temperature", 1));
		sensors.put("Humidity", newSensor("Temperature", 2));
		sensors.put("Light", newSensor("Light", 3));
		sensors.put("Sound", newSensor("Sound", 4));
		
	}
	
	private Sensor newSensor(String sensorName, int channelIndex) {
		Sensor s = new Sensor();
		s.setName(sensorName);
		s.addToChannel(channelIndex);
		return s;
	}
	
	public Map<String, Float> performSensorSweep() {
		Map<String, Float> results = new HashMap<String, Float>();
		sensors.forEach((name, sensor) -> readSensorIn(sensor, results));		
		return results;
	}
	
	private void readSensorIn(Sensor s, Map<String, Float> results) {
		s.read();
		results.put(s.getName(), s.getValue());
	}
	
	public SensorsSystem() {
		initSensors(sensors);
	}

	public Map<String, Float> readSensor(String sensorName) {
		assert sensors.containsKey(sensorName): sensorName + " is not a known sensor";
		Map<String, Float> results = new HashMap<String, Float>();
		readSensorIn(sensors.get(sensorName), results);
		return results;
	}
}
