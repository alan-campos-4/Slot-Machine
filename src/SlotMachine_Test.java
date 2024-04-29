import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class SlotMachines_Test
{
	
	@Test
	void exists_test()
	{
		char[] arr = {'a','B','P','7','@',',','.'};
		assertTrue(SlotMachines.exists(arr,'P'));
		assertFalse(SlotMachines.exists(arr,'p'));
	}
	
	
	
	
	
	
	@Test
	void Reroll_Test()
	{
		int op;
		int methodNum = 1;
		
		do {
			SlotMachines.clear();
			System.out.println("\n\t ----- "+getMethodName(methodNum)+" ----- \n");
			System.out.println(" 1. Test single row reroll.");
			System.out.println(" 2. Test multiway reroll.");
			System.out.println(" 0. Finish.");
			op = SlotMachines.readInput("Choose an option",0,' ',2);
			
			if (op!=0)
			{
				SlotMachines.clear();
				System.out.println("\t----- "+getMethodName(methodNum+op)+" -----");
				switch(op)
				{
					case 1:	{Reroll_SingleRow_Test();}	break;
					case 2:	{Reroll_Multiway_Test();}	break;
				}
				SlotMachines.pressAnyKeyTo("continue");
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
		SlotMachines.SingleRow M1 = new SlotMachines.SingleRow(6, 4);
		
		//Create random symbols
		char sym1, sym2;
		sym1 = M1.arrSyms[SlotMachines.rand.nextInt(M1.arrSyms.length)];
		do {sym2 = M1.arrSyms[SlotMachines.rand.nextInt(M1.arrSyms.length)];} 
		while (sym2==sym1);
		
		//Assign the symbols
		for (int i=0; i<M1.reels; i++)		{M1.arrResults[0][i] = sym1;}
		M1.arrResults[0][SlotMachines.rand.nextInt(M1.arrSyms.length)] = sym2;
		
		//Calculate re-roll
		M1.displayReels();
		M1.checkResults();
		M1.calculatePrize();
		
		
	}
	
	
	void Reroll_Multiway_Test()
	{
		//Create the machine
		SlotMachines.Multiway M2 = new SlotMachines.Multiway(3, 4);
		
		//Generate the two positions for the different sets of symbols
		int pos1, pos2;
		pos1 = SlotMachines.rand.nextInt(M2.arrSyms.length);
		do {pos2 = SlotMachines.rand.nextInt(M2.arrSyms.length);} 
		while (pos2==pos1);
		
		//Generate the position of the differing reel 
		int differReel = SlotMachines.rand.nextInt(M2.reels);
		
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