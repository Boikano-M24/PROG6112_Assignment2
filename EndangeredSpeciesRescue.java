package wildliferescue;

/**
 * Rescue case for an endangered species. Total cost includes security
 * costs, plus an R8 000 surcharge when a specialist team is required.
 */
public class EndangeredSpeciesRescue extends RescueCase {

    public static final double SPECIALIST_TEAM_SURCHARGE = 8000.0;

    private final String conservationClassification;
    private final double securityCost;
    private final boolean specialistTeamRequired;

    public EndangeredSpeciesRescue(String rescueCaseId,
                                   String animalName,
                                   String species,
                                   String rescueLocation,
                                   String assignedRanger,
                                   int numberOfRescueDays,
                                   double dailyCareCost,
                                   String conservationClassification,
                                   double securityCost,
                                   boolean specialistTeamRequired) {

        super(rescueCaseId, animalName, species, rescueLocation, assignedRanger,
                numberOfRescueDays, dailyCareCost);

        if (conservationClassification == null || conservationClassification.trim().isEmpty()) {
            throw new IllegalArgumentException("Conservation Classification cannot be blank.");
        }
        if (securityCost < 0) {
            throw new IllegalArgumentException("Security Cost cannot be negative.");
        }

        this.conservationClassification = conservationClassification.trim();
        this.securityCost = securityCost;
        this.specialistTeamRequired = specialistTeamRequired;
    }

    public String getConservationClassification() {
        return conservationClassification;
    }

    public double getSecurityCost() {
        return securityCost;
    }

    public boolean isSpecialistTeamRequired() {
        return specialistTeamRequired;
    }

    @Override
    public double calculateTotalRescueCost() {
        double total = getBaseCareCost() + securityCost;
        if (specialistTeamRequired) {
            total += SPECIALIST_TEAM_SURCHARGE;
        }
        return total;
    }

    @Override
    public String determineRescuePriority() {
        String classification = conservationClassification.toLowerCase();
        if (classification.contains("critically")) {
            return "Critical";
        }
        if (classification.contains("endangered")) {
            return "High";
        }
        return "Medium";
    }

    @Override
    public String getRescueType() {
        return "Endangered Species Rescue";
    }

    @Override
    public String getSpecificInfo() {
        return String.format(
                "Conservation Classification: %s%nSecurity Cost              : R%.2f%nSpecialist Team Required   : %s",
                conservationClassification, securityCost, specialistTeamRequired ? "Yes" : "No");
    }

    /** Overridden to record a more specific completed status for endangered species. */
    @Override
    public void completeRescueOperation() {
        setCurrentStatus(specialistTeamRequired
                ? "Rescue Completed - Relocated Under Specialist Escort"
                : "Rescue Completed - Relocated to Protected Reserve");
    }
}
