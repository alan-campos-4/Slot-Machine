
//		----- ----- ----- -----  ----- ----- ----- -----
//		----- ----- ----- 1º TRIMESTRE ----- ----- -----
//		----- ----- ----- -----  ----- ----- ----- -----


import java.util.Scanner;
import java.util.ArrayList;
import java.text.DecimalFormat;





public class SlotMachine
{

	// Clears the terminal output.
	public static void clear()
	{
		// "Works" in the Eclipse IDE Console 
		//
		System.out.println("\n\n\n\n\n");  //5
        System.out.println("\n\n\n\n\n");  //10
        System.out.println("\n\n\n\n\n");  //15
        System.out.println("\n\n\n\n\n");  //20
        System.out.println("\n\n\n\n\n");  //25
        //System.out.flush(); 
        //
        
		// Works in Windows Powershell
        //
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
		//
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






	static Scanner input = new Scanner(System.in);
	static boolean gameEnter;	//Player input for stopping or continuing the game.
	static char gameInput;		//Player input for reading a character within the game.
	
	final static double betmin=20;	//Minimum amount of money the player can bet at a time.
	final static double betmax=100;	//Maximum amount of money the player can bet at a time.
	final static int gameLimit=10;	//Maximum amount of times the player can spin the reels.
	final static int winLimit=10000;//Maximum amount of money the player can win.
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbs=4;	//Number of available symbols when spinning.
	//All of the possible symbols that can be obtained by spinning.
	static char[] symbols = {'~','#','@','/','\\','|','_','%','&'};
	static char[] Results;	//Array of the results of spinning all the reels in the machine.
	static ResSymbol MRS;	//Most Repeated Symbol in the results
	static ResSymbol LRS;	//Least Repeated Symbol in the results
	
	static ArrayList<ResSymbol> FoundArray = new ArrayList<ResSymbol>();
	
	
	public static class ResSymbol //Symbol found in the Results
	{
		private char sym;	//Character value
		private int count;	//Times the symbol appears in the Results
		//Positions the symbol is found in the Results
		private ArrayList<Integer> pos = new ArrayList<Integer>();
		
		public ResSymbol()						{sym=' '; count=0;}
		public ResSymbol(char s, int c)			{sym=s; count=c;}
		public ResSymbol(char s, int c, int p)	{sym=s; count=c; pos.add(p);}
		
		public void set(char s, int c, int p)	{sym=s; count=c; pos.add(p);}
		
		public char getSym()	{return sym;}
		public int getCount()	{return count;}
		public int getPos(int p){return pos.get(p);}
	}


	public static class Player //The player and their money
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
			if (MRS.count==Results.length-1) {reroll();}
			
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
			}
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








	// Returns a random character to assign to the results.
	public static char spinReel()
		{return symbols[(int)(Math.random()*nSymbs)];}


	// Displays the slot machine with the given number of reels visible.
	public static void displayMachine(int nShown)
	{
		System.out.print("  ");
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.print(" _____");}
		
