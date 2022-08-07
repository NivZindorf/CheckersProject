package com.NivZindorf.checkers;

public class AI {
	private static int MAX_DEPTH;
	public static void setMaxDepth(int max)
	{
		MAX_DEPTH=max;
	}
	public static int miniMax(GameBoard board, int depth , boolean max)
	{
		if (depth==MAX_DEPTH)
			return board.evaluate();
		Step [] steps;
		int tmpVal;
		if(max)
		{
			steps=CheckStep.checkAllB(board);
			if (steps.length==0)
				return -300;
			int maxVal = miniMax(GameBoard.duplicateAndExecute(board, steps[0]),depth+1,!max);
			for (int i=1;i<steps.length;i++)
			{
				tmpVal=miniMax(GameBoard.duplicateAndExecute(board, steps[i]), depth+1, !max);
				if (maxVal<tmpVal)
					maxVal=tmpVal;
			}
			return maxVal;
		}
		else
		{
			steps=CheckStep.checkAllA(board);
			if (steps.length==0)
				return 300;
			int minVal = miniMax(GameBoard.duplicateAndExecute(board, steps[0]),depth+1,!max);
			for (int i=1;i<steps.length;i++)
			{
				tmpVal=miniMax(GameBoard.duplicateAndExecute(board, steps[i]), depth+1, !max);
				if (minVal>tmpVal)
					minVal=tmpVal;
			}
			return minVal;
		}
	}
	public static Step getBestStep (GameBoard board)
	{
		int max;
		int tmp;
		int tmpDepth=0;
		int index=0;
		Step [] steps=CheckStep.checkAllB(board);
		if (steps.length==1)
			return steps[0];
		if(board.getCounters()[1]+board.getCounters()[3]>2&&MAX_DEPTH==8)
		{
			MAX_DEPTH--;
			tmpDepth++;
		}
		if((board.getCounters()[1]>2||board.getCounters()[3]>2)&&MAX_DEPTH==7)
		{
			MAX_DEPTH--;
			tmpDepth++;
		}
		max=miniMax(GameBoard.duplicateAndExecute(board, steps[0]),1,false);
		for (int i = 1 ; i<steps.length;i++)
		{
			tmp=miniMax(GameBoard.duplicateAndExecute(board, steps[i]),1,false);
			if (max<tmp)
			{
				max=tmp;
				index=i;
			}
		}
		MAX_DEPTH +=tmpDepth;
		return steps[index];
			
	}
	
			
		
	}
