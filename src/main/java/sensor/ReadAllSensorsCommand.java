package sensor;

import java.util.Map;

public class ReadAllSensorsCommand extends AbstractSensorCommand {

    public Map<String, Float> execute(SensorsSystem system) {

        return system.performSensorSweep();
    }

}
