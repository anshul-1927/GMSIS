/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Mario_
 */
public class Parts {

    private IntegerProperty partID;
    private StringProperty partName;
    private StringProperty description;
    private IntegerProperty quantity;
    private DoubleProperty cost;

    public Parts(int partID, String partName, String description, int quantity, double cost) {
        this.partID = new SimpleIntegerProperty(partID);
        this.partName = new SimpleStringProperty(partName);
        this.description = new SimpleStringProperty(description);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.cost = new SimpleDoubleProperty(cost);
    }

    public Parts(int partID, String partName, String description, double cost) {
        this.partID = new SimpleIntegerProperty(partID);
        this.partName = new SimpleStringProperty(partName);
        this.description = new SimpleStringProperty(description);
        this.cost = new SimpleDoubleProperty(cost);
    }

    public int getPartID() {
        return partID.get();
    }

    public void setPartID(int value) {
        partID.set(value);
    }

    public String getPartName() {
        return partName.get();
    }

    public void setPartName(String value) {
        partName.set(value);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int value) {
        quantity.set(value);
    }

    public double getCost() {
        return cost.get();
    }


    public IntegerProperty idProperty() {
        return partID;
    }

    public StringProperty nameProperty() {
        return partName;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public IntegerProperty qunatityProperty() {
        return quantity;
    }

    public DoubleProperty costProperty() {
        return cost;
    }


}
