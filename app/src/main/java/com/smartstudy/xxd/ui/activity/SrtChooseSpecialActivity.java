package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.SerializableMap;
import com.smartstudy.commonlib.entity.SpecialQaInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.customView.NoScrollViewPager;
import com.smartstudy.commonlib.ui.customView.ViewPagerScroller;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SrtChooseSpecialContract;
import com.smartstudy.xxd.mvp.presenter.SrtChooseSpecialPresenter;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SrtChooseSpecialActivity extends UIActivity implements SrtChooseSpecialContract.View {

    private NoScrollViewPager vpcontent;
    private TextView tvhastest;
    private TextView tv_num;
    private PagerAdapter mPagerAdapter;
    private ProgressBar pb_qa;

    private List<SpecialQaInfo> specialQaInfos;
    private List<SpecialQaInfo> allDatas;
    private SrtChooseSpecialContract.Presenter srtP;
    private int allSize = 0; //总题数
    private int nowPos = 0;
    private WeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_choose_special);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("智能选专业");
        TextView topdefault_rightmenu = (TextView) findViewById(R.id.topdefault_rightmenu);
        topdefault_rightmenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_intro_blue, 0, 0, 0);
        topdefault_rightmenu.setVisibility(View.VISIBLE);
        this.vpcontent = (NoScrollViewPager) findViewById(R.id.vp_content);
        vpcontent.setNoScroll(true);
        ViewPagerScroller scroller = new ViewPagerScroller(this);
        scroller.initViewPagerScroll(vpcontent);
        this.tvhastest = (TextView) findViewById(R.id.tv_has_test);
        this.tv_num = (TextView) findViewById(R.id.tv_num);
        tv_num.setText("0");
        this.pb_qa = (ProgressBar) findViewById(R.id.pb_qa);
        tvhastest.setText(String.format(getString(R.string.has_test), "0"));
        specialQaInfos = new ArrayList<>();
        vpcontent.setAdapter(mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return specialQaInfos.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                final SpecialQaInfo info = specialQaInfos.get(position);
                View view = View.inflate(SrtChooseSpecialActivity.this, R.layout.item_special_qa_list, null);
                TextView tv_special_qa = (TextView) view.findViewById(R.id.tv_special_qa);
                tv_special_qa.setText(position + 1 + ". " + info.getText());
                final RadioGroup rg_btn = (RadioGroup) view.findViewById(R.id.rg_btn);
                rg_btn.check(info.getSelectedId());//记住上次的选择
                rg_btn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        info.setSelectedId(checkedId);
                        if (!info.is_selected()) {
                            info.setIs_selected(true);
                            //下一题解锁
                            if (position < allSize) {
                                nowPos = position;
                                //解题进度＋1
                                int progress = pb_qa.getProgress() + 1;
                                tv_num.setText(progress + "");
                                pb_qa.setProgress(progress);
                                //跳转到下一题
                                mHandler.sendEmptyMessageDelayed(1, 0);
                            }
                        }
                        if (checkedId == R.id.btn_qa_yes) {
                            info.setScore(1);
                        } else if (checkedId == R.id.btn_qa_no) {
                            info.setScore(0);
                        }
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
                // 解决数据源清空，Item 不销毁的 bug
                return specialQaInfos != null && specialQaInfos.size() == 0 ? POSITION_NONE : super.getItemPosition(object);
                // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
//                return POSITION_NONE;
            }
        });
        new SrtChooseSpecialPresenter(this);
        srtP.goChooseQa();
        srtP.getHasTestNum();
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        //下一题解锁,总共12道题
                        if (nowPos + 1 + 1 <= 12) {
                            unLockItem(nowPos + 1 + 1);
                            vpcontent.setCurrentItem(nowPos + 1, true);
                        } else {
                            //回答完毕，展示结果
                            srtP.handlerScore(specialQaInfos);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.topdefault_rightmenu).setOnClickListener(this);
    }

    private void unLockItem(int position) {
        specialQaInfos.clear();
        specialQaInfos.addAll(allDatas.subList(0, position));
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.topdefault_rightmenu:
                startActivity(new Intent(this, SrtSpecialResultIntroActivity.class));
                break;
        }
    }

    @Override
    public void setPresenter(SrtChooseSpecialContract.Presenter presenter) {
        if (presenter != null) {
            srtP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void getHasTestNumSuccess(String num) {
        tvhastest.setText(String.format(getString(R.string.has_test), num));
    }

    @Override
    public void showQa(List<SpecialQaInfo> list, JSONObject answers) {
        allDatas = list;
        allSize = allDatas.size();
        unLockItem(1);
    }

    @Override
    public void showResult(Map<String, Object> result, JSONArray arr_fit, JSONArray arr_rec) {
        SerializableMap map = new SerializableMap();
        map.setMap(result);
        Bundle data = new Bundle();
        data.putSerializable("data", map);
        startActivityForResult(new Intent(this, SrtSpecialResultActivity.class)
                .putExtras(data)
                .putExtra("fit", arr_fit.toString())
                .putExtra("rec", arr_rec.toString()), ParameterUtils.REQUEST_CODE_SPECIAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_SPECIAL:
                //复原初始数据
                nowPos = 0;
                tv_num.setText("0");
                pb_qa.setProgress(0);
                specialQaInfos.clear();
                mPagerAdapter.notifyDataSetChanged();
                srtP.goChooseQa();
                break;
        }
    }
}
