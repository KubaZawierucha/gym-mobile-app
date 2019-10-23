package com.kubazawierucha.powerfulbodyapp.UserProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// TODO: 22.10.2019
// photo adding from phone

public class UserProfileActivity extends AppCompatActivity {

    private DBManager myDB;

    private EditText bmiET;
    private EditText bfpET;
    private RadioGroup genderRG;
    private TextView bmiTV;
    private TextView bfpTV;
    private List<EditText> editTexts;

    private String name;
    private int age = 0;
    private String gender = "Male";
    private double height = 0;
    private double weight = 0;
    private double waist = 0;
    private double neck = 0;
    private double hip = 0;
    private double BMI = 0;
    private double BFP = 0;
    private BMIClassifier bmiClassifier;

    private Button submitBtn;
    private Button editBtn;

    private final int USER_CREATED_BY_APP_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editTexts = new ArrayList<>();
        editTexts.add((EditText) findViewById(R.id.user_name_edit_view));
        editTexts.add((EditText) findViewById(R.id.user_age_edit_view));
        editTexts.add((EditText) findViewById(R.id.height_edit_text));
        editTexts.add((EditText) findViewById(R.id.weight_edit_text));
        editTexts.add((EditText) findViewById(R.id.waist_edit_text));
        editTexts.add((EditText) findViewById(R.id.neck_edit_text));
        editTexts.add((EditText) findViewById(R.id.hip_edit_text));

        submitBtn = findViewById(R.id.submit_btn);
        editBtn = findViewById(R.id.edit_btn);

