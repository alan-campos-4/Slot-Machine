import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class JFX_SceneBuilder extends Application
{
	
	@Override
	public void start(Stage stage)
	{
        
        Parent root;
        try
        {
			root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("Slot Machine");
			stage.setMaximized(true);
			stage.show();
		}
        catch (IOException e) {e.printStackTrace();}
        
	}
	
	
	public static void main(String[] args)
		{launch(args);}
	
	
	
}