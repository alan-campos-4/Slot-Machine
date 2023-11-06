import java.util.Scanner;



public class DAM_Proyecto_Tragaperras 
{

	public static void clearScreen() 
	{	
		System.out.println("\n");	//1
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//5
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//10
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//15
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//20
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");	//25
        System.out.flush();
	}


    public static boolean check(char[] arr, char value)
    {
        for (int element: arr)
        {
            if (element == value) {return true;}
        }
        return false;
    }
    
    public static int indexMaxValue(int[] arr)
    {
        int position = 0, maximum = 0;
        for (int element: arr)
        {
            if (arr[element] >= maximum)
            {
            	maximum = arr[element]; 
                position = element;
            }
        }
        return position;
    }



    public static void main(String[] args)
    {
    	
	/******************** Declaring variables ********************/
    	
    	int num_Reels = 4;	//Number of spinning reels the machine has
    	int numSymbols = 4;	//Number of possible symbols in each reel
    	
		char[] results = new char[num_Reels];
		//Every position is the result of a spinning reel
		char[] symbolsFound = new char[num_Reels];
		//Every position is a new different symbol found in the results array
		int[] symbolsRepeated = new int[num_Reels];
		//symbolsRepeated[a] is the times the symbol in symbolsFound[a] is repeated
		
		Scanner input = new Scanner(System.in);
		char playerInput;	//Input for player interaction
		
		int pos_found;		//Position for a new symbol found.
		int matchIndex;		//Position of the most repeated symbol.
		int matchCount;		//Times the most repeated symbol appears. Can be 0 if no symbols match.
		
		double playerBet;	//Amount of money the player has bet
		//double win_chance;	//Percentage of winning the maximum prize.
		//double win_mod;		//Modifier applied to the bet to calculate prize.
		double win_amount;	//Amount of money the player wins.
		
		
		
		System.out.print("Do you want to play (y/n): ");
		playerInput = input.next().charAt(0);
		System.out.println("\nEnter how much money you want to bet: ");
		playerBet = input.nextInt();
		
		while (playerInput == 'y'|| playerInput=='Y') 
		{
            
	    	
	/*************** Spinning the reels and resetting values ***************/
	    	
	    	
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
	        
	        
	/*************** Display Spinning Reels ***************/ 
	        
	        
            for (int current_reel=0; current_reel<=num_Reels; current_reel++)
            {
                clearScreen();

                for (int i=0; i<num_Reels; i++)	{System.out.print(" _____");}
                System.out.print("\n|");
                for (int i=0; i<num_Reels; i++)	{System.out.print("     |");}
                System.out.print("\n|");
                for (int i=0; i<num_Reels; i++)
                {
                    if (current_reel>i)
                        System.out.print("  "+results[i]+"  |");
                	else 
                        System.out.print("     |");
                }
                System.out.print("\n|");
                for (int i=0; i<num_Reels; i++) {System.out.print("_____|");}
                System.out.println();

                if (current_reel!=num_Reels)
                {
                    System.out.print((current_reel==0)? "\nStart (p): ":"\nNext reel (p): ");
                    do {playerInput = input.next().charAt(0);}
                    while(playerInput!='p');
                }
            }
            
            
	/*************** Calculating player payout ****************/
            
            pos_found = 0;
            matchCount = 0;
            matchIndex = -1;
            
            for (int i=0; i<num_Reels; i++)
            {
                if (!check(symbolsFound, results[i]))
                {
                	symbolsFound[pos_found] = results[i];
                	symbolsRepeated[i] = 0;
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
                    pos_found++;
                }
            }
            
            //win_chance = (Math.pow((1/numSymbols), num_Reels))*100;
            //System.out.printf("\n\nThe chances of winning were %.4f percent.\n\n", win_chance);
            
            
            System.out.println();
	        
            if ( (matchCount==0) || (matchIndex==-1) )
            {
            	System.out.println("You got no matches. You lost. gtfo");
            } 
            else 
            {
        		System.out.println("You got the "+symbolsFound[matchIndex]+" symbol "+(matchCount+1)+" times.");
        		
        		if (matchCount==1)					{win_amount = playerBet*1.0;}
        		else if (matchCount==num_Reels-1)	{win_amount = playerBet*100.0;}
        		else if (matchCount<=num_Reels/2)	{win_amount = playerBet*10.0;}
        		else								{win_amount = playerBet*20.0;}
        		        		
        		if (win_amount > playerBet)
        			{System.out.println("You won "+(win_amount-playerBet)+"€ and now have "+win_amount+"€.\n");}
        		else if (win_amount < playerBet)
                	{System.out.println("You lost "+(playerBet-win_amount)+"€ and now have "+win_amount+"€.\n");}
        		else //win_amount == playerBet
        			{System.out.println("You retain the amount of money you had: "+win_amount+" €.\n");}
                
                
                System.out.println("Do you want continue playing? (y)");
                System.out.println("Or do you take your earnings? (n)");
                System.out.print("    (y/n): ");
                playerInput = input.next().charAt(0);
                
                
                if (playerInput == 'y'|| playerInput=='Y')
                {
                	playerBet = win_amount;
                	
                	System.out.print("Do you want to bet more money? (y/n): ");
                    playerInput = input.next().charAt(0);
                    if (playerInput == 'y'|| playerInput=='Y')
                    {
                    	win_amount = input.nextInt();
                    	playerBet += win_amount;
                    }
                    else {playerInput = 'y';}
                    	
                	clearScreen();
                }
                else
                {
                	System.out.println("\n\t  You got "+win_amount+" €");
                }
        		
                
            }
            
            
            
            
            
            
            
	    }
	    
        input.close();
        
        
        
/* TODO
 * 		Clear the terminal
 * ---- Increase price after spinning again
 * ---- Moved the results generator into the loop
 * ---- Playing again after spinning
 * ---- Enter bet and calculate reward
 * ¬/ Lever (player input for each step)
 * ¬/ Display match (most repeated symbol) 
 */


    }
	
}
