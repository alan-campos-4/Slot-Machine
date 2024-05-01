import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class SlotMachines_Test
{
	
	@Test
	void exists_test()
	{
		char[] arr = {'a','B','P','7','@',',','.'};
		assertTrue(SlotMachine.exists(arr,'P'));
		assertFalse(SlotMachine.exists(arr,'p'));
	}
	
	
	
	
	
	
	@Test
	void Reroll_Test()
	{
		int op;
		int methodNum = 1;
		
		do {
			SlotMachine.clear();
			System.out.println("\n\t ----- "+getMethodName(methodNum)+" ----- \n");
			System.out.println(" 1. Test single row reroll.");
			System.out.println(" 2. Test multiway reroll.");
			System.out.println(" 0. Finish.");
			op = SlotMachine.readInput("Choose an option",0,' ',2);
			
			if (op!=0)
			{
				SlotMachine.clear();
				System.out.println("\t----- "+getMethodName(methodNum+op)+" -----");
				switch(op)
				{
					case 1:	{Reroll_SingleRow_Test();}	break;
					case 2:	{Reroll_Multiway_Test();}	break;
				}
				SlotMachine.pressAnyKeyTo("continue");
			}
		
		} while (op!=0);
		System.out.println("\n\t ----- End ----- ");
		assertTrue(op==0);
	}
	
	
	
	
	String getMethodName(int pos)
	{
		return SlotMachines_Test.class.getDeclaredMethods()[pos].getName();
	}
	
	
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
		M1.calculatePrize();
		
		
	}
	
	
	void Reroll_Multiway_Test()
	{
		//Create the machine
		SlotMachine.Multiway M2 = new SlotMachine.Multiway(3, 4);
		
		//Generate the two positions for the different sets of symbols
		int pos1, pos2;
		pos1 = SlotMachine.rand.nextInt(M2.arrSyms.length);
		do {pos2 = SlotMachine.rand.nextInt(M2.arrSyms.length);} 
		while (pos2==pos1);
		
		//Generate the position of the differing reel 
		int differReel = SlotMachine.rand.nextInt(M2.reels);
		
		//Generate the value of all the reels
		int startPos=0, nextPos=0;
		for (int i=0; i<M2.reels; i++)
		{
			if (i==differReel)	{startPos = pos1;}
			else				{startPos = pos2;}
			
			M2.arrResults[0][i] = M2.arrSyms[startPos];
			for (int j=1; j<M2.rows; j++)
			{
				if (startPos+j>=M2.arrSyms.length)
					{nextPos = startPos+j-M2.arrSyms.length;}
				else
					{nextPos = startPos+j;}
				
				M2.arrResults[j][i] = M2.arrSyms[nextPos];
			}
		}
		
		//The rest of the program
		M2.displayReels();
		M2.checkResults();
		M2.calculatePrize();
		
		
	}
	
	
	
	
}