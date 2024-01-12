import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



class SlotMachine_Test 
{


	@Test
	void found_Test() 
	{
		SlotMachine.Symbol s1 = new SlotMachine.Symbol('#');
		SlotMachine.Symbol s2 = new SlotMachine.Symbol('-');
		SlotMachine.Symbol s3 = new SlotMachine.Symbol('@');
		SlotMachine.Symbol s4 = new SlotMachine.Symbol('l');
		SlotMachine.Symbol s5 = new SlotMachine.Symbol('E');
		SlotMachine.Symbol[] arr = {s1, s2, s3, s4, s5};
		
		assertTrue(SlotMachine.exists(arr, '@'));
		assertTrue(SlotMachine.exists(arr, '-'));
		assertTrue(SlotMachine.exists(arr, '#'));
		assertTrue(SlotMachine.exists(arr, 'E'));
		assertFalse(SlotMachine.exists(arr, 'e'));
		assertFalse(SlotMachine.exists(arr, 'L'));
	}




	@Test
	void countSymbolsFound_Test()
	{
		int len = SlotMachine.nReels;
		
		char[] arr = {'@','E','E','@','E','-','@','@'};
		SlotMachine.Results = new char[arr.length];
		
		for (int i=0; i<arr.length; i++) {SlotMachine.Results[i] = arr[i];}
		
		SlotMachine.countSymbolsFound();
		assertTrue(SlotMachine.Results[SlotMachine.MRS.getP()]=='@');
		assertTrue(SlotMachine.MRS.getS()=='@');
		assertTrue(SlotMachine.MRS.getC()==4);
		assertTrue(SlotMachine.MRS.getP()==0);
		assertTrue(SlotMachine.Results[SlotMachine.LRS.getP()]=='-');
		assertTrue(SlotMachine.LRS.getS()=='-');
		assertTrue(SlotMachine.LRS.getC()==1);
		assertTrue(SlotMachine.LRS.getP()==5);
		assertFalse(SlotMachine.MRS.getP()==3);
		assertFalse(SlotMachine.LRS.getP()==6);
		
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
		
		SlotMachine.countSymbolsFound();
		SlotMachine.displayMachine(nReels);
		SlotMachine.reroll();
	}


}
