package com.NivZindorf.checkers;

import java.util.ArrayList;

public class CheckStep {

	public static ArrayList<Step> forceMove;
	public static ArrayList<Step> unForceMove;
	public static void reset()
	{
		forceMove= new ArrayList<Step>();
		unForceMove = new ArrayList<Step>();
	}
	public static Step [] checkAllA(GameBoard board)
	{
		reset();
		
		for (int k=0; k<8; k++)
			for (int t=0;t<8;t++)
			{
				if(board.getType(k,t)==Type.A)
				{
					if(forceMove.isEmpty())
						checkA(board , IndexMatrix.getIndex(k,t));
					
						canAKill(board , IndexMatrix.getIndex(k,t) , null);
				}
				if (board.getType(k, t)==Type.SuperA)
					checkSuper(board, IndexMatrix.getIndex(k,t));
			}
		if (forceMove.isEmpty())
			return unForceMove.toArray(new Step [unForceMove.size()]);
		else
			return forceMove.toArray(new Step [forceMove.size()]);
			}
	public static Step [] checkAllB(GameBoard board)
	{
		reset();
		
		for (int k=0; k<8; k++)
			for (int t=0;t<8;t++)
			{
				if(board.getType(k,t)==Type.B)
				{	
					if(forceMove.isEmpty())
						checkB(board , IndexMatrix.getIndex(k,t));
					
						canBKill(board , IndexMatrix.getIndex(k,t) , null);
				}
				if(board.getType(k,t)==Type.SuperB)
					checkSuper(board, IndexMatrix.getIndex(k,t));
			}
		if (forceMove.isEmpty())
			return unForceMove.toArray(new Step[unForceMove.size()]);
		else
			return forceMove.toArray(new Step[forceMove.size()]);
	}
	public static void checkSuper(GameBoard board , Index ind)
	{
		int typ = board.getType(ind);
		int x ;
		int y ;
		int indX= ind.getX();
		int indY= ind.getY();
		
		Step dummyStep = new Step(ind , ind , board);
		if (typ==Type.SuperA)
			canAKill(board,ind,dummyStep);
		else
			canBKill(board, ind, dummyStep);
		if (dummyStep.getI()>0)
		{
			forceMove.add(new Step(dummyStep));
			
			dummyStep = new Step(ind , ind , board);
		}
		if (indX!=0 && indY!=7)
		{
		x = indX-1;
		y = indY+1;
		
		while (x>=0 && y<=7 && board.getType(x, y)==Type.Empty) // up right
		{
			if (forceMove.isEmpty())
			{
				unForceMove.add( new Step(ind , IndexMatrix.getIndex(x,y),board));
				
			}
			x--;
			y++;
		}
		if (y<7 && x>0)
		{
			if (typ==Type.SuperA)
			{
				if (board.getType(x, y)==Type.B || board.getType(x, y)==Type.SuperB)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x+1,y-1), board);
					if (x+1!=indX)
					{
						canAKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
			else
			{
				if (board.getType(x, y)==Type.A || board.getType(x, y)==Type.SuperA)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x+1,y-1), board);
					if (x+1!=indX)
					{
						canBKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
		}
		}
		if (indX!=7 && indY!=7)
		{
		x = indX+1;
		y= indY+1;
		
		while (x<=7 && y<=7 && board.getType(x, y)==Type.Empty) // down right
		{
			if (forceMove.isEmpty())
			{
				unForceMove.add( new Step(ind , IndexMatrix.getIndex(x,y),board));
				
				
			}
			x++;
			y++;
		}
		if (y<7 && x<7)
		{
			if (typ==Type.SuperA)
			{
				if (board.getType(x, y)==Type.B || board.getType(x, y)==Type.SuperB)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x-1,y-1), board);
					if (x-1!=indX)
					{
						canAKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
			else
			{
				if (board.getType(x, y)==Type.A || board.getType(x, y)==Type.SuperA)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x-1,y-1), board);
					if (x-1!=indX)
					{
						canBKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
		}
		}
		if (indX!=0 && indY!=0)
		{
		x = indX-1;
		y= indY-1;
		
		while (x>=0 && y>=0 && board.getType(x, y)==Type.Empty) // up left
		{
			if (forceMove.isEmpty())
			{
				unForceMove.add( new Step(ind , IndexMatrix.getIndex(x,y),board));
				
				
			}
			x--;
			y--;
		}
		if (y>0 && x>0)
		{
			if (typ==Type.SuperA)
			{
				if (board.getType(x, y)==Type.B || board.getType(x, y)==Type.SuperB)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x+1,y+1), board);
					if (x+1!=indX)
					{
						canAKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
			else
			{
				if (board.getType(x, y)==Type.A || board.getType(x, y)==Type.SuperA)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x+1,y+1), board);
					if (x+1!=indX)
					{
						canBKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
		}
		}
		if (indX!=7 && indY!=0)
		{
		x = indX+1;
		y= indY-1;
		
		while (x<=7 && y>=0 && board.getType(x, y)==Type.Empty) // down left
		{
			if (forceMove.isEmpty())
			{
				unForceMove.add( new Step(ind , IndexMatrix.getIndex(x,y),board));
				
				
			}
			x++;
			y--;
		}
		if (y>0 && x<7)
		{
			if (typ==Type.SuperA)
			{
				if (board.getType(x, y)==Type.B || board.getType(x, y)==Type.SuperB)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x-1,y+1), board);
					if (x-1!=indX)
					{
						canAKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
			else
			{
				if (board.getType(x, y)==Type.A || board.getType(x, y)==Type.SuperA)
				{
					dummyStep = new Step(ind , IndexMatrix.getIndex (x-1,y+1), board);
					if (x-1!=indX)
					{
						canBKill(board, dummyStep.getTo(), dummyStep);
						if (dummyStep.getKill() != null)
						{
							forceMove.add(new Step(dummyStep));
							
						}
					}
				}
			}
		}
		}
		}
		
		
	
