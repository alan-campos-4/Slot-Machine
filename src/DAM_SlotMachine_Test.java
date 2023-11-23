import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class DAM_SlotMachine_Test 
{
	
	char[] arr = {'@','E','@','#','-','@','l','#','E','l'};
	
	
	
	@Test
	void found_Test() 
	{
		assertTrue(DAM_SlotMachine.found(arr, '@'));
		assertTrue(DAM_SlotMachine.found(arr, '-'));
		assertTrue(DAM_SlotMachine.found(arr, '#'));
		assertTrue(DAM_SlotMachine.found(arr, 'E'));
		assertFalse(DAM_SlotMachine.found(arr, 'e'));
		assertFalse(DAM_SlotMachine.found(arr, 'L'));
	}


	@Test
	void symbolCount_Test()
	{
		// Save initial length to restore later
		int len = DAM_SlotMachine.nReels;
		
		// Change results to test symbolCount
		DAM_SlotMachine.Results = new char[arr.length];
		for (int i=0; i<arr.length; i++)
			{DAM_SlotMachine.Results[i] = arr[i];}
			
		DAM_SlotMachine.symbolCount();
		assertTrue(DAM_SlotMachine.MRcount==3);
		assertTrue(DAM_SlotMachine.MRindex==0);
		assertTrue(DAM_SlotMachine.LRcount==1);
		assertTrue(DAM_SlotMachine.LRindex==4);
		assertFalse(DAM_SlotMachine.MRindex==1);
		assertFalse(DAM_SlotMachine.LRindex==0);
		
		// Restore original length
		DAM_SlotMachine.Results = new char[len];
	}


	@Test
	void reroll_Test()
	{
		// Defines values for the test
		int nReels = DAM_SlotMachine.Results.length;
		char sym1 = DAM_SlotMachine.spinReel(); //Most repeated symbol
		char sym2; //Different symbol
		do {
			sym2 = DAM_SlotMachine.spinReel(); 
		} while(sym2==sym1); //Make sure they're not the same
		
		
		// Assigns the most and least repeated symbols
		for (int i=0; i<nReels; i++)
			{DAM_SlotMachine.Results[i] = sym1;}
		DAM_SlotMachine.Results[(int)(Math.random()*nReels)] = sym2;
		
		
		// Calculates the most repeated Symbol
		DAM_SlotMachine.symbolCount();
		// Displays the results and and the re-roll
		DAM_SlotMachine.displayMachine(nReels);
		DAM_SlotMachine.reroll();
	}
//*/

}
