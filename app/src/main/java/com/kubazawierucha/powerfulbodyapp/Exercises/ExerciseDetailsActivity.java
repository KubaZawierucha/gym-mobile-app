package com.kubazawierucha.powerfulbodyapp.Exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.MuscleDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.Models.Exercise;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDetailsActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private ExerciseDAO exerciseDAO;
    private Exercise exercise;

    private TextView exerciseNameTextView;
    private TextView muscleGroupNameTextView;
    private TextView muscleSimpleNameTextView;
    private TextView muscleFormalNameTextView;
    private TextView exerciseDescriptionTextView;

    private ImageView imageView_1;
    private ImageView imageView_2;
    private ImageView imageView_3;

    private List<TextView> specificationValuesTextViews = new ArrayList<>();
    private List<EditText> specificationEditViews = new ArrayList<>();

    private Button modifySpecBtn;
    private Button saveSpecBtn;

    private ContextWrapper wrapper;
    private File downloadDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        wrapper = new ContextWrapper(getApplicationContext());
        downloadDirectory = wrapper.getDir("images", MODE_PRIVATE);

        imageView_1 = findViewById(R.id.exercise_image_view_1);
        imageView_2 = findViewById(R.id.exercise_image_view_2);
        imageView_3 = findViewById(R.id.exercise_image_view_3);

        exerciseNameTextView = findViewById(R.id.detailed_exercise_name_text_view);
        muscleGroupNameTextView = findViewById(R.id.detailed_muscle_group_text_view);
        muscleSimpleNameTextView = findViewById(R.id.main_muscle_simple_name);
        muscleFormalNameTextView = findViewById(R.id.main_muscle_formal_name);
        exerciseDescriptionTextView = findViewById(R.id.exercise_description);

        specificationValuesTextViews.add((TextView) findViewById(R.id.weight_value));
        specificationValuesTextViews.add((TextView) findViewById(R.id.series_num_value));
        specificationValuesTextViews.add((TextView) findViewById(R.id.repetitions_num_value));
        specificationValuesTextViews.add((TextView) findViewById(R.id.break_time_value));

        specificationEditViews.add((EditText) findViewById(R.id.last_weight_edit_text));
        specificationEditViews.add((EditText) findViewById(R.id.last_series_edit_text));
        specificationEditViews.add((EditText) findViewById(R.id.last_rep_edit_text));
        specificationEditViews.add((EditText) findViewById(R.id.last_break_edit_text));

        modifySpecBtn = findViewById(R.id.modify_specification_button);
        modifySpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    enableEdit();
            }
        });

        saveSpecBtn = findViewById(R.id.save_specification_button);
        saveSpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSpecification();
            }
        });


        exerciseDAO = new ExerciseDAO(this);
        Intent getIntent = getIntent();
        String exerciseNameString = getIntent.getStringExtra("exerciseName");

        boolean isFocusRequestedFromIntent = getIntent.getBooleanExtra("requestFocusModify", false);
        if (isFocusRequestedFromIntent) {
            enableEdit();
            specificationEditViews.get(0).requestFocus();
            setResult(RESULT_OK, null);
        }


        loadDataAboutCurrentExercise(exerciseNameString);
    }

    private void loadDataAboutCurrentExercise(String exerciseName) {
        exercise = exerciseDAO.getExerciseByName(exerciseName);
        MuscleDAO muscleDAO = new MuscleDAO(this);

        exerciseNameTextView.setText(exercise.getName());
        muscleGroupNameTextView.setText(exercise.getMuscleGroupName());
        String muscleFormalName = exercise.getMuscleName();
        muscleSimpleNameTextView.setText(muscleFormalName);
        muscleFormalNameTextView.setText(muscleDAO.getMuscleByFormalName(muscleFormalName).getSimpleName());
        exerciseDescriptionTextView.setText(exercise.getDescription());

        for (int i = 1; i < 4; i++) {
            String fileName = exerciseName + "_" + i;
            File currFile = new File(downloadDirectory.getAbsolutePath() + "/" + fileName);
            if (currFile.exists()) {
                if ( i == 1) {
                    imageView_1.setImageBitmap(BitmapFactory.decodeFile(downloadDirectory.getAbsolutePath() + "/" + fileName));
                } else if (i == 2) {
                    imageView_2.setImageBitmap(BitmapFactory.decodeFile(downloadDirectory.getAbsolutePath() + "/" + fileName));
                } else {
                    imageView_3.setImageBitmap(BitmapFactory.decodeFile(downloadDirectory.getAbsolutePath() + "/" + fileName));
                }
            } else {

                mProgressDialog = new ProgressDialog(ExerciseDetailsActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setTitle("Please wait");
                mProgressDialog.setMessage("We are downloading images...");

                AsyncTask mMyTask;
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.setFilename(fileName);

                if (i == 1) {
                    mMyTask = downloadTask.execute(stringToURL(exercise.getFstPicURL()));
                } else if (i == 2) {
                    mMyTask = downloadTask.execute(stringToURL(exercise.getSecPicURL()));
                } else if (i == 3) {
                    mMyTask = downloadTask.execute(stringToURL(exercise.getThdPicURL()));
                }

            }
        }

        updateSpecificationTextViews();
    }

    private void enableEdit() {
        for (int i = 0; i < specificationValuesTextViews.size(); i++) {
            specificationValuesTextViews.get(i).setVisibility(View.GONE);
            specificationEditViews.get(i).setVisibility(View.VISIBLE);
        }
        modifySpecBtn.setVisibility(View.GONE);
        saveSpecBtn.setVisibility(View.VISIBLE);
    }

    private void saveSpecification() {
        if (isDataOk()) {
            Exercise updatedExercise = new Exercise(exercise.getId());
            updatedExercise.setName(exercise.getName());
            updatedExercise.setDescription(exercise.getDescription());
            updatedExercise.setMuscleName(exercise.getMuscleName());
            updatedExercise.setMuscleGroupName(exercise.getMuscleGroupName());
            updatedExercise.setLastWeight(Integer.parseInt(specificationEditViews.get(0).getText().toString()));
            updatedExercise.setLastSeriesNumber(Integer.parseInt(specificationEditViews.get(1).getText().toString()));
            updatedExercise.setLastRepetitionsNumber(Integer.parseInt(specificationEditViews.get(2).getText().toString()));
            updatedExercise.setLastBreakTime(Integer.parseInt(specificationEditViews.get(3).getText().toString()));

            boolean result = exerciseDAO.updateExercise(updatedExercise);

            if (result) {
                exercise = exerciseDAO.getExerciseByName(updatedExercise.getName());
                updateSpecificationTextViews();
                for (int i = 0; i < specificationValuesTextViews.size(); i++) {
                    specificationValuesTextViews.get(i).setVisibility(View.VISIBLE);
                    specificationEditViews.get(i).setVisibility(View.GONE);
                }
                modifySpecBtn.setVisibility(View.VISIBLE);
                saveSpecBtn.setVisibility(View.GONE);
            }
        }
    }

    private boolean isDataOk() {
        if (areEditTextsEmpty()) {
            return false;
        }

        double weight = Integer.parseInt(specificationEditViews.get(0).getText().toString());
        if (weight < 0 || weight >= 500) {
            specificationEditViews.get(0).setError("Weight must be at least 0 and lower than 500!");
            specificationEditViews.get(0).requestFocus();
            return false;
        }

        int series = Integer.parseInt(specificationEditViews.get(1).getText().toString());
        if (series <= 0 || series >= 20) {
            specificationEditViews.get(1).setError("Number of series must be bigger than 0 and lower than 20!");
            specificationEditViews.get(1).requestFocus();
            return false;
        }

        int rep = Integer.parseInt(specificationEditViews.get(2).getText().toString());
        if (rep <= 0 || rep >= 50) {
            specificationEditViews.get(2).setError("Number of repetitions must be bigger than 0 and lower than 50!");
            specificationEditViews.get(2).requestFocus();
            return false;
        }

        int breakTime = Integer.parseInt(specificationEditViews.get(3).getText().toString());
        if (breakTime < 0 || breakTime >= 300) {
            specificationEditViews.get(3).setError("Break time must be at least 0 and lower than 300!");
            specificationEditViews.get(3).requestFocus();
            return false;
        }

        return true;
    }

    private boolean areEditTextsEmpty() {
        for (EditText editText: specificationEditViews) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError("Value cannot be empty!");
                editText.requestFocus();
                return true;
            }
        }
        return false;
    }

    private void updateSpecificationTextViews() {
        specificationValuesTextViews.get(0).setText(Integer.toString(exercise.getLastWeight()));
        specificationValuesTextViews.get(1).setText(Integer.toString(exercise.getLastSeriesNumber()));
        specificationValuesTextViews.get(2).setText(Integer.toString(exercise.getLastRepetitionsNumber()));
        specificationValuesTextViews.get(3).setText(Integer.toString(exercise.getLastBreakTime()));
    }

    private void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    protected URL stringToURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Uri saveImageToInternalStorage(Bitmap bitmap, String fileName) {
        File file = new File(downloadDirectory, fileName);
        try {
            OutputStream stream;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.parse(file.getAbsolutePath());
    }

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        private String filename;
        void setFilename(String name) {
            this.filename = name;
        }

        protected void onPreExecute() {
            mProgressDialog.show();
        }

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            mProgressDialog.dismiss();
            if (result != null) {
                Uri imageInternalUri = saveImageToInternalStorage(result, filename);
                ImageView imageView = null;
                if (filename.charAt(filename.length() - 1) == '1') {
                    imageView = findViewById(R.id.exercise_image_view_1);
                } else if (filename.charAt(filename.length() - 1) == '2') {
                    imageView = findViewById(R.id.exercise_image_view_2);
                } else if (filename.charAt(filename.length() - 1) == '3') {
                    imageView = findViewById(R.id.exercise_image_view_3);
                }

                if (imageView != null) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imageInternalUri.getPath()));
                }
            }
        }
    }
}
