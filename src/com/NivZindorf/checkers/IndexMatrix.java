package com.NivZindorf.checkers;

public class IndexMatrix {
	private static final Index [] [] matrix= createMatrix();
	public static Index getIndex(int x,int y)
	{
		return matrix[x][y];
	}
	public static Index getIndex(String id)
	{
		int x = Character.getNumericValue(1);
		int y= Character.getNumericValue(2);
		return matrix[x][y];
		
	}
	private static Index[][] createMatrix()
	{
		Index [][] matrix=new Index[8][8];
		for (byte i=0;i<8;i++)
			if(i%2==0)
				for(byte j=1;j<8;j +=2)
					matrix[i][j]=new Index(i,j);
			else
				for(byte j=0;j<8; j+=2)
					matrix[i][j]=new Index(i,j);
		return matrix;
	}

}
