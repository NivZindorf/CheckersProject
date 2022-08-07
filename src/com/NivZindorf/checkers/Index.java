package com.NivZindorf.checkers;

import android.content.Context;


public class Index {
	private byte x;
	private byte y;
	public Index (int x , int y)
	{
		this.x=(byte)x;
		this.y=(byte)y;
	}

	public Index(String id)
	{
		this.x =(byte) Character.getNumericValue(id.charAt(1));
		this.y =(byte) Character.getNumericValue(id.charAt(2));
		
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int toId(Context context)
	{
		
		String str = "i"+this.x+this.y;
		int id = context.getResources().getIdentifier(str, "id", context.getPackageName());
		return id;
	}
	public boolean compare(Index ind)
	{
		return (this.x==ind.getX())&&(this.y==ind.getY());
	}

}
