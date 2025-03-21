import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.efabrika.util.DBTablePrinter;






public class SlotMachine
{
	
	/********** General classes and methods **********/
	
	static Scanner input;
	static Random rand = new Random();
	
	// Delays the program for a given amount of milliseconds.
	public static void wait(int mili)
	{
		try {
			Thread.sleep(mili);
		}
        catch (InterruptedException e) {e.printStackTrace();}
	}
	
	// Clears the terminal output.
	public static void clear()
	{
		/*try {
			
            if (System.getProperty("os.name").contains("Windows")) 
            {
            	Process startProcess = new ProcessBuilder("cmd","/c","clear").inheritIO().start();
				startProcess.waitFor();
			}
            else
            {
				Process startProcess = new ProcessBuilder("clear").inheritIO().start();
				startProcess.waitFor();
			}
			System.out.print("\033[H\033[2J");
			System.out.flush();
		}
		catch (Exception e)*/
		{
			System.out.println("\n\n\n\n\n");  //5
	        System.out.println("\n\n\n\n\n");  //10
	        System.out.println("\n\n\n\n\n");  //15
	        System.out.println("\n\n\n\n\n");  //20
	        System.out.println("\n\n\n\n\n");  //25
		}
	}
	
	// Returns true if the value given exists in the array
	public static boolean exists(char[] arr, char value)
	{
		for (char element : arr)
		{
			if (element==value) {return true;}
		}
		return false;
	}
	
	// Stops the program until the player presses a key.
	public static void pressAnyKeyTo(String message)
	{
		System.out.println("\nPress any key to "+message+".");
		//Scanner input1 = new Scanner(System.in);
        try
        {
            System.in.read();
            input.nextLine();
        }
        catch(Exception e) {e.printStackTrace();}
        //input1.close();
	}
	
	// Loops until the input and type returned are valid.
	@SuppressWarnings("unchecked")
	public static <T> T readInput(String message, T param1, char dif, T param2)
	{
		if (dif=='-')		{message += " ("+param1+" - "+param2+"): ";}
		else if (dif=='/')	{message += " ("+param1+"/"+param2+"): ";}
		else				{message += ": ";}
		
		//input = new Scanner(System.in);
		String inputstr;
		boolean read = true;
		do {
			try {
				System.out.print(message);
				inputstr = input.nextLine();
				if (!inputstr.isEmpty())
				{
					if (param1 instanceof String)
						{return (T)inputstr;}
					else if (param1 instanceof Character)
					{
						char ans = inputstr.charAt(0);
						if (ans==(char)param1 || ans==Character.toUpperCase((char)param1) 
						 || ans==(char)param2 || ans==Character.toUpperCase((char)param2) )
							{return (T)param1.getClass().cast(ans);} //can throw ClassCastException
						else
							{System.out.println("\nInput outside of range.");}
					}
					else if (param1 instanceof Integer)
					{
						int ans = Integer.parseInt(inputstr); //can throw NumberFormatException
						if (ans>=(int)param1 && ans<=(int)param2)
							{return (T)Integer.class.cast(ans);} //can throw ClassCastException
						else
							{System.out.println("\nInput outside of range.");}
					}
					else if (param1 instanceof Double)
					{
						double ans = Double.parseDouble(inputstr);
						if (ans>=(double)param1 && ans<=(double)param2)
							{return (T)Double.class.cast(ans);}
						else
							{System.out.println("\nInput outside of range.");}
					}
					else {System.out.println("\nInput type not accepted.");}
				}
				else {System.out.println("\nNo input recognized.");}
			}
			catch (NumberFormatException e)	{System.out.println("\nInput should be a number.");}
			catch (ClassCastException e)	{System.out.println("\nInput type not casted.");}
		} while(read);
		//input.close();
		return null;
	}
	
	
	
	
	
	
	
	
	/********** Game attributes **********/
	
	//All symbols that can appear on a machine
	static final char[] all = {'7','A','H','K','T','*','@','^','|','%','&','\\'};
	
