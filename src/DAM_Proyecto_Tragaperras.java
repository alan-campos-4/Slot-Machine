import java.util.Objects;
import java.util.Scanner;


public class DAM_Proyecto_Tragaperras
{
	
	/******************** Declaring global variables ********************/
	
	static int nReels=4;	//Number of spinning reels the machine has.
	static int nSymbols=4;	//Number of possible symbols in each reel.
	
	static char[] Results = new char[nReels];
	//Array containing the results of spinning all reels in the machine
	static char[] symbolsFound = new char[nReels];
	static int[] symbolsRepeated = new int[nReels];
	/*For every position in both arrays:
		symbolsFound: a new different symbol found in the results.
		symbolsRepeated: the times that symbol is repeated in the results.
	 *Example: the array Results={@,@,#,!,@,!} 
		will yield symbolsFound={@,#,!, , , } and symbolsRepeated={3,1,2,0,0,0} */
	
	static Scanner input = new Scanner(System.in);
	static char gameEnter;	//Player input to stop or continue the game.
	static char gameInput;	//Player input for other methods.
	
	static char MRS;	//Most Repeated Symbol in the results.
	static char LRS;	//Least Repeated Symbol in the results.
	static int Mcount;	//Times the Most Repeated Symbol is present in the results.
	static int Lcount;	//Times the Least Repeated Symbol is present in the results.
	
	static int betMin=20;	//Minimum amount of money the player is allowed to bet
	static int betMax=100;	//Maximum amount of money the player is allowed to bet
	
	public class Symbol{
		char sym;
		int count;
		int index;
	}
	
	
	/******************** Declaring methods ********************/
	
	public static void clearScreen() 
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
		System.out.println("You one reel away from the jackpot.");
		System.out.println(" You can reroll for the chance to get all matches,");
		System.out.println(" but if you fail you will loose all your money.");
		System.out.print("Do you want to reroll it? (y/n): ");
		
		gameInput = input.next().charAt(0);
		if (gameInput=='y' || gameInput=='Y')
		{
			int MRS_index = posOfValue(Results, MRS);
			int LRS_index = posOfValue(Results, LRS);
			spinReel(LRS_index);
			displayMachine(nReels);
			
			if (Objects.equals(Results[MRS_index], Results[LRS_index]))
			{
				System.out.println("The new symbol is a match.");
				Mcount = nReels;
			}
			else 
			{
				System.out.println("The new symbol is still different.");
				Mcount = 0;
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
    public static boolean foundSymbol(char value)
    {
        for (int element : symbolsFound)
        {
            if (element == value) 
            	{return true;}
        }
        return false;
    }
    
    // Returns the position of the value given in the array
    public static int posOfValue(char[] arr, char value)
    {
    	for (int i : arr)
        {
            if (arr[i] == value) {return i;}
        }
        return -1;
    }





    public static void main(String[] args)
    {
    	
    /******************** Declaring local variables ********************/
    	
    	double playerBet;	//Amount of money the player has bet.
    	double playerSpent;	//Amount of money the player spent playing the game.
    	double winAmount;	//Amount of money the player is awarded.
    	int newIndex;		//Position of new different symbol found in the results
    	Symbol MRSS;
    	Symbol LRSS;
    	
    	
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
			displayMachine(0);
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
				//Due to the design of each function
				for (int reels=0; reels<=nReels; reels++)
	            {
	                clearScreen();
	                displayMachine(reels);
	                wait(400+reels*50);
	                
	                if (reels<nReels)
	                {
		                do {
		                    System.out.print(((reels==0)?"Start":"Next reel")+" (p): ");
		                    gameInput = input.next().charAt(0);
	                	} while(gameInput!='p');
		                spinReel(reels);
	                }
	            }
	            
				
			/*************** Resetting the values necessary for the loop ***************/
				    	
		        for (int i=0; i<nReels; i++) 
		        {
		            symbolsFound[i] = ' ';
		            symbolsRepeated[i] = 0;
		        }
	            MRS = ' ';
	            LRS = ' ';
	            Mcount = 0;
	            Lcount = 10;
	            newIndex = 0;
	            
	            
            /*************** Counting symbols in results ****************/
	            
	            for (int i=0; i<nReels; i++)
	            {
	                if (!foundSymbol(Results[i]))
	                {
	                	symbolsFound[newIndex] = Results[i];
	                    for (int j=0; j<nReels; j++)
	                    {
	                        if (Results[i]==Results[j])
	                        {
	                        	symbolsRepeated[newIndex]++;
	                        	if (symbolsRepeated[newIndex] > Mcount) 
	                        	{
	                        		Mcount = symbolsRepeated[newIndex]; 
	                        		MRS = Results[i];
	                        	}
	                        }
	                        if (symbolsRepeated[newIndex] < Lcount) 
                        	{
                        		Lcount = symbolsRepeated[newIndex]; 
                        		LRS = Results[i];
                        	}
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
            	if (Mcount==nReels-1) {reroll();}
            	
	            if (Mcount==1) /* No matches. Game Over */
	            {
	            	winAmount = 0;
	            	gameEnter = 'n';
	            	System.out.println("You got no matches. You lost "+playerSpent+" €.");
	            }
	            else 
	            {
	            	System.out.println("You got the "+MRS+" symbol "+Mcount+" times.\n");
	            	
	            	if (Mcount==nReels) /* All matches. Maximum prize & Game Over */
	            	{
	            		winAmount = playerBet*100;
	            		gameEnter = 'n';
	            		System.out.println("\t You won the jackpot!!");
	            		System.out.println("\t  You have spent "+playerSpent+" €.");
    	            	System.out.println("\t    And have won "+winAmount+" €.");
	            	}
	            	else if ( (Mcount>1) && (Mcount<nReels) )
	            	{
	            		if (Mcount>nReels/2) /* 3 matches: prize*/
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
	                        	System.out.print("Enter the money do you want to add: ");
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
