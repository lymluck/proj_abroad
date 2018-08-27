package com.smartstudy.permissions;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * {@link AppCompatDialogFragment} to display rationale for permission requests when the request
 * comes from a Fragment or Activity that can host a Fragment.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class RationaleDialogFragmentCompat extends AppCompatDialogFragment {

    private PermissionUtil.PermissionCallbacks mPermissionCallbacks;

    private AlertDialog mDialog;

    static RationaleDialogFragmentCompat newInstance(
            @StringRes int positiveButton, @StringRes int negativeButton,
            @NonNull String rationaleMsg, int requestCode, @NonNull String[] permissions) {

        // Create new Fragment
        RationaleDialogFragmentCompat dialogFragment = new RationaleDialogFragmentCompat();

        // Initialize configuration as arguments
        RationaleDialogConfig config = new RationaleDialogConfig(
                positiveButton, negativeButton, rationaleMsg, requestCode, permissions);
        if (dialogFragment.getArguments() == null) {
            dialogFragment.setArguments(config.toBundle());
        } else {
            //Consider explicitly clearing arguments here
            dialogFragment.getArguments().putAll(config.toBundle());
        }
        config = null;
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof PermissionUtil.PermissionCallbacks) {
            mPermissionCallbacks = (PermissionUtil.PermissionCallbacks) getParentFragment();
        } else if (context instanceof PermissionUtil.PermissionCallbacks) {
            mPermissionCallbacks = (PermissionUtil.PermissionCallbacks) context;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionCallbacks = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Rationale dialog should not be cancelable
        setCancelable(false);

        // Get config from arguments, create click listener
        RationaleDialogConfig config = new RationaleDialogConfig(getArguments());
        RationaleDialogClickListener clickListener =
                new RationaleDialogClickListener(this, config, mPermissionCallbacks);

        // Create an AlertDialog
        if (mDialog == null) {
            mDialog = config.createDialog(getActivity(), clickListener);
        }
        return mDialog;
    }
}
