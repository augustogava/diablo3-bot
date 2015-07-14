package br.com.d3.main;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public class ReadProcessMemory {
	// Public Variables for AccessRights
	public static final int PROCESS_QUERY_INFORMATION = 0x0400;
	public static final int PROCESS_VM_READ = 0x0010;
	public static final int PROCESS_VM_WRITE = 0x0020;
	public static final int PROCESS_VM_OPERATION = 0x0008;
	public static final int ALLOCATION_TYPE_MEM_COMMIT = 0x1000;
	public static final int ALLOCATION_TYPE_MEM_RESERVE = 0x2000;

	// Some functions, depending on Windows Version are located in Kernel32.dll,
	// some in Psapi.dll

	public interface DllMem extends StdCallLibrary {
		DllMem INSTANCE = (DllMem) Native.loadLibrary("DllMem", DllMem.class);
		
		int getValueMemory(Pointer hProcess, int inBaseAddress);
		
	}
	// Access to external Kernel32.dll
	public interface Kernel32 extends StdCallLibrary {
		Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32",
				Kernel32.class);

		boolean ReadProcessMemory(Pointer hProcess, int inBaseAddress,
				Pointer outputBuffer, int nSize,
				IntByReference outNumberOfBytesRead);

		public Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle,
				int dwProcessId);

		boolean WriteProcessMemory(Pointer hProcess, int lpBaseAddress,
				byte[] lpBuffer, int nSize, IntByReference lpNumberOfBytesRead);

		int GetLastError();

		public void VirtualAllocEx(Pointer ProcessToAllocateRamIn,
				int AddresToStartAt, int DesiredSizeToAllocate,
				int AllocationType, int ProtectType);

		// Needed for some Windows 7 Versions
		boolean EnumProcesses(int[] ProcessIDsOut, int size, int[] BytesReturned);

		int GetProcessImageFileNameW(Pointer Process, char[] outputname,
				int lenght);

	}

	// Access to external Psapi.dll
	public interface Psapi extends StdCallLibrary {
		Psapi INSTANCE = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

		// For some Windows 7 Versions and older down to XP
		boolean EnumProcesses(int[] ProcessIDsOut, int size, int[] BytesReturned);

		int GetProcessImageFileNameW(Pointer Process, char[] outputname,
				int lenght);
	}

	// Processfinder - returns NULL if Process wasnt found
	public static Pointer FindMyProcess(String ProcessNameToFind) {
		// Related to Version we have to use Kernel32.dll OR Psapi.dll to find
		// the Process
		Psapi Psapidll = Psapi.INSTANCE;
		Kernel32 Kernel32dll = Kernel32.INSTANCE;

		// we take an arraysize of 1024 - coz noone will have 1024 Processes
		// running :D
		int[] processlist = new int[1024];
		int[] dummylist = new int[1024];

		// Lets first try Psapi.dll
		try {
			Psapidll.EnumProcesses(processlist, 1024, dummylist);
		} catch (Exception e) {

		}

		// A pointer for our Processfinding mechanism
		Pointer tempProcess;
		// Pointer for our desired Process
		Pointer Process = null;
		// Char Array for the path of the processes containing also the
		// filename.exe
		char[] outputnames = new char[1024];
		// A String for easier Comparison - see below
		String path = "";

		for (int processid : processlist) {

			tempProcess = Kernel32dll.OpenProcess(
					PROCESS_QUERY_INFORMATION | PROCESS_VM_READ
							| PROCESS_VM_WRITE | PROCESS_VM_OPERATION, false,
					processid);

			// Again we have to try both dll files in order to obtain our goal -
			// one will work
			try {
				Psapidll.GetProcessImageFileNameW(tempProcess, outputnames,
						1024);
			} catch (Exception e) {
			}

			// try{Kernel32dll.GetProcessImageFileNameW(tempProcess,
			// outputnames, 1024);}
			// catch(Exception e){}

			// reset Path String
			path = "";

			for (int k = 0; k < 1024; k++) {
				// Convert our Char Array into a nice readable String
				if ((int) outputnames[k] != 0)
					path = path + outputnames[k];
			}

			if (path.contains(ProcessNameToFind)) {
				System.out.println( path + " e " + processid);
				// If one of the processes found has the desired process exe
				// name in its path its the one we want :)
				Process = tempProcess;
			}

			// reset char
			outputnames = new char[1024];

		}

		// Finally returning our Process - Null if we didnt find it.
		return Process;
	}

	// ReadProcessMemory ^^
	public static Object ReadMyProcessMemory(Pointer ProcessToReadFrom, int AdressToReadFrom, int NumberOfBytesToRead, int tipo) {
		// Make the Desired Functions available
		Kernel32 Kernel32dll = Kernel32.INSTANCE;

		int offset = AdressToReadFrom;
		IntByReference baseAddress = new IntByReference();
		baseAddress.setValue(offset);
		Memory outputBuffer = new Memory(NumberOfBytesToRead);

		boolean reader = Kernel32dll.ReadProcessMemory(ProcessToReadFrom, offset, outputBuffer, NumberOfBytesToRead, null);

		if (reader) {
			// Process the received Data
			byte[] bufferBytes = outputBuffer.getByteArray(0,
					NumberOfBytesToRead);

			return foundValueToString( bufferBytes, tipo ) ;

		} else {
			// Reading went wrong - SHIT
			return null;
		}
	}
	
	public static boolean WriteMyProcessMemory(Pointer ProcessToWriteTo, int AddressToWriteTo, String valorSalvar, int type ) {
		// Gain Access to Kernel32.dll
		Kernel32 Kernel32dll = Kernel32.INSTANCE;

		int offset = AddressToWriteTo;
		IntByReference baseAddress = new IntByReference();
		baseAddress.setValue(offset);
		IntByReference dummy = new IntByReference();

		byte[] valor = getValueBytes( valorSalvar, type );
		return Kernel32dll.WriteProcessMemory(ProcessToWriteTo, offset, valor, valor.length, dummy);
		
	}
	
	private static byte[] getValueBytes(String s, int type) {
		
		byte[] result = null;
		
		if (s == null) {
			return new byte[0];
		}
		int radix = 10;
		if (s.startsWith("$")) {
			s = s.substring(1);
			radix = 16;
		} else
		if (s.startsWith("0x") || s.startsWith("0X")) {
			s = s.substring(2);
			radix = 16;
		}
		switch ( type ) {
			// BYTE
			case 0:
				result = new byte[1];
				try {
					int val = Integer.parseInt(s, radix);
					result[0] = (byte)(val & 0xFF);
				} catch (NumberFormatException ex) {
					// ignored
				}
				break;
			// SHORT
			case 1:
				try {
					int val = Integer.parseInt(s, radix);
					result = new byte[2];
					result[0] = (byte)(val & 0xFF);
					result[1] = (byte)((val >> 8) & 0xFF);
				} catch (NumberFormatException ex) {
					// ignored
				}
				break;
			// INT
			case 2:
				try {
					long val = Long.parseLong(s, radix);
					result = new byte[4];
					result[0] = (byte)(val & 0xFFL);
					result[1] = (byte)((val >> 8) & 0xFFL);
					result[2] = (byte)((val >> 16) & 0xFFL);
					result[3] = (byte)((val >> 24) & 0xFFL);
				} catch (NumberFormatException ex) {
					
				}
				break;
			// LONG
			case 3:
				try {
					BigInteger val = new BigInteger(s, radix);
					result = new byte[8];
					byte[] vb = val.toByteArray();
					int start = vb.length < 8 ? 0 : vb.length - 8;
					int len = vb.length > 8 ? 8 : vb.length;
					System.arraycopy(vb, start, result, 0, len);
					reverse(result);
				} catch (NumberFormatException ex) {
					
				}
				break;
			// FLOAT
			case 4:
				try {
					int val = Float.floatToRawIntBits(Float.parseFloat(s));
					result = new byte[4];
					result[0] = (byte)(val & 0xFFL);
					result[1] = (byte)((val >> 8) & 0xFFL);
					result[2] = (byte)((val >> 16) & 0xFFL);
					result[3] = (byte)((val >> 24) & 0xFFL);
				} catch (NumberFormatException ex) {
					
				}
				break;
			// DOUBLE
			case 5:
				long val = Double.doubleToRawLongBits(Double.parseDouble(s));
				result = new byte[8];
				result[0] = (byte)(val & 0xFFL);
				result[1] = (byte)((val >> 8) & 0xFFL);
				result[2] = (byte)((val >> 16) & 0xFFL);
				result[3] = (byte)((val >> 24) & 0xFFL);
				result[4] = (byte)((val >> 32) & 0xFFL);
				result[5] = (byte)((val >> 40) & 0xFFL);
				result[6] = (byte)((val >> 48) & 0xFFL);
				result[7] = (byte)((val >> 56) & 0xFFL);
				break;
		}
		return result;
	}
	
	/**
	 * Converts the given bytes of values to a number using the value type.
	 * @param value
	 * @param valueType
	 * @return
	 */
	private static Object foundValueToString(byte[] value, int valueType) {
		byte[] val = value.clone();
		
		switch (valueType) {
			// BYTE
			case 0:
				reverse(val);
				return value[0];
			// SHORT
			case 1:
				reverse(val);
				return new BigInteger(val).shortValue();
			// INT
			case 2:
				reverse(val);
				return new BigInteger(val).intValue();
			// LONG
			case 3:
				reverse(val);
				return new BigInteger(val).longValue();
			// FLOAT
			case 4:
				reverse(val);
				//int v = (val[0] << 24) | (val[1] << 16) | (val[2] << 8) | val[3];
				int v = (val[0] << 24) | ((val[1] & 0xff) << 16) | ((val[2] & 0xff) << 8) | (val[3] & 0xff);
				
				return Float.intBitsToFloat(v);
			// DOUBLE
			case 5:
				long l = (val[0] << 56) | (val[1] << 48) | (val[2] << 40) | (val[3] << 32)
					| (val[4] << 24) | (val[5] << 16) | (val[6] << 16) | val[7];
				return Double.longBitsToDouble(l);
			//String
			case 6:
				ArrayList<Byte> saida = new ArrayList<Byte>();
				
				for(int i =0, j=0; i<value.length; i++){
					if( value[i] > 0 ){
						saida.add( value[i] );
					}
				}
				
				byte[] ret = new byte[saida.size()];
				for(int i =0; i<saida.size(); i++){
					ret[i] = saida.get( i );
				}
				
				return new String( ret );
			
		}
		
		return Arrays.toString(value);
	}
	
	/**
	 * Reverse the array.
	 * @param a
	 */
	private static void reverse(byte[] a) {
		int left = 0;
		int right = a.length - 1;
		while (left < right) {
			byte tmp = a[left];
			a[left] = a[right];
			a[right] = tmp;
			left++;
			right--;
		}
	}

	// AllocateMemory - Especially good for Codecaves ^^
	public static void AllocateMyProcessMemory(Pointer ProcessToAllocateRamIn, int StartingAdress, int SizeToAllocate) {
		// Access to desired functions
		Kernel32 Kernel32dll = Kernel32.INSTANCE;

		// Getting our Space
		Kernel32dll.VirtualAllocEx(ProcessToAllocateRamIn, StartingAdress,
				SizeToAllocate, ALLOCATION_TYPE_MEM_COMMIT
						| ALLOCATION_TYPE_MEM_RESERVE, 0x04);

	}

	public static void main(String[] args) {
//		WriteMyProcessMemory(MyProcess, 0x00913F13, newvalue);
//		AllocateMyProcessMemory(MyProcess, 0x10000000, 500);

	}
}