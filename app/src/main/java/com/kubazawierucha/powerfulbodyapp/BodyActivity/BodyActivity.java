package com.kubazawierucha.powerfulbodyapp.BodyActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.kubazawierucha.powerfulbodyapp.R;

public class BodyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.body_fragment_container, new FrontBodyFragment());
        fragmentTransaction.commit();
    }

}
