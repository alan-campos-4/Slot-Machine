import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;




/* TODO:
 * - Implement re-roll 
 * - SlotMachine improvements
 * 
 * */




public class SlotNodes_1 extends Application
{
	
	//*************** MAIN ***************
	
	public static void main(String[] args)	{launch(args);}
	@Override public void start(Stage stage)
    {
		try {
			
			Single	= new SlotMachine.SingleRow();
			Multi	= new SlotMachine.Multiway();
			
	        MainMenu main		= new MainMenu("----- MAIN MENU -----\n\n\n\n");
			MenuSingle menu1	= new MenuSingle("-- SINGLE-ROW MENU --\n", main);
			MenuMulti menu2		= new MenuMulti("-- MULTIWAY MENU --\n", main);
			MachineSingle Mach1	= new MachineSingle("Play the game", menu1);
			MachineMulti Mach2	= new MachineMulti("Play the game", menu2);
			DB1_Lay DBText		= new DB1_Lay("Display database\nas plain text", main);
			DB2_Lay DBScript	= new DB2_Lay("Display database\nwith script", main);
			ParamSingle param1	= new ParamSingle("Change the parameters\n of the machine", menu1);
			ParamMulti param2	= new ParamMulti("Change the parameters\n of the machine", menu2);
			main.addNext(menu1, menu2, DBText, DBScript);
			menu1.addNext(Mach1, param1);
			menu2.addNext(Mach2, param2);
	        
			stage.setTitle("Welcome to the Casino!");
	        stage.setScene(new Scene(main, sceneWidth, sceneHeight));
	        stage.show();
	        
    	} catch (Exception e) {e.printStackTrace();}
    }
	
	
	
	
	//********** Static variables **********
	
	// Machine objects that the UI will work with.
	public static SlotMachine.SingleRow Single;
	public static SlotMachine.Multiway Multi;
	
	// Established sizes for elements.
	public static int sceneWidth=600,	sceneHeight=500;
	public static int textWidth=600,	textHeight=350;
	public static int alertWidth=400,	alertHeight=200;
	public static int buttonWidth=130,	buttonHeight=80;
	
	
	
	
	
	
	//********** Base node **********
	
	//Class that allows for changing between "previous" and "next" scene roots.
	private abstract class Layout extends VBox
    {
		protected Layout prev;
    	protected ArrayList<Layout> next;
    	protected Label title;
    	protected GridPane grid;
    	
    	public Layout(String titletext, Layout prevNode)
        {
    		this.prev = prevNode;
    		this.next = new ArrayList<Layout>();
    		this.title = new Label(titletext);
    		this.grid = new GridPane();
    		
    		this.title.setTextAlignment(TextAlignment.CENTER);
    		this.title.setAlignment(Pos.CENTER);
    		this.grid.setAlignment(Pos.CENTER);
            this.setAlignment(Pos.CENTER);
            
            this.getChildren().addAll(title);
            createGUI();
        }

        protected void addNext(Layout... args)	{for (Layout node : args) {next.add(node);}}
        protected void callNext(int i)			{getScene().setRoot(next.get(i));}
        protected void callPrev()				{getScene().setRoot(prev);}
        
        abstract void createGUI();
        
    }
	
	
	
	
	
	
	//********** Menu options UI nodes **********
	
	//Applies the desired settings to the buttons and adds them to the pane.
	public void createButtonMenu(GridPane grid, Button... btns)
    {
		int maxCols=3, col=0, row=0;
    	
    	for (Button btn : btns)
    	{
    		//Makes all the buttons look the same.
    		btn.setMinSize(buttonWidth, buttonHeight);
    		btn.setTextAlignment(TextAlignment.CENTER);
    		btn.fontProperty();
    		
    		//Adds the button to the desired position in the gridPane
    		grid.add(btn, col, row);
    		// and prepares the position of the next.
    		col += 1;
    		if (col==maxCols)
    		{
    			col = 0;
    			row += 1;
    			//If there are 4 buttons, put the last one in the middle.
    			if (btns.length==4 && row==1) {col+=1;}
    		}
    	}
    }
	
