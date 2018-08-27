package com.smartstudy.xxd.ui.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.ui.customview.audio.AudioRecorder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.TextBrHandle;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.Answerer;
import com.smartstudy.xxd.entity.QaCommenInfo;
import com.smartstudy.xxd.ui.activity.MyInfoActivity;

import org.greenrobot.eventbus.EventBus;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author yqy
 * @date on 2018/6/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QaDetailTwoTreeItem extends TreeItem<Answerer.Comments> {
    AnimationDrawable animationDrawable;
    private AudioRecorder audioRecorder;
    private ImageView imageView;
    private boolean isPlaying = false;
    private String askName;
    private String askUserId;
    private boolean isMine;

    @Override
    protected int initLayoutId() {
        audioRecorder = AudioRecorder.getInstance();
        isMine = (boolean) SPCacheUtils.get("isMine", false);
        askName = SPCacheUtils.get("askName", "").toString();
        askUserId = SPCacheUtils.get("askId", "").toString();
        return R.layout.item_qa_detail_answer;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder) {
        final TextView tvDetail = viewHolder.getView(R.id.tv_detail_answer);
        if (data.getCommentType().equals("subQuestion")) {
            String content = data.getContent() != null ? data.getContent() : "";
            String strEnd = "\n点击查看基本信息";
            if ("complete_request_info".equals(data.getActionType())) {
                // 查看基本信息
                tvDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isMine) {
                            tvDetail.getContext().startActivity(new Intent(tvDetail.getContext(), MyInfoActivity.class)
                                .putExtra("from", "editInfo")
                                .putExtra("questionId", data.getQuestionId())
                                .putExtra("commentId", data.getAtCommentId()));
                        } else {
                            tvDetail.getContext().startActivity(new Intent(tvDetail.getContext(), MyInfoActivity.class)
                                .putExtra("from", "seeInfo")
                                .putExtra(TITLE, askName + "的基本信息")
                                .putExtra("id", askUserId));
                        }
                    }
                });
                SpannableString spannableString = new SpannableString(content + strEnd);
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
                tvDetail.setText(spannableString);
                tvDetail.setBackgroundResource(R.drawable.bg_complete_info);
            } else {
                String strPre = "追问 @" + data.getAnswerName();
                content = ": " + content;
                String question = "<font color='#FF9C08'>" + strPre + "</font>" + content;
                tvDetail.setText(Html.fromHtml(TextBrHandle.parseContent(question)));
                tvDetail.setBackgroundResource(0);
                tvDetail.setPadding(0, 0, 0, 0);
            }
            viewHolder.getView(R.id.ll_voice).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_answer_again).setVisibility(View.GONE);
        } else {
            if (data.getVoiceUrl() != null) {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": " + (data.getContent() == null ? "" : data.getContent());
                tvDetail.setText(Html.fromHtml(TextBrHandle.parseContent(answer)));
                viewHolder.getView(R.id.ll_voice).setVisibility(View.VISIBLE);
                viewHolder.setText(R.id.tv_voice_time, data.getVoiceDuration());
                tvDetail.setBackgroundResource(0);
                tvDetail.setPadding(0, 0, 0, 0);
            } else {
                if ("request_info".equals(data.getActionType()) && isMine) {
                    tvDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvDetail.getContext().startActivity(new Intent(tvDetail.getContext(), MyInfoActivity.class)
                                .putExtra("from", "editInfo")
                                .putExtra("questionId", data.getQuestionId())
                                .putExtra("commentId", data.getId()));
                        }
                    });
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
                    tvDetail.setText(spannableString);
                    tvDetail.setBackgroundResource(R.drawable.bg_request_info);
                } else {
                    String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": " + (data.getContent() == null ? "" : data.getContent());
                    tvDetail.setText(Html.fromHtml(TextBrHandle.parseContent(answer)));
                    viewHolder.getView(R.id.ll_voice).setVisibility(View.GONE);
                    tvDetail.setBackgroundResource(0);
                    tvDetail.setPadding(0, 0, 0, 0);
                }
            }

            if (isMine) {
                if (data.isAnswerAgain()) {
                    viewHolder.getView(R.id.tv_answer_again).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.tv_answer_again).setVisibility(View.GONE);
                }
            } else {
                viewHolder.getView(R.id.tv_answer_again).setVisibility(View.GONE);
            }
        }
        if (getItemManager() != null && getItemManager().getItemPosition(this)
            == getItemManager().getItemPosition(getParentItem()) + getParentItem().getChildCount()) {
            // 尾部分隔符
            viewHolder.getView(R.id.rlyt_comment).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.getRatingScore())) {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.VISIBLE);
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
            if (isMine) {
                if (TextUtils.isEmpty(data.getRatingScore())) {
                    viewHolder.getView(R.id.rlyt_comment).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
                }
            } else {
                viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
            }
            viewHolder.getView(R.id.v_last).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.line_content).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.rlyt_comment).setVisibility(View.GONE);
            viewHolder.getView(R.id.ll_comment_detail).setVisibility(View.GONE);
            viewHolder.getView(R.id.v_last).setVisibility(View.GONE);
            viewHolder.getView(R.id.line_content).setVisibility(View.VISIBLE);
        }

        //对老师的评价
        viewHolder.getView(R.id.rlyt_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new QaCommenInfo(data.getQuestionId(), data.getAnsewerId()));
            }
        });
        viewHolder.getView(R.id.view_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new QaCommenInfo(data.getQuestionId(), data.getAnsewerId()));
            }
        });
        viewHolder.setText(R.id.tv_time, data.getCreateTimeText());
        viewHolder.getView(R.id.tv_answer_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(data);
            }
        });

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


    //点击了条目后，重置上一条目录的播放状态
    public void setVoiceState() {
        if (isPlaying) {
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            if (audioRecorder != null) {
                if (audioRecorder.isPlaying()) {
                    audioRecorder.playStop();
                }
            }
            isPlaying = false;
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }
}

