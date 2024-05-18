import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

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
 * - Implement results
 * - Implement re-roll 
 * - SlotMachine improvements
 * 
 * */




public class SlotNodes extends Application
{
	public static void main(String[] args) {launch(args);}
	
	@Override
    public void start(Stage primaryStage)
    {
		try {
			Single	= new SlotMachine.SingleRow();
			Multi	= new SlotMachine.Multiway();
			
			
	        MainMenu main		= new MainMenu("----- MAIN MENU -----\n\n\n\n");
			MenuSingle menu1	= new MenuSingle("-- SINGLE-ROW MENU --\n", main);
			MenuMulti menu2		= new MenuMulti("-- MULTIWAY MENU --\n", main);
			Machine1 SingleLay	= new Machine1("Play the game", menu1);
			Machine2 MultiLay	= new Machine2("Play the game", menu2);
			
			DB1_Lay DBText		= new DB1_Lay("Display database\nas plain text", main);
			DB2_Lay DBScript	= new DB2_Lay("Display database\nwith script", main);
			ParamSingle param1	= new ParamSingle("Change the parameters\n of the machine", menu1);
			ParamMulti param2	= new ParamMulti("Change the parameters\n of the machine", menu2);
			
			main.addNext(menu1, menu2, DBText, DBScript);
			menu1.addNext(SingleLay, param1);
			menu2.addNext(MultiLay, param2);
	        
	        
			primaryStage.setTitle("Welcome to the Casino!");
	        primaryStage.setScene(new Scene(menu2, sceneWidth, sceneHeight));
	        primaryStage.show();
    	}
        catch (Exception e) {e.printStackTrace();}
    }
	
	
	
	
	public static SlotMachine.SingleRow Single;
	public static SlotMachine.Multiway Multi;
	public static String playername;
	
