import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;






public class SlotMachineFX extends Application
{
	public static void main(String args[])	{launch(args);}
	
	VBox root;
	VBox MainMenu			= createMainMenu();
    VBox MenuSingle			= createMenuSingle();
    VBox MenuMultiw			= createMenuMultiw();
    VBox SinglerowMachine	= createSinglerowMachine();
    VBox MultiwayMachine	= createMultiwayMachine();
    
    VBox DBText				= createDisplayDB(false);
    VBox DBTScript			= createDisplayDB(true);
	
    @Override public void start(Stage stage) throws Exception
    {
        //* ***** Declaration of scenes. *****
//        Scene MainMenu			= new Scene(createMainMenu());
//        Scene MenuSingle		= new Scene(createMenuSingle());
//        Scene MenuMultiw		= new Scene(createMenuMultiw());
//        Scene SinglerowMachine	= new Scene(createSinglerowMachine());
//        Scene MultiwayMachine	= new Scene(createMultiwayMachine());
//        
//        Scene DBText			= new Scene(createDisplayDB(false));
//        Scene DBTScript			= new Scene(createDisplayDB(true));
    	
        
        
        //***** Declaration of buttons for menu navigation. *****
    	exit.setOnAction((ActionEvent event) -> {Platform.exit();});
    	openMenuSingle.setOnAction(new openSingleRow());
    	openMenuMultiw.setOnAction(new openMultiway());
    	
    	backToMainMenu1.setOnAction(new returnMainMenu());
    	backToMainMenu2.setOnAction(new returnMainMenu());
    	
//        openMenuMultiw.setOnAction(e ->		stage.setScene(MenuMultiw));
//        openMachineSingle.setOnAction(e ->	stage.setScene(SinglerowMachine));
//        openMachineMultiw.setOnAction(e -> 	stage.setScene(MultiwayMachine));
//        backToMainMenu1.setOnAction(e ->	stage.setScene(MainMenu));
//        backToMainMenu2.setOnAction(e ->	stage.setScene(MainMenu));
//        backToMainMenu3.setOnAction(new returnMainMenu());
//        backToMainMenu4.setOnAction(new returnMainMenu());
//        backToMenuSingle.setOnAction(e ->	stage.setScene(MenuSingle));
//        backToMenuMultiw.setOnAction(e ->	stage.setScene(MenuMultiw));
        
        
        //***** Declaration of buttons for different information displays. ******
//        displayDBText.setOnAction(e ->		stage.setScene(DBText));
//        displayDBTable.setOnAction(e ->		stage.setScene(DBTScript));
        
        
        stage.setTitle("Slot Machine");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }
	
	
    
    
    static TextArea text;
    
    
	
	//Buttons for menu navigation
    static Button openMenuSingle, openMenuMultiw, openMachineSingle, openMachineMultiw;
    static Button backToMainMenu1, backToMainMenu2, backToMainMenu3, backToMainMenu4,
    	backToMenuSingle, backToMenuMultiw;
    //Buttons for the Main Menu
    static Button displayDBText, displayDBTable, showDiff, exit;
	//Buttons for the Single-row Menu
    static Button showRulesSingle, changeParamSingle;
	//Buttons for the Multi-way Menu
    static Button showRulesMultiw, changeParamMultiw;
    //Size for the window
    static int size = 300;
    
    
    
    
    //Arranges all the buttons for the menu to be displayed correctly
    public void applyMenuConfig(GridPane grid, Button... args)
    {
    	int maxCols = ((args.length==1)? 1 : 2);
    	int maxRows = args.length/maxCols;
    	int col=0, row=0;
    	
    	int buttonWidth=180, buttonHeight=80;
    	
    	for (Button btn : args)
    	{
    		//Makes all the buttons look the same
    		btn.setMinSize(buttonWidth, buttonHeight);
    		btn.fontProperty();
    		
    		//Add the button to the desired position
    		grid.add(btn, col, row);
    		// and prepare the position of the next
    		col += 1;
    		if (col==maxCols)
    		{
    			col = 0;
    			row += 1;
    			if (row==maxRows)	{break;}
    		}
    	}
    }
    
    
    //Creates the scene for the Main Menu
    public VBox createMainMenu()
    {
    	Label title0 = new Label("Main Menu");
    	GridPane grid0 = new GridPane();
    	VBox menu0 = new VBox(title0, grid0);
    	
        openMenuSingle	= new Button("Single-row\n"+"Slot Machine");
        openMenuMultiw	= new Button("Multiwway\n"+"Slot Machine");
        displayDBText	= new Button("Display Database\n"+"plainly");
        displayDBTable	= new Button("Display Database\n"+"with script");
        showDiff		= new Button("Show differences");
        exit			= new Button("Exit");
        
        applyMenuConfig(grid0,
        	openMenuSingle, openMenuMultiw, displayDBText, displayDBTable, showDiff, exit);
        
        menu0.setAlignment(Pos.CENTER);
        title0.setAlignment(Pos.CENTER);
        grid0.setAlignment(Pos.CENTER);
    	return menu0;
    }
    
