package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.RankTypeInfo;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.TreeRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.BaseRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.TreeHeaderAndFootWapper;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.RankTypeContract;
import com.smartstudy.xxd.mvp.presenter.RankTypePresenter;
import com.smartstudy.xxd.ui.adapter.RankTypeOneTreeItemParent;

import java.util.ArrayList;
import java.util.List;

@Route("RankTypeActivity")
public class RankTypeActivity extends UIActivity implements RankTypeContract.View {

    private LinearLayoutManager rankTypeLayoutManager;
    private TreeHeaderAndFootWapper<TreeItem> mHeaderAdapter;
    private RecyclerView rclv_ranktype;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private TreeRecyclerAdapter rankTypeAdapter;
    private View searchView;
    private View emptyView;
    private LinearLayout mSuspensionBar;
    private ImageView iv_more;
    private ImageView iv_type;


    private RankTypeContract.Presenter rtP;
    private ArrayList<TreeItem> rankTypeTreeItems;
    private WeakHandler mHandler;
    private int mCurrentPosition = 0;
    private int mClickPos = 0;
    private int mSuspensionHeight;
    private ArrayList<Integer> roots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_type);
        StatisticUtils.actionEvent(this, "11_B_rank_list");
    }

    @Override
    protected void onDestroy() {
        if (rtP != null) {
            rtP = null;
        }
        if (rclv_ranktype != null) {
            rclv_ranktype.removeAllViews();
            rclv_ranktype = null;
        }
        if (rankTypeTreeItems != null) {
            rankTypeTreeItems.clear();
            rankTypeTreeItems = null;
        }
        if (roots != null) {
            roots.clear();
            roots = null;
        }
        super.onDestroy();
        removeActivity(CommonSearchActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(getString(R.string.rank_type));
        if (getIntent().getExtras() != null) {
            addActivity(this);
        }
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        mSuspensionBar = (LinearLayout) findViewById(R.id.layout_title);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        iv_type = (ImageView) findViewById(R.id.iv_type);
        rclv_ranktype = (RecyclerView) findViewById(R.id.rclv_ranktype);
        rankTypeLayoutManager = new LinearLayoutManager(this);
        rclv_ranktype.setHasFixedSize(true);
        rclv_ranktype.setLayoutManager(rankTypeLayoutManager);
        rclv_ranktype.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        new RankTypePresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_ranktype, false);
        rtP.showLoading(this, emptyView);
        rtP.getRankType();
    }

    private void initAdapter() {
        roots = new ArrayList<>();
        rankTypeAdapter = new TreeRecyclerAdapter();
        mHeaderAdapter = new TreeHeaderAndFootWapper<>(rankTypeAdapter);
        rankTypeTreeItems = new ArrayList<>();
        emptyWrapper = new EmptyWrapper<>(mHeaderAdapter);
        searchView = mInflater.inflate(R.layout.layout_search_btn, null);
        emptyWrapper = new EmptyWrapper<>(mHeaderAdapter);
        rclv_ranktype.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rclv_ranktype.scrollBy(0, searchView.getHeight());
                        break;
                    case ParameterUtils.MSG_WHAT_SMOOTH:
                        rclv_ranktype.smoothScrollBy(0, msg.arg1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticUtils.actionEvent(RankTypeActivity.this, "11_A_search_btn");
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(RankTypeActivity.this, CommonSearchActivity.class);
                if (ParameterUtils.MYSCHOOL_FLAG.equals(getIntent().getStringExtra("flag"))) {
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.MYSCHOOL_FLAG);
                } else {
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.RANKTYPE_FLAG);
                }
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(RankTypeActivity.this, searchTop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, compat.toBundle());
                } else {
                    startActivity(toSearch);
                }
            }
        });
        mSuspensionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeItem item;
                if (mClickPos == 0 && mCurrentPosition != 0) {
                    item = rankTypeAdapter.getItemManager().getItem(mClickPos);

                } else {
                    item = rankTypeAdapter.getItemManager().getItem(mCurrentPosition);
                }
                if (item instanceof TreeItemGroup) {
                    if (((TreeItemGroup) item).isExpand()) {
                        ((TreeItemGroup) item).setExpand(false);
                        ((TreeItemGroup) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_arrow_down_gray);
                        StatisticUtils.actionEvent(RankTypeActivity.this, "11_A_row_expand_btn");
                    } else {
                        ((TreeItemGroup) item).setExpand(true);
                        ((TreeItemGroup) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_home_arrow_rg);
                        StatisticUtils.actionEvent(RankTypeActivity.this, "11_A_row_shrink_btn");
                    }
                }
            }
        });
        mHeaderAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ViewHolder viewHolder, BaseItem baseItem, int itemPosition) {
                if (baseItem instanceof TreeItemGroup) {
                    if (!((TreeItemGroup) baseItem).isExpand()) {
                        mSuspensionBar.setVisibility(View.INVISIBLE);
                    }
                    mClickPos = itemPosition;
                    rclv_ranktype.scrollToPosition(0);
                    Message msg = Message.obtain();
                    msg.what = ParameterUtils.MSG_WHAT_SMOOTH;
                    View view_line = viewHolder.itemView.findViewById(R.id.view_line);
                    if (view_line.getVisibility() == View.VISIBLE) {
                        msg.arg1 = viewHolder.itemView.getTop() + view_line.getHeight();
                    } else {
                        msg.arg1 = viewHolder.itemView.getTop();
                    }
                    mHandler.sendMessageAtTime(msg, 200);
                } else {
                    if (baseItem.getData() instanceof RankTypeInfo.RankingsEntity) {
                        StatisticUtils.actionEvent(RankTypeActivity.this, "11_A_specific_rank_btn");
                        RankTypeInfo.RankingsEntity info = (RankTypeInfo.RankingsEntity) baseItem.getData();
                        Intent touk = new Intent(RankTypeActivity.this, RankActivity.class);
                        touk.putExtra("title", info.getYear() + info.getType() + info.getTitle());
                        touk.putExtra("id", info.getId());
                        startActivity(touk);
                    }
                }
            }
        });
        final TextView tv_type_name = (TextView) findViewById(R.id.tv_type_name);
        //实现悬浮停靠效果
        rclv_ranktype.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollPos = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int count = rankTypeAdapter.getItemCount();
                if (count > 0 && scrollPos < count) {
                    TreeItem item = rankTypeAdapter.getItemManager().getItem(scrollPos);
                    if (rankTypeAdapter.getItemManager() != null
                            && item instanceof TreeItemGroup) {
                        View view = rankTypeLayoutManager.findViewByPosition(scrollPos + 1);
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
                if (scrollPos != rankTypeLayoutManager.findFirstVisibleItemPosition()) {
                    if (!roots.contains(mCurrentPosition)) {
                        roots.add(mCurrentPosition);
                    }
                    scrollPos = rankTypeLayoutManager.findFirstVisibleItemPosition();
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
                    updateSuspensionBar(tv_type_name);
                }
                //修正第一个item悬浮停靠的显示
                if (Utils.getScollYDistance(rclv_ranktype) > searchView.getHeight()) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                } else {
                    mSuspensionBar.setVisibility(View.INVISIBLE);
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
        updateSuspensionBar(tv_type_name);
    }

    private void updateSuspensionBar(TextView tv_type_name) {
        if (mCurrentPosition < rankTypeAdapter.getItemCount()) {
            TreeItem item;
            if (mClickPos == 0 && mCurrentPosition != 0) {
                item = rankTypeAdapter.getItemManager().getItem(mClickPos);

            } else {
                item = rankTypeAdapter.getItemManager().getItem(mCurrentPosition);
            }
            if (item instanceof TreeItemGroup) {
                if (item.getData() instanceof RankTypeInfo) {
                    tv_type_name.setText(((RankTypeInfo) item.getData()).getGroupName());
                    DisplayImageUtils.displayCircleImage(this, ((RankTypeInfo) item.getData()).getGroupIcon(), iv_type);
                }
                if (((TreeItemGroup) item).isExpand()) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                    iv_more.setImageResource(R.drawable.ic_arrow_down_gray);
                } else {
                    mSuspensionBar.setVisibility(View.INVISIBLE);
                    iv_more.setImageResource(R.drawable.ic_home_arrow_rg);
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

    @Override
    public void setPresenter(RankTypeContract.Presenter presenter) {
        if (presenter != null) {
            this.rtP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showRankTypes(List<RankTypeInfo> data, int count) {
        if (rtP != null) {
            rtP.setEmptyView(emptyView);
            rankTypeTreeItems.clear();
            rankTypeTreeItems.addAll(ItemFactory.createTreeItemList(data, RankTypeOneTreeItemParent.class, null));
            rankTypeAdapter.setDatas(rankTypeTreeItems);
            mHeaderAdapter.addHeaderView(searchView);
            rclv_ranktype.setAdapter(mHeaderAdapter);
            searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    searchView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    rclv_ranktype.scrollBy(0, searchView.getHeight());
                }
            });
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        rtP.showLoading(this, emptyView);
        rtP.getRankType();
    }
}
