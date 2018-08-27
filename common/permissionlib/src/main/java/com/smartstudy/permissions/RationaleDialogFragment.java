package com.smartstudy.permissions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

/**
 * {@link DialogFragment} to display rationale for permission requests when the request comes from
 * a Fragment or Activity that can host a Fragment.
 */
@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
public class RationaleDialogFragment extends DialogFragment {

    private PermissionUtil.PermissionCallbacks mPermissionCallbacks;
    private AlertDialog mDialog;

    public static RationaleDialogFragment newInstance(@StringRes int positiveButton, @StringRes int negativeButton,
                                                      @NonNull String rationaleMsg, int requestCode, @NonNull String[] permissions) {
        // Create new Fragment
        RationaleDialogFragment dialogFragment = new RationaleDialogFragment();

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

    @SuppressLint("NewApi")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // getParentFragment() requires API 17 or higher
        boolean isAtLeastJellyBeanMR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;

        if (isAtLeastJellyBeanMR1
                && getParentFragment() != null
                && getParentFragment() instanceof PermissionUtil.PermissionCallbacks) {
            mPermissionCallbacks = (PermissionUtil.PermissionCallbacks) getParentFragment();
        } else if (context instanceof PermissionUtil.PermissionCallbacks) {
            mPermissionCallbacks = (PermissionUtil.PermissionCallbacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionCallbacks = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
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
