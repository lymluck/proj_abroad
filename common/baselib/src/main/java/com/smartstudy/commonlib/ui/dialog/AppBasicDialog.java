package com.smartstudy.commonlib.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;

public class AppBasicDialog extends Dialog {

    public AppBasicDialog(Context context, int theme) {
        super(context, theme);
    }

    public AppBasicDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;

        private View contentView;

        private View layout = null;

        private String title;

        private String message;

        private String positiveButtonText;

        private String negativeButtonText;

        private Boolean isInput;

        private String editValue;

        private String hint;

        private OnClickListener positiveButtonClickListener;

        private OnClickListener negativeButtonClickListener;

        public Builder(Context context, Boolean isInput) {
            this.context = context;
            this.isInput = isInput;
        }

        public String getEditValue() {
            return editValue;
        }

        public Builder setEditValue(String value) {
            this.editValue = value;
            return this;

        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public AppBasicDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
            if (inflater != null) {
                layout = inflater.inflate(R.layout.app_basicdialog, null);
                dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                // set the dialog title
                ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
                ((EditText) layout.findViewById(R.id.dialog_edit)).setText(editValue);
                ((EditText) layout.findViewById(R.id.dialog_edit)).setHint(hint);
                if (!isInput) {
                    layout.findViewById(R.id.dialog_edit).setVisibility(View.GONE);
                } else {
                    layout.findViewById(R.id.cont_layout).setPadding(20, 0, 20, 20);
                }

                // set the confirm button
                if (positiveButtonText != null) {
                    ((Button) layout.findViewById(R.id.positive_btn)).setText(positiveButtonText);
                    if (positiveButtonClickListener != null) {
                        layout.findViewById(R.id.positive_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isInput) {
                                    editValue = ((EditText) layout.findViewById(R.id.dialog_edit)).getText().toString();
                                }
                                positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            }
                        });
                    }
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.positive_btn).setVisibility(View.GONE);
                }
                // set the cancel button
                if (negativeButtonText != null) {
                    ((Button) layout.findViewById(R.id.negative_btn)).setText(negativeButtonText);
                    if (negativeButtonClickListener != null) {
                        layout.findViewById(R.id.negative_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
                    }
                } else {
                    // if no confirm button just set the visibility to GONE
                    layout.findViewById(R.id.negative_btn).setVisibility(View.GONE);
                }
                // set the content message
                if (message != null) {
                    ((TextView) layout.findViewById(R.id.dialog_info)).setText(message);
                } else if (contentView != null) {
                    // if no message set
                    // add the contentView to the dialog body
                    ((LinearLayout) layout.findViewById(R.id.cont_layout)).removeAllViews();
                    ((LinearLayout) layout.findViewById(R.id.cont_layout)).addView(contentView, new LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }
                dialog.setContentView(layout);
            }
            return dialog;
        }
    }
}
