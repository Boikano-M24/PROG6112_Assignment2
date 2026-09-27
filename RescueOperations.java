package wildliferescue;

/**
 * Contract that every rescue case type must fulfil.
 * Demonstrates the use of an interface and, through its different
 * implementations, polymorphism and method overriding.
 */
public interface RescueOperations {

    /**
     * Starts the rescue operation, updating the case status accordingly.
     */
    void startRescueOperation();

    /**
     * Completes the rescue operation, updating the case status accordingly.
     */
    void completeRescueOperation();

    /**
     * Builds a human-readable rescue summary containing:
     * Rescue Case ID, Rescue Type, Species, Assigned Ranger,
     * Rescue Priority, Current Status and Total Rescue Cost.
     */
    String generateRescueSummary();
}
