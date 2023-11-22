import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class DAM_Proyecto_Test 
{
	

	@Test
	void found_Test() 
	{
		char[] arr = {' ','E','@','#','-'};
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, ' '));
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, '-'));
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, '#'));
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, 'E'));
		assertFalse(DAM_Proyecto_Tragaperras.found(arr, 'e'));
		assertFalse(DAM_Proyecto_Tragaperras.found(arr, 'k'));
	}


	@Test
	void symbolCount_Test()
	{
		//
		char[] arr = {'@','E','@','#','-','@','#','E'};
		DAM_Proyecto_Tragaperras.symbolCount(arr);
		assertTrue(DAM_Proyecto_Tragaperras.MRcount==3);
		assertTrue(DAM_Proyecto_Tragaperras.MRindex==0);
		assertTrue(DAM_Proyecto_Tragaperras.LRcount==1);
		assertTrue(DAM_Proyecto_Tragaperras.LRindex==4);
		assertFalse(DAM_Proyecto_Tragaperras.LRindex==0);
		
	}


	@Test
	void reroll_Test()
	{
		// Defines values for the test
		int nReels = DAM_Proyecto_Tragaperras.nReels;
		char sym1 = DAM_Proyecto_Tragaperras.spinReel(); //Most repeated symbol
		char sym2; //Different symbol
		do{ 
			sym2 = DAM_Proyecto_Tragaperras.spinReel(); 
		}while(sym2==sym1); //Make sure they're not the same
		
		
		// Assigns the most repeated symbol
		for (int i=0; i<nReels; i++)
			{DAM_Proyecto_Tragaperras.Results[i] = sym1;}
		
		// Assigns the other symbol to a random position
		DAM_Proyecto_Tragaperras.Results[(int)(Math.random()*nReels)] = sym2;
		
		
		// Calculates the most repeated Symbol
		DAM_Proyecto_Tragaperras.symbolCount(DAM_Proyecto_Tragaperras.Results);

		// Displays results and re-rolls
		DAM_Proyecto_Tragaperras.displayMachine(nReels);
		DAM_Proyecto_Tragaperras.reroll();
	}


}
