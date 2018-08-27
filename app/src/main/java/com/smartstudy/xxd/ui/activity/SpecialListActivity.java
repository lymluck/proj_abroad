package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
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

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.MajorInfo;
import com.smartstudy.commonlib.entity.ProgramInfo;
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
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.SpecialListContract;
import com.smartstudy.xxd.mvp.presenter.SpecialListPresenter;
import com.smartstudy.xxd.ui.adapter.ProgramOneTreeItemParent;
import com.smartstudy.xxd.ui.adapter.SpeDataOneTreeItemParent;

import java.util.ArrayList;
import java.util.List;

public class SpecialListActivity extends UIActivity implements SpecialListContract.View {

    private RecyclerView rclv_special;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private TreeHeaderAndFootWapper<TreeItem> mHeaderAdapter;
    private TreeRecyclerAdapter treeRecyclerAdapter;
    private View emptyView;
    private View searchView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout mSuspensionBar;
    private ImageView iv_more;

    private SpecialListContract.Presenter specialP;
    private WeakHandler mHandler;
    private String flag;
    private int mCurrentPosition = 0;
    private int mClickPos = 0;
    private ArrayList<Integer> roots;
    private int mSuspensionHeight;
    private ArrayList<TreeItem> treeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_list);
        addActivity(this);
        StatisticUtils.actionEvent(this, "13_B_professional_list");
    }

    @Override
    protected void onDestroy() {
        releaseRes();
        super.onDestroy();
        removeActivity(RankActivity.class.getSimpleName());
    }

    private void releaseRes() {
        if (specialP != null) {
            specialP = null;
        }
        if (roots != null) {
            roots.clear();
            roots = null;
        }
        if (treeItems != null) {
            treeItems.clear();
            treeItems = null;
        }
        if (mHandler != null) {
            mHandler = null;
        }
        if (treeRecyclerAdapter != null) {
            treeRecyclerAdapter = null;
        }
        if (rclv_special != null) {
            rclv_special.removeAllViews();
            rclv_special.clearOnScrollListeners();
            rclv_special = null;
        }
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        mSuspensionBar = (LinearLayout) findViewById(R.id.layout_title);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        flag = data.getStringExtra("flag");
        titleView.setText(data.getStringExtra("title"));
        rclv_special = (RecyclerView) findViewById(R.id.rclv_special);
        linearLayoutManager = new LinearLayoutManager(this);
        rclv_special.setLayoutManager(linearLayoutManager);
        rclv_special.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        new SpecialListPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_special, false);
        specialP.showLoading(this, emptyView);
        specialP.getData(flag);
    }

    private void initAdapter() {
        roots = new ArrayList<>();
        treeRecyclerAdapter = new TreeRecyclerAdapter();
        mHeaderAdapter = new TreeHeaderAndFootWapper<>(treeRecyclerAdapter);
        treeItems = new ArrayList<>();
        searchView = mInflater.inflate(R.layout.layout_search_btn, null);
        emptyWrapper = new EmptyWrapper<>(mHeaderAdapter);
        rclv_special.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rclv_special.scrollBy(0, searchView.getHeight());
                        break;
                    case ParameterUtils.MSG_WHAT_SMOOTH:
                        rclv_special.smoothScrollBy(0, msg.arg1);
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
                StatisticUtils.actionEvent(SpecialListActivity.this, "13_A_professional_search_btn");
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(SpecialListActivity.this, CommonSearchActivity.class);
                if ("home".equals(flag)) {
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.SPEDATA_FLAG);
                } else {
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.PROGRAMSPE_FLAG);
                    if ("bk".equals(flag)) {
                        toSearch.putExtra("typeId", "PROG_UNDERGRADUATE");
                    } else if ("yjs".equals(flag)) {
                        toSearch.putExtra("typeId", "PROG_GRADUATE");
                    }
                }
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(SpecialListActivity.this, searchTop);
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
                    item = treeRecyclerAdapter.getItemManager().getItem(mClickPos);

                } else {
                    item = treeRecyclerAdapter.getItemManager().getItem(mCurrentPosition);
                }
                if (item instanceof TreeItemGroup) {
                    if (((TreeItemGroup) item).isExpand()) {
                        ((TreeItemGroup) item).setExpand(false);
                        ((TreeItemGroup) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_arrow_right);
                    } else {
                        ((TreeItemGroup) item).setExpand(true);
                        ((TreeItemGroup) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_arrow_down);
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
                    rclv_special.scrollToPosition(0);
                    Message msg = Message.obtain();
                    msg.what = ParameterUtils.MSG_WHAT_SMOOTH;
                    msg.arg1 = viewHolder.itemView.getTop();
                    mHandler.sendMessageAtTime(msg, 200);
                } else {
                    if ("home".equals(flag)) {
                        StatisticUtils.actionEvent(SpecialListActivity.this, "13_A_professional_cell");
                        MajorInfo.MajorsInfo.Majors data = (MajorInfo.MajorsInfo.Majors) baseItem.getData();
                        Intent toMoreDetails = new Intent(SpecialListActivity.this, ShowWebViewActivity.class);
                        toMoreDetails.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_SPE_INTRO), data.getId()));
                        toMoreDetails.putExtra("title", data.getName());
                        toMoreDetails.putExtra("url_action", "get");
                        startActivity(toMoreDetails);
                    } else {
                        ProgramInfo.ProgramsInfo data = (ProgramInfo.ProgramsInfo) baseItem.getData();
                        startActivity(new Intent(SpecialListActivity.this, CommandSchoolListActivity.class)
                                .putExtra("id", data.getId())
                                .putExtra("title", data.getName() + "推荐院校"));
                    }
                }
            }
        });
        TextView tv_type_name = findViewById(R.id.tv_type_name);
        //实现悬浮停靠效果
        scrollEvent(tv_type_name);
        updateSuspensionBar(tv_type_name);
    }

    //实现悬浮停靠效果
    private void scrollEvent(final TextView tv_type_name) {
        rclv_special.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollPos = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int count = treeRecyclerAdapter.getItemCount();
                if (count > 0 && scrollPos < count) {
                    TreeItem item = treeRecyclerAdapter.getItemManager().getItem(scrollPos);
                    if (treeRecyclerAdapter.getItemManager() != null
                            && item instanceof TreeItemGroup) {
                        View view = linearLayoutManager.findViewByPosition(scrollPos + 1);
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
                if (scrollPos != linearLayoutManager.findFirstVisibleItemPosition()) {
                    if (!roots.contains(mCurrentPosition)) {
                        roots.add(mCurrentPosition);
                    }
                    scrollPos = linearLayoutManager.findFirstVisibleItemPosition();
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
                if (Utils.getScollYDistance(rclv_special) > searchView.getHeight()) {
                    if (mCurrentPosition < treeRecyclerAdapter.getItemCount()) {
                        TreeItem item;
                        if (mClickPos == 0 && mCurrentPosition != 0) {
                            item = treeRecyclerAdapter.getItemManager().getItem(mClickPos);

                        } else {
                            item = treeRecyclerAdapter.getItemManager().getItem(mCurrentPosition);
                        }
                        if (item instanceof TreeItemGroup) {
                            if (((TreeItemGroup) item).isExpand()) {
                                if (mSuspensionBar.getVisibility() == View.INVISIBLE) {
                                    mSuspensionBar.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } else {
                    if (mSuspensionBar.getVisibility() == View.VISIBLE) {
                        mSuspensionBar.setVisibility(View.INVISIBLE);
                    }
                }
                if (dy == 0) {
                    if (mSuspensionBar.getVisibility() == View.VISIBLE) {
                        mSuspensionBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }
        });
    }

    private void updateSuspensionBar(TextView tv_type_name) {
        if (mCurrentPosition < treeRecyclerAdapter.getItemCount()) {
            TreeItem item;
            if (mClickPos == 0 && mCurrentPosition != 0) {
                item = treeRecyclerAdapter.getItemManager().getItem(mClickPos);
            } else {
                item = treeRecyclerAdapter.getItemManager().getItem(mCurrentPosition);
            }
            if (item instanceof TreeItemGroup) {
                if (item.getData() instanceof ProgramInfo) {
                    tv_type_name.setText(((ProgramInfo) item.getData()).getName());
                } else if (item.getData() instanceof MajorInfo) {
                    tv_type_name.setText(((MajorInfo) item.getData()).getName());
                }
                if (((TreeItemGroup) item).isExpand()) {
                    if (mSuspensionBar.getVisibility() == View.INVISIBLE) {
                        mSuspensionBar.setVisibility(View.VISIBLE);
                    }
                    iv_more.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    if (mSuspensionBar.getVisibility() == View.VISIBLE) {
                        mSuspensionBar.setVisibility(View.INVISIBLE);
                    }
                    iv_more.setImageResource(R.drawable.ic_arrow_right);
                }
            }
            item = null;
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
    public void setPresenter(SpecialListContract.Presenter presenter) {
        if (presenter != null) {
            this.specialP = presenter;
            presenter = null;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (specialP != null) {
            treeRecyclerAdapter.notifyDataSetChanged();
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void showSpecialData(List<MajorInfo> data) {
        if (specialP != null) {
            specialP.setEmptyView(emptyView);
            treeItems.clear();
            treeItems.addAll(ItemFactory.createTreeItemList(data, SpeDataOneTreeItemParent.class, null));
            treeRecyclerAdapter.setDatas(treeItems);
            mHeaderAdapter.addHeaderView(searchView);
            rclv_special.setAdapter(mHeaderAdapter);
            searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    searchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    rclv_special.scrollBy(0, searchView.getHeight());
                }
            });
        }
        data = null;
    }

    @Override
    public void showProgramSpecial(List<ProgramInfo> data) {
        if (specialP != null) {
            specialP.setEmptyView(emptyView);
            treeItems.clear();
            treeItems.addAll(ItemFactory.createTreeItemList(data, ProgramOneTreeItemParent.class, null));
            treeRecyclerAdapter.setDatas(treeItems);
            mHeaderAdapter.addHeaderView(searchView);
            rclv_special.setAdapter(mHeaderAdapter);
            searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    searchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    rclv_special.scrollBy(0, searchView.getHeight());
                }
            });
        }
        data = null;
    }


    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
        view = null;
    }

    @Override
    public void reload() {
        specialP.showLoading(this, emptyView);
        specialP.getData(flag);
    }
}
