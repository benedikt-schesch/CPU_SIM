/**
 * This simulates the behavior of a Dram_Cell logically
 * @author scheschb
 *
 */
public class Dram_Cell {
	private int bit_value;
	private Wire_Digital wordline;
	private Wire_Analogue bitline;
	
	
	public Dram_Cell(int bit_value, Wire_Digital wordline, Wire_Analogue bitline) {
		this.bit_value = bit_value;
		this.wordline = wordline;
		this.bitline = bitline;
	}

	/**
	 * Refresh is called to refresh the state of the cell.
	 * In case wordline is 1 we check if it is a write or a read by checking the value of the bitline.
	 * Either it bitline is 0.5 which is a read (The voltage is set to half) or it is a write in which case we change the stored value.
	 * @throws IllegalStateException 
	 */
	public void Refresh() throws IllegalStateException {
		if (wordline.state) {
			double state = bitline.state;
			if (state == 0.5) {
				bitline.state = bit_value;
				return;
			} else if(state == 0 || state == 1){
				bit_value = (int)state;
				return;
			}
			throw new IllegalStateException("One of the bitlines in a a Dram Cell was set to "+bitline.state);
		}
	}
	/**
	 * 
	 * @return Returns the current value stored in the cell
	 */
	public int Read() {
		return bit_value;
	}
	
	/*
	 * Side Note
	 * Actually you can do some very cool operations exploiting the analog properties of DRAM
	 * Here is a paper that explains some very cool operations you can do:
	 * https://people.inf.ethz.ch/omutlu/pub/ambit-bulk-bitwise-dram_micro17.pdf
	 */
}
