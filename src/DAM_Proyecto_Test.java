import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class DAM_Proyecto_Test 
{

	@Test
	void testFound() 
	{
		char[] arr = {' ','E','@','#','-'};
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, ' '));
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, '-'));
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, '#'));
		assertTrue(DAM_Proyecto_Tragaperras.found(arr, 'E'));
		assertFalse(DAM_Proyecto_Tragaperras.found(arr, 'e'));
		assertFalse(DAM_Proyecto_Tragaperras.found(arr, 'k'));
	}

}
