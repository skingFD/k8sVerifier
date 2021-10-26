package utils;

import java.util.BitSet;

public class bitsetUtil{
	/**
	 * remove a bit in a bitset and return a new bit set
	 * @param origin bitset
	 * @param the removed bit index
	 * @return
	 */
	public static void removeBit(BitSet origin, int index) {
		if(index >= origin.length()) {
			assert false;
		}else if(index == origin.length() - 1) {
			origin.get(0, index);
		}else {
			BitSet tmp = origin.get(1, origin.length());
			origin.clear(index, origin.length());
			tmp.clear(0, index);
			origin.or(tmp);
			//BitSet result = origin.get(1, origin.length());
			//result.clear(0, index);
			//result.or(origin.get(0, index));
			//return result;
		}
	}
	
}