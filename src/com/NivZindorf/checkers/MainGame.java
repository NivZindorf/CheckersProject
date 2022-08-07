package com.NivZindorf.checkers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGame extends Activity implements OnClickListener{
	private final String EXTRA_GAME_TYPE = "com.NivZindorf.checkers.Type";
	private final String EXTRA_DEPTH = "com.NivZindorf.checkers.Depth";
	static final String STATE_BOARD= "gameMainBoard";
	static final String STATE_TYPE="gameType";
	private final String EXTRA_NEW="com.NivZindorf.checkers.New";
	private  ImageView [] [] IMAGE_BOARD;
	private boolean isPath;
	private Step [] steps;
	private Index[] froms;
	private Index [] pathHover;
	private Step [] currStep;
	private GameBoard mainBoard ;
	private  boolean isCPU;
	private Index pressedFrom;
	private Step stepToExecute;
	private TextView textB;
	private TextView textA;
	private boolean turn;//true=A false=B
	private int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_game);
		textA=(TextView) findViewById(R.id.aTurnTV);
		textB=(TextView) findViewById(R.id.bTurnTV);
		if(setUp(getIntent()))
		{
			if(savedInstanceState==null)
			{
			SharedPreferences setting = getPreferences(0);
			mainBoard= new GameBoard(GameBoard.stringToBoard(setting.getString("matrix", "")));
			turn = setting.getBoolean("turn", true);
			level = setting.getInt("level", 0);
			isCPU=setting.getBoolean("cpu", false);
			}
			else
			{
				int [] [] matrix = new int [8][8];
				for (int i=0;i<8;i++)
					matrix[i]=savedInstanceState.getIntArray("matrix"+i);
				mainBoard=new GameBoard(matrix);
				level=savedInstanceState.getInt("level", 0);
				isCPU= level==0;
				turn=savedInstanceState.getBoolean("turn",true);
			}
		}
		
		IMAGE_BOARD = setImageBoard();
		cleanAllHover(mainBoard);
		if (turn)
		{
		textB.setVisibility(View.GONE);
		hoverFroms(CheckStep.checkAllA(mainBoard));
		}
		else
		{
			textA.setVisibility(View.GONE);
			hoverFroms(CheckStep.checkAllB(mainBoard));
		}
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		int [] [] matrix = mainBoard.getBoard();
		for (int i =0;i<8;i++)
			savedInstanceState.putIntArray("matrix"+i, matrix[i]);
		savedInstanceState.putInt("level", level);//0 = two player
		if(level==0)
			savedInstanceState.putBoolean("turn",turn);
		super.onSaveInstanceState(savedInstanceState);
		
	}
	@Override
	public void onStop()
	{
		super.onStop();
		SharedPreferences setting = getPreferences(0);
		SharedPreferences.Editor editor=setting.edit();
		editor.putString("matrix", mainBoard.toString());
		editor.putBoolean("cpu", isCPU);
		editor.putInt("level", level);
		editor.putBoolean("turn", turn);
		editor.commit();
	}
	public boolean setUp(Intent intent)//return false if  new game has been started and true otherwise.
	{
		if(!intent.getBooleanExtra(EXTRA_NEW, true)||intent==null)
		{
			return true;
		}
		else
		{
		mainBoard=new GameBoard();
		if (intent.getBooleanExtra(EXTRA_GAME_TYPE, false))
		{
			level=intent.getIntExtra(EXTRA_DEPTH, 5);
			AI.setMaxDepth(level);
			isCPU = true;
			
		}
		else
			isCPU=false;
		turn=true;
		return false;
		}
	}
	public void cpuTurn()
	{
		new DrawAndAITask().execute(mainBoard);
	}
	public void aTurn ()
	{
		turn=true;
		textA.setText(R.string.who_turn);
		textB.setVisibility(View.GONE);
		cleanAllListener();
		if (mainBoard.isEnd())
		{
			//do something to stop
		}
		Step [] stp = CheckStep.checkAllA(mainBoard);
		hoverFroms(stp);
		textA.setVisibility(View.VISIBLE);
	}
	public void bTurn()
	{
		turn=false;
		textA.setVisibility(View.GONE);
		cleanAllHover(mainBoard);
		cleanAllListener();
		if (mainBoard.isEnd())
		{
			//do something to stop
		}
		hoverFroms(CheckStep.checkAllB(mainBoard));
		textB.setVisibility(View.VISIBLE);
	}
	public boolean makeNextPath (Index ind)
	{
		int currStepPos=0;
		Step [] newCurrStep = new Step [currStep.length];
		int newCurrStepPos =0;
		int pathHoverPos=0;
		boolean found = false;
		int pathPos=0;
		pathHover = new Index [pathHover.length];
		while(currStepPos<currStep.length && currStep[currStepPos]!=null)
		{
			if(currStep[currStepPos].getPath()!=null)
			{
				pathPos=0;
				found=false;
				while (currStep[currStepPos].getPath()[pathPos]!=null &&
						pathPos<currStep[currStepPos].getPath().length && !found)
				{
					if(currStep[currStepPos].getPath()[pathPos].compare(ind))
					{
						found=true;
						newCurrStep[newCurrStepPos]=currStep[currStepPos];
						for (int i = 0; i<=pathPos ; i++)
							getImageView(currStep[currStepPos].getKill()[i]).setImageResource(R.drawable.empty) ;
						getImageView(currStep[currStepPos].getFrom()).setImageResource(R.drawable.empty) ;
						pathPos++;	
						while (currStep[currStepPos].getPath()[pathPos]!=null && pathPos<currStep[currStepPos].getPath().length )
						{
							pathHover[pathHoverPos]= currStep[currStepPos].getPath()[pathPos];
							pathHoverPos++;
							pathPos++;
						}
						pathHover[pathHoverPos]=currStep[currStepPos].getTo();
						pathHoverPos++;
					}
					pathPos++;
				}
			}
			if (currStep[currStepPos].getTo().compare(ind))
			{
				stepToExecute=currStep[currStepPos];
				return true;
			}
			currStepPos++;
		}
		currStep=newCurrStep;
		return false;	
	}
	public boolean makeNextPath1(Index ind)
	{
		boolean found= false;
		int i=0;//מיקום currStep
		int j =0; // מיקום path
		int t=0;
		Index [] pos = new Index [currStep.length]; //מסמל מיקום שנמצא i=x , j=y
		while (i<currStep.length&& currStep[i]!=null && !found)
		{
			if (currStep[i].getTo().compare(ind))
			{
				found=true;
				j=-1;
			}
			while (j<currStep[i].getPath().length &&currStep[i].getPath()[j]!=null && found==false)
			{
				if(!currStep[i].getPath()[j].compare(ind))
					j++;
				else
					found=true;
			}
			
			if(found)
			{
				pos[t]=new Index (i,j);
				t++;
				found = false;
			}
				i++;
				j=0;
		}
		if (pos[0]==null)
		{
			i=0;
			while (currStep[i]!=null&&i<currStep.length)
			{
				if (currStep[i].getTo().compare(ind))
				{
					Step [] newCurrStep = new Step[1];
					newCurrStep[0]=currStep[i];
					currStep = newCurrStep;
					return true;
				}
				i++;
			}
			return true;
		}
		else
		{
		i=0;j=0;
		Step [] newCurrStep = new Step[t];
		pathHover = new Index [5];
		if(t==1)
		{
			int k= pos[0].getY()+1;
			newCurrStep[0]= currStep[pos[0].getX()];
			
			while (newCurrStep[0].getPath()[k]!=null)
			{
			pathHover[i]=newCurrStep[0].getPath()[k];
			i++;
			k++;
			}
		}
		while (j<t)
		{
			int k= pos[j].getY()+1;
			newCurrStep[j]= currStep[pos[j].getX()];
			while (newCurrStep[j].getPath()[k]!=null)
			{
			pathHover[i]=newCurrStep[0].getPath()[k];
			i++;
			k++;
			}
		}
		currStep = newCurrStep;
		return false;
		}
		
	}
	public void getPathHover (Index ind)
	{
		
		boolean found=false;
		currStep= new Step[steps.length];
		ArrayList<Index> pathHoverList = new ArrayList<Index>();
		int j=-1; // מיקום currStep וגודלו
		
		for (int r=0 ; r<steps.length;r++)
			if (ind.compare(steps[r].getFrom()))
			{
				j++;
				currStep[j]=steps[r];
				
			}
		
		int i=1;// מיקום currstep
		int t=0;// מיקום path
		int k=0;//מיקום pathover אחרון
		int l=0 ; //מיקום pathover ראשון
		if (currStep[0].getKill()==null)
		{
			pathHoverList.add(currStep[0].getTo());
			if (j>0)
			{
				while ( i<currStep.length && currStep[i]!=null)
				{
					pathHoverList.add(currStep[i].getTo());
					i++;
				}
					
			}
		}
		else
		{
		if(currStep[0].getPath()!=null)
		while (currStep[0].getPath()[k]!=null)
		{
			pathHoverList.add(currStep[0].getPath()[k]);
			k++;
		}
		pathHoverList.add(currStep[0].getTo());
		if (j>0)
		{
			while ( i<currStep.length && currStep[i]!=null)
			{
				if (currStep[i].getPath()!=null)
				while (currStep[i].getPath()[t] != null)
				{
					found= false;
					while (l<=k && !found)
					{
						found = (currStep[i].getPath()[t]).compare(pathHoverList.get(l));
						l++;
					}
					if (!found)
					{
						k++;
						pathHoverList.add(currStep[i].getPath()[t]);
					}
					t++;
				}
				k++;
				pathHoverList.add(currStep[i].getTo());
				i++;
				
			}
				
		}
		}
		pathHover=pathHoverList.toArray(new Index[pathHoverList.size()]);
	}
	public void cleanAllHover(GameBoard board)
	{
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				switch (board.getType(i, j)) 
				{
				case Type.A:IMAGE_BOARD[i][j].setImageResource(R.drawable.a_step) ;
						break;
				case Type.SuperA: IMAGE_BOARD[i][j].setImageResource(R.drawable.supera_step) ;
				break;
				case Type.B:IMAGE_BOARD[i][j].setImageResource(R.drawable.b_step) ;
				break;
				case Type.SuperB:IMAGE_BOARD[i][j].setImageResource(R.drawable.superb_step) ;
				break;
				case Type.Empty:IMAGE_BOARD[i][j].setImageResource(R.drawable.empty) ;
				break;
				case Type.UnPlayed:IMAGE_BOARD[i][j].setImageResource(R.drawable.non_play) ;
				break;
				
				}
			}
	
	}
	public void cleanStepHover(Step stp)
	{
		getImageView(stp.getFrom()).setImageResource(R.drawable.empty);
		if(stp.getI()>0)
		for(Index ind:stp.getKill())
		{
			if(ind==null)
				break;
			getImageView(ind).setImageResource(R.drawable.empty);
		}
		switch(stp.getType())
		{
		case Type.A:getImageView(stp.getTo()).setImageResource(R.drawable.a_step) ;
		break;
		case Type.SuperA: getImageView(stp.getTo()).setImageResource(R.drawable.supera_step) ;
		break;
		case Type.B:getImageView(stp.getTo()).setImageResource(R.drawable.b_step) ;
		break;
		case Type.SuperB:getImageView(stp.getTo()).setImageResource(R.drawable.superb_step) ;
		break;
		}
		
		
	}
	
	public void cleanAllListener ()
	{
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				getImageView(new Index (i,j)).setOnClickListener(null);
				
	}
	public void setHover(Index [] indexes)
	{
		int i=0;
		while (i<indexes.length && indexes[i]!=null )
		{
			getImageView(indexes[i]).setImageResource(R.drawable.hover);
			i++;
		}
	}
	public void cleanListener(Index [] indexes)
	{
		for (int i=0; i<indexes.length ; i++)
		{
			if (indexes[i]==null)
				break;
			getImageView(indexes[i]).setOnClickListener(null);
		}
	}
	public void setListener(Index [] indexes ,boolean isPath)
	{
		this.isPath=isPath;
		int i=0;
		while (i<indexes.length&& indexes[i]!=null)
		{
			getImageView(indexes[i]).setOnClickListener(this);
			i++;
		}
			
	}
	public  void hoverFroms(Step [] stp)
	{
		steps= stp;
		if (stp.length==1)
		{
			getImageView(stp[0].getTo()).setImageResource(R.drawable.hover);
			getImageView(stp[0].getTo()).setOnClickListener(this);
			froms=new Index[1];
			froms[0]=stp[0].getFrom();
			currStep = stp;
			return;
		}
		froms= new Index [steps.length];
		int i=0;
		while(i<steps.length && steps[i]!=null)
		{
			froms[i]=steps[i].getFrom();
				i++;
		}
		
		setListener(froms, false);
		
	}
	public  void onClick(View v )
	{
		isPath=true;
		Index ind = viewToIndex(v);
		int i=0;
		while ( i<froms.length  && froms[i]!=null && isPath)
		{
				isPath= !(ind.compare(froms[i]));
				i++;
		}
		
		
		if (isPath)
		{
			cleanListener(froms);
			cleanListener(pathHover);
			
			if (makeNextPath(ind))
			{
				
				mainBoard.executeStep(stepToExecute);
				
				if(!isCPU)
				{
				cleanAllHover(mainBoard);
				switch (stepToExecute.getType())
				{
				case Type.A:
				case Type.SuperA: bTurn();
								break;
				default : aTurn();
				}
				}
				else
					cpuTurn();
			}
			else
			{
				cleanAllHover(mainBoard);
				cleanAllListener();
				switch (mainBoard.getType(pressedFrom))
				{
				case Type.A: getImageView(ind).setImageResource(R.drawable.a_step) ;
				break;
				case Type.SuperA : getImageView(ind).setImageResource(R.drawable.supera_step) ;
				break;
				case Type.B:getImageView(ind).setImageResource(R.drawable.b_step) ;
				break;
				case Type.SuperB : getImageView(ind).setImageResource(R.drawable.superb_step) ;
				break;
				default: 
					break;
				}
				setHover(pathHover);
				
				setListener(pathHover, true);
			}
			
			
		}
		else
		{
			cleanAllHover(mainBoard);
			getPathHover(ind);
			setHover(pathHover);
			setListener(pathHover, true);
			pressedFrom= ind;
			
			
			
		}
			
	}
	public ImageView [] [] setImageBoard()
	{
		ImageView [] [] imageBoard = new ImageView [8][8];
		for (int i=0; i<8; i++)
		{
			for (int j=0;j<8;j++)
			{
				imageBoard[i][j]=(ImageView)findViewById(new Index(i,j).toId(getApplicationContext()));
			}
		}
		return imageBoard;
	}
	public Index viewToIndex(View v)
	{
		String str = getResources().getResourceEntryName(v.getId());
		int x = Character.getNumericValue(str.charAt(1));
		int y = Character.getNumericValue(str.charAt(2));
		return new Index(x,y);
	}
	public ImageView getImageView(Index ind)
	{
		int x=ind.getX();
		int y=ind.getY();
		return IMAGE_BOARD[x][y];
	}
	
		private class DrawAndAITask extends AsyncTask<GameBoard, Integer , Long>
		{

			@Override
			protected Long  doInBackground(GameBoard... arg0) {
				long tStart=System.nanoTime();
				Step ret=AI.getBestStep(mainBoard);	
				stepToExecute=ret;
				return tStart;
			}
			@Override
			protected void onPreExecute() {
		         cleanAllHover(mainBoard);
		         textA.setText(R.string.computer_turn);
		     }
			@Override
			 protected void onPostExecute(Long result) 
			{
		         mainBoard.executeStep(stepToExecute);
				 long tEnd=System.nanoTime();
				 long time;
				 if (tEnd-result<1000000000)
					  time=(1000000000-(tEnd-result))/1000000;
				 else time=0;
				 try {
	                    new Handler().postDelayed(new Runnable() {
	                          @Override
	                          public void run() {
	                        	  cleanStepHover(stepToExecute);
	     				         textA.setText(R.string.who_turn);
	     				        aTurn();
	                          }
	                      }, time);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
		         
		     }
			
			
		}
	}





