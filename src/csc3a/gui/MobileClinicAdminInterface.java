package csc3a.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import csc3a.File.FileHandler;
import csc3a.File.User;
import csc3a.model.GPSPoint;
import csc3a.model.Graph;
import csc3a.model.Graph.TYPE;
import csc3a.model.MobileClinic;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MobileClinicAdminInterface extends Application {

	private ObservableList<MobileClinic> mobileClinics = FXCollections.observableArrayList();
	private static Graph<MobileClinic> graph = new Graph<>(TYPE.UNDIRECTED);
	private static List<Graph.Vertex<MobileClinic>> vertices = new ArrayList<Graph.Vertex<MobileClinic>>();
	private static List<Graph.Edge<MobileClinic>> edges = new ArrayList<>();
	private TableView<MobileClinic> tableView;
	private TextField nameField;
	private TextField capacityField;
	private TextField locationLatField;
	private TextField locationLongField;
	private TextField servicesField;
	private MenuBar menubar;
	private Menu menuFile;
	
	private User user;

	public  MobileClinicAdminInterface(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Mobile Clinic Admin Interface");

		// Create a border pane to hold the main content of the UI
		BorderPane root = new BorderPane();

		// create the top bar
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(10, 20, 10, 20));
		topBar.setSpacing(10);
		topBar.setAlignment(Pos.CENTER_LEFT);

		menubar = new MenuBar();
		menuFile = new Menu("File");
		MenuItem mEx = new MenuItem("Export Data");
		MenuItem mImp = new MenuItem("Import Data");
		menuFile.getItems().addAll(mImp, mEx);
		menubar.getMenus().addAll(menuFile);

		Label titleLabel = new Label("Manage Clinics");
		titleLabel.setFont(Font.font("Arial", 24));
		titleLabel.setTextFill(Color.WHITE);
				
		StackPane backPane = new StackPane();
		Image backImage = new Image(new FileInputStream(new File("resources/data/icons/back-arrow.jpeg")));
		ImageView backImageView = new ImageView(backImage);
		backImageView.setFitHeight(20);
		backImageView.setFitWidth(20);
		backPane.getChildren().add(backImageView);
		topBar.getChildren().addAll(backPane, titleLabel, menubar);
		root.setTop(topBar);


		
		
		backPane.setOnMouseClicked(e -> {
		    // Go back to HomeScreen
			AdminHomeScreen homeScreen = new AdminHomeScreen(user);
			try {

				homeScreen.start(primaryStage);
		        primaryStage.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});




		// Create a table view to display the mobile clinics
		tableView = new TableView<>();
		tableView.setEditable(false);

		// Create columns for the table view
		TableColumn<MobileClinic, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(100);

		TableColumn<MobileClinic, String> locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getLocation().toString()));
		locationColumn.setPrefWidth(200);

		TableColumn<MobileClinic, Number> capacityColumn = new TableColumn<>("Capacity");
		capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
		capacityColumn.setPrefWidth(100);

		TableColumn<MobileClinic, Number> availableSlotsColumn = new TableColumn<>("Available Slots");
		availableSlotsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSlots"));
		availableSlotsColumn.setPrefWidth(100);

		TableColumn<MobileClinic, String> servicesColumn = new TableColumn<>("Services");
		servicesColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getServices())));
		servicesColumn.setPrefWidth(300);

		// Add the columns to the table view
		tableView.getColumns().addAll(nameColumn, locationColumn, capacityColumn, availableSlotsColumn, servicesColumn);
		// Set the data for the table view
		tableView.setItems(mobileClinics);

		// Create a grid pane to hold the form for adding a new mobile clinic
		GridPane formPane = new GridPane();
		formPane.setPadding(new Insets(10, 10, 10, 10));
		formPane.setHgap(10);
		formPane.setVgap(10);

		nameField = new TextField();
		nameField.setPromptText("Name");
		capacityField = new TextField();
		capacityField.setPromptText("Capacity");
		locationLatField = new TextField();
		locationLatField.setPromptText("Latitude");
		locationLongField = new TextField();
		locationLongField.setPromptText("Longitude");
		servicesField = new TextField();
		servicesField.setPromptText("Services");

		formPane.add(nameField, 0, 0);
		formPane.add(capacityField, 1, 0);
		formPane.add(locationLatField, 0, 1);
		formPane.add(locationLongField, 1, 1);
		formPane.add(servicesField, 0, 2, 2, 2);

		// MenuItem for saving or exporting data..
		mEx.setOnAction(s -> {

			final FileChooser fsChooser = new FileChooser();
			File file = null;
			fsChooser.setTitle("Select file or create new file to save");

			fsChooser.setInitialDirectory(new File("./resources/data"));

			file = fsChooser.showSaveDialog(null);

			boolean success = FileHandler.saveAll(graph, file);

			Alert alert = new Alert(AlertType.INFORMATION);

			if (success) {

				alert.setTitle("Notification");
				alert.setHeaderText("Success");
				alert.setContentText("Data saved");
				alert.showAndWait();

			} else if (success == false) {

				alert.setTitle("Notification");
				alert.setHeaderText("Error");
				alert.setContentText("Oops something went wrong, data not saved");
				alert.showAndWait();
			}
		});

		// MenuItem for selecting a file to read or import..
		mImp.setOnAction(r -> {

			File file = null;
			final FileChooser fsChooser = new FileChooser();

			fsChooser.setTitle("Select file to read");

			fsChooser.setInitialDirectory(new File("./resources/data/clininis"));
			file = fsChooser.showOpenDialog(null);
			System.out.println(graph.toString());
			graph = FileHandler.readAll(file);
			mobileClinics.addAll(graph.getVertices().stream().map(Graph.Vertex::getValue).collect(Collectors.toList()));

			Alert alert = new Alert(AlertType.INFORMATION);
			if (graph != null) {

				alert.setTitle("Notification");
				alert.setHeaderText("Success");
				alert.setContentText("Data imported");
				alert.showAndWait();

			} else if (graph == null) {
				alert.setTitle("Notification");
				alert.setHeaderText("Error");
				alert.setContentText("Oops something went wrong, data corrupted");
				alert.showAndWait();
			}

		});

		// create the buttons
		Button addButton = new Button("Add");
		addButton.setOnAction(event -> addMobileClinic());

		Button deleteClinicButton = new Button("Delete Clinic");
		deleteClinicButton.setOnAction(event -> {
			deleteClinic();
		});

		// Create a horizontal box to hold the "Add" button
		HBox buttonBox = new HBox();
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.getChildren().addAll(addButton, deleteClinicButton);

		// Add the table view and the form to the root pane
		root.setCenter(tableView);
		root.setBottom(formPane);
		BorderPane.setMargin(tableView, new Insets(10, 10, 10, 10));
		BorderPane.setMargin(formPane, new Insets(0, 10, 10, 10));

		// Add the button box to the form pane
		formPane.add(buttonBox, 3, 4);

		// Create a new graph and add vertices for each mobile clinic
		graph = new Graph<MobileClinic>();
		for (MobileClinic clinic : mobileClinics) {
			Graph.Vertex<MobileClinic> vertex = new Graph.Vertex<>(clinic);
			vertices.add(vertex);
			graph.getVertices().add(vertex);
		}

		// Create edges between all pairs of vertices
		for (int i = 0; i < vertices.size(); i++) {
			for (int j = i + 1; j < vertices.size(); j++) {
				Graph.Vertex<MobileClinic> v1 = vertices.get(i);
				Graph.Vertex<MobileClinic> v2 = vertices.get(j);
				double distance = v1.getValue().getLocation().calculateDistance(v2.getValue().getLocation());
				Graph.Edge<MobileClinic> edge = new Graph.Edge<>((int) distance, v1, v2);
				edges.add(edge);
			}
		}

		/*
		 * // Create three MobileClinic objects MobileClinic mc1 = new
		 * MobileClinic("Medi Clinic", new GPSPoint(33.9632, -118.4165), 10,
		 * Arrays.asList("Surgery", "Eye Care")); MobileClinic mc2 = new
		 * MobileClinic("Joe Clinic", new GPSPoint(34.0522, -118.2437), 15,
		 * Arrays.asList("Therapy", "Pain Reliever")); MobileClinic mc3 = new
		 * MobileClinic("Tuk Clinic", new GPSPoint(32.7157, -117.1611), 20,
		 * Arrays.asList("Toot Care", "Skin Care"));
		 * 
		 * // Create a graph and add the MobileClinic objects as vertices
		 * Graph.Vertex<MobileClinic> vertex1 = new Graph.Vertex<>(mc1);
		 * vertices.add(vertex1); graph.getVertices(); Graph.Vertex<MobileClinic>
		 * vertex2 = new Graph.Vertex<>(mc2); graph.getVertices().add(vertex2);
		 * Graph.Vertex<MobileClinic> vertex3 = new Graph.Vertex<>(mc3);
		 * graph.getVertices().add(vertex3); graph.getVertices();
		 * 
		 * // Add edges between the vertices
		 * 
		 * Graph.Edge<MobileClinic> edge = new Graph.Edge<>(5,vertex1, vertex2);
		 * edges.add(edge); graph.getEdges().add(edge); Graph.Edge<MobileClinic> edge2 =
		 * new Graph.Edge<>(8,vertex1, vertex3); edges.add(edge2);
		 * graph.getEdges().add(edge2); Graph.Edge<MobileClinic> edge3 = new
		 * Graph.Edge<>(10,vertex2, vertex3); edges.add(edge3);
		 * graph.getEdges().add(edge3);
		 * 
		 * 
		 * // Save the graph to a binary file File file = new
		 * File("resources/data/mobile_clinics.bin"); if (!file.exists()) { try {
		 * file.createNewFile(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } } FileHandler.saveAll(graph, file);
		 */

		// Set the scene for the primary stage
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void addMobileClinic() {
		edges = graph.getEdges();
		vertices = graph.getVertices();
		try {
			String name = nameField.getText();
			int capacity = Integer.parseInt(capacityField.getText());
			double lat = Double.parseDouble(locationLatField.getText());
			double lon = Double.parseDouble(locationLongField.getText());
			List<String> services = new ArrayList<>();
			String[] servicesArray = servicesField.getText().split(",");
			for (String service : servicesArray) {
				services.add(service.trim());
			}
			GPSPoint location = new GPSPoint(lat, lon);
			boolean exist = false;

			// Checking if the same clinic name exists in the graph
			for (Graph.Vertex<MobileClinic> vertex : graph.getVertices()) {
				if (name.equals(vertex.getValue().getName())) {
					exist = true;
					break;
				}
			}

			if (!exist) {
				MobileClinic mobileClinic = new MobileClinic(name, location, capacity, services);
				// Add MobileClinic object to the TableView
				mobileClinics.add(mobileClinic);
				// clear the mobileClinics list before loading the data
				clearForm();
				// Add a vertex for the new clinic
				Graph.Vertex<MobileClinic> vertex = new Graph.Vertex<>(mobileClinic);
				vertices.add(vertex);
				graph.getVertices().add(vertex);

				// Create edges between the new vertex and all other vertices
				for (Graph.Vertex<MobileClinic> v : vertices) {
					if (v != vertex) {
						double distance = v.getValue().getLocation().calculateDistance(mobileClinic.getLocation());
						Graph.Edge<MobileClinic> edge = new Graph.Edge<MobileClinic>((int) distance, v, vertex);
						edges.add(edge);
					}
				}
				// Save the graph to a binary file
				File file = new File("resources/data/clininis/mobile_clinics.bin");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FileHandler.saveAll(graph, file);
				// Show success alert
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Notification");
				alert.setContentText("Mobile Clinic added successfully");
				alert.showAndWait();
			} else {
				// Show error alert if clinic already exists
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Notification");
				alert.setContentText("Mobile Clinic already exists");
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			// Do nothing if there is an error parsing the input
		}
	}

	private void clearForm() {
		nameField.setText("");
		capacityField.setText("");
		locationLatField.setText("");
		locationLongField.setText("");
		servicesField.setText("");
	}

	private void deleteClinic() {
		MobileClinic clinic = tableView.getSelectionModel().getSelectedItem();
		if (clinic != null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you want to delete this clinic?");
			alert.setContentText("This action cannot be undone.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				// Remove clinic from the graph
				Graph.Vertex<MobileClinic> vertexToRemove = null;
				for (Graph.Vertex<MobileClinic> vertex : graph.getVertices()) {
					if (vertex.getValue().getName().equals(clinic.getName())) {
						vertexToRemove = vertex;
						break;
					}
				}
				if (vertexToRemove != null) {
					// Remove clinic vertex from the graph
					graph.getVertices().remove(vertexToRemove);
					mobileClinics.remove(clinic);

				}

				// Remove clinic from the table view and Graph
				tableView.getItems().remove(clinic);
				File file = new File("resources/data/clininis/mobile_clinics.bin");
				FileHandler.saveAll(graph, file);
		        graph = FileHandler.readAll(file);


				// Show success alert
				Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
				alert1.setTitle("Notification");
				alert1.setContentText("Mobile Clinic deleted successfully");
				alert1.showAndWait();
			}
		}
	}
}
