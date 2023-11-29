import java.util.Scanner;



public class DAM_SlotMachine
{

	/******************** Declaring global variables ********************/
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbols=6;	//Number of possible symbols in each reel.
	
	static char[] Results = new char[nReels];	
	//Results of spinning all the reels in the machine.
	static char[] symbolsFound;	
	//Every different symbol found in the results.
	static int[] symbolsAmount;	
	//The times every different symbol is present in the results.
	
	static int MRcount;		//Times the Most Repeated symbol is present in the results.
	static int LRcount;		//Times the Least Repeated symbol is present in the results.
	static int MRindex;		//Position of the Most Repeated symbol in the results
	static int LRindex;		//Position of the Least Repeated symbol in the results
	
	static int betmin=20;	//Minimum amount of money the player can bet in on time
	static int betmax=100;	//Maximum amount of money the player can bet in on time
	
	static char gameInput;	//Player input for methods apart from stopping the game.
	static char gameEnter;	//Player input to stop or continue the game.
	static Scanner input = new Scanner(System.in);





	/******************** Declaring methods ********************/
	
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
        //System.out.println("\n");
        //System.out.println("\n");
        //System.out.println("\n");
        //System.out.println("\n");
        //System.out.println("\n");	//25
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
	
	
	// Loops until the character or amount introduced is valid.
	public static double readBet(String message)
    {
		message += " ("+betmin+", "+betmax+"): ";
		System.out.print(message);
		double bet = input.nextDouble();
		while (bet<betmin || bet>betmax)
		{
			System.out.println("  Bet must be within the limits. Try again.");
			System.out.print(message);
	    	bet = input.nextDouble();
		}
		return bet;
    }
	public static char readChar(String message, char op1, char op2)
	{
		if (op2==' ')	{message += " ("+op1+"): ";}
		else			{message += " ("+op1+"/"+op2+"): ";}
		System.out.print(message);
		char answer = input.next().charAt(0);
		while (!(answer==op1 || answer==Character.toUpperCase(op1) ||
				 answer==op2 || answer==Character.toUpperCase(op2) ))
		{
			System.out.println("  Invalid input. Try again.");
			System.out.print(message);
			answer = input.next().charAt(0);
		}
		return answer;
	}
	
	
	
	// Displays the slot machine with the given number of reels visible.
	public static void displayMachine(int nShown)
	{
		System.out.print("  ");
		for (int slot=0; slot<Results.length; slot++) {System.out.print(" _____");}
		
		System.out.print("\n  |");
		for (int slot=0; slot<Results.length; slot++) {System.out.print("     |");}
        
		System.out.print("\n  |");
		for (int slot=0; slot<Results.length; slot++) {System.out.printf("  %c  |", (nShown>slot) ? Results[slot]:' ');}
		
        System.out.print("\n  |");
        for (int slot=0; slot<Results.length; slot++) {System.out.print("_____|");}
        
        System.out.println("\n");
	}
	
	
	// Counts the symbols in the results to find the most and least repeated.
	public static void symbolCount()
	{
		symbolsFound = new char[Results.length];
		symbolsAmount = new int[Results.length];
        MRcount = 0; LRcount = Results.length;
        MRindex = 0; LRindex = 0;
        int i, j;	//Positions working with Results
		int pos=0;	//Position working with symbolsFound and symbolsAmount
		
		for (i=0; i<Results.length; i++)
        {
            if (!found(symbolsFound, Results[i]))
            {
            	symbolsFound[pos] = Results[i];
            	symbolsAmount[pos] = 1;
            	for (j=0; j<Results.length; j++)
                {
                    if ( (i!=j) && (Results[i]==Results[j]) )
                    	{symbolsAmount[pos]++;}
                }
                if (symbolsAmount[pos] > MRcount)
            	{
            		MRcount = symbolsAmount[pos];
            		MRindex = i;
            	}
                if (symbolsAmount[pos] < LRcount)
            	{
            		LRcount = symbolsAmount[pos];
            		LRindex = i;
            	}
                pos++;
            }
        }
	}
	
	
	// Re-rolls the "missing" reel and changes the results accordingly.
	public static void reroll()
	{
		System.out.println("You are one reel away from the jackpot.");
		System.out.println(" You can reroll for the chance to get all matches,");
		System.out.println(" but if you fail you will loose all your money.");
		
		gameInput = readChar("Do you want to reroll the "+(LRindex+1)+"º reel?",'y','n');
		if (gameInput=='y' || gameInput=='Y')
		{
			Results[LRindex] = spinReel();
			
			displayMachine(Results.length);
			
			if (Results[MRindex]==Results[LRindex])
			{
				System.out.println("The new symbol is a match.");
				MRcount = Results.length;
			}
			else 
			{
				System.out.println("The new symbol is still different.");
				MRcount = 1;
			}
		}
		System.out.println();
	}
	
	
	
	// Returns a random symbol to assign to the results.
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
	
	
	// Returns true if the value given exists in the array.
    public static boolean found(char[] arr, char value)
    {
        for (char element : arr) //array[element] 
        {
            if (element == value) //array[element] == value
            	{return true;}
        }
        return false;
    }






