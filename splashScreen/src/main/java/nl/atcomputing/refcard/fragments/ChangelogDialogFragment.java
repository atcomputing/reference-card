package nl.atcomputing.refcard.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nl.atcomputing.refcard.R;

/**
 * Created by martijn on 8-1-15.
 */
public class ChangelogDialogFragment extends DialogFragment {
    private ArrayList<String> changelog = new ArrayList<String>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setInverseBackgroundForced(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_changelog, null);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.changelogentries);
        for(String line : changelog) {
            View v = (View) inflater.inflate(R.layout.changelog_entry, null);
            TextView tv = (TextView) v.findViewById(R.id.bullet);
            tv.setText(Html.fromHtml("&#8226;"));
            tv = (TextView) v.findViewById(R.id.changelogtext);
            tv.setText(line);
            layout.addView(v);
        }

        builder.setView(view)
                .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ChangelogDialogFragment.this.getDialog().dismiss();
                    }
                });

        return builder.create();
    }

    public ArrayList<String> getChangelog() {
        return changelog;
    }

    public void addChangelogEntry(String change) {
        changelog.add(change);
    }
}
