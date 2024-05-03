import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
	static SlotMachine.SingleRow Single;
	static SlotMachine.Multiway Multi;
	
	
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
            this.getChildren().addAll(title, grid);
            createGUI();
        }
    	
        abstract void createGUI();
        
        protected void addNext(Layout... next)	{for (Layout nd : next) {Next.add(nd);}}
        protected void callNext(int i)			{getScene().setRoot(Next.get(i));}
        protected void callPrev()				{getScene().setRoot(prev);}
        
        //protected void readInput()        
    }
	
	
	//Layout containing the main navigation menu.
	private class MainMenu extends Layout
	{
		public MainMenu(String title)	{super(title, null);}
		public void createGUI()
		{
			/*
			Button btn5 = new Button("Show differences")
				{@Override public void fire() {callNext(4);}};
			Button btn6 = new Button("Exit")
				{@Override public void fire() {Platform.exit();}};//*/
			
			Button btn1 = new Button("Single-row\nSlot Machine");
			Button btn2 = new Button("Multi-way\nSlot Machine");
			Button btn3 = new Button("Display Database\nas plain text");
			Button btn4 = new Button("Display Database\nwith script");
			Button btn5 = new Button("Show differences");
			Button btn6 = new Button("Exit");
			btn1.setOnAction((ActionEvent event) -> 
			{
				Single = new SlotMachine.SingleRow();
				callNext(0);
			});
			btn2.setOnAction((ActionEvent event) -> 
			{
				Multi = new SlotMachine.Multiway();
				callNext(1);
			});
			btn3.setOnAction((ActionEvent event) -> {callNext(2);});
			btn4.setOnAction((ActionEvent event) -> {callNext(3);});
			btn5.setOnAction((ActionEvent event) -> {callNext(4);});
			btn6.setOnAction((ActionEvent event) -> {Platform.exit();});
			
			applyButtonConfig(grid, btn1, btn2, btn3, btn4, btn5, btn6);
		}
	}
	
	//Layout containing the sub-menus for each machine type.
	private class SubMenu extends Layout
	{
		public SubMenu(String title, Layout prev)	{super(title, prev);}
		void createGUI()
		{
			Button btn1 = new Button("Play Slot Machine");
			Button btn2 = new Button("Change Parameters");
			Button btn3 = new Button("Show Rules");
			Button btn4 = new Button("Return to the\n main menu");
			btn1.setOnAction((ActionEvent event) -> {callNext(0);});
			btn2.setOnAction((ActionEvent event) -> {callNext(1);});
			btn3.setOnAction((ActionEvent event) -> {callNext(2);});
			btn4.setOnAction((ActionEvent event) -> {callPrev();});
			
			applyButtonConfig(grid, btn1, btn2, btn3, btn4);
		}
	}
	
	//Applies the desired settings to the buttons and adds them to the pane.
	public void applyButtonConfig(GridPane grid, Button... args)
    {
		int buttonWidth=180, buttonHeight=80;
    	int maxCols, col=0, row=0;
    	
    	if (args.length%2==0)		{maxCols = 2;}
    	else if (args.length%3==0)	{maxCols = 3;}
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
	
	
	
	
	//Layout containing different information displays for the payer.
	private abstract class Display extends Layout
	{
		protected TextArea tArea;
		
		public Display(String title, Layout prev)
		{
			super(title, prev);
			this.tArea = new TextArea();
		}
		void createGUI()
		{
			tArea = new TextArea();
			tArea.setCenterShape(true);
			tArea.setWrapText(true);
			tArea.setEditable(false);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PrintStream printSteam = new PrintStream(outStream);
			PrintStream old = System.out;
			System.setOut(printSteam);
			
			displayAction();
			
			System.out.flush();
			System.setOut(old);
			tArea.appendText(outStream.toString());
			
			Button rtn = new Button("Return to the main menu");
			rtn.setOnAction((ActionEvent event) -> {callPrev();});
				
			getChildren().addAll(tArea, rtn);
		}
		abstract void displayAction();
	}
	
	//Display for the record database.
	private class DisplayDB extends Display
	{
		protected boolean script;
		
		public DisplayDB(String title, SlotNodes.Layout prev, boolean op)
		{
			super(title, prev);
			this.script = op;
		}
		void displayAction() {SlotMachine.displayDB(script);}
	}
	
	//Displays for various machine properties.
	private class DisplayMach extends Display
	{
		protected int op;
		protected SlotMachine.Machine M;
		
		public DisplayMach(String title, SlotMachine.Machine ma, Layout prev, int op)
		{
			super(title, prev);
			this.M = ma;
			this.op = op;
		}
		void displayAction()
		{
			switch(op)
			{
				case 1: case 2: {M.showRules();} break;
				case 3: {SlotMachine.showDiff();} break;
			}
		}
	}
	
	
	//Layout for displaying the machine's parameters and allowing to change them.
	private class Parameters extends Layout
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
			root.add(new Label("Reels"),	0, 0);
			root.add(new TextField(),		1, 0);
			if (M instanceof SlotMachine.Multiway)
			{
				root.add(new Label("Rows"),	0, 1);
				root.add(new TextField(),	1, 1);
			}
			root.add(new Label("Symbols"),	0, 2);
			root.add(new TextField(),		1, 2);
			
			Button btn = new Button("Return to the previous menu");
			btn.setOnAction((ActionEvent e) -> {callPrev();});
			
			getChildren().addAll(root, btn);
		}	
	}
	
	
	
	
	//Layout for displaying the machine's reels and results.
	private class MachReels extends Layout
	{
		protected SlotMachine.Machine M;	//The Machine this class works with.
		Label subtitle;
		
		public MachReels(String title, SlotMachine.Machine ma, Layout prev)
		{
			super(title, prev);
			this.M = ma;
		}
		void createGUI()
		{
			if (M instanceof SlotMachine.SingleRow)
			{
				subtitle = new Label("This machine has 1 row");
			}
			else
			{
				subtitle = new Label("This machine has >1 row");
			}
			Button btn = new Button("Return to the previous menu");
			btn.setOnAction((ActionEvent e) -> {callPrev();});
			
			getChildren().addAll(subtitle, btn);
		}
	}
	
	
	
	
	
	
	@Override
    public void start(Stage primaryStage) throws Exception
    {
		try {
	        MainMenu main		= new MainMenu("--- MAIN MENU ---\n\n\n");
			SubMenu menu1		= new SubMenu("Single-row Slot Machine\n\n", main);
			SubMenu menu2		= new SubMenu("Multi-way Slot Machine\n\n", main);
			MachReels SingleR	= new MachReels("Play the game", Single, menu1);
			MachReels MultiwR	= new MachReels("Play the game", Multi, menu2);
			
			Parameters param1	= new Parameters("Change the\nparameters", Single, menu1);
			Parameters param2	= new Parameters("Change the\nparameters", Multi, menu2);
			DisplayDB DBText	= new DisplayDB("Display Database\nas plain text", main, false);
			DisplayDB DBScript 	= new DisplayDB("Display Database\nwith script", main, true);
			DisplayMach rules1	= new DisplayMach("Show the rules", Single, menu1, 1);
			DisplayMach rules2	= new DisplayMach("Show the rules", Multi, menu2, 2);
			DisplayMach diff	= new DisplayMach("Show differences", Single, main, 3);
			
			main.addNext(menu1, menu2, DBText, DBScript, diff);
			menu1.addNext(SingleR, param1, rules1);
			menu2.addNext(MultiwR, param2, rules2);
	        
	        
			primaryStage.setTitle("Welcome to the Casino!");
	        primaryStage.setScene(new Scene(main, 700, 500));
	        primaryStage.show();
    	}
        catch (Exception e) {e.printStackTrace();}
    }
    
	
    public static void main(String[] args) {launch(args);}
    
    
}