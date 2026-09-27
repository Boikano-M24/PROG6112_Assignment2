# WildLife SA – Wildlife Rescue Operations System

A console-based Java application for recording, searching, updating and
reporting on wildlife rescue cases, built for WildLife SA conservation
staff.

## Project structure

```
wildlife-rescue-system/
├── pom.xml
├── src/
│   ├── main/java/wildliferescue/
│   │   ├── RescueOperations.java        (interface)
│   │   ├── RescueCase.java              (abstract base class)
│   │   ├── InjuredAnimalRescue.java     (concrete subclass)
│   │   ├── OrphanedAnimalRescue.java    (concrete subclass)
│   │   ├── EndangeredSpeciesRescue.java (concrete subclass)
│   │   ├── RescueManager.java           (ArrayList-backed store)
│   │   ├── InputValidator.java          (console input validation)
│   │   └── WildlifeRescueApp.java       (main menu / entry point)
│   └── test/java/wildliferescue/
│       └── RescueCaseTest.java          (JUnit 5 tests)
```

## Design / OOP principles used

- **Abstraction & Inheritance** – `RescueCase` is an abstract class holding
  all fields and behaviour common to every rescue case. `InjuredAnimalRescue`,
  `OrphanedAnimalRescue` and `EndangeredSpeciesRescue` extend it and add
  their own type-specific fields.
- **Interfaces & Polymorphism** – `RescueOperations` defines
  `startRescueOperation()`, `completeRescueOperation()` and
  `generateRescueSummary()`. Every rescue case is used through this
  interface/abstract type, so the same call resolves to different
  behaviour depending on the concrete rescue type.
- **Method overriding** – each subclass overrides `calculateTotalRescueCost()`,
  `determineRescuePriority()`, `getSpecificInfo()` and
  `completeRescueOperation()` to apply its own rules (e.g. surgery
  surcharge vs foster-care surcharge vs specialist-team surcharge).
- **Encapsulation** – all fields are private, exposed only via getters, and
  constructors validate their inputs before an object can be created.

## Business rules implemented

| Rescue type | Extra cost added when... | Priority rule |
|---|---|---|
| Injured Animal | Surgery Required → +R5 000 | Surgery required → Critical; vet cost > R5 000 → High; else Medium |
| Orphaned Animal | Foster Care Required → +R2 500 | Age ≤ 6 months → Critical; ≤ 12 months → High; else Medium |
| Endangered Species | Specialist Team Required → +R8 000 | "Critically Endangered" → Critical; "Endangered" → High; else Medium |

Total rescue cost always = (Daily Care Cost × Number of Rescue Days) +
type-specific cost + applicable surcharge.

## How to build and run

**Requires a JDK 17+ installation.**

### With Maven
```bash
mvn compile
mvn test
mvn package
java -jar target/wildlife-rescue-system.jar
```

### Without Maven (plain javac/java)
```bash
mkdir -p out/main
javac -d out/main src/main/java/wildliferescue/*.java
java -cp out/main wildliferescue.WildlifeRescueApp
```

### Running the unit tests without Maven
Install JUnit 5's console-standalone jar (e.g. on Debian/Ubuntu:
`apt-get install junit5`, which provides
`/usr/share/java/junit-platform-console-standalone.jar`), then:

```bash
mkdir -p out/test
javac -cp "out/main:/usr/share/java/junit-platform-console-standalone.jar" \
    -d out/test src/test/java/wildliferescue/*.java
java -jar /usr/share/java/junit-platform-console-standalone.jar \
    execute -cp out/main:out/test --scan-classpath --details=tree
```

All 14 tests pass, covering: rescue cost calculations for each rescue
type, rescue priority calculations, rescue status updates (start/complete
with type-specific completion statuses), searching for an existing rescue
case, and preventing duplicate Rescue Case IDs.

## Notes / assumptions

- The application is console-based (as permitted by the brief) with a
  menu matching the example in the assignment brief.
- Rescue cases are stored in-memory in an `ArrayList<RescueCase>` for the
  lifetime of the running application.
- Rescue Case IDs are treated as unique, case-insensitively.
- All user input is validated and re-prompted on error; the application
  never exits due to bad input.
