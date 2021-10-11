package sensor;

import java.util.Map;

public class ReadTemperatureSensorCommand extends AbstractSensorCommand {

    public Map<String, Float> execute(SensorsSystem system) {

        return system.readSensor("temperature"); // Manque majuscule
    }
}
