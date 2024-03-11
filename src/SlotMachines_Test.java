import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class SlotMachines_Test 
{
	
	@Test
	void isFound_Test()
	{
		char[] all = {'7','A','H','K','T','*','@','^','|','%','&','\\'};	
		assertTrue(SlotMachines.exists(all,'K'));
		assertTrue(SlotMachines.exists(all,'@'));
		assertTrue(SlotMachines.exists(all,'\\'));
		assertTrue(SlotMachines.exists(all,'7'));
		assertFalse(SlotMachines.exists(all,'a'));
		
		//int[] arrr = {1,2,3,5,8,9,6,4};
		//assertTrue(SlotMachines.exists1(arrr, 5));
		
	}

	
}
