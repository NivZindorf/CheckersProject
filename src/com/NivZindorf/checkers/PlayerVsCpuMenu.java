package com.NivZindorf.checkers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlayerVsCpuMenu extends Activity {
	private static final String EXTRA_DEPTH = "com.NivZindorf.checkers.Depth";
	private static final String EXTRA_GAME_TYPE = "com.NivZindorf.checkers.Type";
	private static final String EXTRA_NEW="com.NivZindorf.checkers.New";
	private int level;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_vs_cpu_menu);
		intent = new Intent(this , MainGame.class);
		level=5;
		
		
	}
	public void onRadioButtonClicked(View view)
	{
		switch (view.getId())
		{
		case R.id.easyRB : level=3;
		break;
		case R.id.normalRB : level=5;
		break;
		case R.id.hardRB : level=6;
		break;
		}
	}
	public void onButtonClick (View view)
	{
		intent.putExtra(EXTRA_GAME_TYPE, true);
		intent.putExtra(EXTRA_DEPTH, level);
		intent.putExtra(EXTRA_NEW, true);
		startActivity(intent);
	}
	
	
}
