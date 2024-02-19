import java.util.Scanner;
import java.util.ArrayList;
import java.text.DecimalFormat;
//import java.io.File;



/* TODO:
 * Multi-way re-roll functionality
 * !! symMax cannot be smaller than rows !!
 * symbol for jack-pot
 * Pay table
 * Cost of spinning and free spins
 * FileWriter class -> record results || instances of 7
 */



public class SlotMachines
{


	static Scanner input = new Scanner(System.in);
	static boolean gameEnter;	//Player input for stopping or continuing the game.
	static char gameInput;		//Player input for reading a character within the game.
	
	static final double BETMIN = 20;	//Minimum amount of money the player can bet at a time.
	static final double BETMAX = 100;	//Maximum amount of money the player can bet at a time.
	static final int WINLIMIT = 10000;	//Maximum amount of money the player can win.
	static final int GAMELIMIT = 10;	//Maximum amount of times the player can spin the reels.
	static final int TIMEWAIT = 500;	//Standard time to wait.
	static final int TIMESTOP = 400;	//Time delayed after printing a line with StopPrint.
	static final int TIMESLOW = 15;		//Time delayed between characters during SlowPrint.
	
	//All symbols that can appear on a machine
	static char[] all = {'7','A','H','K','T','*','@','^','|','%','&','\\'};
	
	
	
	
	
	
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
	public static void waitM(int ms)
	{
		try 
			{Thread.sleep(ms);}
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}
	public static void waitM(int ms, String message)
	{
		SlowPrint("\n\t"+message+".");
		waitM(ms);
	}
	// Stops the program until the player presses a key.
	public static void pressAnyKeyTo(String message)
	{
		SlowPrintln("Press any key to "+message+".");
        try 
        {
            System.in.read();
            input.nextLine();
        }
        catch(Exception e) {}
	}
	
	
	// Prints a message slowly instead of all at once.
	public static void SlowPrint(String message)
	{
		char[] chars = message.toCharArray();
		for (int i=0; i<chars.length; i++)
        {
            System.out.print(chars[i]);
            waitM(TIMESLOW);
        }
	}
	public static void SlowPrintln(String message)
	{
		SlowPrint(message);
		System.out.println();
	}
	// Prints a message and stops for a small amount of time.
	public static void StopPrint(String message)
	{
		System.out.print(message);
		waitM(TIMESTOP);
	}
	public static void StopPrintln(String message)
	{
		StopPrint(message);
		System.out.println();
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
	// Returns true if the value given exists in the array
	public static <T> boolean exists(T arr[], T value)
	{
		for (T element : arr)
		{
			if (element==value) {return true;}
		}
		return false;
	}
	// Loops until the input and type returned are valid.
	@SuppressWarnings("unchecked")
	public static <T> T readInput(String message, T param1, char dif, T param2)
	{
		if (dif=='-')		{message += " ("+param1+" - "+param2+"): ";}
		else if (dif=='/')	{message += " ("+param1+"/"+param2+"): ";}
		else if (dif=='|')	{message += " ("+param1+"): ";}
		else				{message += ": ";}
		
		//String str;
		boolean readError=true, isNumber=true;
		
		try{
			do{
				SlowPrint("\n"+message);
				String str = input.nextLine();
				if (str!="")
				{
					if (param1 instanceof String)	{return (T)str;}
					else if (param1 instanceof Character)
					{
						char ans = str.charAt(0);
						if (ans==(char)param1 || ans==Character.toUpperCase((char)param1) 
						 || ans==(char)param2 || ans==Character.toUpperCase((char)param2) )
							{return (T)(T)param1.getClass().cast(ans);}
						else {StopPrintln("  Input outside of specified range. Try again.");}
					}
					else 
					{
						//Checks that the string can be turned into a number
						for (int i=0; i<str.length() && isNumber; i++) 
						{
							if ((int)str.charAt(i)<48 || (int)str.charAt(i)>57) //ASCII: 48=0, 57=9
								{isNumber = false;}
						}
						if (isNumber)
						{
							if (param1 instanceof Integer && str.length()<9)
							{
								int ans = Integer.parseInt(str);
								if (ans>=(int)param1 && ans<=(int)param2)
									{return (T)param1.getClass().cast(ans);}
								else {StopPrintln("  Input outside of range. Try again.");}	
							}
							else if (param1 instanceof Double && str.length()<14)
							{
								double ans = Double.parseDouble(str);
								if (ans>=(double)param1 && ans<=(double)param2)
									{return (T)param1.getClass().cast(ans);}
								else {StopPrintln("  Input outside of range. Try again.");}
							}
							else {StopPrintln("  Input type not accepted. Try again.");}
						}
						else {StopPrintln("  Input type not accepted. Try again.");}
					}
				}
				else {StopPrintln("  Try again.");}
				
			}while(readError);
		}catch (ClassCastException | NumberFormatException e) {e.printStackTrace();}
		return param1;
	}
	
	
	
	
	
	
	//The symbols found in the result of spinning the reels
	public static class symFound 
	{
		private char sym;	//Character value
		private int count;	//Times the symbol appears in the Results
		private int pos;	//Position the symbol is first found in the Results
		
		public symFound()						{sym=' '; count=0; pos=-1;}
		public symFound(char s, int c, int p)	{sym = s; count=c; pos=p;}
		
		public char getSym()	{return sym;}
		public int getCount()	{return count;}
		public int getPos()		{return pos;}
		
	}
	
	
	
	
	
	
	//The players and their money
	public static class Player 
	{
		private String name;	//Name of the player
		private double bet;		//Money currently as a bet
		private double spent;	//Money introduced as a bet overall
		private int numGames;	//Number of games fully completed
		
		public Player()	{name = readInput("What is your name?","__",'_',"__");}
		
		public double getBet()	{return bet;}
		public int getGames()	{return numGames;}
		
		//Initializes the player's bet
		public void betInit(double read)	{bet=read; spent=read; numGames=0;}
		
		// Increases the bet if it stays within the limit
		public void betIncrease(double increase)
		{
			while (bet+increase > WINLIMIT) 
			{
				SlowPrintln("\nThis increase exceeds the limit by"+
						(WINLIMIT-bet-increase)+"€.");
				SlowPrintln("Your bet remains as "+bet+"€.");
				increase = readInput("Enter a lower increase",BETMIN,'-',BETMAX);
			}
			bet += increase;
			spent += increase;
			SlowPrintln("\nYour bet is now "+bet+" €.");
			pressAnyKeyTo("continue the game");
		}
		
		// Displays the money won or lost at the end of a game
		public void endMessage()
		{
			StopPrintln("\n- - - - - - - - - - - - - - -");
			SlowPrint("\n  You played "+numGames+" game"+((numGames==1)?"":"s") );
			
			if (bet==0)	
				{SlowPrintln("\n   and lost "+spent+" €.");}
			else if (bet==spent)
			{
				SlowPrintln("\n   and made back the money");
				SlowPrintln("   that you bet: "+bet+" €.");
			}
			else
			{
				SlowPrintln(".\n   You have spent "+spent+" €.");
				if (bet<spent)
					{SlowPrintln("    And left with "+bet+" €.");}
				else
					{SlowPrintln("    And have won "+bet+" €.");}
			}
			StopPrintln("\n- - - - - - - - - - - - - - -\n");
		}
	}
	
	
	
	
	
	
	//The slot machines and its types
	abstract static class Machine
	{
		protected int rows;			//Rows of the machine
		protected int reels;		//Columns of the machine, the spinning reels
		protected char[][] results;	//The results of spinning. Rows first, columns second
		
		protected int symMax;		//Amount of symbols available when spinning
		protected char[] machSyms;	//Symbols available in the machine
		
		protected int minSize;		//Minimum amount of rows and reels allowed
		protected int maxSize;		//Maximum amount of rows and reels allowed
		protected int minSyms;		//Minimum number of symbols available allowed
		protected double cost;		//Cost of spinning the reels per game
		
		protected Player P;			//Player using the machine at the moment
		
		public Machine(int ro, int re, int syms)
		{
			this.rows = ro;
			this.reels = re;
			this.symMax = syms;
			this.results = new char[rows][reels];
			this.machSyms = new char[symMax];
			
			if (this instanceof SingleRow)
			{
				this.cost = Math.ceil( ((double)reels*(double)symMax)
							*((double)3/(double)10) );
			} else {
				this.cost = Math.ceil( ((double)reels*(double)rows*(double)symMax)
							*((double)1/(double)10) );
			}
			
			char newsym;
			for (int i=0; i<symMax; i++)
			{
				do {newsym = all[(int)(Math.random()*symMax)];}
				while (exists(machSyms, newsym));
				machSyms[i] = newsym;
			}
			
		}
		
		public int getRows()	{return rows;}
		public int getReels()	{return reels;}
		public int getSyms()	{return symMax;}
		
		public void changeParameters()
		{
			int nRows, nReels, nSyms;
			
			SlowPrintln("\n\t Reels="+reels
					+ ( (this instanceof Multiway) ? (" Rows="+rows+" "):(" ") )
					+ "Symbols="+symMax);
			SlowPrintln("Input new values for these parameters.");
			if (this instanceof Multiway)
				{nRows = readInput(" Number of rows",minSize,'-',maxSize);}
			else
				{nRows = 1;}
			nReels = readInput(" Number of reels",minSize,'-',maxSize);
			nSyms = readInput(" Number of symbols",minSyms,'-',all.length);
			
			while (nSyms>nRows || nSyms>nReels)
			{
				StopPrintln("The number of symbols cannot be smaller than \n"
						+ "the number of "+((nRows<nReels)? "rows":"reels" )+"." );
				SlowPrintln("Input the amount again.");
				
				nSyms = readInput(" Number of symbols",minSyms,'-',all.length);
			}
			
			rows = nRows;
			reels = nReels;
			symMax = nSyms;
			results = new char[rows][reels];
			
			machSyms = new char[symMax];
			for (int i=0; i<symMax; i++)
				{machSyms[i] = all[i];}
			
		}
		public void displayMachine(int nShown)
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
					{System.out.printf("  %c  |", (nShown>reel) ? results[row][reel]:' ');}
		        
				System.out.print("\n  |");
		        for (int reel=0; reel<reels; reel++)
		        	{System.out.print("_____|");}
			}
			System.out.println("\n");
		}
		public void displayProgressively(int game)
		{
			for (int reelsShown=0; reelsShown<=reels; reelsShown++)
		    {
				StopPrintln("\t-- "+(game)+"º Game --");
				displayMachine(reelsShown);
				if(reelsShown<reels)
				{
					pressAnyKeyTo("spin");
					clear();
				}
				else {waitM(TIMEWAIT);}
		    }
		}
		
