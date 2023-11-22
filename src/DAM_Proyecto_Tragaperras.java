import java.util.Objects;
import java.util.Scanner;


public class DAM_Proyecto_Tragaperras
{
	
	/******************** Declaring global variables ********************/
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbols=4;	//Number of possible symbols in each reel.
	
	static char[] Results = new char[nReels]; //Results of spinning all the reels in the machine
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
	
	// Clear the terminal output
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
	
	
	// Delay the program for a given amount of mili-seconds
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);} 
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}
	
	
	// Returns a random symbol to assign to the Results
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
	
	
	// Displays the slot machine with the given number of reels visible
	public static void displayMachine(int nShown)
	{
		System.out.print(" ");
		for (int slot=0; slot<nReels; slot++) {System.out.print(" _____");}
		
		System.out.print("\n |");
		for (int slot=0; slot<nReels; slot++) {System.out.print("     |");}
        
		System.out.print("\n |");
		for (int slot=0; slot<nReels; slot++)
        	{System.out.printf("  %c  |", (nShown>slot)? Results[slot] : ' ');}
		
        System.out.print("\n |");
        for (int slot=0; slot<nReels; slot++) {System.out.print("_____|");}
        
        System.out.println("\n");
	}
	
	
	// Counts the symbols in the results to find the most and least repeated
	public static void symbolCount(char[] Results)
	{
		// Values need to be reset in every loop
		symbolsFound = new char[nReels];
		symbolsAmount = new int[nReels];
        MRcount = 0; LRcount = 10;
        MRindex = 0; LRindex = 0;
		int newPos = 0;
		
		for (int i=0; i<Results.length; i++)
        {
            if (!found(symbolsFound, Results[i]))
            {
            	symbolsFound[newPos] = Results[i];
            	symbolsAmount[newPos] = 1;
            	for (int j=0; j<Results.length; j++)
                {
                    if ( (i!=j) && (Results[i]==Results[j]) )
                    	{symbolsAmount[newPos]++;}
                }
                if (symbolsAmount[newPos] > MRcount) 
            	{
            		MRcount = symbolsAmount[newPos];
            		MRindex = i;
            	}
                if (symbolsAmount[newPos] < LRcount) 
            	{
            		LRcount = symbolsAmount[newPos];
            		LRindex = i;
            	}
                newPos++;
            }
        }
	}
	
	
	// Re-rolls the "missing" reel and changes the results accordingly
	public static void reroll()
	{
		System.out.println("You are one reel away from the jackpot.");
		System.out.println(" You can reroll for the chance to get all matches,");
		System.out.println(" but if you fail you will loose all your money.");
		System.out.print("Do you want to reroll the "+(LRindex+1)+"º reel ? (y/n): ");
		
		gameInput = input.next().charAt(0);
		if (gameInput=='y' || gameInput=='Y')
		{
			Results[LRindex] = spinReel();
			displayMachine(nReels);
			
			if (Objects.equals(Results[MRindex], Results[LRindex]))
			{
				System.out.println("The new symbol is a match.");
				MRcount = nReels;
			}
			else 
			{
				System.out.println("The new symbol is still different.");
				MRcount = 1;
			}
		}
	}
	
	
	// Makes sure the amount introduced as a bet is between the limits
	public static double readBet()
    {
		double bet;
		do {
			System.out.print(" Min "+betmin+", Max "+betmax+": ");
	    	bet = input.nextDouble();
		} while(bet<betmin || bet>betmax);
		return bet;
    }
    
	
	// Returns true if the value given exists in the array
    public static boolean found(char[] arr, char value)
    {
        for (int element : arr) //array[element] 
        {
            if (element == value) //array[element] == value
            	{return true;}
        }
        return false;
    }





    public static void main(String[] args)
    {
    
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
		System.out.print("Do you want to play? (y/n): ");
		gameEnter = input.next().charAt(0);
		
		if (gameEnter=='y' || gameEnter=='Y')
		{
			System.out.println("Enter how much money you want to bet.");
			playerBet = readBet();
			playerSpent = playerBet;
			
			while (gameEnter=='y' || gameEnter=='Y')
			{
				
				
			/*************** Assigning & Display Results ***************/ 
				
				/* ***** Design of the following loop ***** 
				The range of Results[] is (0, n-1) because it is an array.
				The range of spinReel() is (0, n-1) because 
				   its input is used to locate a position in the Results array.
				The range of displayMachine() is (0, n) because
				   its input determines how many positions of Results are displayed:
				   any amount within the range of Results (1,n) or none at all (0).
				We can conclude that, for any random position Results[x] 
				we use spinReel(x) to assign a value and displayMachine(x+1) to display it.
				*/
				for (int reels=0; reels<=nReels; reels++)
	            {
	                clear();
	                displayMachine(reels);
	                wait(400+reels*50);
	                
	                if (reels<nReels)
	                {
		                do {
		                    System.out.print(((reels==0)?"Start":"Next reel")+" (p): ");
		                    gameInput = input.next().charAt(0);
	                	} while(gameInput!='p');
		                Results[reels] = spinReel();
	                }
	            }//clear(); displayMachine(nReels);
	            
				
			/*************** Resetting the values necessary for the loop ***************/
				    	
		        
	            
	            
            /*************** Counting symbols in results ****************/
	            
	            symbolCount(Results);
	            
	            
            /*************** Calculating and displaying prize ****************/
	            
	            /*
        		if you get the same symbol 3 times or more you win a prize.
				if you get the same symbol 2 times you keep what you bet.
				if you get the same symbol in all reels you win the jackpot.
				if you don't get any repeated symbols you lose.
        		*/
	            if (MRcount==nReels-1)
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
	            	
	            	if (MRcount==nReels) /* All matches. Maximum prize & Game Over */
	            	{
	            		winAmount = playerBet*100;
	            		gameEnter = 'n';
	            		System.out.println("\t You won the jackpot!!");
	            		System.out.println("\t  You have spent "+playerSpent+" €.");
    	            	System.out.println("\t    And have won "+winAmount+" €.");
	            	}
	            	else if ( (MRcount>1) && (MRcount<nReels) )
	            	{
	            		if (MRcount>nReels/2) /* 3 matches: prize*/
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
	            		System.out.print("Do you want continue playing? (y/n): ");
	                    
	                    gameEnter = input.next().charAt(0);
	                    if (gameEnter=='y' || gameEnter=='Y')
	                    {
	                    	playerBet = winAmount; //The money won is used in the next loop
	                    	
	                    	System.out.print("Do you want to bet more money? (y/n): ");
	                    	
	                    	gameInput = input.next().charAt(0);
	                        if (gameInput=='y' || gameInput=='Y')
	                        {
	                        	System.out.println("Enter the money do you want to add.");
	                        	double increase = readBet();
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
