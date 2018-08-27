package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.TreeRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItemGroup;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.GpaCalculationDetail;
import com.smartstudy.xxd.entity.GpaDesInfo;
import com.smartstudy.xxd.mvp.contract.GpaDescriptionContract;
import com.smartstudy.xxd.mvp.presenter.GpaDescriptionPresenter;
import com.smartstudy.xxd.ui.adapter.GpaDesOneTreeItemParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/10/24.
 */

public class GpaDescriptionActivity extends UIActivity implements GpaDescriptionContract.View {
    private LinearLayout mSuspensionBar;
    private TextView tvName;
    private ImageView iv_more;
    private RecyclerView rclvGpaDes;
    private LinearLayoutManager gpaDesLayoutManager;
    private ArrayList<Integer> roots;
    private TreeRecyclerAdapter gpaDesAdapter;
    private ArrayList<TreeItem> GpaDesTreeItems;
    private int mCurrentPosition = 0;
    private int mClickPos = 0;
    private View emptyView;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private int mSuspensionHeight;
    private GpaDescriptionContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_des);
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("GPA计算");
        mSuspensionBar = (LinearLayout) findViewById(R.id.layout_title);
        tvName = (TextView) findViewById(R.id.tv_name);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        rclvGpaDes = (RecyclerView) findViewById(R.id.rclv_gpa_des);
        gpaDesLayoutManager = new LinearLayoutManager(this);
        rclvGpaDes.setHasFixedSize(true);
        rclvGpaDes.setLayoutManager(gpaDesLayoutManager);
        rclvGpaDes.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        new GpaDescriptionPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvGpaDes, false);
        presenter.showLoading(this, emptyView);
        presenter.getGpaDescription();
    }

    private void initAdapter() {
        roots = new ArrayList<>();
        gpaDesAdapter = new TreeRecyclerAdapter();
        GpaDesTreeItems = new ArrayList<>();
        emptyWrapper = new EmptyWrapper<>(gpaDesAdapter);
        rclvGpaDes.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        mSuspensionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeItem item;
                if (mClickPos == 0 && mCurrentPosition != 0) {
                    item = gpaDesAdapter.getItemManager().getItem(mClickPos);

                } else {
                    item = gpaDesAdapter.getItemManager().getItem(mCurrentPosition);
                }
                if (item instanceof TreeItemGroup) {
                    if (((TreeItemGroup) item).isExpand()) {
                        ((TreeItemGroup) item).setExpand(false);
                        ((TreeItemGroup) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_arrow_down);
                    } else {
                        ((TreeItemGroup) item).setExpand(true);
                        ((TreeItemGroup) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_arrow_right);
                    }
                }
            }
        });


        rclvGpaDes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollPos = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int count = gpaDesAdapter.getItemCount();
                if (count > 0 && scrollPos < count) {
                    TreeItem item = gpaDesAdapter.getItemManager().getItem(scrollPos);
                    if (gpaDesAdapter.getItemManager() != null
                            && item instanceof TreeItemGroup) {
                        View view = gpaDesLayoutManager.findViewByPosition(scrollPos + 1);
                        if (view != null) {
                            if (view.getTop() < mSuspensionHeight) {
                                mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                            } else {
                                mSuspensionBar.setY(0);
                            }
                            //记录跟节点的position
                            mCurrentPosition = scrollPos;
                        }
                    }
                }
                if (scrollPos != gpaDesLayoutManager.findFirstVisibleItemPosition()) {
                    if (!roots.contains(mCurrentPosition)) {
                        roots.add(mCurrentPosition);
                    }
                    scrollPos = gpaDesLayoutManager.findFirstVisibleItemPosition();
                    //修正悬浮条内容为前一个根节点
                    if (scrollPos <= mCurrentPosition) {
                        if (roots.indexOf(mCurrentPosition) - 1 >= 0) {
                            mCurrentPosition = roots.get(roots.indexOf(mCurrentPosition) - 1);
                        }
                        if (scrollPos >= 1) {
                            scrollPos -= 1;
                        }
                    }
                    mSuspensionBar.setY(0);
                    updateSuspensionBar();
                }
                if (dy == 0) {
                    mSuspensionBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }
        });
    }


    private void updateSuspensionBar() {
        if (mCurrentPosition < gpaDesAdapter.getItemCount()) {
            TreeItem item;
            if (mClickPos == 0 && mCurrentPosition != 0) {
                item = gpaDesAdapter.getItemManager().getItem(mClickPos);

            } else {
                item = gpaDesAdapter.getItemManager().getItem(mCurrentPosition);
            }
            if (item instanceof TreeItemGroup) {
                if (item.getData() instanceof GpaDesInfo) {
                    tvName.setText(((GpaDesInfo) item.getData()).getName());
                }
                if (((TreeItemGroup) item).isExpand()) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                    iv_more.setImageResource(R.drawable.ic_arrow_down_gray);
                } else {
                    mSuspensionBar.setVisibility(View.INVISIBLE);
                    iv_more.setImageResource(R.drawable.ic_arrow_right);
                }
            }
        }
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


    public void showGpaDes(List<GpaDesInfo> data) {
        GpaDesTreeItems.clear();
        GpaDesTreeItems.addAll(ItemFactory.createTreeItemList(data, GpaDesOneTreeItemParent.class, null));
        gpaDesAdapter.setDatas(GpaDesTreeItems);
    }


    @Override
    public void setPresenter(GpaDescriptionContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showGpaDescription(List<GpaCalculationDetail> data) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            if (data != null && data.size() >= 0) {
                showGpaDes(GpaDesInfo.creatData(data));
                rclvGpaDes.setAdapter(gpaDesAdapter);
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getGpaDescription();
    }
}