		public void callShowRules()
		{
			SlowPrintln("\n  Game Rules:");
			SlowPrintln("  - Every bet has to be between "+BETMIN+" and "+BETMAX+" €.");
			SlowPrintln("  - You can't play more than "+GAMELIMIT+" games.");
			SlowPrintln("  - There is a limit to how much you can win.");
			if (this instanceof SingleRow)
				{((SingleRow)this).showRules();}
			else
				{((Multiway)this).showRules();}
		}
		public void callShowProb()
		{
			if (this instanceof SingleRow)
				{((SingleRow)this).showProb();}
			else
				{((Multiway)this).showProb();}
			pressAnyKeyTo("continue");
		}
		public void callSpinReels()
		{
			P.numGames++;
			if (this instanceof SingleRow)
				{((SingleRow)this).spinReels();;}
			else
				{((Multiway)this).spinReels();}
		}
		public void callCheckResults()
		{
			if (this instanceof SingleRow)
				{((SingleRow)this).countMatches();}
			else
				{((Multiway)this).checkWinLines();}
		}
		public void callCalcPrize(Player P)
		{
			P.bet -= cost;
			if (this instanceof SingleRow)
				{((SingleRow)this).calculatePrize(P);}
			else
				{((Multiway)this).calculatePrize(P);}
		}
		
