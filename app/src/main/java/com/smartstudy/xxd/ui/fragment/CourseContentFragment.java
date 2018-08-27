package com.smartstudy.xxd.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.PlayListener;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.adapter.TreeRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.BaseRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customview.WrapContentLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.commonlib.utils.NetUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseContentInfo;
import com.smartstudy.xxd.mvp.contract.CourseContentContract;
import com.smartstudy.xxd.mvp.presenter.CourseContentPresenter;
import com.smartstudy.xxd.ui.activity.CourseDetailActivity;
import com.smartstudy.xxd.ui.adapter.Course3TreeItem;
import com.smartstudy.xxd.ui.adapter.CourseOneTreeItemParent;

import java.util.ArrayList;
import java.util.List;

public class CourseContentFragment extends BaseFragment implements CourseContentContract.View {

    private RecyclerView rclv_content;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private TreeRecyclerAdapter treeRecyclerAdapter;
    private View emptyView;
    private WrapContentLinearLayoutManager linearLayoutManager;
    private LinearLayout mSuspensionBar;
    private TextView tv_type_name;
    private ImageView iv_more;
    private CourseContentContract.Presenter courseP;
    private WeakHandler mHandler;
    private String courseId;
    private int mCurrentPosition = 0;
    private int mClickPos = 0;
    private ArrayList<Integer> roots;  //存储展开过的item位置
    private int mSuspensionHeight;
    private ArrayList<TreeItem> treeItems;
    private PlayListener listener;
    public long lastClickTime; //最近一次的点击时间
    private long nowClickTime; //当前点击播放的时间
    private String lastSectionId; //保存最近一次的sectionId
    private boolean isFirstClick = true;
    private boolean isDestroy;
    private boolean isFirstLoad = true;
    private BaseItem lastItem; //上次点击播放课程的item
    private boolean mIsRefreshing; //是否正在刷新

