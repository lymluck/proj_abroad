package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.customview.audio.AudioRecorder;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
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
public class QaDetailAdapter extends RecyclerView.Adapter<QaDetailAdapter.MyViewHolder> {

    private Context mContext;

    private List<Answerer> answerers;

    private String askName;

    private boolean isMine;

    AnimationDrawable animationDrawable;

    private Uri isPlayingUri;

    private ImageView imageView;

    int positionOnclick = -1;

    private AudioRecorder audioRecorder;

    private boolean isPlaying = false;

    private FirstAnswerAgainOnclickListener firstAnswerAgainOnclickListener;

    private QadetailAnswerItemAdapter.AnswerAgainOnclickListener answerAgainOnclickListener;

    private TeacherInfoGetOnclickListener teacherInfoGetOnclickListener;

    public void setAnswers(List<Answerer> answerers, String askName, boolean ismine) {
        if (this.answerers != null) {
            this.answerers.clear();
        }
        audioRecorder = AudioRecorder.getInstance();
        this.answerers = answerers;
        this.askName = askName;
        this.isMine = ismine;
        this.notifyDataSetChanged();
    }

    public QaDetailAdapter(Context context) {
        this.mContext = context;
        EventBus.getDefault().register(this);//订阅
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_qa_detail, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Answerer entity = (Answerer) answerers.get(position);

        holder.tv_answer_time.setText(entity.getCreateTimeText());
        DisplayImageUtils.formatPersonImgUrl(mContext, entity.getCommenter().getAvatar(), holder.ivAssignee);
        holder.tv_assignee.setText(entity.getCommenter().getName());
        holder.tvAnswer.setText(entity.getContent());

        if (entity.getVoiceUrl() != null) {
            holder.ll_voice.setVisibility(View.VISIBLE);
            holder.tv_voice_time.setText(entity.getVoiceDuration());
            holder.tvAnswer.setVisibility(View.GONE);
        } else {
            holder.ll_voice.setVisibility(View.GONE);
            holder.tvAnswer.setVisibility(View.VISIBLE);
        }

        holder.ll_answer_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teacherInfoGetOnclickListener != null) {
                    teacherInfoGetOnclickListener.teacherInfoGetOnclickListener(entity);
                }
            }
        });


        holder.ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ItemOnClick("firstItem"));
                animationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.bg_voice_receive);
                holder.iv_voice.setImageDrawable(animationDrawable);
                if (isPlayingUri == null) {
                    if (audioRecorder != null) {
                        isPlaying = true;
                        if (animationDrawable != null && !audioRecorder.isPlaying()) {
                            animationDrawable.start();
                            audioRecorder.playByUri(mContext, entity.getVoiceUrl());
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
                            audioRecorder.playByUri(mContext, entity.getVoiceUrl());
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
                        audioRecorder.playByUri(mContext, entity.getVoiceUrl());
                    }

                }

                audioRecorder.playComplete(new AudioRecorder.PlayComplete() {
                    @Override
                    public void playComplete() {
                        if (audioRecorder != null) {
                            animationDrawable.stop();
                            audioRecorder.playStop();
                            holder.iv_voice.setImageResource(R.drawable.sound_icon);
                        }
                    }
                });
                positionOnclick = position;
                isPlayingUri = entity.getVoiceUrl();
                imageView = v.findViewById(R.id.iv_voice);
            }
        });

        holder.tv_answer_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstAnswerAgainOnclickListener != null) {
                    firstAnswerAgainOnclickListener.answerAgainOnclickListener(entity);
                }
            }
        });
        if (isMine) {
            if (entity.isAnswerAgain()) {
                holder.rl_answer_again.setVisibility(View.VISIBLE);
            } else {
                holder.rl_answer_again.setVisibility(View.GONE);
            }
        } else {
            holder.rl_answer_again.setVisibility(View.GONE);
        }


        QadetailAnswerItemAdapter qadetailAnswerItemAdapter = new QadetailAnswerItemAdapter(mContext);
        qadetailAnswerItemAdapter.setAnswerAgainOnclickListener(new QadetailAnswerItemAdapter.AnswerAgainOnclickListener() {
            @Override
            public void answerAgainOnclick(Answerer.Comments comments) {
                if (answerAgainOnclickListener != null) {
                    answerAgainOnclickListener.answerAgainOnclick(comments);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.rvDetailAnswer.setLayoutManager(linearLayoutManager);

        holder.rvDetailAnswer.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_home_search).margin(DensityUtils.dip2px(64), 0).build());

        holder.rvDetailAnswer.setAdapter(qadetailAnswerItemAdapter);
        if (entity.getComments() != null && entity.getComments().size() > 0) {
            qadetailAnswerItemAdapter.setComments(entity.getComments(), entity.getCommenter().getName(), askName, isMine);
            holder.v_line.setVisibility(View.VISIBLE);
        } else {
            holder.v_line.setVisibility(View.GONE);
        }
        holder.rvDetailAnswer.setVisibility(View.VISIBLE);
        /////////////////////////////////////////////////////

    }

    @Override
    public int getItemCount() {
        return answerers == null ? 0 : answerers.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAssignee;
        TextView tv_answer_time;
        TextView tv_assignee;
        TextView tvAnswer;
        RecyclerView rvDetailAnswer;
        TextView tv_answer_again;
        LinearLayout ll_voice;
        ImageView iv_voice;
        TextView tv_voice_time;
        LinearLayout ll_answer_person;
        RelativeLayout rl_answer_again;
        LinearLayout ll_item;
        View v_line;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivAssignee = (ImageView) itemView.findViewById(R.id.iv_assignee);
            tv_answer_time = (TextView) itemView.findViewById(R.id.tv_answer_time);
            tv_assignee = (TextView) itemView.findViewById(R.id.tv_assignee);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            rvDetailAnswer = itemView.findViewById(R.id.rv_answer_detail);
            tv_answer_again = itemView.findViewById(R.id.tv_answer_again);
            ll_voice = itemView.findViewById(R.id.ll_voice);
            iv_voice = itemView.findViewById(R.id.iv_voice);
            tv_voice_time = itemView.findViewById(R.id.tv_voice_time);
            ll_answer_person = itemView.findViewById(R.id.ll_answer_person);
            rl_answer_again = itemView.findViewById(R.id.rl_answer_again);
            ll_item = itemView.findViewById(R.id.ll_item);
            v_line = itemView.findViewById(R.id.v_line);
        }
    }


    public void setAnswerAgainOnclickListener(QadetailAnswerItemAdapter.AnswerAgainOnclickListener answerAgainOnclickListener) {
        this.answerAgainOnclickListener = answerAgainOnclickListener;
    }


    public void setFirstAnswerAgainOnclickListener(FirstAnswerAgainOnclickListener firstAnswerAgainOnclickListener) {
        this.firstAnswerAgainOnclickListener = firstAnswerAgainOnclickListener;
    }

    public interface FirstAnswerAgainOnclickListener {
        void answerAgainOnclickListener(Answerer answerer);
    }

    public interface TeacherInfoGetOnclickListener {
        void teacherInfoGetOnclickListener(Answerer answerer);
    }

    public void setTeacherInfoGetOnclickListener(TeacherInfoGetOnclickListener teacherInfoGetOnclickListener) {
        this.teacherInfoGetOnclickListener = teacherInfoGetOnclickListener;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ItemOnClick event) {
        if (event.getItemWhere().equals("secondItem") || event.getItemWhere().equals("Qa")) {
            isPlayingUri = null;
            isPlaying = false;
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
            if (audioRecorder.isPlaying()) {
                audioRecorder.playStop();
            }
            if (imageView != null) {
                imageView.setImageResource(R.drawable.sound_icon);
            }
        }
    }

}
