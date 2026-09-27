package wildliferescue;

/**
 * Abstract base class representing a wildlife rescue case.
 * <p>
 * Holds all information common to every rescue case type and implements
 * {@link RescueOperations} with sensible default behaviour that concrete
 * subclasses may override (method overriding / polymorphism). Cost
 * calculation and priority determination are left abstract because they
 * differ per rescue type (abstraction).
 */
public abstract class RescueCase implements RescueOperations {

    public static final String STATUS_REPORTED = "Reported";
    public static final String STATUS_IN_PROGRESS = "Rescue In Progress";
    public static final String STATUS_COMPLETED = "Rescue Completed";

    private final String rescueCaseId;
    private final String animalName;
    private final String species;
    private final String rescueLocation;
    private final String assignedRanger;
    private final int numberOfRescueDays;
    private final double dailyCareCost;
    private String currentStatus;

    protected RescueCase(String rescueCaseId,
                          String animalName,
                          String species,
                          String rescueLocation,
                          String assignedRanger,
                          int numberOfRescueDays,
                          double dailyCareCost) {

        if (rescueCaseId == null || rescueCaseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Rescue Case ID cannot be blank.");
        }
        if (species == null || species.trim().isEmpty()) {
            throw new IllegalArgumentException("Species cannot be blank.");
        }
        if (rescueLocation == null || rescueLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("Rescue Location cannot be blank.");
        }
        if (assignedRanger == null || assignedRanger.trim().isEmpty()) {
            throw new IllegalArgumentException("Assigned Ranger cannot be blank.");
        }
        if (numberOfRescueDays <= 0) {
            throw new IllegalArgumentException("Number of Rescue Days must be greater than zero.");
        }
        if (dailyCareCost <= 0) {
            throw new IllegalArgumentException("Daily Care Cost must be greater than zero.");
        }

        this.rescueCaseId = rescueCaseId.trim();
        this.animalName = (animalName == null) ? "Unknown" : animalName.trim();
        this.species = species.trim();
        this.rescueLocation = rescueLocation.trim();
        this.assignedRanger = assignedRanger.trim();
        this.numberOfRescueDays = numberOfRescueDays;
        this.dailyCareCost = dailyCareCost;
        this.currentStatus = STATUS_REPORTED;
    }

    
    // Encapsulated accessors
    

    public String getRescueCaseId() {
        return rescueCaseId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getSpecies() {
        return species;
    }

    public String getRescueLocation() {
        return rescueLocation;
    }

    public String getAssignedRanger() {
        return assignedRanger;
    }

    public int getNumberOfRescueDays() {
        return numberOfRescueDays;
    }

    public double getDailyCareCost() {
        return dailyCareCost;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    /** Allows subclasses to set a more specific status when completing a rescue. */
    protected void setCurrentStatus(String status) {
        this.currentStatus = status;
    }

    /** Care cost shared by every rescue type: dailyCareCost * numberOfRescueDays. */
    protected double getBaseCareCost() {
        return dailyCareCost * numberOfRescueDays;
    }

    // ---------------------------------------------------------------
    // Abstraction - each rescue type implements these differently
    // ---------------------------------------------------------------

    /** Calculates the total rescue cost, including type-specific surcharges. */
    public abstract double calculateTotalRescueCost();

    /** Determines the rescue priority based on type-specific data. */
    public abstract String determineRescuePriority();

    /** Human readable name of the rescue type, e.g. "Injured Animal Rescue". */
    public abstract String getRescueType();

    /** Information specific to the rescue type (for detailed displays/reports). */
    public abstract String getSpecificInfo();

    
    // RescueOperations - default behaviour, overridable by subclasses
    

    @Override
    public void startRescueOperation() {
        if (STATUS_REPORTED.equals(currentStatus)) {
            currentStatus = STATUS_IN_PROGRESS;
        }
    }

    @Override
    public void completeRescueOperation() {
        currentStatus = STATUS_COMPLETED;
    }

    @Override
    public String generateRescueSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rescue Case ID  : ").append(rescueCaseId).append(System.lineSeparator());
        sb.append("Rescue Type     : ").append(getRescueType()).append(System.lineSeparator());
        sb.append("Species         : ").append(species).append(System.lineSeparator());
        sb.append("Assigned Ranger : ").append(assignedRanger).append(System.lineSeparator());
        sb.append("Rescue Priority : ").append(determineRescuePriority()).append(System.lineSeparator());
        sb.append("Current Status  : ").append(currentStatus).append(System.lineSeparator());
        sb.append(String.format("Total Cost      : R%.2f", calculateTotalRescueCost()));
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s) | Status: %s | Priority: %s | Cost: R%.2f",
                rescueCaseId, getRescueType(), animalName, species,
                currentStatus, determineRescuePriority(), calculateTotalRescueCost());
    }
}
