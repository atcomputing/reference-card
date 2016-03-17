/**
 * 
 * Copyright 2012 AT Computing BV
 *
 * This file is part of Linux Reference Card.
 *
 * Linux Reference Card is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Linux Reference Card is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Linux Reference Card.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        /**
        * Added to fix ClassCastException bug in some android versions
        * as reported by users
        */
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

            int lastVersionCode = sharedPreferences
                    .getInt(KEY_APP_VERSION, -1);

            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(KEY_APP_VERSION, currentVersionCode).commit();

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
            case 12:
                fragment.addChangelogEntry("Improved tablet support\n");
            case 13:
            case 14:
                fragment.addChangelogEntry("Removed command descriptions for calendar, compress, dc, egrep, expand, false, fgrep, ftp, newgrp, slogin, true, uncompress, unexpand, wait\n");
                fragment.addChangelogEntry("Added command descriptions for alias, cupsdisable, cupsenable, paste, unxz, which, zless, zmore\n");
            default:
        }

        if( fragment.getChangelog().size() > 0 ) {
            fragment.show(getSupportFragmentManager(), null);
        }
    }
}
