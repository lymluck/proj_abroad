package com.smartstudy.xxd.ui.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.ui.customview.audio.AudioRecorder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.Answerer;
import com.smartstudy.xxd.entity.QaCommenInfo;
import com.smartstudy.xxd.ui.activity.MyInfoActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailOneTreeItemParent extends TreeItemGroup<Answerer> {
    private AnimationDrawable animationDrawable;
    private AudioRecorder audioRecorder;
    private boolean isPlaying = false;
    private ImageView imageView;
    private boolean isMine;

    @Override
    protected int initLayoutId() {
        audioRecorder = AudioRecorder.getInstance();
        isMine = (boolean) SPCacheUtils.get("isMine", false);
        return R.layout.item_qa_detail;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        //是否是自己的问答题目
        viewHolder.setText(R.id.tv_answer_time, data.getCreateTimeText());
        final ImageView ivAvatar = viewHolder.getView(R.id.iv_assignee);
        ivAvatar.setTag(data.getCommenter().getAvatar());
        DisplayImageUtils.displayPersonImage(BaseApplication.appContext, data.getCommenter().getAvatar(), new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                if (data.getCommenter().getAvatar().equals(ivAvatar.getTag())) {
                    ivAvatar.setImageDrawable(drawable);
                }
            }
        });
        viewHolder.setText(R.id.tv_assignee, data.getCommenter().getName());
        final TextView tvAnswer = viewHolder.getView(R.id.tv_answer);
        tvAnswer.setText(data.getContent());
        viewHolder.getView(R.id.iv_v).setVisibility(data.getCommenter().isSchoolCertified()
            ? View.VISIBLE : View.GONE);
        LinearLayout llVoice = viewHolder.getView(R.id.ll_voice);
        if (data.getVoiceUrl() != null) {
            //如果有录音
            llVoice.setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_voice_time, data.getVoiceDuration());
            tvAnswer.setVisibility(View.GONE);
        } else {
            llVoice.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.VISIBLE);
        }

        //老师的头像点击事件
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setFrom("image");
                EventBus.getDefault().post(data);
            }
        });
        //追问
        viewHolder.getView(R.id.tv_answer_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setFrom("again_answer");
                EventBus.getDefault().post(data);
            }
        });

        //对老师的评价
        viewHolder.getView(R.id.rlyt_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new QaCommenInfo(data.getQuestionId(), data.getId()));
            }
        });

        viewHolder.getView(R.id.view_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new QaCommenInfo(data.getQuestionId(), data.getId()));
            }
        });
        if (isMine) {
            if (data.isAnswerAgain()) {
                viewHolder.getView(R.id.rl_answer_again).setVisibility(View.VISIBLE);
            } else {
                viewHolder.getView(R.id.rl_answer_again).setVisibility(View.GONE);
            }
            if ("request_info".equals(data.getActionType())) {
                // 完善个人信息
                SpannableString spannableString = new SpannableString(data.getContent() + "\n点击完善信息");
                final int len = data.getContent().length();
                spannableString.setSpan(new AbsoluteSizeSpan(14, true), len,
                    spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#078CF1")),
                    len, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new LineHeightSpan() {
                    @Override
                    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
                        if (start >= len) {
                            fm.ascent -= DensityUtils.dip2px(8f);
                        }
                    }
                }, len, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvAnswer.setText(spannableString);
                tvAnswer.setBackgroundResource(R.drawable.bg_request_info);
            } else {
                tvAnswer.setBackgroundResource(0);
                tvAnswer.setPadding(0, 0, 0, 0);
            }
            tvAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvAnswer.getContext().startActivity(new Intent(tvAnswer.getContext(), MyInfoActivity.class)
                        .putExtra("from", "editInfo")
                        .putExtra("questionId", data.getQuestionId())
                        .putExtra("commentId", data.getId()));
                }
            });
        } else {
            viewHolder.getView(R.id.rl_answer_again).setVisibility(View.GONE);
            viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
            tvAnswer.setBackgroundResource(0);
            tvAnswer.setPadding(0, 0, 0, 0);
        }

        //下面没有追问的情况下，评论的显示逻辑
        if (data.getComments() == null || data.getComments().size() == 0) {
            if (TextUtils.isEmpty(data.getRatingScore())) {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
                if (isMine) {
                    viewHolder.getView(R.id.rlyt_comment).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
                }
            } else {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.VISIBLE);
                viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
                RatingBar rbCourseRate = viewHolder.getView(R.id.rb_course_rate);
                rbCourseRate.setStar(TextUtils.isEmpty(data.getRatingScore())
                    ? 0f : Float.parseFloat(data.getRatingScore()));
                if (TextUtils.isEmpty(data.getRatingComment())) {
                    viewHolder.getView(R.id.tv_comment).setVisibility(View.GONE);
                } else {
                    viewHolder.getView(R.id.tv_comment).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_comment, data.getRatingComment());
                }
            }
        } else {
            viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
            viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
        }

        if (viewHolder.getView(R.id.ll_comment_detail).getVisibility() == View.GONE &&
            viewHolder.getView(R.id.rlyt_comment).getVisibility() == View.GONE) {
            viewHolder.getView(R.id.view_last).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.view_last).setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected List<? extends BaseItem> initChildsList(Answerer data) {
        return ItemFactory.createTreeItemList(handleAnswerName(data), QaDetailTwoTreeItem.class, this);
    }


    @Override
    public void onExpand(boolean isCollapseOthers) {
        isCollapseOthers = true;
        super.onExpand(isCollapseOthers);
    }

    public void voiceOnclick(ViewHolder viewHolder) {
        if (data.getVoiceUrl() == null) {
            return;
        }
        animationDrawable = (AnimationDrawable) BaseApplication.appContext.getResources().getDrawable(R.drawable.bg_voice_receive);
        final ImageView ivVoice = viewHolder.getView(R.id.iv_voice);
        ivVoice.setImageDrawable(animationDrawable);
        if (animationDrawable != null && !audioRecorder.isPlaying()) {
            isPlaying = true;
            animationDrawable.start();
            audioRecorder.playByUri(BaseApplication.appContext, data.getVoiceUrl());
        }
        audioRecorder.playComplete(new AudioRecorder.PlayComplete() {
            @Override
            public void playComplete() {
                if (audioRecorder != null) {
                    isPlaying = false;
                    animationDrawable.stop();
                    audioRecorder.playStop();
                    ivVoice.setImageResource(R.drawable.sound_icon);
                }
            }
        });
        imageView = viewHolder.getView(R.id.iv_voice);
    }

    public void setVoiceState() {
        if (isPlaying) {
            isPlaying = false;
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            if (audioRecorder != null) {
                if (audioRecorder.isPlaying()) {
                    audioRecorder.playStop();
                }
            }
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }

    private List<Answerer.Comments> handleAnswerName(Answerer answerer) {
        if (answerer != null) {
            if (answerer.getComments() != null && answerer.getComments().size() > 0) {
                for (Answerer.Comments comments : answerer.getComments()) {
                    comments.setAnswerName(answerer.getCommenter().getName());
                    comments.setAnsewerId(answerer.getId());
                    comments.setRatingScore(answerer.getRatingScore());
                    comments.setRatingComment(answerer.getRatingComment());
                }
            }
        }
        return answerer.getComments();
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

}
