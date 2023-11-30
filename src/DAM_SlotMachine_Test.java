import static org.junit.jupiter.api.Assertions.*;

import java.text.DecimalFormat;

import org.junit.jupiter.api.Test;



class DAM_SlotMachine_Test 
{


	@Test
	void menu_Test()
	{
		DAM_SlotMachine.menu('Y');
	}



/*
	@Test
	void compareCharUL_Test()
	{
		char ch1 = 'C', ch2 = 'c';
		//Character chK1 = Character.valueOf(ch1), chK2 = Character.valueOf(ch2);
		//assertTrue(ch1==ch2);
		//assertTrue(chK1.equals(chK2));
		//assertTrue(chK1.compareTo(chK2)==0);
		//assertTrue(Objects.equals(ch1, ch2));
		//assertTrue(Character.compare(ch1,ch2)==0);
		assertTrue(DAM_SlotMachine.compareCharUL(ch1, ch2));
	}




	@Test
	void found_Test() 
	{
		char[] arr = {'#','-','@','l','E'};
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
		int len = DAM_SlotMachine.nReels;
		
		char[] arr = {'@','E','@','E','-','@'};
		DAM_SlotMachine.Results = new char[arr.length];
		for (int i=0; i<arr.length; i++)
			{DAM_SlotMachine.Results[i] = arr[i];}
			
		DAM_SlotMachine.symbolCount();
		assertTrue(DAM_SlotMachine.Results[DAM_SlotMachine.MRindex]=='@');
		assertTrue(DAM_SlotMachine.MRindex==0);
		assertTrue(DAM_SlotMachine.MRcount==3);
		assertTrue(DAM_SlotMachine.Results[DAM_SlotMachine.LRindex]=='-');
		assertTrue(DAM_SlotMachine.LRindex==4);
		assertTrue(DAM_SlotMachine.LRcount==1);
		assertFalse(DAM_SlotMachine.MRindex==1);
		assertFalse(DAM_SlotMachine.LRindex==0);
		
		DAM_SlotMachine.Results = new char[len];
	}




	@Test
	void reroll_Test()
	{
		int nReels = DAM_SlotMachine.Results.length;
		char sym1, sym2;
		
		sym1 = DAM_SlotMachine.spinReel();
		do {sym2 = DAM_SlotMachine.spinReel();} while(sym2==sym1);
		
		for (int i=0; i<nReels; i++) {DAM_SlotMachine.Results[i] = sym1;}
		DAM_SlotMachine.Results[(int)(Math.random()*nReels)] = sym2;
		
		DAM_SlotMachine.symbolCount();
		DAM_SlotMachine.displayMachine(nReels);
		DAM_SlotMachine.reroll();
	}

//*/
}
