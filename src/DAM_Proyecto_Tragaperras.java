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
    
    public static int posOfValue(int[] arr, int value)
    {
    	for (int element : arr)
        {
            if (element == value) 
            	{return element;}
        }
        return -1;
    }
    
    public static int posOfValue(char[] arr, char value)
    {
    	for (int element : arr)
        {
            if (arr[element] == value)
            	{return element;}
        }
        return -1;
    }




/*
 * TODO
 * - Re-roll if one reel away from jackpot
 * 		find different reel:	
 * 			symbol found 1 time
 *			position in results
 * 		re-spin different reel:	method?
 * 		re-evaluate results:	??!!
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
		
		boolean reroll = false;
		int newIndex;	//Position of a new different symbol found in the results.
		int MRS_index;	//Position of the Most Repeated Symbol.
		int MRS_count;	//Times the Most Repeated Symbol is present in the results.
		int LRS_index;	//Position of the Least Repeated Symbol
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
			System.out.print("Enter how much money you want to bet: ");
			playerBet = input.nextInt();
			playerSpent = playerBet;
			
			while (gameEnter=='y'|| gameEnter=='Y')
			{
	            
		    	
		/*************** Assigning results ***************/
		    	
		        for (int index=0; index<nReels; index++) 
		        {
		            assignRandom(results, index, nSymbols);
		            symbolsFound[index] = ' ';
		            symbolsRepeated[index] = 0;
		        }
	            
	            
	            //char results[] = {'#','#','@','#'};
	            
		        
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
	            
	            
		/*************** Calculating player prize ****************/
	            
	            newIndex = 0;
	            MRS_count = 0;
	            LRS_count = 10;
	            MRS_index = -1;
	            LRS_index = -1;
	            
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
	                        		MRS_index = i;
	                        	}
	                        }
	                        if (symbolsRepeated[newIndex] < LRS_count) 
                        	{
                        		LRS_count = symbolsRepeated[newIndex]; 
                        		LRS_index = i;
                        	}
	                    }
	                    newIndex++;
	                }
	            }
	            
	            if (MRS_count==1)
	            {
	            	System.out.println("You got no repeated symbols. You lost.");
	            	winAmount = 0;
	            	reroll = false;
	      		}
	            else 
	            {
	        		System.out.println("You got the "+symbolsFound[MRS_index]+
	        							" symbol "+MRS_count+" times.\n");
	        		
	        		if (MRS_count==nReels-1)
	        		{
	        			System.out.println("You are very close to winning the prize.");
	        			System.out.print("Do you want to reroll the last reel? (y/n): ");
	        			gameInput = input.next().charAt(0);
	        			
	        			if (gameInput=='y'||gameInput=='Y')
	        			{
	        				assignRandom(results, LRS_index, nSymbols);
	        				displayMachine(results, nReels);
	        				System.out.println("\n");
	        				
	        				if (Objects.equals(results[MRS_index], results[LRS_index]))
	        				{
	        					MRS_count++;
	        				}
	        			}
	        			
	        		}
	        		
	            }
            
	            
	        	if (MRS_count>1)	
	        	{
	        		/*
	        		
	        		if you get the same symbol 3 times or more you win a prize.");
					if you get the same symbol 2 times you keep what you bet.");
					if you get the same symbol in all reels you win the jackpot.");
					if you don't get any repeated symbols you lose.");
	        		
	        		*/
	        		if (MRS_count==nReels)			{winAmount = playerBet*100;}
	        		else if (MRS_count>nReels/2)	{winAmount = playerBet*10;}
	        		else							{winAmount = playerBet;}
	        		
	        		if (winAmount==playerBet)
	        		{
	        			System.out.println("You still have "+winAmount+" €.");
	        		}
	        		else
	                {
	        			System.out.println("You now have "+winAmount+" €.");
	        		}
	        		
	        		System.out.println();
	                System.out.print("Do you want continue playing? (y/n): ");
	                gameEnter = input.next().charAt(0);
	                
	                if ( (MRS_count<nReels) && (gameEnter=='y'|| gameEnter=='Y') )
		            {
		            	playerBet = winAmount;
		            	
		            	System.out.print("Do you want to bet more money? (y/n): ");
		            	gameInput = input.next().charAt(0);
		                
		                if (gameInput=='y'|| gameInput=='Y')
		                {
		                	System.out.print("How much money do you want to add: ");
		                	increase = input.nextInt();
		                	playerBet += increase;
		                	playerSpent += increase;
		                }
		                
		            	clearScreen();
		            }
		            else
		            {
		            	if (MRS_count==nReels)
		            		{System.out.print("\n\tYou won the jackpot!!!");}
		            	System.out.println("\n\t  You have spent "+playerSpent+" €");
		            	System.out.println("\t    And have won "+winAmount+" €");
		            }
		            
		            System.out.println("...");
	        	}
	            
	            
		    }
        	input.close();
    	}
    }
}
