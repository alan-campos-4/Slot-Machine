import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SlotNodes extends Application
{
	private abstract class Layout extends VBox
    {
		private Layout prev;
    	protected ArrayList<Layout> Next;
    	protected Label title;
    	protected GridPane grid;
    	
    	public Layout(String titletext, Layout prevNode)
        {
    		this.Next = new ArrayList<Layout>();
    		this.title = new Label(titletext);
    		this.grid = new GridPane();
    		this.title.setAlignment(Pos.CENTER);
    		this.grid.setAlignment(Pos.CENTER);
            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(title, grid);
            this.prev = prevNode;
            createGUI();
        }
    	
        abstract void createGUI();
        
        protected void addNext(Layout... next)	{for (Layout nd : next) {Next.add(nd);}};
        protected void callNext(int i)	{getScene().setRoot(Next.get(i));}
        protected void callPrev()		{getScene().setRoot(prev);}
        
    }
	
	
	
	
	private class MainMenu extends Layout
	{
		public MainMenu(String title)	{super(title, null);}
		
		public void createGUI()
		{
			Button btn1 = new Button("Single-row\nSlot Machine")
				{@Override public void fire() {callNext(0);}};
			Button btn2 = new Button("Multi-way\nSlot Machine")
				{@Override public void fire() {callNext(1);}};
			Button btn3 = new Button("Display Database\nas plain text")
				{@Override public void fire() {callNext(2);}};
			Button btn4 = new Button("Display Database\nwith script")
				{@Override public void fire() {callNext(3);}};
			Button btn5 = new Button("Show differences")
				{@Override public void fire() {callNext(4);}};
			Button btn6 = new Button("Exit");
			btn6.setOnAction((ActionEvent event) -> {Platform.exit();});
			
			applyButtonConfig(grid, btn1, btn2, btn3, btn4, btn5, btn6);
		}
	}
	
	
	private class SubMenu extends Layout
	{
		public SubMenu(String title, Layout prev)	{super(title, prev);}
		
		void createGUI()
		{
			//Button btn1 = new Button("1")	{@Override public void fire() {callNext(1);}};
			//Button btn2 = new Button("2")	{@Override public void fire() {callNext(2);}};
			//Button btn3 = new Button("3")	{@Override public void fire() {callNext(3);}};
			Button btn4 = new Button("Return to the\n main menu")
				{@Override public void fire() {callPrev();}};
			
			applyButtonConfig(grid, btn4);
		}
	}
	
	
	private class Display extends Layout
	{
		public Display(String title, Layout prev)	{super(title, prev);}
		
		void createGUI()
		{
			Button btn4 = new Button("Return to the\n main menu")
				{@Override public void fire() {callPrev();}};
			
			applyButtonConfig(grid, btn4);
		}
	}
	
	
	
	public void applyButtonConfig(GridPane grid, Button... args)
    {
		int buttonWidth=180, buttonHeight=80;
    	int maxCols, col=0, row=0;
    	
    	if (args.length%2==0)		{maxCols = 2;}
    	else if (args.length%3==0)	{maxCols = 3;}
    	else if (args.length%5==0)	{maxCols = 5;}
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
	
	
	
	
	
	
	
	/*
	 * Menu MainMenu
	 * *	Next:	MenuSingleRow / MenuMultiway / DisplayText / DisplayScript / ShowDiff / Exit
	 * *	Prev:	<none>  
	 * 
	 * 
	 * 
	 * SubMenu MenuSingleRow
	 * *	Next:	MachineSingleRow / showRulesS / changeParamS / Return
	 * *	Prev:	MainMenu
	 * 
	 * SubMenu MenuMultiway
	 * *	Next:	MachineSingleRow / showRulesM / changeParamM / Return
	 * *	Prev:	MainMenu
	 * 
	 * 
	 * 
	 * Machine MachineSingleRow
	 * *	Next:	<none>
	 * *	Prev:	MenuSingleRow
	 * 
	 * Machine MachineMultiway
	 * *	Next:	<none>	
	 * *	Prev:	MenuMultiway
	 * 
	 * 
	 * 
	 * Display ...
	 * *	Next:	<none>
	 * *	Prev:	menu...
	 * 
	 * 
	 * */
	
	@Override
    public void start(Stage primaryStage) throws Exception
    {
		try {
	        MainMenu main		= new MainMenu("--- Main Menu ---");
			SubMenu singleMenu	= new SubMenu("Single-row Slot Machine", main);
			SubMenu multiwMenu	= new SubMenu("Multi-way Slot Machine", main);
			Display DBText		= new Display("Display Database\nas plain text", main);
			Display DBScript 	= new Display("Display Database\nwith script", main);
			Display showDiff	= new Display("Show differences", main);
			main.addNext(singleMenu, multiwMenu, DBText, DBScript, showDiff);
			
	        
	        
	        
			Scene scene = new Scene(main, 500, 500);
	        primaryStage.setTitle("Welcome to the Casino!");
	        primaryStage.setScene(scene);
	        primaryStage.show();
    	}
        catch (Exception e) {e.printStackTrace();}
    }
    
    public static void main(String[] args) {launch(args);}
}
