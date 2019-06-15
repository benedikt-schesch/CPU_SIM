
public class Memory {
	final int WordSize = 32;
	final int Capacity = 65536; //In words (We will assume that the address space does not go further than 16 bits)
	private Dram_Cell[][] Dram_Matrix = new Dram_Cell[Capacity][WordSize];
	
	
	private Wire_Digital[] Wordlines = new Wire_Digital[Capacity];
	private Wire_Analogue[] Bitlines = new Wire_Analogue[WordSize];
	
	
	Address_Mux Address_Mux;
	
	Wire_Digital[] Address;
	Wire_Digital[] ReadPort;
	Wire_Digital[] WritePort;
	Wire_Digital WriteEnable;
	
	int Previous_Address;
	/**
	 * This takes some time to execute
	 * @param Address
	 * @param ReadPort
	 * @param WriteEnable	
	 * @param Init	Initial values in Memory
	 */
	Memory(Wire_Digital[] Address,Wire_Digital[] ReadPort,Wire_Digital WriteEnable,Wire_Digital[] WritePort,int[][] Init){
		this(Init);
		this.Address = Address;
		this.ReadPort = ReadPort;
		this.WriteEnable = WriteEnable;
		this.WritePort = WritePort;
		Previous_Address = 0;
		Address_Mux = new Address_Mux(Address,Wordlines);
	}	
	
	
	/**
	 * Initializes the cells and wires
	 * @param Init contains the initial values of the memory
	 */
	private Memory(int[][] Init){
		for(int i = 0;i<Capacity;i++){
			Wordlines[i] = new Wire_Digital();
		}
		for(int i = 0;i<WordSize;i++){
			Bitlines[i] = new Wire_Analogue();
		}
		for(int i = 0;i<Capacity;i++){
			for(int j = 0;j<WordSize;j++) {
				Dram_Matrix[i][j] = new Dram_Cell(Init[i][j],Wordlines[i],Bitlines[j]);
			}
		}
	}
	/**
	 * Refreshes the logical state of the memory
	 * @throws IllegalAddressException
	 * @throws IllegalStateException
	 */
	public void Refresh() throws IllegalAddressException, IllegalStateException{
		int New_Address = Address_Mux.Refresh();
		for(int i = 0;i<WordSize;i++){
			Bitlines[i].state = WriteEnable.state ? (WritePort[i].state ? 1: 0) : 0.5;
		}
		for(int i = 0;i<WordSize;i++){
			Dram_Matrix[New_Address][i].Refresh();;
			ReadPort[i].state = (Bitlines[i].state == 1);
		}
	}
	
	/*
	 * Side note
	 * This Memory is very stupid it just stores values
	 * This has been the dominating model in Computer Architecture but we now
	 * realize that this a problem. The Memory bandwidth is a huge bottleneck.
	 * That's why Processing-In-Memory exists. Here is a great paper that explains it:
	 * https://people.inf.ethz.ch/omutlu/pub/ProcessingDataWhereItMakesSense_micpro19-invited.pdf
	 * 
	 * Also there is a major flaw in DRAM design which is a security threat to DRAM devices.
	 * Basically if we turn quickly a row in DRAM on and off it will corrupt the adjacent rows.
	 * It's called RowHammer here you can read about it:
	 * https://users.ece.cmu.edu/~yoonguk/papers/kim-isca14.pdf
	 * Here you can read about using RowHammer to access kernel privileges:
	 * https://googleprojectzero.blogspot.com/2015/03/exploiting-dram-rowhammer-bug-to-gain.html
	 */
}
