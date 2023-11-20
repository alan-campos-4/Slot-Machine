import java.util.Objects;
import java.util.Scanner;


public class DAM_Proyecto_Tragaperras
{
	
	/******************** Declaring global variables ********************/
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbols=4;	//Number of possible symbols in each reel.
	
	static char[] Results = new char[nReels]; //Results of spinning all the reels in the machine
	
	static Scanner input = new Scanner(System.in);
	
	static int betMin=20;	//Minimum amount of money the player is allowed to bet
	static int betMax=100;	//Maximum amount of money the player is allowed to bet
	
	static int MRcount;		//Times the Most Repeated Symbol is present in the results.
	static int LRcount;		//Times the Least Repeated Symbol is present in the results.
	static int MRindex;		//Position of the Most Repeated Symbol in the results
	static int LRindex;		//Position of the Least Repeated Symbol in the results
	
	
	
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
	
	// Delay the program for a given amount of miliseconds
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);} 
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}
	
	// Assigns a random symbol to the Results in the position given
	public static void spinReel(int index)
	{
        switch((int)(Math.random()*nSymbols))
        {
            case 0:  Results[index]='o';  break;
            case 1:  Results[index]='!';  break;
            case 2:  Results[index]='@';  break;
            case 3:  Results[index]='#';  break;
            case 4:  Results[index]='$';  break;
            case 5:  Results[index]='%';  break;
            case 6:  Results[index]='&';  break;
            case 7:  Results[index]='~';  break;
            case 8:  Results[index]='\\'; break;
            case 9:  Results[index]='/';  break;
            default: Results[index]='_';  break;
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
	
	// Re-rolls the "missing" reel and changes the results accordingly
	public static void reroll()
	{
		System.out.println("You are one reel away from the jackpot.");
		System.out.println(" You can reroll for the chance to get all matches,");
		System.out.println(" but if you fail you will loose all your money.");
		System.out.print("Do you want to reroll the "+(LRindex+1)+"º reel ? (y/n): ");
		
		char gameInput = input.next().charAt(0);
		if (gameInput=='y' || gameInput=='Y')
		{
			spinReel(LRindex);
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
		System.out.print(" Min "+betMin+", Max "+betMax+": ");
    	double bet = input.nextDouble();
    	while (!(bet>=betMin && bet<=betMax))
		{
			System.out.println("Invalid amount. Try again.");
			System.out.print(" Min "+betMin+", Max "+betMax+": ");
			bet = input.nextDouble();
		}
    	return bet;
    }
    
	// Returns true if the symbol given exists in symbolsFound
    public static boolean foundSymbol(char[] arr, char value)
    {
        for (int element : arr)
        {
            if (element == value) 
            	{return true;}
        }
        return false;
    }





    public static void main(String[] args)
    {
    
    /******************** Declaring local variables ********************/
    	
    	char[] symbolsFound = new char[nReels];
    	int[]  symbolsAmount = new int[nReels];
    	/*For every position in both arrays:
    		symbolsFound: a new different symbol found in the results.
    		symbolsAmount: the times that symbol is present in the results.*/
    	char gameEnter;		//Player input to stop or continue the game.
    	char gameInput;		//Player input for other uses.
    	double playerBet;	//Amount of money the player has bet.
    	double playerSpent;	//Amount of money the player spent playing the game.
    	double winAmount;	//Amount of money the player is awarded.
    	int newIndex;		//Position of new different symbol found in the results
    	
    	
	/******************** GAME START ********************/
		
		System.out.println("\t Welcome to the slot machine ");
		System.out.println("In order to play you have to bet an amount of money");
		System.out.println("and then spin the reels to win a prize:");
		System.out.println("· >half the reels match : win a prize.");
		System.out.println("· <half the reels match : keep what you bet.");
		System.out.println("· Almost all reels match : chance for all or nothing.");
		System.out.println("· All reels match : jackpot.");
		System.out.println("· No matches : you lose.");
		
		System.out.println();
		System.out.print("Do you want to play? (y/n): ");
		gameEnter = input.next().charAt(0);
		
		if (gameEnter=='y'|| gameEnter=='Y')
		{
			System.out.println("Enter how much money you want to bet.");
			playerBet = readBet();
			playerSpent = playerBet;
			
			while (gameEnter=='y'|| gameEnter=='Y')
			{
				
				
			/*************** Assigning & Display Results ***************/ 
				
				/***** Design of the following for loop ***** 
				The range of Results[] is (0, n-1) because it is an array.
				The range of spinReel() is (0, n-1) because 
				   the input is used to locate a position in the Results array,
				   and therefore needs to have the same range as an array.
				The range of displayMachine() is (0, n) because
				   the input is used for how many positions of Results are displayed,
				   the options being any amount allowed by the range of Results, plus none;
				   making the number of options n-1+1 = n.
				We can conclude that, for any random position Results[x] 
				we use spinReel(x) to assign a value and displayMachine(x+1) to display it.
				If we want to start with an empty machine and end with displaying all reels
				we must start apply the (0,n-1) range to both functions 
				and make sure displayMachine() reaches n but spinReel() doesn't.
				*/
				for (int reels=0; reels<=nReels; reels++)
	            {
	                /*clearScreen();
	                displayMachine(nReels);
	                wait(400+reels*50);*/
	                
	                if (reels<nReels)
	                {
		                /*do {
		                    System.out.print(((reels==0)?"Start":"Next reel")+" (p): ");
		                    gameInput = input.next().charAt(0);
	                	} while(gameInput!='p');*/
		                spinReel(reels);
	                }
	            }clear(); displayMachine(nReels);
	            
				
			/*************** Resetting the values necessary for the loop ***************/
				    	
		        for (int i=0; i<nReels; i++) 
		        {
		            symbolsFound[i] = ' ';
		            symbolsAmount[i] = 0;
		        }
	            MRcount = 0;	LRcount = 10;
	            MRindex = 0;	LRindex = 0;
	            newIndex = 0;
	            
	            
            /*************** Counting symbols in results ****************/
	            
	            for (int i=0; i<nReels; i++)
	            {
	                if (!foundSymbol(symbolsFound, Results[i]))
	                {
	                	symbolsFound[newIndex]  = Results[i];
	                	symbolsAmount[newIndex] = 1;
	                    for (int j=0; j<nReels; j++)
	                    {
	                        if ( (i!=j) && (Results[i]==Results[j]) )
	                        {
	                        	symbolsAmount[newIndex]++;
	                        	if (symbolsAmount[newIndex] > MRcount) 
	                        	{
	                            	MRindex = i;
	                        		MRcount = symbolsAmount[newIndex];
	                        	}
	                        }
	                    }
	                    if (symbolsAmount[newIndex] < LRcount) 
                    	{
                        	LRindex = i;
                    		LRcount = symbolsAmount[newIndex];
                    	}
	                    newIndex++;
	                }
	            }
	            
	            
            /*************** Calculating and displaying prize ****************/
	            
	            /*
        		if you get the same symbol 3 times or more you win a prize.
				if you get the same symbol 2 times you keep what you bet.
				if you get the same symbol in all reels you win the jackpot.
				if you don't get any repeated symbols you lose.
        		*/
	            
	            /* check for possibility to reroll before anything
            	 * in case it results in all or no matches */
            	if (MRcount==nReels-1) {reroll();}
            	
	            if (MRcount==1) /* No matches. Game Over */
	            {
	            	winAmount = 0;
	            	gameEnter = 'n';
	            	System.out.println("You got no matches. You lost "+playerSpent+" €.");
	            }
	            else 
	            {
	            	System.out.println("You got the "+Results[MRindex]+" symbol "
        			+MRcount+" times.\n");
	            	
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
	                    if (gameEnter=='y'|| gameEnter=='Y')
	                    {
	                    	playerBet = winAmount; //The money won is used in the next loop
	                    	
	                    	System.out.print("Do you want to bet more money? (y/n): ");
	                    	
	                    	gameInput = input.next().charAt(0);
	                        if (gameInput=='y'|| gameInput=='Y')
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