        bmiET = findViewById(R.id.bmi_edit_text);
        bfpET = findViewById(R.id.bfp_edit_text);
        genderRG = findViewById(R.id.sex_radio_group);
        bmiTV = findViewById(R.id.bmi_text_view);
        bfpTV = findViewById(R.id.bfp_text_view);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()){
                    setGender();
                    setCalculatedEditTexts();
                    switchBodyCalculationTexts(1);
                    setEditTextsDisabled(editTexts);
                    submitBtn.setVisibility(View.GONE);
                    editBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(UserProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    saveUserToDB();
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextsEnabled();
            }
        });

        myDB = DBManager.getInstance(this);

        initData();
    }

    private void initData() {
        myDB.open();
        Cursor data = myDB.getData("User", " WHERE createdByApp = " + (USER_CREATED_BY_APP_FLAG));

        if (isUserInDB(data)) {
            loadUserFromDB(data);
            loadValuesFromEditTexts();
            setEditTextsDisabled(editTexts);
            setCalculatedEditTexts();
            switchBodyCalculationTexts(1);
            submitBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
        } else {
            createNewUser();
        }
        data.close();
    }

    private boolean isUserInDB(Cursor data) {
        return data.getCount() != 0;
    }

    private void loadUserFromDB(Cursor data) {
        data.moveToNext();
        for (int i = 0, j = 0; i < editTexts.size(); i++, j++) {
            if (j == 2) {
                j++;
            }
            editTexts.get(i).setText(data.getString(j));
        }
        if (data.getString(2).equals("M")) {
            genderRG.check(R.id.male_radio_button);
            gender = "Male";
        } else {
            genderRG.check(R.id.female_radio_button);
            gender = "Female";
        }
    }

    private void loadValuesFromEditTexts() {
        name = editTexts.get(0).getText().toString();
        age = Integer.parseInt(editTexts.get(1).getText().toString());
        height = Double.parseDouble(editTexts.get(2).getText().toString())/100;
        weight = Double.parseDouble(editTexts.get(3).getText().toString());
        waist = Double.parseDouble(editTexts.get(4).getText().toString());
        neck = Double.parseDouble(editTexts.get(5).getText().toString());
        hip = Double.parseDouble(editTexts.get(6).getText().toString());

        BMI = BodyConditionCalculator.calculateBMI(height, weight);
        bmiClassifier = BodyConditionCalculator.classifyBMI(BMI);
        BFP = BodyConditionCalculator.calculateBFP(waist, neck, hip, height*100, gender);
    }

    private void setCalculatedEditTexts() {
        bmiET.setText(String.format(Locale.US, "%.2f", BMI));
        bmiET.append(" (" + bmiClassifier.toString() + ")");

        bfpET.setText(String.format(Locale.US, "%.2f", BFP));
        bfpET.append("%");
    }

    private void switchBodyCalculationTexts(int trigger) {
        if (trigger == 0) {
            bmiTV.setVisibility(View.GONE);
            bmiET.setVisibility(View.GONE);
            bfpTV.setVisibility(View.GONE);
            bfpET.setVisibility(View.GONE);
        } else if (trigger == 1) {
            bmiTV.setVisibility(View.VISIBLE);
            bmiET.setVisibility(View.VISIBLE);
            bfpTV.setVisibility(View.VISIBLE);
            bfpET.setVisibility(View.VISIBLE);
        }
    }

    private void setEditTextsDisabled(List<EditText> et) {
        for (EditText single: et) {
            single.setEnabled(false);
            single.setTextColor(Color.BLACK);
            hideGenderRadioButton();
            submitBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
        }
    }

    private void hideGenderRadioButton() {
        if (gender.equals("Male")) {
            findViewById(R.id.female_radio_button).setVisibility(View.GONE);
        } else {
            findViewById(R.id.male_radio_button).setVisibility(View.GONE);
        }
    }

    private void createNewUser() {
        editBtn.setVisibility(View.GONE);
        submitBtn.setVisibility(View.VISIBLE);
    }

    private boolean validateInput() {
         int emptyIndex = checkIfAllFieldsAreNotEmpty();
         try {
             if (emptyIndex == -1) {
                 loadValuesFromEditTexts();
                 if (isDataOk()) {
                     return true;
                 }
             } else {
                 editTexts.get(emptyIndex).setError("Empty input! Inputs cannot start with '0'");
                 editTexts.get(emptyIndex).requestFocus();
             }
         } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
         }
         return false;
    }

    private int checkIfAllFieldsAreNotEmpty() {
        int result = -1;
        for (int i = 0; i < editTexts.size(); i++) {
            String currString = editTexts.get(i).getText().toString();
            if (TextUtils.isEmpty(currString)) {
                result = i;
                break;
            }
            if (currString.charAt(0) == '0') {
                result = i;
                break;
            }
        }
        return result;
    }

    private boolean isDataOk() {
        if (age <= 0 || age >= 120) {
            editTexts.get(1).setError("Age must be bigger than 0 and lower than 120!");
            editTexts.get(1).requestFocus();
            return false;
        } else if (height <= 0 || height >= 300) {
            editTexts.get(2).setError("Height must be bigger than 0 and lower than 300!");
            editTexts.get(2).requestFocus();
            return false;
        } else if (weight <= 0 || weight >= 500) {
            editTexts.get(3).setError("Weight must be bigger than 0 and lower than 500!");
            editTexts.get(3).requestFocus();
            return false;
        } else if (waist <= 0 || waist >= 300) {
            editTexts.get(4).setError("Waist must be bigger than 0 and lower than 300!");
            editTexts.get(4).requestFocus();
            return false;
        } else if (neck <= 0 || neck >= 200) {
            editTexts.get(5).setError("Neck must be bigger than 0 and lower than 200!");
            editTexts.get(5).requestFocus();
            return false;
        } else if (hip <= 0 || hip >= 300){
            editTexts.get(6).setError("Hip must be bigger than 0 and lower than 300!");
            editTexts.get(6).requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void setGender() {
        switch (genderRG.getCheckedRadioButtonId()) {
            case R.id.male_radio_button:
                gender = "Male";
                findViewById(R.id.female_radio_button).setVisibility(View.GONE);
                break;

            case R.id.female_radio_button:
                gender = "Female";
                findViewById(R.id.male_radio_button).setVisibility(View.GONE);
                break;
        }
    }

    private void setEditTextsEnabled() {
        for (EditText single: editTexts) {
            single.setEnabled(true);
            single.setTextColor(Color.GRAY);
        }
        editTexts.get(0).requestFocus();
        switchBodyCalculationTexts(0);
        editBtn.setVisibility(View.GONE);
        submitBtn.setVisibility(View.VISIBLE);
        findViewById(R.id.female_radio_button).setVisibility(View.VISIBLE);
        findViewById(R.id.male_radio_button).setVisibility(View.VISIBLE);
    }

    private void saveUserToDB() {
        myDB.deleteTable("User");
        boolean result = myDB.insertData("User", "'" + name + "', " + age + ", '" + gender.substring(0,1) +
                "', " + height*100 + ", " + weight + ", " + waist + ", " + neck + ", " + hip + ", " + USER_CREATED_BY_APP_FLAG + ")");
        if (result) {
            Toast.makeText(UserProfileActivity.this, "DB updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UserProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
