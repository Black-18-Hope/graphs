package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import csc3a.gui.RegistrationScreen;


public class Main extends Application {
	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of RegistrationScreen
        RegistrationScreen registrationScreen = new RegistrationScreen();
        // Start the RegistrationScreen
        registrationScreen.start(primaryStage);
    }
}
