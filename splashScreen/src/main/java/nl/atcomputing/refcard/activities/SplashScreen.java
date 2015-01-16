package nl.atcomputing.refcard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import nl.atcomputing.refcard.R;

public class SplashScreen extends FragmentActivity {
    private static final String KEY_APP_FRESHINSTALL = "key_app_freshinstall";

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splashscreen);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	startMainActivity();
	    }
	    return true;
	}

    private void startMainActivity() {
        finish();
        Intent i = new Intent();
        i.setClass(this, ATComputingrefcardActivity.class);
        startActivity(i);
    }
}