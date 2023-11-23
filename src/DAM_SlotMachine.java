import java.util.Objects;
import java.util.Scanner;


public class DAM_SlotMachine
{
	
	/******************** Declaring global variables ********************/
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbols=4;	//Number of possible symbols in each reel.
	
	static char[] Results;	//Results of spinning all the reels in the machine
	static char[] symbolsFound;
	static int[] symbolsAmount;
	/*For every position in both arrays:
		symbolsFound: a new different symbol found in the results.
		symbolsAmount: the times that symbol is present in the results.*/
	
	static Scanner input = new Scanner(System.in);
	static char gameInput;	//Player input for methods apart from stopping the game.
	
	static int betmin=20;	//Minimum amount of money the player is allowed to bet
	static int betmax=100;	//Maximum amount of money the player is allowed to bet
	
	static int MRcount;		//Times the Most Repeated symbol is present in the results.
	static int LRcount;		//Times the Least Repeated symbol is present in the results.
	static int MRindex;		//Position of the Most Repeated symbol in the results
	static int LRindex;		//Position of the Least Repeated symbol in the results
	
	
	
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
	
	
	
	// Displays the slot machine with the given number of reels visible.
	public static void displayMachine(int nShown)
	{
		System.out.print(" ");
		for (int slot=0; slot<Results.length; slot++) {System.out.print(" _____");}
		
		System.out.print("\n |");
		for (int slot=0; slot<Results.length; slot++) {System.out.print("     |");}
        
		System.out.print("\n |");
		for (int slot=0; slot<Results.length; slot++) {System.out.printf("  %c  |", (nShown>slot) ? Results[slot]:' ');}
		
        System.out.print("\n |");
        for (int slot=0; slot<Results.length; slot++) {System.out.print("_____|");}
        
        System.out.println("\n");
	}
	
	
	// Counts the symbols in the results to find the most and least repeated.
	public static void symbolCount()
	{
		// Values need to be reset in every loop
		symbolsFound = new char[Results.length];
		symbolsAmount = new int[Results.length];
        MRcount = 0; LRcount = Results.length;
        MRindex = 0; LRindex = 0;
        int i, j;	//Positions working with Results
		int pos = 0;//Position working with symbolsFound and symbolsAmount
		
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
		//System.out.println("Results.length = "+Results.length);
		//System.out.println("MRS: index = "+MRindex+" count = "+MRcount);
		//System.out.println("LRS: index = "+LRindex+" count = "+LRcount);
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
			char spin; //Make sure the new symbol is not the same
			do {spin = spinReel();} while(spin==Results[LRindex]);
			Results[LRindex] = spin;
			
			displayMachine(Results.length);
			
			if (Objects.equals(Results[MRindex], Results[LRindex]))
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
	}
	
	
	// Make sure the character or amount introduced is a valid input.
	public static double readBet(String message)
    {
		System.out.print(message+" ("+betmin+", "+betmax+"): ");
		double bet = input.nextDouble();
		while (bet<betmin || bet>betmax) 
		{
			System.out.println("\tInvalid input. Try again.\n");
			System.out.print(message+" ("+betmin+", "+betmax+"): ");
	    	bet = input.nextDouble();
		}
		return bet;
    }
	public static char readChar(String message, char valid)
	{
		System.out.print(message+" ("+valid+"): ");
		char answer = input.next().charAt(0);
		while (!(answer==valid||answer==Character.toUpperCase(valid)))
		{
			System.out.println("\tInvalid input. Try again.\n");
			System.out.print(message);
			answer = input.next().charAt(0);
		}
		return answer;
	}
	public static char readChar(String message, char valid1, char valid2)
	{
		System.out.print(message+" ("+valid1+"/"+valid2+"): ");
		char answer = input.next().charAt(0);
		while (!(	answer==valid1 || answer==Character.toUpperCase(valid1) ||
					answer==valid2 || answer==Character.toUpperCase(valid2) ) )
		{
			System.out.println(" Invalid input. Try again.\n");
			System.out.print(message);
			answer = input.next().charAt(0);
		}
		return answer;
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
    	
    	Results = new char[nReels];
    
    /******************** Declaring local variables ********************/
    	
    	char gameEnter;			//Player input to stop or continue the game.
    	double playerBet=0;		//Amount of money the player has bet.
    	double playerSpent=0;	//Amount of money the player spent playing the game.
    	double winAmount;		//Amount of money the player is awarded.
    	
    	
	/******************** GAME START ********************/
		
		System.out.println("\t Welcome to the slot machine ");
		System.out.println("In order to play you have to bet an amount of money");
		System.out.println("and then spin the reels to win a prize:");
		System.out.println("· More than half the reels match : win a prize.");
		System.out.println("· Less than half the reels match : keep what you bet.");
		System.out.println("· Almost all reels match : chance for all or nothing.");
		System.out.println("· All reels match : jackpot.");
		System.out.println("· No matches : you lose.");
		
		System.out.println();
		gameEnter = readChar("Do you want to play?",'y','n');
		
		if (gameEnter=='y' || gameEnter=='Y')
		{
			playerBet = readBet("Enter your bet");
			playerSpent = playerBet;
			
			while (gameEnter=='y' || gameEnter=='Y')
			{
				
				
			/*************** Assigning & Display Results ***************/ 
				
				/* ***** Design of the following loop ***** 
				The range of Results[] is (0, n-1) because it is an array.
				The range of spinReel() is (0, n-1) because 
				   it is used directly with the Results array.
				The range of displayMachine() is (0, n) because
				   its input determines how many positions of Results are displayed:
				   any amount within the range of Results (1,n) or none at all (0).
				We can conclude that, for any random position Results[x] 
				we use spinReel(x) to assign a value and displayMachine(x+1) to display it.
				*/
				for (int reels=0; reels<=Results.length; reels++)
	            {
	                clear();
	                displayMachine(reels);
	                wait(400+reels*50);
	                if (reels<Results.length) 
	                {
		                gameInput = readChar(((reels==0) ? "Start":"Next reel"),'p');
		                Results[reels] = spinReel();
	                }
	            }
				
				
            /*************** Counting symbols in results ****************/
	            
	            symbolCount();
	            
	            
            /*************** Calculating and displaying prize ****************/
	            
	            /*
        		if you get the same symbol 3 times or more you win a prize.
				if you get the same symbol 2 times you keep what you bet.
				if you get the same symbol in all reels you win the jackpot.
				if you don't get any repeated symbols you lose.
        		*/
	            if (MRcount==Results.length-1)
            	{
	            	System.out.println("You got the "+Results[MRindex]+" symbol "+MRcount+" times.\n");
            		reroll();
            	}
	            
	            if (MRcount==1 || MRcount==0) /* No matches. Game Over */
	            {
	            	winAmount = 0;
	            	gameEnter = 'n';
	            	System.out.println("You got no matches. You lost "+playerSpent+" €.");
	            }
	            else 
	            {
	            	System.out.println("You got the "+Results[MRindex]+" symbol "+MRcount+" times.\n");
	            	
	            	if (MRcount==Results.length) /* All matches. Maximum prize & Game Over */
	            	{
	            		winAmount = playerBet*100;
	            		gameEnter = 'n';
	            		System.out.println("\t You won the jackpot!!");
	            		System.out.println("\t  You have spent "+playerSpent+" €.");
    	            	System.out.println("\t    And have won "+winAmount+" €.");
	            	}
	            	else if ( (MRcount>1) && (MRcount<Results.length) )
	            	{
	            		if (MRcount>Results.length/2) /* 3 matches: prize*/
			            {
			    			winAmount = playerBet*10; 
			    			System.out.println("You now have "+winAmount+" €.");
			    		}
			    		else  /* 2 matches: no change*/
			    		{
			    			winAmount = playerBet; 
			    			System.out.println("You still have "+winAmount+" €.");
			    		}
	            		
            /*************** Restarting game ***************/
	            		
	            		System.out.println();
	                    
	                    gameEnter = readChar("Do you want to continue playing?",'y','n');
	                    if (gameEnter=='y' || gameEnter=='Y')
	                    {
	                    	playerBet = winAmount; //The money won is used in the next loop
	                    	
	                    	gameInput = readChar("Do you want to bet more money?",'y','n');
	                        if (gameInput=='y' || gameInput=='Y')
	                        {
	                        	double increase = readBet("Enter how mcuh you want to add");
	                        	playerBet += increase; //The money added is used in the next loop
	                        	playerSpent += increase; //Keeps track of money entered 
	                        }
	                    }
	                    else
	                    {
	                    	System.out.println("\n\t  You have spent "+playerSpent+" €.");
	    	            	System.out.println("\t    And have won "+winAmount+" €.");
	                    }
	            	}
	            }
	            
	            
			}//While loop
        	System.out.println("...");
    	}//if
		input.close();
    }//Main
    
}//Class
