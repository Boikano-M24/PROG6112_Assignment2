package wildliferescue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying the core functionality of the Wildlife Rescue
 * Operations System: rescue cost calculations, rescue priority
 * calculations, rescue status updates, searching for an existing rescue
 * case, and preventing duplicate Rescue Case IDs.
 */
class RescueCaseTest {

    private RescueManager manager;

    @BeforeEach
    void setUp() {
        manager = new RescueManager();
    }

    
    // Rescue cost calculations
    

    @Test
    void injuredAnimalRescue_costIncludesSurgerySurchargeWhenRequired() {
        InjuredAnimalRescue rescue = new InjuredAnimalRescue(
                "WR101", "Tembo", "African Elephant", "Kruger National Park", "Ranger Dlamini",
                5, 500.0, "Snare wound to left leg", 4000.0, true);

        // base = 5 * 500 = 2500; + vet 4000; + surgery surcharge 5000 = 11500
        assertEquals(11500.0, rescue.calculateTotalRescueCost(), 0.001);
    }

    @Test
    void injuredAnimalRescue_costExcludesSurchargeWhenSurgeryNotRequired() {
        InjuredAnimalRescue rescue = new InjuredAnimalRescue(
                "WR102", "Simba", "Lion", "Sabi Sands", "Ranger Nkosi",
                3, 300.0, "Minor laceration", 1000.0, false);

        // base = 3 * 300 = 900; + vet 1000 = 1900
        assertEquals(1900.0, rescue.calculateTotalRescueCost(), 0.001);
    }

    @Test
    void orphanedAnimalRescue_costIncludesFosterCareSurchargeWhenRequired() {
        OrphanedAnimalRescue rescue = new OrphanedAnimalRescue(
                "WR201", "Baby Rhino", "White Rhino", "Hluhluwe Reserve", "Ranger Botha",
                10, 200.0, 4, 1500.0, true);

        // base = 10 * 200 = 2000; + feeding 1500; + foster 2500 = 6000
        assertEquals(6000.0, rescue.calculateTotalRescueCost(), 0.001);
    }

    @Test
    void endangeredSpeciesRescue_costIncludesSpecialistTeamSurchargeWhenRequired() {
        EndangeredSpeciesRescue rescue = new EndangeredSpeciesRescue(
                "WR301", "Nkosi", "Black Rhino", "Addo Elephant Park", "Ranger Van Wyk",
                7, 400.0, "Critically Endangered", 3000.0, true);

        // base = 7 * 400 = 2800; + security 3000; + specialist 8000 = 13800
        assertEquals(13800.0, rescue.calculateTotalRescueCost(), 0.001);
    }

    
    // Rescue priority calculations
   

    @Test
    void injuredAnimalRescue_priorityIsCriticalWhenSurgeryRequired() {
        InjuredAnimalRescue rescue = new InjuredAnimalRescue(
                "WR103", "Tau", "Lion", "Sabi Sands", "Ranger Nkosi",
                2, 250.0, "Fractured leg", 2000.0, true);
        assertEquals("Critical", rescue.determineRescuePriority());
    }

    @Test
    void injuredAnimalRescue_priorityIsHighWhenTreatmentCostExceedsThreshold() {
        InjuredAnimalRescue rescue = new InjuredAnimalRescue(
                "WR104", "Tau", "Lion", "Sabi Sands", "Ranger Nkosi",
                2, 250.0, "Deep wound", 6000.0, false);
        assertEquals("High", rescue.determineRescuePriority());
    }

    @Test
    void orphanedAnimalRescue_priorityIsCriticalForVeryYoungAnimal() {
        OrphanedAnimalRescue rescue = new OrphanedAnimalRescue(
                "WR202", "Cub", "Cheetah", "Kgalagadi", "Ranger Smith",
                14, 150.0, 3, 800.0, false);
        assertEquals("Critical", rescue.determineRescuePriority());
    }

