package com.everyme.LoveGraffiti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener{
	private ImageButton startGame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	    
		startGame = (ImageButton) findViewById(R.id.buttonPlay);
		startGame.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonPlay: {
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			break;
		}
		}
	}
}