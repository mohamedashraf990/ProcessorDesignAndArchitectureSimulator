import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class project {
	
	static short InstructionMemory[]=new short [1024]; //16 
	static byte  DataMemory[]=new byte [2048];
	static int ProgramCounter=0;
	static byte GeneralPurposeRegisters[]=new byte[64];
	static int SREG[] =new int[8];
	static int opcode =0;  // bits31:28
	static int r1 =0;      // bits27:24
    static int r2 =0;      // bit23:20
    static int imm =0;     // bits19:0
    static int clkCycles=0;
    static int i=1;
    static int valueRS =0;
    static int valueRT =0;
    static int instructionsFetched=0;
    static int instructionsCount=0;
    static int savedIntCount;
    boolean fetcher=true;
    static boolean branching = false;
    static boolean jumping = false;
    static int decimalValue;
    static ArrayList<String> operation = new ArrayList<String>();
    static ArrayList<String> regg1 = new ArrayList<String>();
    static ArrayList<String> regg2 = new ArrayList<String>();
    
   
	public static void main(String[] args) {
	//	System.out.println(Integer.parseInt(Integer.toBinaryString(64).substring(24)));
		for(int i=0; i <InstructionMemory.length ; i++) {
			  InstructionMemory[i]=-1;
		}
		readFile("Program 1.txt");
		int instruction=0;
		int executer=0;
		int pcbefore=0;
		
		
		
		
		
	//	System.out.println(Integer.parseInt(Integer.toBinaryString((int)GeneralPurposeRegisters[2]).substring(24)));

		GeneralPurposeRegisters[3]=2;
		for(int i=0;i<instructionsCount + 2 ;i++) {
			 clkCycles++;
			 if(ProgramCounter<operation.size()) {
					System.out.println(operation.get(ProgramCounter) + " operation is currently being fetched on register " +regg1.get(ProgramCounter) + " and R2/IMM " + regg2.get(ProgramCounter)  );
				}

			if(clkCycles==1) {
				 System.out.println("Program Counter "+ProgramCounter);
				 System.out.println("Clock Cycles  "+clkCycles);
				 instruction=fetch();
				 System.out.println("Fetch Stage "+instruction);
				 System.out.println("Decode Stage "+-1);
				 System.out.println("Execute Stage "+-1);
				 

				 
			}else if(clkCycles==2) {
				 System.out.println("Program Counter "+ProgramCounter);
				 System.out.println("Clock Cycles  "+clkCycles);
				 decode(instruction);
				 System.out.println("Decode Stage "+instruction);			 
//				  instructionsCount=instructionsCount-imm;               ????????
					  executer=instruction; // function about to execute
					  instruction=fetch();
					  System.out.println("Fetch Stage " +instruction);
					  System.out.println("Execute Stage "+-1);
					 
				  
			}
			else {
				 System.out.println("Program Counter "+ProgramCounter);
				 System.out.println("Clk Cycles " +clkCycles);
				  
				  switch (opcode) {
					// these are the opcodes of R instructions	  
					case 0: //0		
					case 1: //1
					case 2: //2
					case 5: //5
					case 6: //6
					case 7: //7	
						pcbefore=ProgramCounter;
					System.out.println("opcode = " + opcode + " r1 = " + r1 + " r2 = " + r2 );
						 executeRformate(opcode,r1, r2);
						 System.out.println("Execute stage "+executer);
				
						 break;
					case 3:
					case 4:	
					case 8:
					case 9:
					case 10:
					case 11:		
						System.out.println("opcode = " + opcode + " r1 = " + r1 + " imm = " + imm );
						executeIformate(opcode,r1, r2, imm);
						System.out.println("Execute stage "+executer);
						break;
				  }
				  
				  
				  if(opcode ==7) {
					  jumping=true;
					  if(pcbefore<decimalValue)
					  
					  instructionsCount=instructionsCount-decimalValue;
					  else {
						  
						  instructionsCount=instructionsCount+(decimalValue)+pcbefore; 
						  
					  }
						 
				  }
				  
				  if(opcode ==4) {
					  branching=true;
					  instructionsCount=instructionsCount-imm;
					
					  
				  }
				  
					if(opcode==-1){
						System.out.println("Execute stage "+-1);
						
					}
					  
					  decode(instruction);
					  executer=instruction;
					  System.out.println("Decode Stage "+instruction);
					  instruction=fetch();
						  System.out.println("Fetch Stage " + instruction);
						  
				
				  
					if(branching) { 
						instruction=-1;
						opcode=-1;
						r1=0;
						r2=0;
						imm=0;
						valueRS=0;
						valueRT=0;
						branching=false;
					}
					if(jumping) { 
						 
						instruction=-1;
						opcode=-1;
						r1=0;
						r2=0;
						imm=0;
						valueRS=0;
						valueRT=0;
						ProgramCounter--;
						jumping=false;
					}
					
				  
			}
		
			System.out.print("SREG VALUES ");
			for(int j=0 ; j <SREG.length ; j++) {
				System.out.print(SREG[j]);
			}
			System.out.println();
			System.out.println("Sign Flag "+SREG[1]);
			System.out.println("Negative flag "+SREG[2]);
			System.out.println("OverFlag is "+SREG[3]);
			System.out.println("Zero Flag "+SREG[0]);
			System.out.println("Carry Flag "+SREG[4]);
			 System.out.println("*************************");
		}
	   
		
//		System.out.println("Number of clkCycles "+clkCycles);
	}
	
    public static int fetch() {
    	if(InstructionMemory[ProgramCounter]==-1) {
    		return -1;
    	}
    	
    		if(instructionsFetched<instructionsCount) {
        		int instruction=InstructionMemory[ProgramCounter];
        		if(ProgramCounter<savedIntCount) {
        			ProgramCounter++;
        		}
            	instructionsFetched++;
            	return instruction;
        	}
    		return -1;
    	
      
    }
    
    //flushing after branch and after jump
    //    000101
    //       111101

    public static void decode(int instruction) {
    
        
        if(instruction==-1) {
        	return;
        }
        
        opcode=instruction & 0b00000000000000001111000000000000;
        opcode=opcode>>>12;
        
        r1=instruction&0b00000000000000000000111111000000;
        r1=r1>>>6;
        
        r2=instruction &0b00000000000000000000000000111111;
        r2=r2>>>0;

        
        imm=instruction & 0b00000000000000000000000000111111;
        imm=imm>>>0;
        valueRS=GeneralPurposeRegisters[r1];
        valueRT=GeneralPurposeRegisters[r2];

        
    }
    
	public static void executeIformate(int opCode,int reg1,int reg2,int immediate) {
		ArrayList<String> twosC = new ArrayList<String>();
		String binary;
		
		
		switch (opCode) {
		
		case 3: //load i
			
			binary = Integer.toBinaryString(immediate);
			binary=zero6Adjuster(binary);
			if(binary.charAt(0) == '1') {
				
				for(int i=0 ; i<binary.length() ; i++) {
					twosC.add(binary.charAt(i) +"");
				}
				
				for(int i=0 ; i<twosC.size() ;i++) {
					if(twosC.get(i).equals("1"))
						twosC.set(i, "0");
					else
						twosC.set(i, "1");
				}
				binary ="";
				
				for(int i=0 ; i<twosC.size(); i++) {
					binary = binary + twosC.get(i);
				}
				
				
				
				binary=binary.substring(binary.length()-5);
				immediate=-(Integer.parseInt(binary,2) +1);
			}
			GeneralPurposeRegisters[reg1]=(byte) immediate;
			System.out.println("register " + reg1 + " value has been loaded with " + immediate);
			break;
			
		case 4: //beqz
			binary = Integer.toBinaryString(immediate);
			binary=zero6Adjuster(binary);
			
			if(binary.charAt(0) == '1') {
				
				for(int i=0 ; i<binary.length() ; i++) {
					twosC.add(binary.charAt(i) +"");
				}
				
				for(int i=0 ; i<twosC.size() ;i++) {
					if(twosC.get(i).equals("1"))
						twosC.set(i, "0");
					else
						twosC.set(i, "1");
				}
				binary ="";
				
				for(int i=0 ; i<twosC.size(); i++) {
					binary = binary + twosC.get(i);
				}
				
				
				
				binary=binary.substring(binary.length()-5);
				immediate=-(Integer.parseInt(binary,2) +1);
				
			}
			if(GeneralPurposeRegisters[reg1]==0) {
				
				ProgramCounter=(ProgramCounter+immediate);
				
				
			}
			
			System.out.println("the pc has been updated to " + ProgramCounter);
			break;
			
		case 8: //slc
			 binary = Integer.toBinaryString(immediate);
			binary=zero6Adjuster(binary);
			
			if(binary.charAt(0) == '1') {
				
				for(int i=0 ; i<binary.length() ; i++) {
					twosC.add(binary.charAt(i) +"");
				}
				
				for(int i=0 ; i<twosC.size() ;i++) {
					if(twosC.get(i).equals("1"))
						twosC.set(i, "0");
					else
						twosC.set(i, "1");
				}
				binary ="";
				
				for(int i=0 ; i<twosC.size(); i++) {
					binary = binary + twosC.get(i);
				}
				
				
				
				binary=binary.substring(binary.length()-5);
				immediate=-(Integer.parseInt(binary,2) +1);
			}
			byte xSlc= (byte) ( (GeneralPurposeRegisters[reg1]<<immediate) | GeneralPurposeRegisters[reg1] >>> 8-immediate);
			GeneralPurposeRegisters[reg1]= (byte) ( (GeneralPurposeRegisters[reg1]<<immediate) | GeneralPurposeRegisters[reg1] >>> 8-immediate);
			updateNFlag(xSlc);
			SREG[0]= xSlc==0?1:0;
			System.out.println(" the value of " + reg1 + "has been updated to " + GeneralPurposeRegisters[reg1]);
			break;
			
		case 9: //src
			binary = Integer.toBinaryString(immediate);
			binary=zero6Adjuster(binary);
			
			if(binary.charAt(0) == '1') {
				
				for(int i=0 ; i<binary.length() ; i++) {
					twosC.add(binary.charAt(i) +"");
				}
				
				for(int i=0 ; i<twosC.size() ;i++) {
					if(twosC.get(i).equals("1"))
						twosC.set(i, "0");
					else
						twosC.set(i, "1");
				}
				binary ="";
				
				for(int i=0 ; i<twosC.size(); i++) {
					binary = binary + twosC.get(i);
				}
				
				
				
				binary=binary.substring(binary.length()-5);
				immediate=-(Integer.parseInt(binary,2) +1);
			}
			byte xSrc=  (byte) ( (GeneralPurposeRegisters[reg1]>>>immediate) | GeneralPurposeRegisters[reg1] << 8-immediate);
			GeneralPurposeRegisters[reg1]=  (byte) ( (GeneralPurposeRegisters[reg1]>>>immediate) | GeneralPurposeRegisters[reg1] << 8-immediate);
			updateNFlag(xSrc);
			SREG[0]= xSrc==0?1:0;
			System.out.println(xSrc+"jj");
			System.out.println(" the value of " + reg1 + "has been updated to " + GeneralPurposeRegisters[reg1]);

			break;
			
		case 10: //lb
			GeneralPurposeRegisters[reg1]=(byte) DataMemory[immediate];
			System.out.println("the register " + reg1 + " value has been updated with " + GeneralPurposeRegisters[reg1]);
			
			break;
		case 11: //sb
			DataMemory[immediate] = GeneralPurposeRegisters[reg1];
			System.out.println("the memory location of" + immediate + " has been updated with of " + DataMemory[immediate]);
			break;	
		}
		
		imm = immediate;
	}
	
	
	public static void executeRformate(int opCode,int reg1,int reg2) {
		boolean neg1=false;
		boolean neg2=false;
		int tempreg1=0;
		int tempreg2=0;
		
		switch(opCode) {
		
		case 0:  // add
			byte xAdd=(byte) (GeneralPurposeRegisters[reg1] + GeneralPurposeRegisters[reg2]);
			int yAdd= GeneralPurposeRegisters[reg1] + GeneralPurposeRegisters[reg2];
			String binary1= GeneralPurposeRegisters[reg1]<0? Integer.toBinaryString((int)GeneralPurposeRegisters[reg1]).substring(24):Integer.toBinaryString((int)GeneralPurposeRegisters[reg1]);
			String binary2= GeneralPurposeRegisters[reg2]<0? Integer.toBinaryString((int)GeneralPurposeRegisters[reg2]).substring(24):Integer.toBinaryString((int)GeneralPurposeRegisters[reg2]);
			int forC = Integer.parseInt(binary1,2)+Integer.parseInt(binary2,2);
			GeneralPurposeRegisters[reg1]=xAdd;
			updateCFlag(reg1, xAdd, forC);
			int addVFlag= updateVFlag(reg1, xAdd, yAdd);
			int addNFlag= updateNFlag(yAdd);
			SREG[1]= addNFlag ^ addVFlag; //update S Flag
			SREG[0]= yAdd==0?1:0;
			System.out.println("register " + reg1 + " value has been updated with " + GeneralPurposeRegisters[reg1]);
			break;	
			
		case 1: // minus
			byte xSub=(byte) (GeneralPurposeRegisters[reg1] - GeneralPurposeRegisters[reg2]);
			int ySub= GeneralPurposeRegisters[reg1] - GeneralPurposeRegisters[reg2];
			String binary3= GeneralPurposeRegisters[reg1]<0? Integer.toBinaryString((int)GeneralPurposeRegisters[reg1]).substring(24):Integer.toBinaryString((int)GeneralPurposeRegisters[reg1]);
			String binary4= GeneralPurposeRegisters[reg2]<0? Integer.toBinaryString((int)GeneralPurposeRegisters[reg2]).substring(24):Integer.toBinaryString((int)GeneralPurposeRegisters[reg2]);
			int forC1 = Integer.parseInt(binary3,2)-Integer.parseInt(binary4,2);
			GeneralPurposeRegisters[reg1]=xSub;
			updateCFlag(reg1, xSub, forC1);
			int subVFlag= updateVFlag(reg1, xSub, ySub);
			int subNFlag= updateNFlag(ySub);
			SREG[1]= subNFlag ^ subVFlag; //update S Flag
			SREG[0]= ySub==0?1:0;
			System.out.println("register " + reg1 + " value has been updated with " + GeneralPurposeRegisters[reg1]);
			break;
			
		case 2:  // mul
			//System.out.println(GeneralPurposeRegisters[reg1]);
			//System.out.println(GeneralPurposeRegisters[reg2]);
			byte xMult=(byte) (GeneralPurposeRegisters[reg1] * GeneralPurposeRegisters[reg2]);
			int yMult= GeneralPurposeRegisters[reg1] * GeneralPurposeRegisters[reg2];
			String binary5= GeneralPurposeRegisters[reg1]<0? Integer.toBinaryString((int)GeneralPurposeRegisters[reg1]).substring(24):Integer.toBinaryString((int)GeneralPurposeRegisters[reg1]);
			String binary6= GeneralPurposeRegisters[reg2]<0? Integer.toBinaryString((int)GeneralPurposeRegisters[reg2]).substring(24):Integer.toBinaryString((int)GeneralPurposeRegisters[reg2]);
			int forC2 = Integer.parseInt(binary5,2)+Integer.parseInt(binary6,2);
			GeneralPurposeRegisters[reg1]=xMult;
			updateCFlag(reg1, xMult, forC2);
			updateNFlag(yMult);
			SREG[0]= yMult==0?1:0;
			System.out.println("register " + reg1 + " value has been updated with " + GeneralPurposeRegisters[reg1]);
			break;
			
		case 5:  // and
			byte xAnd= (byte) (GeneralPurposeRegisters[reg1] & GeneralPurposeRegisters[reg2]);
			int yAnd= GeneralPurposeRegisters[reg1] & GeneralPurposeRegisters[reg2];
			GeneralPurposeRegisters[reg1]=  (byte) (GeneralPurposeRegisters[reg1] & GeneralPurposeRegisters[reg2]);
			updateNFlag(yAnd);
			SREG[0]= yAnd==0?1:0;
			System.out.println("register " + reg1 + " value has been updated with " + GeneralPurposeRegisters[reg1]);
			break;
		
		case 6: //or
			byte xOr= (byte) (GeneralPurposeRegisters[reg1] | GeneralPurposeRegisters[reg2]);
			int yOr= GeneralPurposeRegisters[reg1] | GeneralPurposeRegisters[reg2];
			GeneralPurposeRegisters[reg1]= (byte) (GeneralPurposeRegisters[reg1] | GeneralPurposeRegisters[reg2]);
			updateNFlag(yOr);
			SREG[0]= yOr==0?1:0;
			System.out.println("register " + reg1 + " value has been updated with " + GeneralPurposeRegisters[reg1]);
			break;
			
		case 7: //jump
		   
			
			if(GeneralPurposeRegisters[reg1]<0)
				GeneralPurposeRegisters[reg1]=(byte) -(GeneralPurposeRegisters[reg1]);
			
			if(GeneralPurposeRegisters[reg2]<0)
				GeneralPurposeRegisters[reg2]=(byte) -(GeneralPurposeRegisters[reg2]);
			
			String GeneralPurpose1=Integer.toString(GeneralPurposeRegisters[reg1],2);
			String GeneralPurpose2=Integer.toString(GeneralPurposeRegisters[reg2],2);
			GeneralPurpose1=zero6Adjuster(GeneralPurpose1);
			GeneralPurpose2=zero6Adjuster(GeneralPurpose2);

			String GeneralString=GeneralPurpose1.concat(GeneralPurpose2);
	
			decimalValue = Integer.parseInt(GeneralString, 2);	
			
		    ProgramCounter= decimalValue;
		    System.out.println("the pc has been updated to " + ProgramCounter);
			break;
		}
	
	}


	public static int updateNFlag(int y) { // X or Y ??????????
		// TODO Auto-generated method stub
		int yMask= y & 0b00000000000000000000000010000000;
		if(yMask==0)
			SREG[2]=0;
		else
			SREG[2]=1;
		return SREG[2];
	
	}

	public static int updateVFlag(int reg1, byte x, int y) {
		// TODO Auto-generated method stub
		if(x>=0 && y>=0 || x<0 && y<0)
			SREG[3]= 0;
		else
			SREG[3]=1;
		return SREG[3];
	}

	public static void updateCFlag(int reg1, byte x, int y) {
		// TODO Auto-generasysd method stub
		//System.out.println(y+"yy");
		int yMask= y & 0b00000000000000000000000100000000;
		if(yMask==0)
			SREG[4]=0;
		else
			SREG[4]=1;
		
		/*if(x==y) //case 1 and 3
			SREG[4]=0;
		
		else if(x<0 && y<0 && x>y) //case 2
			SREG[4]=1;
		
		else if(x>0 && y>0 && x<y) { //case 4
			if(y>Byte.MAX_VALUE)
				SREG[4]=1;
			else
				SREG[4]=0;
		}
		else if(x<0 && y>0) //case 5
			SREG[4]=0;
		
		else if(x>0 && y<0) //case 6
			SREG[4]=1;
		
	*/
	}

	public static void readFile(String fileName) {
		String fileString="";
		  try {
		      File myObj = new File(fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) { 
		        String data = myReader.nextLine();
		        fillMemory(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		  }
	
	
	public static void fillMemory(String data) {

		String resultString = "";
		int reg1num;
		int reg2num;
		String immorAddress;
		byte immOrAddressInt;
		String splittedArray[]=data.split(" ");
		String reg1Binary="";
		String reg2Binary="";
		int instructionValue=0;
		for(int i=0;i<splittedArray.length;i++) {
			splittedArray[i]=splittedArray[i].trim();	
		}
		switch (splittedArray[0]) {
		case "ADD":
			
			     resultString="0000";
			     operation.add("ADD");
			     reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			 	 reg2num=Integer.parseInt(splittedArray[2].substring(1)+"");
			 	 regg1.add(reg1num + "");
			 	 regg2.add(reg1num + "");
				 reg1Binary=Integer.toBinaryString(reg1num);
				 reg2Binary=Integer.toBinaryString(reg2num);
				 reg1Binary=zero6Adjuster(reg1Binary);
				 reg2Binary=zero6Adjuster(reg2Binary);
				 resultString=resultString.concat(reg1Binary).concat(reg2Binary);
				 instructionValue=Integer.parseInt(resultString,2);			 
			break;
		case "SUB":
			 resultString="0001";
			 operation.add("SUB");
		     reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
		 	 reg2num=Integer.parseInt(splittedArray[2].substring(1)+"");
		 	 regg1.add(reg1num + "");
		 	 regg2.add(reg2num + "");
			 reg1Binary=Integer.toBinaryString(reg1num);
			 reg2Binary=Integer.toBinaryString(reg2num);
			 reg1Binary=zero6Adjuster(reg1Binary);
			 reg2Binary=zero6Adjuster(reg2Binary);
			 resultString=resultString.concat(reg1Binary).concat(reg2Binary);
			 instructionValue=Integer.parseInt(resultString,2);
			break;
		case "MUL": 
			 resultString="0010";
			 operation.add("MUL");
		     reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
		 	 reg2num=Integer.parseInt(splittedArray[2].substring(1)+"");
		 	 regg1.add(reg1num + "");
		 	 regg2.add(reg2num + "");
			 reg1Binary=Integer.toBinaryString(reg1num);
			 reg2Binary=Integer.toBinaryString(reg2num);
			 reg1Binary=zero6Adjuster(reg1Binary);
			 reg2Binary=zero6Adjuster(reg2Binary);
			 resultString=resultString.concat(reg1Binary).concat(reg2Binary);
			 instructionValue=Integer.parseInt(resultString,2);
			break;
		case "LDI": 
			operation.add("LDI");
			resultString="0011";
			reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			 regg1.add(reg1num + "");
		 	 
			reg1Binary=Integer.toBinaryString(reg1num);
			reg1Binary=zero6Adjuster(reg1Binary);
			immorAddress=splittedArray[2];
			regg2.add(immorAddress + "");
			immOrAddressInt=Byte.parseByte(immorAddress+"");
			immorAddress=Integer.toBinaryString(immOrAddressInt);
			immorAddress=zero6Adjuster(immorAddress);
			immorAddress=immorAddress.substring(immorAddress.length()-6);
			resultString=resultString.concat(reg1Binary).concat(immorAddress);
			instructionValue=Integer.parseInt(resultString,2);
			break;
		case "BEQZ": 
			resultString="0100";
			 operation.add("BEQZ");
			reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			regg1.add(reg1num + "");
		 	 
			reg1Binary=Integer.toBinaryString(reg1num);
			
			reg1Binary=zero6Adjuster(reg1Binary);
			
			immorAddress=splittedArray[2];
			regg2.add(immorAddress + "");
			immOrAddressInt=Byte.parseByte(immorAddress+"");
			immorAddress=Integer.toBinaryString(immOrAddressInt);
			immorAddress=zero6Adjuster(immorAddress);
			immorAddress=immorAddress.substring(immorAddress.length()-6);
			resultString=resultString.concat(reg1Binary).concat(immorAddress);
			instructionValue=Integer.parseInt(resultString,2);
			break;
		case "AND": 
			resultString="0101";
			operation.add("AND");
		     reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
		 	 reg2num=Integer.parseInt(splittedArray[2].substring(1)+"");
		 	regg1.add(reg1num + "");
		 	 regg2.add(reg2num + "");
			 reg1Binary=Integer.toBinaryString(reg1num);
			 reg2Binary=Integer.toBinaryString(reg2num);
			 reg1Binary=zero6Adjuster(reg1Binary);
			 reg2Binary=zero6Adjuster(reg2Binary);
			 resultString=resultString.concat(reg1Binary).concat(reg2Binary);
			 instructionValue=Integer.parseInt(resultString,2);
			break;
		case "OR": 
			operation.add("OR");
			 resultString="0110";
		     reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
		 	 reg2num=Integer.parseInt(splittedArray[2].substring(1)+"");
		 	 regg1.add(reg1num + "");
		 	 regg2.add(reg2num + "");
			 reg1Binary=Integer.toBinaryString(reg1num);
			 reg2Binary=Integer.toBinaryString(reg2num);
			 reg1Binary=zero6Adjuster(reg1Binary);
			 reg2Binary=zero6Adjuster(reg2Binary);
			 resultString=resultString.concat(reg1Binary).concat(reg2Binary);
			 instructionValue=Integer.parseInt(resultString,2);
			break;
		case "JR": 
			operation.add("JR");
			resultString="0111";
		     reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
		 	 reg2num=Integer.parseInt(splittedArray[2].substring(1)+"");
		 	regg1.add(reg1num + "");
		 	 regg2.add(reg2num + "");
			 reg1Binary=Integer.toBinaryString(reg1num);
			 reg2Binary=Integer.toBinaryString(reg2num);
			 reg1Binary=zero6Adjuster(reg1Binary);
			 reg2Binary=zero6Adjuster(reg2Binary);
			 resultString=resultString.concat(reg1Binary).concat(reg2Binary);
			 instructionValue=Integer.parseInt(resultString,2);
			break;
		case "SLC": 
			operation.add("SLC");
			resultString="1000";
			reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			regg1.add(reg1num + "");
		 	 
			reg1Binary=Integer.toBinaryString(reg1num);
			reg1Binary=zero6Adjuster(reg1Binary);
			immorAddress=splittedArray[2];
			regg2.add(immorAddress + "");
			immOrAddressInt=(byte) Integer.parseInt(immorAddress+"");
			immorAddress=Integer.toBinaryString(immOrAddressInt);
			immorAddress=zero6Adjuster(immorAddress);
			resultString=resultString.concat(reg1Binary).concat(immorAddress);
			instructionValue=Integer.parseInt(resultString,2);
			break;
		case "SRC": 
			resultString="1001";
			operation.add("SRC");
			reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			regg1.add(reg1num + "");
		 	
			reg1Binary=Integer.toBinaryString(reg1num);
			reg1Binary=zero6Adjuster(reg1Binary);
			immorAddress=splittedArray[2];
			regg2.add(immorAddress + "");
			immOrAddressInt=(byte) Integer.parseInt(immorAddress+"");
			immorAddress=Integer.toBinaryString(immOrAddressInt);
			immorAddress=zero6Adjuster(immorAddress);
			resultString=resultString.concat(reg1Binary).concat(immorAddress);
			instructionValue=Integer.parseInt(resultString,2);
			break;
		case "LB": 
			resultString="1010";
			operation.add("LB");
			reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			regg1.add(reg1num + "");
		 	
			reg1Binary=Integer.toBinaryString(reg1num);
			reg1Binary=zero6Adjuster(reg1Binary);
			immorAddress=splittedArray[2];
			regg2.add(immorAddress + "");
			immOrAddressInt=(byte) Integer.parseInt(immorAddress+"");
			immorAddress=Integer.toBinaryString(immOrAddressInt);
			immorAddress=zero6Adjuster(immorAddress);
			resultString=resultString.concat(reg1Binary).concat(immorAddress);
			instructionValue=Integer.parseInt(resultString,2);
			break;
		case "SB": 
			resultString="1011";
			operation.add("SB");
			reg1num=Integer.parseInt(splittedArray[1].substring(1)+"");
			regg1.add(reg1num + "");
		 	
			reg1Binary=Integer.toBinaryString(reg1num);
			reg1Binary=zero6Adjuster(reg1Binary);
			immorAddress=splittedArray[2];
			regg2.add(immorAddress + "");
			immOrAddressInt=(byte) Integer.parseInt(immorAddress+"");
			immorAddress=Integer.toBinaryString(immOrAddressInt);
			immorAddress=zero6Adjuster(immorAddress);
			resultString=resultString.concat(reg1Binary).concat(immorAddress);
			instructionValue=Integer.parseInt(resultString,2);
			break;
			
		}
		InstructionMemory[instructionsCount]=(short) instructionValue;
		instructionsCount++;
		savedIntCount++;
	
	
	
	}

	public static String zero6Adjuster(String binaryValue) {
		 if(binaryValue.length()<6) {
	        	int repeatZeros=6-binaryValue.length();
	        	binaryValue="0".repeat(repeatZeros)+binaryValue;
	        }
		   return binaryValue;
	   }

}
