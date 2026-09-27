package wildliferescue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the collection of {@link RescueCase} objects for the
 * Wildlife Rescue Operations System. Rescue cases are stored in an
 * {@link ArrayList} as required by the assignment brief.
 */
public class RescueManager {

    private final List<RescueCase> rescueCases = new ArrayList<>();

    /**
     * Adds a new rescue case, rejecting duplicate Rescue Case IDs.
     *
     * @throws IllegalArgumentException if a case with the same ID already exists
     */
    public void addRescueCase(RescueCase rescueCase) {
        if (rescueCase == null) {
            throw new IllegalArgumentException("Rescue case cannot be null.");
        }
        if (isDuplicateId(rescueCase.getRescueCaseId())) {
            throw new IllegalArgumentException(
                    "A rescue case with ID '" + rescueCase.getRescueCaseId() + "' already exists.");
        }
        rescueCases.add(rescueCase);
    }

    /** Returns true if a rescue case with the given ID already exists (case-insensitive). */
    public boolean isDuplicateId(String rescueCaseId) {
        if (rescueCaseId == null) {
            return false;
        }
        return rescueCases.stream()
                .anyMatch(rc -> rc.getRescueCaseId().equalsIgnoreCase(rescueCaseId.trim()));
    }

    /** Searches for a rescue case by its ID (case-insensitive). */
    public Optional<RescueCase> findByCaseId(String rescueCaseId) {
        if (rescueCaseId == null) {
            return Optional.empty();
        }
        return rescueCases.stream()
                .filter(rc -> rc.getRescueCaseId().equalsIgnoreCase(rescueCaseId.trim()))
                .findFirst();
    }

    /** Returns an unmodifiable view of all rescue cases currently stored. */
    public List<RescueCase> getAllRescueCases() {
        return List.copyOf(rescueCases);
    }

    public int getTotalCaseCount() {
        return rescueCases.size();
    }

    public double getTotalEstimatedCost() {
        return rescueCases.stream().mapToDouble(RescueCase::calculateTotalRescueCost).sum();
    }

    /**
     * Builds the full operational rescue report as a String, listing every
     * rescue case followed by summary totals.
     */
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(45)).append(System.lineSeparator());
        sb.append("WILDLIFE RESCUE REPORT").append(System.lineSeparator());
        sb.append("=".repeat(45)).append(System.lineSeparator());

        if (rescueCases.isEmpty()) {
            sb.append("No rescue cases recorded yet.").append(System.lineSeparator());
        } else {
            for (RescueCase rc : rescueCases) {
                sb.append(System.lineSeparator());
                sb.append("Case ID   : ").append(rc.getRescueCaseId()).append(System.lineSeparator());
                sb.append("Type      : ").append(rc.getRescueType()).append(System.lineSeparator());
                sb.append("Species   : ").append(rc.getSpecies()).append(System.lineSeparator());
                sb.append("Location  : ").append(rc.getRescueLocation()).append(System.lineSeparator());
                sb.append("Ranger    : ").append(rc.getAssignedRanger()).append(System.lineSeparator());
                sb.append("Priority  : ").append(rc.determineRescuePriority()).append(System.lineSeparator());
                sb.append("Status    : ").append(rc.getCurrentStatus()).append(System.lineSeparator());
                sb.append(String.format("Total Cost: R%.2f%n", rc.calculateTotalRescueCost()));
                sb.append("-".repeat(45)).append(System.lineSeparator());
            }
        }

        sb.append(System.lineSeparator());
        sb.append("Total Rescue Cases : ").append(getTotalCaseCount()).append(System.lineSeparator());
        sb.append(String.format("Total Rescue Cost  : R%.2f%n", getTotalEstimatedCost()));
        return sb.toString();
    }
}