	//Layout containing the main navigation menu.
	public class MainMenu extends Layout
	{
		public MainMenu(String title)	{super(title, null);}
		public void createGUI()
		{
			//***	Creates the button menu.	***
			Button btn1 = new Button("Single-row\nSlot Machine");
			Button btn2 = new Button("Multi-way\nSlot Machine");
			Button btn3 = new Button("Display database\nas plain text");
			Button btn4 = new Button("Display database\nwith script");
			Button btn5 = new Button("Show\ndifferences");
			Button btn6 = new Button("Exit");
			createButtonMenu(grid, btn1, btn2, btn5, btn3, btn4, btn6);
            getChildren().addAll(grid);
            
            //***	Creates the alert to access the slot machines.	***
            Alert openMenu	= new Alert(AlertType.CONFIRMATION);
            GridPane gridp	= new GridPane();
            TextField input	= new TextField();
        	Label labelName	= new Label("What is your name?: ");
        	gridp.setAlignment(Pos.CENTER);
        	gridp.add(labelName,	0, 0);
        	gridp.add(input,	1, 0);
        	gridp.setHgap(10);
        	gridp.setVgap(10);
        	openMenu.setTitle("Enter your name and bet"); 
        	openMenu.setHeaderText("");
        	openMenu.getDialogPane().setContent(gridp);
        	openMenu.getDialogPane().setMinWidth(alertWidth);
        	openMenu.getDialogPane().setMinHeight(alertHeight);
	    	
	    	//***	Creates the alert to exit the program.	***
	    	Alert logout = new Alert(AlertType.CONFIRMATION);
	    	logout.setTitle("Logout");
	    	logout.setHeaderText("You are about to logout.");
	    	logout.setContentText("Are you sure you want\nto exit the program?");
	    	
	    	//***	Creates the alert that appears if an error occurs.	***
	    	Alert wrong = new Alert(AlertType.ERROR);
	    	wrong.setTitle("Error");
	    	wrong.setHeaderText("Something went wrong.");
	    	wrong.setContentText("The inputs are not valid.");
	    	
	    	
	    	//***	Set the function of each button.	***
	    	btn3.setOnAction((ActionEvent event) -> {callNext(2);});
			btn4.setOnAction((ActionEvent event) -> {callNext(3);});
			btn5.setOnAction((ActionEvent event) -> {new Differ_Al().showAndWait();});
			btn6.setOnAction((ActionEvent event) -> 
			{
				if (logout.showAndWait().get()==ButtonType.OK) {Platform.exit();}
			});
			btn1.setOnAction((ActionEvent event) -> 
			{
				if (openMenu.showAndWait().get()==ButtonType.OK)
				{
					if (!input.getText().isEmpty())
					{
						callNext(0);
						Single.assignName(input.getText());
					}
					else {wrong.showAndWait();}
				}
			});
			btn2.setOnAction((ActionEvent event) -> 
			{
				if (openMenu.showAndWait().get()==ButtonType.OK)
				{
					if (!input.getText().isEmpty())
					{
						callNext(1);
						Multi.assignName(input.getText());
					}
					else {wrong.showAndWait();}
				}
			});
		}
		
	}
	
	
	//Layout containing the sub-menus for each machine type.
	public abstract class SubMenu extends Layout
	{
		Text params;
		public SubMenu(String title, Layout prev)	{super(title, prev); setParamText();}
		abstract void showRules();
		abstract void setParamText();
		abstract void setBet(double b);
		void createGUI()
		{
			params = new Text();
			Button btn1 = new Button("Play the\nSlot Machine");
			Button btn2 = new Button("Change\nParameters");
			Button btn3 = new Button("Show the\nrules");
			Button btn4 = new Button("Return to the\nmain menu");
			
			createButtonMenu(grid, btn1, btn2, btn3, btn4);
			getChildren().addAll(params, grid);
			
			TextFormatter<Integer> form = new TextFormatter<Integer>(new IntegerStringConverter());
            Alert openMachine	= new Alert(AlertType.CONFIRMATION);
            GridPane gridpane	= new GridPane();
            TextField inputBet	= new TextField();
        	Label labelBet		= new Label("How much do you want to bet?    "
        						+ "\n("+SlotMachine.BETMIN+" - "+SlotMachine.BETMAX+"): ");
        	gridpane.setAlignment(Pos.CENTER);
        	gridpane.add(labelBet,	0, 0);
        	gridpane.add(inputBet,	1, 0);
        	gridpane.setHgap(10);
        	gridpane.setVgap(10);
        	inputBet.setTextFormatter(form);
        	openMachine.setTitle("Enter your name and bet"); 
        	openMachine.setHeaderText("");
        	openMachine.getDialogPane().setMinWidth(alertWidth);
	    	openMachine.getDialogPane().setMinHeight(alertHeight);
	    	openMachine.getDialogPane().setContent(gridpane);
	    	
	    	Alert wrong = new Alert(AlertType.ERROR);
	    	wrong.setTitle("Error");
	    	wrong.setHeaderText("Something went wrong.");
	    	wrong.setContentText("The inputs are not valid.");
        	
			
	    	btn2.setOnAction((ActionEvent event) -> {callNext(1);});
			btn3.setOnAction((ActionEvent event) -> {showRules();});
			btn4.setOnAction((ActionEvent event) -> {callPrev();});
			btn1.setOnAction((ActionEvent event) -> 
			{
				if (openMachine.showAndWait().get()==ButtonType.OK)
				{
					if (form.getValue()>=SlotMachine.BETMIN
					&& form.getValue()<=SlotMachine.BETMAX)
					{
						callNext(0);
						setBet(form.getValue());
					}
					else {wrong.showAndWait();}
				}
			});
		}
	}
	
