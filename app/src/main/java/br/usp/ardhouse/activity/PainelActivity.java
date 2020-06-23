package br.usp.ardhouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

import br.usp.R;
import br.usp.ardhouse.progress.CustomProgressBar;

public class PainelActivity extends AppCompatActivity {

    CustomProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_painel);
    }

    ;

    //startAnimation();
    private void startAnimation() {
        ProgressBarAnimation localProgressBarAnimation = new ProgressBarAnimation(0.0F, 75.0F);
        localProgressBarAnimation.setInterpolator(new OvershootInterpolator(0.5F));
        localProgressBarAnimation.setDuration(4000L);
        pb.startAnimation(localProgressBarAnimation);
    }

    private class ProgressBarAnimation extends Animation {
        private float from;
        private float to;

        public ProgressBarAnimation(float from, float to) {
            this.from = from;
            this.to = to;
        }

        protected void applyTransformation(float paramFloat, Transformation paramTransformation) {
            super.applyTransformation(paramFloat, paramTransformation);
            float f = this.from + paramFloat * (this.to - this.from);
            pb.setProgress((int) f);
        }
    }
}
