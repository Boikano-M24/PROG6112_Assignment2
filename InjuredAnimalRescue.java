package wildliferescue;

/**
 * Rescue case for an injured animal. Total cost includes veterinary
 * treatment costs, plus a R5 000 surcharge when surgery is required.
 */
public class InjuredAnimalRescue extends RescueCase {

    public static final double SURGERY_SURCHARGE = 5000.0;

    private final String injuryDescription;
    private final double veterinaryTreatmentCost;
    private final boolean surgeryRequired;

    public InjuredAnimalRescue(String rescueCaseId,
                                String animalName,
                                String species,
                                String rescueLocation,
                                String assignedRanger,
                                int numberOfRescueDays,
                                double dailyCareCost,
                                String injuryDescription,
                                double veterinaryTreatmentCost,
                                boolean surgeryRequired) {

        super(rescueCaseId, animalName, species, rescueLocation, assignedRanger,
                numberOfRescueDays, dailyCareCost);

        if (injuryDescription == null || injuryDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Injury Description cannot be blank.");
        }
        if (veterinaryTreatmentCost < 0) {
            throw new IllegalArgumentException("Veterinary Treatment Cost cannot be negative.");
        }

        this.injuryDescription = injuryDescription.trim();
        this.veterinaryTreatmentCost = veterinaryTreatmentCost;
        this.surgeryRequired = surgeryRequired;
    }

    public String getInjuryDescription() {
        return injuryDescription;
    }

    public double getVeterinaryTreatmentCost() {
        return veterinaryTreatmentCost;
    }

    public boolean isSurgeryRequired() {
        return surgeryRequired;
    }

    @Override
    public double calculateTotalRescueCost() {
        double total = getBaseCareCost() + veterinaryTreatmentCost;
        if (surgeryRequired) {
            total += SURGERY_SURCHARGE;
        }
        return total;
    }

    @Override
    public String determineRescuePriority() {
        if (surgeryRequired) {
            return "Critical";
        }
        if (veterinaryTreatmentCost > 5000) {
            return "High";
        }
        return "Medium";
    }

    @Override
    public String getRescueType() {
        return "Injured Animal Rescue";
    }

    @Override
    public String getSpecificInfo() {
        return String.format(
                "Injury Description       : %s%nVeterinary Treatment Cost: R%.2f%nSurgery Required         : %s",
                injuryDescription, veterinaryTreatmentCost, surgeryRequired ? "Yes" : "No");
    }

    /** Overridden to record a more specific completed status for injured animals. */
    @Override
    public void completeRescueOperation() {
        setCurrentStatus(surgeryRequired
                ? "Rescue Completed - Post-Surgery Recovery"
                : "Rescue Completed - Released Back to Habitat");
    }
}
