package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.fragment.QaListFragment;


public class QaListActivity extends BaseActivity {

    private TextView topRightMenu;
    private ImageView topLeftBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_list);
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        topRightMenu = findViewById(R.id.topdefault_rightmenu);
        topRightMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_blue, 0, 0, 0);
        topRightMenu.setVisibility(View.VISIBLE);
        topLeftBtn = findViewById(R.id.topdefault_leftbutton);
        Bundle data = getIntent().getExtras();
        TextView topTitle = findViewById(R.id.topdefault_centertitle);
        String flag = data.getString("data_flag");
        if ("list".equals(flag)) {
            topTitle.setText(getString(R.string.qa_list));
        } else if ("school".equals(flag)) {
            topTitle.setText(getString(R.string.school_qa));
        } else if ("teacher".equals(flag)) {
            String title = data.getString("nickName");
            topTitle.setText(title + "的回答");
            topRightMenu.setVisibility(View.GONE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        QaListFragment qaListFragment = QaListFragment.getInstance(data);
        qaListFragment.setUserVisibleHint(true);
        transaction.replace(R.id.flyt_qa, qaListFragment);
        transaction.commit();
    }

    @Override
    public void initEvent() {
        topRightMenu.setOnClickListener(this);
        topLeftBtn.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.topdefault_rightmenu:
                UApp.actionEvent(this, "19_A_post_btn");
                startActivity(new Intent(this, AddQuestionActivity.class));
                break;
            default:
                break;
        }
    }
}
