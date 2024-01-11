import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



class SlotMachine_Test 
{


	@Test
	void found_Test() 
	{
		char[] arr = {'#','-','@','l','E'};
		assertTrue(SlotMachine.found(arr, '@'));
		assertTrue(SlotMachine.found(arr, '-'));
		assertTrue(SlotMachine.found(arr, '#'));
		assertTrue(SlotMachine.found(arr, 'E'));
		assertFalse(SlotMachine.found(arr, 'e'));
		assertFalse(SlotMachine.found(arr, 'L'));
	}




	@Test
	void symbolCount_Test()
	{
		int len = SlotMachine.nReels;
		
		char[] arr = {'@','E','@','E','-','@'};
		SlotMachine.Results = new char[arr.length];
		for (int i=0; i<arr.length; i++)
			{SlotMachine.Results[i] = arr[i];}
			
		SlotMachine.symbolCount();
		assertTrue(SlotMachine.Results[SlotMachine.MRindex]=='@');
		assertTrue(SlotMachine.MRindex==0);
		assertTrue(SlotMachine.MRcount==3);
		assertTrue(SlotMachine.Results[SlotMachine.LRindex]=='-');
		assertTrue(SlotMachine.LRindex==4);
		assertTrue(SlotMachine.LRcount==1);
		assertFalse(SlotMachine.MRindex==1);
		assertFalse(SlotMachine.LRindex==0);
		
		SlotMachine.Results = new char[len];
	}




	@Test
	void reroll_Test()
	{
		SlotMachine.Results = new char[SlotMachine.nReels];
		int nReels = SlotMachine.Results.length;
		char sym1, sym2;
		
		sym1 = SlotMachine.spinReel();
		do {sym2 = SlotMachine.spinReel();} while(sym2==sym1);
		
		for (int i=0; i<nReels; i++)
			{SlotMachine.Results[i] = sym1;}
		SlotMachine.Results[(int)(Math.random()*nReels)] = sym2;
		
		SlotMachine.symbolCount();
		SlotMachine.displayMachine(nReels);
		SlotMachine.reroll();
	}


}
