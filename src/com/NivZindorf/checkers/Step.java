package com.NivZindorf.checkers;

public class Step {
	private int i; //כמות ההריגות
	private Index from;
	private Index to;
	private Index [] kill;
	private Index [] path;
	private int type;
	private GameBoard board;
	private boolean infinityStep;// אם המהלך הולך לחזור על עצמו
	public Step (Index from , Index to , GameBoard board)
	{
		i=0;
		this.from = from;
		this.to= to;
		kill = null;
		path = null;
		this.board = board;
		this.type = this.board.getType(this.from);
		infinityStep = false;
	}
	public Step (int xFrom , int yFrom , int xTo , int yTo ,GameBoard board)
	{
		i=0;
		this.from = new Index (xFrom , yFrom);
		this.to= new Index(xTo , yTo);
		kill = null;
		path = null;
		this.board = board;
		this.type = this.board.getType(this.from);
		infinityStep = false;
	}
	public Step(Index from , Index to , Index kill , GameBoard board)
	{
		i=1;
		this.from = from;
		this.to= to;
		this.kill = new Index [10];
		this.path = null;
		this.kill[0]=kill;
		this.board = board;
		this.type = this.board.getType(this.from);
		infinityStep = false;
	}
	public Step (Step stp)
	{
		this.from = stp.getFrom();
		this.to = stp.getTo();
		if (stp.getKill()!=null)
			this.kill=stp.getKill().clone();
		else
			this.kill=null;
		if (stp.getPath()!=null)
			this.path=stp.getPath().clone();
		else
			this.path=null;
		this.i=stp.getI();
		this.board = stp.getBoard();
		this.type = stp.getType();
		this.infinityStep = stp.isInfinity();
	}
	public boolean isInfinity ()
	{
		return infinityStep;
	}
	public Index getFrom ()
	{
		return this.from;
	}
	public Index getTo ()
	{
		return this.to;
	}
	public Index [] getKill ()
	{
		return this.kill;
	}
	public Index [] getPath ()
	{
		return this.path;
	}
	public int getI()
	{
		return this.i;
	}
	public void addKill(Index kill, Index to)
	{
		
		if (this.kill==null)
		{
			this.kill = new Index[10];
			this.kill[i]=kill;
			this.to = to;
			i++;
		}
		else
		{
			if (path==null)
				this.path = new Index [10];
			if (i>3)
			{
				if (to.compare(from))
					infinityStep = true;
				else
					for (int j=0;j<i-1;j++)
						if (to.compare(path[j]))
							infinityStep=true;
			}
			if (!infinityStep)
				path[i-1]=this.to;
			this.to = to;
			this.kill[i]=kill;
			i++;
		}
	}
	public GameBoard getBoard ()
	{
		return this.board;
	}
	public int getType ()
	{
		return this.type;
	}

}
