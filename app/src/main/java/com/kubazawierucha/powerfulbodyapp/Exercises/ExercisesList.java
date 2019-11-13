package com.kubazawierucha.powerfulbodyapp.Exercises;

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
import com.kubazawierucha.powerfulbodyapp.models.Exercise;

import java.util.List;

public class ExercisesList extends ArrayAdapter {

    private final Activity context;
    private final List<Exercise> exercise;
    private final List<Integer> imageID;

    public ExercisesList(Activity context, List<Exercise> exercise, List<Integer> imageID) {
        super(context, R.layout.single_exercise_list_item, exercise);
        this.context = context;
        this.exercise = exercise;
        this.imageID = imageID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_exercise_list_item, null, true);
        TextView txtTitle = rowView.findViewById(R.id.exercise_name_text_view);
        ImageView imageView = rowView.findViewById(R.id.exercise_image_view);
        txtTitle.setText(exercise.get(position).getName());
        imageView.setImageResource(imageID.get(position));
        return rowView;
    }


}
