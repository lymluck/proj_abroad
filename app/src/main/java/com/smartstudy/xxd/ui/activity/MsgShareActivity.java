package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.OnSendMsgDialogClickListener;
import com.smartstudy.commonlib.entity.ChatUserInfo;
import com.smartstudy.commonlib.mvp.contract.MsgShareContract;
import com.smartstudy.commonlib.mvp.presenter.MsgSharePresenter;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@Route("MsgShareActivity")
public class MsgShareActivity extends UIActivity implements MsgShareContract.View {

    private HeaderAndFooterWrapper<ChatUserInfo> mHeaderAdapter;
    private RecyclerView rclv_recent;
    private CommonAdapter<ChatUserInfo> chatUserAdapter;
    private View searchView;

    private List<ChatUserInfo> chatUserInfoList;
    private WeakHandler mHandler;
    private MsgShareContract.Presenter shareP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_share);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("选择");
        rclv_recent = findViewById(R.id.rclv_recent);
        rclv_recent.setHasFixedSize(true);
        rclv_recent.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        new MsgSharePresenter(this);
        shareP.getChatUsers();
    }

    private void initAdapter() {
        chatUserInfoList = new ArrayList<>();
        chatUserAdapter = new CommonAdapter<ChatUserInfo>(this, R.layout.item_chat_user_list, chatUserInfoList) {
            @Override
            protected void convert(ViewHolder holder, ChatUserInfo chatUserInfo, int position) {
                DisplayImageUtils.displayPersonImage(MsgShareActivity.this, chatUserInfo.getAvatar(), (ImageView) holder.getView(R.id.iv_avatar));
                holder.setText(R.id.tv_name, chatUserInfo.getName());
            }
        };
        mHeaderAdapter = new HeaderAndFooterWrapper<>(chatUserAdapter);
        View headView = mInflater.inflate(R.layout.header_rencent_user, null, false);
        mHeaderAdapter.addHeaderView(headView);
        searchView = headView.findViewById(R.id.searchView);
        rclv_recent.setAdapter(mHeaderAdapter);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rclv_recent.scrollBy(0, searchView.getHeight());
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转搜索
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(MsgShareActivity.this, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.RECENTUSER_FLAG);
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(MsgShareActivity.this, searchTop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, compat.toBundle());
                } else {
                    startActivity(toSearch);
                }
            }
        });
        chatUserAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //头部view占去一个position
                final ChatUserInfo chatUserInfo = chatUserInfoList.get(position - 1);
                final String msgType = getIntent().getStringExtra("type");
                if (chatUserInfo != null) {
                    if ("text".equals(msgType)) {
                        final String content = getIntent().getStringExtra("content");
                        DialogCreator.createSendTextMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(), content, new OnSendMsgDialogClickListener() {
                            @Override
                            public void onPositive(String word) {
                                //转发内容
                                setResult(RESULT_OK, new Intent()
                                        .putExtra("targetId", chatUserInfo.getId())
                                        .putExtra("type", msgType)
                                        .putExtra("content", content)
                                        .putExtra("word", word));
                                finish();
                            }

                            @Override
                            public void onNegative() {
                            }
                        });
                    } else if ("image".equals(msgType)) {
                        final Uri uri = getIntent().getParcelableExtra("uri");
                        DialogCreator.createSendImgMsgDialog(MsgShareActivity.this, chatUserInfo.getAvatar(), chatUserInfo.getName(),
                                uri.toString(), new OnSendMsgDialogClickListener() {
                                    @Override
                                    public void onPositive(String word) {
                                        //转发内容
                                        Intent data = new Intent();
                                        String path = getIntent().getStringExtra("path");
                                        if (!TextUtils.isEmpty(path)) {
                                            data.putExtra("path", path);
                                        }
                                        data.putExtra("targetId", chatUserInfo.getId());
                                        data.putExtra("type", msgType);
                                        data.putExtra("uri", uri);
                                        data.putExtra("word", word);
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }

                                    @Override
                                    public void onNegative() {
                                    }
                                });
                    }
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
        if (v.getId() == R.id.topdefault_leftbutton) {
            finish();
        }
    }

    @Override
    public void showUsers(List<ChatUserInfo> data) {
        chatUserInfoList.clear();
        chatUserInfoList.addAll(data);
        chatUserAdapter.notifyDataSetChanged();
        searchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                searchView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                rclv_recent.scrollBy(0, searchView.getHeight());
            }
        });
    }

    @Override
    public void setPresenter(MsgShareContract.Presenter presenter) {
        if (presenter != null) {
            shareP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }
}
