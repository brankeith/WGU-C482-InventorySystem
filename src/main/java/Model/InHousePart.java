package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents an InHousePart, extending the general Part class.
 * This class includes additional properties specific to in-house parts, such as machine ID.
 */
public class InHousePart extends Part {
    /**
     * Property to store the machine ID associated with the in-house part.
     */
    private final IntegerProperty machineID;
    /**
     * Constructor to initialize an InHousePart.
     */
    public InHousePart() {
        super();
        machineID = new SimpleIntegerProperty();
    }
    /**
     * Retrieves the machine ID associated with this part.
     * @return The machine ID as an integer.
     */
    public int getMachineID() {
        return this.machineID.get();
    }
    /**
     * Sets the machine ID for this part.
     * Corrected Error: Previously, the code allowed negative values for machineID,
     * this has been corrected to throw an IllegalArgumentException when a negative value is attempted to be set.
     * @param machineID The machine ID to set, as an integer.
     * @throws IllegalArgumentException if machineID is negative.
     */
    public void setMachineID(int machineID) {
        if (machineID < 0) {
            throw new IllegalArgumentException("Machine ID cannot be negative");
        }
        this.machineID.set(machineID);
    }
}
