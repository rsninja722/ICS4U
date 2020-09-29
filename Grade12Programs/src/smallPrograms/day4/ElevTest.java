package smallPrograms.day4;

/**
 * James N 
 * 2020.09.28 
 * ElevTest 
 * does various actions with instances of an elevator class to test it
 */

public class ElevTest {
    public static void main(String[] args) {
        Elevator south = new Elevator();
        Elevator east = new Elevator(5);

        // have 10 people get on south at the ground floor
        south.openDoors();
        south.addPeople(10);
        south.printInfo();

        // 3 get out on floor 4 and the rest get out on the top floor
        south.closeDoors();
        south.goToFloor(4);
        south.openDoors();
        south.removePeople(3);
        south.closeDoors();
        south.goToFloor(Elevator.topFloor);
        south.openDoors();
        south.removePeople(7);
        south.printInfo();

        // have 8 get into east on the 5th floor.
        east.goToFloor(5);
        east.openDoors();
        east.addPeople(8);
        east.printInfo();

        // try and add 30 more on the 8th floor
        east.closeDoors();
        east.goToFloor(8);
        east.openDoors();
        east.addPeople(30);
        east.printInfo();

        // try to go above the top floor; try to go to a negative floor
        east.closeDoors();
        east.goToFloor(Elevator.topFloor);
        east.up();
        east.printInfo();

        east.goToFloor(1);
        east.down();
        east.printInfo();

        // move both elevators to the second floor
        south.goToFloor(2);
        east.goToFloor(2);

        // open the doors on east
        east.openDoors();

        // have a power failure
        Elevator.setPowerState(false);

        // then test to make sure that your elevators can't move or close or open doors
        south.up();
        south.openDoors();
        east.closeDoors();

        // restore the power
        Elevator.setPowerState(true);

        // restore the power again
        Elevator.setPowerState(true);

        // make elevator south move up one floor at a time from the ground floor to floor 10, stopping on each floor and opening and closing doors

        south.closeDoors();
        south.goToFloor(1);
        south.printInfo();
        for (int i = 0; i < 9; i++) {
            south.up();
            south.openDoors();
            south.closeDoors();
            south.printInfo();
        }
    }
}
