package nl.atcomputing.refcard.activities;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.fragments.AboutDialogFragment;
import nl.atcomputing.refcard.fragments.ChangelogDialogFragment;
import nl.atcomputing.refcard.tabs.SlidingTabFragment;

public class ATComputingrefcardActivity extends ActionBarActivity {
    private static final String KEY_APP_VERSION = "key_app_version";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fragments are saved by the FragmentManager during rotation. So we do not need
        //to recreated it when the device rotates
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabFragment fragment = new SlidingTabFragment();
            transaction.replace(R.id.fragment, fragment);
            transaction.commit();
        }

        newVersion();
    }

    /**
     * Added to fix ClassCastException bug in some android versions
     * as reported by users
     */
    @Override
    protected void onRestoreInstanceState(Bundle state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // determine what happens when one of the menu button is pressed
        switch (item.getItemId()) {
            case R.id.about:
                AboutDialogFragment fragment = new AboutDialogFragment();
                fragment.show(getSupportFragmentManager(), null);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void newVersion() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersionCode = pInfo.versionCode;

           // Update version in preferences
            sharedPreferences.edit()
                    .putInt(KEY_APP_VERSION, currentVersionCode).commit();

            int lastVersionCode = sharedPreferences
                    .getInt(KEY_APP_VERSION, -1);
            if( lastVersionCode == -1 ) {
                return;
            }

            if( currentVersionCode > lastVersionCode ) {
                handleUpgrade(lastVersionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SplashScreen",
                    "Unable to determine current app version.");
        }
    }

    private void handleUpgrade(int lastVersionCode) {
        ChangelogDialogFragment fragment = new ChangelogDialogFragment();
        switch (lastVersionCode) {
            case 9:
                fragment.addChangelogEntry("Command descriptions for sudo, systemctl, and xz\n");
            case 10:
                fragment.addChangelogEntry("New layout conform material design guidelines\n");
        }

        if( fragment.getChangelog().size() > 0 ) {
            fragment.show(getSupportFragmentManager(), null);
        }
    }
}
