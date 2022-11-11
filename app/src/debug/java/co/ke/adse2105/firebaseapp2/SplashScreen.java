package co.ke.adse2105.firebaseapp2;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

    //Thread for the animation
    Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Call the method to animate the splash screen then load the login screen
        StartSplashAnimation();


    }

    private void StartSplashAnimation() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout layout = findViewById(R.id.activity_splash_lin_lay);
        layout.clearAnimation();
        layout.startAnimation(anim); //First animation alters the linear layout visibility over one second

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView imageView = findViewById(R.id.activity_splash_iv_logo);
        imageView.clearAnimation();
        imageView.startAnimation(anim); //Second animation moves the logo from bellow screen upwards

        //Instantiate and run the thread
        splashThread = new Thread() {
            @Override
            public void run() {

                try {
                    int waited = 0;
                    //Splash screen pause time
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }
                    //Set the activity to be loaded once the splash screen animation is over
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent); //Launch the login screen
                    SplashScreen.this.finish();  //End or finish the splash screen
                } catch (InterruptedException ie) {
                    ie.getLocalizedMessage();
                } finally {
                    SplashScreen.this.finish();
                }
            }

            ;

        };

        //Start execute the thread
        splashThread.start();

    }

    @Override
    public  void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


}