package com.chaithras.smartalarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SmartAlarmRing extends Activity
{
	MediaPlayer mMediaPlayer;
	boolean mMatched = false;
    int marginLeft1 = 30;
    int marginTop1 = 120;
    int marginLeft2 = 480;
    int marginTop2 = 570;
    int imageViewId[]  = { R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4};
    int imageResId[][]  = {
    		{ R.drawable.house1, R.drawable.house2, R.drawable.house3, R.drawable.house4},
    		{ R.drawable.bty1, R.drawable.bty2, R.drawable.bty3, R.drawable.bty4},
    		{ R.drawable.flr1, R.drawable.flr2, R.drawable.flr3, R.drawable.flr4},
    		{ R.drawable.gr1, R.drawable.gr2, R.drawable.gr3, R.drawable.gr4}
    };
    int imageLeft[]  = { marginLeft1, marginLeft2, marginLeft1, marginLeft2};
    int imageTop[]  = { marginTop1, marginTop1, marginTop2, marginTop2};
    int mTileCount = 4;
	ImageView mImgViewObj[];
	Button stopButton;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_smart_alarm_ring);

       	 	stopButton = (Button)findViewById(R.id.stopAlarm); 
       	 	stopButton.setEnabled(false);

       	 	mImgViewObj = new ImageView[4];
	        
			    
       	 	List<Integer> imgeRandomIndex = new ArrayList<Integer>();
			for(int i = 0; i < mTileCount; i++) {
				imgeRandomIndex.add(i);
			}
			
			for(int i=0; i <20; i++) {
				int random = 0;
				Collections.shuffle(imgeRandomIndex);	        
				for(int j = 0; j < mTileCount; j++) {
					if(j != imgeRandomIndex.get(j) ) {
						random++;
					}
				}
				 Log.d("shuffle", "random");
				
				if( random >= 3) {
					break;
				}
			}

            for(int i = 0; i < mTileCount; i++) {
            	int indexPlaced = imgeRandomIndex.get(i);
	            mImgViewObj[indexPlaced]= (ImageView) findViewById(imageViewId[indexPlaced]);
		        mImgViewObj[indexPlaced].setImageResource(imageResId[imgeRandomIndex.get(0)][i]);
                RelativeLayout.LayoutParams layoutParam = 
                		(RelativeLayout.LayoutParams) mImgViewObj[indexPlaced].getLayoutParams();
                layoutParam.leftMargin 	= imageLeft[indexPlaced];
	            layoutParam.topMargin 	= imageTop[indexPlaced];
	            mImgViewObj[indexPlaced].setTag(i);
	            setupOntouch(mImgViewObj[indexPlaced]);
            }
	        
			 Log.d("alarmdb", "alarm triggerd");
			 try {
				   Uri alert;
				   alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
				   if( alert == null) {
					   alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				   }
				   if( alert == null) {
					   alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
				   }
				  mMediaPlayer = new MediaPlayer();
				  mMediaPlayer.setDataSource(this, alert);
				  final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				 if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
				 mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
				 mMediaPlayer.setLooping(true);
				 mMediaPlayer.prepare();
				 Log.d("alarmdb", "mMediaPlayer start");
				 mMediaPlayer.start();
				 }
			} catch(Exception e) {
		}   	 
	 }
	 
	 
	 private void setupOntouch(final ImageView img) {
		 img.setOnTouchListener(new OnTouchListener() {
	
	         public boolean onTouch(View v, MotionEvent event) {
	             // TODO Auto-generated method stub
	             int eid = event.getAction();
	             switch (eid) {
	             case MotionEvent.ACTION_MOVE: {
	                 RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) img.getLayoutParams();
	                 int x = (int) event.getRawX();
	                 int y = (int) event.getRawY();
	                 layoutParam.leftMargin = x-3*layoutParam.width/4;
	                 layoutParam.topMargin = y-3*layoutParam.height/4;
	                 img.setLayoutParams(layoutParam);
	             }
	                 break;
	     		case MotionEvent.ACTION_UP: {
	                 RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) img.getLayoutParams();
	     			int curDst = 0;
	                 if(layoutParam.leftMargin > (marginLeft1+marginLeft2)/2) {
	                	 curDst += 1; 
	     			}
	     	
	     			if(layoutParam.topMargin > (marginTop1 + marginTop2)/2) {
	     				curDst += 2; 
	     			}
	                 Log.d("alarm", "moving img to:" + curDst );
	     			
	     			// Swap images 
	                 
	                 int curSrc = 0;
	                 for(int i = 0; i < mTileCount; i++) {
	                	 if(mImgViewObj[i].getTag() == img.getTag()) {
	                		 curSrc = i;
	    	                 Log.d("alarm", "moving img num:" + curSrc );
	                	 }
	                 }
	                 Log.d("alarm", "movingsrcdst:" + curSrc+curDst );

	                 mImgViewObj[curSrc] = mImgViewObj[curDst]; 
	                 
	     			if(curDst < mTileCount ) {
		                 RelativeLayout.LayoutParams layoutParamDest = 
		                		 (RelativeLayout.LayoutParams) mImgViewObj[curDst].getLayoutParams();
		                 layoutParamDest.leftMargin = imageLeft[curSrc];
		                 layoutParamDest.topMargin 	= imageTop[curSrc];
		                 mImgViewObj[curDst].setLayoutParams(layoutParamDest);
		                 mImgViewObj[curDst] = img;
	     			}
	     			
	                 layoutParam.leftMargin 	= imageLeft[curDst];
	                 layoutParam.topMargin 		= imageTop[curDst];
	                 img.setLayoutParams(layoutParam);
	                 Log.d("alarm", "moving from top to new top:" + imageTop[curSrc]+imageTop[curDst] );
	                 	
	                 /* Verify if order matches actual order */
	                 boolean Matched = true;
	                 for(int i = 0; i < mTileCount; i++) {
	                	 if((Integer)mImgViewObj[i].getTag() != i) {
	                		 Matched = false;	                	 
	                	 }
	                 }
	                 mMatched = Matched;	
	                 Log.d("alarm", "Matched=" + mMatched );
	                 
	                 if(mMatched == true) {
	                	 stopButton.setEnabled(true);
	                 }
	     		}
	             default:
	                 break;
	             }
	             return true;
	         }
	     });
	}
	 
	public void StopAlarm(View button) {
	 Log.d("alarmdb", "StopAlarm");
		 mMediaPlayer.stop();
	 finish();
	}
}
