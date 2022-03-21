package ru.geekbrains.notes.ui.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import ru.geekbrains.notes.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragmentExit extends androidx.fragment.app.DialogFragment {
    public static final String TAG = "MyDialog";
    private int gradeItem = -1;

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = requireActivity();
        return new AlertDialog.Builder(activity).setTitle("Выход!")
                .setMessage("Вы действительно хотите выйти?")
                .setPositiveButton("Да", ((dialogInterface, i) ->
                        exit(activity)))
                .setNeutralButton("Нет", null).create();
    }

    void exit(Activity activity) {
        String[] grade = getResources().getStringArray(R.array.grade);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Пожалуста оцените приложение по пятибальной системе:").setSingleChoiceItems(grade, gradeItem,
                (dialogInterface, i) ->
                        gradeItem = i).setPositiveButton("Ок",
                (dialogInterface, i) -> {
                    if (gradeItem == -1) {
                        Toast.makeText(activity, "Не выбрана оценка!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, String.format("Спасибо за оценку приложения!"), Toast.LENGTH_LONG).show();
                        Toast.makeText(activity, "Приложение закрыто", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                }).create().show();
    }

}