import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class SlotMachine_Test 
{
	
	
//*
	@Test
	void countSymbolsFound_Test()
	{
		char[] arr = {'E','@','l','E','@','l','@','-','@'};
		SlotMachine.Results = new char[arr.length];
		for (int i=0; i<arr.length; i++) {SlotMachine.Results[i] = arr[i];}
		
		
		SlotMachine.countSymbolsFound();
		
		assertTrue(SlotMachine.MRS.getSym()=='@');
		assertTrue(SlotMachine.MRS.getCount()==	4);
		//assertTrue(SlotMachine.MRS.getPos()==1);
		assertTrue(SlotMachine.LRS.getSym()=='-');
		assertTrue(SlotMachine.LRS.getCount()==1);
		//assertTrue(SlotMachine.LRS.getPos()==7);
		
		//assertFalse(SlotMachine.MRS.getPos()==3);
		//assertFalse(SlotMachine.LRS.getPos()==4);
	}
//*/
	
	
		
//*
	@Test
	void reroll_Test()
	{
		int nReels = SlotMachine.nReels;
		SlotMachine.Results = new char[nReels];
		
		//Creates two different symbols
		char sym1 = SlotMachine.spinReel(), sym2;
		do {sym2 = SlotMachine.spinReel();} while(sym2==sym1);
		
		//Assigns one symbol to a random position and the other to the rest
		for (int i=0; i<nReels; i++) {SlotMachine.Results[i] = sym1;}
		SlotMachine.Results[(int)(Math.random()*nReels)] = sym2;
		
		//Performs the re-rolling
		SlotMachine.displayMachine(nReels);
		SlotMachine.countSymbolsFound();
		SlotMachine.reroll();
	}
//*/

}
