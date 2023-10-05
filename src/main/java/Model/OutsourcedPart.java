package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Represents an Outsourced Part, extending the general Part class.
 */
public class OutsourcedPart extends Part {
    /**
     * The name of the company that provides this outsourced part.
     */
    private final StringProperty companyName;
    /**
     * Constructor for OutsourcedPart.
     */
    public OutsourcedPart() {
        super();
        companyName = new SimpleStringProperty();
    }
    /**
     * Gets the company name of the outsourced part.
     *
     * @return Company name as a string.
     */
    public String getCompanyName() {
        return this.companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
}