		public void menuSelect()
		{
			int opc2;
			P = new Player();
			
			do {
				clear();
				StopPrintln("\n     --- "+P.name+", Welcome to the Slot Machine ---");
				SlowPrintln("\nSpin the reels and try to match the symbols");
				SlowPrintln("The bigger your bet, the bigger a prize you'll win!!");
				
				SlowPrintln("\nThis machine has "+reels+" reels"
						+ ((this instanceof Multiway) ? (", "+rows+" rows \n"):(" ") )
						+ "and "+symMax+" possible symbols");
				SlowPrintln(" and costs "+cost+"€ to spin per turn.");
				
				SlowPrintln("\nWhat do you want to do?");
				StopPrintln(" 1. Play the game.");
				StopPrintln(" 2. View the ruleset.");
				StopPrintln(" 3. Change the parameters.");
				StopPrintln(" 0. Return to the previous menu.");
				opc2 = readInput("Choose an option",0,' ',4);
				
				switch(opc2)
				{
					case 1: {startGame();} break;
					case 3: {changeParameters();} break;
					case 2: {callShowRules();} break;
					case 4: {callShowProb();} break;
					case 0: {waitM(TIMEWAIT, "Returning to the previous menu");} break;
				}
				if (opc2!=0)
				{
					pressAnyKeyTo("continue");
					waitM(TIMEWAIT,"Returning to the slot menu");
				}
				
			} while (opc2!=0);
		}
		