	static final String def_name = "P1";//Default name for the machine's player
	static final double BETMIN = 20;	//Minimum amount of money the player can bet at a time.
	static final double BETMAX = 100;	//Maximum amount of money the player can bet at a time.
	static final int WINLIMIT = 100000;	//Maximum amount of money the player can win.
	static final int GAMELIMIT = 10;	//Maximum amount of times the player can spin the reels.
	
	static char gameInput;				//Player input for reading a character within the game.
	
	
	
	
	
	
	
	/********** Class and interface implementation **********/
	
	//The player and their actions
	public static class Player
	{
		protected String name;		//The player's name
		protected int numGames;		//Number of games fully completed
		protected double bet;		//Money currently as a bet
		protected double spent;		//Money introduced as a bet overall
		
		public Player(String nm)
		{
			this.name = nm;
			this.numGames = 0;
			this.bet = 0.0;
			this.spent = 0.0;
		}
		public Player()		{this("P1");}
 		
		public void betInit(double read)	//Initializes the player's bet.
		{
 			bet = read;
 			spent = read;
 			numGames = 0;
 		}
		public void betIncrease(double inc)	//Increases the bet if it stays within the limit.
		{
			while (bet+inc > WINLIMIT)
			{
				System.out.println("\nThis increase exceeds the limit by"+(WINLIMIT-bet-inc)+"€.");
				System.out.println("Your bet remains as "+bet+"€.");
				inc = readInput("Enter a lower increase",BETMIN,'-',BETMAX);
			}
			bet += inc;
			spent += inc;
			System.out.println("\nYour bet is now "+bet+" €.");
			pressAnyKeyTo("continue the game");
		}
		public void endMessage()			//Displays the money won or lost at the end of a game.
		{
			System.out.println("\n- - - - - - - - - - - - - - -");
			System.out.print("\n  You played "+numGames+" game"+((numGames==1)?"":"s") );
			
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
				if (bet<spent)	{System.out.println("    And left with "+bet+" €.");}
				else			{System.out.println("    And have won "+bet+" €.");}
			}
			System.out.println("\n- - - - - - - - - - - - - - -");
		}
		
	}
	
	//The slot machines' characteristics and actions regardless of type.
	abstract static class Machine
	{
		protected char[][] arrResults;	//Array of the results of spinning. [Rows][Columns]
		protected char[] arrSyms;		//Array of the symbols available to this machine.
		protected int rows;				//Number of rows of the machine.
		protected int reels;			//Number of columns or reels of the machine.
		protected double cost;			//Cost of spinning the reels per game
		protected boolean rerolled;		//True if a re-rolled has been done.
		
		protected int minSize, maxSize;	//Minimum and maximum amount of rows and reels allowed
		protected int minSyms, maxSyms;	//Minimum and maximum number of symbols available allowed
		
		protected Player P;				//The machine's player
		protected boolean gameEnter;	//Player input for stopping or continuing the game.
		
		public Machine(int ro, int re, int syms, String name)
		{
			assignParameters(ro, re, syms);
			this.P = new Player(name);
		}
		public Machine(int ro, int re, int syms)	{this(ro, re, syms, def_name);}
		
		public int getReels()	{return reels;}
		public int getRows()	{return rows;}
		public int getSyms()	{return arrSyms.length;}
		public int getGames()	{return P.numGames;}
		public double getCost()		{return cost;}
		public double getBet()		{return P.bet;}
		public double getSpent()	{return P.spent;}
		public boolean gameEnter()	{return gameEnter;}
		
		public void assignParameters(int ro, int re, int sy)	//Assigns the parameters to the machine.
		{
			this.rows = ro;
			this.reels = re;
			this.arrResults = new char[ro][re];
			assignSymbols(sy);
			assignSpinCost();
		}
		public void assignSymbols(int size)		//Gives values to the array of available symbols.
		{
			this.arrSyms = new char[size];
			char newsym;
			for (int i=0; i<size; i++)
			{
				do {
					newsym = all[rand.nextInt(arrSyms.length)];
				} while (exists(arrSyms, newsym));
				this.arrSyms[i] = newsym;
			}
		}
		public void assignSpinCost()			//Assigns the cost of spinning the reels.
		{
			double mod = ((this instanceof SingleRow)? 5 : 1.5);
			this.cost = Math.ceil( ((double)rows * (double)reels) / (double)arrSyms.length )*mod;
		}
		public void assignName(String pname)	//Assigns a new name to the player.
		{
			this.P.name = pname;
		}
		public void inputParameters()			//Changes the rows, reels and symbols of the machine.
		{
			int nRows, nReels, nSyms;
			
			System.out.println("\n\t"+((this instanceof Multiway) ? (" Rows="+rows+" "):(" "))
					+ "Reels="+reels+"Symbols="+arrSyms.length);
			System.out.println("Input new values for these parameters.");
			
			if (this instanceof SingleRow)
				{nRows = 1;}
			else
				{nRows = readInput(" Number of rows",minSize,'-',maxSize);}
			nReels = readInput(" Number of reels",minSize,'-',maxSize);
			nSyms = readInput(" Number of symbols",minSyms,'-',all.length);
			
			while (nSyms<nRows || nSyms<nReels)
			{
				System.out.println("The number of symbols cannot be smaller than \n"
						+ "the number of "+((nRows<nReels)? "rows":"reels" )+"." );
				System.out.println("Input the amount again.");
				nSyms = readInput(" Number of symbols",minSyms,'-',all.length);
			}
			
			assignParameters(nRows, nReels, nSyms);
			if (this instanceof Multiway)
				{((Multiway)this).limit = Integer.min(nReels, nRows);}
		}
		
		public void displayReels(int show)	//Displays the given number of reels of the machine.
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
					{System.out.printf("  %c  |", (show>reel) ? arrResults[row][reel]:' ');}
		        
				System.out.print("\n  |");
		        for (int reel=0; reel<reels; reel++)
		        	{System.out.print("_____|");}
			}
			System.out.println("\n");
		}
		public void displayReels()			//Displays all of the reels of the machine
		{
			displayReels(reels);
		}
		public void displayAndSpin()		//Displays the reels as they are spinning.
		{
			for (int reelsShown=0; reelsShown<=reels; reelsShown++)
		    {
				System.out.println("\t-- "+(P.numGames)+"º Game --");
				displayReels(reelsShown);
				if(reelsShown<reels)
				{
					pressAnyKeyTo("spin the "+(reelsShown+1)+"º reel");
					clear();
				}
		    }
		}
		
		public void showRules()				//Shows the rules and outcomes of playing.
		{
			System.out.println("\n  Game Rules:");
			System.out.println("  - Every bet has to be between "+BETMIN+" and "+BETMAX+" €.");
			System.out.println("  - You can't play more than "+GAMELIMIT+" games.");
			System.out.println("  - There is a limit to how much you can win.");
			System.out.println("\n  Prizes available:");
			if (this instanceof SingleRow)
			{
				
				System.out.println("  - All reels match : jackpot.");
				System.out.println("  - Almost all reels match : chance at all or nothing.");
				System.out.println("  - More than half the reels match : win some money.");
				System.out.println("  - Less than half the reels match : lose some money.");
				System.out.println("  - Half of the reels match : keep your bet.");
				System.out.println("  - No matches : you lose.");
			}
			else
			{
				System.out.println("  - All lines match: jackpot");
				System.out.println("  - Both diagonals match: win a lot of money.");
				System.out.println("  - Any diagonal match: win some money.");
				System.out.println("  - Any horizontal match: win some money");
				System.out.println("  - No lines match: you lose.");
			}
		}
		public void showProb()				//Shows the probability of each outcome.
		{
			DecimalFormat formatter = new DecimalFormat("#0.0000");
			System.out.println("\n  Probability:");
			if (this instanceof SingleRow)
			{
				double probAll;		//All of the reels match.
				double probAlmost;	//All of the reels except one match.
				double probMH=0; 	//More than half the reels match.
				double probHalf;	//Half of the reels match.
				double probLH=0;	//Less than half the reels match.
				double probNone=1;	//Not a single match.
				int half = (int)Math.ceil((double)reels/2);
				
				probAll		= Math.pow( ((double)1/arrSyms.length), reels );
				probAlmost	= Math.pow( ((double)1/arrSyms.length), reels-1 );
				probHalf	= Math.pow( ((double)1/arrSyms.length), half );
				for (int i=half+1; i<=reels; i++)	{probMH += Math.pow( ((double)1/arrSyms.length), i );}
				for (int i=2; i<half; i++)			{probLH += Math.pow( ((double)1/arrSyms.length), i );}
				for (int i=0; i<reels; i++)			{probNone *= ((double)(reels-i)/arrSyms.length);}
				
				System.out.println("  - All reels \t"+formatter.format(probAll*100)+" %");
				System.out.println("  - Almost all \t"+formatter.format(probAlmost*100)+" %");
				System.out.println("  - > Half \t"+formatter.format(probMH*100)+" %");
				System.out.println("  - = Half \t"+formatter.format(probHalf*100)+" %");
				System.out.println("  - < Half \t"+formatter.format(probLH*100)+" %");
				System.out.println("  - No match \t"+formatter.format(probNone*100)+" %");
			}
			else
			{
				double probAll=0;	//All lines match.
				double probDiags=0;	//Both diagonals match.
				double probDiag=0;	//One of the diagonals match.
				double probHori=0;	//Any horizontal match.
				double probNone=1;	//No lines match.
				int limit = Integer.min(rows,reels);
				
				probDiag 	= Math.pow( ((double)1/arrSyms.length), limit );
				probDiags	= probDiag*probDiag;
				probHori	= Math.pow( ((double)1/arrSyms.length), reels );
				probAll		= probHori*rows;
				probNone	= (1-probDiag)*(1-probHori);
				
				System.out.println("\n  Probability:");
				System.out.println("  - All lines    \t"+formatter.format(probAll*100)+" %");
				System.out.println("  - Both diagonals \t"+formatter.format(probDiags*100)+" %");
				System.out.println("  - One diagonal \t"+formatter.format(probDiag*100)+" %");
				System.out.println("  - One horizontal \t"+formatter.format(probHori*100)+" %");
				System.out.println("  - No lines     \t"+formatter.format(probNone*100)+" %");
			}
		}
		
		public void saveResults(int type)	//Saves the results of spinning in the record file.
		{
			String res = "", typeS = "";
			for (int i=0; i<rows; i++)
			{
				for (int j=0; j<reels; j++)
					{res += arrResults[i][j];}
				if (i+1<rows)
					{res += '|';}
			}
			switch(type)
			{
				case 1: typeS = "Terminal"; break;
				case 2: typeS = "JavaFx"; break;
			}
			
			insertDB(P.name, P.numGames, res, P.spent, P.bet, typeS);
		}
		
		public void menuSelect()	//Menu and options for every action of a machine
		{
			int opc2;
			
			do {
				clear();
				System.out.println("\n\t  --- Welcome to the Slot Machine ---");
				System.out.println("\nSpin the reels and try to match the symbols");
				System.out.println("The bigger your bet, the bigger a prize you'll win!!");
				
				System.out.println("This machine has "+reels+" reels"
						+ ((this instanceof Multiway) ? (", "+rows+" rows \n"):(" ") )
						+ "and "+arrSyms.length+" possible symbols");
				System.out.println(" and costs "+cost+"€ to spin per turn.\n");
				
				System.out.println("What do you want to do?");
				System.out.println(" 1. Play the game.");
				System.out.println(" 2. View the ruleset.");
				System.out.println(" 3. Change the parameters.");
				System.out.println(" 0. Return to the previous menu.");
				opc2 = readInput("Choose an option",0,' ',4);
				
				switch(opc2)
				{
					case 3: {inputParameters();} break;
					case 2: {showRules();} break;
					case 4: {showProb();} break;
					case 0: {} break;
					case 1:
					{
						//gameEnter = true;
						P.betInit(readInput("Enter your bet",BETMIN,'-',BETMAX));
						
						do {
							clear();
							Game();
							if (gameEnter)
							{
						        gameInput = readInput("Do you want to continue playing?",'y','/','n');
						        if (gameInput=='y'||gameInput=='Y')
						        {
						        	gameInput = readInput("Do you want to bet more money?",'y','/','n');
						            if (gameInput=='y'||gameInput=='Y')
						            	{P.betIncrease(readInput("Enter the increase",BETMIN,'-',BETMAX));}
						        }
						        else {break;}//{gameEnter = false;}
						    }
							
						} while ((gameEnter) && (P.bet<WINLIMIT) && (P.numGames<GAMELIMIT));
						P.numGames = 0;
						P.endMessage();
					}
					break;
				}
				if (opc2!=0) {pressAnyKeyTo("return to the slot menu");}
				
			} while (opc2!=0);
			
		}
		
		public void Game()			//Playing one game of the slot machine.
		{
			P.numGames++;
			P.bet -= cost;
			rerolled = false;
			gameEnter = true;
			if (this instanceof SingleRow)
			{
				((SingleRow)this).generateValue();
				displayAndSpin();
				((SingleRow)this).checkResults();
				((SingleRow)this).generatePayout();
			}
			else
			{
				((Multiway)this).generateValue();
				displayAndSpin();
				((Multiway)this).checkResults();
				((Multiway)this).generatePayout();
			}
			saveResults(1);
		}
		
	}
	
	//The actions that vary between the type of slot machine.
	public static interface Actions
	{
		public void generateValue();	//Spins the reels and gives value to the results.
		public void checkResults();		//Checks for the win conditions of each machine.
		public void generatePayout();	//Calculates the prize based on the conditions of each machine.
		public void create_Reroll();	//Changes the reels to ensure a re-roll opportunity. 
		public void reroll();			//Gives opportunity to re-spin if close to the maximum prize.
	}
	
	
	
	//Type of slot machine that shows one symbol of the reel at a time
	public static class SingleRow extends Machine implements Actions
	{
		//ArrayList of every different symbol found in the results
		protected ArrayList<Character> arrFound;
		//For both the most and least repeated symbols: MRS and LRS
		protected char MRsym, LRsym;	//Character value
		protected int MRcount, LRcount;	//Times the symbol appears in the results
		protected int MRpos, LRpos;		//Position the symbol is first found in the results
		
		public SingleRow(int nreels, int nsymbols, String name)
		{
			super(1, nreels, nsymbols, name);
			minSize = 4;	maxSize = 8;
			minSyms = 4;	maxSyms = 8;
		}
		public SingleRow(int nreels, int nsymbols)	{this(4, 6, def_name);}
		public SingleRow(String name)				{this(4, 6, name);}
		public SingleRow()							{this(4, 6, def_name);}
		
		public void generateValue()
		{
			for (int i=0; i<reels; i++)
				{arrResults[0][i] = arrSyms[rand.nextInt(arrSyms.length)];}
		}
		public void checkResults()
		{
			boolean found;	//If the symbol being examined has been found
			int count, pos;	//Count and position of the symbol being examined
			MRsym = LRsym = ' ';
			MRpos = LRpos = -1;
			MRcount = 0; LRcount = reels;
			arrFound = new ArrayList<Character>();
			
			for (int i=0; i<reels; i++)
			{
				found = false;
				if (!arrFound.isEmpty())
				{
					for (int j=0; j<arrFound.size() && !found; j++)
					{
						if (arrResults[0][i]==arrFound.get(j))
							{found = true;}
		 			}
				}
				if (!found)
				{
					count = 0; 
					pos = -1;
					for (int j=0; j<reels; j++)
	                {
	                    if (arrResults[0][i]==arrResults[0][j])
	                    {
	                    	if (pos==-1) {pos=j;}
	                    	count++;
	                    }
	                }
	                if (count > MRcount)	{MRsym=arrResults[0][i]; MRcount=count; MRpos=pos;}
	                if (count < LRcount)	{LRsym=arrResults[0][i]; LRcount=count; LRpos=pos;}
	                arrFound.add(arrResults[0][i]);
	            }
			}
		}	
		public void generatePayout()
		{
			if (MRcount==reels-1) {reroll();}
			
			if (MRcount==1)
		    {
		    	P.bet = 0;
		    	gameEnter = false;
		    	System.out.println("You got no matches.");
		    }
		    else if (MRcount==reels)
			{
		    	P.bet *= 100;
				gameEnter = false;
				System.out.println("You won the jackpot!!!");
			}
			else
			{
				System.out.println("You got the "+MRsym+" symbol "+MRcount+" times.");
				if (MRcount>reels/2)
		        {
					P.bet *= 10;
					System.out.println("You have gained money and now have "+P.bet+" €.");
				}
				else if (MRcount<reels/2)
		        {
					P.bet *= 0.5;
					System.out.println("You have lost money and now have "+P.bet+" €.");
				}
				else
					{System.out.println("You still have "+P.bet+" €.");}
			}
			
			if (P.numGames==GAMELIMIT) 
			{
				gameEnter = false;
				System.out.println("\n  You have reached the maximum amount of numGames.");
			}
			if (P.bet>WINLIMIT)
			{
				P.bet = WINLIMIT;
				gameEnter = false;
				System.out.println("\n\t You have reached the maximum amount");
				System.out.println("\t of money that can be awarded.");
				System.out.println("\t You will recieve that instead.");
			}
		}
		public void create_Reroll()
		{
			char sym2;
			char sym1 = arrSyms[rand.nextInt(arrSyms.length)];
			do {sym2 = arrSyms[rand.nextInt(arrSyms.length)];} 
			while (sym2==sym1);
			
			for (int i=0; i<reels; i++)	{arrResults[0][i] = sym1;}
			
			arrResults[0][rand.nextInt(reels)] = sym2;
		}
		public void reroll()
		{
			if (!rerolled)
			{
				System.out.println(" You are one reel away from the jackpot.\n");
				System.out.println(" · You can leave it be and get the reward"
								+"\n  for the "+MRcount+" matching reels.");
				System.out.println(" · Or you can risk loosing all your money"
								+"\n  for the chance of getting the jackpot.");
				
				gameInput = readInput("Do you want to reroll the "+(LRpos+1)+"º reel?",'y','/','n');
				if (gameInput=='y' || gameInput=='Y')
				{
					arrResults[0][LRpos] = arrSyms[rand.nextInt(arrSyms.length)];
					displayReels();
					
					if (arrResults[0][MRpos]==arrResults[0][LRpos])
					{
						System.out.println("The new symbol is a match.\n");
						MRcount = reels;
					}
					else
					{
						System.out.println("The new symbol is still different.\n");
						MRcount = 1;
					}
				}
				System.out.println();
				rerolled = true;
			}
		}
		
	}
	
	
	//Type of slot machine that shows several symbols of the reel
	public static class Multiway extends Machine implements Actions
	{
		protected char checking;	//Character used to check for the winning lines.
		protected int limit;		//Number of reels or rows, whichever one is smaller.

		protected int[] horizontal;	//Numbers of symbols matching along the horizontal lines.
		protected int diagonal1;	//Number of symbols matching along the first diagonal line.
		protected int diagonal2;	//Number of symbols matching along the second diagonal line.
		
		protected int missingPos;	//Position of the symbol left to match whole line.
		protected int horiMatch;	//Position of the horizontal line with matching symbols.
		
		public Multiway(int size, int nSymbols, String name)
		{
			super(size, size, nSymbols, name);
			minSize = 3;	maxSize = 7;
			minSyms = 4;	maxSyms = 8;
			horiMatch = missingPos = -1;
			horizontal = new int[reels];
			limit = Integer.min(rows,reels);
		}
		public Multiway(int size, int syms)		{this(size, syms, def_name);}
		public Multiway(String name)			{this(5, 6, name);}
		public Multiway()						{this(5, 6, def_name);}
		
		public void	generateValue()
		{
			int startPos=0, nextPos=0;
			for (int i=0; i<reels; i++)
			{
				startPos = rand.nextInt(arrSyms.length);
				
				generateReelValue(i, startPos, nextPos);
			}
		}
		//As it goes through the reel vertically,
		//each following square is assigned the symbol following the random one.
		public void generateReelValue(int i, int startPos, int nextPos)
		{
			arrResults[0][i] = arrSyms[startPos];
			for (int j=1; j<rows; j++)				
			{
				if (startPos+j>=arrSyms.length)
					{nextPos = startPos+j-arrSyms.length;}
				else
					{nextPos = startPos+j;}
				
				arrResults[j][i] = arrSyms[nextPos];
			}
		}
		
		public void checkResults()
		{
			limit = Integer.min(rows,reels);
			diagonal1 = 0;
			diagonal2 = 0;
			horizontal = new int[rows];
			for (int i=0; i<horizontal.length; i++) {horizontal[i]=0;}
			
			//Check horizontal lines: from top to bottom
			for (int i=0; i<horizontal.length; i++)
			{
				checking = arrResults[i][0]; 
				for (int j=1; j<reels; j++)
				{
					if (checking==arrResults[i][j])	{horizontal[i]++;}
				}
			}
			
			///Check Diagonal 1: top-left --> bottom-right
			checking = arrResults[0][0];
			for (int i=1; i<limit; i++)
			{
				if (checking==arrResults[i][i]) {diagonal1++;}
			}
			
			//Check Diagonal 2: top-right --> bottom-left
			checking = arrResults[0][reels-1];
			for (int i=1; i<limit; i++)
			{
				if (checking==arrResults[i][reels-1-i]) {diagonal2++;}
			}
			
		}
		public void generatePayout()
		{
			boolean horiFull = true;	//True if there is a full match in all horizontal lines.
			boolean horiFound = false;	//True if there is a full match in a horizontal line.
			int horiMatch = 0;
			
			for (int i=0; i<horizontal.length; i++)
			{
				if (horizontal[i]!=reels-1)
				{
					horiFull = false;
				}
				if (horizontal[i]==reels-1)
				{
					horiFound = true;
					horiMatch = i;
				}
			}
			
			if (diagonal1==limit-1 && diagonal2==limit-1)
			{
				P.bet *= 10;
				System.out.println("Both diagonals are a match!");
			}
			else if (diagonal1==limit-1)
			{
				P.bet *= 2;
				System.out.println("The first diagonal is a match.");
			}
			else if (diagonal2==limit-1)
			{
				P.bet *= 2;
				System.out.println("The second diagonal is a match.");
			}
			else if (horiFound)
			{
				P.bet *= 2;
				System.out.println("The "+(horiMatch+1)+" horizonal line is a match.");
			}
			else if (horiFull)
			{
				P.bet *= 20;
				gameEnter = false;
				System.out.println("You won the jackpot!!.");
			}
			else
			{
				P.bet *= 0;
				gameEnter = false;
				System.out.println("You got no matches.");
			}
		}
		public void create_Reroll()
		{
			int pos2;
			int pos1 = rand.nextInt(arrSyms.length);
			do {pos2 = rand.nextInt(arrSyms.length);} 
			while (pos2==pos1);
			
			//Generate the position of the differing reel 
			int differReel = rand.nextInt(reels);
			
			//Generate the value of all the reels
			int startPos=0, nextPos=0;
			for (int i=0; i<reels; i++)
			{
				if (i==differReel)	{startPos = pos1;}
				else				{startPos = pos2;}
				
				generateReelValue(i, startPos, nextPos);
			}
		}
		public void reroll()
		{
			if (!rerolled)
			{
				System.out.println("reroll");
				rerolled = true;
			}
		}
		
	}
 	
	
	
	
	
	
	
	
	/********** Database connection implementation **********/
	
	static final String URL = "jdbc:mysql://localhost:3306/java_connect";
	static final String USER = "root";
	static final String PASS = "root";
	
	//Inserts the values given into the database table
	public static void insertDB
		(String name, int games, String results, double spent, double prize, String type)
	{
		//Obtains the number of rows.
		String countQuery = "SELECT COUNT(*) AS total FROM results";
		
		//Inserts the values given into the database.
		String insertQuery = "INSERT INTO results(id,player,spent,game,Result,endPrize,fullDate,type) "
							+"VALUES (?,?,?,?,?,?,?,?)";
		
		//Read date to store it.
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		//All these resources will be closed after the try catch.
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASS);	//Connection to the database.
			ResultSet rs = conn.createStatement().executeQuery(countQuery);	//Executes the query.
			PreparedStatement ps = conn.prepareStatement(insertQuery);		//Prepares the query.
		){
			//Read number of rows to create the id.	
			int count = 0;
			while(rs.next())	{count = rs.getInt("total");}
			
			//Complete prepared statement.
			ps.setInt(		1, (count+1000));
			ps.setString(	2, name);
			ps.setDouble(	3, spent);
	    	ps.setInt(		4, games);
	    	ps.setString(	5, results);
	    	ps.setDouble(	6, prize);
	    	ps.setString(	7, date);
	    	ps.setString(	8, type);
			
			//Check insertion was successful.
			int m = ps.executeUpdate();
			if (m==1)	{System.out.println("Results saved");}
			else		{System.out.println("Insertion failed");}
		}
		catch (SQLException e)	{e.printStackTrace();}
		
	}
	
	//Displays the complete database table, regularly or with a script depending on the parameter
	public static void displayDB(boolean table)
	{
		try (
			Connection con = DriverManager.getConnection(URL, USER, PASS);
			ResultSet rs = con.createStatement().executeQuery
				("SELECT * FROM results ORDER BY 1 DESC LIMIT 100");
		){
			if (table)	{DBTablePrinter.printResultSet(rs);}
			else
			{
				int colnum = rs.getMetaData().getColumnCount();
	    		while (rs.next())
		    	{
		    		for(int i=1; i<=colnum; i++)
		    			{System.out.print(((i==1)? " - ":" | ")+rs.getString(i)); }
		    		System.out.println();
		    	}
			}
			
	    } catch (SQLException e) {e.printStackTrace();}
	}
	
	
	
	
	
	
	
	
	/*************** MAIN ***************/
	
	public static void main(String[] args)
	{
		int opc1;
		input = new Scanner(System.in);
		
		do {
			
			System.out.println("\n\t\t ----- MAIN MENU ----- ");
			System.out.println("\t\t Welcome to the Casino ");
			System.out.println("\nWhat do you want to do?");
			System.out.println(" 1. Play single row Slot Machine.");
			System.out.println(" 2. Play multiway Slot Machine.");
			System.out.println(" 3. Show machine differences.");
			System.out.println(" 4. Display database plainly.");
			System.out.println(" 5. Display database with script.");
			System.out.println(" 0. Exit program.\n");
			opc1 = readInput("Choose an option",0,' ',5);
			
			switch(opc1)
			{
				case 0: {} break;
				case 1: 
				{
					SingleRow M1 = new SingleRow();
					M1.menuSelect();
				}
				break;
				case 2: 
				{
					Multiway M2 = new Multiway();
					M2.menuSelect();
				}
				break;
				case 3:	{showDiff();}		break;
				case 4: {displayDB(false);}	break;
				case 5: {displayDB(true);}	break;
			}
			if (opc1!=0) 
			{
				pressAnyKeyTo("continue");
				clear();
			}
			
		} while (opc1!=0);
		input.close();
		System.out.println("\n\n\t\t ***** Game Over ***** ");
	}
	
	
	public static void showDiff()
	{
		System.out.println("\n - A \"singlerow\" slot machine\n"
						 + "   has several reels and in one row.");
		System.out.println("   Prizes are won after spinning the reels\n"
						 + "   if along the reels there are\n"
						 + "   at least two symbols matching.");
		System.out.println("\n - A \"multiway\" slot machine\n"
						 + "   has several reels and rows, not always equal.");
		System.out.println("   Prizes are won after spinning the reels\n"
				 		 + "   if along the winning lines\n"
				 		 + "   all symbols match.");
	}
	
	
	
	
	
	
	
	
}