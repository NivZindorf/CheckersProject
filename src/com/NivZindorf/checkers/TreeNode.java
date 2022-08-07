package com.NivZindorf.checkers;

import java.util.LinkedList;
import java.util.List;

	public class TreeNode {
		private static  int MAX_DEPTH=8;
		private GameBoard info;
		private List<TreeNode> children;
		private boolean isMax; //true = max false = min
		public static void setMaxDepth(int maxDepth)
		{
			MAX_DEPTH=maxDepth;
		}
		public TreeNode (GameBoard board , boolean isMax)
		{
			this.info=board;
			this.children= new LinkedList<TreeNode>();
			this.isMax=isMax;
		}
		
		public void generateChildren(int depth)
		{
			Step[] steps;
			if(this.isMax)
			 steps =CheckStep.checkAllB(this.info);
			else
				steps= CheckStep.checkAllA(this.info);
			for (Step stp : steps)
			{
				this.children.add(new TreeNode(GameBoard.duplicateAndExecute(this.info,stp),!this.isMax));
				if(depth<MAX_DEPTH)//will changed when i would decide
					children.get(children.size()-1).generateChildren(depth+1);				
			}
		}
		public boolean isLeaf()
		{
			return children.size()==0;
		}
		public boolean isMax()
		{
			return isMax;
		}
		public GameBoard getInfo ()
		{
			return this.info;
		}
		public List<TreeNode> getChildren()
		{
			return this.children;
		}
		
}


