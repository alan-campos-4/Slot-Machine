import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;




public class JFX extends Application
{
	
	@Override
	public void start(Stage stage)
	{
		
		GridPane menu = new GridPane();
        Label title = new Label("--- Main Menu ---");
        Button p1 = new Button("Single-row\n"+"Slot Machine");
        Button p2 = new Button("Multiway\n"+"Slot Machine");
        Button s1 = new Button("Display Database\n"+"plainly");
        Button s2 = new Button("Display Database\n"+"with script");
        Button e1 = new Button("Show differences");
        Button e2 = new Button("Exit");
        
        int menuWidth = 200;
        int menuHeight = 80;
        menu.setAlignment(Pos.CENTER);
        title.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(title, 2);
        
        title.setMinWidth(menuWidth);
        p1.setMinSize(menuWidth, menuHeight);
        p2.setMinSize(menuWidth, menuHeight);
        s1.setMinSize(menuWidth, menuHeight);
        s2.setMinSize(menuWidth, menuHeight);
        e1.setMinSize(menuWidth, menuHeight);
        e2.setMinSize(menuWidth, menuHeight);
        
        p1.setOnAction(new playSingle());
        p2.setOnAction(new playMultiway());
        s1.setOnAction(new displayPlain());
        s2.setOnAction(new displayScript());
        e1.setOnAction(new differences());
        e2.setOnAction((ActionEvent event) -> {Platform.exit();});
        
        menu.add(title, 0, 0, 2, 1);
        menu.add(p1, 0, 1);
        menu.add(s1, 1, 1);
        menu.add(p2, 0, 2);
        menu.add(s2, 1, 2);
        menu.add(e1, 0, 3);
        menu.add(e2, 1, 3);
        
        stage.setScene(new Scene(menu));
		stage.setTitle("Slot Machine");
		stage.setMaximized(true);
		stage.show();
        
	}
	
	
	public static void main(String[] args)
		{launch(args);}
	
	
	
	
	
	
	class playSingle implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e) {System.out.println("[][][][].");}
	}
	
	class playMultiway implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e) {System.out.println("[][][]\n[][][].");}
	}
	
	
	
	
	class displayPlain implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e) {System.out.println("Table.");}
	}
	
	class displayScript implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e)	{System.out.println("DBTablePrinter.");}
	}
	
	
	
	
	class differences implements EventHandler<ActionEvent> 
	{
		@Override
		public void handle(ActionEvent e) {System.out.println("Differences.");}
	}
	
	
	
	
}