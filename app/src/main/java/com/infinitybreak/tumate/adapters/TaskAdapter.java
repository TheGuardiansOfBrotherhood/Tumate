package com.infinitybreak.tumate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.infinitybreak.tumate.R;
import com.infinitybreak.tumate.entities.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context mContext;
    private View.OnClickListener mListenerPlay;
    private View.OnClickListener mListenerStop;
    private ArrayList<Task> mTasks = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;
        public TextView mTextViewEstimated;
        public TextView mTextViewRealized;
        public ImageButton mImageButtonPlay;
        public ImageButton mImageButtonStop;

        public ViewHolder(View view) {
            super(view);

            mTextViewName = (TextView) view.findViewById(R.id.text_view_name);
            mTextViewEstimated = (TextView) view.findViewById(R.id.text_view_estimated);
            mTextViewRealized = (TextView) view.findViewById(R.id.text_view_realized);
            mImageButtonPlay = (ImageButton) view.findViewById(R.id.image_button_play);
            mImageButtonStop = (ImageButton) view.findViewById(R.id.image_button_stop);
        }
    }

    public TaskAdapter(Context context, ArrayList<Task> tasks,
                       View.OnClickListener listenerPlay, View.OnClickListener listenerStop) {
        mContext = context;
        mTasks = tasks;
        mListenerPlay = listenerPlay;
        mListenerStop = listenerStop;
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
        holder.mTextViewEstimated.setText(String.valueOf(task.getEstimated()));
        holder.mTextViewRealized.setText(String.valueOf(task.getRealized()));

        holder.mImageButtonPlay.setOnClickListener(mListenerPlay);
        holder.mImageButtonStop.setOnClickListener(mListenerStop);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
