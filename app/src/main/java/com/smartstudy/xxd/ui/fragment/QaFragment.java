package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.PagerSlidingTabStrip;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.activity.AddQuestionActivity;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QaFragment extends BaseFragment {

    private ImageView topRightBtn;
    private TextView myQaRed;
    private ViewPager pagerQas;
    private QaListFragment myQaFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_qa;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 含子Fragment，不统计当前页面
        isPrepared = true;
        UApp.actionEvent(mActivity, "19_B_question_list");
    }

    @Override
    protected void initView() {
        myQaRed = rootView.findViewById(R.id.my_qa_red);
        RelativeLayout homeTopQa = rootView.findViewById(R.id.home_top_qa);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) homeTopQa.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight();
            homeTopQa.setLayoutParams(params);
            homeTopQa.setPadding(0, ScreenUtils.getStatusHeight(), 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.topdefault_centertitle)).setText(mActivity.getString(R.string.qa));
        TextView topRightMenu = rootView.findViewById(R.id.topdefault_rightmenu);
        topRightMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_blue, 0, 0, 0);
        topRightMenu.setPadding(DensityUtils.dip2px(8f), 0, 0, 0);
        topRightMenu.setVisibility(View.VISIBLE);
        topRightMenu.setOnClickListener(this);
        topRightBtn = rootView.findViewById(R.id.topdefault_rightbutton);
        topRightBtn.setImageResource(R.drawable.ic_search_blue);
        topRightBtn.setVisibility(View.VISIBLE);
        topRightBtn.setOnClickListener(this);
        ImageView topLeftBtn = rootView.findViewById(R.id.topdefault_leftbutton);
        topLeftBtn.setOnClickListener(this);
        Bundle data = getArguments();
        if (data == null) {
            topLeftBtn.setVisibility(View.GONE);
        } else {
            if (data.containsKey("showRed")) {
                myQaRed.setVisibility(data.getBoolean("showRed") ? View.VISIBLE : View.GONE);
            }
        }
        pagerQas = rootView.findViewById(R.id.pager_qas);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        initFragments(fragments);
        List<String> titles = Arrays.asList("推荐问答", "最新问答", "我的问答");
        pagerQas.setAdapter(new XxdViewPagerFragmentAdapter(getChildFragmentManager(), titles, null, fragments));
        pagerQas.setOffscreenPageLimit(3);
        PagerSlidingTabStrip tabQas = rootView.findViewById(R.id.tabs_qas);
        tabQas.setViewPager(pagerQas);
        pagerQas.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                mActivity.finish();
                break;
            case R.id.topdefault_rightbutton:
                UApp.actionEvent(mActivity, "17_A_search_btn");
                Intent toSearch = new Intent(mActivity, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.QA_FLAG);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                        topRightBtn, "btn_tr").toBundle());
                } else {
                    startActivity(toSearch);
                }
                break;
            case R.id.topdefault_rightmenu:
                startActivity(new Intent(mActivity, AddQuestionActivity.class));
                break;
            default:
                break;
        }
    }


    private void initFragments(ArrayList<BaseFragment> fragments) {
        Bundle recommendData = new Bundle();
        recommendData.putString("data_flag", "recommend");
        fragments.add(QaListFragment.getInstance(recommendData));
        Bundle lastData = new Bundle();
        lastData.putString("data_flag", "list");
        fragments.add(QaListFragment.getInstance(lastData));
        Bundle myData = new Bundle();
        myData.putString("data_flag", "my");
        myQaFragment = QaListFragment.getInstance(myData);
        fragments.add(myQaFragment);
    }

    public void showMyQaRed(int unread) {
        // 刷新我的问答接口
        if (pagerQas.getCurrentItem() == 2) {
            // 如果是在我的问答tab，直接刷新
            if (myQaFragment != null) {
                myQaFragment.refreshList();
            }
        } else {
            // 缓存需要刷新的标记
            SPCacheUtils.put("my_qa_refresh", true);
        }
        if (unread > 0) {
            showMyQaRedIsVisible(View.VISIBLE);

        } else {
            myQaRed.setVisibility(View.GONE);
        }
    }

    public void showMyQaRedIsVisible(int visible) {
        myQaRed.setVisibility(visible);
    }
}
