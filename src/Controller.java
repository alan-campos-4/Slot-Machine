import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Controller
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToMenu(ActionEvent event, String name) throws IOException
	{
		String menu;
		if (name=="main")			{menu = "MainMenu.fxml";}
		else if (name=="single")	{menu = "SinglerowsMenu.fxml";}
		else						{menu = "MultiwayMenu.fxml";}
		
		root = FXMLLoader.load(getClass().getResource(menu));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