    @Test
    void endangeredSpeciesRescue_priorityIsHighForEndangeredClassification() {
        EndangeredSpeciesRescue rescue = new EndangeredSpeciesRescue(
                "WR302", "Kudu", "Roan Antelope", "Marakele Park", "Ranger Zulu",
                4, 350.0, "Endangered", 1000.0, false);
        assertEquals("High", rescue.determineRescuePriority());
    }

    
    // Rescue status updates


    @Test
    void startRescueOperation_updatesStatusFromReportedToInProgress() {
        InjuredAnimalRescue rescue = new InjuredAnimalRescue(
                "WR105", "Duma", "Cheetah", "Kgalagadi", "Ranger Smith",
                1, 100.0, "Minor injury", 500.0, false);

        assertEquals(RescueCase.STATUS_REPORTED, rescue.getCurrentStatus());
        rescue.startRescueOperation();
        assertEquals(RescueCase.STATUS_IN_PROGRESS, rescue.getCurrentStatus());
    }

    @Test
    void completeRescueOperation_setsSpecificStatusPerRescueType() {
        InjuredAnimalRescue withSurgery = new InjuredAnimalRescue(
                "WR106", "Duma", "Cheetah", "Kgalagadi", "Ranger Smith",
                1, 100.0, "Broken leg", 2000.0, true);
        withSurgery.startRescueOperation();
        withSurgery.completeRescueOperation();
        assertEquals("Rescue Completed - Post-Surgery Recovery", withSurgery.getCurrentStatus());

        OrphanedAnimalRescue withFoster = new OrphanedAnimalRescue(
                "WR203", "Cub", "Leopard", "Kruger", "Ranger Dlamini",
                5, 120.0, 2, 600.0, true);
        withFoster.completeRescueOperation();
        assertEquals("Rescue Completed - Placed in Foster Care", withFoster.getCurrentStatus());
    }

    
    // Searching for an existing rescue case
    

    @Test
    void findByCaseId_returnsMatchingCaseIgnoringCase() {
        RescueCase rescue = new EndangeredSpeciesRescue(
                "WR400", "Nkosi", "Black Rhino", "Addo", "Ranger Van Wyk",
                6, 300.0, "Vulnerable", 500.0, false);
        manager.addRescueCase(rescue);

        Optional<RescueCase> found = manager.findByCaseId("wr400");
        assertTrue(found.isPresent());
        assertEquals("WR400", found.get().getRescueCaseId());
    }

    @Test
    void findByCaseId_returnsEmptyWhenCaseDoesNotExist() {
        Optional<RescueCase> found = manager.findByCaseId("DOES-NOT-EXIST");
        assertTrue(found.isEmpty());
    }

    
    // Preventing duplicate Rescue Case IDs
   

    @Test
    void addRescueCase_throwsExceptionForDuplicateId() {
        RescueCase first = new InjuredAnimalRescue(
                "WR500", "Tembo", "Elephant", "Kruger", "Ranger Dlamini",
                3, 200.0, "Snare wound", 1000.0, false);
        RescueCase duplicate = new InjuredAnimalRescue(
                "WR500", "Different Name", "Elephant", "Kruger", "Ranger Dlamini",
                3, 200.0, "Different injury", 1200.0, false);

        manager.addRescueCase(first);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> manager.addRescueCase(duplicate));
        assertTrue(exception.getMessage().contains("WR500"));

        List<RescueCase> all = manager.getAllRescueCases();
        assertEquals(1, all.size());
    }

    @Test
    void isDuplicateId_detectsExistingIdRegardlessOfCase() {
        RescueCase rescue = new OrphanedAnimalRescue(
                "WR600", "Baby Elephant", "African Elephant", "Kruger", "Ranger Botha",
                8, 250.0, 5, 900.0, false);
        manager.addRescueCase(rescue);

        assertTrue(manager.isDuplicateId("wr600"));
        assertFalse(manager.isDuplicateId("WR601"));
    }
}
