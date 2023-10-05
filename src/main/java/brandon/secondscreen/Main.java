package brandon.secondscreen;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*Brandon Council */
/**
 * Main class for the Inventory Management System.
 */
public class Main extends Application {
    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If the FXML file isn't found or can't be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * The main entry point for the application.
     *
     * @param args Command-line arguments.
     */

    public static void main(String[] args) {
        launch(args);
    }
}