package com.kubazawierucha.powerfulbodyapp.UserProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DAO.UserDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// TODO: 22.10.2019
// photo adding from phone

public class UserProfileActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private User user;

    private EditText bmiET;
    private EditText bfpET;
    private RadioGroup genderRG;
    private TextView bmiTV;
    private TextView bfpTV;
    private List<EditText> editTexts;

    private double BMI = 0;
    private double BFP = 0;
    private BMIClassifier bmiClassifier;

    private Button submitBtn;
    private Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userDAO = new UserDAO(this);

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

        initData();
    }

    private void initData() {
        // load user from db
        user = userDAO.getUserCreatedByApp();

        // if user exists
        if (user != null) {
            prepareEditFields(user);
            setCalculatedEditTextsValues();
            setGender();
            setEditTextsDisabled(editTexts);
            setCalculatedEditTexts();
            switchBodyCalculationTexts(1);
            submitBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
        } else {
            user = new User(true);
            createNewUser();
        }
    }

    // we set texts of user's body values
    private void setCalculatedEditTextsValues() {
        BMI = BodyConditionCalculator.calculateBMI(user.getHeight() / 100, user.getWeight());
        bmiClassifier = BodyConditionCalculator.classifyBMI(BMI);
        BFP = BodyConditionCalculator.calculateBFP(user.getWaist(), user.getNeck(), user.getHip(), user.getHeight(), user.getGender());
    }

    // we set texts as user's profile
    private void prepareEditFields(User user) {
        editTexts.get(0).setText(user.getName());
        editTexts.get(1).setText(String.format(Locale.US, "%d", user.getAge()));
        editTexts.get(2).setText(String.format(Locale.US, "%.2f", user.getHeight()));
        editTexts.get(3).setText(String.format(Locale.US, "%.2f", user.getWeight()));
        editTexts.get(4).setText(String.format(Locale.US, "%.2f", user.getWaist()));
        editTexts.get(5).setText(String.format(Locale.US, "%.2f", user.getNeck()));
        editTexts.get(6).setText(String.format(Locale.US, "%.2f", user.getHip()));

        if (user.getGender().equals("M")) {
            genderRG.check(R.id.male_radio_button);
        } else {
            genderRG.check(R.id.female_radio_button);
        }
    }

    private void loadValuesFromEditTexts() {
        user.setName(editTexts.get(0).getText().toString());
        user.setAge(Integer.parseInt(editTexts.get(1).getText().toString()));
        user.setHeight(Double.parseDouble(editTexts.get(2).getText().toString()));
        user.setWeight(Double.parseDouble(editTexts.get(3).getText().toString()));
        user.setWaist(Double.parseDouble(editTexts.get(4).getText().toString()));
        user.setNeck(Double.parseDouble(editTexts.get(5).getText().toString()));
        user.setHip(Double.parseDouble(editTexts.get(6).getText().toString()));
        setGender();

        setCalculatedEditTextsValues();
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
            single.setTextColor(Color.WHITE);
            hideGenderRadioButton();
            submitBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
        }
    }

    private void hideGenderRadioButton() {
        if (user.getGender().equals("M")) {
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
        if (user.getAge() <= 0 || user.getAge() >= 120) {
            editTexts.get(1).setError("Age must be bigger than 0 and lower than 120!");
            editTexts.get(1).requestFocus();
            return false;
        } else if (user.getHeight() <= 0 || user.getHeight() >= 300) {
            editTexts.get(2).setError("Height must be bigger than 0 and lower than 300!");
            editTexts.get(2).requestFocus();
            return false;
        } else if (user.getWeight() <= 0 || user.getWeight() >= 500) {
            editTexts.get(3).setError("Weight must be bigger than 0 and lower than 500!");
            editTexts.get(3).requestFocus();
            return false;
        } else if (user.getWaist() <= 0 || user.getWaist() >= 300) {
            editTexts.get(4).setError("Waist must be bigger than 0 and lower than 300!");
            editTexts.get(4).requestFocus();
            return false;
        } else if (user.getNeck() <= 0 || user.getNeck() >= 200) {
            editTexts.get(5).setError("Neck must be bigger than 0 and lower than 200!");
            editTexts.get(5).requestFocus();
            return false;
        } else if (user.getHip() <= 0 || user.getHip() >= 300){
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
                user.setGender("M");
                findViewById(R.id.female_radio_button).setVisibility(View.GONE);
                break;

            case R.id.female_radio_button:
                user.setGender("F");
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
        boolean result = userDAO.updateOrCreateUser(user, true);

        if (!result) {
            result = userDAO.updateOrCreateUser(user, false);
        }

        if (result) {
            Toast.makeText(UserProfileActivity.this, "DB updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UserProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
