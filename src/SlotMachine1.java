import java.util.Scanner;
import java.util.ArrayList;
//import java.io.File;
//import java.text.DecimalFormat;



/* TODO:
 * Basic functionality
 * Player-Machine interaction
 * Pay table
 * Charge for spinning and free spins
 * FileWriter class
 */


public class SlotMachine1
{


	// Clears the terminal output.
	public static void clear()
	{
		// "Works" in the Eclipse IDE Console 
		//*
		System.out.println("\n\n\n\n\n");  //5
        System.out.println("\n\n\n\n\n");  //10
        System.out.println("\n\n\n\n\n");  //15
        System.out.println("\n\n\n\n\n");  //20
        System.out.println("\n\n\n\n\n");  //25
        //System.out.flush(); 
        //*/
        
		// Works in Windows Powershell
        /*
		try {
            if (System.getProperty("os.name").contains("Windows")) 
            {
				Process startProcess = new ProcessBuilder("cmd","/c","clear").inheritIO().start();
				startProcess.waitFor();
			} else {
				Process startProcess = new ProcessBuilder("clear").inheritIO().start();
				startProcess.waitFor();
			}
		} catch (Exception E) {System.out.println(E);}  
		//*/
	}
	// Delays the program for a given amount of mili-seconds.
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);}
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}
	// Stops the program until the player presses a key.
	public static void pressAnyKeyTo(String message)
	{
		System.out.println("\nPress any key to "+message+".");
        try {
            System.in.read();
            input.nextLine();
        } catch(Exception e) {}
	}
	
	// Loops until the input and type returned are valid.
	@SuppressWarnings("unchecked")
	public static <T> T readInput(String message, T par1, char dif, T par2)
	{
		if (dif=='-')		{message += " ("+par1+" - "+par2+"): ";}
		else if (dif=='/')	{message += " ("+par1+"/"+par2+"): ";}
		else if (dif=='|')	{message += " ("+par1+"): ";}
		else				{message += ": ";}
		
		boolean readError = true;
		try{
			do{
				System.out.print("\n"+message);
				String str = input.next();
				if (par1 instanceof Double)
				{
					double ans = Double.parseDouble(str);
					if (ans>=(double)par1 && ans<=(double)par2)
						{return (T)par1.getClass().cast(ans);}
					else {System.out.println("  Input outside of specified range.");}
				}
				else if (par1 instanceof Integer)
				{
					int ans = Integer.parseInt(str);
					if (ans>=(int)par1 && ans<=(int)par2)
						{return (T)par1.getClass().cast(ans);}
					else {System.out.println("  Input outside of specified range.");}	
				}
				else
				{
					if (str.charAt(0)==(char)par1 || 
						str.charAt(0)==(char)par2 || 
						str.charAt(0)==Character.toUpperCase((char)par1) || 
						str.charAt(0)==Character.toUpperCase((char)par2) )
						{return (T)par1.getClass().cast(str.charAt(0));}
					else {System.out.println("  Input outside of specified range.");}
				}
			}while(readError);
		}catch (ClassCastException | NumberFormatException e) {e.printStackTrace();}
		return par1;
	}
	@SuppressWarnings("unchecked")
	public static <T> T readInput(String message, T par1, char dif, T par2, T par3)
	{
		if (dif=='-')		{message += " ("+par1+" - "+par2+"): ";}
		else if (dif=='/')	{message += " ("+par1+"/"+par2+"): ";}
		else if (dif=='|')	{message += " ("+par1+"): ";}
		else				{message += ": ";}
		
		boolean readError = true;
		try{
			do{
				System.out.print("\n"+message);
				String str = input.next();
				if (par1 instanceof Double)
				{
					double ans = Double.parseDouble(str);
					if ( (ans>=(double)par1 && ans<=(double)par2) || ans==(double)par3 )
						{return (T)par1.getClass().cast(ans);}
					else {System.out.println("  Input outside of specified range.");}
				}
				else if (par1 instanceof Integer)
				{
					int ans = Integer.parseInt(str);
					if ( (ans>=(int)par1 && ans<=(int)par2) || ans==(int)par3 )
						{return (T)par1.getClass().cast(ans);}
					else {System.out.println("  Input outside of specified range.");}	
				}
				else
				{
					if (str.charAt(0)==(char)par1 || 
						str.charAt(0)==(char)par2 || 
						str.charAt(0)==(char)par3 ||
						str.charAt(0)==Character.toUpperCase((char)par1) || 
						str.charAt(0)==Character.toUpperCase((char)par2) ||
						str.charAt(0)==Character.toUpperCase((char)par3) )
						{return (T)par1.getClass().cast(str.charAt(0));}
					else {System.out.println("  Input outside of specified range.");}
				}
			}while(readError);
		}catch (ClassCastException | NumberFormatException e) {e.printStackTrace();}
		return par1;
	}
	
	
	
	
	
	static Scanner input = new Scanner(System.in);
	static boolean gameEnter;	//Player input for stopping or continuing the game.
	static char gameInput;		//Player input for reading a character within the game.
	
	static final double betmin=20;	//Minimum amount of money the player can bet at a time.
	static final double betmax=100;	//Maximum amount of money the player can bet at a time.
	static final int gameLimit=10;	//Maximum amount of times the player can spin the reels.
	static final int winLimit=10000;//Maximum amount of money the player can win.
	
	static char[] allSymbols = {'7','#','@','/','H','|','_','%','&','\\'};
	
	
	
	
	
	
	//The symbols found in the results of spinning
	public static class symFound 
	{
		private char sym;	//Character value
		private int count;	//Times the symbol appears in the Results
		private int pos;	//Positions the symbol is found in the Results
		
		public symFound()						{sym=' '; count=0; pos=-1;}
		public symFound(char s, int c, int p)	{sym = s; count=c; pos=p;}
		
		public char getSym()	{return sym;}
		public int getCount()	{return count;}
		public int getPos()		{return pos;}
		
	}
	
	
	
	//The players and their money
	public static class Player 
	{
		private double bet;		//Money currently as a bet
		private double spent;	//Money introduced as a bet overall
		private int numGames;	//Number of games fully completed
		
		public Player()				{bet=0.0;  spent=0.0;  numGames=0;}
		public Player(double read)	{bet=read; spent=read; numGames=0;}
		
		public double getBet()	{return bet;}
		public int getGames()	{return numGames;}
		
		
		// Increases the bet if it stays within the limit
		public void betIncrease(double increase)
		{
			while (bet+increase > winLimit) 
			{
				System.out.println("\nThis increase exceeds the limit by"+
						(winLimit-bet-increase)+"€.");
				System.out.println("Your bet remains as "+bet+"€.");
				increase = readInput("Enter a lower increase",betmin,'-',betmax);
			}
			bet += increase;
			spent += increase;
			System.out.println("\nYour bet is now "+bet+" €.");
			pressAnyKeyTo("continue the game");
		}
		
		
		// Modifies the bet based on the number of matches found 
		public void calculatePrize()
		{
			/*if (MRS.count==Results.length-1) {reroll();}
			
			if (MRS.count==1)
		    {
		    	bet = 0;
		    	gameEnter = false;
		    	System.out.println("You got no matches.");
		    }
		    else if (MRS.count==Results.length)
			{
		    	bet *= 100;
				gameEnter = false;
				System.out.println("You won the jackpot!!!");
			}
			else
			{
				System.out.println("You got the "+MRS.sym+" symbol "+MRS.count+" times.");
				if (MRS.count>Results.length/2)
		        {
					bet *= 10;
					System.out.println("You have gained money and now have "+bet+" €.");
				}
				else if (MRS.count<Results.length/2)
		        {
					bet *= 0.5;
					System.out.println("You have lost money and now have "+bet+" €.");
				}
				else {System.out.println("You still have "+bet+" €.");}
			}
			numGames++;
			
			if (numGames==gameLimit) 
			{
				gameEnter = false;
				System.out.println("\n  You have reached the maximum amount of numGames.");
			}
			if (bet>winLimit)
			{
				bet = winLimit;
				gameEnter = false;
				System.out.println("\n\t You have reached the maximum amount");
				System.out.println("\t of money that can be awarded.");
				System.out.println("\t You will recieve that instead.");
			}//*/
		}
		
		
		// Displays the money won or lost at the end of a game
		public void endMessage()
		{
			System.out.print("\n- - - - - - - - - - - - - - -");
			System.out.print("\n\n  You played "+numGames+" game"+((numGames==1)?"":"s") );
			
			if (bet==0)	
				{System.out.println("\n   and lost "+spent+" €.");}
			else if (bet==spent)
			{
				System.out.println("\n   and made back the money");
				System.out.println("   that you bet: "+bet+" €.");
			}
			else
			{
				System.out.println(".\n   You have spent "+spent+" €.");
				if (bet<spent)
					{System.out.println("    And left with "+bet+" €.");}
				else
					{System.out.println("    And have won "+bet+" €.");}
			}
			System.out.println("\n- - - - - - - - - - - - - - -");
		}
	}
	
	
	
	//The slot machines and its types
	private static class Machine
	{
		protected int rows;		//Rows of the machine
		protected int reels;	//Columns of the machine
		protected int symMax;	//Number of symbols available
		protected double cost;	//Cost of spinning the reels
		protected char[][] results;		//Rows first, columns second
		
		public Machine(int ro, int re, int syms)
		{
			this.rows = ro;
			this.reels = re;
			this.symMax = syms;
			this.results = new char[rows][reels];
		}
		
		public int getRows()	{return rows;}
		public int getReels()	{return reels;}
		public int getSyms()	{return symMax;}
		
		public int random()	{return (int)(Math.random()*symMax);}
		public void setValues(int ro, int re)
		{
			if (ro!=-1)	{rows = ro;}
			if (re!=-1)	{reels = re;}
			if (ro!=-1 && re!=-1)
				{results = new char[rows][reels];}
		}
		public void displayMachine(int reelsShown)
		{
			System.out.print("  ");
			for (int re=0; re<reels; re++) 
				{System.out.print(" _____");}
			
			for (int row=0; row<rows; row++)
			{
				System.out.print("\n  |");
				for (int reel=0; reel<reels; reel++)
					{System.out.print("     |");}
				
				System.out.print("\n  |");
				for (int reel=0; reel<reels; reel++)
					{System.out.printf("  %c  |", (reelsShown>reel) ? results[row][reel]:' ');}
		        
				System.out.print("\n  |");
		        for (int reel=0; reel<reels; reel++)
		        	{System.out.print("_____|");}
			}
			System.out.println("\n");
		}
		public void displayProgressively()
		{
			for (int reelsShown=0; reelsShown<=reels; reelsShown++)
		    {
				displayMachine(reelsShown);
				if(reelsShown<reels) {pressAnyKeyTo("spin"); clear();}
		    }
		}
		
		
	}
	
	//The machine shows one symbol of the reel at a time
	public static class SingleRow extends Machine
	{
		symFound MRS;
		symFound LRS;
		ArrayList<symFound> FoundArray;
		
		public SingleRow(int nreels, int nsymbols)
		{
			super(1, nreels, nsymbols);
			this.spinReels();
		}
		
		public void changeValues(int re, int sm)
		{
			super.setValues(1, re);
			if (sm!=-1)	{symMax = sm;}
		}
		public void spinReels()		//Gives a value to all the reels
		{
			for (int i=0; i<reels; i++)
				{results[0][i] = allSymbols[random()];}
		}
		public void countMatches()	//For every row and check for matching symbols
		{
			boolean exists;
			MRS = new symFound();
			LRS = new symFound();
			FoundArray = new ArrayList<symFound>();
			
			for (int i=0; i<reels; i++)
			{
				exists = false;
				if (!FoundArray.isEmpty())
				{
					for (int j=0; j<FoundArray.size() && !exists; j++)
					{
						if (results[0][i]==FoundArray.get(j).getSym())
							{exists = true;}
		 			}
				}
				if (!exists)
				{
					int symcount = 0, newpos = -1;
					for (int j=0; j<reels; j++)
	                {
	                    if (results[0][i]==results[0][j])
	                    {
	                    	symcount++;
	                    	if (newpos==-1) {newpos=j;}
	                    }
	                }
					symFound newSym = new symFound(results[0][i], symcount, newpos);
					FoundArray.add(newSym);
	                if (symcount > MRS.count)	{MRS = newSym;}
	                if (symcount < LRS.count)	{LRS = newSym;}
	            }
			}
		}	
		public void reroll()		//Re-roll when one reel is left for the jackpot
		{
			System.out.println(" You are one reel away from the jackpot.");
			System.out.println(" You can reroll for the chance to get all matches,");
			System.out.println(" but if you fail you will loose all your money.");
			
			gameInput = readInput("Do you want to reroll the "+(LRS.pos+1)+"º reel?",'y','/','n');
			if (gameInput=='y' || gameInput=='Y')
			{
				//results[LRS.pos.get(0)] = spinReel();
				displayMachine(results.length);
				
				if (results[MRS.pos]==results[LRS.pos])
				{
					System.out.println("The new symbol is a match.\n");
					MRS.count = results.length;
				}
				else
				{
					System.out.println("The new symbol is still different.\n");
					MRS.count = 1;
				}
			}
			System.out.println();
		}
		
	}
	
	//The machine shows several symbols of the reel
	public static class Multiway extends Machine
	{
		char[] machSymbols;	//Array of symbols available in the machine
		char checking;		//Character used to check for the winning lines
		boolean diagonal1;	//True if the first diagonal is a winning line
		boolean diagonal2;	//True if the second diagonal is a winning line
		int horizontal;		//Position of the row that is a winning line
		
		public Multiway(int size, int nSymbols)	{this(size, size, nSymbols);}
		public Multiway(int nRows, int nReels, int nSymbols)
		{
			super(nRows, nReels, nSymbols);
			this.machSymbols = new char[symMax];
			for (int i=0; i<symMax; i++) {this.machSymbols[i] = allSymbols[i];}
			this.spinReels();
		}
		
		public void changeValues(int ro, int re, int sm)
		{
			super.setValues(ro, re);
			if (sm!=-1)	
			{
				symMax = sm;
				machSymbols = new char[symMax];
				for (int i=0; i<symMax; i++) {machSymbols[i] = allSymbols[i];}
			}
		}
		public void	spinReels()		//Generates symbols in the reels in the intended order
		{
			for (int i=0; i<reels; i++)
			{
				int pos = random();	//Position of a random symbol
				int next;			//Position of the next symbol in the array
				results[0][i] = allSymbols[pos];
				for (int j=0; j<rows; j++)
				{
					if (pos+j >= rows)	{next = pos+j-rows;}
					else				{next = pos+j;}
					
					results[0+j][i] = machSymbols[next];
				}
			}
		}
		public void checkWinLines()	//Checks all the winning lines
		{
			diagonal1 = true;
			diagonal2 = true;
			horizontal = -1;
			int limit = ((rows<reels) ? rows:reels);//Whichever one is smaller
			
			//Diagonal 1: top-left --> bottom-right
			checking = results[0][0];
			for (int i=0; i<limit; i++)
			{
				if (checking!=results[i][i]) {diagonal1 = false; break;}
			}
			
			//Diagonal 2: top-right --> bottom-left
			checking = results[0][reels];
			for (int i=0; i<limit; i++)
			{
				if (checking!=results[i][reels-i]) {diagonal2 = false; break;}
			}
			
			//Horizontal lines: from top to bottom
			for (int i=0; i<rows; i++)
			{
				checking = results[i][0];
				for (int j=0; j<reels; j++)
				{
					if (checking!=results[i][j]) {horizontal = i; break;}
				}
			}
		}
		public void reroll()		//__Re-roll when one reel is left for the jackpot
		{
			
		}
		
	}

	
	
	
	
	
	
	
	
	
	public static void main(String[] args)
	{
		int opc1, opc2;
		int nReels, nRows, nSyms;
		
		do {
			
			
			clear();
			System.out.println("\n\t --- MAIN MENU --- ");
			System.out.println("\nWhat do you want to do?");
			System.out.println(" 1. Single row slot machine.");
			System.out.println(" 2. Multiway slot machie.");
			System.out.println(" 0. Exit.");
			opc1 = readInput("Choose an option",0,' ',3);
			
			
			switch(opc1)
			{
				
				// *************** Single Row Slot Machine ***************
				case 1:
				{
					System.out.println("\nChoose the parameters for the machine");
					nReels = readInput(" Number of reels",4,'-',8,-1);
					nSyms = readInput(" Number of symbols",4,'-',allSymbols.length,-1);
					SingleRow M1 = new SingleRow(nReels, nSyms);
					
					do {
						clear();
						System.out.println("\n\t --- Welcome to the Slot Machine --- ");
						System.out.println("Bet an some money and try to win a prize!!");
						System.out.println("This machine has "+M1.getReels()+" reels and "
								+M1.getSyms()+" available symbols.");
						System.out.println("\nWhat do you want to do?");
						System.out.println(" --1. Play the game.");
						System.out.println(" --2. View the ruleset.");
						System.out.println(" 3. Change the parameters.");
						System.out.println(" 0. Return to the previous menu.");
						opc2 = readInput("Choose an option",0,' ',4);
						
						switch(opc2)
						{
							case 1: {startGame(M1);} break;
							case 2: {} break;
							case 3:
							{
								System.out.println("\n\t Reels="+M1.getReels()+" Symbols="+M1.getSyms());
								System.out.println("Input new values for these parameters.");
								System.out.println("If you don't want to change the value type -1");
								nReels = readInput(" Number of reels",4,'-',8,-1);
								nSyms = readInput(" Number of symbols",4,'-',allSymbols.length,-1);
								M1.changeValues(nReels, nSyms);
							}
							break;
							case 0: 
							{
								System.out.println("Returning to the previous menu");
								wait(800);
							}
						}
						if (opc2!=0) {pressAnyKeyTo("return to the menu");}
						
					} while (opc2!=0);
					
					
				}
				break;
				
				
				// *************** Multi-way Slot Machine *************** 
				case 2: 
				{
					
					
					System.out.println("\nChoose the parameters for the machine");
					nReels = readInput(" Number of reels",3,'-',7,-1);
					nRows = readInput(" Number of rows",3,'-',7,-1);
					nSyms = readInput(" Number of symbols",4,'-',allSymbols.length,-1);
					Multiway M2 = new Multiway(nReels,nRows,nSyms);
					
					do {
						clear();
						System.out.println("\n\t --- Welcome to the Slot Machine --- ");
						System.out.println("Bet an some money and try to win a prize!!");
						System.out.println("This machine has "+M2.getReels()+" reels, "
								+M2.getRows()+" rows \n and "+M2.getSyms()+" available symbols.");
						System.out.println("\nWhat do you want to do?");
						System.out.println(" 1. Play the game.");
						System.out.println(" --2. View the ruleset.");
						System.out.println(" 3. Change the parameters.");
						System.out.println(" 0. Return to the previous menu.");
						opc2 = readInput("Choose an option",0,' ',4);
						
						switch(opc2)
						{
							case 1: {startGame(M2);} break;
							case 2: {} break;
							case 3:
							{
								System.out.println("\n\tRows="+M2.getRows()+" Reels="+M2.getReels()+
										"Symbols="+M2.getSyms());
								System.out.println("Input new values for these parameters.");
								System.out.println("If you don't want to change the value type -1");
								nReels = readInput(" Number of reels",3,'-',7,-1);
								nRows = readInput(" Number of rows",3,'-',7,-1);
								nSyms = readInput(" Number of symbols",4,'-',allSymbols.length,-1);
								M2.changeValues(nRows, nReels, nSyms);
							}
							break;
							case 0: 
							{
								System.out.println("Returning to the previous menu");
								wait(800);
							}
							break;
						}
						if (opc2!=0) {pressAnyKeyTo("continue");}
						
						
					} while (opc2!=0);
					
					
				}
				break;
				case 0: {}	break;
				default: {}	break;
			}
			if (opc1!=0)
			{
				pressAnyKeyTo("return to the main menu");
				clear();
			}
			
			
		} while (opc1!=0);
		
		System.out.println("\n\n\t\t --- Game Over ---");
		input.close();
	}
	
	
	
	
	public static <T> void startGame(T M)
	{
		Player P1 = new Player(readInput("Enter your bet",betmin,'-',betmax));
		gameEnter = true;
		
		do {
			
			if (M instanceof SingleRow)
			{
				((SingleRow) M).displayProgressively();
				((SingleRow) M).countMatches();
			}
			else
			{
				((Multiway) M).displayProgressively();
				((Multiway) M).checkWinLines();
			}
			P1.calculatePrize();
			
			
			if (gameEnter)
			{
		        gameInput = readInput("Do you want to continue playing?",'y','/','n');
		        if (gameInput=='y'||gameInput=='Y')
		        {
		        	gameInput = readInput("Do you want to bet more money?",'y','/','n');
		            if (gameInput=='y'||gameInput=='Y')
		            	{P1.betIncrease(readInput("Enter the increase",betmin,'-',betmax));}
		        }
		        else {gameEnter = false;}
		    }
			
		} while ((gameEnter) && (P1.getBet()<winLimit) && (P1.getGames()<gameLimit));
		
		P1.endMessage();
		
	}



}