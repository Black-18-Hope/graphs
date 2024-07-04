package csc3a.gui;

import csc3a.File.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class ManageClinicsHandler implements EventHandler<ActionEvent>{
	
	private User user;

	public  ManageClinicsHandler(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	@Override
    public void handle(ActionEvent event) {
		MobileClinicAdminInterface adminView = new MobileClinicAdminInterface(user);
        try {
			adminView.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