	//Sub-menu for the single-row slot machine.
	public class MenuSingle extends SubMenu
	{
		public MenuSingle(String title, Layout prev)	{super(title, prev);}
		void showRules()		{new Rules1_Al().showAndWait();}
		void setBet(double b)	{Single.P.betInit(b);}
		void setParamText()
		{
			params.setText
			("This machine has "+Single.getReels()+" reels and "+Single.getSyms()+" symbols."
			+ "\n and costs "+Single.getCost()+" € to spin per turn.\n");
		}
	}
	
	//Sub-menu for the multi-row slot machine.
	public class MenuMulti extends SubMenu
	{
		public MenuMulti(String title, Layout prev)		{super(title, prev);}
		void showRules()		{new Rules2_Al().showAndWait();}
		void setBet(double b)	{Multi.P.betInit(b);}
		void setParamText()
		{
			params.setText
			("This machine has "+Multi.getRows()+" rows, "
			+Multi.getReels()+" reels and "+ Multi.getSyms()+" symbols."
			+ "\n and costs "+Multi.getCost()+" € to spin per turn.\n");
		}
	}
	
	
	
	
	
	
	
	//********** Parameter change UI nodes **********
	
	//Layout that allows for the change of a machine's parameters.
	public abstract class Parameters extends Layout
	{
		protected TextFormatter<Integer> form1, form2, form3;
		protected TextField input1, input2, input3;
		protected Label label1, label2, label3, limit1, limit2, limit3;
		protected Button change, rtn;
		protected Alert complete, wrong;
		
		public Parameters(String title, Layout prev)	{super(title, prev);}
		abstract void changeParams();
		void createGUI()
		{
			label1 = new Label("Rows ");
			label2 = new Label("Reels ");
			label3 = new Label("Symbols ");
			input1 = new TextField();
			input2 = new TextField();
			input3 = new TextField();
			
			form1 = new TextFormatter<>(new IntegerStringConverter());
	        form2 = new TextFormatter<>(new IntegerStringConverter());
	        form3 = new TextFormatter<>(new IntegerStringConverter());
	        input1.setTextFormatter(form1);
	        input2.setTextFormatter(form2);
	        input3.setTextFormatter(form3);
			
	        change = new Button("Establish the new parameters");
			rtn = new Button("Return to the previous menu");
			rtn.setOnAction((ActionEvent e) -> {callPrev();});
			
			wrong = new Alert(AlertType.ERROR);
			wrong.setTitle("Error");
			wrong.setHeaderText("");
			wrong.setContentText("One of the values is not valid");
			
			complete = new Alert(AlertType.INFORMATION);
			complete.setTitle("Parameters changed");
	    	complete.setHeaderText("");
	    	complete.setContentText("The new parameters have been successfully\n"
	    			+ "established in the machine");
			
	    	changeParams();
	    	
			setSpacing(10);
			getChildren().addAll(grid, change, rtn);
		}
	}
	
