package wildliferescue;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console entry point for the WildLife SA Wildlife Rescue Operations System.
 */
public class WildlifeRescueApp {

    private final Scanner scanner = new Scanner(System.in);
    private final RescueManager manager = new RescueManager();

    public static void main(String[] args) {
        new WildlifeRescueApp().run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputValidator.readMenuOption(scanner, "Select an option: ", 1, 6);
            switch (choice) {
                case 1 -> createRescueCase();
                case 2 -> searchRescueCase();
                case 3 -> updateRescueStatus();
                case 4 -> displayAllRescueCases();
                case 5 -> showRescueReport();
                case 6 -> {
                    System.out.println("Thank you for using the Wildlife Rescue Operations System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("=".repeat(45));
        System.out.println("WILDLIFE RESCUE OPERATIONS SYSTEM");
        System.out.println("=".repeat(45));
        System.out.println("1. Create Rescue Case");
        System.out.println("2. Search Rescue Case");
        System.out.println("3. Update Rescue Status");
        System.out.println("4. Display All Rescue Cases");
        System.out.println("5. Rescue Report");
        System.out.println("6. Exit");
    }

   
    // 1. Create Rescue Case
    
    private void createRescueCase() {
        System.out.println();
        System.out.println("Select Rescue Case Type:");
        System.out.println("1. Injured Animal Rescue");
        System.out.println("2. Orphaned Animal Rescue");
        System.out.println("3. Endangered Species Rescue");
        int typeChoice = InputValidator.readMenuOption(scanner, "Select an option: ", 1, 3);

        String rescueCaseId;
        while (true) {
            rescueCaseId = InputValidator.readNonBlankString(scanner, "Rescue Case ID: ");
            if (manager.isDuplicateId(rescueCaseId)) {
                System.out.println("A rescue case with that ID already exists. Please use a different ID.");
                continue;
            }
            break;
        }

        String animalName = InputValidator.readNonBlankString(scanner, "Animal Name: ");
        String species = InputValidator.readNonBlankString(scanner, "Species: ");
        String rescueLocation = InputValidator.readNonBlankString(scanner, "Rescue Location: ");
        String assignedRanger = InputValidator.readNonBlankString(scanner, "Assigned Ranger: ");
        int numberOfRescueDays = InputValidator.readPositiveInt(scanner, "Number of Rescue Days: ");
        double dailyCareCost = InputValidator.readPositiveDouble(scanner, "Daily Care Cost (R): ");

        try {
            RescueCase rescueCase = switch (typeChoice) {
                case 1 -> createInjuredAnimalRescue(rescueCaseId, animalName, species, rescueLocation,
                        assignedRanger, numberOfRescueDays, dailyCareCost);
                case 2 -> createOrphanedAnimalRescue(rescueCaseId, animalName, species, rescueLocation,
                        assignedRanger, numberOfRescueDays, dailyCareCost);
                default -> createEndangeredSpeciesRescue(rescueCaseId, animalName, species, rescueLocation,
                        assignedRanger, numberOfRescueDays, dailyCareCost);
            };
            manager.addRescueCase(rescueCase);
            System.out.println();
            System.out.println("Rescue case created successfully:");
            System.out.println(rescueCase.generateRescueSummary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not create rescue case: " + e.getMessage());
        }
    }

    private RescueCase createInjuredAnimalRescue(String id, String animalName, String species, String location,
                                                  String ranger, int days, double dailyCost) {
        String injuryDescription = InputValidator.readNonBlankString(scanner, "Injury Description: ");
        double vetCost = InputValidator.readNonNegativeDouble(scanner, "Veterinary Treatment Cost (R): ");
        boolean surgeryRequired = InputValidator.readYesNo(scanner, "Surgery Required? (y/n): ");
        return new InjuredAnimalRescue(id, animalName, species, location, ranger, days, dailyCost,
                injuryDescription, vetCost, surgeryRequired);
    }

    private RescueCase createOrphanedAnimalRescue(String id, String animalName, String species, String location,
                                                   String ranger, int days, double dailyCost) {
        int ageMonths = InputValidator.readPositiveInt(scanner, "Estimated Age (Months): ");
        double feedingCost = InputValidator.readNonNegativeDouble(scanner, "Feeding Cost (R): ");
        boolean fosterCareRequired = InputValidator.readYesNo(scanner, "Foster Care Required? (y/n): ");
        return new OrphanedAnimalRescue(id, animalName, species, location, ranger, days, dailyCost,
                ageMonths, feedingCost, fosterCareRequired);
    }

    private RescueCase createEndangeredSpeciesRescue(String id, String animalName, String species, String location,
                                                      String ranger, int days, double dailyCost) {
        String classification = InputValidator.readNonBlankString(scanner, "Conservation Classification: ");
        double securityCost = InputValidator.readNonNegativeDouble(scanner, "Security Cost (R): ");
        boolean specialistTeamRequired = InputValidator.readYesNo(scanner, "Specialist Team Required? (y/n): ");
        return new EndangeredSpeciesRescue(id, animalName, species, location, ranger, days, dailyCost,
                classification, securityCost, specialistTeamRequired);
    }

    
    // 2. Search Rescue Case
    
    private void searchRescueCase() {
        String id = InputValidator.readNonBlankString(scanner, "Enter Rescue Case ID to search: ");
        Optional<RescueCase> found = manager.findByCaseId(id);
        if (found.isPresent()) {
            RescueCase rc = found.get();
            System.out.println();
            System.out.println(rc.generateRescueSummary());
            System.out.println(rc.getSpecificInfo());
        } else {
            System.out.println("No rescue case found with ID '" + id + "'.");
        }
    }

    
    // 3. Update Rescue Status
    
    private void updateRescueStatus() {
        String id = InputValidator.readNonBlankString(scanner, "Enter Rescue Case ID to update: ");
        Optional<RescueCase> found = manager.findByCaseId(id);
        if (found.isEmpty()) {
            System.out.println("No rescue case found with ID '" + id + "'.");
            return;
        }
        RescueCase rc = found.get();
        System.out.println("Current Status: " + rc.getCurrentStatus());
        System.out.println("1. Start Rescue Operation");
        System.out.println("2. Complete Rescue Operation");
        int choice = InputValidator.readMenuOption(scanner, "Select an option: ", 1, 2);
        if (choice == 1) {
            rc.startRescueOperation();
        } else {
            rc.completeRescueOperation();
        }
        System.out.println("Status updated. New Status: " + rc.getCurrentStatus());
    }

    
    // 4. Display All Rescue Cases
    
    private void displayAllRescueCases() {
        List<RescueCase> all = manager.getAllRescueCases();
        if (all.isEmpty()) {
            System.out.println("No rescue cases recorded yet.");
            return;
        }
        System.out.println();
        for (RescueCase rc : all) {
            System.out.println(rc);
        }
    }

    
    // 5. Rescue Report
    
    private void showRescueReport() {
        System.out.println();
        System.out.println(manager.generateReport());
    }
}