	public static void canSuperKill (GameBoard board , Index ind , Step stp)
	{
		
	}
	
	public static void checkA (GameBoard board , Index ind )
	{
		
		int x =ind.getX();
		int y = ind.getY();	
		if (x!=0)
		{
			if (y==0)
			{
				if (board.getType(x-1,y+1)==Type.Empty)
				{
					unForceMove.add( new Step (x,y,x-1,y+1 , board));
					
				}
				
			}
			if (y==7)
			{
				if (board.getType(x-1,y-1)==Type.Empty)
				{
					unForceMove.add( new Step (x,y,x-1,y-1 , board));
					
				}
				
			}
			if (y>0&&y<7)
			{
				if (board.getType(x-1,y+1)==Type.Empty)
				{
					unForceMove.add( new Step (x,y,x-1,y+1 , board));
					
				}	
				if (board.getType(x-1,y-1)==Type.Empty)
				{
					unForceMove.add( new Step (x,y,x-1,y-1 , board));
					
				}
				
			}
		}
	}
	public static void canAKill(GameBoard board , Index ind ,  Step stp) //פעולה רקורסיבית
	{
		
		int x =ind.getX();
		int y = ind.getY();	
		boolean hasStep = stp!=null;
		boolean killUpRight = false;
		boolean killUpLeft = false;
		boolean checkDownLeft = false , checkDownRight = false;
		boolean checkUpLeft = false , checkUpRight = false;
		boolean killDownRight=false , killDownLeft=false;
		Step tmpStep;
		if (hasStep)
		{
			if (stp.isInfinity())
				return;
			tmpStep=new Step(stp);
			if (stp.getType()==Type.A)
			{
				if (stp.getKill()[stp.getI()-1].getX()==ind.getX()-1)
				{
					if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
						checkUpLeft=true;
					else
						checkUpRight=true;
					checkDownLeft=true;
					checkDownRight=true;
				}
				else
				{
					if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
						checkDownLeft=true;
					else
						checkDownRight=true;
					checkUpLeft=true;
					checkUpRight=true;
				}
			}
			else
			{
				if (stp.getFrom().compare(stp.getTo())) // dummyStep
				{
					int tmpX= stp.getFrom().getX();
					int tmpY=stp.getFrom().getY();
					if (tmpX>1)
					{
						if (tmpY>1)
							checkUpLeft=true;
						if (tmpY<6)
							checkUpRight=true;
					}
					if (tmpX<6)
					{
						if (tmpY>1)
							checkDownLeft=true;
						if (tmpY<6)
							checkDownRight=true;
					}	
				}
				else 
				{
					if (stp.getI()>0) // if there is kill on Step
					{
						if (stp.getKill()[stp.getI()-1].getX()==ind.getX()-1)
						{
							if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
								checkUpLeft=true;
							else
								checkUpRight=true;
							checkDownLeft=true;
							checkDownRight=true;
						}
						else
						{
							if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
								checkDownLeft=true;
							else
								checkDownRight=true;
							checkUpLeft=true;
							checkUpRight=true;
						}
					}
					else
					{
						if (stp.getTo().getX()<stp.getFrom().getX())
						{
							if (stp.getTo().getY()>stp.getFrom().getY())
								checkUpRight=true;
							else
								checkUpLeft=true;
						}
						else
						{
							if (stp.getTo().getY()>stp.getFrom().getY())
								checkDownRight=true;
							else
								checkDownLeft=true;
						}
					}
				}
			}
		}
		
		else
		{
			tmpStep= new Step (ind , ind , board);
			checkUpLeft=true;
			checkUpRight=true;
		}
		if (x>1)
		{
		if (y>5)
		{
			if (checkUpLeft)
			killUpLeft= ((board.getType(x-1, y-1)==Type.B ||board.getType(x-1, y-1)== Type.SuperB)&&board.getType(x-2, y-2)==Type.Empty);
		}
		else  
			if (y<2)
			{
				if (checkUpRight)
				killUpRight= (board.getType(x-1, y+1)==Type.B || board.getType(x-1, y+1)==Type.SuperB)&&board.getType(x-2, y+2)==Type.Empty;
			}
			else
				{
				if (checkUpLeft)
				killUpLeft= ((board.getType(x-1, y-1)==Type.B ||board.getType(x-1, y-1)== Type.SuperB)&&board.getType(x-2, y-2)==Type.Empty);
				if (checkUpRight)
				killUpRight= (board.getType(x-1, y+1)==Type.B || board.getType(x-1, y+1)==Type.SuperB)&&board.getType(x-2, y+2)==Type.Empty;
				}
		}
		if (x<6)
		{
			if (y>5)
			{
				if (checkDownLeft)
				killDownLeft= ((board.getType(x+1, y-1)==Type.B ||board.getType(x+1, y-1)== Type.SuperB)&&board.getType(x+2, y-2)==Type.Empty);
			}
			else  
				if (y<2)
				{
					if (checkDownRight)
					killDownRight= (board.getType(x+1, y+1)==Type.B || board.getType(x+1, y+1)==Type.SuperB)&&board.getType(x+2, y+2)==Type.Empty;
				}
				else
					{
					if (checkDownLeft)
					killDownLeft= ((board.getType(x+1, y-1)==Type.B ||board.getType(x+1, y-1)== Type.SuperB)&&board.getType(x+2, y-2)==Type.Empty);
					if (checkDownRight)
					killDownRight= (board.getType(x+1, y+1)==Type.B || board.getType(x+1, y+1)==Type.SuperB)&&board.getType(x+2, y+2)==Type.Empty;
					}
		}
		
			if(killUpRight)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x-1, y+1), IndexMatrix.getIndex(x-2, y+2));// add right kill 
					canAKill(board , IndexMatrix.getIndex(x-2, y+2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x-1, y+1), IndexMatrix.getIndex(x-2, y+2));
					
					canAKill(board , IndexMatrix.getIndex(x-2, y+2) , forceMove.get(forceMove.size()-1));
				}
			}
			if (killUpLeft)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x-1, y-1), IndexMatrix.getIndex(x-2, y-2));// add left kill 
					canAKill(board , IndexMatrix.getIndex(x-2, y-2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x-1, y-1), IndexMatrix.getIndex(x-2, y-2));
					
					canAKill(board , IndexMatrix.getIndex(x-2, y-2) , forceMove.get(forceMove.size()-1));
				}
			}
			if(killDownRight)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x+1, y+1), IndexMatrix.getIndex(x+2, y+2));// add right kill 
					canAKill(board , IndexMatrix.getIndex(x+2, y+2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x+1, y+1), IndexMatrix.getIndex(x+2, y+2));
					
					canAKill(board , IndexMatrix.getIndex(x+2, y+2) , forceMove.get(forceMove.size()-1));
				}
			}
			if (killDownLeft)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x+1, y-1), IndexMatrix.getIndex(x+2, y-2));// add left kill 
					canAKill(board , IndexMatrix.getIndex(x+2, y-2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x+1, y-1), IndexMatrix.getIndex(x+2, y-2));
					
					canAKill(board , IndexMatrix.getIndex(x+2, y-2) , forceMove.get(forceMove.size()-1));
				}
			}
		
				
		
				
		} 
	public static void checkB (GameBoard board , Index ind)
	{
		
		int x =ind.getX();
		int y = ind.getY();	
			if (x!=7)
			{
				if (y==0)
				{
					if (board.getType(x+1,y+1)==Type.Empty)
					{
						unForceMove.add( new Step (x,y, x+1,y+1 , board));
						
					}
					
				}
				if (y==7)
				{
					if (board.getType(x+1,y-1)==Type.Empty)
					{
						unForceMove.add( new Step (x,y, x+1,y-1 , board));
						
					}
					
				}
				if (y>0&&y<7)
				{
					if (board.getType(x+1,y+1)==Type.Empty)
					{
						unForceMove.add( new Step (x,y, x+1,y+1 , board));
						
					}	
					if (board.getType(x+1,y-1)==Type.Empty)
					{
						unForceMove.add( new Step (x,y, x+1,y-1 , board));
						
					}
					
				}
			}
		}
	public static void canBKill(GameBoard board ,Index ind , Step stp) //פעולה רקורסיבית
	{
		int x =ind.getX();
		int y = ind.getY();	
		boolean hasStep = stp!=null;
		boolean killUpRight = false;
		boolean killUpLeft = false;
		boolean checkDownLeft = false , checkDownRight = false;
		boolean checkUpLeft = false , checkUpRight = false;
		boolean killDownRight=false , killDownLeft=false;
		Step tmpStep;
		if (hasStep)
		{
			if (stp.isInfinity())
				return;
			tmpStep= new Step(stp);
			if(stp.getType()==Type.B)
			{
			if (stp.getKill()[stp.getI()-1].getX()==ind.getX()+1)
			{
				if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
					checkUpLeft=true;
				else
					checkUpRight=true;
				checkDownLeft=true;
				checkDownRight=true;
			}
			else
			{
				if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
					checkDownLeft=true;
				else
					checkDownRight=true;
				checkUpLeft=true;
				checkUpRight=true;
			}
			
		}
		else
		{
			if (stp.getFrom().compare(stp.getTo())) // dummyStep
			{
				int tmpX= stp.getFrom().getX();
				int tmpY=stp.getFrom().getY();
				if (tmpX>1)
				{
					if (tmpY>1)
						checkDownLeft=true;
					if (tmpY<6)
						checkDownRight=true;
				}
				if (tmpX<6)
				{
					if (tmpY>1)
						checkUpLeft=true;
					if (tmpY<6)
						checkUpRight=true;
				}	
			}
			else 
			{
				if (stp.getI()>0) // if there is kill on Step
				{
					if (stp.getKill()[stp.getI()-1].getX()==ind.getX()-1)
					{
						if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
							checkDownLeft=true;
						else
							checkDownRight=true;
						checkUpLeft=true;
						checkUpRight=true;
					}
					else
					{
						if (stp.getKill()[stp.getI()-1].getY()==ind.getY()+1)
							checkUpLeft=true;
						else
							checkUpRight=true;
						checkDownLeft=true;
						checkDownRight=true;
					}
				}
				else
				{
					if (stp.getTo().getX()<stp.getFrom().getX())
					{
						if (stp.getTo().getY()>stp.getFrom().getY())
							checkDownRight=true;
						else
							checkDownLeft=true;

					}
					else
					{
						if (stp.getTo().getY()>stp.getFrom().getY())
							checkUpRight=true;
						else
							checkUpLeft=true;

					}
				}
			}
		}
	}
		else
		{
			tmpStep= new Step(ind , ind , board);
			checkUpLeft=true;
			checkUpRight=true;
		}
		if (x<6)
		{
		if (y>5)
		{
			if (checkUpLeft)
			killUpLeft= ((board.getType(x+1, y-1)==Type.A ||board.getType(x+1, y-1)== Type.SuperA)&&board.getType(x+2, y-2)==Type.Empty);
		}
		else 
			if (y<2)
			{
				if (checkUpRight)
					killUpRight= (board.getType(x+1, y+1)==Type.A || board.getType(x+1, y+1)==Type.SuperA)&&board.getType(x+2, y+2)==Type.Empty;
			}
			else
				{
				if (checkUpLeft)
					killUpLeft= ((board.getType(x+1, y-1)==Type.A ||board.getType(x+1, y-1)== Type.SuperA)&&board.getType(x+2, y-2)==Type.Empty);
				if (checkUpRight)
					killUpRight= (board.getType(x+1, y+1)==Type.A || board.getType(x+1, y+1)==Type.SuperA)&&board.getType(x+2, y+2)==Type.Empty;
				}
		}
		if (x>1)
		{
			if (y>5)
			{
				if (checkDownLeft)
				killDownLeft= ((board.getType(x-1, y-1)==Type.A ||board.getType(x-1, y-1)== Type.SuperA)&&board.getType(x-2, y-2)==Type.Empty);
			}
			else 
				if (y<2)
				{
					if (checkDownRight)
						killDownRight= (board.getType(x-1, y+1)==Type.A || board.getType(x-1, y+1)==Type.SuperA)&&board.getType(x-2, y+2)==Type.Empty;
				}
				else
					{
					if (checkDownLeft)
						killDownLeft= ((board.getType(x-1, y-1)==Type.A ||board.getType(x-1, y-1)== Type.SuperA)&&board.getType(x-2, y-2)==Type.Empty);
					if (checkDownRight)
						killDownRight= (board.getType(x-1, y+1)==Type.A || board.getType(x-1, y+1)==Type.SuperA)&&board.getType(x-2, y+2)==Type.Empty;
					}
		}
			if(killUpRight)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x+1, y+1), IndexMatrix.getIndex(x+2, y+2));// add right kill
					canBKill(board , IndexMatrix.getIndex(x+2, y+2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x+1, y+1), IndexMatrix.getIndex(x+2, y+2));
					
					canBKill(board , IndexMatrix.getIndex(x+2, y+2) , forceMove.get(forceMove.size()-1));
				}
			}
			if (killUpLeft)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x+1, y-1), IndexMatrix.getIndex(x+2, y-2));// add left kill 
					canBKill(board , IndexMatrix.getIndex(x+2, y-2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x+1, y-1), IndexMatrix.getIndex(x+2, y-2));
					
					canBKill(board , IndexMatrix.getIndex(x+2, y-2) , forceMove.get(forceMove.size()-1));
				}
			}
			if(killDownRight)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x-1, y+1), IndexMatrix.getIndex(x-2, y+2));// add right kill 
					canBKill(board , IndexMatrix.getIndex(x-2, y+2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x-1, y+1), IndexMatrix.getIndex(x-2, y+2));
					
					canBKill(board , IndexMatrix.getIndex(x-2, y+2) , forceMove.get(forceMove.size()-1));
				}
			}
			if (killDownLeft)
			{
				if (hasStep)
				{
					stp.addKill(IndexMatrix.getIndex (x-1, y-1), IndexMatrix.getIndex(x-2, y-2));// add left kill 
					canBKill(board , IndexMatrix.getIndex(x-2, y-2) , stp);
					hasStep=false;
				}
				else
				{
					forceMove.add(new Step(tmpStep));
					forceMove.get(forceMove.size()-1).addKill(IndexMatrix.getIndex (x-1, y-1), IndexMatrix.getIndex(x-2, y-2));
					
					canBKill(board , IndexMatrix.getIndex(x-2, y-2) , forceMove.get(forceMove.size()-1));
				}
			}		
		}	
	}
					