	//Layout for changing the parameters of the single-row slot machine.
	public class ParamSingle extends Parameters
	{
		public ParamSingle(String title, Layout prev)	{super(title, prev);}
		void changeParams()
		{
			limit2 = new Label("("+Single.minSize+" - "+Single.maxSize+") ");
			limit3 = new Label("("+Single.minSyms+" - "+Single.maxSyms+") ");
			grid.add(label2,	0, 0);
			grid.add(limit2,	1, 0);
			grid.add(input2,	2, 0);
			grid.add(label3,	0, 1);
			grid.add(limit3,	1, 1);
			grid.add(input3,	2, 1);
			
            change.setOnAction((ActionEvent e) -> 
            {
            	Integer j = form2.getValue();
                Integer k = form3.getValue();
                if (j==null || k==null) {return;}
                
                final int n2 = j;
                final int n3 = k;
                
	            if ((n2>=Single.minSize && n2<=Single.maxSize) 
	            &&	(n3>=Single.minSyms && n3<=Single.maxSyms))
	            {
		            Single.changeParameters(1, n2, n3);
		            complete.showAndWait();
		            if (this.prev instanceof MenuSingle)	{((MenuSingle)this.prev).setParamText();}
		            callPrev();
	            }
	            else {wrong.showAndWait();}
            });
		}
	}
	
	//Layout for changing the parameters of the multi-way slot machine.
	public class ParamMulti extends Parameters
	{
		public ParamMulti(String title, Layout prev)	{super(title, prev);}
		void changeParams()
		{
			limit1 = new Label("("+Multi.minSize+" - "+Multi.maxSize+") ");
			limit2 = new Label("("+Multi.minSize+" - "+Multi.maxSize+") ");
			limit3 = new Label("("+Multi.minSyms+" - "+Multi.maxSyms+") ");
			grid.add(label1,	0, 0);
			grid.add(limit1,	1, 0);
			grid.add(input1,	2, 0);
			grid.add(label2,	0, 1);
			grid.add(limit2,	1, 1);
			grid.add(input2,	2, 1);
			grid.add(label3,	0, 2);
			grid.add(limit3,	1, 2);
			grid.add(input3,	2, 2);
			
            change.setOnAction((ActionEvent e) -> 
            {
            	Integer i = form1.getValue();
                Integer j = form2.getValue();
                Integer k = form3.getValue();
                if (i==null || j==null || k==null) {return;}
                
                final int n1 = i;
                final int n2 = j;
                final int n3 = k;
                
	            if ((n1>=Multi.minSize && n1<=Multi.maxSize) 
	            &&	(n2>=Multi.minSize && n2<=Multi.maxSize)
	            &&	(n3>=Multi.minSyms && n3<=Multi.maxSyms))
	            {
		            Multi.changeParameters(n1, n2, n3);
		            complete.showAndWait();
		            if (this.prev instanceof MenuMulti)	{((MenuMulti)this.prev).setParamText();}
		            callPrev();
	            }
	            else {wrong.showAndWait();}
            });
		}
	}
	
	
	
	
	
	
	//********** Slot machine UI nodes **********
	
	//Layout for representing the machines and their spinning reels
	public abstract class Machine extends Layout
	{
		protected int reelsShown;	//Amount of reels of the machine that are being shown.
		protected Button spin, rtn;	//Buttons to spin the reels and return to the machine menu.
		protected Label games;		//Label that displays the game being played.
		
