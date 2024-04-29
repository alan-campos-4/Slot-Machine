import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class JFX extends Application
{
	public static void main(String args[])	{launch(args);}
	
    @Override    
    public void start(Stage stage) throws Exception
    {
    	//Declaration of labels
        Label title0 = new Label("Main Menu");
        Label title1 = new Label("Single-row Slot Machine");
        Label title2 = new Label("Multiway Slot Machine");
        title0.setAlignment(Pos.CENTER);
        title1.setAlignment(Pos.CENTER);
        title2.setAlignment(Pos.CENTER);
        
        
        //Declaration and assigning of buttons
        int buttonWidth = 200;
        int buttonHeight = 80;
        
        GridPane ops0 = new GridPane();
        Button openMenu1 = new Button("Single-row\n"+"Slot Machine");
        Button openMenu2 = new Button("Multiway\n"+"Slot Machine");
        Button displayD1 = new Button("Display Database\n"+"plainly");
        Button displayD2 = new Button("Display Database\n"+"with script");
        Button diff = new Button("Show differences");
        Button exit = new Button("Exit");
        openMenu1.setMinSize(buttonWidth, buttonHeight);
        openMenu2.setMinSize(buttonWidth, buttonHeight);
        displayD1.setMinSize(buttonWidth, buttonHeight);
        displayD2.setMinSize(buttonWidth, buttonHeight);
        diff.setMinSize(buttonWidth, buttonHeight);
        exit.setMinSize(buttonWidth, buttonHeight);
        ops0.setAlignment(Pos.CENTER);
        ops0.add(openMenu1, 0, 1);
        ops0.add(openMenu2, 1, 1);
        ops0.add(displayD1, 0, 2);
        ops0.add(displayD2, 1, 2);
        ops0.add(diff, 0, 3);
        ops0.add(exit, 1, 3);
        
        
        GridPane ops1 = new GridPane();
        Button playSingle	= new Button("Play single-row machine");
        Button showRules1	= new Button("View the ruleset");
        Button changeParam1	= new Button("Change the parameters");
        Button openMenuM1	= new Button("Return to the menu");
        playSingle.setMinSize	(buttonWidth, buttonHeight);
        showRules1.setMinSize	(buttonWidth, buttonHeight);
        changeParam1.setMinSize	(buttonWidth, buttonHeight);
        openMenuM1.setMinSize	(buttonWidth, buttonHeight);
        ops1.setAlignment(Pos.CENTER);
        ops1.add(playSingle,	0, 1);
        ops1.add(showRules1,	0, 2);
        ops1.add(changeParam1,	1, 1);        
        ops1.add(openMenuM1,	1, 2);
        
        
        GridPane ops2 = new GridPane();
        Button playMulti	= new Button("Play multiway machine");
        Button showRules2	= new Button("View the ruleset");
        Button changeParam2	= new Button("Change the parameters");
        Button openMenuM2	= new Button("Return to the menu");
        playMulti.setMinSize	(buttonWidth, buttonHeight);
        showRules2.setMinSize	(buttonWidth, buttonHeight);
        changeParam2.setMinSize	(buttonWidth, buttonHeight);
        openMenuM2.setMinSize	(buttonWidth, buttonHeight);
        ops2.setAlignment(Pos.CENTER);
        ops2.add(playMulti,		0, 1);
        ops2.add(showRules2,	0, 2);
        ops2.add(changeParam2,	1, 1);        
        ops2.add(openMenuM2,	1, 2);
        
        
        
        
        
        //Declaration of the menus
        VBox menu0 = new VBox(title0, ops0);
        VBox menu1 = new VBox(title1, ops1);
        VBox menu2 = new VBox(title2, ops2);
        menu0.setAlignment(Pos.CENTER);
        menu1.setAlignment(Pos.CENTER);
        menu2.setAlignment(Pos.CENTER);
        
        //Declaration of the scenes and scene navigation.
        Scene scene0 = new Scene(menu0, 300, 300);
        Scene scene1 = new Scene(menu1, 300, 300);
        Scene scene2 = new Scene(menu2, 300, 300);
        
        exit.setOnAction((ActionEvent event) -> {Platform.exit();});
        openMenu1.setOnAction(e -> stage.setScene(scene1));
        openMenu2.setOnAction(e -> stage.setScene(scene2));
        openMenuM1.setOnAction(e -> stage.setScene(scene0));
        openMenuM2.setOnAction(e -> stage.setScene(scene0));
        
        
        //Declaration of the button actions
        playSingle.setOnAction(new PlaySingleRow());
        playMulti.setOnAction(new PlayMultiway());
        
        
        //Setting the Stage 
        stage.setTitle("Slot Machine");
        stage.setMaximized(true);
        stage.setScene(scene0);
        stage.show();
        
    }
    
    
    
    
    
    
    
    
    class PlaySingleRow implements EventHandler<ActionEvent>
    {
    	@Override
    	public void handle(ActionEvent e) {System.out.println("Single-row");}
    }
    
    
    class PlayMultiway implements EventHandler<ActionEvent>
    {
    	@Override
    	public void handle(ActionEvent e) {System.out.println("Multiway");}
    }
	
	
	
	
	
}