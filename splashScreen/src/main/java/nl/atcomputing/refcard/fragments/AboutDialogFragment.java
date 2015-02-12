package nl.atcomputing.refcard.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import nl.atcomputing.refcard.R;

/**
 * Created by martijn on 8-1-15.
 */
public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setInverseBackgroundForced(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null);

        String version;
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("nl.atcomputing.refcard", 0);
            version = info.versionName + "-" +info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            version = getString(R.string.unknown);
        }
        TextView tv = (TextView) view.findViewById(R.id.about_version_number);
        tv.setText(version);

        builder.setView(view)
                .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AboutDialogFragment.this.getDialog().dismiss();
                    }
                });

        return builder.create();
    }
}
