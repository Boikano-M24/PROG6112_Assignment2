package wildliferescue;

/**
 * Rescue case for an orphaned animal. Total cost includes feeding costs,
 * plus a R2 500 surcharge when foster care is required.
 */
public class OrphanedAnimalRescue extends RescueCase {

    public static final double FOSTER_CARE_SURCHARGE = 2500.0;

    private final int estimatedAgeMonths;
    private final double feedingCost;
    private final boolean fosterCareRequired;

    public OrphanedAnimalRescue(String rescueCaseId,
                                 String animalName,
                                 String species,
                                 String rescueLocation,
                                 String assignedRanger,
                                 int numberOfRescueDays,
                                 double dailyCareCost,
                                 int estimatedAgeMonths,
                                 double feedingCost,
                                 boolean fosterCareRequired) {

        super(rescueCaseId, animalName, species, rescueLocation, assignedRanger,
                numberOfRescueDays, dailyCareCost);

        if (estimatedAgeMonths <= 0) {
            throw new IllegalArgumentException("Estimated Age (Months) must be greater than zero.");
        }
        if (feedingCost < 0) {
            throw new IllegalArgumentException("Feeding Cost cannot be negative.");
        }

        this.estimatedAgeMonths = estimatedAgeMonths;
        this.feedingCost = feedingCost;
        this.fosterCareRequired = fosterCareRequired;
    }

    public int getEstimatedAgeMonths() {
        return estimatedAgeMonths;
    }

    public double getFeedingCost() {
        return feedingCost;
    }

    public boolean isFosterCareRequired() {
        return fosterCareRequired;
    }

    @Override
    public double calculateTotalRescueCost() {
        double total = getBaseCareCost() + feedingCost;
        if (fosterCareRequired) {
            total += FOSTER_CARE_SURCHARGE;
        }
        return total;
    }

    @Override
    public String determineRescuePriority() {
        if (estimatedAgeMonths <= 6) {
            return "Critical";
        }
        if (estimatedAgeMonths <= 12) {
            return "High";
        }
        return "Medium";
    }

    @Override
    public String getRescueType() {
        return "Orphaned Animal Rescue";
    }

    @Override
    public String getSpecificInfo() {
        return String.format(
                "Estimated Age (Months): %d%nFeeding Cost          : R%.2f%nFoster Care Required  : %s",
                estimatedAgeMonths, feedingCost, fosterCareRequired ? "Yes" : "No");
    }

    /** Overridden to record a more specific completed status for orphaned animals. */
    @Override
    public void completeRescueOperation() {
        setCurrentStatus(fosterCareRequired
                ? "Rescue Completed - Placed in Foster Care"
                : "Rescue Completed - Reunited/Released");
    }
}
