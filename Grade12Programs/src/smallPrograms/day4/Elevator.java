package smallPrograms.day4;

public class Elevator {
    static final String manufacturer = "Otis";
    static final int topFloor = 19;
    static final int maxOccupants = 25;
    static boolean powerOn = true;

    private int floor = 1;
    private int people = 0;
    private boolean doorsOpen = false;

    Elevator() {
    }

    Elevator(int startFloor) {
        if (startFloor >= 1 && startFloor <= topFloor) {
            floor = startFloor;
        } else {
            floor = 1;
        }
    }

    static void setPowerState(boolean shouldBeOn) {
        if (powerOn != shouldBeOn) {
            powerOn = shouldBeOn;
            System.out.println("power is now " + (powerOn ? "on" : "off"));
        }
    }

    static boolean getPowerState() {
        return powerOn;
    }

    void up() {
        if (floor + 1 > topFloor || !getPowerState() || doorsOpen) {
            System.err.println("There is no power, the doors are open, or the elevator attempted to phase through the ceiling");
        } else {
            floor++;
        }
    }

    void down() {
        if (floor - 1 < 1 || !getPowerState() || doorsOpen) {
            System.err.println("There is no power, the doors are open, or the elevator attempted to sink into the ground");
        } else {
            floor--;
        }
    }

    void goToFloor(int n) {
        if (n < 1 || n > topFloor || !getPowerState() || doorsOpen) {
            System.err.println("There is no power, the doors are open, or the elevator is attempting to teleport outside of the shaft");
        } else {
            floor = n;
        }
    }

    void openDoors() {
        if (getPowerState()) {
            doorsOpen = true;
        } else {
            System.err.println("There is no power");
        }
    }

    void closeDoors() {
        if (getPowerState()) {
            doorsOpen = false;
        } else {
            System.err.println("There is no power");
        }
    }

    void addPeople(int n) {
        if (n > 0 && doorsOpen) {
            people += n;
            people = Math.min(people, maxOccupants);
        } else {
            System.err.println("cannot add people");
        }
    }

    void removePeople(int n) {
        if (n > 0 && doorsOpen) {
            people -= n;
            people = Math.max(people, 0);
        } else {
            System.err.println("cannot remove people");
        }
    }

    @Override
    public String toString() {
        return "manufacturer: " + manufacturer + " | floor: " + floor + " | people: " + people + " | doors: " + (doorsOpen ? "open" : "closed");
    }

    void printInfo() {
        System.out.println(toString());
    }
}
