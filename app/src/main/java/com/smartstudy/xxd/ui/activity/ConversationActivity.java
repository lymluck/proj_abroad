package com.smartstudy.xxd.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.manager.TeacherInfoManager;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.BitmapUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.IMUtils;
import com.smartstudy.commonlib.utils.RongUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.ui.fragment.XxdConversationFragment;

import java.io.File;
import java.util.List;
import java.util.Locale;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongMessageItemLongClickActionManager;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.MessageItemLongClickAction;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import me.kareluo.imaging.IMGEditActivity;

import static me.kareluo.imaging.IMGEditActivity.REQUEST_EDIT;
import static me.kareluo.imaging.IMGEditActivity.REQUEST_SHARE;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends UIActivity implements RongIM.OnSendMessageListener, RongIM.ConversationClickListener {
    private TextView tvTitle;
    private TextView tvCompany;
    private String targeId;

    private XxdConversationFragment fragment;
    private WeakHandler mHandler;
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_conversation);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void initViewAndData() {
        tvTitle = findViewById(R.id.topdefault_centertitle1);
        tvCompany = findViewById(R.id.tv_company);
        Uri uri = getIntent().getData();
        tvTitle.setText(uri.getQueryParameter("title"));
        targeId = uri.getQueryParameter("targetId");
        mConversationType = Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        showFragment(mConversationType);
                        mHandler.sendEmptyMessageDelayed(2, 300);
                        break;
                    case 2:
                        setTvCompany();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        enterFragment();
    }

    private void enterFragment() {
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            String cacheToken = (String) SPCacheUtils.get("imToken", "");
            if (!TextUtils.isEmpty(cacheToken)) {
                //登录融云IM
                RongIM.connect(cacheToken, IMUtils.getConnectCallback());
                mHandler.sendEmptyMessageDelayed(1, 300);
            } else {
                startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            }
        } else {
            setTvCompany();
            showFragment(mConversationType);
        }
    }

    private void showFragment(Conversation.ConversationType mConversationType) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = new XxdConversationFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                    .appendQueryParameter("targetId", targeId).build();

            fragment.setUri(uri);
            transaction.add(R.id.rong_content, fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    private void setTvCompany() {
        RongIM.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, targeId, 0, 100, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                String company = null;
                for (Message message : messages) {
                    String extra = RongUtils.getMsgExtra(message.getContent());
                    JSONObject obj_msg = JSON.parseObject(extra);
                    if (obj_msg != null && obj_msg.containsKey("title")) {
                        company = obj_msg.getString("company");
                        break;
                    }
                }
                if (TextUtils.isEmpty(company)) {
                    TeacherInfoManager.getInstance().getTeacherInfo(targeId, tvCompany);
                } else {
                    tvCompany.setVisibility(View.VISIBLE);
                    tvCompany.setText(company);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    @Override
    public void initEvent() {
        this.findViewById(R.id.topdefault_rightbutton1).setOnClickListener(this);
        this.findViewById(R.id.topdefault_leftbutton1).setOnClickListener(this);
        RongIM.getInstance().setSendMessageListener(this);
        RongIM.setConversationClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_rightbutton1:
                Intent intent = new Intent();
                intent.putExtra("id", targeId);
                intent.setClass(this, ChatDetilActivity.class);
                startActivity(intent);
                break;

            case R.id.topdefault_leftbutton1:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public Message onSend(Message message) {
        String blockIds = (String) SPCacheUtils.get("blockIds", "");
        if (blockIds.contains(message.getTargetId())) {
            message.setContent(new InformationNotificationMessage("对方账号异常，无法收到该消息"));
        }
        //获取个人信息，通过extra
        return setExtra(message);
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        return false;
    }

    private Message setExtra(Message message) {
        //按消息类型添加extra
        JSONObject object = new JSONObject();
        object.put("abroadyear", SPCacheUtils.get("aboard_time", ""));
        object.put("grade", SPCacheUtils.get("project_name", ""));
        String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        if (!TextUtils.isEmpty(targetCountryInfo)) {
            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                    = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
            object.put("country", targetCountryEntity.getName());
        }
        MessageContent messageContent = message.getContent();
        //按消息类型添加extra
        if (TextMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((TextMessage) messageContent).setExtra(object.toJSONString());
        }
        if (ImageMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((ImageMessage) messageContent).setExtra(object.toJSONString());
        }
        if (LocationMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((LocationMessage) messageContent).setExtra(object.toJSONString());
        }
        if (FileMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((FileMessage) messageContent).setExtra(object.toJSONString());
        }
        if (VoiceMessage.class.isAssignableFrom(messageContent.getClass())) {
            ((VoiceMessage) messageContent).setExtra(object.toJSONString());
        }
        return message;
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        String myId = (String) SPCacheUtils.get("imUserId", "");
        String userId = userInfo.getUserId();
        if (userId.equals(myId)) {
            Router.build("MyInfoActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).go(this);
        } else {
            Router.build("ChatDetilActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).with("id", s).go(this);
        }
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        addMsgAction(message);
        return false;
    }

    private void addMsgAction(final Message clickMsg) {
        List<MessageItemLongClickAction> messageItemLongClickActions = RongMessageItemLongClickActionManager.getInstance().getMessageItemLongClickActions();
        MessageItemLongClickAction shareAction = null;
        MessageItemLongClickAction imgAction = null;
        for (MessageItemLongClickAction clickAction : messageItemLongClickActions) {
            if (getString(R.string.rc_dialog_item_message_share).equals(clickAction.getTitle(this))) {
                shareAction = clickAction;
            }
            if (getString(R.string.rc_dialog_item_message_edit).equals(clickAction.getTitle(this))) {
                imgAction = clickAction;
            }
        }
        Class clazz = clickMsg.getContent().getClass();
        if (shareAction == null) {
            if (ImageMessage.class.isAssignableFrom(clazz) || TextMessage.class.isAssignableFrom(clazz)) {
                shareAction = textAndImageLongClickAction();
                RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(shareAction, 0);
            }
        } else {
            if (!ImageMessage.class.isAssignableFrom(clazz) && !TextMessage.class.isAssignableFrom(clazz)) {
                RongMessageItemLongClickActionManager.getInstance().removeMessageItemLongClickAction(shareAction);
            }
        }
        if (imgAction == null) {
            if (ImageMessage.class.isAssignableFrom(clazz)) {
                imgAction = imageLongClickAction();
                RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(imgAction, 1);
            }
        } else {
            if (!ImageMessage.class.isAssignableFrom(clazz)) {
                RongMessageItemLongClickActionManager.getInstance().removeMessageItemLongClickAction(imgAction);
            }
        }
    }

    private MessageItemLongClickAction textAndImageLongClickAction() {
        return (new MessageItemLongClickAction.Builder()).titleResId(io.rong.imkit.R.string.rc_dialog_item_message_share).actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
            @Override
            public boolean onMessageItemLongClick(Context context, UIMessage message) {
                //转发消息
                if (ImageMessage.class.isAssignableFrom(message.getContent().getClass())) {
                    if (((ImageMessage) message.getContent()).getLocalPath() == null && ((ImageMessage) message.getContent()).getRemoteUri() == null) {
                        ToastUtils.showToast(context, "图片已被清理");
                        return true;
                    } else {
                        handleUri(((ImageMessage) message.getContent()), "share");
                    }
                } else if (TextMessage.class.isAssignableFrom(message.getContent().getClass())) {
                    startActivityForResult(new Intent(context, MsgShareActivity.class)
                            .putExtra("content", ((TextMessage) message.getMessage().getContent()).getContent())
                            .putExtra("type", "text"), REQUEST_SHARE);
                }
                return true;
            }
        }).build();
    }

    private MessageItemLongClickAction imageLongClickAction() {
        return (new MessageItemLongClickAction.Builder()).titleResId(io.rong.imkit.R.string.rc_dialog_item_message_edit).actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
            @Override
            public boolean onMessageItemLongClick(final Context context, final UIMessage message) {
                //编辑图片
                ImageMessage msg = (ImageMessage) message.getContent();
                if (msg.getLocalPath() == null && msg.getRemoteUri() == null) {
                    ToastUtils.showToast(context, "图片已被清理");
                } else {
                    handleUri(msg, "edit");
                }
                return true;
            }
        }).build();
    }

    private void handleUri(ImageMessage msg, String action) {
        if (msg.getLocalPath() != null) {
            if (msg.getLocalPath().toString().startsWith("file")) {
                if ("share".equals(action)) {
                    startActivityForResult(new Intent(this, MsgShareActivity.class)
                            .putExtra("uri", msg.getLocalPath())
                            .putExtra("type", "image"), REQUEST_SHARE);
                } else if ("edit".equals(action)) {
                    startActivityForResult(new Intent(this, IMGEditActivity.class)
                            .putExtra("uri", msg.getLocalPath()), REQUEST_EDIT);
                }
            } else {
                handleRemoteUrl(msg.getLocalPath().toString(), action);
            }
        } else {
            if (msg.getRemoteUri() != null) {
                handleRemoteUrl(msg.getRemoteUri().toString(), action);
            }
        }
    }

    private void handleRemoteUrl(String url, final String action) {
        File file = ImageLoader.getInstance().getDiskCache().get(url);
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            if ("share".equals(action)) {
                startActivityForResult(new Intent(this, MsgShareActivity.class)
                        .putExtra("uri", uri)
                        .putExtra("type", "image"), REQUEST_SHARE);
            } else if ("edit".equals(action)) {
                startActivityForResult(new Intent(this, IMGEditActivity.class)
                        .putExtra("uri", uri), REQUEST_EDIT);
            }
        } else {
            DisplayImageUtils.displayImage(this, url, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    String savePath = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures").getAbsolutePath();
                    String fileName = "im_" + System.currentTimeMillis() + ".png";
                    //临时存储文件
                    if (BitmapUtils.saveBitmap(resource, fileName, savePath, getApplicationContext())) {
                        Uri uri = Uri.parse("file://" + savePath + File.separator + fileName);
                        if ("share".equals(action)) {
                            startActivityForResult(new Intent(ConversationActivity.this, MsgShareActivity.class)
                                    .putExtra("uri", uri)
                                    .putExtra("path", savePath + File.separator + fileName)
                                    .putExtra("type", "image"), REQUEST_SHARE);
                        } else if ("edit".equals(action)) {
                            startActivityForResult(new Intent(ConversationActivity.this, IMGEditActivity.class)
                                    .putExtra("uri", uri)
                                    .putExtra("path", savePath + File.separator + fileName), REQUEST_EDIT);
                        }
                    } else {
                        ToastUtils.showToast(ConversationActivity.this, "获取图片失败！");
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_SHARE:
                handleResult(data);
                break;
            case REQUEST_EDIT:
                handleResult(data);
                break;
            default:
                break;
        }
    }

    private void handleResult(Intent data) {
        String targetId = data.getStringExtra("targetId");
        String msgType = data.getStringExtra("type");
        String word = data.getStringExtra("word");
        if ("text".equals(msgType)) {
            String content = data.getStringExtra("content");
            sendTextMsg(targetId, content);
        } else if ("image".equals(msgType)) {
            Uri uri = data.getParcelableExtra("uri");
            String path = data.getStringExtra("path");
            sendImageMsg(targetId, uri, path);
        }
        if (!TextUtils.isEmpty(word)) {
            sendTextMsg(targetId, word);
        }
    }

    private void sendTextMsg(final String userId, String content) {
        TextMessage myTextMessage = TextMessage.obtain(content);
        Message myMessage = Message.obtain(userId, Conversation.ConversationType.PRIVATE, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                String targetId = getIntent().getData().getQueryParameter("targetId");
                if (!userId.equals(targetId)) {
                    ToastUtils.showToast(ConversationActivity.this, "已发送");
                }
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                String targetId = getIntent().getData().getQueryParameter("targetId");
                if (!userId.equals(targetId)) {
                    ToastUtils.showToast(ConversationActivity.this, "消息发送失败！");
                }
            }
        });
    }

    private void sendImageMsg(final String userId, Uri uri, final String filePath) {
        ImageMessage sendImgMsg = ImageMessage.obtain(uri, uri, false);
        RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, userId, sendImgMsg, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                String targetId = getIntent().getData().getQueryParameter("targetId");
                if (!userId.equals(targetId)) {
                    ToastUtils.showToast(ConversationActivity.this, "消息发送失败！");
                }
            }

            @Override
            public void onSuccess(Message message) {
                if (!TextUtils.isEmpty(filePath)) {
                    BitmapUtils.deleteFile(filePath);
                }
                String targetId = getIntent().getData().getQueryParameter("targetId");
                if (!userId.equals(targetId)) {
                    ToastUtils.showToast(ConversationActivity.this, "消息已发送");
                }
            }

            @Override
            public void onProgress(Message message, int i) {
            }
        });

    }
}