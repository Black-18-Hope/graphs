package csc3a.gui;

import csc3a.model.Graph;
import csc3a.model.Graph.TYPE;
import csc3a.model.MobileClinic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ClinicDetailsScreen {
	private BorderPane layout;
	private Graph<MobileClinic> graph;
	

	public ClinicDetailsScreen(Graph<MobileClinic> graph2) {
	    layout = new BorderPane();
	    
	    // Create header
	    Label headerLabel = new Label("Clinic Details");
	    headerLabel.setFont(new Font("Arial", 20));
	    layout.setTop(headerLabel);
	    BorderPane.setMargin(headerLabel, new Insets(10));
	    
	    // Create clinic details
	    Label nameLabel = new Label("Clinic Name: Medi Clinic");
	    Label locationLabel = new Label("Location: [33.9632, -118.4165]");
	    Label phoneLabel = new Label("Phone: 0800055555");
	    Label hoursLabel = new Label("Hours: Monday-Friday, 9am-5pm");
	    VBox detailsBox = new VBox(nameLabel, locationLabel, phoneLabel, hoursLabel);
	    detailsBox.setAlignment(Pos.CENTER);
	    detailsBox.setSpacing(10);
	    layout.setCenter(detailsBox);
	    BorderPane.setMargin(detailsBox, new Insets(10));
	    
	    // Create book appointment button
	    Button bookButton = new Button("Book Appointment");
	    layout.setBottom(bookButton);
	    BorderPane.setAlignment(bookButton, Pos.CENTER);
	    BorderPane.setMargin(bookButton, new Insets(10));
	    
	 // Create contact us button
	    Button contactButton = new Button("Contact Us");
	    contactButton.setOnAction(e -> {
	        // Code to handle contact us button click
	    });
	    layout.setBottom(contactButton);
	    BorderPane.setAlignment(contactButton, Pos.CENTER_RIGHT);
	    BorderPane.setMargin(contactButton, new Insets(10));

	}

	public BorderPane getLayout() {
	    return layout;
	}
	
	// Method to book an appointment at a MobileClinic
		public void bookAppointment(String name) {
			boolean booked = false;
			// Loop through the vertices in the graph to find the MobileClinic by name
			for (Graph.Vertex<MobileClinic> vertex : graph.getVertices()) {
				if (name.equals(vertex.getValue().getName())) {
					// Check if there are available slots in the MobileClinic
					if (vertex.getValue().getAvailableSlots() > 0) {
						// Decrease the available slots by 1
						vertex.getValue().setAvailableSlots(vertex.getValue().getAvailableSlots() - 1);
						booked = true;
						break;
					} else {
						// Show error alert if no available slots
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Notification");
						alert.setContentText("No available slots in the Mobile Clinic");
						alert.showAndWait();
						booked = false;
						break;
					}
				}
			}

			if (booked) {
				// Show success alert
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setContentText("Appointment booked successfully");
				alert.showAndWait();
			} else {
				// Show error alert if MobileClinic not found
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Notification");
				alert.setContentText("Mobile Clinic not found");
				alert.showAndWait();
			}

		}

	}

