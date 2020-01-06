package bean;

import java.util.ArrayList;
import java.util.BitSet;

import utils.randomUtil;

public class bitMatrix{
	int rows;
	int columns;
	BitSet data;
	
	public bitMatrix() {
		this.rows = 0;
		this.columns = 0;
		data = new BitSet();
	}
	
	public bitMatrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		data = new BitSet(rows*columns);
	}
	
	public bitMatrix(int pods) {
		this.rows = pods;
		this.columns = pods;
		data = new BitSet(pods*pods);
	}
	
	public bitMatrix(int size, BitSet row, BitSet Column) {
		this.rows = size;
		this.columns = size;
		data = new BitSet(size*size);
		int bitIndex = row.nextSetBit(0);
		while(bitIndex != -1) {
			this.setRow(bitIndex, Column);
			bitIndex = row.nextSetBit(bitIndex+1);
		}
	}
	
	public boolean getBit(int row, int column) {
		return data.get(row*columns+column);
	}
	
	public void setBit(int row, int column, boolean value) {
		data.set(row*columns+column, value);
	}
	
	public void setRow(int row, boolean value) {
		data.set(row*columns, (row+1)*+columns, value);
	}
	
	public void setRow(int row, BitSet rowdata) {
		for(int i = 0; i < columns; i++) {
			this.data.set(row*columns+i, rowdata.get(i));
		}
	}
	
	public BitSet getRow(int row) {
		BitSet result = new BitSet(columns);
		result.or(data.get(row*columns, row*columns+columns));
		return result;
	}
	
	public void setColumn(int column, boolean value) {
		for(int i = 0; i < rows; i++) {
			data.set(i*columns + column, value);
		}
	}
	
	public void setColumn(int column, BitSet rowdata) {
		for(int i = 0; i < rows; i++) {
			this.data.set(i*columns+column, rowdata.get(i));
		}
	}
	
	public BitSet getColumn(int column) {
		BitSet result = new BitSet(rows);
		for(int i = 0; i < rows; i++) {
			result.set(i, data.get(i*columns+column));
		}
		return result;
	}
	
	public void print() {
		System.out.print("[");
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				if(data.get(i*columns+j)) {
					System.out.print("1,");
				}else {
					System.out.print("0,");
				}
			}
			System.out.println("");
		}
		System.out.print("]");
	}
	
	public void printRow(int row) {
		System.out.print("[");
		for (int j = 0; j < columns; j++) {
			if (data.get(row * columns + j)) {
				System.out.print("1,");
			} else {
				System.out.print("0,");
			}
		}
		System.out.print("]");
	}
	
	public void printColumn(int column) {
		System.out.print("[");
		for (int i = 0; i < rows; i++) {
			if (data.get(i * columns + column)) {
				System.out.print("1,");
			} else {
				System.out.print("0,");
			}
		}
		System.out.print("]");
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public BitSet getData() {
		return data;
	}

	public void setData(BitSet data) {
		this.data = data;
	}
	
	public void and(bitMatrix matrix0) {
		this.data.and(matrix0.getData());
	}
	
	public void or(bitMatrix matrix0) {
		this.data.or(matrix0.getData());
	}
	
	public void xor(bitMatrix matrix0) {
		this.data.xor(matrix0.getData());
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	public static void main(String args[]) {
		int size = 10; // matrix
		int points = 50; // points 
		//test main
		//init reachability matrix
		bitMatrix reachability = new bitMatrix(size);
		for(int i = 0; i < points; i++) {
			int row = randomUtil.getRandomInt(0, size);
			int column = randomUtil.getRandomInt(0, size);
			reachability.setBit(row, column, true);
		}
		//Init:
		//[1,1,0,0,0...
		// 1,1,0,0,0...
		// 0,0,0,0,0...]
		//reachability.setBit(0, 0, true);
		//reachability.setBit(0, 1, true);
		//reachability.setBit(1, 0, true);
		//reachability.setBit(1, 1, true);
		
		//init record matrix
		bitMatrix tempReachability = new bitMatrix(size);
		tempReachability.or(reachability);
		ArrayList<bitMatrix> divisions = new ArrayList<bitMatrix>();
		ArrayList<Integer> coveredRows = new ArrayList<Integer>();
		ArrayList<Integer> coveredColumns = new ArrayList<Integer>();
		boolean haveSingleRow = true;
		boolean haveSingleColumn = true;
		
		while (!tempReachability.isEmpty()) {
			while (haveSingleRow || haveSingleColumn) {
				// find single row
				haveSingleRow = false;
				haveSingleColumn = false;
				for (int i = 0; i < size; i++) {
					int position = -1;
					int num = 0;
					for (int j = 0; j < size; j++) {
						if (tempReachability.getBit(i, j)) {
							if (num == 0) {
								num++;
								position = j;
							} else {
								num++;
								break;
							}
						}
					}
					if (num == 1) {
						haveSingleRow = true;
						coveredRows.add(position);
						bitMatrix tempDivision = new bitMatrix(size);
						for (int j = 0; j < size; j++) {
							tempDivision.setBit(j, position, tempReachability.getBit(j, position));
							tempReachability.setBit(j, position, false);
						}
						divisions.add(tempDivision);
					}
				}
				// find single column
				for (int i = 0; i < size; i++) {
					int position = -1;
					int num = 0;
					for (int j = 0; j < size; j++) {
						if (tempReachability.getBit(j, i)) {
							if (num == 0) {
								num++;
								position = j;
							} else {
								num++;
								break;
							}
						}
					}
					if (num == 1) {
						haveSingleColumn = true;
						coveredColumns.add(position);
						bitMatrix tempDivision = new bitMatrix(size);
						for (int j = 0; j < size; j++) {
							tempDivision.setBit(position, j, tempReachability.getBit(position, j));
							tempReachability.setBit(position, j, false);
						}
						divisions.add(tempDivision);
					}
				}
			}
			//get all linked pairs
			int seed = -1;
			int seedRow = -1;
			int seedColumn = -1;
			BitSet columnBit = new BitSet(size);
			BitSet rowBit = new BitSet(size);
			seed = tempReachability.getData().nextSetBit(0);
			if (seed != -1) {
				seedRow = seed / size;
				seedColumn = seed % size;
				columnBit.or(tempReachability.getRow(seedRow));
				rowBit.set(seedRow);
				// strategy: column in priority
				tempReachability.setRow(seedRow, false);
				for (int i = seedRow; i < size; i++) {
					if (tempReachability.getRow(i).equals(columnBit)) {
						rowBit.set(i);
						tempReachability.setRow(i, false);
					}
				}
				divisions.add(new bitMatrix(size, rowBit, columnBit));
			}
		}
		
		reachability.print();
		System.out.print(divisions);
	}
}