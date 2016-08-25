package idv.ron.gesturedemo;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Integer> colorList;
    private int index;
    private RelativeLayout relativeLayout;
    private TextView tvMessage;
    private LinearLayout linearLayout;
    private GestureLibrary gestureLibrary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initColorList();
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLibrary.load()) {
            finish();
        }
        findViews();
    }

    private void initColorList() {
        colorList = new ArrayList<>();
        colorList.add(Color.RED);
        colorList.add(Color.GRAY);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.CYAN);
    }

    private void findViews() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        GestureOverlayView govColor = (GestureOverlayView) findViewById(R.id.govColor);

        linearLayout.setBackgroundColor(colorList.get(index));

        govColor.addOnGesturePerformedListener(new OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                if (predictions == null || predictions.size() <= 0) {
                    tvMessage.setText("cannot recognize your gesture; make a gesture again");
                    return;
                }
                String gestureName = predictions.get(0).name;
                double gestureScore = predictions.get(0).score;
                String text = String.format(" name: %s %n score: %.1f", gestureName, gestureScore);
                tvMessage.setText(text);
                switch (gestureName) {
                    case "swipe_left":
                        if (gestureScore >= 30) {
                            index++;
                            if (index >= colorList.size()) {
                                index = 0;
                            }
                            linearLayout.setBackgroundColor(colorList.get(index));
                        }
                        break;
                    case "swipe_right":
                        if (gestureScore >= 30) {
                            index--;
                            if (index < 0) {
                                index = colorList.size() - 1;
                            }
                            linearLayout.setBackgroundColor(colorList.get(index));
                        }
                        break;
                    case "check":
                        if (gestureScore >= 3) {
                            relativeLayout.setBackgroundColor(Color.DKGRAY);
                        }
                        break;
                    case "circle":
                        if (gestureScore >= 3) {
                            relativeLayout.setBackgroundColor(Color.WHITE);
                        }
                        break;
                }
            }
        });
    }
}
