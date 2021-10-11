package sensor;

import java.util.Map;

public abstract class AbstractSensorCommand {
    public abstract Map<String, Float> execute(SensorsSystem system);
}
