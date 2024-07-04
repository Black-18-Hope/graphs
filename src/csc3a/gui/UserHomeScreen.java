package csc3a.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import csc3a.File.User;
import csc3a.model.Dijkstra;
import csc3a.model.GPSPoint;
import csc3a.model.Graph;
import csc3a.model.Graph.CostPathPair;
import csc3a.model.Graph.TYPE;
import csc3a.model.Graph.Vertex;
import csc3a.model.MobileClinic;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserHomeScreen extends Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField locationLatField;
	private TextField locationLongField;
	private Label statusLabel;
	private ProgressIndicator progressIndicator;
	private Stage stage;

	private User user;
	File file = new File("resources/data/clininis/mobile_clinics.bin");
	private static Graph<MobileClinic> graph = new Graph<>(TYPE.UNDIRECTED);

	public UserHomeScreen(User user) {
		this.user = user;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 800, 600);

		// add a background image
		Image image = null;
		try {
			// Reference: https://energycouncil.com/wp-content/uploads/Eskom-1.png
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

		Label titleLabel = new Label("Request Mobile Clinic");
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

		VBox content = new VBox();
		content.setPadding(new Insets(20));
		content.setSpacing(20);
		content.setAlignment(Pos.CENTER);
		Label welcomeLabel = new Label("Welcome, <" + user.userName() + ">!");
		welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0;");
		/* welcomeLabel.setFont(customFont); */
		welcomeLabel.setTextFill(Color.WHITE);

		// Create input field
		Label locationLabel = new Label("Enter your location:");
		locationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		locationLabel.setTextFill(Color.WHITE);
		locationLatField = new TextField();
		locationLatField.setPromptText("Latitude");
		locationLongField = new TextField();
		locationLongField.setPromptText("Longitude");

		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10));
		vBox.getChildren().addAll(locationLatField, locationLongField);

		// Create button
		Button requestButton = new Button("Search Clinic");
		HBox locationBox = new HBox(locationLabel, vBox, requestButton);
		locationBox.setAlignment(Pos.CENTER);
		locationBox.setSpacing(10);
		locationBox.setPadding(new Insets(15));
		content.getChildren().addAll(welcomeLabel, locationBox);
		root.setCenter(content);
		requestButton.setOnAction(e -> {

			String latText = locationLatField.getText().trim();
			String lonText = locationLongField.getText().trim();
			if (latText.isEmpty() || lonText.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "Please fill location!");
				alert.showAndWait();
				return;
			} else {
				double lat, lon;

				try {
					lat = Double.parseDouble(latText);
					lon = Double.parseDouble(lonText);
				} catch (NumberFormatException e1) {
					Alert alert = new Alert(AlertType.ERROR, "Invalid location format!");
					alert.showAndWait();
					return;
				}
				statusLabel.setText("Searching for mobile clinic near " + user.getUserName() + "'s location...");
				statusLabel.setVisible(true);
				progressIndicator.setVisible(true);

				Task<Graph<MobileClinic>> task = new Task<Graph<MobileClinic>>() {
					@Override
					protected Graph<MobileClinic> call() throws Exception {
						// Create a new Vertex object for the user's location
						GPSPoint userlocation = new GPSPoint(lat, lon);
						MobileClinic userDet = new MobileClinic(userlocation);
						Graph.Vertex<MobileClinic> userVertex = new Graph.Vertex<MobileClinic>(userDet);

						// Use Dijkstra's algorithm to find the shortest path from the user's location
						// to all clinics in the graph, maintaining the order of distances with a
						// PriorityQueue
						PriorityQueue<Map.Entry<Graph.Vertex<MobileClinic>, CostPathPair<MobileClinic>>> queue = new PriorityQueue<>(
								Comparator.comparing(entry -> entry.getValue().getCost()));
						Map<Vertex<MobileClinic>, CostPathPair<MobileClinic>> distances = new HashMap<>();
						for (Graph.Vertex<MobileClinic> vertex : graph.getVertices()) {
							try {
								CostPathPair<MobileClinic> costPathPair = Dijkstra.getShortestPath(graph, userVertex,
										vertex);
								distances.put(vertex, costPathPair);
								queue.add(new AbstractMap.SimpleEntry<>(vertex, costPathPair));
							} catch (IllegalArgumentException e) {
								System.out.println(e.getMessage());
							}
						}

						// Return the clinic that is closest to the user's location
						Graph.Vertex<MobileClinic> nearestClinicVertex = queue.poll().getKey();
						MobileClinic nearestClinic = nearestClinicVertex.getValue();
						Graph.Vertex<MobileClinic> nearestClinicVertexWrapper = new Graph.Vertex<MobileClinic>(
								nearestClinic);
						Graph<MobileClinic> nearestClinicGraph = new Graph<>();
						nearestClinicGraph.getVertices().add(nearestClinicVertexWrapper);
						return nearestClinicGraph;
					}
				};
				 //new Thread(task).start();//

				task.setOnSucceeded(event -> {
					// once the clinic is found, update the status label
					statusLabel.setText("Nearest clinic found!");
					statusLabel.setTextFill(Color.web("#4CAF50"));

					// create a new scene to show the clinic details
					ClinicDetailsScreen clinicDetailsScreen = new ClinicDetailsScreen(task.getValue());
					Scene clinicDetailsScene = new Scene(clinicDetailsScreen.getLayout(), 600, 400);
					primaryStage.setScene(clinicDetailsScene);

					// hide the progress indicator
					progressIndicator.setVisible(false);
				});

				task.setOnFailed(event -> {
					// if the search fails, update the status label and show an alert
					statusLabel.setText("Error: could not find nearest clinic.");
					statusLabel.setTextFill(Color.RED);
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Could not find nearest clinic");
					alert.setContentText("Please try again later.");
					alert.showAndWait();

					// hide the progress indicator
					progressIndicator.setVisible(false);
				});

			}
		});

		// Create status label and progress indicator
		statusLabel = new Label("");
		statusLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px 0;");
		statusLabel.setFont(customFont);
		statusLabel.setTextFill(Color.WHITE);
		progressIndicator = new ProgressIndicator();
		progressIndicator.setVisible(false);

		VBox statusBox = new VBox(statusLabel, progressIndicator);
		statusBox.setAlignment(Pos.CENTER);
		statusBox.setSpacing(10);
		root.setBottom(statusBox);
		BorderPane.setMargin(statusBox, new Insets(10));

		// Set the scene
		primaryStage.setTitle("Mobile Clinic Finder");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
