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
	
	
	public static void displayMachine(char[] arr, int show)
	{
		for (int slot=0; slot<arr.length; slot++) {System.out.print(" _____");}
		
		System.out.print("\n|");
		for (int slot=0; slot<arr.length; slot++) {System.out.print("     |");}
        
		System.out.print("\n|");
		for (int slot : arr)
        	{System.out.print((show>slot) ? "  "+arr[slot]+"  |" : "     |" );}
		
        //for (int slot=0; slot<arr.length; slot++)
		/*{
			if (show>slot)	{System.out.print("  "+arr[slot]+"  |");}
			else			{System.out.print("     |");}
		}*/
        
        System.out.print("\n|");
        for (int slot=0; slot<arr.length; slot++) {System.out.print("_____|");}
	}
	
	public static void wait(int ms)
	{
		try 
			{Thread.sleep(ms);} 
		catch (InterruptedException e) 
			{Thread.currentThread().interrupt();}
	}
	
	
    public static boolean check(char[] arr, char value)
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
		char playerInput;	//Input for player interaction.
		
		int newfoundIndex;	//Position of a new different symbol found in the results.
		int repeatIndex;	//Position of the most repeated symbol.
		int repeatCount;	//Times the most repeated symbol is repeated.
		
		double playerBet;	//Amount of money the player has bet.
		double playerSpent;	//Amount of money the player spent playing the game.
		double increase;	//Amount of money the player adds to their bet.
		double winAmount;	//Amount of money the player is awarded.
		
		
		
		System.out.print("Do you want to play (y/n): ");
		playerInput = input.next().charAt(0);
		System.out.println("\nEnter how much money you want to bet: ");
		playerBet = input.nextInt();
		playerSpent = playerBet;
		
		while (playerInput=='y'|| playerInput=='Y') 
		{
            
	    	
	/*************** Assigning results and resetting loop values ***************/
	    	
	        for (int index=0; index<nReels; index++) 
	        {
	            int numrand = (int)(Math.random()*nSymbols);
	            switch(numrand)
	            {
	                case 0:  results[index]='o';  break;
	                case 1:  results[index]='!';  break;
	                case 2:  results[index]='@';  break;
	                case 3:  results[index]='#';  break;
	                case 4:  results[index]='$';  break;
	                case 5:  results[index]='%';  break;
	                case 6:  results[index]='&';  break;
	                case 7:  results[index]='~';  break;
	                case 8:  results[index]='\\'; break;
	                case 9:  results[index]='/';  break;
	                default: results[index]='_';  break;
	            }
	            symbolsFound[index] = ' ';
	            symbolsRepeated[index] = 0;
	        }
            newfoundIndex = 0;
            repeatCount = 0;
            repeatIndex = -1;
            
	        
	/*************** Display Spinning Reels ***************/ 
	        
            for (int reelsShown=0; reelsShown<=nReels; reelsShown++)
            {
                clearScreen();
                
                displayMachine(results, reelsShown);
                
                wait(500);
                
                System.out.println("\n");
                
                if (reelsShown!=nReels)
                {
                    System.out.print(((reelsShown==0)?"Start":"Next reel")+" (p): ");
                    do {playerInput = input.next().charAt(0);}
                    while(playerInput!='p');
                }
            }
            
            
	/*************** Calculating player payout ****************/
            
            for (int i=0; i<nReels; i++)
            {
                if (!check(symbolsFound, results[i]))
                {
                	symbolsFound[newfoundIndex] = results[i];
                	newfoundIndex++;
                    for (int j=0; j<nReels; j++)
                    {
                        if ( (i!=j) && (results[i]==results[j]) )
                        {
                        	symbolsRepeated[i]++;
                        	if (repeatCount < symbolsRepeated[i]) 
                        	{
                        		repeatCount = symbolsRepeated[i]; 
                        		repeatIndex = i;
                        	}
                        }
                    }
                }
            }
            
            if ( (repeatCount==0) || (repeatIndex==-1) )
            {
            	System.out.println("You got no repeated symbols. You lost.");
            	winAmount = 0;
            }
            else 
            {
        		System.out.println("You got the "+symbolsFound[repeatIndex]+
        							" symbol "+(repeatCount+1)+" times.\n");
        		
        		if (repeatCount==1)				{winAmount = playerBet;}
        		else if (repeatCount==nReels-1)	{winAmount = playerBet*100.0;}
        		else if (repeatCount<=nReels/2)	{winAmount = playerBet*10.0;}
        		else							{winAmount = playerBet*20.0;}
        		        		
        		if (winAmount > playerBet)
    			{
        			System.out.println("You gained "+(winAmount-playerBet)+"€.");
        			System.out.println("And went from "+playerBet+" to "+winAmount+" €.");
    			}
        		else if (winAmount < playerBet)
                {
        			System.out.println("You lost "+(playerBet-winAmount)+"€.");
        			System.out.println("And went from "+playerBet+" to "+winAmount+" €.");
        		}
        		else //winAmount == playerBet
        		{
        			System.out.println("You retain the amount of money you had: ");
        			System.out.println(winAmount+" €.");
        		}
        		System.out.println();
                System.out.print("Do you want continue playing? (y/n): ");
                playerInput = input.next().charAt(0);
            }
            
            if ( (repeatCount>0) && (playerInput=='y'|| playerInput=='Y') )
            {
            	playerBet = winAmount;
            	
            	System.out.print("Do you want to bet more money? (y/n): ");
                playerInput = input.next().charAt(0);
                
                if (playerInput=='y'|| playerInput=='Y')
                {
                	System.out.print("How much money do you want to add: ");
                	increase = input.nextInt();
                	playerBet += increase;
                	playerSpent += increase;
                }
                else {playerInput = 'y';}
                
            	clearScreen();
            }
            else
            {
            	System.out.println("\n\t You have spent "+playerSpent+" €");
            	System.out.println("\t   And have won "+winAmount+" €");
            }

	    }
        input.close();
        
    }
}
