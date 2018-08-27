package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.smartstudy.commonlib.ui.customview.audio.AudioRecorder;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.Answerer;
import com.smartstudy.xxd.entity.ItemOnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/3/5
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class QadetailAnswerItemAdapter extends RecyclerView.Adapter<QadetailAnswerItemAdapter.MyViewHolder> {

    private Context mContext;

    private List<Answerer.Comments> mDatas;

    AnimationDrawable animationDrawable;

    private AudioRecorder audioRecorder;

    private Uri isPlayingUri;

    private String answerName;

    private String askName;

    private boolean isMine;

    int positionOnclick = -1;

    private ImageView imageView;

    private boolean isPlaying = false;

    private AnswerAgainOnclickListener answerAgainOnclickListener;


    public void setComments(List<Answerer.Comments> comments, String answerName, String askName, boolean isMine) {
        if (this.mDatas != null) {
            this.mDatas.clear();
        }
        this.mDatas = comments;
        audioRecorder = AudioRecorder.getInstance();
        this.answerName = answerName;
        this.askName = askName;
        this.isMine = isMine;
        this.notifyDataSetChanged();
    }

    public QadetailAnswerItemAdapter(Context context) {
        this.mContext = context;
        EventBus.getDefault().register(this);//订阅
    }

    @NonNull
    @Override
    public QadetailAnswerItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_qa_detail_answer, parent, false);
        MyViewHolder myViewHolderw = new MyViewHolder(view);
        return myViewHolderw;

    }

    @Override
    public void onBindViewHolder(@NonNull final QadetailAnswerItemAdapter.MyViewHolder holder, final int position) {
        final Answerer.Comments comments = mDatas.get(position);
        if (comments.getCommentType().equals("subQuestion")) {
            String question = "<font color='#FF9C08'>" + "追问 @" + answerName + "</font>" + ": " + (comments.getContent() == null ? "" : comments.getContent());
            holder.tv_detail_answer.setText(Html.fromHtml(question));
            holder.ll_voice.setVisibility(View.GONE);
            holder.answer_again.setVisibility(View.GONE);
        } else {
            if (isMine) {
                if (comments.isAnswerAgain()) {
                    holder.answer_again.setVisibility(View.VISIBLE);
                } else {
                    holder.answer_again.setVisibility(View.GONE);
                }
            } else {
                holder.answer_again.setVisibility(View.GONE);
            }
            if (comments.getVoiceUrl() != null) {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": " + (comments.getContent() == null ? "" : comments.getContent());
                holder.tv_detail_answer.setText(Html.fromHtml(answer));
                holder.ll_voice.setVisibility(View.VISIBLE);
                holder.tv_voice_time.setText(comments.getVoiceDuration());
            } else {
                String answer = "回复<font color='#078CF1'>" + " @" + askName + "</font>" + ": " + (comments.getContent() == null ? "" : comments.getContent());
                holder.tv_detail_answer.setText(Html.fromHtml(answer));
                holder.ll_voice.setVisibility(View.GONE);
            }
        }
        holder.tv_time.setText(comments.getCreateTimeText());

        holder.answer_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerAgainOnclickListener != null) {
                    answerAgainOnclickListener.answerAgainOnclick(comments);
                }
            }
        });

        holder.ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ItemOnClick("secondItem"));
                animationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.bg_voice_receive);
                holder.iv_voice.setImageDrawable(animationDrawable);
                if (isPlayingUri == null) {
                    isPlaying = true;
                    if (audioRecorder != null) {
                        isPlaying = true;
                        if (animationDrawable != null && !audioRecorder.isPlaying()) {
                            animationDrawable.start();
                            audioRecorder.playByUri(mContext, comments.getVoiceUrl());
                        } else {
                            isPlaying = false;
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            if (imageView != null) {
                                imageView.setImageResource(R.drawable.sound_icon);
                            }
                        }
                    }
                } else {
                    if (positionOnclick == position) {
                        if (isPlaying) {
                            isPlaying = false;
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            if (imageView != null) {
                                imageView.setImageResource(R.drawable.sound_icon);
                            }
                        } else {
                            isPlaying = true;
                            audioRecorder.playByUri(mContext, comments.getVoiceUrl());
                            holder.iv_voice.setImageDrawable(animationDrawable);
                            animationDrawable.start();
                        }

                    } else {
                        isPlaying = true;
                        animationDrawable.stop();
                        audioRecorder.playStop();
                        if (imageView != null) {
                            imageView.setImageResource(R.drawable.sound_icon);
                        }
                        animationDrawable.start();
                        audioRecorder.playByUri(mContext, comments.getVoiceUrl());
                    }

                }

                audioRecorder.playComplete(new AudioRecorder.PlayComplete()

                {
                    @Override
                    public void playComplete() {
                        if (audioRecorder != null) {
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            holder.iv_voice.setImageResource(R.drawable.sound_icon);
                        }
                    }
                });
                isPlayingUri = comments.getVoiceUrl();
                imageView = v.findViewById(R.id.iv_voice);
                positionOnclick = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_detail_answer;

        TextView tv_time;

        LinearLayout ll_voice;

        TextView tv_voice_time;

        ImageView iv_voice;

        LinearLayout ll_answer_again;

        private TextView answer_again;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_detail_answer = (TextView) itemView.findViewById(R.id.tv_detail_answer);
            tv_time = itemView.findViewById(R.id.tv_time);
            ll_voice = itemView.findViewById(R.id.ll_voice);
            tv_voice_time = itemView.findViewById(R.id.tv_voice_time);
            iv_voice = itemView.findViewById(R.id.iv_voice);
            ll_answer_again = itemView.findViewById(R.id.ll_answer_again);
            answer_again = itemView.findViewById(R.id.tv_answer_again);
        }
    }

    public interface AnswerAgainOnclickListener {
        void answerAgainOnclick(Answerer.Comments comments);
    }

    public void setAnswerAgainOnclickListener(AnswerAgainOnclickListener answerAgainOnclickListener) {
        this.answerAgainOnclickListener = answerAgainOnclickListener;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ItemOnClick event) {
        if (event.getItemWhere().equals("firstItem") || event.getItemWhere().equals("Qa")) {
            isPlayingUri = null;
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            positionOnclick = -1;
            if (audioRecorder.isPlaying()) {
                audioRecorder.playStop();
            }
            isPlaying = false;
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }
}
