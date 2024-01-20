import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.InputMismatchException;





public class SlotMachine
{


	static Scanner input = new Scanner(System.in);
	static boolean gameEnter;	//Player input for stopping or continuing the game.
	static char gameInput;		//Player input for reading a character within the game.
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbols=4;	//Number of possible symbol in each reel.
	
	static char[] Results;	//Array of the results of spinning all the reels in the machine.
	static Symbol MRS;		//Most Repeated Symbol in the results
	static Symbol LRS;		//Least Repeated Symbol in the results
	
	final static double betmin=20;	//Minimum amount of money the player can bet at a time.
	final static double betmax=100;	//Maximum amount of money the player can bet at a time.
	final static int gameLimit=10;	//Maximum amount of times the player can spin the reels.
	final static int winLimit=10000;//Maximum amount of money the player can win.





	// Clears the terminal output.
	public static void clear()
	{
		System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//10
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//20
        System.out.flush();
	}

	// Delays the program for a given amount of mili-seconds.
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);}
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}

	// Waits for the input of the player
	public static void pressToContinue()
	{
		System.out.println("\nPress any key to continue.");
        try
        {
            System.in.read();
            input.nextLine();
        }  
        catch(Exception e)	{}
	}	

	// Returns true if the value given exists in the array.
	public static boolean exists(Symbol[] arr, char value)
    {
		for (int i=0; i<arr.length; i++)
		{
			if (arr[i].sym==value) {return true;}
		}
		return false;
    }
	
	// Returns the object cast as a class
	public static <T> T convertObject(Object o, Class<T> clazz) 
	{
	    try 
	    	{return clazz.cast(o);}
	    catch(ClassCastException e) 
	    	{return null;}
	}
	
	// Loops until the input and type returned is valid.
	@SuppressWarnings("unchecked")
	public static <T> T readInput(String message, T par1, T par2, char dif)
	{
		if (dif=='/')		{message += " ("+par1+"/"+par2+"): ";}
		else if (dif=='-')	{message += " ("+par1+" - "+par2+"): ";}
		else if (dif=='_')	{message += " ("+par1+"): ";}
		else				{message += ": ";}
		
		boolean readError = true;
		do {
			try 
			{
				System.out.print(message);
				
				/***** Type Integer ****/
				if (par1 instanceof Integer)
				{
					int ans = input.nextInt();
					if (ans>=(int)par1 && ans<=(int)par2)
					{
						readError = false;
						return (T)convertObject(ans, Integer.class);
					}
					else {System.out.println("  Input not in range. Try again.");}
				}
				
				/***** Type Double ****/
				else if (par1 instanceof Double)
				{
					double ans = input.nextDouble();
					if (ans>=(double)par1 && ans<=(double)par2)
					{
						readError = false;  
						return (T)convertObject(ans, Double.class);
					}
					else {System.out.println("  Input not in range. Try again.");}
				}
				
				/***** Type Character ****/
				else if (par1 instanceof Character)
				{
					char ans = input.next().charAt(0);
					if (ans==(char)par1 || ans==Character.toUpperCase((char)par1) ||
						ans==(char)par2 || ans==Character.toUpperCase((char)par2) )
					{
						readError = false;  
						return (T)convertObject(ans, Character.class);
					}
					else {System.out.println("  Input not in range. Try again.");}
				}
				
				else {System.out.println("  Input not accepted.");}
				
			}
			catch (InputMismatchException e) {System.out.println("  Invalid input.");}
			input.nextLine();
			
		} while(readError);
		
		
		return par1;
		
	}






	public static class Symbol //Symbol found in the Results
	{
		private char sym;	//Symbol character
		private int count;	//Amount of times it appears in Results
		private int pos;	//Position it has in Results
		
		public Symbol()
			{sym=' '; count=-1; pos=-1;}
		public Symbol(char s)
			{sym = s; count=-1; pos=-1;}
		public Symbol(int c, int p)
			{sym=' '; count=c;  pos=p;}
		
		public char getS()	{return sym;}
		public int getC()	{return count;}
		public int getP()	{return pos;}
	}


	public static class Player
	{
		private double bet;
		private double spent;
		private int games;
		
		public Player()	{bet=0.0; spent=0.0; games=0;}
		
		public void betInit(double read)
			{bet = read; spent = bet;}
		public void betIncrease(double increase)
		{
			if (bet+increase >= winLimit)
			{
				do {
					System.out.println("\nThis increase exceeds the limit.");
					System.out.println("Your bet is still "+bet+".");
					if (gameInput=='y'||gameInput=='Y')
						{increase = readInput("Enter the increase",betmin,betmax,'-');}
					else
						{increase = 0;}
				} while (bet+increase >= winLimit);
			}
			bet += increase;
			spent += increase;
			System.out.println("\nYour bet is now "+bet+" €.");
			pressToContinue();
		}
		
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
					bet += 0.5;
					System.out.println("You have lost money and now have "+bet+" €.");
				}
				else 
					{System.out.println("You still have "+bet+" €.");}
			}
		}
		public void endMessage() 
		{
			System.out.print("\n- - - - - - - - - - - - - - -");
			System.out.print("\n\n  You played "+games+" game"+((games==1)?"":"s") );
			
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
		}
	}






	// Starts the slot machine
	public static void game() 
	{
		Player p1 = new Player();
		Results = new char[nReels];
		gameEnter = true;
		
		p1.betInit(readInput("Enter your bet",betmin,betmax,'-'));
		
		do {
			
			
		/********** Spins the reels of the machine and displays it **********/ 
			
			for (int reels=0; reels<=Results.length; reels++)
		    {
		        clear();
		        System.out.println("\t-- "+(p1.games+1)+"º Game --");
		        displayMachine(reels);
		        if (reels==0)				{gameInput = readInput("Start",'p',' ','_');}
		        if (reels<Results.length)	{Results[reels] = spinReel();}
		        wait(400+reels*50);
		    }
		    p1.games++;
		    
		    
	    /********** Calculates a prize according to the symbols found **********/ 
		    
		    countSymbolsFound();
		    
		    p1.calculatePrize();
			
			
		/********** Restarting or ending the game **********/
		    
		    if (p1.games==gameLimit) 
			{
				gameEnter = false;
				System.out.println("\n  You have reached the maximum amount of games.");
			}
		    
			if (p1.bet>=winLimit)
			{
				p1.bet = winLimit;
				gameEnter = false;
				System.out.println("\n\t You have reached the maximum amount");
				System.out.println("\t of money that can be awarded.");
				System.out.println("\t You will recieve that instead.");
			}
			
			if (gameEnter)
			{
		        gameInput = readInput("Do you want to continue playing?",'y','n','/');
		        if (gameInput=='y'||gameInput=='Y')
		        {
		        	gameInput = readInput("Do you want to bet more money?",'y','n','/');
		            if (gameInput=='y'||gameInput=='Y')
		            {
		            	p1.betIncrease(readInput("Enter the increase",betmin,betmax,'-'));
		            }
		        }
		        else 
		        	{gameEnter = false;}
		    }
			
			
		} while ((gameEnter) && (p1.bet<winLimit) && (p1.games<gameLimit));
		
		
		p1.endMessage();
	}

	// Shows the rules of the game
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

	// Shows the probability of each outcome
	public static void showProb() 
	{
		DecimalFormat formatter = new DecimalFormat("#0.0000");
		double probAll;		// All reels match
		double probAlmost;	// Almost all reels match
		double probMH=0;	// More than half the reels match
		double probLH=0;	// Less than half the reels match
		double probHalf=0;	// Half of the reels match
		double probNone=1;	// No matches
		
		probAll		= Math.pow( ((double)1/nSymbols), nReels );		// (1/x)^4
		probAlmost	= Math.pow( ((double)1/nSymbols), nReels-1 );	// (1/x)^3
		probHalf	= Math.pow( ((double)1/nSymbols), nReels/2 );	// (1/x)^2
		
		for (int i=1; i<=nReels/2; i++)			// (1/x)^1 + (1/x)^2
			{probLH += Math.pow( ((double)1/nSymbols), i );}
		
		for (int i=nReels/2+1; i<=nReels; i++)	// (1/x)^3 + (1/x)^4
			{probMH += Math.pow( ((double)1/nSymbols), i );}
		
		for (int i=0; i<nReels; i++)	// (4/x)*(3/x)*(2/x)*(1/x)
			{probNone *= ((double)(nReels-i)/nSymbols);}
		
		System.out.println("\n  Probability:");
		System.out.println("  - All reels \t"+formatter.format(probAll*100)+" %");
		System.out.println("  - Almost all \t"+formatter.format(probAlmost*100)+" %");
		System.out.println("  - < Half \t"+formatter.format(probLH*100)+" %");
		System.out.println("  - = Half \t"+formatter.format(probHalf*100)+" %");
		System.out.println("  - > Half \t"+formatter.format(probMH*100)+" %");
		System.out.println("  - No match \t"+formatter.format(probNone*100)+" % \n");
	}



	// Displays the slot machine with the given number of reels visible.
	public static void displayMachine(int nShown)
	{
		String space = "  ";
		
		System.out.print(space);
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.print(" _____");}
		
		System.out.print("\n"+space+"|");
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.print("     |");}
		
		System.out.print("\n"+space+"|");
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.printf("  %c  |", (nShown>slot) ? Results[slot]:' ');}
        
		System.out.print("\n"+space+"|");
        for (int slot=0; slot<Results.length; slot++) 
        	{System.out.print("_____|");}
       
        System.out.println("\n");
	}	

	// Returns a random character to assign to the results.
	public static char spinReel()
	{
        switch((int)(Math.random()*nSymbols))
        {
            case 0:  return 'o';
            case 1:  return '!';
            case 2:  return '@';
            case 3:  return '#';
            case 4:  return '$';
            case 5:  return '%';
            case 6:  return '&';
            case 7:  return '~';
            case 8:  return '\\';
            case 9:  return '/';
            default: return '_';
        }
	}	

	// Counts the symbols in the results to find the most and least repeated.
	public static void countSymbolsFound()
	{
		Symbol[] Found = new Symbol[Results.length]; //Every different symbol found in the results.
	    for (int i=0; i<Results.length; i++) {Found[i] = new Symbol();}
		
	    MRS = new Symbol(0,0);
        LRS = new Symbol(Results.length,0);
        int i, j;	//Positions working with Results
		int pos=0;	//Position working with Found
        
		for (i=0; i<Results.length; i++)
		{
			if (!(exists(Found, Results[i])) && (pos<=Results.length))
            {
				Found[pos].sym = Results[i];
				Found[pos].pos = i;
				Found[pos].count = 1;
				for (j=0; j<Results.length; j++)
                {
                    if ( (i!=j) && (Results[i]==Results[j]) )
                    	{Found[pos].count++;}
                }
                if (Found[pos].count > MRS.count) {MRS = Found[pos];}
                if (Found[pos].count < LRS.count) {LRS = Found[pos];}
                pos++;
            }
		}

	}	

	// Re-rolls the "missing" reel and changes the results accordingly.
	public static void reroll()
	{
		System.out.println(" You are one reel away from the jackpot.");
		System.out.println(" You can reroll for the chance to get all matches,");
		System.out.println(" but if you fail you will loose all your money.");
		
		gameInput = readInput("Do you want to reroll the "+(LRS.pos+1)+"º reel?",'y','n','/');
		if (gameInput=='y' || gameInput=='Y')
		{
			Results[LRS.pos] = spinReel();
			
			displayMachine(Results.length);
			
			if (Results[MRS.pos]==Results[LRS.pos])
			{
				System.out.println("\nThe new symbol is a match.");
				MRS.pos = Results.length;
			}
			else 
			{
				System.out.println("\nThe new symbol is still different.");
				MRS.pos = 1;
			}
		}
		System.out.println();
	}










	public static void main(String[] args)
	{
		
		int opc;
		boolean loop = true;
		
		do {
			System.out.println("\n\t --- Welcome to the SLOT MACHINE --- ");
			System.out.println("Bet an some money and try to win a prize!!");
			System.out.println("The machine currently has "+nReels+" reels and "+nSymbols+" symbols.");
			
			System.out.println("\nWhat do you want to do?");
			System.out.println("1. Play the game.");
			System.out.println("2. View the ruleset.");
			System.out.println("3. Change the parameters.");
			System.out.println("0. Exit.\n");
			
			opc = readInput("Choose an option",0,4,'-');
			
			switch(opc)
			{
				case 0: {loop = false;} 
				break;
				case 1: {game();}
				break;
				case 2:	{showRules();}
				break;
				case 4:	{showProb();}
				break;
				case 3: 
				{
					nReels = readInput("  Set the number of reels",4,8,'-');
					nSymbols = readInput("  Set the number of symbols",4,10,'-');
				}
				break;
				default: {System.out.println("  Input not in range. Try again.");} 
				break;
			}
			pressToContinue();
			clear();
			
		}while (loop);
		
		

		System.out.println("\n- - - - - Game Over - - - - -");
		input.close();
    }



}