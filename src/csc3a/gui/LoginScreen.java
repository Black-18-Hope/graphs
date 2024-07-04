package csc3a.gui;

import java.util.Date;

import csc3a.File.FileHandler;
import csc3a.File.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScreen extends Application {

	private TextField usernameField;
	private PasswordField passwordField;
	private TextField userTypeField; // Added missing field
	private Label errorMessageLabel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Login Screen");

		// Create the UI elements
		Label titleLabel = new Label("Login");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		Label usernameLabel = new Label("Username:");
		Label passwordLabel = new Label("Password:");
		Label userTypeLabel = new Label("User type:"); // Added missing label
		usernameField = new TextField();
		passwordField = new PasswordField();
		userTypeField = new TextField();
		userTypeField.setPromptText("Admin or Client");

		Button loginButton = new Button("Login");
		// Use a lambda expression to define the login button's action
		loginButton.setOnAction(event -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			String userType = userTypeField.getText(); // Get the user type from the input field

			// Authenticate the user
			if (FileHandler.AuthenticateUser(username, password, userType)) {
				// If authentication succeeds, proceed to the next screen
				User user = new User(username, password, userType);
				System.out.println("Login successful!");
				AdminHomeScreen homeScreen = new AdminHomeScreen(user);
				UserHomeScreen userHome = new UserHomeScreen(user);

				if (isAdmin(user)) {
					try {

						homeScreen.start(primaryStage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						userHome.start(primaryStage);
						FileHandler.logUser(username, new Date().toString(), (int)Math.random()*500);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			} else {
				// If authentication fails, display an error message
				errorMessageLabel.setText("Incorrect username, password, or user type.");
			}
		});

		// Create the "Registration" button
		Button registrationButton = new Button("Registration");
		registrationButton.setOnAction(event -> {
			// Create a new instance of the RegistrationScreen class and show it
			RegistrationScreen registrationScreen = new RegistrationScreen();
			try {
				registrationScreen.start(new Stage());
				primaryStage.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		errorMessageLabel = new Label();
		errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

		// Create the layout
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.add(titleLabel, 0, 0, 2, 1);
		grid.add(usernameLabel, 0, 1);
		grid.add(usernameField, 1, 1);
		grid.add(passwordLabel, 0, 2);
		grid.add(passwordField, 1, 2);
		grid.add(userTypeLabel, 0, 3); // Add the user type label and input field to the grid
		grid.add(userTypeField, 1, 3);
		grid.add(loginButton, 1, 4); // Increase the row index of the login button
		grid.add(registrationButton, 2, 4); // Add the registration button to the grid at column 2, row 4
		grid.add(errorMessageLabel, 0, 5, 2, 1);

		Scene scene = new Scene(grid, 400, 300);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static boolean isAdmin(User user) {
		return user.getRole().equals("admin");
	}

}
