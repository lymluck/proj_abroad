package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customView.FullyLinearLayoutManager;
import com.smartstudy.commonlib.ui.customView.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.RelatedQuesInfo;
import com.smartstudy.xxd.mvp.contract.RelatedQaContract;
import com.smartstudy.xxd.mvp.presenter.RelatedQaPresenter;

import java.util.ArrayList;
import java.util.List;

public class RelatedQuesActivity extends UIActivity implements RelatedQaContract.View {

    private RecyclerView rclvreqa;
    private TextView tvchangenext;
    private Button btnhelpno;
    private Button btnhelpyes;
    private View emptyView;

    private CommonAdapter<RelatedQuesInfo> mAdapter;
    private EmptyWrapper<RelatedQuesInfo> emptyWrapper;
    private List<RelatedQuesInfo> relatedQuesInfoList;
    private String ques_id;
    private RelatedQaContract.Presenter reP;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_ques);
    }

    @Override
    protected void onDestroy() {
        if (reP != null) {
            reP = null;
        }
        if (rclvreqa != null) {
            rclvreqa.removeAllViews();
            rclvreqa = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (relatedQuesInfoList != null) {
            relatedQuesInfoList.clear();
            relatedQuesInfoList = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("问答推荐");
        ques_id = getIntent().getStringExtra("ques_id");
        this.btnhelpyes = (Button) findViewById(R.id.btn_help_yes);
        this.btnhelpno = (Button) findViewById(R.id.btn_help_no);
        this.tvchangenext = (TextView) findViewById(R.id.tv_change_next);
        this.rclvreqa = (RecyclerView) findViewById(R.id.rclv_re_qa);
        rclvreqa.setHasFixedSize(true);
        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvreqa.setLayoutManager(mLayoutManager);
        rclvreqa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16), 0).colorResId(R.color.horizontal_line_color).build());
        relatedQuesInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<RelatedQuesInfo>(this, R.layout.item_related_qa_list, relatedQuesInfoList) {
            @Override
            protected void convert(ViewHolder holder, RelatedQuesInfo relatedQuesInfo, int position) {
                holder.setText(R.id.tv_qa, relatedQuesInfo.getQuestion());
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rclvreqa.setAdapter(emptyWrapper);
        new RelatedQaPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvreqa, false);
        reP.showLoading(this, emptyView);
        reP.getQuestions(ques_id);
    }

    @Override
    public void initEvent() {
        btnhelpno.setOnClickListener(this);
        btnhelpyes.setOnClickListener(this);
        tvchangenext.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                RelatedQuesInfo info = relatedQuesInfoList.get(position);
                Intent toMoreDetails = new Intent(RelatedQuesActivity.this, ShowWebViewActivity.class);
                toMoreDetails.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_QUESTION_DETAIL), info.getId()));
                toMoreDetails.putExtra("title", "问答详情");
                toMoreDetails.putExtra("url_action", "get");
                startActivity(toMoreDetails);
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
            case R.id.btn_help_yes:
                reP.qaHelpful(ques_id, result, true);
                break;
            case R.id.btn_help_no:
                reP.qaHelpful(ques_id, result, false);
                break;
            case R.id.tv_change_next:
                reP.getQuestions(ques_id);
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(RelatedQaContract.Presenter presenter) {
        if (presenter != null) {
            reP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showQa(List<RelatedQuesInfo> list, String result) {
        if (reP != null) {
            reP.setEmptyView( emptyView);
            this.result = result;
            relatedQuesInfoList.clear();
            relatedQuesInfoList.addAll(list);
            emptyWrapper.notifyDataSetChanged();
        }
    }

    @Override
    public void goFinish() {
        finish();
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        reP.showLoading(this, emptyView);
        reP.getQuestions(ques_id);
    }
}