	public static void main(String[] args)
	{
    
    /******************** Declaring local variables ********************/
    	
    	double playerBet=0;		//Amount of money the player currently has as a bet.
    	double playerSpent=0;	//Amount of money the player has spent playing the game.
    	double winLimit=100000;	//Maximum amount of money the player can win.
    	int gameCount=0;		//Amount of times the player has spun the reels.
    	int gameLimit=10;		//Maximum amount of times the player can spin.
    	
    	
	/******************** Game Menu ********************/
		
		System.out.println("\n\t --- Welcome to the SLOT MACHINE --- ");
		System.out.println("\tBet an some money and try to win a prize!!");
		displayMachine(0);
		System.out.println("Game Rules:");
		System.out.println(" - Every bet has to be between "+betmin+" and "+betmax+" €");
		System.out.println(" - You can't play more than "+gameLimit+" games.");
		System.out.println(" - There is a limit to how much you can win ");
		System.out.println("Prize Rules:");
		System.out.println(" - More than half the reels match : win a prize.");
		System.out.println(" - Less than half the reels match : keep what you bet.");
		System.out.println(" - Almost all reels match : chance for all or nothing.");
		System.out.println(" - All reels match : jackpot.");
		System.out.println(" - No matches : you lose.\n");
		
		gameEnter = readChar("Do you want to play?",'y','n');
		
		if (gameEnter=='y'||gameEnter=='Y')
		{
			playerBet = readBet("Enter your bet");
			playerSpent = playerBet;
			
			while ((gameEnter=='y'||gameEnter=='Y') && (playerBet<winLimit) && (gameCount<gameLimit))
			{
				
				
				/*************** Assigning & Display Results ***************/ 
				
				for (int reels=0; reels<=Results.length; reels++)
	            {
	                clear();
	                System.out.println("\t-- "+(gameCount+1)+"º Game --");
	                displayMachine(reels);
	                if (reels<Results.length) 
	                {
		                gameInput = readChar(((reels==0) ? "Start":"Next reel"),'p',' ');
		                Results[reels] = spinReel();
	                }
	                wait(400+reels*50);
	            }
				
				
				/*************** Counting symbols in results ***************/
	            
	            symbolCount();
	            
	            
	            /*************** Calculating and displaying prize ***************/
	            
            	if (MRcount==Results.length-1) {reroll();}
	            
	            if (MRcount==1) /* No matches. Game Over */
	            {
	            	playerBet = 0;
	            	gameEnter = 'n';
	            	System.out.println("You got no matches. You lost "+playerSpent+" €.");
	            }
	            else if (MRcount==Results.length) /* All matches. Maximum prize & Game Over */
            	{
            		playerBet *= 100;
            		gameEnter = 'n';
            		System.out.println("You won the jackpot!!!");
            	}
            	else
            	{
            		System.out.println("You got the "+Results[MRindex]
            		+" symbol "+MRcount+" times.");
	            	
            		if (MRcount>Results.length/2) /* 3 matches: prize*/
		            {
		    			playerBet *= 10; 
		    			System.out.println("You now have "+playerBet+" €.");
		    		}
		    		else  /* 2 matches: no change*/
		    		{
		    			System.out.println("You still have "+playerBet+" €.");
		    		}
            	}
            	
            	if (playerBet > winLimit) /* Limit exceeded. Maximum prize & Game Over */
        		{
        			playerBet = winLimit;
            		gameEnter = 'n';
        		}
            	
            	
            	/*************** Restarting or ending game ***************/
	            		
        		if (gameEnter!='n')
        		{
        			System.out.println();
                    gameEnter = readChar("Do you want to continue playing?",'y','n');
                    if (gameEnter=='y'||gameEnter=='Y')
                    {
                    	gameInput = readChar("Do you want to bet more money?",'y','n');
                        if (gameInput=='y'||gameInput=='Y')
                        {
                        	double increase = readBet("Enter how much you want to add");
                        	if (playerBet+increase > winLimit)
                        	{
                        		increase = 0;
                        		playerBet = winLimit;
                        		System.out.println("Your new bet exceeds the limit.");
                        		System.out.println("It has been changed to "+playerBet+".");
                        	}
                        	else
                        	{
                        		playerBet += increase;
                            	playerSpent += increase;
                        		System.out.println("Your bet is now "+playerBet+" €.");
                        	}
                        	wait(1000);
                        }
                    }
                }
        		gameCount++;
        		
        		System.out.println();
        		if (gameCount==gameLimit) 
        		{
        			System.out.println("  You have reached the maximum amount of games.");
        		}
        		if (playerBet==winLimit)
        		{
            		System.out.println("\t You have reached the maximum amount");
        			System.out.println("\t of money that can be awarded.");
        			System.out.println("\t You will recieve that instead.");
        		}
        		
        		
			}//While loop
			System.out.println("\n\t You played "+gameCount+" game"+((gameCount==1)? "":"s")+".");
    		System.out.println("\t  You have spent "+playerSpent+" €.");
    		System.out.println("\t    And have won "+playerBet+" €.");
    		
        	
    	}//if
		input.close();
		
    }//Main


}
