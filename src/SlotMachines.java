import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.text.DecimalFormat;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;








public class SlotMachines
{


	static FileWriter output;						//For saving the arrResults in a record
	static File filename = new File("record.txt");	//The arrResults record
	static Random rand = new Random();				//For generating random numbers
	static Scanner input = new Scanner(System.in);	//For reading player inputs
	
	static boolean gameEnter;	//Player input for stopping or continuing the game.
	static char gameInput;		//Player input for reading a character within the game.
	
	static final double BETMIN = 20;	//Minimum amount of money the player can bet at a time.
	static final double BETMAX = 100;	//Maximum amount of money the player can bet at a time.
	static final int WINLIMIT = 10000;	//Maximum amount of money the player can win.
	static final int GAMELIMIT = 10;	//Maximum amount of times the player can spin the reels.
	static final int TIMEWAIT = 400;	//Standard time to wait.
	//All symbols that can appear on a machine
	static final char[] all = {'7','A','H','K','T','*','@','^','|','%','&','\\'};
	
	
	
	
	
	
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
	// Stops the program until the player presses a key.
	public static void pressAnyKeyTo(String message)
	{
		System.out.println("\nPress any key to "+message+".");
        try 
        {
            System.in.read();
            input.nextLine();
        }
        catch(Exception e) {}
	}
	// Closes the connection to the output file
	public static void outputClose()
	{
		try {
			output.close();
		} catch (IOException e) {e.printStackTrace();}
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
	public static <T> boolean exists1(T[] arr, T value)
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
				System.out.print("\n"+message);
				String str = input.nextLine();
				if (str!="")
				{
					if (param1 instanceof String)
					{
						return (T)str;
					}
					else if (param1 instanceof Character)
					{
						char ans = str.charAt(0);
						if (ans==(char)param1 || ans==Character.toUpperCase((char)param1) 
						 || ans==(char)param2 || ans==Character.toUpperCase((char)param2) )
							{return (T)param1.getClass().cast(ans);}
						else
							{System.out.println("  Input outside of specified range. Try again.");}
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
								else
									{System.out.println("  Input outside of range. Try again.");}	
							}
							else if (param1 instanceof Double && str.length()<14)
							{
								double ans = Double.parseDouble(str);
								if (ans>=(double)param1 && ans<=(double)param2)
									{return (T)param1.getClass().cast(ans);}
								else 
									{System.out.println("  Input outside of range. Try again.");}
							}
							else
								{System.out.println("  Input type not accepted. Try again.");}
						}
						else {System.out.println("  Input type not accepted. Try again.");}
					}
				}
				else {System.out.println("  Try again.");}
				
			}while(readError);
		}catch (ClassCastException | NumberFormatException e) {e.printStackTrace();}
		return param1;
	}
	
	
	
	
	
	
	//The slot machines' characteristics and actions regardless of type.
	abstract static class Machine
	{
		protected char[][] arrResults;	//Array of the results of spinning. [Rows][Columns]
		protected char[] arrSyms;		//Array of the symbols available to this machine.
		protected int rows;			//Number of rows of the machine.
		protected int reels;		//Number of columns or reels of the machine.
		
		protected int minSize, maxSize;	//Minimum and maximum amount of rows and reels allowed
		protected int minSyms, maxSyms;	//Minimum and maximum number of symbols available allowed
		
		protected int numGames;		//Number of games fully completed
		protected double cost;		//Cost of spinning the reels per game
		protected double bet;		//Money currently as a bet
		protected double spent;		//Money introduced as a bet overall
		
		public Machine(int ro, int re, int syms)
		{
			this.rows = ro;
			this.reels = re;
			this.arrResults = new char[rows][reels];
			assignSymbols(syms);
			assignSpinCost();
		}
		
		public void assignSymbols(int size)	//Gives values to the array of available symbols.
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
		public void assignSpinCost()		//Gives value to the cost of spinning the reels.
		{
			double mod;
			if (this instanceof SingleRow)	{mod = 0.4;} 
			else							{mod = 0.1;}
			
			this.cost = Math.ceil( (double)reels * (double)rows * (double)arrSyms.length * mod );
		}
		public void changeParameters()		//Changes the rows, reels and symbols of the machine.
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
			
			this.rows = nRows;
			this.reels = nReels;
			this.arrResults = new char[rows][reels];
			assignSymbols(nSyms);
			assignSpinCost();
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
		public void displayAndSpin()		//Displays the reels as they are spinning.
		{
			for (int reelsShown=0; reelsShown<=reels; reelsShown++)
		    {
				System.out.println("\t-- "+(numGames)+"º Game --");
				displayReels(reelsShown);
				if(reelsShown<reels)
				{
					pressAnyKeyTo("spin the "+(reelsShown+1)+"º reel");
					clear();
				}
		    }
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
		
		public void showRules()				//Shows the rules and outcomes of playing.
		{
			System.out.println("\n  Game Rules:");
			System.out.println("  - Every bet has to be between "+BETMIN+" and "+BETMAX+" €.");
			System.out.println("  - You can't play more than "+GAMELIMIT+" games.");
			System.out.println("  - There is a limit to how much you can win.");
		}
		public void showProb()				//Shows the probability of each outcome.
		{
			System.out.println("\n  Probability:");
		}
		
		public void betInit(double read)	//Initializes the player's bet.
			{bet=read; spent=read; numGames=0;}
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
			try {
				output = new FileWriter(filename,true);
				output.write("    Bet increased by "+inc+"\n");
			    output.flush();
		    } catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		    }
			pressAnyKeyTo("continue the game");
		}	
		
		public void saveStart()				//Saves the date, time and bet in the record file.
		{
			try {
				output = new FileWriter(filename,true);
			    
				output.write("\n"+
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"))
				+"\n");
			    
			    if (this instanceof Multiway) {output.write(" Rows = "+rows+", ");}
			    output.write(" Reels = "+reels);
				output.write("\tSymbols = "+arrSyms.length+"\n");
				output.write(" Cost of spinning = "+cost+"\n");
			    output.flush();
			  
		    } catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		    }
		}
		public void saveResults()			//Saves the arrResults of spinning in the record file.
		{
			try {
				output = new FileWriter(filename,true);
				output.write("   Bet: "+bet+"\n");
				for (int i=0; i<rows; i++)
				{
					if (i==0)	{output.write("   Game "+numGames+": \t");}
					else		{output.write("\t\t");}
					for (int j=0; j<reels; j++)
						{output.write(" ["+arrResults[i][j]+"]");}	
					output.write("\n");
				}
				output.flush();
		    } catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		    }
		}
		public void saveEnd()				//Saves the money involved in the record file.
		{
			try {
				output = new FileWriter(filename,true);
				output.write(" Prize: "+bet+"\n");
				output.write(" Spent: "+spent+"\n");
				output.flush();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		    }
		}
		
		public void menuSelect()
		{
			int opc2;
			
			do {
				clear();
				System.out.println("\n\t  --- Welcome to the Slot Machine ---");
				System.out.println("\nSpin the reels and try to match the symbols");
				System.out.println("The bigger your bet, the bigger a prize you'll win!!");
				//displayMachine(0);
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
					case 3: {changeParameters();} break;
					case 2: {showRules();} break;
					case 4: {showProb();} break;
					case 0: {} break;
					case 1: {
						betInit(readInput("Enter your bet",BETMIN,'-',BETMAX));
						saveStart();
						gameEnter = true;
						
						do {
							clear();
							Game();
							saveResults();
							if (gameEnter)
							{
						        gameInput = readInput("Do you want to continue playing?",'y','/','n');
						        if (gameInput=='y'||gameInput=='Y')
						        {
						        	gameInput = readInput("Do you want to bet more money?",'y','/','n');
						            if (gameInput=='y'||gameInput=='Y')
						            	{betIncrease(readInput("Enter the increase",BETMIN,'-',BETMAX));}
						        }
						        else {gameEnter = false;}
						    }
							saveEnd();
							
						} while ((gameEnter) && (bet<WINLIMIT) && (numGames<GAMELIMIT));
						
						endMessage();
						
					} break;
				}
				if (opc2!=0) {pressAnyKeyTo("return to the slot menu");}
				
			} while (opc2!=0);
			
			
		}
		
		public void Game()
		{
			numGames++;
			bet -= cost;
			if (this instanceof SingleRow)
			{
				((SingleRow)this).spinReels();
				displayAndSpin();
				((SingleRow)this).checkResults();
				((SingleRow)this).calculatePrize();
			}
			else
			{
				((Multiway)this).spinReels();
				displayAndSpin();
				((Multiway)this).checkResults();
				((Multiway)this).calculatePrize();
			}
		}
	}
	
	
	//The actions that vary between the type of slot machine.
	public static interface Actions
	{
		public void spinReels();		//Spins the reels and gives value to the results
		public void checkResults();		//Checks the winning conditions of each machine
		public void calculatePrize();	//Calculates the prize based on the conditions of each machine
		public void reroll();			//Allow to re-calculate if close to the maximum prize
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
		
		public SingleRow(int nreels, int nsymbols)
		{
			super(1, nreels, nsymbols);
			minSize = 4;	maxSize = 8;
			minSyms = 4;	maxSyms = 8;
		}
		public SingleRow()	{this(4, 6);}
		
		@Override
		public void showRules()
		{
			super.showRules();
			System.out.println("\n  Prizes available:");
			System.out.println("  - All reels match : jackpot.");
			System.out.println("  - Almost all reels match : chance at all or nothing.");
			System.out.println("  - More than half the reels match : win some money.");
			System.out.println("  - Less than half the reels match : lose some money.");
			System.out.println("  - Half of the reels match : keep your bet.");
			System.out.println("  - No matches : you lose.");
		}
		@Override
		public void showProb()
		{
			super.showProb();
			double probAll;		//All of the reels match.
			double probAlmost;	//All of the reels except one match.
			double probMH=0; 	//More than half the reels match.
			double probHalf;	//Half of the reels match.
			double probLH=0;	//Less than half the reels match.
			double probNone=1;	//Not a single match.
			int half = (int)Math.ceil((double)reels/2);
			DecimalFormat formatter = new DecimalFormat("#0.0000");
			
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
		
		public void playGame()
		{
			numGames++;
			spinReels();
			displayAndSpin();
			checkResults();
			calculatePrize();
		}
		public void spinReels()
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
		public void calculatePrize()
		{
			
			if (MRcount==reels-1) {reroll();}
			
			if (MRcount==1)
		    {
		    	bet = 0;
		    	gameEnter = false;
		    	System.out.println("You got no matches.");
		    }
		    else if (MRcount==reels)
			{
		    	bet *= 100;
				gameEnter = false;
				System.out.println("You won the jackpot!!!");
			}
			else
			{
				System.out.println("You got the "+MRsym+" symbol "+MRcount+" times.");
				if (MRcount>reels/2)
		        {
					bet *= 10;
					System.out.println("You have gained money and now have "+bet+" €.");
				}
				else if (MRcount<reels/2)
		        {
					bet *= 0.5;
					System.out.println("You have lost money and now have "+bet+" €.");
				}
				else
					{System.out.println("You still have "+bet+" €.");}
			}
			
			if (numGames==GAMELIMIT) 
			{
				gameEnter = false;
				System.out.println("\n  You have reached the maximum amount of numGames.");
			}
			if (bet>WINLIMIT)
			{
				bet = WINLIMIT;
				gameEnter = false;
				System.out.println("\n\t You have reached the maximum amount");
				System.out.println("\t of money that can be awarded.");
				System.out.println("\t You will recieve that instead.");
			}
		}
		public void reroll()
		{
			System.out.println(" You are one reel away from the jackpot.");
			System.out.println(" You can reroll for the chance to get all matches,");
			System.out.println(" but if you fail you will loose all your money.");
			
			gameInput = readInput("Do you want to reroll the "+(LRpos+1)+"º reel?",'y','/','n');
			if (gameInput=='y' || gameInput=='Y')
			{
				//arrResults[LRS.pos.get(0)] = spinReel();
				displayReels(arrResults.length);
				
				if (arrResults[0][MRpos]==arrResults[0][LRpos])
				{
					System.out.println("The new symbol is a match.\n");
					MRcount = arrResults.length;
				}
				else
				{
					System.out.println("The new symbol is still different.\n");
					MRcount = 1;
				}
			}
			System.out.println();
		}
		
	}
	
	
	//Type of slot machine that shows several symbols of the reel
	public static class Multiway extends Machine implements Actions
	{
		protected int limit;		//Number of reels or rows, whichever one is smaller.
		protected char checking;	//Character used to check for the winning lines.
		
		protected int diagonal1;	//Number of symbols matching along the first diagonal line.
		protected int diagonal2;	//Number of symbols matching along the second diagonal line.
		
		protected int[] horizontal;	//Numbers of symbols matching along the horizontal lines.
		protected int horiMatch;	//Position of the horizontal line with matching symbols.
		protected int missingPos;	//Position of the symbol left to match whole line.
		
		public Multiway(int nRows, int nReels, int nSymbols)
		{
			super(nRows, nReels, nSymbols);
			minSize = 3;	maxSize = 7;
			minSyms = 4;	maxSyms = 8;
			horiMatch = missingPos = -1;
			limit = Integer.min(rows,reels);
		}
		public Multiway(int size, int nSymbols)	{this(size, size, nSymbols);}
		public Multiway()						{this(5, 5, 5);}
		
		@Override
		public void showRules()
		{
			super.showRules();
			System.out.println("\n  Prizes available:");
			System.out.println("  - All lines match: jackpot");
			System.out.println("  - Both diagonals match: win a lot of money.");
			System.out.println("  - Any diagonal match: win some money.");
			System.out.println("  - Any horizontal match: win some money");
			System.out.println("  - No lines match: you lose.");
		}
		@Override
		public void showProb()
		{
			double probAll=0;	//All lines match.
			double probDiags=0;	//Both diagonals match.
			double probDiag=0;	//One of the diagonals match.
			double probHori=0;	//Any horizontal match.
			double probNone=1;	//No lines match.
			DecimalFormat formatter = new DecimalFormat("#0.0000");
			
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
		
		public void playGame()
		{
			numGames++;
			spinReels();
			displayAndSpin();
			checkResults();
			calculatePrize();
		}
		public void	spinReels()
		{
			int next=0, pos=0;
			for (int i=0; i<reels; i++)
			{
				pos = rand.nextInt(arrSyms.length);
				arrResults[0][i] = arrSyms[pos];
				for (int j=1; j<rows; j++)
				{
					next = pos+j-((pos+j>=reels)? reels:0 );
					
					arrResults[j][i] = arrSyms[next];
				}
			}
		}
		public void checkResults()
		{
			diagonal1 = 0;
			diagonal2 = 0;
			horizontal = new int[rows];
			for (int i=0; i<horizontal.length; i++) {horizontal[i]=0;}
			
			//Diagonal 1: top-left --> bottom-right
			checking = arrResults[0][0];
			for (int i=1; i<limit; i++)
			{
				if (checking==arrResults[i][i]) {diagonal1++;}
				else {missingPos = i;}
			}
			if (diagonal1!=reels-1) {missingPos=-1;}
			
			//Diagonal 2: top-right --> bottom-left
			checking = arrResults[0][reels-1];
			for (int i=1; i<limit; i++)
			{
				if (checking==arrResults[i][reels-1-i]) {diagonal2++;}
			}
			
			//Horizontal lines: from top to bottom
			for (int i=0; i<rows; i++)
			{
				checking = arrResults[i][0];
				for (int j=1; j<reels; j++)
				{
					if (checking==arrResults[i][j]) {horizontal[i]++;}
				}
			}
		}
		public void calculatePrize()
		{
			boolean fullHorizontal = true;
			for (int i=0; i<horizontal.length; i++)	//Checks if all horizontal lines match
			{
				if (horizontal[i]!=reels)		
				{
					fullHorizontal = false;		//If not, checks if the line being checked
					if (horizontal[i]==reels-1)		//can be re-rolled
						{reroll();}
					else if (horizontal[i]==reels)	//or matches by itself, only once
						{if (horiMatch==-1) {horiMatch = i;}}
				}
			}
			if (fullHorizontal)
			{
				bet *= 100;
				System.out.println("You won the jackpot!!!");
			}
			else if (diagonal1==reels-1 || diagonal2==reels-1)
			{
				reroll();
			}
			else if (diagonal1==reels && diagonal2==reels)
			{
				bet *= 10;
				System.out.println("You got winning lines on both diagonals!");
			}
			else if (diagonal1==reels)
			{
				bet *= 10;
				System.out.println("You got a winning line on the first diagonal");
			}
			else if (diagonal2==reels)
			{
				bet *= 10;
				System.out.println("You got a winning line on the second diagonal");
			}
			else
			{
				gameEnter = false;
				System.out.println("You got no winning lines.");
			}
		}
		public void reroll()
		{
			if (diagonal1==reels-1)
			{
				//
			}
			if (diagonal2==reels-1)
			{
				//
			}
			if (horizontal[horiMatch]==reels-1)
			{
				//
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args)
	{
		int opc1;
		
		do {
			
			System.out.println("\n\t\t ----- MAIN MENU ----- ");
			System.out.println("\t\t Welcome to the Casino ");
			System.out.println("\nWhat do you want to do?");
			System.out.println(" 1. Play single row Slot Machine.");
			System.out.println(" 2. Play multiway Slot Machine.");
			System.out.println(" 3. Show simple instructions.");
			System.out.println(" 0. Exit.");
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
					System.out.println("\n· A \"singlerow\" slot machine\n"
									 + "  has several reels and in one row.");
					System.out.println("· Prizes are obtained after spinning the reels\n"
									 + "  if along the reels there are\n"
									 + "  at least two symbols matching.");
					System.out.println("\n· A \"multiway\" slot machine\n"
									 + "  has several reels and row, not always equal.");
					System.out.println("· Prizes are obtained after spinning the reels\n"
									 + "  if along the winning lines\n"
									 + "  all symbols match.");
				}
				break;
				case 0: {} break;
			}
			if (opc1!=0) 
			{
				pressAnyKeyTo("continue");
				clear();
			}
			
		} while (opc1!=0);
		
		input.close();
		outputClose();
		System.out.println("\n\n\t\t ***** Game Over ***** ");
	}







}