package sample;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

public class Main extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label idLabel = new Label("ID: ");
        Label nameLabel = new Label("Name: ");
        Label weightLabel = new Label("Weight: ");
        Label heightLabel = new Label("Height: ");
        Label abilityLabel = new Label("Ability: ");
        Label strengthLabel = new Label("Strength: ");
        Label colorLabel = new Label("Color: ");
        Label blankLabel1 = new Label("");
        Label blankLabel2 = new Label("");
        Label weightValueLabel = new Label("0.00");
        Label heightValueLabel = new Label("0.00");
        Label strengthValueLabel = new Label("0.00");

        idLabel.setPrefWidth(100);
        nameLabel.setPrefWidth(100);
        weightLabel.setPrefWidth(100);
        heightLabel.setPrefWidth(100);
        abilityLabel.setPrefWidth(100);
        strengthLabel.setPrefWidth(100);
        colorLabel.setPrefWidth(100);


        idLabel.setPadding(new Insets(0, 0, 0, 20));
        nameLabel.setPadding(new Insets(0, 0, 0, 20));
        weightLabel.setPadding(new Insets(0, 0, 0, 20));
        heightLabel.setPadding(new Insets(0, 0, 0, 20));
        abilityLabel.setPadding(new Insets(0, 0, 0, 20));
        strengthLabel.setPadding(new Insets(0, 0, 0, 20));
        colorLabel.setPadding(new Insets(0, 0, 0, 20));

        weightValueLabel.setPadding(new Insets(0, 0, 0, 10));
        heightValueLabel.setPadding(new Insets(0, 0, 0, 10));
        strengthValueLabel.setPadding(new Insets(0, 0, 0, 10));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        Slider weightSlider = new Slider(0, 30, 15);
        weightSlider.setShowTickMarks(true);
        weightSlider.setShowTickLabels(true);
        Slider heightSlider = new Slider(0, 2, 1);
        heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);
        ComboBox<String> abilityBox = new ComboBox<>();
        abilityBox.getItems().addAll("Fire","Water","Grass","Elec","Ice","Ground","Steel","Rock");
        Slider strengthSlider = new Slider(0, 100, 50);
        strengthSlider.setShowTickMarks(true);
        strengthSlider.setShowTickLabels(true);
        strengthSlider.setSnapToTicks(true);
        ComboBox<String> colorBox = new ComboBox<>();
        colorBox.getItems().addAll("Red", "Green", "Blue", "Orange");
        colorBox.getItems().setAll("BLACK", "BLUE", "GREEN", "RED");

        weightSlider.valueProperty().addListener(event -> {
                weightValueLabel.setText(String.format("%.2f", weightSlider.getValue()));
        });

        heightSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number before, Number after) {
                double heightValue = heightSlider.getValue();
                heightValueLabel.setText(String.format("%.2f", heightValue));
            }
        });

        strengthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number before, Number after) {
                double strengthValue = strengthSlider.getValue();
                strengthValueLabel.setText(String.format("%.2f", strengthValue));
            }
        });

        Button getAll = new Button("See all Tokimon");
        Button getById = new Button("See Tokimon by ID");
        Button addToList = new Button("Add Tokimon to List");
        Button deleteByID = new Button("Delete Tokimon by ID");

        GridPane gridpane = new GridPane();
        gridpane.setVgap(5);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.add(getAll, 1, 0);

        gridpane.add(blankLabel1, 1, 1);

        gridpane.add(idLabel, 0, 2);
        gridpane.add(idField, 1, 2);
        gridpane.add(getById, 1, 3);
        gridpane.add(deleteByID, 1, 4);

        gridpane.add(blankLabel2, 1, 5);

        gridpane.add(nameLabel, 0, 6);
        gridpane.add(nameField, 1, 6);
        gridpane.add(weightLabel, 0, 7);
        gridpane.add(weightSlider, 1, 7);
        gridpane.add(weightValueLabel, 2, 7);
        gridpane.add(heightLabel, 0, 8);
        gridpane.add(heightSlider, 1, 8);
        gridpane.add(heightValueLabel, 2, 8);
        gridpane.add(abilityLabel, 0, 9);
        gridpane.add(abilityBox, 1, 9);
        gridpane.add(strengthLabel, 0, 10);
        gridpane.add(strengthSlider, 1, 10);
        gridpane.add(strengthValueLabel, 2, 10);
        gridpane.add(colorLabel, 0, 11);
        gridpane.add(colorBox, 1, 11);
        gridpane.add(addToList, 1, 12);

        getAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    URL url = new URL("http://localhost:8080/api/tokimon/all");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);

                    File file = new File("scraped/tokimon.json");
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String output;
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    while((output = br.readLine()) != null) {
                        writer.write(output);
                        writer.close();
                    }
                    List<Tokimon> tokimons = new ArrayList<>();
                    JSONParser parser = new JSONParser();
                    try {
                        Object objects = parser.parse(new FileReader("scraped/tokimon.json"));
                        JSONArray arrayOfObjects =  (JSONArray) objects;
                        for(Object singleJSONObject : arrayOfObjects) {
                            Gson gson = new Gson();
                            String jsonObjectAsString = singleJSONObject.toString();
                            tokimons.add(gson.fromJson(jsonObjectAsString, Tokimon.class));
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(connection.getResponseCode());
                    connection.disconnect();

                    Stage subStage = new Stage();
                    subStage.setTitle("Tokimon Information");
                    GridPane innerGridPane = new GridPane();
                    Label instructionLabel = new Label("Hover over the shape to get more info! (May take a bit to load)");
                    HBox hbox = new HBox(30, instructionLabel);
                    BorderPane borderPane = new BorderPane();
                    innerGridPane.setPadding(new Insets(50, 50, 50, 0));
                    innerGridPane.setHgap(10);
                    innerGridPane.setAlignment(Pos.BOTTOM_CENTER);
                    int i = 0;
                    for (Tokimon tokimon: tokimons){
                        double boxX = 30+i*70*5;
                        double boxY = 100+100-tokimon.getStrength();
                        double boxWidth = tokimon.getWeight()*tokimon.getHeight()*3;
                        double boxHeight = tokimon.getStrength()*3;
                        Rectangle box = new Rectangle(boxX, boxY, boxWidth, boxHeight);
                        if (tokimon.getColor().equals("Blue")){
                            box.setFill(Color.BLUE);
                        }
                        else if (tokimon.getColor().equals("Red")){
                            box.setFill(Color.RED);
                        }
                        else if (tokimon.getColor().equals("Green")){
                            box.setFill(Color.GREEN);
                        }
                        else if (tokimon.getColor().equals("Orange")){
                            box.setFill(Color.ORANGE);
                        }
                        Tooltip tooltip = new Tooltip("ID: " + tokimon.getId() + "\n" + "Name: " + tokimon.getName() + "\n"
                                + "Weight: " + tokimon.getWeight() + "\n" + "Height: " + tokimon.getHeight() + "\n"
                                + "Ability: " + tokimon.getAbility() + "\n" + "Strength: " + tokimon.getStrength() + "\n");
                        Tooltip.install(box, tooltip);
                        innerGridPane.add(box, i, 1);
                        i++;
                    }
                    borderPane.setCenter(innerGridPane);
                    borderPane.setTop(hbox);
                    Scene subScene = new Scene(borderPane, 150*i, 350);
                    subStage.setScene(subScene);
                    subStage.show();

                }
                catch (IOException e){

                }
            }
        });

        getById.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    String id = idField.getText();
                    URL url = new URL("http://localhost:8080/api/tokimon/" + id);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    System.out.println(connection.getResponseCode());
                    connection.disconnect();
                }
                catch (IOException e){

                }
            }
        });

        addToList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    URL url = new URL("http://localhost:8080/api/tokimon/add");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    String name = nameField.getText();
                    double weight = Double.parseDouble(String.format("%.2f", weightSlider.getValue()));
                    double height = Double.parseDouble(String.format("%.2f", heightSlider.getValue()));
                    String ability = abilityBox.getValue();
                    double strength = Double.parseDouble(String.format("%.2f", strengthSlider.getValue()));
                    String color = colorBox.getValue();
                    wr.write("{\"name\":" + "\"" + name + "\"" +",\"ability\":" + "\"" + ability + "\"" + ",\"strength\":" + strength + ",\"weight\":" + weight + ",\"height\":" + height +",\"color\":"+ "\"" + color + "\"" + "}");
                    wr.flush();
                    wr.close();

                    connection.connect();
                    System.out.println(connection.getResponseCode());
                    connection.disconnect();
                }
                catch (IOException e){

                }
            }
        });

        deleteByID.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    String id = idField.getText();
                    URL url = new URL("http://localhost:8080/api/tokimon/" + id);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");

                    connection.connect();
                    System.out.println(connection.getResponseCode());
                    connection.disconnect();
                }
                catch (IOException e){

                }
            }
        });

        Scene scene = new Scene(gridpane, 350, 450);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tokimon Controller");
        primaryStage.show();
    }
}