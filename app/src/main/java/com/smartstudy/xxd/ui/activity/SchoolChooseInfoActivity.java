package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.entity.SchoolChooseInfo;
import com.smartstudy.xxd.mvp.contract.SchoolChooseInfoContract;
import com.smartstudy.xxd.mvp.presenter.SchoolChooseInfoPresenter;
import com.smartstudy.xxd.ui.customview.RingView;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author luoyongming
 * @date on 2018/6/1
 * @describe 选择的学校数据统计页面
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class SchoolChooseInfoActivity extends BaseActivity implements SchoolChooseInfoContract.View {

    private SchoolChooseInfoContract.Presenter presenter;
    private RingView ringView;
    private TextView tvCount;
    private TextView tvTopSchool;
    private TextView tvMiddleSchool;
    private TextView tvBottomSchool;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_choose_info);
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        id = data.getStringExtra("id");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(data.getStringExtra(TITLE));
        tvCount = findViewById(R.id.tv_count);
        tvCount.setText(String.format(getString(R.string.people_choose_school), "0"));
        ringView = findViewById(R.id.ring_view);
        SpannableString styledText = new SpannableString("0 人");
        styledText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_count), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_unit), 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTopSchool = findViewById(R.id.tv_top_school);
        tvTopSchool.setText(styledText, TextView.BufferType.SPANNABLE);
        tvMiddleSchool = findViewById(R.id.tv_middle_school);
        tvMiddleSchool.setText(styledText, TextView.BufferType.SPANNABLE);
        tvBottomSchool = findViewById(R.id.tv_bottom_school);
        tvBottomSchool.setText(styledText, TextView.BufferType.SPANNABLE);
        new SchoolChooseInfoPresenter(this);
        presenter.getSchoolStat(id);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.tv_to_detail).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_to_detail:
                if (!TextUtils.isEmpty(id)) {
                    startActivity(new Intent(this, CollegeDetailActivity.class).putExtra("id", id));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SchoolChooseInfoContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void showSchoolStat(SchoolChooseInfo info) {
        findViewById(R.id.llyt_loading).setVisibility(View.GONE);
        // 展示选校
        List<SchoolChooseInfo.MatchTypesEntity> types = info.getMatchTypes();
        if (types.size() >= 3) {
            showTypeStat(types);
        }
        // 展示环形图
        List<SchoolChooseInfo.YearsEntity> years = info.getYears();
        if (years.size() >= 4) {
            showRingView(years);
        }
        // 展示选校的人
        List<SchoolChooseInfo.WatchersEntity> watchers = info.getWatchers();
        if (watchers != null && watchers.size() > 0) {
            showSchoolPerson(watchers);
        }
        // 展示另外的选校
        List<SchoolChooseInfo.TopSchoolsEntity> schools = info.getTopSchools();
        if (schools != null && schools.size() > 0) {
            showSchools(schools);
        }
    }

    @Override
    public void getOtherStudentDetailSuccess(OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo) {
        if (otherStudentChoiceDetailInfo != null) {
            startActivity(new Intent(this, ChoiceSchoolPersonActivity.class).putExtra("person_detail", otherStudentChoiceDetailInfo));
        }
    }

    private void showTypeStat(List<SchoolChooseInfo.MatchTypesEntity> types) {
        int count = 0;
        for (SchoolChooseInfo.MatchTypesEntity type : types) {
            count += type.getUsersCount();
        }
        tvCount.setText(String.format(getString(R.string.people_choose_school), count + ""));
        findViewById(R.id.tv_top_me).setVisibility(types.get(0).getUsersIncludeMe() ? View.VISIBLE : View.GONE);
        findViewById(R.id.tv_middle_me).setVisibility(types.get(1).getUsersIncludeMe() ? View.VISIBLE : View.GONE);
        findViewById(R.id.tv_bottom_me).setVisibility(types.get(2).getUsersIncludeMe() ? View.VISIBLE : View.GONE);
        String topCount = types.get(0).getUsersCount() + " 人";
        SpannableString styledTopText = new SpannableString(topCount);
        styledTopText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_count), 0,
            topCount.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledTopText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_unit),
            topCount.length() - 2, topCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTopSchool.setText(styledTopText, TextView.BufferType.SPANNABLE);
        String middleCount = types.get(1).getUsersCount() + " 人";
        SpannableString styledMiddleText = new SpannableString(middleCount);
        styledMiddleText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_count), 0,
            middleCount.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledMiddleText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_unit),
            middleCount.length() - 2, middleCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMiddleSchool.setText(styledMiddleText, TextView.BufferType.SPANNABLE);
        String bottomCount = types.get(2).getUsersCount() + " 人";
        SpannableString styledBottomText = new SpannableString(bottomCount);
        styledBottomText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_count), 0,
            styledBottomText.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledBottomText.setSpan(new TextAppearanceSpan(this, R.style.style_choose_unit),
            styledBottomText.length() - 2, styledBottomText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBottomSchool.setText(styledBottomText, TextView.BufferType.SPANNABLE);
    }

    private void showRingView(List<SchoolChooseInfo.YearsEntity> years) {
        // 添加的是颜色
        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.ring_color_2);
        colorList.add(R.color.ring_color_1);
        colorList.add(R.color.ring_color_4);
        colorList.add(R.color.ring_color_3);
        int count = 0;
        for (SchoolChooseInfo.YearsEntity year : years) {
            count += year.getCount();
        }
        //  添加的是百分比
        List<Float> rateList = new ArrayList<>();
        float one = Math.round((float) years.get(0).getCount() * 100 / count);
        float two = Math.round((float) years.get(1).getCount() * 100 / count);
        float three = Math.round((float) years.get(2).getCount() * 100 / count);
        float four = 100 - one - two - three;
        rateList.add(one);
        rateList.add(two);
        rateList.add(three);
        rateList.add(four);
        ringView.setShow(colorList, rateList, true, false);
        ringView.invalidate();
        ((TextView) findViewById(R.id.tv_rt_year)).setText(years.get(0).getYear());
        ((TextView) findViewById(R.id.tv_rt_person)).setText(years.get(0).getCount() + "人");
        ((TextView) findViewById(R.id.tv_lt_year)).setText(years.get(1).getYear());
        ((TextView) findViewById(R.id.tv_lt_person)).setText(years.get(1).getCount() + "人");
        ((TextView) findViewById(R.id.tv_lb_year)).setText(years.get(2).getYear());
        ((TextView) findViewById(R.id.tv_lb_person)).setText(years.get(2).getCount() + "人");
        ((TextView) findViewById(R.id.tv_rb_year)).setText(years.get(3).getYear());
        ((TextView) findViewById(R.id.tv_rb_person)).setText(years.get(3).getCount() + "人");
        findViewById(R.id.rl_ring).setVisibility(View.VISIBLE);
    }

    private void showSchoolPerson(List<SchoolChooseInfo.WatchersEntity> watchers) {
        LinearLayout llSchoolPerson = findViewById(R.id.ll_school_person);
        llSchoolPerson.setVisibility(View.VISIBLE);
        int itemPadding = DensityUtils.dip2px(16f);
        int avatarWidth = DensityUtils.dip2px(40f);
        int nameLeftMargin = DensityUtils.dip2px(12f);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        nameParams.weight = 1;
        nameParams.leftMargin = nameLeftMargin;
        nameParams.rightMargin = nameLeftMargin;
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(avatarWidth, avatarWidth);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            DensityUtils.dip2px(0.5f));
        lineParams.leftMargin = itemPadding;
        lineParams.rightMargin = itemPadding;
        ContextThemeWrapper btnWrapper = new ContextThemeWrapper(this, R.style.app_onebtn);
        for (final SchoolChooseInfo.WatchersEntity watcher : watchers) {
            LinearLayout item = new LinearLayout(this);
            item.setClickable(true);
            item.setBackgroundResource(R.drawable.simple_item_bg);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setLayoutParams(itemParams);
            item.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
            ImageView avatar = new ImageView(this);
            avatar.setLayoutParams(avatarParams);
            DisplayImageUtils.formatPersonImgUrl(this, watcher.getAvatar(), avatar);
            TextView tvName = new TextView(this);
            tvName.setText(watcher.getName());
            tvName.setEllipsize(TextUtils.TruncateAt.END);
            tvName.setMaxLines(1);
            tvName.setLayoutParams(nameParams);
            tvName.setTextColor(getResources().getColor(R.color.app_text_color2));
            tvName.setTextSize(15f);
            TextView tvCount = new TextView(this);
            String text = "共<font color='#078CF1'>" + watcher.getSelectSchoolCount() + "</font>所选校";
            tvCount.setText(Html.fromHtml(text));
            tvCount.setTextColor(getResources().getColor(R.color.app_text_color));
            tvCount.setTextSize(13f);
            item.addView(avatar, -1);
            item.addView(tvName, -1);
            item.addView(tvCount, -1);
            View line = new View(this);
            line.setBackgroundColor(getResources().getColor(R.color.horizontal_line_color));
            line.setLayoutParams(lineParams);
            llSchoolPerson.addView(item, -1);
            llSchoolPerson.addView(line, -1);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.getOtherStudentDetail(watcher.getId() + "");
                }
            });
        }
        // 代码方式设置view的style
        Button btnMore = new Button(btnWrapper, null, 0);
        btnMore.setText("查看全部同学");
        btnMore.setTextSize(14f);
        btnMore.setClickable(true);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            DensityUtils.dip2px(36f));
        int lrMargin = DensityUtils.dip2px(96f);
        int tbMargin = DensityUtils.dip2px(24f);
        btnParams.leftMargin = lrMargin;
        btnParams.rightMargin = lrMargin;
        btnParams.topMargin = tbMargin;
        btnParams.bottomMargin = tbMargin;
        btnMore.setLayoutParams(btnParams);
        llSchoolPerson.addView(btnMore, -1);
        //查看全部同学信息
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(id)) {
                    startActivity(new Intent(SchoolChooseInfoActivity.this, ChoiceSchoolListActivity.class).putExtra("id", id));
                }
            }
        });
    }

    private void showSchools(List<SchoolChooseInfo.TopSchoolsEntity> schools) {
        LinearLayout llSchool = findViewById(R.id.ll_school);
        llSchool.setVisibility(View.VISIBLE);
        int itemPadding = DensityUtils.dip2px(16f);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            DensityUtils.dip2px(0.5f));
        lineParams.leftMargin = itemPadding;
        lineParams.rightMargin = itemPadding;
        for (final SchoolChooseInfo.TopSchoolsEntity school : schools) {
            View view = mInflater.inflate(R.layout.item_choose_school_list, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 查看学校选校统计信息
                    Bundle params = new Bundle();
                    params.putString("id", school.getId() + "");
                    Intent toMoreDetails = new Intent(SchoolChooseInfoActivity.this, CollegeDetailActivity.class);
                    toMoreDetails.putExtras(params);
                    startActivity(toMoreDetails);
                }
            });
            ImageView ivLogo = view.findViewById(R.id.iv_logo);
            DisplayImageUtils.formatCircleImgUrl(this, school.getLogo(), ivLogo);
            TextView tvName = view.findViewById(R.id.tv_school_name);
            tvName.setText(school.getChineseName());
            TextView tvEgName = view.findViewById(R.id.tv_English_name);
            tvEgName.setText(school.getEnglishName());
            TextView tvCount = view.findViewById(R.id.tv_count);
            String textCount = "共有<font color='#078CF1'>" + school.getSelectedCount() + "</font>人选择";
            tvCount.setText(Html.fromHtml(textCount));
            llSchool.addView(view, -1);
            View line = new View(this);
            line.setBackgroundColor(getResources().getColor(R.color.horizontal_line_color));
            line.setLayoutParams(lineParams);
            llSchool.addView(line, -1);
        }
    }
}
