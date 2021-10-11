# TD2

## Bugs relevés

### Bug 1
Lorsqu'on utilise la commande ``temperature`` le programme plante avec l'erreur 
````
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "sensor.Sensor.read()" because "s" is null
	at sensor.SensorsSystem.readSensorIn(SensorsSystem.java:32)
	at sensor.SensorsSystem.readSensor(SensorsSystem.java:43)
	at sensor.ReadTemperatureSensorCommand.execute(ReadTemperatureSensorCommand.java:9)
	at sensor.SensorMonitoringApp.run(SensorMonitoringApp.java:39)
	at sensor.Run.main(Run.java:9)
````

### Bug 2
Lors de la commande `all`, le programme n'affiche pas l'humidité.
````
all
===============================
Temperature: 8.0
Light: 8.0
Sound: 8.0
===============================
````

### Bug 3
La commande ``quit`` ne fonctionne pas.

````
quit
===============================
This command does not exist: NaN
===============================
````

## Breakpoints
