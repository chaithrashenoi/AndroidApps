package com.chaithra.flappybird;

import java.util.Random;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlappyBirdActivity extends Activity implements OnTouchListener {
	Button flappyBird;
	boolean isInMotion;
	int screenWidth;
	int screenHeight;
	int deltaFBirdY = 10;
	int deltaFBirdYStep = 10;
	int BirdYPos;
	int deltaPipeX = -30;
	int flapAction = 0;
	int pipeStartXPos;
	Random pipeHeightR;
	Random pipeGapR;
	ImageView pipeUp;
	ImageView pipeDown;
	int pipeWidth;
	int scoreValue = 0;
	Button score;
	float pipeXPos = 0;
	int pipeGap;
	int pipeHeightDown;
	int pipeHeightUp;
	ShapeDrawable pipeShapeUp;
	ShapeDrawable pipeShapeDown;
	boolean gameEnd = true;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flappy_bird);
		flappyBird = (Button)this.findViewById(R.id.flappyBird);
		pipeUp = (ImageView)this.findViewById(R.id.pipeUp);
		pipeDown = (ImageView)this.findViewById(R.id.pipeDown);
		score = (Button)this.findViewById(R.id.score);
		Display display = getWindowManager().getDefaultDisplay(); 
		Point screenSize = new Point();
		display.getSize(screenSize);
		screenHeight = screenSize.y;
		screenWidth = screenSize.x;
		pipeStartXPos = screenWidth; 
		View screenView = findViewById(R.id.screenView);
		screenView.setOnTouchListener(this);
		pipeHeightR = new Random();
		pipeGapR = new Random();
		pipeShapeUp = new ShapeDrawable (new RectShape());   
		pipeShapeDown = new ShapeDrawable (new RectShape()); 
	}

	@SuppressLint("NewApi")
	public void startGame(View button){
		Log.d("fp", "StartisInMotion" + isInMotion);
		if( isInMotion == false ) {
			pipeWidth = flappyBird.getWidth();
			pipeGap   = 4*flappyBird.getHeight();
			BirdYPos = screenHeight/2;
			scoreValue = 0;
			gameEnd = false;
			isInMotion = true;
			pipeXPos = pipeStartXPos;
			changePipeSetOnBoundary(0);
			flappyBird.setY(BirdYPos);
			PipeBackgroundMove();
			flappyBirdFly();
		}
	}

	@SuppressLint("NewApi")
	public void pauseGame(View button){
		Log.d("fp", "pauseGameInMotion" + isInMotion);
		if( gameEnd == false ) {
			isInMotion = !isInMotion;
			if(isInMotion) {
				flappyBirdFly();
				PipeBackgroundMove();
			}
		}
	}

	public void stopGame(View button){
		Log.d("fp", "stopInMotion" + isInMotion);
		isInMotion = false;
		gameEnd = true;
	}

	@SuppressLint("NewApi")
	private void PipeBackgroundMove() {
		if( pipeXPos < 0 ) {
			pipeXPos = pipeStartXPos;
			changePipeSetOnBoundary(1);
		}
		pipeXPos = pipeXPos + deltaPipeX;

		drawPipes();
		score.setText(Integer.toString(scoreValue));

		if (isInMotion)
			pipeUp.postDelayed(new PipeMover(), 50);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void drawPipes( ) {
		pipeShapeUp.setIntrinsicHeight(pipeHeightUp);   
		pipeShapeUp.setIntrinsicWidth(pipeWidth);  
		pipeShapeUp.getPaint().setColor(Color.rgb(128, 238, 128));
		pipeUp.setX(pipeXPos);
		pipeUp.setY(0);
		pipeUp.setBackgroundDrawable(pipeShapeUp);

		pipeShapeDown.setIntrinsicHeight(pipeHeightDown);   
		pipeShapeDown.setIntrinsicWidth(pipeWidth);  
		pipeShapeDown.getPaint().setColor(Color.rgb(128, 238, 128));
		pipeDown.setX(pipeXPos);
		pipeDown.setY(pipeHeightUp + pipeGap );
		pipeDown.setBackgroundDrawable(pipeShapeDown);
	}


	@SuppressLint("NewApi")
	private void changePipeSetOnBoundary(int scoreIncr) {
		scoreValue = scoreValue + scoreIncr;
		pipeHeightUp = pipeHeightR.nextInt(screenHeight - pipeGap);
		pipeHeightDown = screenHeight - pipeHeightUp - pipeGap;
	}
	
	public class PipeMover implements Runnable {
		@Override
		public void run() {
			PipeBackgroundMove();
		}
	}
	
	@SuppressLint("NewApi")
	private void flappyBirdFly() {
		if( deltaFBirdY < 0 ) {
			if( flapAction == 0 ) {
				flapAction = 1;
				flappyBird.setBackgroundResource(R.drawable.birdfly1);
			} 
			else if( flapAction == 1 ){
				flapAction = 2;
				flappyBird.setBackgroundResource(R.drawable.birdfly2);
			} 
			else{
				flapAction = 0;
				flappyBird.setBackgroundResource(R.drawable.birdfly3);
			}
		} 
		else {
			flappyBird.setBackgroundResource(R.drawable.birdfly2);
		}

		boolean boundary = CheckBirdBoundary();
		
		boolean collison = false;
		if( boundary == false ) {
			BirdYPos = BirdYPos + deltaFBirdY;
			flappyBird.setY(BirdYPos);
			collison = DetectBirdCollison(pipeUp);
			collison |= DetectBirdCollison(pipeDown);
		}

		Log.d("fp", "boundary:" + boundary + "collison:" + collison);
		if( collison || boundary ) {
			stopGame( flappyBird );
		}

		if (isInMotion)
			flappyBird.postDelayed(new FlappyBirdMover(), 25);
	}
	
	@SuppressLint("NewApi")
	private boolean CheckBirdBoundary() {
		boolean boundary;
		float YendNext = flappyBird.getY() + flappyBird.getHeight() + 
				deltaFBirdY;
		float YStartNext = flappyBird.getY() + deltaFBirdY;

		if((deltaFBirdY < 0 && YStartNext < 0 )||
		   (deltaFBirdY > 0 && YendNext > screenHeight) ) {
			boundary = true;
		}
		else {
			boundary = false;
		}
		return( boundary );
	}

	@SuppressLint("NewApi")
	private boolean DetectBirdCollison(ImageView pipe) {
		boolean collison = true;
		if( pipe.getY() > (flappyBird.getY() + flappyBird.getHeight() - deltaFBirdY) ||
				(pipe.getY() + pipe.getHeight()) < (flappyBird.getY() - deltaFBirdY) ||
				pipe.getX() > (flappyBird.getX() + flappyBird.getWidth() - deltaPipeX ) ||
				(pipe.getX() + pipe.getWidth()) < (flappyBird.getX() - deltaPipeX) ) {
			collison = false;
		}

		return( collison );
	}


	public class FlappyBirdMover implements Runnable {
		@Override
		public void run() {
			flappyBirdFly();
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		switch (actionCode) {
		case MotionEvent.ACTION_DOWN:
			return handleActionDown(event);
		case MotionEvent.ACTION_UP: {
			boolean btouchTrue;
			btouchTrue = handleActionUp(event);
			return (btouchTrue);
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			return false;
		}
		return false;
	}

	private boolean handleActionDown(MotionEvent event) {
		deltaFBirdY = -deltaFBirdYStep;	
		return true;
	}

	private boolean handleActionUp(MotionEvent event) {
		deltaFBirdY = deltaFBirdYStep;
		return true;
	}
}

