package csc3a.gui;
import csc3a.File.FileHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegistrationScreen extends Application {
	private Stage stage;
	private Scene scene;

	public RegistrationScreen() {
		// create the UI components
		Label title = new Label("Mobile Clinic Registration");
		title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0;");
		Label userName = new Label("UserName:");
		TextField userNameField = new TextField();
		Label passwordLabel = new Label("Password:");
		TextField passwordField = new TextField();
		Label confirmPasswordLabel = new Label("Confirm Password:");
		TextField confirmPasswordField = new TextField();
		Label userType = new Label("User type");
		TextField userTypeField = new TextField();
		userTypeField.setPromptText("Admin or Client");
		;
		Button registerButton = new Button("Register");
		Button loginButton = new Button("Login");

		// create the layout
		GridPane layout = new GridPane();
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPadding(new Insets(25, 25, 25, 25));
		layout.add(title, 0, 0, 2, 1);
		layout.add(userName, 0, 1);
		layout.add(userNameField, 1, 1);
		layout.add(passwordLabel, 0, 2);
		layout.add(passwordField, 1, 2);
		layout.add(confirmPasswordLabel, 0, 3);
		layout.add(confirmPasswordField, 1, 3);
		layout.add(userType, 0, 4);
		layout.add(userTypeField, 1, 4);
		layout.add(registerButton, 0, 5);
		layout.add(loginButton, 1, 5);

		// create the scene
		scene = new Scene(layout, 400, 300);

		// set the event handlers
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// go back to the login screen
				LoginScreen loginScreen = new LoginScreen();
				try {
					loginScreen.start(stage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// validate the input
				String userName = userNameField.getText().trim();
				String password = passwordField.getText();
				String confirmPassword = confirmPasswordField.getText();
				String userType = userTypeField.getText();
				if (userName.isEmpty() || userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Registration Error");
					alert.setHeaderText(null);
					alert.setContentText("All fields are required.");
					alert.showAndWait();
					return;
				}
				
				if (!password.equals(confirmPassword)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Registration Error");
					alert.setHeaderText(null);
					alert.setContentText("Passwords do not match.");
					alert.showAndWait();
					password=null;
					return;
				}
				String type = userTypeField.getText().trim();
				if (!type.matches("^(user|admin)$")) {
				    Alert alert = new Alert(AlertType.ERROR);
				    alert.setTitle("Registration Error");
				    alert.setHeaderText(null);
				    alert.setContentText("User type must be 'user' or 'admin'.");
				    alert.showAndWait();
				    return;
				}
				

				if (FileHandler.createUserCredentials(userName, password, userType)) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Registration Success");
					alert.setHeaderText(null);
					alert.setContentText("Registration successful. Please login.");
					alert.showAndWait();

					// go back to the login screen
					LoginScreen loginScreen = new LoginScreen();
					try {
						loginScreen.start(stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Registration Error");
					alert.setHeaderText(null);
					alert.setContentText("Registration failed. Please try again.");
					alert.showAndWait();
				}
			}
		});
	}

	public void start(Stage stage) {
		this.stage = stage;
		stage.setScene(scene);
		stage.setTitle("Mobile Clinic Registration");
		stage.show();
	}

}
