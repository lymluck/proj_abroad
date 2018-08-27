package com.smartstudy.commonlib.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.utils.ScreenUtils;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/3/1
 * @describe 选项对话框
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OptionsPopupDialog extends AlertDialog {
    private Context mContext;
    private ListView mListView;
    private List<String> arrays;
    private OptionsPopupDialog.OnOptionsItemClickedListener mItemClickedListener;

    public static OptionsPopupDialog newInstance(Context context, List<String> arrays) {
        OptionsPopupDialog optionsPopupDialog = new OptionsPopupDialog(context, arrays);
        return optionsPopupDialog;
    }

    public OptionsPopupDialog(Context context, List<String> arrays) {
        super(context, R.style.appBasicDialog);
        this.mContext = context;
        this.arrays = arrays;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_dialog_popup_options, (ViewGroup) null);
        this.mListView = view.findViewById(R.id.list_dialog_popup_options);
        ArrayAdapter<String> adapter = new ArrayAdapter(this.mContext, R.layout.image_dialog_popup_options_item, R.id.dialog_popup_item_name, this.arrays);
        this.mListView.setAdapter(adapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (OptionsPopupDialog.this.mItemClickedListener != null) {
                    OptionsPopupDialog.this.mItemClickedListener.onOptionsItemClicked(position);
                    OptionsPopupDialog.this.dismiss();
                }

            }
        });
        this.setContentView(view);
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = this.getPopupWidth();
        layoutParams.height = -2;
        this.getWindow().setAttributes(layoutParams);
    }

    private int getPopupWidth() {
        int distanceToBorder = (int) this.mContext.getResources().getDimension(R.dimen.popup_dialog_distance_to_edge);
        return ScreenUtils.getScreenWidth() - 2 * distanceToBorder;
    }

    public OptionsPopupDialog setOptionsPopupDialogListener(OptionsPopupDialog.OnOptionsItemClickedListener itemListener) {
        this.mItemClickedListener = itemListener;
        return this;
    }

    public void addItem(String itme) {
        this.arrays.add(itme);
    }

    public void refreshItem() {
        ((ArrayAdapter) this.mListView.getAdapter()).notifyDataSetChanged();
    }

    public interface OnOptionsItemClickedListener {
        void onOptionsItemClicked(int var1);
    }
}
