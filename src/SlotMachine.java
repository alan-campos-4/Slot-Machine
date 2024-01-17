import java.util.Scanner;
//import java.text.DecimalFormat;
import java.util.InputMismatchException;




//Possible classes: symbol, player, machine

public class SlotMachine
{


	static Scanner input = new Scanner(System.in);
	static char gameEnter;		//Player input for stopping or continuing the game.
	static char gameInput;		//Player input for reading a character within the game.
	
	static int nReels=4;		//Number of spinning reels the machine has.
	static int nSymbols=4;		//Number of possible symbol in each reel.
	
	static char[] Results;		//Array of the results of spinning all the reels in the machine.
	static Symbol MRS;			//Most Repeated Symbol in the results
	static Symbol LRS;			//Least Repeated Symbol in the results
	
	static int betmin=20;		//Minimum amount of money the player can bet at a time.
	static int betmax=100;		//Maximum amount of money the player can bet at a time.
	static int gameLimit=10;	//Maximum amount of times the player can spin the reels.
	static int winLimit=10000;	//Maximum amount of money the player can win.




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







	// Clear the terminal output.
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
	
	
	// Delay the program for a given amount of mili-seconds.
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);}
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
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
	
	
	// Loops until the character returned is valid.
	public static char readInput(String message, char op1, char op2)
	{
		if (op2==' ')	{message += " ("+op1+"): ";}
		else			{message += " ("+op1+"/"+op2+"): ";}
		boolean readError = true;
		char ans = ' ';
		do {
			try 
			{
				System.out.print(message);
				ans = input.next().charAt(0);
				if (ans==op1 || ans==Character.toUpperCase(op1) ||
					ans==op2 || ans==Character.toUpperCase(op2) )
					{readError = false;}
				else
					{System.out.println("  Input not accepted. Try again.");}
			}
			catch (InputMismatchException e)
				{System.out.println("  Invalid input.");}
			
		} while(readError);
		
		return ans;
	}	
	
	
	// Loops until the amount returned is valid.
	public static double readNum(String message, int min, int max)
	{
		message += " ("+min+"-"+max+"): ";
		boolean readError = true;
		double ans = 0.0;
		do {
			try 
			{
				System.out.print(message);
				ans = input.nextDouble();
				if (ans>=min && ans<=max)
					{readError = false;}
				else
					{System.out.println("  Input not accepted. Try again.");}
			}
			catch (InputMismatchException e)
				{System.out.println("  Invalid input.");}
			input.nextLine();
		} while(readError);
		
		return ans;
	}




	// Displays the menu with the rules and rewards of the game.
	public static void showMenu(char type)
	{
		System.out.println("\nGame Rules:");
		System.out.println(" - Every bet has to be between "+betmin+" and "+betmax+" €");
		System.out.println(" - You can't play more than "+gameLimit+" games.");
		System.out.println(" - There is a limit to how much you can win ");
		System.out.println("\nPrizes available:");
		System.out.println(" - All reels match : jackpot.");
		System.out.println(" - Almost all reels match : chance at all or nothing.");
		System.out.println(" - More than half the reels match : win some money.");
		System.out.println(" - Less than half the reels match : lose some money.");
		System.out.println(" - Half of the reels match : keep your bet.");
		System.out.println(" - No matches : you lose.\n");
		/*
		if (type=='Y' || type=='N')
		{
			System.out.println("Probability:");
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
			
			for (int i=nReels/2+1; i<=nReels; i++)	// (1/x)^3 + (1/x)^4
				{probMH += Math.pow( ((double)1/nSymbols), i );}
			
			for (int i=1; i<nReels/2; i++)			// (1/x)^1 + (1/x)^2
				{probLH += Math.pow( ((double)1/nSymbols), i );}
	    	
			for (int i=0; i<nReels; i++)	// (4/x)*(3/x)*(2/x)*(1/x)
				{probNone *= ((double)(nReels-i)/nSymbols);}
	    	
	    	System.out.println(" - All reels \t"+formatter.format(probAll*100)+" %");
	    	System.out.println(" - Almost all \t"+formatter.format(probAlmost*100)+" %");
	    	System.out.println(" - > Half \t"+formatter.format(probMH*100)+" %");
	    	System.out.println(" - < Half \t"+formatter.format(probLH*100)+" %");
	    	System.out.println(" - = Half \t"+formatter.format(probHalf*100)+" %");
	    	System.out.println(" - No match \t"+formatter.format(probNone*100)+" % \n");
		}*/
	}
	
	
	// Displays the slot machine with the given number of reels visible.
	public static void displayMachine(int nShown)
	{
		String space = "\t";
		
		System.out.print(space);
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.print(" _____");}
		
		System.out.print("\n"+space);
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.print("     |");}
		
		System.out.print("\n"+space);
		for (int slot=0; slot<Results.length; slot++) 
			{System.out.printf("  %c  |", (nShown>slot) ? Results[slot]:' ');}
        
		System.out.print("\n"+space);
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
		
		gameInput = readInput("Do you want to reroll the "+(LRS.pos+1)+"º reel?",'y','n');
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
		
    	double playerBet=0;		//Amount of money the player currently has as a bet.
    	double playerSpent=0;	//Amount of money the player has spent playing the game.
    	int gameCount=0;		//Amount of times the player has spun the reels.
    	
		System.out.println("\n\t --- Welcome to the SLOT MACHINE --- ");
		System.out.println("\tBet an some money and try to win a prize!!\n");
		
		gameEnter = readInput("Do you want to play?",'y','n');
		if (gameEnter=='y'||gameEnter=='Y')
		{
			showMenu(gameEnter);
			
			System.out.println("The machine has "+nReels+" reels and "+nSymbols+" symbols.");
			gameInput = readInput("Do you want to change these numbers?",'y','n');
			if (gameInput=='y'||gameInput=='Y')
			{
				nReels = (int)readNum("Set the number of reels",4,8);
				nSymbols = (int)readNum("Set the number of symbols",4,10);
			}
			Results = new char[nReels];
			displayMachine(0);
			
			playerBet = readNum("Enter your bet",betmin,betmax);
			playerSpent = playerBet;
			
			
			do {
				
				
			/********** Assigns & displays the results of spinning **********/ 
				
				for (int reels=0; reels<=Results.length; reels++)
	            {
	                clear();
	                System.out.println("\t-- "+(gameCount+1)+"º Game --");
	                displayMachine(reels);
	                if (reels==0)				{gameInput = readInput("Start",'p',' ');}
	                if (reels<Results.length)	{Results[reels] = spinReel();}
	                wait(400+reels*50);
	            }
	            gameCount++;
				
				
			/********** Finds most and least repeated symbol **********/
	            
	            countSymbolsFound();
	            
	            
            /********** Calculates and displays the player's prize **********/
	            
	            if (MRS.count==Results.length-1) {reroll();}
	            
            	if (MRS.count==1)
	            {
	            	playerBet = 0;
	            	gameEnter = 'n';
	            	System.out.println("You got no matches.");
	            }
	            else if (MRS.count==Results.length)
            	{
            		playerBet *= 1000;
            		gameEnter = 'n';
            		System.out.println("You won the jackpot!!!");
            	}
            	else
            	{
            		System.out.println("You got the "+MRS.sym+" symbol "+MRS.count+" times.");
	            	
            		if (MRS.count>Results.length/2)
		            {
		    			playerBet *= 10; 
		    			System.out.println("You have gained money and now have "+playerBet+" €.");
		    		}
		    		else if (MRS.count<Results.length/2)
		            {
		    			playerBet *= 0.5; 
		    			System.out.println("You have lost money and now have "+playerBet+" €.");
		    		}
		    		else 
		    			{System.out.println("You still have "+playerBet+" €.");}
            	}
            	
            	
            /********** Restarts, increases bet or ends game **********/
	            
	            if (gameCount==gameLimit) 
        		{
            		gameEnter = 'n';
        			System.out.println("\n  You have reached the maximum amount of games.");
        		}
	            
        		if (playerBet>=winLimit)
        		{
        			playerBet = winLimit;
            		gameEnter = 'n';
        			System.out.println("\n\t You have reached the maximum amount");
        			System.out.println("\t of money that can be awarded.");
        			System.out.println("\t You will recieve that instead.");
        		}
            	
        		if (gameEnter!='n')
        		{
                    gameEnter = readInput("Do you want to continue playing?",'y','n');
                    if (gameEnter=='y'||gameEnter=='Y')
                    {
                    	gameInput = readInput("Do you want to bet more money?",'y','n');
                        if (gameInput=='y'||gameInput=='Y')
                        {
                        	double increase = readNum("Enter the increase",betmin,betmax);
                        	if (playerBet+increase >= winLimit)
                        	{
                        		do {
                        			System.out.println("\nThis increase exceeds the limit.");
                            		System.out.println("Your bet is still "+playerBet+".");
                        			if (gameInput=='y'||gameInput=='Y')
                        				{increase = readNum("Enter the increase",betmin,betmax);}
                        			else
                        				{increase = 0;}
                        		} while (playerBet+increase >= winLimit);
                        	}
                        	playerBet += increase;
                        	playerSpent += increase;
                    		System.out.println("\nYour bet is now "+playerBet+" €.");
                    		wait(1200);
                        }
                    }
                }
        		
        		
			} while ((gameEnter=='y'||gameEnter=='Y')&&(playerBet<winLimit)&&(gameCount<gameLimit));
			
			
			
		/********** End of Loop. Final Message **********/
			
			System.out.print("\n  You played "+gameCount+" game"+((gameCount==1)?"":"s") );
			
			if (playerBet==0)
			{
				System.out.println("\n   and lost "+playerSpent+" €.");
			}
			else if (playerBet==playerSpent)
			{
				System.out.println("\n   and made back the money");
				System.out.println("   that you bet: "+playerBet+" €.");
			}
			else
			{
				System.out.println(".\n   You have spent "+playerSpent+" €.");
				if (playerBet<playerSpent)
					{System.out.println("    And left with "+playerBet+" €.");}
				else
					{System.out.println("    And have won "+playerBet+" €.");}
			}
    		
			
    	}
		System.out.println("\n\n\t *** Game Over *** ");
		input.close();
    }


}