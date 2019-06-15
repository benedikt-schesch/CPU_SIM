import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class Memory_test {

	final int WordSize = 32;
	final int Capacity = 65536;
	
	/**
	 * This test only Initializes memory with some random values
	 * then it checks if the values have been correctly initialized
	 */
	@Test
	public void TestInitandRead() {
		// Generate Random Inputs
		int[][] Init = new int[Capacity][WordSize];
		GenerateRandomInputs(Init);

		// Initialize Test Bench
		Wire_Digital[] Address = new Wire_Digital[32];
		Wire_Digital[] ReadPort = new Wire_Digital[32];
		Wire_Digital[] WritePort = new Wire_Digital[32];
		Wire_Digital WriteEnable = new Wire_Digital();
		for (int i = 0; i < 32; i++) {
			Address[i] = new Wire_Digital();
			ReadPort[i] = new Wire_Digital();
			WritePort[i] = new Wire_Digital();
		}

		// Initialize Memory
		Memory memory = new Memory(Address, ReadPort, WriteEnable, WritePort,
				Init);

		// Input addresses in binary signals
		int[] Computed_Address = new int[32];

		for (int i = 0; i < Math.pow(2, 16); i++) {

			// Compute and update the address in binary signals
			GenerateAddress(i, Computed_Address);
			for (int j = 0; j < 32; j++) {
				Address[j].state = (1 == Computed_Address[j]);
			}

			// Refresh the memory
			try {
				memory.Refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Check if inputs are still good
			for (int j = 0; j < 32; j++) {
				assertEquals(Init[i][j], ReadPort[j].state ? 1 : 0);
			}

		}

	}

	/**
	 * This test writes some values to memory
	 * Writes to some address
	 * Read from another address
	 * Check if the written address values are still equal
	 */
	@Test
	public void TestWrite() {
		// Generate Random Inputs
		int[][] Init = new int[Capacity][WordSize];
		GenerateRandomInputs(Init);

		// Initialize Test Bench
		Wire_Digital[] Address = new Wire_Digital[32];
		Wire_Digital[] ReadPort = new Wire_Digital[32];
		Wire_Digital[] WritePort = new Wire_Digital[32];
		Wire_Digital WriteEnable = new Wire_Digital();
		for (int i = 0; i < 32; i++) {
			Address[i] = new Wire_Digital();
			ReadPort[i] = new Wire_Digital();
			WritePort[i] = new Wire_Digital();
		}

		// Initialize Memory
		Memory memory = new Memory(Address, ReadPort, WriteEnable, WritePort,Init);

		// Input addresses in binary signals
		int[] Computed_Address = new int[32];

		for (int i = 0; i < Math.pow(2, 16); i++) {
			// Compute and update the address in binary signals
			GenerateAddress(i, Computed_Address);
			for (int j = 0; j < 32; j++) {
				Address[j].state = (1 == Computed_Address[j]);
			}
			
			//Compute and update random values to be written
			int[][] Write_value = new int[1][32];
			GenerateRandomInputs(Write_value);
			for(int j = 0;j<32;j++){
				WritePort[j].state = Write_value[0][j] == 1;
			}
			WriteEnable.state = true;
			
			//Refresh Memory
			try {
				memory.Refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
			WriteEnable.state = false;
			
			
			//Do some Random Computations
			RandomComputations(memory);
			
			//Read the values that have been written
			for (int j = 0; j < 32; j++) {
				Address[j].state = (1 == Computed_Address[j]);
			}
			try {
				memory.Refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			for(int j = 0;j<32;j++){
				assertEquals(memory.ReadPort[j].state,1==Write_value[0][j]);
			}
			
			
		}
	}

	
	/*
	 * Reads from a random Address
	 */
	private void RandomComputations(Memory memory) {
		Random random = new Random();
		for(int i = 0;i<16;i++){
			memory.Address[i].state = random.nextBoolean();
		}
		try {
			memory.Refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param Address To be converted
	 * @param Computed_Address	Converted value in binary
	 */
	public void GenerateAddress(int Address, int[] Computed_Address) {

		for (int i = 31; i >= 0; i--) {
			if (Address - Math.pow(2, i) >= 0) {
				Address = (int) (Address - Math.pow(2, i));
				Computed_Address[i] = 1;
			} else {
				Computed_Address[i] = 0;
			}
		}
	}
	
	/**
	 * Generate Random binary inputs in the matrix Init
	 * @param Init
	 */
	public void GenerateRandomInputs(int[][] Init) {
		for (int i = 0; i < Init.length; i++) {
			for (int j = 0; j < Init[i].length; j++) {
				Random Random = new Random();
				Init[i][j] = Random.nextBoolean() ? 1 : 0;
			}
		}
	}
}
