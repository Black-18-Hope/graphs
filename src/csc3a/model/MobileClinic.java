package csc3a.model;

import java.io.Serializable;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MobileClinic implements Comparable<MobileClinic>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID =8515829695898921355L;
	private String name;
	private int capacity;
    private int availableSlots;
    private GPSPoint location;

	private List<String> services;

	public MobileClinic(String name,GPSPoint location,int capacity,List<String> services) {
		this.location = location;
		this.name = name;
		this.capacity = capacity;
		this.availableSlots = capacity;
	    this.services = services;

		}

	

	public MobileClinic(GPSPoint userlocation) {
		this.location=userlocation;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public void setLocation(GPSPoint location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String id) {
		this.name = id;
	}
	 public GPSPoint getLocation() {
			return location;
		}


	// Method to check if the mobile clinic has available slots
    public boolean hasAvailableSlots() {
        return availableSlots > 0;
    }
    
 // Method to book a slot in the mobile clinic
    public void bookSlot() {
    	if (hasAvailableSlots()) {
            availableSlots--;
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Available Slots");
            alert.setHeaderText(null);
            alert.setContentText("No available slots in " + name + ".");
            alert.showAndWait();
        }
    }
    
 // Method to cancel a slot booking in the mobile clinic
    public void cancelSlot() {
    	if (availableSlots < capacity) {
            availableSlots++;
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("All Slots Available");
            alert.setHeaderText(null);
            alert.setContentText("All slots in " + name + " are currently available.");
            alert.showAndWait();
        }
    }

	@Override
	public int compareTo(MobileClinic o) {
		if (o.getName().equals(this.getName()) && o.getCapacity() == this.getCapacity()) {
			return 0;
		} else {
			return -1;
		}
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public String toString() {
        return "<" + this.name + "> Capacity: <" + this.capacity + "> Available Slots: <" + this.availableSlots + "> Location: <" + this.location + "> Services: <" + this.services + ">";
    }


}
