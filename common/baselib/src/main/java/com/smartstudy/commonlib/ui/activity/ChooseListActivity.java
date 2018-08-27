package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.mvp.contract.ChooseListContract;
import com.smartstudy.commonlib.mvp.presenter.ChooseListPresenter;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.router.Router;

import java.util.ArrayList;
import java.util.List;

public class ChooseListActivity extends BaseActivity implements ChooseListContract.View {

    private RecyclerView rcv_list;
    private View emptyView;
    private View footView;
    private TextView topdefault_righttext;

    private String flag;
    private String value;
    private ChooseListContract.Presenter chooseP;
    private CommonAdapter<IdNameInfo> mAdapter;
    private EmptyWrapper<IdNameInfo> emptyWrapper;
    private HeaderAndFooterWrapper mFooter;
    private List<IdNameInfo> nameInfos;
    private boolean isChange;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
    }

    @Override
    protected void onDestroy() {
        if (rcv_list != null) {
            rcv_list.removeAllViews();
            rcv_list = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (nameInfos != null) {
            nameInfos.clear();
            nameInfos = null;
        }
        if (chooseP != null) {
            chooseP = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
        from = data.getStringExtra("from");
        isChange = data.getBooleanExtra("ischange", false);
        value = data.getStringExtra("value");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        topdefault_righttext = (TextView) findViewById(R.id.topdefault_righttext);
        TextView topdefault_centertitle = (TextView) findViewById(com.smartstudy.commonlib.R.id.topdefault_centertitle);
        topdefault_centertitle.setText(data.getStringExtra("title"));
        rcv_list = (RecyclerView) findViewById(R.id.rcv_list);
        rcv_list.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(mLayoutManager);
        if (ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
            topdefault_righttext.setVisibility(View.VISIBLE);
            topdefault_righttext.setText("保存");
            topdefault_righttext.setTextColor(getResources().getColor(R.color.app_main_color));
            findViewById(R.id.tv_multi_tips).setVisibility(View.VISIBLE);
        }
        if (data.hasExtra("list")) {
            nameInfos = initData(value, data.<IdNameInfo>getParcelableArrayListExtra("list"));
        } else {
            nameInfos = new ArrayList<>();
        }
        mAdapter = new CommonAdapter<IdNameInfo>(ChooseListActivity.this, R.layout.item_choose_list, nameInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, IdNameInfo idNameInfo, int position) {
                if (ParameterUtils.EDIT_EG_SCORE.equals(flag) || ParameterUtils.EDIT_GRE_SCORE.equals(flag)
                    || ParameterUtils.EDIT_SAT_SCORE.equals(flag)) {
                    holder.getView(R.id.view_first).setVisibility(View.GONE);
                    if (idNameInfo.isTop()) {
                        holder.getView(R.id.tv_title).setVisibility(View.VISIBLE);
                    } else {
                        holder.getView(R.id.tv_title).setVisibility(View.GONE);
                    }
                    holder.setText(R.id.tv_title, idNameInfo.getGroup());
                } else {
                    if (position == 0) {
                        holder.getView(R.id.view_first).setVisibility(View.VISIBLE);
                    } else {
                        holder.getView(R.id.view_first).setVisibility(View.GONE);
                    }
                }
                if (ParameterUtils.EDIT_GZ_SCORE.equals(flag) || ParameterUtils.EDIT_BK_SCORE.equals(flag)) {
                    holder.setText(R.id.tv_list_name, Html.fromHtml(idNameInfo.getName() + "\u3000<font color=#bbbbbb>" + idNameInfo.getHint() + "</font>"));
                } else {
                    holder.setText(R.id.tv_list_name, idNameInfo.getName());
                }
                ImageView ivChoose = holder.getView(R.id.iv_choose);
                if (ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
                    if (idNameInfo.is_selected()) {
                        ivChoose.setImageResource(R.drawable.ic_circle_selected);
                    } else {
                        ivChoose.setImageResource(R.drawable.ic_circle_unselected);
                    }
                } else {
                    if (idNameInfo.is_selected()) {
                        ivChoose.setVisibility(View.VISIBLE);
                    } else {
                        ivChoose.setVisibility(View.INVISIBLE);
                    }
                }
                if (position == nameInfos.size() - 1) {
                    if (!ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
                        holder.getView(R.id.view_last).setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.getView(R.id.view_last).setVisibility(View.GONE);
                }
            }
        };
        if (ParameterUtils.EDIT_INTENT.equals(flag)) {
            footView = mInflater.inflate(R.layout.layout_targetcountry_footer, rcv_list, false);
            footView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Router.build("CompareCountryActivity").go(ChooseListActivity.this);
                }
            });
            mFooter = new HeaderAndFooterWrapper(mAdapter);
            if (ParameterUtils.TYPE_OPTIONS_PERSON.equals(from)) {
                mFooter.addFootView(footView);
            }
            emptyWrapper = new EmptyWrapper<>(mFooter);
        } else {
            emptyWrapper = new EmptyWrapper<>(mAdapter);
        }
        rcv_list.setAdapter(emptyWrapper);
        new ChooseListPresenter(this);
        if (!ParameterUtils.TYPE_OPTIONS_PERSON.equals(from)
            && !ParameterUtils.TYPE_OPTIONS_COURTY.equals(from)
            && !ParameterUtils.TYPE_OPTIONS_PROJ.equals(from)) {
            emptyView = mInflater.inflate(R.layout.layout_empty, rcv_list, false);
            chooseP.showLoading(this, emptyView);
            chooseP.getOptionsData(from, value, flag, nameInfos);
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topdefault_righttext.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
                    IdNameInfo nameInfo = nameInfos.get(position);
                    ImageView ivChoose = holder.itemView.findViewById(R.id.iv_choose);
                    if (nameInfo.is_selected()) {
                        nameInfo.setIs_selected(false);
                        ivChoose.setImageResource(R.drawable.ic_circle_unselected);
                    } else {
                        nameInfo.setIs_selected(true);
                        ivChoose.setImageResource(R.drawable.ic_circle_selected);
                    }
                } else {
                    chooseP.doItemClick(flag, position, nameInfos);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            finish();
        } else if (id == R.id.topdefault_righttext) {
            //社会活动
            String ids = "";
            for (IdNameInfo nameInfo : nameInfos) {
                if (nameInfo.is_selected()) {
                    ids += "," + nameInfo.getId();
                }
            }
            if (TextUtils.isEmpty(ids)) {
                ToastUtils.showToast("请至少选一项！");
            } else {
                PersonalParamsInfo info = new PersonalParamsInfo();
                info.setActivitySocial(ids.substring(1));
                editData(null, info);
            }
        }
    }

    @Override
    public void setPresenter(ChooseListContract.Presenter presenter) {
        if (presenter != null) {
            this.chooseP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void editMyInfoSuccess(String jsonObject, IdNameInfo nameInfo) {
        Intent data = new Intent();
        if (nameInfo != null) {
            if (ParameterUtils.EDIT_EG_SCORE.equals(flag) || ParameterUtils.EDIT_GRE_SCORE.equals(flag)
                || ParameterUtils.EDIT_SAT_SCORE.equals(flag)) {
                if ("WAIVER".equals(nameInfo.getId())) {
                    data.putExtra("new_value", nameInfo.getName());
                } else {
                    data.putExtra("new_value", nameInfo.getGroup() + nameInfo.getName());
                }
            } else {
                data.putExtra("new_value", nameInfo.getName());
            }
            if (ParameterUtils.EDIT_INTENT.equals(flag)) {
                JSONObject object = JSONObject.parseObject(nameInfo.toString());
                object.put("options", nameInfos);
                SPCacheUtils.put("target_countryInfo", JSON.toJSONString(object));
            }
        }
        data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
        // 返回个人信息详情数据
        data.putExtra("result", jsonObject);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void refreshItems() {
        if (chooseP != null) {
            chooseP.setEmptyView(emptyView);
            if (ParameterUtils.EDIT_INTENT.equals(flag)) {
                mFooter.addFootView(footView);
            }
            emptyWrapper.notifyDataSetChanged();
        }
    }

    @Override
    public void editData(IdNameInfo nameInfo, PersonalParamsInfo paramsModel) {
        if (isChange) {
            chooseP.editMyInfo(paramsModel, nameInfo);
        } else {
            Intent data = new Intent();
            data.putExtra("new_value", nameInfo.toString());
            data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        chooseP.getOptionsData(from, value, flag, nameInfos);
    }

    private List<IdNameInfo> initData(String value, ArrayList<IdNameInfo> nameInfos) {
        if (nameInfos != null) {
            if (ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
                String[] ids = value.split(",");
                for (IdNameInfo info : nameInfos) {
                    info.setIs_selected(false);
                    for (String id : ids) {
                        if (id.equals(info.getId())) {
                            info.setIs_selected(true);
                        }
                    }
                }
            } else {
                for (IdNameInfo info : nameInfos) {
                    if (value.equals(info.getName()) || value.equals(info.getGroup() + info.getName())) {
                        info.setIs_selected(true);
                    } else {
                        info.setIs_selected(false);
                    }
                }
            }
        } else {
            nameInfos = new ArrayList<>();
        }
        return nameInfos;
    }
}