		public Machine(String title, Layout prev)	{super(title, prev);}
		abstract void GameStart();			//Actions of the Game() method before the display.
		abstract void GameComplete();		//Actions of the Game() method after the display.
		abstract void resetMach();			//Actions to reset the machine object's values.
		abstract void generateValue();		//Calls the machine obejct's method to generate value.
		abstract void checkResults();		//Calls the machine obejct's method to check the results.
		abstract void betIncrease(double b);//Calls the machine obejct's method to increase the bet.
		abstract Text calculatePrize();		//Calls the machine obejct's method to calculate the prize,
											//  and returns the output.
		abstract Text endMessage();			//Calls the machine obejct's method to show the end message,
											//  and returns the output.
		abstract int getRows();				//Returns the machine obejct's number of rows.
		abstract int getReels();			//Returns the machine obejct's number of reels or columns.
		abstract int getGames();			//Returns the number of games that have been played.
		abstract char getSym(int r, int c);	//Returns a character in the machine's results array.
		abstract boolean gameEnter();		//Returns the gameEnter value from the machine object.
		
		void createGUI()
		{
			reelsShown = 0;
			spin = new Button("Start game");
			rtn = new Button("Return to the previous menu");
			games = new Label();
			games.setAlignment(Pos.CENTER);
			
			setSpacing(10);
			getChildren().addAll(games, grid, spin, rtn);
			
			//Creating the alerts.
			Alert gameLimit = new Alert(AlertType.INFORMATION);
			gameLimit.getDialogPane().setMinWidth(alertWidth);
			gameLimit.getDialogPane().setMinHeight(alertHeight/2);
			gameLimit.getDialogPane().setContent
				(new Text("You have reached the limit of games.\nYou will return to the menu."));
			
			Alert winLimit = new Alert(AlertType.INFORMATION);
			winLimit.getDialogPane().setMinWidth(alertWidth);
			winLimit.getDialogPane().setMinHeight(alertHeight/2);
			gameLimit.getDialogPane().setContent
				(new Text("You have exceeded the bet amount allowed.\nYou will return to the menu."));
			
			Alert endMessage = new Alert(AlertType.CONFIRMATION);
			endMessage.getDialogPane().setMinWidth(alertWidth);
			endMessage.getDialogPane().setMinHeight(alertHeight/2);
			
			Alert results = new Alert(AlertType.CONFIRMATION);
			results.getDialogPane().setMinWidth(alertWidth);
			results.getDialogPane().setMinHeight(alertHeight);
			
			
			//Creating the grid containing an input that can increase the current bet.
			GridPane increase = new GridPane();
			TextField betInc = new TextField();
			TextFormatter<Integer> form = new TextFormatter<Integer>(new IntegerStringConverter());
			betInc.setTextFormatter(form);
			increase.add(new Label("Do you want to bet more money?: "), 0, 1);
			increase.add(betInc, 1, 1);
			increase.add(new Label("The amount you enter will be added to your current bet."), 0, 2);
			
			rtn.setOnAction((ActionEvent e) -> {closeLayout();});
			spin.setOnAction((ActionEvent e) -> 
			{
				if (reelsShown==0)
				{
					GameStart();
					createGrid();
				}
				else if (reelsShown<=getReels())
				{
					grid.getChildren().get(reelsShown-1).setVisible(true);
					reelsShown++;
					if (reelsShown>getReels()) {spin.setText("Get the results");}
				}
				else
				{
					GameComplete();
					Text result = calculatePrize();
					Label again = new Label("Do you want to play again? ");
					VBox content;
					
					if (gameEnter())	{content = new VBox(result, increase, again);}
					else				{content = new VBox(result);}
					
					content.setSpacing(25);
					results.getDialogPane().setContent(content);
					
					if (getGames()>=SlotMachine.GAMELIMIT)
					{
						gameLimit.showAndWait();
						closeLayout();
					}
					else if (getGames()>=SlotMachine.GAMELIMIT)
					{
						winLimit.showAndWait();
						closeLayout();
					}
					else
					{
						Optional<ButtonType> Result = results.showAndWait();
						if (Result.get()==ButtonType.OK)
						{
							if (gameEnter())
							{
								if (form.getValue()!=null)	{betIncrease( form.getValue() );}
								GameStart();
								createGrid();
							}
							else {closeLayout();}
						}
						else if (Result.get()==ButtonType.CANCEL)
						{
							endMessage.getDialogPane().setContent(endMessage());
							endMessage.showAndWait();
							closeLayout();
						}
					}
				}
			});
		}
		void createGrid()	//Establishes the parameters and creates the reels to spin.
		{
			reelsShown = 1;
			games.setText(getGames()+"º game");
			
			//Make sure the grid is empty.
			if (!grid.getChildren().isEmpty())
				{grid.getChildren().clear();}
			//Adds the CSS file to this Java class.
			grid.getStylesheets().add(SlotNodes_1.class.getResource("style.css").toString());
			
			for (int j=0; j<getReels(); j++)
			{
				VBox reel = new VBox();
				reel.getStyleClass().add("reel-column-data");	//Applies the CSS class to the object.
				for (int i=0; i<getRows(); i++)
				{
					TextArea box = new TextArea( String.valueOf(getSym(i,j)) );
					box.getStyleClass().add("reel-box-data");	//Applies the CSS class to the object.
					box.setEditable(false);
					box.setMaxSize(30, 30);
					reel.getChildren().add(box);
				}
				reel.setVisible(false);
				grid.add(reel, j, 0);
			}
			
			reelsShown = 1;
			spin.setText("Spin the reels");
		}
		void closeLayout()	//Resets the parameters and closes the machine layout.
		{
			grid.getChildren().clear();
			reelsShown = 0;
			resetMach();
			callPrev();
		}
		
	}
	
