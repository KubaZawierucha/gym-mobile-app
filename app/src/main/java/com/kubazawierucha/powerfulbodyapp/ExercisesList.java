package com.kubazawierucha.powerfulbodyapp;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExercisesList extends ArrayAdapter {

    private final Activity context;
    private final List<String> web;
    private final List<Integer> imageID;

    public ExercisesList(Activity context, List<String> web, List<Integer> imageID) {
        super(context, R.layout.single_exercise_list_item, web);
        this.context = context;
        this.web = web;
        this.imageID = imageID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_exercise_list_item, null, true);
        TextView txtTitle = rowView.findViewById(R.id.exercise_name_text_view);
        ImageView imageView = rowView.findViewById(R.id.exercise_image_view);
        txtTitle.setText(web.get(position));
        imageView.setImageResource(imageID.get(position));
        return rowView;
    }


}
