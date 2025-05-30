# Train Station Simulator

A Java-based multithreaded simulation that models trains and passengers navigating a network of stations. This project emphasizes concurrency control, synchronization, and event-driven design, making it a valuable resource for understanding complex thread interactions in a simulated environment.

## Features

- **Multithreaded Simulation**: Each train and passenger operates on its own thread.
- **Synchronization Mechanisms**: Uses locks and conditions for thread-safe interaction.
- **Event Logging**: Records boarding, deboarding, and train movement events.
- **Configurable Setup**: Train and passenger data are defined in configuration files.
- **Verification Tools**: Ensures simulation correctness via log analysis.

## Project Structure

### `Config.java`

- Maps each passenger to a unique lock and condition.
- Stores all station, train, and passenger info.
- Offers helper functions to query simulation state (e.g. `hasArrived()`, `atStation()`).

### `PassengerThread.java`

- Represents a single passenger’s logic.
- Waits to be signaled by the train at a station.
- Determines if it should board or deboard based on its current state.

### `TrainThread.java`

Each train operates in two synchronized phases:
1. **Wake-Up Phase**: Alerts all passengers at the current station and onboard to take their actions.
2. **Move Phase**: If the next station is unoccupied, the train moves to it with a 5 ms delay (to allow for concurrency fairness).

### Event Logging

- `BoardEvent`, `DeboardEvent`, and `MoveEvent` represent core simulation events.
- All events are logged and used by `Verify.java` to ensure correctness.