	//Layout for the playing on the single-row slot machine.
	public class MachineSingle extends Machine
	{
		public MachineSingle(String title, Layout prev)	{super(title, prev);}
		
		void generateValue()		{Single.generateValue();}
		void checkResults()			{Single.checkResults();}
		void betIncrease(double b)	{Single.P.betIncrease(b);}
		Text calculatePrize()		{return new Results1_T();}
		Text endMessage()			{return new EndMssg1_T();}
		int getRows()				{return Single.getRows();}
		int getReels()				{return Single.getReels();}
		int getGames()				{return Single.getGames();}
		char getSym(int r, int c)	{return Single.arrResults[r][c];}
		boolean gameEnter()			{return Single.gameEnter();}
		
		void GameStart()
		{
			Single.P.numGames++;
			Single.P.bet -= Single.getCost();
			Single.gameEnter = true;
			Single.rerolled = false;
			Single.generateValue();
		}
		void GameComplete()
		{
			Single.checkResults();
			Single.saveResults();
		}
		void resetMach()
		{
			Single.P.bet = 0;
			Single.P.spent = 0;
			Single.P.numGames = 0;
		}
		
	}
	
	//Layout for the playing on the multi-way slot machine.
	public class MachineMulti extends Machine
	{
		public MachineMulti(String title, Layout prev)	{super(title, prev);}
		
		void generateValue()		{Multi.generateValue();}
		void checkResults()			{Multi.checkResults();}
		void betIncrease(double b)	{Multi.P.betIncrease(b);}
		Text calculatePrize()		{return new Results2_T();}
		Text endMessage()			{return new EndMssg2_T();}
		int getRows()				{return Multi.getRows();}
		int getReels()				{return Multi.getReels();}
		int getGames()				{return Multi.getGames();}
		char getSym(int r, int c)	{return Multi.arrResults[r][c];}
		boolean gameEnter()			{return Multi.gameEnter();}
		
		void GameStart()
		{
			Multi.P.numGames++;
			Multi.P.bet -= Single.getCost();
			Multi.gameEnter = true;
			Multi.rerolled = false;
			Multi.generateValue();
		}
		void GameComplete()
		{
			Multi.checkResults();
			Multi.saveResults();
		}
		void resetMach()
		{
			Multi.P.bet = 0;
			Multi.P.spent = 0;
			Multi.P.numGames = 0;
		}
		
	}
	
	
	
	
	
	
	//********** Information display UI nodes **********
	
	//Font sizes and styles for the different information displays.
	public static int		textFontSize = 15,		alertFontSize = 12,		layoutFontSize = 11;;
	public static String	textFont = "Calibri",	alertFont = "Calibri",	layoutFont = "Courier New";
	
