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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;





/* TODO:
 * - Fix database display
 * - Fix info display
 * 
 * - Proper machine sub-type parsing (single-row vs multi-way)
 * - Override readInput<T> with pop-up TextField
 * - Implement Parameter change
 * - Implement Reel and spinning GUI
 * - Implement results GUI
 * 
 * */




public class SlotNodes extends Application
{
	public static SlotMachine.SingleRow Single;
	public static SlotMachine.Multiway Multi;
	public String playername;
	
	
	private abstract class Layout extends VBox
    {
		private Layout prev;
    	protected ArrayList<Layout> Next;
    	protected Label title;
    	protected GridPane grid;
    	
    	public Layout(String titletext, Layout prevNode)
        {
    		this.Next = new ArrayList<Layout>();
    		this.prev = prevNode;
    		
    		this.title = new Label(titletext);
    		this.grid = new GridPane();
    		this.title.setAlignment(Pos.CENTER);
    		this.grid.setAlignment(Pos.CENTER);
            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(title);
            createGUI();
            this.getChildren().addAll(grid);
        }
    	
        abstract void createGUI();
        
        protected void addNext(Layout... next)	{for (Layout nd : next) {Next.add(nd);}}
        protected void callNext(int i)			{getScene().setRoot(Next.get(i));}
        protected void callPrev()				{getScene().setRoot(prev);}
        
    }
	
	
	
	
	//Layout containing the main navigation menu.
	public class MainMenu extends Layout
	{
		public MainMenu(String title)	{super(title, null);}
		public void createGUI()
		{
			//Button btn5 = new Button("") {@Override public void fire() {callNext(4);}};
			
			Button btn1 = new Button("Single-row\nSlot Machine");
			Button btn2 = new Button("Multi-way\nSlot Machine");
			Button btn3 = new Button("Display Database\nas plain text");
			Button btn4 = new Button("Display Database\nwith script");
			Button btn5 = new Button("Show differences");
			Button btn6 = new Button("Exit");
			
			createButtonMenu(grid, btn1, btn3, btn5, btn2, btn4, btn6);
			btn1.setOnAction((ActionEvent event) -> {openMachine(0);});
			btn2.setOnAction((ActionEvent event) -> {openMachine(3);});
			btn3.setOnAction((ActionEvent event) -> {callNext(1);});
			btn4.setOnAction((ActionEvent event) -> {callNext(4);});
			btn5.setOnAction((ActionEvent event) -> {callNext(2);});
			btn6.setOnAction((ActionEvent event) -> {logout();});
		}
        public void logout()
        {
        	//Alert alert = new Alert(AlertType.CONFIRMATION);
        	//alert.setTitle("Logout");
        	//alert.setHeaderText("You are about to logout.");
        	//alert.setContentText("Are you sure you want\nto exit the program?");
        	
        	//if (alert.showAndWait().get()==ButtonType.OK)
        		{Platform.exit();}
        }
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
        		System.out.println(" - "+playername+" | "+result.get());
        	    callNext(i);
        	}
        }
	}
	
	//Layout containing the sub-menus for each machine type.
	public abstract class SubMenu extends Layout
	{
		public SubMenu(String title, Layout prev)	{super(title, prev);}
		abstract void parameters();
		abstract void rules();
		void createGUI()
		{
			Label name = new Label("What do you want to do, "+playername);
			Button btn1 = new Button("Play Slot Machine");
			Button btn2 = new Button("Change Parameters");
			Button btn3 = new Button("Show Rules");
			Button btn4 = new Button("Return to the\n main menu");
			
			createButtonMenu(grid, btn1, btn2, btn3, btn4);
			btn1.setOnAction((ActionEvent event) -> {callNext(0);});
			btn2.setOnAction((ActionEvent event) -> {parameters();});
			btn3.setOnAction((ActionEvent event) -> {rules();});
			btn4.setOnAction((ActionEvent event) -> {callPrev();});
			
			getChildren().add(name);
		}
	}
	
	public class SingleSubMenu extends SubMenu
	{
		public SingleSubMenu(String title, Layout prev)
		{
			super(title, prev);
			Single = new SlotMachine.SingleRow(playername);
		}
		void rules()
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Slot machine rules");
			alert.setHeaderText("");
			
			VBox content = new VBox();
			Label label = new Label("Rules:");
			TextArea ta = new DisplayRules1().getText();
			content.getChildren().addAll(label, ta);
			
			alert.getDialogPane().setContent(content);
			alert.showAndWait();
		}
		void parameters()
		{
			TextInputDialog dialog = new TextInputDialog(SlotMachine.def_name);
        	dialog.setTitle("Change the parameters");
        	dialog.setHeaderText("Enter new parameters for the machine");
        	dialog.setContentText("Reels:");
        	
        	//if (Integer.parseInt(reels) )
		}
		
	}
	
	public class MultiSubMenu extends SubMenu
	{
		public MultiSubMenu(String title, Layout prev)
		{
			super(title, prev);
			Multi = new SlotMachine.Multiway(playername);
		}
		void rules()
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Slot machine rules");
			alert.setHeaderText("");
			
			VBox content = new VBox();
			Label label = new Label("Rules:");
			TextArea ta = new DisplayRules2().getText();
			content.getChildren().addAll(label, ta);
			
			alert.getDialogPane().setContent(content);
			alert.showAndWait();
		}
		void parameters()
		{
			TextInputDialog dialog = new TextInputDialog(SlotMachine.def_name);
        	dialog.setTitle("Change the parameters");
        	dialog.setHeaderText("Enter new parameters for the machine");
        	dialog.setContentText("Rows:");
        	
        	//if (Integer.parseInt(reels) )
		}
	}
	
	
	//Applies the desired settings to the buttons and adds them to the pane.
	public void createButtonMenu(GridPane grid, Button... args)
    {
		int buttonWidth=150, buttonHeight=80;
    	int maxCols, col=0, row=0;
    	
    	if (args.length%3==0)		{maxCols = 3;}
    	else if (args.length%2==0)	{maxCols = 2;}
    	else						{maxCols = 1;}
    	
    	for (Button btn : args)
    	{
    		//Makes all the buttons look the same.
    		btn.setMinSize(buttonWidth, buttonHeight);
    		btn.fontProperty();
    		
    		//Adds the button to the desired position in the gridPane
    		grid.add(btn, col, row);
    		// and prepares the position of the next.
    		col += 1;
    		if (col==maxCols)
    		{
    			col = 0;
    			row += 1;
    		}
    		//Due to the parameters we have to use this instead of a loop.
    	}
    }
	
	
	
	
	//Area containing the console output of an external method.
	public abstract class Display extends VBox
	{
		TextArea tArea;
		abstract void methodToDisplay();
		public TextArea getText()	{return tArea;}
		public Display()
		{
			tArea = new TextArea();
			tArea.setCenterShape(true);
			tArea.setWrapText(false);
			tArea.setEditable(false);
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PrintStream textareaPrint = new PrintStream(outStream);
			PrintStream consolePrint = System.out;
			
			System.setOut(textareaPrint);
			methodToDisplay();
			System.out.flush();
			System.setOut(consolePrint);
			
			tArea.setFont(new Font("Courier New", 10));
			tArea.appendText(outStream.toString());
		}
	}
	
	//Area containing a Display class presented as a scene root. Requires a previous Display class.
	public abstract class DisplayLayout extends Layout
	{
		public DisplayLayout(String title, Layout prev)	{super(title, prev);}
		abstract TextArea areaToDisplay();
		public void createGUI()
		{
			Button rtn = new Button("Return to the main menu");
			TextArea tArea = areaToDisplay();
			rtn.setOnAction((ActionEvent event) -> {callPrev();});
			getChildren().addAll(tArea, rtn);
		}
	}
	
	public class DisplayDBT extends Display		{void methodToDisplay()	{SlotMachine.displayDB(false);}}
	public class DisplayDBS extends Display		{void methodToDisplay()	{SlotMachine.displayDB(true);}}
	public class DisplayRules1 extends Display	{void methodToDisplay()	{Single.showRules();}}
	public class DisplayRules2 extends Display	{void methodToDisplay()	{Multi.showRules();}}

	public class DBTLayout extends DisplayLayout
	{
		public DBTLayout(String title, SlotNodes.Layout prev)	{super(title, prev);}
		TextArea areaToDisplay()	{return new DisplayDBT().getText();}
	}
	public class DBSLayout extends DisplayLayout
	{
		public DBSLayout(String title, SlotNodes.Layout prev)	{super(title, prev);}
		TextArea areaToDisplay()	{return new DisplayDBS().getText();}
	}
	
	
	
	
	//Layout for displaying the machine's parameters and allowing to change them.
	public class Parameters extends Layout
	{
		protected SlotMachine.Machine M;	//The Machine this class works with.
		
		public Parameters(String title, SlotMachine.Machine ma, Layout prev)
		{
			super(title, prev);
			this.M = ma;
		}
		void createGUI()
		{
			GridPane root = new GridPane();
			root.setAlignment(Pos.CENTER);
			
			if (M instanceof SlotMachine.Multiway)
			{
				root.add(new Label("Rows"),	0, 0);
				root.add(new TextField(),	1, 0);
			}
			root.add(new Label("Reels"),	0, 1);
			root.add(new TextField(),		1, 1);
			root.add(new Label("Symbols"),	0, 2);
			root.add(new TextField(),		1, 2);
			
			Button btn = new Button("Return to the previous menu");
			btn.setOnAction((ActionEvent e) -> {callPrev();});
			
			getChildren().addAll(root, btn);
		}
	}
	
	
	
	
	
	
	
	
	
	
	@Override
    public void start(Stage primaryStage) throws Exception
    {
		try {
	        MainMenu main		= new MainMenu("--- MAIN MENU ---\n\n\n");
			SingleSubMenu menu1	= new SingleSubMenu("Single-row Slot Machine\n", main);
			MultiSubMenu menu2	= new MultiSubMenu("Multi-way Slot Machine\n", main);
			//MachReels SingleR	= new MachReels("Play the game", Single, menu1);
			//MachReels MultiwR	= new MachReels("Play the game", Multi, menu2);
			
			Parameters param1	= new Parameters("Change the\nparameters", Single, menu1);
			Parameters param2	= new Parameters("Change the\nparameters", Multi, menu2);
			DBTLayout DBText	= new DBTLayout("Display Database\nas plain text", main);
			DBSLayout DBScript	= new DBSLayout("Display Database\nwith script", main);
			Parameters rules1	= new Parameters("Show the rules", Single, menu1);
			Parameters rules2	= new Parameters("Show the rules", Multi, menu2);
			Parameters diff	= new Parameters("Show differences", Single, main);
			
			main.addNext(menu1, DBText, diff, menu2, DBScript);
			menu1.addNext( param1, rules1);
			menu2.addNext( param2, rules2);
	        
	        
			primaryStage.setTitle("Welcome to the Casino!");
	        primaryStage.setScene(new Scene(main, 700, 500));
	        primaryStage.show();
    	}
        catch (Exception e) {e.printStackTrace();}
    }
    
	
    public static void main(String[] args) {launch(args);}
    
    
}