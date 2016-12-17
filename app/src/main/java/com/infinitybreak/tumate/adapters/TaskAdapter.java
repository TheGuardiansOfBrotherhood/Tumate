package com.infinitybreak.tumate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinitybreak.tumate.R;
import com.infinitybreak.tumate.entities.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context mContext;
    private View.OnClickListener mListener;
    private ArrayList<Task> mTasks = new ArrayList<>();
    private boolean mIsStarting;
    private Integer mPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;
        public TextView mTextViewEstimated;
        public TextView mTextViewRealized;
        public ImageButton mImageButtonPlayOrStop;
        public RelativeLayout mRelativeLayoutTask;

        public ViewHolder(View view) {
            super(view);

            mTextViewName = (TextView) view.findViewById(R.id.text_view_name);
            mTextViewEstimated = (TextView) view.findViewById(R.id.text_view_estimated);
            mTextViewRealized = (TextView) view.findViewById(R.id.text_view_realized);
            mImageButtonPlayOrStop = (ImageButton) view.findViewById(R.id.image_button_play_or_stop);
            mRelativeLayoutTask = (RelativeLayout) view.findViewById(R.id.relative_layout_task);
        }
    }

    public TaskAdapter(Context context, ArrayList<Task> tasks, View.OnClickListener listener,
                       boolean isStarting, Integer position) {
        mContext = context;
        mTasks = tasks;
        mListener = listener;
        mIsStarting = isStarting;
        mPosition = position;

    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);

        holder.mTextViewName.setText(task.getName());
        holder.mTextViewEstimated.setText(String.valueOf(task.getEstimated()).concat(" P"));
        holder.mTextViewRealized.setText(String.valueOf(task.getRealized()).concat(" P"));

        if (mIsStarting && mPosition == position) {
            holder.mImageButtonPlayOrStop.setImageResource(R.mipmap.ic_stop);
            holder.mImageButtonPlayOrStop.setOnClickListener(mListener);
            holder.mImageButtonPlayOrStop.setTag(position);
        } else {
            if (!mIsStarting) {
                holder.mImageButtonPlayOrStop.setOnClickListener(mListener);
                holder.mImageButtonPlayOrStop.setTag(position);
            }
            holder.mImageButtonPlayOrStop.setImageResource(R.mipmap.ic_play_arrow);
        }

        if (mPosition == position) {
            holder.mRelativeLayoutTask.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.mTextViewRealized.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.mTextViewEstimated.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.mTextViewName.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.mRelativeLayoutTask.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackground));
            holder.mTextViewRealized.setTextColor(mContext.getResources().getColor(R.color.colorButtonTint));
            holder.mTextViewEstimated.setTextColor(mContext.getResources().getColor(R.color.colorButtonTint));
            holder.mTextViewName.setTextColor(mContext.getResources().getColor(R.color.colorButtonTint));
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
