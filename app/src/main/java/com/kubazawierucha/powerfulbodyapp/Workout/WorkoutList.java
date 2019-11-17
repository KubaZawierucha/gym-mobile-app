package com.kubazawierucha.powerfulbodyapp.Workout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.WorkoutDay;

import java.util.ArrayList;
import java.util.List;

public class WorkoutList extends ArrayAdapter {

    private final Activity context;
    private final List<WorkoutDay> workoutDays;

    public WorkoutList(Activity context, List<WorkoutDay> workoutDays) {
        super(context, R.layout.single_workout_list_item, workoutDays);
        this.context = context;
        this.workoutDays = workoutDays;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_workout_list_item, null, true);
        TextView txtDate = rowView.findViewById(R.id.workout_date_text_view);
        TextView txtMuscleGroup = rowView.findViewById(R.id.workout_muscle_group_text_view);
        txtDate.setText(workoutDays.get(position).getDate());
        txtMuscleGroup.setText(workoutDays.get(position).getExercisesNames().get(0));
        if (workoutDays.get(position).getExercisesNames().size() > 1) {
            TextView threeDots = rowView.findViewById(R.id.workout_list_dots);
            threeDots.setText("...");
            threeDots.setVisibility(View.VISIBLE);
        }
        return rowView;
    }
}
