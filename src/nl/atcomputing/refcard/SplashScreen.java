package nl.atcomputing.refcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity implements AnimationListener {	
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);		

		ImageView imageView= (ImageView)findViewById(R.id.logo);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		animation.setAnimationListener(this);
		imageView.startAnimation(animation);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		Intent intent = new Intent(SplashScreen.this, ATComputingrefcardActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}