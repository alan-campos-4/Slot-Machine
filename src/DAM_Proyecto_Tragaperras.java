import java.util.Objects;
import java.util.Scanner;


public class DAM_Proyecto_Tragaperras
{

	static int nReels = 4;		//Number of spinning reels the machine has.
	static int nSymbols = 4;	//Number of possible symbols in each reel.
	
	static char[] Results = new char[nReels];
	//Every position is obtained from one of the spinning reels.
	static char[] symbolsFound = new char[nReels];
	static int[] symbolsRepeated = new int[nReels];
	//For every position in both arrays:
	//	symbolsFound: a new different symbol found in the results.
	//	symbolsRepeated: the times that symbol is repeated in the results.
	
	static Scanner input = new Scanner(System.in);
	static char gameEnter;	//Player input to stop or continue the game.
	static char gameInput;	//Player input for other methods.
	
	
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
	
	
	public static void assignRandom(int index)
	{
		int numrand = (int)(Math.random()*nSymbols);
		char rand;
        switch(numrand)
        {
            case 0:  rand='o';  break;
            case 1:  rand='!';  break;
            case 2:  rand='@';  break;
            case 3:  rand='#';  break;
            case 4:  rand='$';  break;
            case 5:  rand='%';  break;
            case 6:  rand='&';  break;
            case 7:  rand='~';  break;
            case 8:  rand='\\'; break;
            case 9:  rand='/';  break;
            default: rand='_';  break;
        }
        Results[index] = rand;
	}
	
	public static void displayMachine(int slotsShown)
	{
		for (int slot=0; slot<nReels; slot++) {System.out.print(" _____");}
		
		System.out.print("\n|");
		for (int slot=0; slot<nReels; slot++) {System.out.print("     |");}
        
		System.out.print("\n|");
		for (int slot=0; slot<nReels; slot++)
        	{System.out.printf("  %c  |", (slotsShown>slot)? Results[slot] : ' ');}
		
        System.out.print("\n|");
        for (int slot=0; slot<nReels; slot++) {System.out.print("_____|");}
	}
	
	
	
	public static void reroll(char MRS, char LRS, int Mcount, int Lcount)
	{
		//
		int MRS_index = posOfValue(Results, MRS);
		int LRS_index = posOfValue(Results, LRS);
		
		assignRandom(LRS_index);
		displayMachine(nReels);
		System.out.println("\n");
		
		if (Objects.equals(Results[MRS_index], Results[LRS_index]))
		{
			System.out.println("The new symbol is a match.");
			Mcount++;
		}
		else 
		{
			System.out.println("The new symbol is still different.");
			Mcount--;
		}
	}
	
	
	
	public static double readBet(int min, int max)
    {
		System.out.print("Min "+min+", Max "+max+": ");
    	double bet = input.nextDouble();
    	while (bet<min && bet>max)
		{
			System.out.println("Invalid amount. Try again.");
			bet = input.nextDouble();
		}
    	return bet;
    }
    
    public static boolean foundSymbol(char value)
    {
        for (int element : symbolsFound)
        {
            if (element == value) 
            	{return true;}
        }
        return false;
    }
    
    public static int posOfValue(char[] arr, char value)
    {
    	for (int e=0; e<arr.length; e++)
        {
            if (arr[e] == value)//--
            	{return e;}
        }
        return -1;
    }

    public static void continuePlaying(double playerBet, double playerSpent, double winAmount)
	{
		//
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
            	double increase = readBet(20, 50);
            	playerBet += increase; //The money added is used in the next loop
            	playerSpent += increase; //Keeps track of money entered 
            }
            
