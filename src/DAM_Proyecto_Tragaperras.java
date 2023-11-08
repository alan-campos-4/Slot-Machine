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
	
	
	public static void displayReels(char[] arr , int lngth, int reel)
	{
		for (int i=0; i<lngth; i++)	{System.out.print(" _____");}
        System.out.print("\n|");
        for (int i=0; i<lngth; i++)	{System.out.print("     |");}
        System.out.print("\n|");
        for (int i=0; i<lngth; i++)	{System.out.print((reel>i)? "  "+arr[i]+"  |":"     |");}
        System.out.print("\n|");
        for (int i=0; i<lngth; i++)	{System.out.print("_____|");}
        System.out.println();
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
        for (int element: arr)
        {
            if (element == value) 
            	{return true;}
        }
        return false;
    }
    
    
    
    public static void main(String[] args)
    {
    	
	/******************** Declaring variables ********************/
    	
    	int num_Reels = 4;	//Number of spinning reels the machine has.
    	int numSymbols = 4;	//Number of possible symbols in each reel.
    	
		char[] results = new char[num_Reels];
		//Every position is the result of a spinning reel.
		char[] symbolsFound = new char[num_Reels];
		//Every position is a new different symbol found in the results array.
		int[] symbolsRepeated = new int[num_Reels];
		//symbolsRepeated[a] is the times the symbol in symbolsFound[a] is repeated.
		
		Scanner input = new Scanner(System.in);
		char playerInput;	//Input for player interaction.
		
		int pos_found;		//Position for a new symbol found.
		int matchIndex;		//Position of the most repeated symbol.
		int matchCount;		//Repetitions of the most repeated symbol. =0 if no symbols match.
		
		double playerBet;	//Amount of money the player has bet.
		double win_amount;	//Amount of money the player wins.
		
		
		
		System.out.print("Do you want to play (y/n): ");
		playerInput = input.next().charAt(0);
		System.out.println("\nEnter how much money you want to bet: ");
		playerBet = input.nextInt();
		
		while (playerInput == 'y'|| playerInput=='Y') 
		{
            
	    	
	/*************** Assigning results and resetting loop values ***************/
	    	
	        for (int index=0; index<num_Reels; index++) 
	        {
	            int numrand = (int)(Math.random()*numSymbols);
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
	            symbolsFound[index]=' ';
	            symbolsRepeated[index]=0;
	        }
	        
            pos_found = 0;
            matchCount = 0;
            matchIndex = -1;
            
	        
	/*************** Display Spinning Reels ***************/ 
	        
            for (int current_reel=0; current_reel<=num_Reels; current_reel++)
            {
                clearScreen();
                
                displayReels(results, results.length, current_reel);
                
                wait(500);
                
                System.out.println();
                
                if (current_reel!=num_Reels)
                {
                    System.out.print(((current_reel==0)?"Start":"Next reel")+" (p): ");
                    do {playerInput = input.next().charAt(0);}
                    while(playerInput!='p');
                }
            }
            
            
	/*************** Calculating player payout ****************/
            
            for (int i=0; i<num_Reels; i++)
            {
                if (!check(symbolsFound, results[i]))
                {
                	symbolsFound[pos_found] = results[i];
                	pos_found++;
                    for (int j=0; j<num_Reels; j++)
                    {
                        if ((i!=j) && (results[i]==results[j]))
                        {
                        	symbolsRepeated[i]++;
                        	if (matchCount < symbolsRepeated[i]) 
                        	{
                        		matchCount = symbolsRepeated[i]; 
                        		matchIndex = i;
                        	}
                        }
                    }
                }
            }
            
            if ( (matchCount==0) || (matchIndex==-1) )
            	{System.out.println("You got no matches. You lost.");} 
            else 
            {
        		System.out.println("You got the "+symbolsFound[matchIndex]+
        							" symbol "+(matchCount+1)+" times.");
        		
        		if (matchCount==1)					{win_amount = playerBet;}
        		else if (matchCount==num_Reels-1)	{win_amount = playerBet*100.0;}
        		else if (matchCount<=num_Reels/2)	{win_amount = playerBet*10.0;}
        		else								{win_amount = playerBet*20.0;}
        		        		
        		if (win_amount > playerBet)
    			{
        			System.out.println("You gained "+(win_amount-playerBet)+"€.");
        			System.out.println("And now have "+win_amount+" €.");
    			}
        		else if (win_amount < playerBet)
                {
        			System.out.println("You lost "+(playerBet-win_amount)+"€.");
        			System.out.println("And now have "+win_amount+" €.");
        		}
        		else //win_amount == playerBet
        		{
        			System.out.println("You retain the amount of money you had: ");
        			System.out.println(win_amount+" €.");
        		}
        		System.out.println();
                System.out.print("Do you want continue playing? (y/n): ");
                playerInput = input.next().charAt(0);
                
                
                if (playerInput == 'y'|| playerInput=='Y')
                {
                	playerBet = win_amount;
                	
                	System.out.print("Do you want to bet more money? (y/n): ");
                    playerInput = input.next().charAt(0);
                    
                    if (playerInput == 'y'|| playerInput=='Y')
                    {
                    	System.out.print("How much money do you want to add: ");
                    	win_amount = input.nextInt();
                    	playerBet += win_amount;
                    }
                    else {playerInput = 'y';}
                    
                	clearScreen();
                }
                else
                	{System.out.println("\n\t You got "+win_amount+" €");}
            }

	    }
        input.close();
        
    }
}
