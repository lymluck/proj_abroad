package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.tools.GpaCreditFilter;
import com.smartstudy.commonlib.base.tools.GpaScoreFilter;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartstudy.xxd.R.id.rclv_gpa;

/**
 * Created by yqy on 2017/10/20.
 */

public class GpaCalculationActivity extends BaseActivity {
    private RecyclerView rclvGpa;
    private int number = 8;
    private List<String> courseList;
    private CommonAdapter<String> mAdapter;
    private HeaderAndFooterWrapper mHeader;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View footView;
    private Map<String, String> dataMap = new HashMap<>();
    private TextView tvExplain;
    private WeakHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_calculation);
    }

    @Override
    protected void onDestroy() {
        if (courseList != null) {
            courseList.clear();
            courseList = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (dataMap != null) {
            dataMap.clear();
            dataMap = null;
        }
        if (handler != null) {
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        ((TextView) this.findViewById(R.id.topdefault_centertitle)).setText("GPA计算");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        rclvGpa = (RecyclerView) findViewById(rclv_gpa);
        initData();
        rclvGpa.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvGpa.setLayoutManager(mLayoutManager);
        closeDefaultAnimator();
        rclvGpa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        initHeaderAndFooter();
    }

    @Override
    public void initEvent() {
        handler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        if (rclvGpa.canScrollVertically(-1)) {
                            rclvGpa.scrollBy(0, DensityUtils.dip2px(64.5f));
                        } else {
                            rclvGpa.smoothScrollToPosition(mHeader.getItemCount() - 1);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        this.findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        this.findViewById(R.id.tv_add_course).setOnClickListener(this);
        this.findViewById(R.id.tv_start_calculation).setOnClickListener(this);
        tvExplain.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_add_course:
                addCourse();
                break;
            case R.id.tv_explain:
                startActivity(new Intent(this, GpaDescriptionActivity.class));
                break;
            case R.id.tv_start_calculation:
                if (TextUtils.isEmpty(handleData())) {
                    ToastUtils.showToast("请填写至少一个完整的成绩");
                    return;
                }
                Intent intent = new Intent(this, GpaCalculationDetailActivity.class);
                intent.putExtra("result", handleData());
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

    }


    private void initData() {
        courseList = new ArrayList<String>();
        if (courseList != null) {
            courseList.clear();
        }
        for (int i = 0; i < number; i++) {
            courseList.add("课程" + (i + 1));
        }
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<String>(this, R.layout.item_gpa_calculation, courseList) {
            @Override
            protected void convert(final ViewHolder holder, String course, final int position) {
                final EditText etResults = holder.getView(R.id.et_results);
                etResults.setFilters(new InputFilter[]{new GpaScoreFilter()});

                final EditText etCredit = holder.getView(R.id.et_credit);
                etCredit.setFilters(new InputFilter[]{new GpaCreditFilter()});
//
                etResults.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            dataMap.put(position + "results", s.toString());
                        }
                    }
                });


                etCredit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            dataMap.put(position + "credit", s.toString());
                        }
                    }
                });
                holder.setText(R.id.tv_course_name, courseList.get(position).toString() + "");
            }
        };

    }

    private void initHeaderAndFooter() {
        footView = mInflater.inflate(R.layout.layout_gpa_footer, null, false);
        tvExplain = (TextView) footView.findViewById(R.id.tv_explain);
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        footView.setLayoutParams(lp);
        mHeader.addFootView(footView);
        rclvGpa.setAdapter(mHeader);
    }


    private void addCourse() {
        if (number >= 100) {
            ToastUtils.showToast("最多只能添加100个课程");
            return;
        }
        courseList.add(number, "课程" + (number + 1));
        mHeader.notifyItemInserted(number);
        handler.sendEmptyMessageDelayed(ParameterUtils.MSG_WHAT_REFRESH, 200);
        number += 1;
    }


    private void deleteCourse() {
        courseList.remove(number);
        number -= 1;
        mAdapter.notifyItemRemoved(number);
    }


    private String handleData() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < number; i++) {
            if (!TextUtils.isEmpty(dataMap.get(i + "credit")) && !TextUtils.isEmpty(dataMap.get(i + "results"))) {
                if (TextUtils.isEmpty(stringBuffer.toString())) {
                    stringBuffer.append(dataMap.get(i + "results")).append(":").append(dataMap.get(i + "credit"));
                } else {
                    stringBuffer.append(",").append(dataMap.get(i + "results")).append(":").append(dataMap.get(i + "credit"));
                }

            }
        }
        return stringBuffer.toString();
    }


    public void closeDefaultAnimator() {
        rclvGpa.getItemAnimator().setAddDuration(0);
        rclvGpa.getItemAnimator().setChangeDuration(0);
        rclvGpa.getItemAnimator().setMoveDuration(0);
        rclvGpa.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) rclvGpa.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}
