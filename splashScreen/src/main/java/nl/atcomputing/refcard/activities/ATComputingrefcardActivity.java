package nl.atcomputing.refcard.activities;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.tabs.SlidingTabFragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ATComputingrefcardActivity extends ActionBarActivity {
	/** Called when the activity is first created. */
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
	}

	/**
	 * Added to fix ClassCastException bug in some android versions
	 * as reported by users
	 */
	@Override
	protected void onRestoreInstanceState(Bundle state) {

	}

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//    }

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
		case R.id.about_atc:
			showAbout(R.string.atcomp, getResources().getString(R.string.explain_atc));
			break;
		case R.id.about_refcard:
			showAbout(R.string.atref, getResources().getString(R.string.explain_ref));
			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
	}

	private void showAbout(int title, CharSequence body) {
		// When clicked, show the description of the flags

		AlertDialog.Builder box = new AlertDialog.Builder(this);
		box.setIcon(R.drawable.at);
		box.setTitle(title);
		box.setMessage(body);
		box.setCancelable(false);
		box.setNeutralButton(R.string.confirm_ok, null);
		box.show();
	}
}
