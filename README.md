# Mining Simulation

This project simulates miners, lumberjacks, and farmers who gather resources over time. The simulation includes a clock thread that ticks every second (simulating a day), and worker threads that gather resources each day and add them to a common treasury.

## Project Structure

```
MiningSimulation/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── mining/
│       │           │   ├── Clock.java
│       │           │   ├── Worker.java
│       │           │   ├── Miner.java
│       │           │   ├── Lumberjack.java
│       │           │   ├── Farmer.java
│       │           │   └── MiningSimulation.java
│       └── resources/
├── reports/
├── .gitignore
├── README.md
├── build.gradle (или pom.xml)
└── settings.gradle (или съответно за Maven)
```

## How to Run

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/MiningSimulation.git
   ```
2. Navigate to the project directory:
   ```
   cd MiningSimulation
   ```
3. Build and run the project using Gradle or Maven:
   ```
   ./gradlew run
   ```
   or
   ```
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.example.mining.MiningSimulation"
   ```

## Project Details

- **Clock:** A thread that counts time and notifies workers every second.
- **Worker:** Abstract class representing a worker.
- **Miner:** Subclass of Worker, simulating gold mining.
- **Lumberjack:** Subclass of Worker, simulating wood cutting.
- **Farmer:** Subclass of Worker, simulating grain harvesting.

## License

This project is licensed under the MIT License.
