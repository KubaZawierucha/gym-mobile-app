package com.kubazawierucha.powerfulbodyapp.Workout;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kubazawierucha.powerfulbodyapp.Alarm.AlarmReceiver;
import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.Models.WorkoutDay;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_workout_list_item, null, true);
        TextView txtDate = rowView.findViewById(R.id.workout_date_text_view);
        TextView txtMuscleGroup = rowView.findViewById(R.id.workout_muscle_group_text_view);
        txtDate.setText(workoutDays.get(position).getDate());
        if (workoutDays.get(position).getExercisesNames().size() != 0) {
            txtMuscleGroup.setText(workoutDays.get(position).getExercisesNames().get(0));
        }

        if (workoutDays.get(position).getExercisesNames().size() > 1) {
            TextView threeDots = rowView.findViewById(R.id.workout_list_dots);
            threeDots.setText("...");
            threeDots.setVisibility(View.VISIBLE);
        }

        Button removeButton = rowView.findViewById(R.id.single_workout_list_remove_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkoutDAO workoutDAO = new WorkoutDAO(context);
                int workoutId = workoutDays.get(position).getId();
                if (!workoutDAO.deleteWorkoutDayById(workoutId)) {
                    Toast.makeText(context, "Problem with deleting workout day occurred!", Toast.LENGTH_LONG).show();
                } else {
                    Intent refresh = new Intent(context, WorkoutListActivity.class);
                    Intent pendingIntent = new Intent(context, AlarmReceiver.class);

                    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, workoutId,
                            pendingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmIntent.cancel();

                    context.startActivity(refresh);
                    context.finish();
                }
            }
        });

        return rowView;
    }
}
