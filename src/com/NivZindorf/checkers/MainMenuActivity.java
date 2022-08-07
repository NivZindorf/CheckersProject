package com.NivZindorf.checkers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {
	private final String EXTRA_GAME_TYPE = "com.NivZindorf.checkers.Type";
	private final String EXTRA_New = "com.NivZindorf.checkers.New";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
	}
	public void startTwoPlayersGame (View view)
	{
		Intent intent = new Intent (this , MainGame.class);
		intent.putExtra(EXTRA_GAME_TYPE, false);
		intent.putExtra(EXTRA_New, true);
		startActivity(intent);
	}
	public void onePlayer(View view)
	{
		Intent intent = new Intent (this , PlayerVsCpuMenu.class);
		startActivity(intent);
	}
	public void resume(View view)
	{ 
		Intent intent = new Intent(this , MainGame.class);
		intent.putExtra(EXTRA_New, false);
		startActivity(intent);
	}
}
