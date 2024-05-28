import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class SlotsLayout_Test
{

	@Test
	void test()
	{
		//
		
	}
	
	
	String getMethodName(int pos)	{return SlotMachine_Test.class.getDeclaredMethods()[pos].getName();}
	
	void Reroll_SingleRow_Test()
	{
		//Create the machine
		SlotMachine.SingleRow M1 = new SlotMachine.SingleRow(6, 4);
		
		//Create random symbols
		char sym1, sym2;
		sym1 = M1.arrSyms[SlotMachine.rand.nextInt(M1.arrSyms.length)];
		do {sym2 = M1.arrSyms[SlotMachine.rand.nextInt(M1.arrSyms.length)];} 
		while (sym2==sym1);
		
		//Assign the symbols
		for (int i=0; i<M1.reels; i++)		{M1.arrResults[0][i] = sym1;}
		M1.arrResults[0][SlotMachine.rand.nextInt(M1.arrSyms.length)] = sym2;
		
		//Calculate re-roll
		M1.displayReels();
		M1.checkResults();
		M1.generatePayout();
		
		SlotsLayout.SingleRow1 S = new SlotsLayout.SingleRow1(M1);
	}
	
}
