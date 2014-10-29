package nl.atcomputing.refcard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

public class ATComputingrefcardActivity extends ActionBarActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        
//        // prepare TABs view
//        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
//        TabHost.TabSpec spec;  				// Reusable TabSpec for each tab
//        Intent intent; 						// Reusable Intent for each tab
//
//        // Create text labels for tabs
//		TextView tab1 = (TextView) getLayoutInflater().inflate(R.layout.tab_item, null);
//		TextView tab2 = (TextView) getLayoutInflater().inflate(R.layout.tab_item, null);
//		TextView tab3 = (TextView) getLayoutInflater().inflate(R.layout.tab_item, null);
//		
//        // Create intent, initialize a TabSpec for the Command Tab and 
//		// add it to the TabHost
//        intent = new Intent().setClass(this, CommandActivity.class);
//		tab1.setText(getResources().getString(R.string.label_cmdref));
//        spec = tabHost.newTabSpec("commands").setIndicator(tab1).setContent(intent);
//        tabHost.addTab(spec);
//
//        // Do the same for the Vi Tab
//        intent = new Intent().setClass(this, ViActivity.class);
//		tab2.setText(getResources().getString(R.string.label_viref));
//        spec = tabHost.newTabSpec("vi").setIndicator(tab2).setContent(intent);
//        tabHost.addTab(spec);
//
//        // Do the same for the RegExp Tab
//        intent = new Intent().setClass(this, RegExpActivity.class);
//        tab3.setText(getResources().getString(R.string.label_regexp));
//        spec = tabHost.newTabSpec("regexp").setIndicator(tab3).setContent(intent);
//        tabHost.addTab(spec);
//
//        // define the first Tab (command list) as the default
//        tabHost.setCurrentTab(0);
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
