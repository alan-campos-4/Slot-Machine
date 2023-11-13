import java.util.Objects;
import java.util.Scanner;


public class DAM_Proyecto_Tragaperras
{

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
	}
	
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);} 
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}
	
	
	public static void assignRandom(char[] arr, int index, int symbols)
	{
		int numrand = (int)(Math.random()*symbols);
        switch(numrand)
        {
            case 0:  arr[index]='o';  break;
            case 1:  arr[index]='!';  break;
            case 2:  arr[index]='@';  break;
            case 3:  arr[index]='#';  break;
            case 4:  arr[index]='$';  break;
            case 5:  arr[index]='%';  break;
            case 6:  arr[index]='&';  break;
            case 7:  arr[index]='~';  break;
            case 8:  arr[index]='\\'; break;
            case 9:  arr[index]='/';  break;
            default: arr[index]='_';  break;
        }
	}
	
	public static void displayMachine(char[] arr, int show)
	{
		for (int slot=0; slot<arr.length; slot++) {System.out.print(" _____");}
		
		System.out.print("\n|");
		for (int slot=0; slot<arr.length; slot++) {System.out.print("     |");}
        
		System.out.print("\n|");
		for (int slot=0; slot<arr.length; slot++)
        	{System.out.printf("  %c  |", (show>slot)? arr[slot]: ' ');}
		
        System.out.print("\n|");
        for (int slot=0; slot<arr.length; slot++) {System.out.print("_____|");}
	}
	
	
    public static boolean found(char[] arr, char value)
    {
        for (int element : arr)
        {
            if (element == value) 
            	{return true;}
        }
        return false;
    }

    public static double readBet()
    {
    	Scanner input1 = new Scanner(System.in);
    	double bet = input1.nextDouble();
    	
    	while (!(bet>=20 && bet<=500))
		{
			System.out.println("Invalid amount. Try again.");
			bet = input1.nextDouble();
		}
    	//input1.close();
    	return bet;
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




/*
 * TODO
 * - Entering money Limit 
 */




    public static void main(String[] args)
    {
    
	/******************** Declaring variables ********************/
    	
    	int nReels = 4;		//Number of spinning reels the machine has.
    	int nSymbols = 4;	//Number of possible symbols in each reel.
    	
		char[] results = new char[nReels];
		//Every position is obtained from one of the spinning reels.
		char[] symbolsFound = new char[nReels];
		int[] symbolsRepeated = new int[nReels];
		//For every position in both arrays:
		//	symbolsFound: a new different symbol found in the results.
		//	symbolsRepeated: the times that symbol is repeated in the results.
		
		Scanner input = new Scanner(System.in);
		char gameEnter;	//Player input to stop or continue the game.
		char gameInput;	//Player input for other methods.
		
		int newIndex;	//Position of a new different symbol found in the results.
		char MRS;		//Most Repeated Symbol.
		char LRS;		//Least Repeated Symbol
		int MRS_count;	//Times the Most Repeated Symbol is present in the results.
		int LRS_count;	//Times the Least Repeated Symbol is present in the results.
		
		double playerBet;	//Amount of money the player has bet.
		double playerSpent;	//Amount of money the player spent playing the game.
		double increase;	//Amount of money the player adds to their bet.
		double winAmount;	//Amount of money the player is awarded.
		
		
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
			System.out.print("\tMin 20, Max 500: ");
			playerBet = readBet();
			playerSpent = playerBet;
			
			while (gameEnter=='y'|| gameEnter=='Y')
			{
	            
		    	
			/*************** Assigning results ***************/
		    	
		        for (int index=0; index<nReels; index++) 
		        {
		            assignRandom(results, index, nSymbols);
		            //Values need to be reset for every loop
		            symbolsFound[index] = ' ';
		            symbolsRepeated[index] = 0;
		        }
		        
		        
			/*************** Display Spinning Reels ***************/ 
		        
	            //for (int reelsShown=0; reelsShown<=nReels; reelsShown++)
	            {
	                clearScreen();
	                
	                displayMachine(results, nReels);
	                
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
	            MRS_count = 0;
	            LRS_count = 10;
	            MRS = ' ';
	            LRS = ' ';
	            
	            for (int i=0; i<nReels; i++)
	            {
	                if (!found(symbolsFound, results[i]))
	                {
	                	symbolsFound[newIndex] = results[i];
	                    for (int j=0; j<nReels; j++)
	                    {
	                        if (results[i]==results[j])
	                        {
	                        	symbolsRepeated[newIndex]++;
	                        	if (symbolsRepeated[newIndex] > MRS_count) 
	                        	{
	                        		MRS_count = symbolsRepeated[newIndex]; 
	                        		MRS = results[i];
	                        	}
	                        }
	                        if (symbolsRepeated[newIndex] < LRS_count) 
                        	{
                        		LRS_count = symbolsRepeated[newIndex]; 
                        		LRS = results[i];
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
	            
	            /*There are no matches. Game Over*/
	            if (MRS_count==1) 
	            {
	            	System.out.println("You got no repeated symbols. You lost "+playerBet+" €.");
	            	winAmount = 0;
	            	gameEnter = 'n'; //ends loop
	            }
	            /*There is at least one match*/
	            else if (MRS_count>1) 
	            {
	        		System.out.println("You got the "+MRS+" symbol "+MRS_count+" times.\n");
	        		
	        		
	        		/*1 match away from jackpot (reroll)*/
	        		if (MRS_count==nReels-1)
	        		{
	        			System.out.println("You are one reel of from the jackpot.");
	        			System.out.print("Do you want to reroll it? (y/n): ");
	        			gameInput = input.next().charAt(0);
	        			
	        			if (gameInput=='y'||gameInput=='Y')
	        			{
	        				int MRS_index = posOfValue(results, MRS);
	        				int LRS_index = posOfValue(results, LRS);
	        				
	        				assignRandom(results, LRS_index, nSymbols);
	        				displayMachine(results, nReels);
	        				System.out.println("\n");
	        				
	        				if (Objects.equals(results[MRS_index], results[LRS_index]))
	        				{
	        					System.out.println("The new symbol is a match.");
	        					MRS_count = nReels;
	        				}
	        				else {System.out.println("The new symbol is still different.");}
	        			}
	        			
	        		}
	        		
	        		/*Matches in all reels: jackpot. Game Over*/
	        		else if (MRS_count==nReels)
	        		{
	        			winAmount = playerBet*100;
	        			gameEnter = 'n'; //ends loop
	        			System.out.print("\n\tYou won the jackpot!!!");
		            	System.out.println("\n\t  You have spent "+playerSpent+" €.");
		            	System.out.println("\t    And have won "+winAmount+" €.");
	        		}
	        		
	        		/*Matches between 1 and max*/
	        		else
	        		{
		        		if (MRS_count > nReels/2)
		                {
		        			winAmount = playerBet*10; /* 3 matches: prize*/
		        			System.out.println("You now have "+winAmount+" €.");
		        		}
		        		else //MRS_count <= nReels/2
		        		{
		        			winAmount = playerBet; /* 2 matches: no change*/
		        			System.out.println("You still have "+winAmount+" €.");
		        		}
	        			
	        			
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
			                	increase = readBet();
			                	playerBet += increase; //The money added is used in the next loop
			                	playerSpent += increase; //Keeps track of money entered 
			                }
			                
			            	clearScreen(); //Game restart whether player adds money or not
			            }
			            else
			            {
			            	System.out.println("\n\t  You have spent "+playerSpent+" €.");
			            	System.out.println("\t    And have won "+winAmount+" €.");
			            }
		            }
		        	
	        		
	            }
	            
		    }
        	System.out.println("...");
    	}
		input.close();
    }
    
}
