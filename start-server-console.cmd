

:: Double-clicking on the jar file opens it with "javaw", so no console,
:: and it can be hard to track down which task to shutdown.
:: This runs the jar with a console window, so can "Ctrl-C" to shutdown.

java -jar target/shithead-0.0.1-SNAPSHOT.jar

pause