package csc3a.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import csc3a.File.User;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AdminHomeScreen extends Application {

	private Stage stage;
	private User user;

	public AdminHomeScreen(User user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 800, 600);

		// add a background image
		Image image = null;
		try {
			image = new Image(new FileInputStream(new File("resources/data/pictures/background.jpg")));
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		}
		ImageView background = new ImageView(image);
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		background.fitWidthProperty()
				.bind(Bindings.createDoubleBinding(() -> screenBounds.getWidth(), background.sceneProperty()));
		background.fitHeightProperty()
				.bind(Bindings.createDoubleBinding(() -> screenBounds.getHeight(), background.sceneProperty()));
		root.getChildren().add(background);

		Font customFont = Font.font("Helvetica", FontWeight.BOLD, 18);

		// create the top bar
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10, 20, 10, 20));
		topBar.setSpacing(10);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #333333;");

		Label titleLabel = new Label("Mobile Clinic Deployment Optimizer");
		titleLabel.setFont(customFont);
		titleLabel.setTextFill(Color.WHITE);

		Button logoutButton = new Button("Logout");
		logoutButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		logoutButton.setOnAction(event -> {
			// handle logout button click
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Are you sure you want to logout?");
			alert.showAndWait();

			if (alert.getResult().getText().equals("OK")) {
				// go to login screen
				LoginScreen lg = new LoginScreen();
				System.out.println("Logged out!");
				lg.start(stage);
			} else {
				// do nothing
			}
		});

		topBar.getChildren().addAll(titleLabel, logoutButton);
		root.setTop(topBar);

		// create the main content
		VBox content = new VBox();
		content.setPadding(new Insets(20));
		content.setSpacing(20);
		content.setAlignment(Pos.CENTER);
		Label welcomeLabel = new Label("Welcome, <" + user.userName() + ">!");
		welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0;");
		/* welcomeLabel.setFont(customFont); */
		welcomeLabel.setTextFill(Color.WHITE);

		Button viewMapButton = new Button("View Map");
		viewMapButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		viewMapButton.setOnMouseEntered(event -> {
			viewMapButton.setStyle("-fx-background-color: #0077be; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		viewMapButton.setOnMouseExited(event -> {
			viewMapButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		viewMapButton.setOnAction(e -> {
			// create the intro stage
			Stage introStage = new Stage();
			introStage.initStyle(StageStyle.UNDECORATED); // remove window decorations
			introStage.centerOnScreen(); // center the stage on the screen

			// create the logo
			ImageView logo = null;

			try {
				logo = new ImageView(new Image(new FileInputStream(new File("resources/data/icons/pin.png"))));
				logo.setFitWidth(200);
				logo.setFitHeight(200);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// create the title
			Label tLabel = new Label("Locate Mobile clinics!");
			tLabel.setFont(new Font("Arial", 30));

			// create the intro layout
			VBox introLayout = new VBox(logo, tLabel);
			introLayout.setAlignment(Pos.CENTER);
			introLayout.setSpacing(50);

			// create the intro scene
			Scene introScene = new Scene(introLayout, 400, 300);
			introScene.setFill(Color.TRANSPARENT); // make the background transparent
			// create the fade in animation
			FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), introLayout);
			fadeIn.setFromValue(0);
			fadeIn.setToValue(1);

			// create the fade out animation
			FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), introLayout);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);

			// show the intro stage
			introStage.setScene(introScene);
			introStage.show();

			// play the animations and wait for them to finish
			SequentialTransition introAnimation = new SequentialTransition(fadeIn,
					new PauseTransition(Duration.seconds(3)), fadeOut);
			introAnimation.setOnFinished(event -> {
				// create and show the map stage
				WebView webView = new WebView();
				WebEngine webEngine = webView.getEngine();
				webEngine.load("https://www.google.com/maps");
				BorderPane root1 = new BorderPane(webView);
				Scene scene1 = new Scene(root1, 800, 600);
				Stage mapStage = new Stage();
				mapStage.setScene(scene1);
				mapStage.setTitle("Mobile Clinics");
				mapStage.show();
			});
			introAnimation.play();

		});
		Button viewReportsButton = new Button("View Reports");
		viewReportsButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		viewReportsButton.setOnMouseEntered(event -> {
			viewReportsButton.setStyle("-fx-background-color: #0077be; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		viewReportsButton.setOnMouseExited(event -> {
			viewReportsButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		});

		viewReportsButton.setOnAction(event -> {
			Button backButton = new Button("Home");
			backButton.setOnAction(e -> {
				// Replace "HomeScreen" with the name of your home screen class
				AdminHomeScreen homeScreen = new AdminHomeScreen(user);
				homeScreen.start(stage);
			});

			HBox topBar2 = new HBox();
			topBar2.setAlignment(Pos.TOP_LEFT);
			topBar2.getChildren().add(backButton);

			// Create a table view to display the list of system users
			TableView<String[]> userTableView = new TableView<>();

			// Add columns to the table view
			TableColumn<String[], String> usernameColumn = new TableColumn<>("Username");
			usernameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
			usernameColumn.setPrefWidth(200);

			TableColumn<String[], String> dateColumn = new TableColumn<>("Last Login Date");
			dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
			dateColumn.setPrefWidth(200);

			TableColumn<String[], String> gpsColumn = new TableColumn<>("Last Login GPS Coordinates");
			gpsColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
			gpsColumn.setPrefWidth(200);

			userTableView.getColumns().addAll(usernameColumn, dateColumn, gpsColumn);

			// Load the list of system users from the log file into an observable list
			ObservableList<String[]> userList = FXCollections.observableArrayList();
			try (BufferedReader reader = new BufferedReader(new FileReader("./resources/data/log.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] fields = line.split("\t");
					String username = fields[0];
					String date = fields[1];
					String gps = fields[2];
					userList.add(new String[] { username, date, gps });
				}
			} catch (IOException e) {
				System.err.println("Error reading log file: " + e.getMessage());
			}

			// Set the items of the table view to the list of system users
			userTableView.setItems(userList);

			// Create a label with a big title at the top
			Label titleLabe = new Label("List of the system users");
			titleLabe.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold;");

			// Create a VBox to hold the label and the table view
			VBox roo = new VBox();
			roo.setSpacing(10);
			roo.setAlignment(Pos.CENTER);
			roo.getChildren().addAll(topBar2, titleLabe, userTableView);

			// Create a scene with the VBox as the root node
			Scene scen = new Scene(roo, 800, 600);

			// Set the scene to be displayed
			stage.setScene(scen);
			stage.setTitle("Reports - System Users");

		});

		Button manageClinicsButton = new Button("Manage Clinics");
		manageClinicsButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		manageClinicsButton.setOnMouseEntered(event -> {
			manageClinicsButton.setStyle("-fx-background-color: #0077be; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		manageClinicsButton.setOnMouseExited(event -> {
			manageClinicsButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		manageClinicsButton.setOnAction(new ManageClinicsHandler(user));

		Button optimizeButton = new Button("Optimize Deployment");
		optimizeButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		optimizeButton.setOnMouseEntered(event -> {
			optimizeButton.setStyle("-fx-background-color: #0077be; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		optimizeButton.setOnMouseExited(event -> {
			optimizeButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold;");
		});
		/* optimizeButton.setOnAction(); */

		content.getChildren().addAll(welcomeLabel, viewMapButton, viewReportsButton, manageClinicsButton,
				optimizeButton);
		root.setCenter(content);

		// animate the top bar and main content
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.play();

		TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), content);
		slideIn.setFromY(100);
		slideIn.setToY(0);
		slideIn.play();

		// show the stage
		stage.setScene(scene);
		stage.setTitle("Mobile Clinic Deployment Optimizer - Home");
		stage.show();
	}
}