		public void startGame()
		{
			P.betInit(readInput("Enter your bet",BETMIN,'-',BETMAX));
			gameEnter = true;
			
			do {
				clear();
				callSpinReels();
				displayProgressively(P.numGames);
				callCheckResults();
				callCalcPrize(P);
				
				if (gameEnter)
				{
			        gameInput = readInput("Do you want to continue playing?",'y','/','n');
			        if (gameInput=='y'||gameInput=='Y')
			        {
			        	gameInput = readInput("Do you want to bet more money?",'y','/','n');
			            if (gameInput=='y'||gameInput=='Y')
			            	{P.betIncrease(readInput("Enter the increase",BETMIN,'-',BETMAX));}
			        }
			        else {gameEnter = false;}
			    }
				
			} while ((gameEnter) && (P.getBet()<WINLIMIT) && (P.getGames()<GAMELIMIT));
			
			P.endMessage();
		}
	}
	
	
	//The machine shows one symbol of the reel at a time
	public static class SingleRow extends Machine
	{
		symFound MRS;	//Most Repeated Symbol in the results
		symFound LRS;	//Least Repeated Symbol in the results
		ArrayList<symFound> FoundArray;	//ArrayList of every different symbol found in the results
		
		public SingleRow(int nreels, int nsymbols)
		{
			super(1, nreels, nsymbols);
			minSize = 4; 
			maxSize = 8;
			minSyms = 4;
		}
		public SingleRow()	{this(4, 6);}
		
		public void showRules()		//Rules and outcomes in playing a single row slot machine
		{
			SlowPrintln("\n  Prizes available:");
			SlowPrintln("  - All reels match : jackpot.");
			SlowPrintln("  - Almost all reels match : chance at all or nothing.");
			SlowPrintln("  - More than half the reels match : win some money.");
			SlowPrintln("  - Less than half the reels match : lose some money.");
			SlowPrintln("  - Half of the reels match : keep your bet.");
			SlowPrintln("  - No matches : you lose.");
		}
		public void showProb()		//Probability of each outcome in a single row slot machine
		{
			DecimalFormat formatter = new DecimalFormat("#0.0000");
			double probAll;		//All of the reels match.
			double probAlmost;	//All of the reels except one match.
			double probMH=0; 	//More than half the reels match.
			double probHalf;	//Half of the reels match.
			double probLH=0;	//Less than half the reels match.
			double probNone=1;	//Not a single match.
			int half = (int)Math.ceil((double)reels/2);
			
			probAll		= Math.pow( ((double)1/symMax), reels );
			probAlmost	= Math.pow( ((double)1/symMax), reels-1 );
			probHalf	= Math.pow( ((double)1/symMax), half );
			for (int i=half+1; i<=reels; i++)	{probMH += Math.pow( ((double)1/symMax), i );}
			for (int i=2; i<half; i++)			{probLH += Math.pow( ((double)1/symMax), i );}
			for (int i=0; i<reels; i++)			{probNone *= ((double)(reels-i)/symMax);}
			
			SlowPrintln("\n  Probability:");
			SlowPrintln("  - All reels \t"+formatter.format(probAll*100)+" %");
			SlowPrintln("  - Almost all \t"+formatter.format(probAlmost*100)+" %");
			SlowPrintln("  - > Half \t"+formatter.format(probMH*100)+" %");
			SlowPrintln("  - = Half \t"+formatter.format(probHalf*100)+" %");
			SlowPrintln("  - < Half \t"+formatter.format(probLH*100)+" %");
			SlowPrintln("  - No match \t"+formatter.format(probNone*100)+" %");
		}
		
		public void spinReels()		//Generates a value for the results
		{
			for (int i=0; i<reels; i++)
				{results[0][i] = machSyms[(int)(Math.random()*symMax)];}
		}
		public void countMatches()	//For every row and check for matching symbols
		{
			boolean exists;
			MRS = new symFound(' ',0,-1);
			LRS = new symFound(' ',reels,-1);
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
		public void calculatePrize(Player P)
		{
			if (MRS.count==reels-1) {reroll();}
			
			if (MRS.count==1)
		    {
		    	P.bet = 0;
		    	gameEnter = false;
		    	SlowPrintln("You got no matches.");
		    }
		    else if (MRS.count==reels)
			{
		    	P.bet *= 100;
				gameEnter = false;
				SlowPrintln("You won the jackpot!!!");
			}
			else
			{
				SlowPrintln("You got the "+MRS.sym+" symbol "+MRS.count+" times.");
				if (MRS.count>reels/2)
		        {
					P.bet *= 10;
					SlowPrintln("You have gained money and now have "+P.bet+" €.");
				}
				else if (MRS.count<reels/2)
		        {
					P.bet *= 0.5;
					SlowPrintln("You have lost money and now have "+P.bet+" €.");
				}
				else
					{SlowPrintln("You still have "+P.bet+" €.");}
			}
			
			if (P.numGames==GAMELIMIT) 
			{
				gameEnter = false;
				SlowPrintln("\n  You have reached the maximum amount of numGames.");
			}
			if (P.bet>WINLIMIT)
			{
				P.bet = WINLIMIT;
				gameEnter = false;
				SlowPrintln("\n\t You have reached the maximum amount");
				SlowPrintln("\t of money that can be awarded.");
				SlowPrintln("\t You will recieve that instead.");
			}
		}
		public void reroll()		//Re-roll when one reel is left for the jackpot
		{
			SlowPrintln(" You are one reel away from the jackpot.");
			SlowPrintln(" You can reroll for the chance to get all matches,");
			SlowPrintln(" but if you fail you will loose all your money.");
			
			gameInput = readInput("Do you want to reroll the "+(LRS.pos+1)+"º reel?",'y','/','n');
			if (gameInput=='y' || gameInput=='Y')
			{
				//results[LRS.pos.get(0)] = spinReel();
				displayMachine(results.length);
				
				if (results[0][MRS.pos]==results[0][LRS.pos])
				{
					SlowPrintln("The new symbol is a match.\n");
					MRS.count = results.length;
				}
				else
				{
					SlowPrintln("The new symbol is still different.\n");
					MRS.count = 1;
				}
			}
			System.out.println();
		}
		
	}
	
	
	//The machine shows several symbols of the reel
	public static class Multiway extends Machine
	{
		int limit;			//Number of reels or rows, whichever one is smaller
		char checking;		//Character used to check for the winning lines
		int diagonal1;		//Number of symbols matching along the first diagonal line
		int diagonal2;		//Number of symbols matching along the second diagonal line
		int[] horizontal;	//Numbers of symbols matching along the horizontal lines
		
		public Multiway(int nRows, int nReels, int nSymbols)
		{
			super(nRows, nReels, nSymbols);
			minSize = 3;
			maxSize = 7;
			minSyms = 4;
			limit = Integer.min(rows,reels);
		}
		public Multiway(int size, int nSymbols)	{this(size, size, nSymbols);}
		public Multiway()						{this(5, 5, 5);}
		
		public void showRules()		//Rules and outcomes in playing a multi-way slot machine
		{
			SlowPrintln("\n  Prizes available:");
			SlowPrintln("  - All lines match: jackpot");
			SlowPrintln("  - Both diagonals match: win a lot of money.");
			SlowPrintln("  - Any diagonal match: win some money.");
			SlowPrintln("  - Any horizontal match: win some money");
			SlowPrintln("  - No lines match: you lose.");
		}
		public void showProb()		//Probability of each outcome in a multi-way slot machine
		{
			DecimalFormat formatter = new DecimalFormat("#0.0000");
			double probAll=0;	//All lines match.
			double probDiags=0;	//Both diagonals match.
			double probDiag=0;	//One of the diagonals match.
			double probHori=0;	//Any horizontal match.
			double probNone=1;	//No lines match.
			
			probDiag 	= Math.pow( ((double)1/symMax), limit );
			probDiags	= probDiag*probDiag;
			
			probHori	= Math.pow( ((double)1/symMax), reels );
			probAll		= probHori*rows;
			
			probNone	= (1-probDiag)*(1-probHori);
			
			SlowPrintln("\n  Probability:");
			SlowPrintln("  - All lines    \t"+formatter.format(probAll*100)+" %");
			SlowPrintln("  - Both diagonals \t"+formatter.format(probDiags*100)+" %");
			SlowPrintln("  - One diagonal \t"+formatter.format(probDiag*100)+" %");
			SlowPrintln("  - One horizontal \t"+formatter.format(probHori*100)+" %");
			SlowPrintln("  - No lines     \t"+formatter.format(probNone*100)+" %");
		}
		
		public void	spinReels()		//Generates symbols in the reels in the intended order
		{
			for (int i=0; i<reels; i++)
			{
				int next, pos = (int)Math.random()*symMax;
				results[0][i] = all[pos];
				for (int j=0; j<rows; j++)
				{
					if (pos+j >= rows)	{next = pos+j-rows;}
					else				{next = pos+j;}
					
					results[0+j][i] = machSyms[next];
				}
			}
		}
		public void checkWinLines()	//Checks all the winning lines
		{
			diagonal1 = 0;
			diagonal2 = 0;
			horizontal = new int[rows];
			for (int i=0; i<rows; i++) {horizontal[i]=0;}
			
			//Diagonal 1: top-left --> bottom-right
			checking = results[0][0];
			for (int i=1; i<limit; i++)
			{
				if (checking==results[i][i]) {diagonal1++;}
			}
			
			//Diagonal 2: top-right --> bottom-left
			checking = results[0][reels-1];
			for (int i=1; i<limit; i++)
			{
				if (checking==results[i][reels-1-i]) {diagonal2++;}
			}
			
			//Horizontal lines: from top to bottom
			for (int i=0; i<rows; i++)
			{
				checking = results[i][0];
				for (int j=1; j<reels; j++)
				{
					if (checking==results[i][j]) {horizontal[i]++;}
				}
			}
		}
		public void calculatePrize(Player P)	//..,Calculates the prize based on the winning lines
		{
			boolean fullHorizontal = true;
			for (int i=0; i<horizontal.length; i++)
			{
				if (horizontal[i]!=reels)	{fullHorizontal = false;}
			}
			if (fullHorizontal)
			{
				P.bet *= 100;
				gameEnter = false;
				SlowPrintln("You won the jackpot!!!");
			}
			else if (diagonal1==reels && diagonal2==reels)
			{
				P.bet *= 10;
				SlowPrintln("You got winning lines on both diagonals!");
			}
			else if (diagonal1==reels)
			{
				P.bet *= 2;
				SlowPrintln("You got a winning line on the first diagonal");
			}
			else if (diagonal2==reels)
			{
				P.bet *= 2;
				SlowPrintln("You got a winning line on the second diagonal");
			}
			else
			{
				SlowPrintln("You got no winning lines.");
			}
		}
		public void reroll()		//...Re-roll when one reel is left for the jackpot
		{
			//
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args)
	{
		int opc1;
		
		do {
			
			StopPrintln("\n\t\t ----- MAIN MENU ----- ");
			StopPrintln("\t\t Welcome to the Casino ");
			SlowPrintln("\nWhat do you want to do?");
			StopPrintln(" 1. Single row slot machine.");
			StopPrintln(" 2. Multiway slot machie.");
			StopPrintln(" 3. Options.");
			StopPrintln(" 0. Exit.");
			opc1 = readInput("Choose an option",0,' ',3);
			
			switch(opc1)
			{
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
				case 3:
				{
					//Machine M3 = new Machine(5,5,5);
				}
				break;
				case 0: {waitM(TIMEWAIT,"Closing the game");} break;
			}
			if (opc1!=0) {clear();}
			
			
		} while (opc1!=0);
		
		System.out.println("\n\n\t\t ***** Game Over ***** ");
		input.close();
	}





}