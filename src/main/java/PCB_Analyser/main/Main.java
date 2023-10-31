package PCB_Analyser.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * A Printed Circuit Board analysis program.
 *
 * @author Dean Lonergan
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        VBox mainVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/PCB_Analyser/views/main-view.fxml")));
        Scene main = new Scene(mainVB, 536, 560);
        stage.setTitle("Printed Circuit Board Analyser");
        stage.setScene(main);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}