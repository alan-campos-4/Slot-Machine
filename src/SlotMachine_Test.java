import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class SlotMachine_Test
{
	
	@Test
	void exists_test()
	{
		char[] arr = {'a','B','P','7','@',',','.'};
		assertTrue(SlotMachine.exists(arr,'P'));
		assertFalse(SlotMachine.exists(arr,'p'));
	}
	
	
	
	
	
	
	@Test
	void Select_Test()
	{
		int op;
		int methodNum = 0;
		
		do {
			SlotMachine.clear();
			System.out.println("\n\t ----- "+getMethodName(methodNum)+" ----- \n");
			System.out.println(" 1. Test single row reroll.");
			System.out.println(" 2. Test multiway reroll.");
			System.out.println(" 3. Test multiway diagonal 1 check.");
			System.out.println(" 4. Test multiway diagonal 2 check.");
			System.out.println(" 0. Finish.");
			op = SlotMachine.readInput("Choose an option",0,' ',4);
			
			if (op!=0)
			{
				SlotMachine.clear();
				System.out.println("\t----- "+getMethodName(methodNum+op)+" -----");
				switch(op)
				{
					case 1:	{Reroll_SingleRow_Test();}	break;
					case 2:	{Reroll_Multiway_Test();}	break;
					case 3:	{Diagonal_1_Multiway_Test();}	break;
					case 4:	{Diagonal_2_Multiway_Test();}	break;
				}
				SlotMachine.pressAnyKeyTo("continue");
			}
		
		} while (op!=0);
		System.out.println("\n\t ----- End ----- ");
		assertTrue(op==0);
	}
	
	
	
	
	String getMethodName(int pos)	{return SlotMachine_Test.class.getDeclaredMethods()[pos].getName();}
	
	
	void Reroll_SingleRow_Test()
	{
		SlotMachine.SingleRow M1 = new SlotMachine.SingleRow(6, 4);
		
		M1.create_Reroll();
		M1.displayReels();
		M1.checkResults();
		M1.generatePayout();
		
	}
	
	
	void Reroll_Multiway_Test()
	{
		SlotMachine.Multiway M2 = new SlotMachine.Multiway(3, 4);
		
		M2.create_Reroll();
		M2.displayReels();
		M2.checkResults();
		M2.generatePayout();
		
	}
	
	
	void Diagonal_1_Multiway_Test()
	{
		SlotMachine.Multiway M2 = new SlotMachine.Multiway(3, 4);
		
		int startPos=0, nextPos=0;
		for (int i=0; i<M2.reels; i++)
		{
			startPos = M2.reels-1-i;
			
			M2.generateReelValue(i, startPos, nextPos);
		}
		
		M2.displayReels();
		M2.checkResults();
		M2.generatePayout();
		
	}
	
	
	
	void Diagonal_2_Multiway_Test()
	{
		SlotMachine.Multiway M2 = new SlotMachine.Multiway(5, 5);
		
		int startPos=0, nextPos=0;
		for (int i=0; i<M2.reels; i++)
		{
			startPos = i;
			
			M2.generateReelValue(i, startPos, nextPos);
		}
		
		//The rest of the program
		M2.displayReels();
		M2.checkResults();
		M2.generatePayout();
		
	}
	
	
	
	
}