        	clearScreen(); //Game restart whether player adds money or not
        }
	}





    public static void main(String[] args)
    {
    
	/******************** Declaring variables ********************/
    	
		char MRS;		//Most Repeated Symbol.
		char LRS;		//Least Repeated Symbol
		int Mcount;	//Times the Most Repeated Symbol is present in the results.
		int Lcount;	//Times the Least Repeated Symbol is present in the results.
		int newIndex;	//Position of a new different symbol found in the results.
		
		int betMin = 20;	//Minimum amount of money the player is allowed to bet
		int betMax = 100;	//Maximum amount of money the player is allowed to bet
		double playerBet;	//Amount of money the player has bet.
		double playerSpent;	//Amount of money the player spent playing the game.
		double winAmount=0;	//Amount of money the player is awarded.
		
		
	/******************** GAME START ********************/
		
		System.out.println("\t Welcome to the slot machine ");
		System.out.println("In order to play you have to bet an amount of money");
		System.out.println("and then spin the reels to win a prize:");
		System.out.println("  if you get the same symbol 3 times or more you win a prize.");
		System.out.println("  if you get the same symbol 2 times you keep what you bet.");
		System.out.println("  if you get the same symbol in all reels you win the jackpot.");
		System.out.println("  if you don't get any repeated symbols you lose.");
		System.out.println();
		System.out.print("Do you want to play? (y/n): ");
		
		gameEnter = input.next().charAt(0);
		if (gameEnter=='y'|| gameEnter=='Y')
		{
			System.out.println("Enter how much money you want to bet.");
			playerBet = readBet(betMin, betMax);
			playerSpent = playerBet;
			
			while (gameEnter=='y'|| gameEnter=='Y')
			{
	            
		    	
			/*************** Assigning results ***************/
		    	
		        for (int index=0; index<nReels; index++) 
		        {
		            assignRandom(index);
		            //Values need to be reset for every loop
		            symbolsFound[index] = ' ';
		            symbolsRepeated[index] = 0;
		        }
		        
		        
			/*************** Display Spinning Reels ***************/ 
		        
	            //for (int reelsShown=0; reelsShown<=nReels; reelsShown++)
	            {
	                clearScreen();
	                
	                displayMachine(nReels);
	                
	                //wait(500);
	                
	                System.out.println("\n");
	                
	                /*if (reelsShown!=nReels)
	                {
	                	do {
		                    System.out.print(((reelsShown==0)?"Start":"Next reel")+" (p): ");
		                    gameInput = input.next().charAt(0);
	                	} while(gameInput!='p');
	                }*/
	            }
	            
	            
            /*************** Counting symbols in results ****************/
	            
	            //Values need to be reset for every loop
	            newIndex = 0;
	            Mcount = 0;
	            Lcount = 10;
	            MRS = ' ';
	            LRS = ' ';
	            
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
	            if (Mcount==1)
	            {
	            	//end :(
	            	winAmount = 0;
	            	gameEnter = 'n';
	            	System.out.println("You got no matches. You lost "+playerSpent+" €.");
	            }
	            else 
	            {
	            	System.out.println("You got the "+MRS+" symbol "+Mcount+" times.\n");
	            	
	            	
	            	if (Mcount==nReels-1) {reroll(MRS, LRS, Mcount, Lcount);}
	            	
	            	if (Mcount==nReels) 
	            		{winAmount = playerBet*100;}
	            	else if ( (Mcount>1) && (Mcount<nReels) )
	            	{
	            		if (Mcount > nReels/2) /* 3 matches: prize*/
			            {
			    			winAmount = playerBet*10; 
			    			System.out.println("You now have "+winAmount+" €.");
			    		}
			    		else  /* 2 matches: no change*/
			    		{
			    			winAmount = playerBet; 
			    			System.out.println("You still have "+winAmount+" €.");
			    		}
	            		
	            		System.out.println();		            
			        	continuePlaying(playerBet, playerSpent, winAmount);
	            	}
	            	
	            	if (Mcount==nReels) 
	        			{System.out.println("\n\t You won the jackpot!!");}
	        		System.out.println("\n\t  You have spent "+playerSpent+" €.");
	            	System.out.println("\t    And have won "+winAmount+" €.");
	            }


			}
        	System.out.println("...");
    	}
		input.close();
    }
    
}
