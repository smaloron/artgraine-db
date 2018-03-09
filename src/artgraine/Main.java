package artgraine;

import artgraine.controller.MainController;
import artgraine.controller.SculptureFormController;
import artgraine.database.DataBaseMigrator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {

        DataBaseMigrator dataBaseMigrator = new DataBaseMigrator();
        dataBaseMigrator.migrate();

        this.mainStage = primaryStage;
        showMainWindow();

    }

    private void showMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/MainView.fxml"));
            Pane pane = loader.load();
            mainStage.setTitle("Artgraine");
            mainStage.setScene(new Scene(pane, 800, 600));
            mainStage.show();

            MainController mainController = loader.getController();
            mainController.setMain(this, mainStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
