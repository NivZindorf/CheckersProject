package com.NivZindorf.checkers;

public class GameBoard {
	private  int [] [] board = new int [8][8];
	private  int aCount;
	private  int bCount;
	private int aSuperCount;
	private int bSuperCount;
	public GameBoard(int [] [] board)
	{
		this.board=board;
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				switch(this.board[i][j])
				{
				case Type.A:this.aCount++;
				break;
				case Type.B:this.bCount++;
				break;
				case Type.SuperA:this.aSuperCount++;
				break;
				case Type.SuperB:this.bSuperCount++;
				break;
				}
	}
	public GameBoard()
	{
		for (int i=0;i<3;i+=2)//build line 0,2 type=B
		{
		for(int j=0;j<8;j+=2)
		{
			board[i][j]=Type.UnPlayed;
			board[i][j+1]=Type.B;
		}
		}
		for(int j=0;j<8;j+=2)//build line 1 type=B
		{
			board[1][j]=Type.B;
			board[1][j+1]=Type.UnPlayed;
		}
		for (int i=5;i<8;i+=2)//build line 5,7 type=A
		{
		for(int j=0;j<8;j+=2)
		{
			board[i][j]=Type.A;
			board[i][j+1]=Type.UnPlayed;
		}
		}
		for(int j=0;j<8;j+=2)//build line 6 type=A
		{
			board[6][j]=Type.UnPlayed;
			board[6][j+1]=Type.A;
		}
		for(int j=0;j<8;j+=2)//build line 3 empty
		{
			board[3][j]=Type.Empty;
			board[3][j+1]=Type.UnPlayed;
		}
		for(int j=0;j<8;j+=2)//build line 4 empty
		{
			board[4][j]=Type.UnPlayed;
			board[4][j+1]=Type.Empty;
		}
		aCount=12;
		bCount=12;
		aSuperCount=0;
		bSuperCount=0;
		
	}
	public GameBoard (GameBoard duplicateBoard)
	{
		int [][] newBoard = new int[duplicateBoard.getBoard().length][];
		for(int i = 0; i < duplicateBoard.getBoard().length; i++)
		    newBoard[i] = duplicateBoard.getBoard()[i].clone();
		this.board=newBoard;
		int [] counters=duplicateBoard.getCounters();
		aCount=counters[0];
		aSuperCount = counters[1];
		bCount=counters[2];
		bSuperCount=counters[3];
	}

	public void undoStep(Step stp)
	{
		board[stp.getTo().getX()][stp.getTo().getY()]=Type.Empty;
		board[stp.getFrom().getX()][stp.getFrom().getY()]=stp.getType();
		if (stp.getType()==Type.A ||stp.getType()==Type.SuperA )
		{
			for (int i=0 ; i<stp.getI();i++)
				board[stp.getKill()[i].getX()][stp.getKill()[i].getY()]=Type.B;
			bCount += stp.getI();
		}
		else
		{
			for (int i=0 ; i<stp.getI();i++)
				board[stp.getKill()[i].getX()][stp.getKill()[i].getY()]=Type.A;
			aCount += stp.getI();
		}
	}
	public  void executeStep (Step stp) // פעולה שמעבירה מיקום של חייל
	{
		int i=0;
		boolean isPromotedToKing= false;
		int to;
		int from;
		if (stp.getType()==Type.A && stp.getTo().getX()==0)
		{
			to=Type.SuperA;
			board[stp.getTo().getX()][stp.getTo().getY()]=to;
			isPromotedToKing=true;
			aSuperCount++;
		}
		if (stp.getType()==Type.B && stp.getTo().getX()==7)
		{
			to=Type.SuperB;
			board[stp.getTo().getX()][stp.getTo().getY()]=to;
			isPromotedToKing=true;
			bSuperCount++;
		}
		if (!isPromotedToKing)
		{
			to=stp.getType();
			board[stp.getTo().getX()][stp.getTo().getY()]=to;
			if (stp.getPath()!=null)
				for (int j=0;j<stp.getI()-2;j++)
				{
					if(stp.getPath()[j].getX()==0&&stp.getType()==Type.A)
					{
						to=Type.SuperA;
						board[stp.getTo().getX()][stp.getTo().getY()]=Type.SuperA;
						aSuperCount++;
					}
					if(stp.getPath()[j].getX()==7&&stp.getType()==Type.B)
					{
						to=Type.SuperB;
						board[stp.getTo().getX()][stp.getTo().getY()]=Type.SuperB;
						bSuperCount++;
					}
				}
		}
		from=Type.Empty;
		board[stp.getFrom().getX()][stp.getFrom().getY()]=from;
		if (stp.getKill()!=null)
		{
		while (i<5 && stp.getKill()[i]!=null)
		{
			kill(stp.getKill()[i]);
			i++;
		}
		
		}
	}
	public  void kill (Index where) // פעולה ש"אוכלת"חייל ומוריד 1 למס, השחקנים שלו
	{
		switch (board[where.getX()][where.getY()])
		{
		case Type.A:aCount--;
		break;
		case Type.B:bCount--;
		break;
		case Type.SuperA: aSuperCount--;
		break;
		case Type.SuperB: bSuperCount--;
		default:
			break;
		}
		int wheretyp=Type.Empty;
		board[where.getX()][where.getY()]=wheretyp;
	}
	public  boolean isEnd()
	{
		if( (aCount==0 && aSuperCount==0)|| bCount==0&& bSuperCount==0 )
			return true;
		else
			return false;
	}
	public  String whoWin()
	{
		if (aCount==0 && aSuperCount==0)
			return ("player A");
		else
			return ("player B");
					
	}
	public  int getType (Index where)
	{
		if (where.getX()<0 || where.getX()>7 || where.getY()<0 || where.getY() >7)
			return Type.UnPlayed;
		else
		return board[where.getX()][where.getY()];
	}
	public  int getType (int x , int y)
	{
		return board [x][y];
	}
	public static GameBoard duplicateAndExecute(GameBoard duplicate ,Step stp)
	{
		GameBoard returnBoard = new GameBoard(duplicate);
		returnBoard.executeStep(stp);
		return returnBoard;
		
	}
	public int evaluate()
	{
		if (!isEnd())
			return bCount+bSuperCount*3-aCount-aSuperCount*3;
		else
		{
			if (whoWin().equals("player A"))
				return -300;
			else
				return 300;
		}
		
	}
	public int [] [] getBoard()
	{
		return this.board;
	}
	public int[] getCounters()
	{
		int [] ret={aCount,aSuperCount,bCount,bSuperCount}; 
		return ret;
	}
	@Override
	public String toString()
	{
		String ret="";
		for (int i =0;i<8;i++)
			for(int j =0; j<8;j++)
				ret += board[i][j];
		return ret;
		
	}
	public static int [] [] stringToBoard(String str)
	{
		int [] [] ret= new int[8][8];
		for (int i =0;i<8;i++)
			for(int j =0; j<8;j++)
			{
				ret[i][j]=Character.getNumericValue(str.charAt(i*8+j));
			}
		return ret;
	}

}