	public static int sceneWidth=700,	sceneHeight=500;
	public static int textWidth=600,	textHeight=350;
	public static int alertWidth=400,	alertHeight=200;
	public static int buttonWidth=130,	buttonHeight=80;
	public static int alertFont=14;
	public static int layoutFont=11;
	
	
	
	
	
	
	//Class that allows for changing between "previous" and "next" scene roots.
	private abstract class Layout extends VBox
    {
		private Layout prev;
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
		Alert openMach, wrong;
		TextField inputName, inputBet;
		
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
            Alert openMachine	= new Alert(AlertType.CONFIRMATION);
            TextFormatter<Integer> form = new TextFormatter<Integer>(new IntegerStringConverter());
            GridPane gridpane	= new GridPane();
            TextField inputName	= new TextField();
            TextField inputBet	= new TextField();
        	Label labelName		= new Label("What is your name?: ");
        	Label labelBet		= new Label("How much do you want to bet?    "
        						+ "\n("+SlotMachine.BETMIN+" - "+SlotMachine.BETMAX+"): ");
        	gridpane.add(labelName,	0, 0);
        	gridpane.add(inputName,	1, 0);
        	gridpane.add(labelBet,	0, 1);
        	gridpane.add(inputBet,	1, 1);
        	gridpane.setAlignment(Pos.CENTER);
        	gridpane.setHgap(10);
        	gridpane.setVgap(10);
        	inputBet.setTextFormatter(form);
        	openMachine.setTitle("Enter your name and bet"); 
        	openMachine.setHeaderText("");
        	openMachine.getDialogPane().setMinWidth(alertWidth);
	    	openMachine.getDialogPane().setMinHeight(alertHeight);
	    	openMachine.getDialogPane().setContent(gridpane);
	    	
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
				if (openMachine.showAndWait().get()==ButtonType.OK)
				{
					if (!inputName.getText().isEmpty()
						&& form.getValue()>=SlotMachine.BETMIN
						&& form.getValue()<=SlotMachine.BETMAX)
						{callNext(0);}
					else 
						{wrong.showAndWait();}
				}
			});
			btn2.setOnAction((ActionEvent event) -> 
			{
				if (openMachine.showAndWait().get()==ButtonType.OK)
				{
					if (!inputName.getText().isEmpty()
						&& form.getValue()>=SlotMachine.BETMIN
						&& form.getValue()<=SlotMachine.BETMAX)
						{callNext(1);}
					else 
						{wrong.showAndWait();}
				}
			});
		}
		
	}
	
	
	
	
	
	
	//Layout containing the sub-menus for each machine type.
	public abstract class SubMenu extends Layout
	{
		Text params;
		
		public SubMenu(String title, Layout prev)	{super(title, prev);}
		abstract void showRules();
		void createGUI()
		{
			params = new Text();
			Button btn1 = new Button("Play the\nSlot Machine");
			Button btn2 = new Button("Change\nParameters");
			Button btn3 = new Button("Show the\nrules");
			Button btn4 = new Button("Return to the\nmain menu");
			
			btn1.setOnAction((ActionEvent event) -> {callNext(0);});
			btn2.setOnAction((ActionEvent event) -> {callNext(1);});
			btn3.setOnAction((ActionEvent event) -> {showRules();});
			btn4.setOnAction((ActionEvent event) -> {callPrev();});
			createButtonMenu(grid, btn1, btn2, btn3, btn4);
			getChildren().addAll(params, grid);
		}
	}
	
	//Sub-menu for the single-row slot machine.
	public class MenuSingle extends SubMenu
	{
		public MenuSingle(String title, Layout prev)
		{
			super(title, prev);
			params.setText("This machine has "+Single.getReels()+" reels and "
				+ Single.getSyms()+" symbols.\n and costs "+Single.getCost()+" € to spin per turn.\n");
		}
		void showRules()	{new Rules1_Al().showAndWait();}
	}
	
	//Sub-menu for the multi-row slot machine.
	public class MenuMulti extends SubMenu
	{
		public MenuMulti(String title, Layout prev)
		{
			super(title, prev);
			params.setText("This machine has "+Multi.getRows()+" rows, "+Multi.getReels()+" reels and "
				+ Multi.getSyms()+" symbols.\n and costs "+Multi.getCost()+" € to spin per turn.\n");
		}
		void showRules()	{new Rules2_Al().showAndWait();}
	}
	
	
	
	
	
	
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
		            //System.out.println("Ro: "+n1+", Re: "+n2+", Syms:"+n3);
		            complete.showAndWait();
		            callPrev();
	            }
	            else {wrong.showAndWait();}
            });
		}
	}
	
	
	
	
	
	
	//Layout for representing the machines and their spinning reels
	public abstract class Machine extends Layout
	{
		protected int reelsShown;
		
		public Machine(String title, Layout prev)	{super(title, prev);}
		abstract void generateValue();		//Generates the value of the machine.
		abstract void displayReel();		//Displays the number of columns equal to "reelsShown".
		abstract char getSym(int r, int c);	//Returns a character in the machine's results array.
		abstract int getRows();				//Returns the number of rows of the machine.
		abstract int getReels();			//Returns the number of columns of the machine. 
		
		void createGUI()
		{
			Button spin = new Button("Start game");
			Button rtn = new Button("Return to the previous menu");
			Alert results = new Alert(AlertType.CONFIRMATION);
			
			reelsShown = 0;
			setSpacing(10);
			getChildren().addAll(grid, spin, rtn);
			
			spin.setOnAction((ActionEvent e) -> 
			{
				System.out.println(reelsShown);
				if (reelsShown==0)
				{
					createGrid();
					reelsShown++;
					spin.setText("Spin the reels");
				}
				else if (reelsShown<=getReels())
				{
					displayReel();
					reelsShown++;
					if (reelsShown>getReels()) {spin.setText("Get the results");}
				}
				else
				{
					Single.checkResults();
					if (results.showAndWait().get()==ButtonType.OK)
					{
						createGrid();
						reelsShown = 1;
						spin.setText("Spin the reels");
					}
					else
					{
						grid.getChildren().clear();
						reelsShown = 0;
						callPrev();
					}
				}
			});
			rtn.setOnAction((ActionEvent e) -> 
			{
				grid.getChildren().clear();
				reelsShown = 0;
				callPrev();
			});
		}
		void createGrid()			//Adds the results of spinning the machine to the grid.
		{
			if (!grid.getChildren().isEmpty())
				{grid.getChildren().clear();}
			generateValue();
			for (int i=0; i<getRows(); i++)
			{
				for (int j=0; j<getReels(); j++)
				{
					TextArea ta = new TextArea();
					ta.setText( String.valueOf(Multi.arrResults[i][j]) );
					ta.setMaxSize(40, 40);
					ta.setEditable(false);
					ta.setVisible(false);
					grid.add(ta, j, i);
				}
			}
		}
		
	}
	
	//Layout for the playing on the single-row slot machine.
	public class Machine1 extends Machine
	{
		public Machine1(String title, Layout prev)	{super(title, prev);}
		void generateValue()		{Single.generateValue();}
		char getSym(int r, int c)	{return Single.arrResults[r][c];}
		int getRows()				{return Single.getRows();}
		int getReels()				{return Single.getReels();}
		
		void displayReel()
		{
			grid.getChildren().get(reelsShown-1).setVisible(true);
		}
		
	}
	
	//Layout for the playing on the multi-way slot machine.
	public class Machine2 extends Machine
	{
		public Machine2(String title, Layout prev)	{super(title, prev);}
		void generateValue()		{Multi.generateValue();}
		char getSym(int r, int c)	{return Multi.arrResults[r][c];}
		int getRows()				{return Multi.getRows();}
		int getReels()				{return Multi.getReels();}
		
		void displayReel()
		{
			for (int i=0; i<Multi.getRows(); i++)
				{grid.getChildren().get(reelsShown-1 + Multi.getReels()*i).setVisible(true);}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	//Class that converts and external method into a Text object.
	public abstract class Display
	{
		Text text;
		abstract void setDisplay();
		public Text getText()		{return text;}
		public Display(int size)
		{
			text = new Text();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PrintStream appPrint = new PrintStream(outStream);
			PrintStream consolePrint = System.out;
			System.setOut(appPrint);
			setDisplay();
			System.out.flush();
			System.setOut(consolePrint);
			
			text.setFont(new Font("Courier New", size));
			text.setText(outStream.toString());
		}
	}
	
	//Class that formats a Display class Text as an alert. Contains a Display subclass.
	public abstract class DisplayAlert
	{
		Alert alert;
		public void showAndWait() {alert.showAndWait();} 
		abstract void textToDisplay();
		public class Display1 extends Display
		{
			public Display1(int size)	{super(size);}
			void setDisplay() {textToDisplay();}
		}
		public DisplayAlert(String title, String subtitle)
		{
			Text text = new Display1(alertFont).getText();
			
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText("");
			alert.getDialogPane().setMinHeight(alertHeight);
			alert.getDialogPane().setMinWidth(alertWidth);
			
			alert.getDialogPane().setContent(new VBox(new Label(subtitle), text));
		}
	}
	
	//Class that formats a Display class Text as a Layout. Contains a Display subclass.
	public abstract class DisplayLayout extends Layout
	{
		public DisplayLayout(String title, Layout prev)	{super(title, prev);}
		abstract void textToDisplay();
		public class Display1 extends Display
		{
			public Display1(int size)	{super(size);}
			void setDisplay() {textToDisplay();}
		}
		public void createGUI()
		{
			Text text = new Display1(layoutFont).getText();
			Button rtn = new Button("Return to the main menu");
			ScrollPane content = new ScrollPane();
			
			content.setContent(text);
			content.setFitToWidth(false);
			content.setFitToHeight(false);
			content.setPrefViewportWidth(textWidth);
			content.setPrefViewportHeight(textHeight);
			
			rtn.setOnAction((ActionEvent event) -> {callPrev();});
			
			setSpacing(10);
			getChildren().addAll(content, rtn);
		}
	}
	
	
	//Methods to Text to alerts.
	public class Differ_Al	extends DisplayAlert
	{
		void textToDisplay()	{SlotMachine.showDiff();}
		public Differ_Al()
			{super("Differences","These are the differences between the two machine types.");}
	}
	public class Rules1_Al	extends DisplayAlert
	{
		void textToDisplay(){Single.showRules();}
		public Rules1_Al()	{super("Show rules","These are the rules of the Single-row Slot Machine.");}
	}
	public class Rules2_Al	extends DisplayAlert
	{
		void textToDisplay(){Multi.showRules();}
		public Rules2_Al()	{super("Show Rules", "These are the rules of the Multiway Slot Machine.");}
	}
	public class Prize1_Al	extends DisplayAlert
	{
		void textToDisplay()	{Single.calculatePrize();}
		public Prize1_Al()		{super("Show Results","This is your prize after spinning the reels.");}
	}
	public class Prize2_Al	extends DisplayAlert
	{
		void textToDisplay()	{Multi.calculatePrize();}
		public Prize2_Al()		{super("Show Results","This is your prize after spinning the reels.");}
	}
	
	//Methods to Text to Layouts.
	public class DB1_Lay	extends DisplayLayout
	{
		void textToDisplay()	{SlotMachine.displayDB(false);}
		public DB1_Lay(String title, SlotNodes.Layout prev)	{super(title, prev);}
	}
	public class DB2_Lay	extends DisplayLayout
	{
		void textToDisplay()	{SlotMachine.displayDB(true);}
		public DB2_Lay(String title, SlotNodes.Layout prev)	{super(title, prev);}
	}
	
	
	
	
    
	
}