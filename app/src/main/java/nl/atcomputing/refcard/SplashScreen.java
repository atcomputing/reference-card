package nl.atcomputing.refcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

public class SplashScreen extends Activity {	
	protected int _splashTime = 2000; 	// in milliseconds
	private Thread splashTread;
    private static final String KEY_APP_VERSION = "key_app_version";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);

        if( newVersion() != 1 ) {

            final SplashScreen sPlashScreen = this;

            // thread for displaying the SplashScreen
            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(_splashTime);
                        }

                    } catch (InterruptedException e) {
                    } finally {
                        startMainActivity();
                    }
                }
            };

            splashTread.start();
        }
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}

    /**
     *
     * @return -1 if this is a fresh installation, 0 if nothing changed, 1 if this is an upgrade
     */
    private int newVersion() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(KEY_APP_VERSION, -1);
            if( lastVersionCode == -1 ) {
                return -1;
            }

            int currentVersionCode = pInfo.versionCode;
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(KEY_APP_VERSION, currentVersionCode).commit();
           if( currentVersionCode > lastVersionCode ) {
                if( handleUpgrade(currentVersionCode, lastVersionCode) ) {
                    return 1;
                } else {
                    return 0;
                }
           }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SplashScreen",
                    "Unable to determine current app version.");
        }
        return 0;
    }

    private boolean handleUpgrade(int currentVersion, int lastVersionCode) {
        StringBuilder changelog = new StringBuilder();
        switch (lastVersionCode) {

        }

        if( changelog.length() > 0 ) {
            AlertDialog.Builder box = new AlertDialog.Builder(this);
            box.setIcon(R.drawable.at);
            box.setTitle("New in Linux Reference Card");
            box.setMessage(changelog.toString());
            box.setCancelable(false);
            box.setNeutralButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startMainActivity();
                }
            });
            box.show();
            return true;
        } else {
            return false;
        }
    }

    private void startMainActivity() {
        finish();
        Intent i = new Intent();
        i.setClass(this, ATComputingrefcardActivity.class);
        startActivity(i);
    }
}