    //接口的实例化 必须
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PlayListener) mActivity;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_course_content;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        SPCacheUtils.remove("nowSectionId");
        courseP.showLoading(mActivity, emptyView);
        courseP.getContents(courseId);
    }

    @Override
    public void onDestroy() {
        if (!TextUtils.isEmpty(lastSectionId)) {
            courseP.recordPlayUrl(courseId, lastSectionId,
                ((CourseDetailActivity) mActivity).getPlayer().getCurrentPosition() + "", (System.currentTimeMillis() - nowClickTime) + "");
            isDestroy = true;
        }
        super.onDestroy();
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        mSuspensionBar = (LinearLayout) rootView.findViewById(R.id.layout_course_title);
        tv_type_name = (TextView) rootView.findViewById(R.id.tv_type_name);
        iv_more = (ImageView) rootView.findViewById(R.id.iv_more);
        courseId = bundle.getString("courseId");
        rclv_content = (RecyclerView) rootView.findViewById(R.id.rclv_content);
        linearLayoutManager = new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rclv_content.setLayoutManager(linearLayoutManager);
        rclv_content.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        new CourseContentPresenter(this);
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rclv_content, false);
    }

    private void initAdapter() {
        roots = new ArrayList<>();
        treeRecyclerAdapter = new TreeRecyclerAdapter();
        treeItems = new ArrayList<>();
        emptyWrapper = new EmptyWrapper<>(treeRecyclerAdapter);
        rclv_content.swapAdapter(emptyWrapper, true);
        initEvent();
    }

    public void initEvent() {
        handleMsg();
        //悬浮条点击事件
        mSuspensionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeItem item = treeRecyclerAdapter.getItemManager().getItem(mClickPos);
                if (item instanceof CourseOneTreeItemParent) {
                    if (((CourseOneTreeItemParent) item).isExpand()) {
                        ((CourseOneTreeItemParent) item).setExpand(false);
                        ((CourseOneTreeItemParent) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_home_arrow_rg);
                    } else {
                        ((CourseOneTreeItemParent) item).setExpand(true);
                        ((CourseOneTreeItemParent) item).notifyExpand();
                        iv_more.setImageResource(R.drawable.ic_arrow_down_gray);
                    }
                }
            }
        });
        itemclick();
        scrollEvent();
        //刷新过程中禁止掉滑动
        rclv_content.setOnTouchListener(
            new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsRefreshing) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        );
    }

    private void handleMsg() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_SMOOTH:
                        rclv_content.smoothScrollBy(0, msg.arg1);
                        break;
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        ((TreeItemGroup) treeRecyclerAdapter.getData(mClickPos)).setCollapseOthers(true);
                        ((TreeItemGroup) treeRecyclerAdapter.getData(mClickPos)).setExpand(true);
                        if (treeRecyclerAdapter.getData(mClickPos).getItemManager() == null) {
                            treeRecyclerAdapter.getData(mClickPos).setItemManager(treeRecyclerAdapter.getItemManager());
                        }
                        ((TreeItemGroup) treeRecyclerAdapter.getData(mClickPos)).notifyExpand();
                        //恢复视频选中位置
                        if (!isFirstLoad) {
                            sildeToLastPos();
                        } else {
                            mIsRefreshing = false;
                            //初始化SuspensionBar
                            updateSuspensionBar();
                        }
                        isFirstLoad = false;
                        break;
                    case ParameterUtils.MSG_WHAT_REPOSITION:
                        //初始化SuspensionBar
                        updateSuspensionBar();
                        int pos = msg.arg1;
                        if (pos > 3) {
                            if (mSuspensionBar.getVisibility() == View.INVISIBLE) {
                                mSuspensionBar.setVisibility(View.VISIBLE);
                            }
                            linearLayoutManager.scrollToPositionWithOffset(pos - 3, 0);
                        }
                        mIsRefreshing = false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void itemclick() {
        treeRecyclerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ViewHolder viewHolder, BaseItem baseItem, int itemPosition) {
                if (baseItem instanceof CourseOneTreeItemParent) {
                    if (baseItem.getItemManager() != null) {
                        mClickPos = baseItem.getItemManager().getRealPos();
                    }
                    if (!((CourseOneTreeItemParent) baseItem).isExpand()) {
                        mSuspensionBar.setVisibility(View.INVISIBLE);
                    }
                    rclv_content.scrollToPosition(0);
                    Message msg = Message.obtain();
                    msg.what = ParameterUtils.MSG_WHAT_SMOOTH;
                    msg.arg1 = viewHolder.itemView.getTop();
                    mHandler.sendMessageAtTime(msg, 200);
                } else if (baseItem instanceof Course3TreeItem) {
                    if (NetUtils.isConnected(mActivity)) {
                        CourseContentInfo.ChaptersEntity.SectionsEntity data = (CourseContentInfo.ChaptersEntity.SectionsEntity) baseItem.getData();
                        SPCacheUtils.put("nowSectionId", data.getId());
                        if (isFirstClick) {
                            lastClickTime = System.currentTimeMillis();
                            isFirstClick = false;
                        }
                        nowClickTime = System.currentTimeMillis();
                        if (!TextUtils.isEmpty(lastSectionId)) {
                            if (!TextUtils.isEmpty((CharSequence) SPCacheUtils.get("ticket", ""))) {
                                if (!data.getId().equals(lastSectionId)) {
                                    recordePlay(false);
                                }
                            }
                        }
                        if (lastItem != null) {
                            treeRecyclerAdapter.notifyItemChanged(treeRecyclerAdapter.getItemManager().getItemPosition((Course3TreeItem) lastItem));
                        }
                        lastItem = baseItem;
                        treeRecyclerAdapter.notifyItemChanged(treeRecyclerAdapter.getItemManager().getItemPosition((Course3TreeItem) baseItem));
                        if (!data.getId().equals(lastSectionId)) {
                            courseP.getPlayUrl(courseId, data.getId(), data.getName(), data.getLastPlayTime(), data.getLongDuration());
                        }
                    } else {
                        showTip(null, ParameterUtils.NET_ERR);
                    }
                }
            }
        });
    }

    private void scrollEvent() {
        //实现悬浮停靠效果
        rclv_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollPos = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int count = treeRecyclerAdapter.getItemCount();
                if (count > 0 && scrollPos < count && scrollPos > 0) {
                    if (treeRecyclerAdapter.getItemManager() != null) {
                        TreeItem item = treeRecyclerAdapter.getItemManager().getItem(scrollPos);
                        if (item instanceof CourseOneTreeItemParent) {
                            View view = linearLayoutManager.findViewByPosition(scrollPos + 1);
                            if (view != null) {
                                if (view.getTop() <= mSuspensionHeight) {
                                    mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                                } else {
                                    mSuspensionBar.setY(0);
                                }
                                //记录跟节点的position
                                mCurrentPosition = scrollPos;
                            }
                        }
                    }
                }
                if (scrollPos != linearLayoutManager.findFirstVisibleItemPosition()) {
                    if (!roots.contains(mCurrentPosition)) {
                        roots.add(mCurrentPosition);
                    }
                    scrollPos = linearLayoutManager.findFirstVisibleItemPosition();
                    //修正悬浮条内容为前一个根节点
                    if (scrollPos < mCurrentPosition) {
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
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }
        });
    }

    //更新悬浮条内容
    private void updateSuspensionBar() {
        if (mCurrentPosition < treeRecyclerAdapter.getItemCount()) {
            TreeItem item = treeRecyclerAdapter.getItemManager().getItem(mCurrentPosition);
            if (item instanceof CourseOneTreeItemParent) {
                if (item.getData() instanceof CourseContentInfo) {
                    tv_type_name.setText(((CourseContentInfo) item.getData()).getName());
                }
                if (((CourseOneTreeItemParent) item).isExpand()) {
                    if (mSuspensionBar.getVisibility() == View.INVISIBLE) {
                        mSuspensionBar.setVisibility(View.VISIBLE);
                    }
                    iv_more.setImageResource(R.drawable.ic_arrow_down_gray);
                } else {
                    if (mSuspensionBar.getVisibility() == View.VISIBLE) {
                        mSuspensionBar.setVisibility(View.INVISIBLE);
                    }
                    iv_more.setImageResource(R.drawable.ic_home_arrow_rg);
                }
            }
        }
    }

    @Override
    public void setPresenter(CourseContentContract.Presenter presenter) {
        if (presenter != null) {
            this.courseP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (courseP != null) {
            treeRecyclerAdapter.notifyDataSetChanged();
            if (mActivity != null) {
                ToastUtils.showToast(message);
            }
        }
    }

    @Override
    public void showContent(List<CourseContentInfo> data) {
        if (courseP != null) {
            courseP.setEmptyView(emptyView);
            if (data != null && data.size() > 0) {
//                int currentSize = treeItems.size();
                treeItems.clear();
                treeItems.addAll(ItemFactory.createTreeItemList(data, CourseOneTreeItemParent.class, null));
                if (isFirstLoad) {
                    mSuspensionBar.setVisibility(View.VISIBLE);
                    treeRecyclerAdapter.setDatas(treeItems);
                    rclv_content.swapAdapter(treeRecyclerAdapter, true);
                } else {
                    treeRecyclerAdapter.setDatas(treeItems);
                }
                if (!mIsRefreshing) {
                    mHandler.sendEmptyMessageDelayed(ParameterUtils.MSG_WHAT_REFRESH, 100);
                }
                mIsRefreshing = true;
            } else {
                emptyWrapper.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showUrl(String url, String title, String sectionId, long lastTime, long duration) {
        if (!TextUtils.isEmpty(url)) {
            lastSectionId = sectionId;
        }
        listener.startPlay(url, title, lastTime, duration, sectionId);
    }

    @Override
    public void recordSuccess() {
        //局部刷新
        if (!isDestroy) {
            refresh();
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        courseP.showLoading(mActivity, emptyView);
        courseP.getContents(courseId);
    }

    public void refresh() {
        courseP.getContents(courseId);
    }

    public void recordePlay(boolean isComplete) {
        if (isComplete) {
            nowClickTime = System.currentTimeMillis();
            if (!TextUtils.isEmpty(lastSectionId)) {
                recordePlayUrl(((CourseDetailActivity) mActivity).getPlayer().getDuration() + "");
            }
        } else {
            recordePlayUrl(((CourseDetailActivity) mActivity).getPlayer().getCurrentPosition() + "");
        }
        lastClickTime = nowClickTime;
        SPCacheUtils.put("canFreshComment", true);
    }

    //记录播放课程的进度
    private void recordePlayUrl(String duration) {
        if (mActivity != null) {
            courseP.recordPlayUrl(courseId, lastSectionId,
                duration, (nowClickTime - lastClickTime) + "");
        }
    }

    private void sildeToLastPos() {
        int pos = 0;
        String nowSectionId = (String) SPCacheUtils.get("nowSectionId", "");
        List<TreeItem> childs = ((TreeItemGroup) treeRecyclerAdapter.getData(mClickPos)).getAllChilds();
        if (childs != null) {
            for (TreeItem item : childs) {
                if (item instanceof Course3TreeItem) {
                    if (((Course3TreeItem) item).getData().getId().equals(nowSectionId)) {
                        pos = treeRecyclerAdapter.getItemManager().getItemPosition(item);
                        break;
                    }
                }
            }
        }
        Message msg = Message.obtain();
        msg.arg1 = pos;
        msg.what = ParameterUtils.MSG_WHAT_REPOSITION;
        mHandler.sendMessageDelayed(msg, 150);
    }
}
