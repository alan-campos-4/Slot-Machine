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
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;




/* TODO:
 * - Class Text instead of TextArea
 * - Implement Parameter change and view
 * - Implement Reel and spinning GUI
 * - Implement results GUI
 * 
 * 
 * - ¬/ Fix database display
 * - ¬/ Fix info display
 * 
 * 
 * */




public class SlotNodes extends Application
{
	public static void main(String[] args) {launch(args);}
	
	@Override
    public void start(Stage primaryStage) throws Exception
    {
		try {
			Single	= new SlotMachine.SingleRow();
			Multi	= new SlotMachine.Multiway();
			
			
	        MainMenu main		= new MainMenu("----- MAIN MENU -----\n\n\n\n");
			SingleSubMenu menu1	= new SingleSubMenu("-- SINGLE-ROW MENU --\n", main);
			MultiSubMenu menu2	= new MultiSubMenu("-- MULTIWAY MENU --\n", main);
			
			DB1Layout DBText	= new DB1Layout("Display database\nas plain text", main);
			DB2Layout DBScript	= new DB2Layout("Display database\nwith script", main);
			ParamSingle param1	= new ParamSingle("Change the parameters\n of the machine", menu1);
			ParamMulti param2	= new ParamMulti("Change the parameters\n of the machine", menu2);
			
			SingleMach	= new Machine1("Play the game", menu1);
			MultiwMach	= new Machine2("Play the game", menu2);
			
			main.addNext(menu1, menu2, DBText, DBScript);
			menu1.addNext(SingleMach, param1);
			menu2.addNext(MultiwMach, param2);
	        
	        
			primaryStage.setTitle("Welcome to the Casino!");
	        primaryStage.setScene(new Scene(main, sceneWidth, sceneHeight));
	        primaryStage.show();
    	}
        catch (Exception e) {e.printStackTrace();}
    }
    
	
	
	
	
	
	public static SlotMachine.SingleRow Single;
	public static SlotMachine.Multiway Multi;
	public static Machine1 SingleMach;
	public static Machine2 MultiwMach;
	public static String playername;
	