	//Class that converts the console output of an external method into a Text object.
	public abstract class MethodText extends Text
	{
		abstract void toDisplay();
		public MethodText()	{this(textFont, textFontSize);}
		public MethodText(String ffont, int size)
		{
			super();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PrintStream appPrint = new PrintStream(outStream);
			PrintStream consolePrint = System.out;
			System.setOut(appPrint);
			toDisplay();
			System.out.flush();
			System.setOut(consolePrint);
			
			this.setFont(Font.font(ffont, size));
			this.setText(outStream.toString());
		}
	}
	
	//Class that formats a MethodText class as an alert. Contains a Display subclass.
	public abstract class MethodAlert extends Alert
	{
		abstract void textToDisplay();
		public class Text1 extends MethodText
		{
			public Text1()	{super(alertFont, alertFontSize);}
			void toDisplay()	{textToDisplay();}
		}
		public MethodAlert(String title, String subtitle, AlertType type)
		{
			super(type);
			this.setTitle(title);
			this.setHeaderText("");
			this.getDialogPane().setMinHeight(alertHeight);
			this.getDialogPane().setMinWidth(alertWidth);
			this.getDialogPane().setContent(new VBox(
					new Label(subtitle), new Text1() ));
		}
	}
	
	//Class that formats a MethodText class as a Layout. Contains a Display subclass.
	public abstract class MethodLayout extends Layout
	{
		public MethodLayout(String title, Layout prev)	{super(title, prev);}
		abstract void textToDisplay();
		public class Text1 extends MethodText
		{
			public Text1()	{super(layoutFont, layoutFontSize);}
			void toDisplay()	{textToDisplay();}
		}
		public void createGUI()
		{
			Text text = new Text1();
			Button rtn = new Button("Return to the main menu");
			ScrollPane content = new ScrollPane();
			
			content.setContent(text);
			content.setMaxWidth(textWidth);
			content.setMaxHeight(textHeight);
			
			rtn.setOnAction((ActionEvent event) -> {callPrev();});
			
			setSpacing(10);
			getChildren().addAll(content, rtn);
		}
	}
	
	
	//Methods to texts.
	public class Results1_T	extends MethodText
	{
		public Results1_T()	{super();}
		void toDisplay()	{Single.calculatePrize();}
	}
	public class Results2_T	extends MethodText
	{
		public Results2_T()	{super();}
		void toDisplay()	{Multi.calculatePrize();}
	}
	public class EndMssg1_T	extends MethodText
	{
		public EndMssg1_T()	{super();}
		void toDisplay()	{Single.P.endMessage();}
	}
	public class EndMssg2_T	extends MethodText
	{
		public EndMssg2_T()	{super();}
		void toDisplay()	{Multi.P.endMessage();}
	}
	
	//Methods to alerts.
	public class Differ_Al	extends MethodAlert
	{
		void textToDisplay()	{SlotMachine.showDiff();}
		public Differ_Al()
		{
			super("Differences", "These are the differences between the two machine types.", 
			AlertType.INFORMATION);
		}
	}
	public class Rules1_Al	extends MethodAlert
	{
		void textToDisplay()	{Single.showRules();}
		public Rules1_Al()
		{
			super("Show rules", "These are the rules of the Single-row Slot Machine.",
			AlertType.INFORMATION);
		}
	}
	public class Rules2_Al	extends MethodAlert
	{
		void textToDisplay()	{Multi.showRules();}
		public Rules2_Al()
		{
			super("Show Rules", "These are the rules of the Multiway Slot Machine.",
			AlertType.INFORMATION);
		}
	}
	
	//Methods to Layouts.
	public class DB1_Lay	extends MethodLayout
	{
		void textToDisplay()	{SlotMachine.displayDB(false);}
		public DB1_Lay(String title, Layout prev)	{super(title, prev);}
	}
	public class DB2_Lay	extends MethodLayout
	{
		void textToDisplay()	{SlotMachine.displayDB(true);}
		public DB2_Lay(String title, Layout prev)	{super(title, prev);}
	}
	
	
	
	
    
	
}