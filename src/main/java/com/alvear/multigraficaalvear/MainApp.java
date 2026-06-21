package com.alvear.multigraficaalvear;

import com.alvear.multigraficaalvear.config.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseInitializer.initializeDatabase();

            Parent root = FXMLLoader.load(getClass().getResource("/com/alvear/multigraficaalvear/views/CuentasCorrientesView.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setTitle("Multigrafica Alvear - Modulo Ventas");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
