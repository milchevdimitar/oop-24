# Mining Simulation

This project simulates miners, lumberjacks, farmers, wood producers and metal producers who gather resources 
over time. The metal producers and wood producers depend on what the others have done. The simulation includes 
a clock thread that ticks every 0.5 second (simulating a day), and worker threads that gather resources each day 
and add them to a common treasury.

## Project Structure

```
MiningSimulation/
├── src/
│   └── main/
│       ├── java/
│           └── com/
│               └── example/
│                   └── mining/
│                       ├── MiningSimulation.java
│                       └── models/
│                           ├── productions/
│                               ├── MetalProduction.java
│                               └── WoodProduction.java
│                           └── single_workers/
│                               ├── Farmer.java
│                               ├── Lumberjack.java
│                               └── Miner.java
│                           └── treasuries/
│                               ├── Treasury.java
│                               └── ProductionsTreasury.java
│                           ├── Clock.java
│                           └── Worker.java
│       └── resources/
├── reports/
├── saved_resources.txt
├── saved_production_resources.txt
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

- **WoodProduction:** Subclass of Worker, simulating wood producing.
- **MetalProduction:** Subclass of Worker, simulating metal producing.
- **Treasury:** Holds what Miner, Lumberjack and Farmer have done as resources.
- **ProductionsTreasury:** Holds what WoodProduction and MetalProduction have done as resources.

## License

This project is licensed under the MIT License.