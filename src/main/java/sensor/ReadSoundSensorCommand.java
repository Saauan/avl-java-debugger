package sensor;

import java.util.Map;

public class ReadSoundSensorCommand extends AbstractSensorCommand {

    public Map<String, Float> execute(SensorsSystem system) {

        return system.readSensor("Sound");
    }
}