		System.out.print("\n  |");
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.print("     |");}
		
		System.out.print("\n  |");
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.printf("  %c  |", (nShown>slot) ? Results[slot]:' ');}
        
		System.out.print("\n  |");
        for (int slot=0; slot<Results.length; slot++) 
        	{System.out.print("_____|");}
       
        System.out.println("\n");
	}


	// Counts the symbols in the results to find the most and least repeated.
	public static void countSymbolsFound()
	{
	    MRS = new ResSymbol(' ',0);
        LRS = new ResSymbol(' ',Results.length);
        boolean exists;
		
		//ArrayList<ResSymbol> FoundArray = new ArrayList<ResSymbol>();
		for (int i=0; i<Results.length; i++)
		{
			exists = false;
			if (!FoundArray.isEmpty())
			{
				for (int j=0; j<FoundArray.size() && !exists; j++)
				{
					if (Results[i]==FoundArray.get(j).getSym())
						{exists = true;}
	 			}
			}
			if (!exists)
			{
				ResSymbol NewSym = new ResSymbol(Results[i], 0);
				for (int j=0; j<Results.length; j++)
                {
                    if (Results[i]==Results[j])
                    	{NewSym.count++; NewSym.pos.add(j);}
                }
				FoundArray.add(NewSym);
                if (NewSym.count > MRS.count)	{MRS = NewSym;}
                if (NewSym.count < LRS.count)	{LRS = NewSym;}
            }
		}
	}


	// Re-rolls the "missing" reel and changes the results accordingly.
	public static void reroll()
	{
		System.out.println(" You are one reel away from the jackpot.");
		System.out.println(" You can reroll for the chance to get all matches,");
		System.out.println(" but if you fail you will loose all your money.");
		
		gameInput = readInput("Do you want to reroll the "+(LRS.pos.get(0)+1)+"º reel?",'y','/','n');
		if (gameInput=='y' || gameInput=='Y')
		{
			Results[LRS.pos.get(0)] = spinReel();
			displayMachine(Results.length);
			
			if (Results[MRS.pos.get(0)]==Results[LRS.pos.get(0)])
			{
				System.out.println("The new symbol is a match.\n");
				MRS.count = Results.length;
			}
			else
			{
				System.out.println("The new symbol is still different.\n");
				MRS.count = 1;
			}
		}
		System.out.println();
	}






	// Shows the rules of the game.
	public static void showRules() 
	{
		System.out.println("\n  Game Rules:");
		System.out.println("  - Every bet has to be between "+betmin+" and "+betmax+" €.");
		System.out.println("  - You can't play more than "+gameLimit+" games.");
		System.out.println("  - There is a limit to how much you can win.");
		System.out.println("\n  Prizes available:");
		System.out.println("  - All reels match : jackpot.");
		System.out.println("  - Almost all reels match : chance at all or nothing.");
		System.out.println("  - More than half the reels match : win some money.");
		System.out.println("  - Less than half the reels match : lose some money.");
		System.out.println("  - Half of the reels match : keep your bet.");
		System.out.println("  - No matches : you lose.");
	}


	// Shows the probability of each outcome of the game.
	public static void showProb() 
	{
		DecimalFormat formatter = new DecimalFormat("#0.0000");
		double probAll;		//All of the reels match.
		double probAlmost;	//All of the reels except one match.
		double probMH=0; 	//More than half the reels match.
		double probHalf;	//Half of the reels match.
		double probLH=0;	//Less than half the reels match.
		double probNone=1;	//Not a single match.
		int half = (int)Math.ceil((double)nReels/2);
		
		probAll		= Math.pow( ((double)1/nSymbs), nReels );
		probAlmost	= Math.pow( ((double)1/nSymbs), nReels-1 );
		probHalf	= Math.pow( ((double)1/nSymbs), half );
		for (int i=half+1; i<=nReels; i++)	{probMH += Math.pow( ((double)1/nSymbs), i );}
		for (int i=2; i<half; i++)			{probLH += Math.pow( ((double)1/nSymbs), i );}
		for (int i=0; i<nReels; i++)		{probNone *= ((double)(nReels-i)/nSymbs);}
		
		System.out.println("\n  Probability:");
		System.out.println("  - All reels \t"+formatter.format(probAll*100)+" %");
		System.out.println("  - Almost all \t"+formatter.format(probAlmost*100)+" %");
		System.out.println("  - > Half \t"+formatter.format(probMH*100)+" %");
		System.out.println("  - = Half \t"+formatter.format(probHalf*100)+" %");
		System.out.println("  - < Half \t"+formatter.format(probLH*100)+" %");
		System.out.println("  - No match \t"+formatter.format(probNone*100)+" %");
	}


	// Starts the slot machine.
	public static void game() 
	{
		Player P1 = new Player(readInput("Enter your bet",betmin,'-',betmax));
		gameEnter = true;
		Results = new char[nReels];
			
		do {
			
			for (int reels=0; reels<=Results.length; reels++)
		    {
				clear();
		        System.out.println("\t-- "+(P1.getGames()+1)+"º Game --");
		        displayMachine(reels);
		        if (reels<Results.length)	{Results[reels] = spinReel();}
		        if (reels==0)				{pressAnyKeyTo("start spinning");}
		        wait(400+reels*50);
		    }
			
		    countSymbolsFound();
		    
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




	public static void main(String[] args)
	{
		int opc;
		
		do {
			System.out.println("\n\t --- Welcome to the SLOT MACHINE --- ");
			System.out.println("Bet an some money and try to win a prize!!");
			System.out.println("This machine has "+nReels+" reels and "+nSymbs+" symbols.");
			
			System.out.println("\nWhat do you want to do?");
			System.out.println(" 1. Play the game.");
			System.out.println(" 2. View the ruleset.");
			System.out.println(" 3. Change the parameters.");
			System.out.println(" 0. Exit.");
			
			opc = readInput("Choose an option",0,' ',4);
			switch(opc)
			{
				case 0: {}				break;
				case 1: {game();}		break;
				case 2:	{showRules();}	break;
				case 4:	{showProb();}	break;
				case 3: {
					nReels = readInput(" Set the number of reels",4,'-',8);
					nSymbs = readInput(" Set the number of symbols",4,'-',symbols.length);
				} break;
				default: {System.out.println("  Input not in range. Try again.");} break;
			}
			if (opc!=0)
			{
				pressAnyKeyTo("return to the menu");
				clear();
			}
		} while (opc!=0);
		
		System.out.println("\n\n\t\t --- Game Over ---");
		input.close();
    }

}




//*/