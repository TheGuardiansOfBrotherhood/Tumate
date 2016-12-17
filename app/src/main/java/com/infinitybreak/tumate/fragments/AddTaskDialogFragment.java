package com.infinitybreak.tumate.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.infinitybreak.tumate.R;
import com.infinitybreak.tumate.activities.MainActivity;
import com.infinitybreak.tumate.daos.TaskDAO;
import com.infinitybreak.tumate.entities.Task;
import com.infinitybreak.tumate.enums.TaskStatus;

public class AddTaskDialogFragment extends DialogFragment {

    private TextInputLayout mTextInputLayoutTaskName;
    private EditText mEditTextTaskName;
    private TextInputLayout mTextInputLayoutTaskEstimated;
    private EditText mEditTextTaskEstimated;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_task, null);
        mTextInputLayoutTaskName = (TextInputLayout) view.findViewById(R.id.text_input_layout_task_name);
        mEditTextTaskName = (EditText) view.findViewById(R.id.edit_text_task_name);
        mTextInputLayoutTaskEstimated = (TextInputLayout) view.findViewById(R.id.text_input_layout_task_estimated);
        mEditTextTaskEstimated = (EditText) view.findViewById(R.id.edit_text_task_estimated);

        builder.setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (formValid()) {
                            addTask(mEditTextTaskName.getText().toString(),
                                    Integer.parseInt(mEditTextTaskEstimated.getText().toString()));
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddTaskDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void addTask(String taskName, Integer taskEstimated) {
        Task task = new Task();
        task.setName(taskName);
        task.setEstimated(taskEstimated);
        task.setRealized(0);
        task.setStatus(TaskStatus.TODO);
        TaskDAO.getInstance(getActivity()).insert(task);

        ((MainActivity) getActivity()).refreshList();
    }

    private boolean formValid() {
        boolean isValid = true;
        if (mEditTextTaskName.getText().toString().trim().isEmpty()) {
            mEditTextTaskName.setText("");
            mTextInputLayoutTaskName.setError(getResources().getString(R.string.error_task_name_required));
            isValid = false;
        } else {
            mTextInputLayoutTaskName.setError("");
            mTextInputLayoutTaskName.setErrorEnabled(false);
        }
        if (mEditTextTaskEstimated.getText().toString().trim().isEmpty()) {
            mEditTextTaskEstimated.setText("");
            mTextInputLayoutTaskEstimated.setError(getResources().getString(R.string.error_task_estimated_required));
            isValid = false;
        } else {
            mTextInputLayoutTaskEstimated.setError("");
            mTextInputLayoutTaskEstimated.setErrorEnabled(false);
        }
        return isValid;
    }
}
