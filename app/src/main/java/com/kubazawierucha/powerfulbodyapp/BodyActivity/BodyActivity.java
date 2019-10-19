package com.kubazawierucha.powerfulbodyapp.BodyActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.SingleExerciseActivity;

public class BodyActivity extends AppCompatActivity implements View.OnTouchListener{

    boolean isFront = true;
    String muscleGroupToSend = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        final Button reverseButton = findViewById(R.id.rev_button);
        final ImageView imageView = findViewById(R.id.body_front_img);
        final ImageView imageViewMask = findViewById(R.id.body_front_img_mask);

        if (imageView != null) {
            imageView.setOnTouchListener(this);
        }

        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFront) {
                    imageView.setImageResource(R.drawable.body_screen_reverse);
                    imageViewMask.setImageResource(R.drawable.body_screen_reverse_mask);
                } else {
                    imageView.setImageResource(R.drawable.body_screen_front);
                    imageViewMask.setImageResource(R.drawable.body_screen_front_mask);
                }
                isFront = !isFront;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handledHere = false;
        final int action = event.getAction();
        final int evX = (int) event.getX();
        final int evY = (int) event.getY();
        ImageView imageView = v.findViewById(R.id.body_front_img);
        if (imageView == null) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                handledHere = true;
                break;

            case MotionEvent.ACTION_UP:
                int touchColor = getHotspotColor(R.id.body_front_img_mask, evX, evY);
                ColorTool ct = new ColorTool();
                int tolerance = 25;

                if (ct.closeMatch(Color.RED, touchColor, tolerance)) {
                    muscleGroupToSend = "Chest";
                    Toast.makeText(this, "Chest", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.BLUE, touchColor, tolerance)) {
                    muscleGroupToSend = "Front Arms";
                    Toast.makeText(this, "Front Arms", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.YELLOW, touchColor, tolerance)) {
                    muscleGroupToSend = "Forearms";
                    Toast.makeText(this, "Forearms", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.rgb(0, 204, 0), touchColor, tolerance)) {
                    muscleGroupToSend = "ABS";
                    Toast.makeText(this, "ABS", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.rgb(102, 0, 153), touchColor, tolerance)) {
                    muscleGroupToSend = "Front Legs";
                    Toast.makeText(this, "Front Legs", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.rgb(255, 102, 102), touchColor, tolerance)) {
                    muscleGroupToSend = "Back Arms";
                    Toast.makeText(this, "Back Arms", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.rgb(153, 0, 0), touchColor, tolerance)) {
                    muscleGroupToSend = "Shoulders";
                    Toast.makeText(this, "Shoulders", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.rgb(77, 77, 77), touchColor, tolerance)) {
                    muscleGroupToSend = "Back";
                    Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
                }
                else if (ct.closeMatch(Color.rgb(102, 51, 0), touchColor, tolerance)) {
                    muscleGroupToSend = "Back Legs";
                    Toast.makeText(this, "Back Legs", Toast.LENGTH_SHORT).show();
                }

                handledHere = true;

                if (muscleGroupToSend != null) {
                    Intent singleExerciseActivity = new Intent(getApplicationContext(), SingleExerciseActivity.class);
                    singleExerciseActivity.putExtra("name", muscleGroupToSend);
                    muscleGroupToSend = null;
                    startActivity(singleExerciseActivity);
                }
                break;
        }


        return handledHere;
    }

    public int getHotspotColor(int hotspotId, int x, int y) {
        ImageView img = findViewById(hotspotId);
        if (img == null) {
            Log.d("BodyActivity", "Hotspot image not found");
            return 0;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Drawable bgDrawable = img.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(canvas);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            img.draw(canvas);

            return bitmap.getPixel(x, y);
        }

    }
}
