package sensor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SensorMonitoringApp {

    private Boolean isRunning;
    private SensorsSystem sensorsSystem;
    private Map<String, AbstractSensorCommand> commandMap;
    private Scanner scanner;


    public SensorMonitoringApp() {
        isRunning = false;
        sensorsSystem = new SensorsSystem();
        scanner = new Scanner(System.in);

        commandMap = new HashMap<String, AbstractSensorCommand>();
        commandMap.put("temperature", new ReadTemperatureSensorCommand());
        commandMap.put("sound", new ReadSoundSensorCommand());
        commandMap.put("light", new ReadLightSensorCommand());
        commandMap.put("humidity", new ReadHumiditySensorCommand());
        commandMap.put("all", new ReadAllSensorsCommand());
    }

    public void run() {
        isRunning = true;
        while (isRunning) {
            String inputCommand = scanner.nextLine();
            if (inputCommand == "quit") {
                isRunning = false;
                System.out.println("Quitting");
                break;
            }

            System.out.println("===============================");
            getCommandFromInput(inputCommand.toLowerCase()).execute(sensorsSystem).forEach((sensorName, sensorValue)
                    -> System.out.println(sensorName + ": " + sensorValue));
            System.out.println("===============================");

        }
    }

    private AbstractSensorCommand getCommandFromInput(String inputCommand) {
        return commandMap.containsKey(inputCommand) ? commandMap.get(inputCommand) : new NullSensorCommand();
    }
}