    //Creates the scene for the Single-row Machine menu options
    public VBox createMenuSingle()
    {
    	Label title1 = new Label("Single-row Slot Machine");
    	GridPane grid1 = new GridPane();
    	VBox menu1 = new VBox(title1, grid1);
    	    	
        openMachineSingle	= new Button("Play single-row machine");
        showRulesSingle		= new Button("View the ruleset");
        changeParamSingle	= new Button("Change the parameters");
        backToMainMenu1		= new Button("Return to the menu");
        
        applyMenuConfig(grid1,
        	openMachineSingle, showRulesSingle, changeParamSingle, backToMainMenu1);
        
        menu1.setAlignment(Pos.CENTER);
        title1.setAlignment(Pos.CENTER);
        grid1.setAlignment(Pos.CENTER);
    	return menu1;
    }
    
    //Creates the scene for the Multi-way Machine menu options
    public VBox createMenuMultiw()
    {
    	Label title2 = new Label("Multiwway Slot Machine");
        GridPane grid2 = new GridPane();
        VBox menu2 = new VBox(title2, grid2);
        
        openMachineMultiw	= new Button("Play Multiwway machine");
        showRulesMultiw		= new Button("View the ruleset");
        changeParamMultiw	= new Button("Change the parameters");
        backToMainMenu2		= new Button("Return to the menu");
        
        applyMenuConfig(grid2,
        	openMachineMultiw, showRulesMultiw, changeParamMultiw, backToMainMenu2);
        
    	menu2.setAlignment(Pos.CENTER);
    	title2.setAlignment(Pos.CENTER);
        grid2.setAlignment(Pos.CENTER);
    	return menu2;
    }
    
    
    //Creates the scene for Single-row Machine and its reels
    public VBox createSinglerowMachine()
    {
    	Label title1 = new Label("Single-row Slot Machine");
    	GridPane grid = new GridPane();
    	VBox menu1 = new VBox(title1, grid);
    	
    	backToMenuSingle = new Button("Return to the machine menu");
    	
    	applyMenuConfig(grid, backToMenuSingle);
    	
    	menu1.setAlignment(Pos.CENTER);
        title1.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
    	return menu1;
    }
    
    
    //Creates the scene for Single-row Machine and its reels
    public VBox createMultiwayMachine()
    {
    	Label title2 = new Label("Multi-way Slot Machine");
    	GridPane grid = new GridPane();
    	VBox menu2 = new VBox(title2, grid);
    	
    	backToMenuMultiw = new Button("Return to the machine menu");
    	
    	applyMenuConfig(grid, backToMenuMultiw);
    	
    	menu2.setAlignment(Pos.CENTER);
        title2.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
    	return menu2;
    }
    
    
    
    
    //
    public VBox createDisplayDB(boolean script)
    {
    	Label title = new Label("Database display\n"+(script? "with script":"as plain text" ));
    	TextArea text = new TextArea();
    	backToMainMenu3 = new Button("Return to the main menu");
    	backToMainMenu4 = new Button("Return to the main menu");
    	
    	VBox root = new VBox(title, text, ((script)? backToMainMenu3 : backToMainMenu4));
    	
    	title.setAlignment(Pos.CENTER);
    	root.setAlignment(Pos.CENTER);
    	text.setCenterShape(true);
		text.setWrapText(true);
		text.setEditable(false);
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PrintStream printSteam = new PrintStream(outStream);
		PrintStream old = System.out;
		System.setOut(printSteam);
		
		SlotMachine.displayDB(script);
		
		System.out.flush();
		System.setOut(old);
		
		text.appendText(outStream.toString());
    	
    	return root;
    }
    
    
    
    class returnMainMenu implements EventHandler<ActionEvent>
    {
		@Override public void handle(ActionEvent e) {root.getScene().setRoot(MainMenu);}
    }
    
    class openSingleRow implements EventHandler<ActionEvent>
    {
    	@Override public void handle(ActionEvent e) {root.getScene().setRoot(MenuSingle);}
    }
    class openMultiway implements EventHandler<ActionEvent>
    {
    	@Override public void handle(ActionEvent e) {root.getScene().setRoot(MenuMultiw);}
    }
    
    
    
	    
}