	public static int sceneWidth=800,	sceneHeight=600;
	public static int textWidth=600,	textHeight=350;
	public static int alertWidth=500,	alertHeight=350;
	public static int buttonWidth=130,	buttonHeight=80;
	
	
	
	
	
	
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
	public void createButtonMenu(GridPane grid, Button... args)
    {
		int maxCols=3, col=0, row=0;
    	
    	for (Button btn : args)
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
    			if (args.length==4 && row==1) {col+=1;}
    		}
    	}
    }
	
	
	//Layout containing the main navigation menu.
	public class MainMenu extends Layout
	{
		public MainMenu(String title)	{super(title, null);}
		public void createGUI()
		{
			Button btn1 = new Button("Single-row\nSlot Machine");
			Button btn2 = new Button("Multi-way\nSlot Machine");
			Button btn3 = new Button("Display database\nas plain text");
			Button btn4 = new Button("Display database\nwith script");
			Button btn5 = new Button("Show\ndifferences");
			Button btn6 = new Button("Exit");
			
			btn1.setOnAction((ActionEvent event) -> {openMachine(0);});
			btn2.setOnAction((ActionEvent event) -> {openMachine(1);});
			btn3.setOnAction((ActionEvent event) -> {callNext(2);});
			btn4.setOnAction((ActionEvent event) -> {callNext(3);});
			btn5.setOnAction((ActionEvent event) -> {showDiff();});
			btn6.setOnAction((ActionEvent event) -> {logout();});
			createButtonMenu(grid, btn1, btn2, btn5, btn3, btn4, btn6);
            getChildren().addAll(grid);
		}
		
		public void showDiff()	{new DiffAlert().getAlert();}
        public void openMachine(int i)
        {
        	TextInputDialog dialog = new TextInputDialog(SlotMachine.def_name);
        	dialog.setTitle("Enter your name");
        	dialog.setHeaderText("");
        	dialog.setContentText("What is your name?");
        	
        	Optional<String> result = dialog.showAndWait();
        	
        	if (result.isPresent() && result.get()!="")
        	{
        		playername = result.get();
        		if (i==0)	{Single.assignName(playername);	SingleMach.resetMachine(); }
        		else		{Multi.assignName(playername);	MultiwMach.resetMachine(); }
        		
        	    callNext(i);
        	}
        }
        public void logout()
        {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Logout");
        	alert.setHeaderText("You are about to logout.");
        	alert.setContentText("Are you sure you want\nto exit the program?");
        	
        	if (alert.showAndWait().get()==ButtonType.OK)	{Platform.exit();}
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
	public class SingleSubMenu extends SubMenu
	{
		public SingleSubMenu(String title, Layout prev)
		{
			super(title, prev);
			params.setText("This machine has "+Single.getReels()+" reels and "
				+ Single.getSyms()+" symbols.\n and costs "+Single.getCost()+" € to spin per turn.\n");
		}
		void showRules()	{new Rules1Alert().getAlert();}
	}
	
	//Sub-menu for the multi-row slot machine.
	public class MultiSubMenu extends SubMenu
	{
		public MultiSubMenu(String title, Layout prev)
		{
			super(title, prev);
			params.setText("This machine has "+Multi.getRows()+" rows, "+Multi.getReels()+" reels and "
				+ Multi.getSyms()+" symbols.\n and costs "+Multi.getCost()+" € to spin per turn.\n");
		}
		void showRules()	{new Rules2Alert().getAlert();}
	}
	
	
	
	
	
	
	//Layout that allows for the change of a machine's parameters.
	public abstract class Parameters extends Layout
	{
		protected TextFormatter<Integer> formatter1, formatter2, formatter3;
		protected TextField input1, input2, input3;
		protected Label label1, label2, label3, limit1, limit2, limit3;
		protected Button change, rtn;
		protected Alert complete, wrong;
		protected int minR, maxR, minS, maxS;
		
		public Parameters(String title, Layout prev, int min1, int max1, int min2, int max2)
		{
			super(title, prev);
			this.minR = min1;
			this.maxR = max1;
			this.minS = min2;
			this.maxS = max2;
		}
		abstract void changeParams();
		void createGUI()
		{
			label1 = new Label("Rows ");
			label2 = new Label("Reels ");
			label3 = new Label("Symbols ");
			input1 = new TextField();
			input2 = new TextField();
			input3 = new TextField();
			
			formatter1 = new TextFormatter<>(new IntegerStringConverter());
	        formatter2 = new TextFormatter<>(new IntegerStringConverter());
	        formatter3 = new TextFormatter<>(new IntegerStringConverter());
	        input1.setTextFormatter(formatter1);
	        input2.setTextFormatter(formatter2);
	        input3.setTextFormatter(formatter3);
			
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
		public ParamSingle(String title, Layout prev) 
			{super(title, prev, Single.minSize, Single.maxSize, Single.minSyms, Single.maxSyms);}
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
            	Integer j = formatter2.getValue();
                Integer k = formatter3.getValue();
                if (j==null || k==null) {return;}
                
                final int n2 = j;
                final int n3 = k;
                
	            if ( (n2>=minR && n2<=maxR) && (n3>=minS && n3<=maxS) )
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
		public ParamMulti(String title, Layout prev)
			{super(title, prev, Multi.minSize, Multi.maxSize, Multi.minSyms, Multi.maxSyms);}
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
            	Integer i = formatter1.getValue();
                Integer j = formatter2.getValue();
                Integer k = formatter3.getValue();
                if (i==null || j==null || k==null) {return;}
                
                final int n1 = i;
                final int n2 = j;
                final int n3 = k;
                
	            if ( (n1>=minR && n1<=maxR) && (n2>=minR && n2<=maxR) && (n3>=minS && n3<=maxS) )
	            {
		            Multi.changeParameters(n1, n2, n3);
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
		protected ArrayList<TextArea> MachineReels;
		protected Button spin, rtn;
		protected int Reels, Rows;
		protected int reelsShown;
		
		public Machine(String title, Layout prev)
		{
			super(title, prev);
			this.reelsShown = 0;
		}
		void createGUI()
		{
			MachineReels = new ArrayList<TextArea>(Reels);
			machineUI();
			
			rtn = new Button("Return to the previous menu");
			rtn.setOnAction((ActionEvent e) -> {callPrev();});
			
			setSpacing(10);
			getChildren().addAll(grid, spin, rtn);
		}
		void resetMachine()
		{
			reelsShown = 0;
			
			Single.spinReels();
			Single.P.numGames++;
			Single.P.bet -= Single.getCost();
			Single.rerolled = false;
			
			Multi.spinReels();
			Multi.P.numGames++;
			Multi.P.bet -= Single.getCost();
			Multi.rerolled = false;
		}
		abstract void machineUI();
	}
	
	//Layout for the playing on the single-row slot machine.
	public class Machine1 extends Machine
	{
		public Machine1(String title, Layout prev)
		{
			super(title, prev);
			this.Rows = 1;
			this.Reels = Single.getReels();
			this.reelsShown = 0;
		}
		void machineUI()
		{
			Single.spinReels();
			
			for (int i=0; i<Single.getReels(); i++)
			{
				TextArea ta = new TextArea();
				String add = String.valueOf(Single.arrResults[0][i]);
				ta.setMaxSize(40, 40);
				ta.setText(add);
				
				System.out.println(add);
				grid.add(ta, i, 0);
				MachineReels.add(ta);
			}
			spin = new Button("Spin the reels");
			spin.setOnAction((ActionEvent e) -> 
			{
				if (reelsShown<Single.getReels())
				{
					reelsShown++;
					System.out.println(reelsShown);
				}
			});
		}
		
	}
	
	//Layout for the playing on the multi-way slot machine.
	public class Machine2 extends Machine
	{
		public Machine2(String title, Layout prev)
		{
			super(title, prev);
			this.Rows = Multi.getRows();
			this.Reels = Multi.getReels();
			this.reelsShown = 0;
		}
		void machineUI()
		{
			Multi.spinReels();
			
			for (int i=0; i<Multi.getRows(); i++)
			{
				for (int j=0; j<Multi.getReels(); j++)
				{
					TextArea ta = new TextArea();
					String add = String.valueOf(Multi.arrResults[i][j]);
					ta.setMaxSize(40, 40);
					ta.setText(add);
					
					System.out.println(add);
					grid.add(ta, i, j);
					MachineReels.add(ta);
				}
			}
			spin = new Button("Spin the reels");
			spin.setOnAction((ActionEvent e) -> 
			{
				if (reelsShown<Single.getReels())
				{
					reelsShown++;
					System.out.println(reelsShown);
				}
			});
		}
		
	}
	
	
	
	
	
	
	//Class that converts the console output of an external method into a Text object.
	public abstract class Display
	{
		Text text;
		abstract void setDisplay();
		public Text getText()		{return text;}
		public Display()
		{
			text = new Text();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PrintStream appPrint = new PrintStream(outStream);
			PrintStream consolePrint = System.out;
			System.setOut(appPrint);
			setDisplay();
			System.out.flush();
			System.setOut(consolePrint);
			
			text.setFont(new Font("Courier New", 10));
			text.setText(outStream.toString());
		}
	}
	
	//Class that formats a Display class Text as an alert. Requires a previous Display subclass.
	public abstract class DisplayAlert
	{
		Alert alert;
		abstract Text textToDisplay();
		public void getAlert() {alert.showAndWait();} 
		public DisplayAlert(String title, String subtitle)
		{
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText("");
			alert.getDialogPane().setMinHeight(alertHeight);
			alert.getDialogPane().setMinWidth(alertWidth);
			alert.getDialogPane().setContent(new VBox(new Label(subtitle), textToDisplay()));
		}
	}

	//Class that formats a Display class Text as a Layout. Requires a previous Display subclass.
	public abstract class DisplayLayout extends Layout
	{
		public DisplayLayout(String title, Layout prev)	{super(title, prev);}
		abstract Text textToDisplay();
		public void createGUI()
		{
			Button rtn = new Button("Return to the main menu");
			ScrollPane content = new ScrollPane();
			Text text = textToDisplay();
//			text.maxWidth(textWidth);
//			text.maxHeight(textHeight);
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
	
	
	//Methods to Text.
	public class DB1Display extends Display		{void setDisplay()	{SlotMachine.displayDB(false);}}
	public class DB2Display extends Display		{void setDisplay()	{SlotMachine.displayDB(true);}}
	public class DiffDisplay extends Display	{void setDisplay()	{SlotMachine.showDiff();}}
	public class Rules1Display extends Display	{void setDisplay()	{Single.showRules();}}
	public class Rules2Display extends Display	{void setDisplay()	{Multi.showRules();}}
	
	//Text to alerts.
	public class DiffAlert extends DisplayAlert
	{
		public DiffAlert()
			{super("Show differences", "These are the differences between the machine types.");}
		Text textToDisplay()	{return new DiffDisplay().getText();}
	}
	public class Rules1Alert extends DisplayAlert
	{
		public Rules1Alert()
			{super("Show Rules", "These are the rules of the Single-row Slot Machine.");}
		Text textToDisplay()	{return new Rules1Display().getText();}
	}
	public class Rules2Alert extends DisplayAlert
	{
		public Rules2Alert()
			{super("Show Rules", "These are the rules of the Multiway Slot Machine.");}
		Text textToDisplay()	{return new Rules2Display().getText();}
	}
	
	//Text to Layouts.
	public class DB1Layout extends DisplayLayout
	{
		public DB1Layout(String title, SlotNodes.Layout prev)	{super(title, prev);}
		Text textToDisplay()	{return new DB1Display().getText();}
	}
	public class DB2Layout extends DisplayLayout
	{
		public DB2Layout(String title, SlotNodes.Layout prev)	{super(title, prev);}
		Text textToDisplay()	{return new DB2Display().getText();}
	}
	
	
	
	
	
	
	
	
    
    
}