/**
 * This decodes the address and turns on the right wordline
 * @author scheschb
 *
 */
public class Address_Mux {
	
	final int Capacity = 65536; //In words (We will assume that the address space does not go further than 16 bits)
	Wire_Digital[] Address;
	Wire_Digital[] Wordlines;
	private int Previous_Address;
	/**
	 * 
	 * @param address
	 * @param Wordlines
	 */
	public Address_Mux(Wire_Digital[] address, Wire_Digital[] Wordlines) {
		assert Address.length == 32;
		assert Wordlines.length == Capacity;
		this.Address = address;
		this.Wordlines = Wordlines;
		Previous_Address = 0;
	}
	
	/**
	 * Refreshes the state of the Address_Mux
	 * @throws IllegalAddressException 
	 */
	public int Refresh() throws IllegalAddressException{
		int New_Address = 0;
		for(int i = 0;i<Address.length;i++){
			New_Address += Address[i].state ?(int)Math.pow(2,i):0;
		}
		if(New_Address>=Math.pow(2, 16)){
			throw new IllegalAddressException("The adress is " + New_Address + " which isn't allowed");
		}
		Wordlines[Previous_Address].state = false;
		Wordlines[New_Address].state = true;
		Previous_Address = New_Address;
		
		return New_Address;
	}
	
}
