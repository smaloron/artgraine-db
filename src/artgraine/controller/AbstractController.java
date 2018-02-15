package artgraine.controller;

import artgraine.Main;
import javafx.stage.Stage;

public abstract class AbstractController {
    protected Main main;
    protected Stage stage;

    public void setMain(Main main, Stage stage) {
        this.main = main;
        this.stage = stage;
    }

    public void closeWindow(){
        stage.close();
    }
}
