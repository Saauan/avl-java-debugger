package sensor;

import java.util.Map;

public class ReadHumiditySensorCommand extends AbstractSensorCommand {

    public Map<String, Float> execute(SensorsSystem system) {

        return system.readSensor("Humidity");
    }
}
