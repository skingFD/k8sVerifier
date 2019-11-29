package bean;

import java.util.BitSet;

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
	
	public boolean getBit(int row, int column) {
		return data.get(row*columns+column);
	}
	
	public void setBit(int row, int column) {
		data.set(row*columns+column);
	}
	
	public void setRow(int row, boolean value) {
		data.set(row*columns, (row+1)*+columns, value);
	}
	
	public void setRow(int row, BitSet rowdata) {
		for(int i = 0; i < columns; i++) {
			this.data.set(row*columns+i, rowdata.get(i));
		}
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
	
	public static void main(String args[]) {
		bitMatrix test = new bitMatrix(3,3);
		test.setRow(1, true);
		test.printRow(1);
	}
}