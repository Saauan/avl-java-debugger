package sensor;

import java.util.HashMap;
import java.util.Map;

public class NullSensorCommand extends AbstractSensorCommand {

    public Map<String, Float> execute(SensorsSystem system) {
        Map<String, Float> results = new HashMap<String, Float>();
        results.put("This command does not exist", Float.NaN);
        return results;
    }
}
