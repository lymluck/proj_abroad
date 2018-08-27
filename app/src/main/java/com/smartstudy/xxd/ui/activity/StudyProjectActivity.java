package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.OptionsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/12/6.
 */

public class StudyProjectActivity extends UIActivity {
    private RecyclerView rvProject;
    private NoScrollLinearLayoutManager mLayoutManager;
    private CommonAdapter<OptionsInfo> mAdapter;
    private String type;

    private String data;
    private List<OptionsInfo> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_study_project);
    }


    @Override
    protected void initViewAndData() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            data = getIntent().getStringExtra("data");
        }
        if (datas != null) {
            datas.clear();
        }
        datas = getDatas(type);

        rvProject = findViewById(R.id.rv_project);

        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);

        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);

        if ("project".equals(type)) {
            titleView.setText("留学项目");
        } else {
            titleView.setText("当前就读年级");
        }


        findViewById(R.id.top_line).setVisibility(View.VISIBLE);

        rvProject.setHasFixedSize(true);

        mLayoutManager = new NoScrollLinearLayoutManager(this);

        mLayoutManager.setScrollEnabled(true);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvProject.setLayoutManager(mLayoutManager);

        initAdapter();

        rvProject.setAdapter(mAdapter);

    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.putExtra("back", datas.get(position));
                setResult(88, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }

    }


    private void initAdapter() {
        mAdapter = new CommonAdapter<OptionsInfo>(this, R.layout.item_study_project, datas) {

            @Override
            protected void convert(ViewHolder holder, OptionsInfo optionsInfo, int position) {
                holder.setText(R.id.tv_project, optionsInfo.getName());
                if (!datas.get(position).getName().equals(data)) {
                    holder.getView(R.id.iv_select).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.iv_select).setVisibility(View.VISIBLE);
                }

                if (position == getItemCount() - 1) {
                    holder.getView(R.id.v_line).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.v_line).setVisibility(View.VISIBLE);
                }

            }
        };
    }


    private List<OptionsInfo> getDatas(String type) {
        List<OptionsInfo> datas = new ArrayList<OptionsInfo>();
        if ("project".equals(type)) {
            datas.add(new OptionsInfo("DEGREE_1", "本科", ""));
            datas.add(new OptionsInfo("DEGREE_2", "研究生", ""));
        } else if ("本科".equals(type)) {
            datas.add(new OptionsInfo("GRA_JHS_3", "初三", "DEGREE_1"));
            datas.add(new OptionsInfo("GRA_SHS_1", "高一", "DEGREE_1"));
            datas.add(new OptionsInfo("GRA_SHS_2", "高二", "DEGREE_1"));
            datas.add(new OptionsInfo("GRA_SHS_3", "高三", "DEGREE_1"));
        } else {
            datas.add(new OptionsInfo("GRA_BC_1", "大一", "DEGREE_2"));

            datas.add(new OptionsInfo("GRA_BC_2", "大二", "DEGREE_2"));

            datas.add(new OptionsInfo("GRA_BC_3", "大三", "DEGREE_3"));

            datas.add(new OptionsInfo("GRA_BC_4", "大四", "GRA_BC_4"));

        }
        return datas;
    }

}