package com.dimas.uasandroid;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dimas.uasandroid.databinding.ActivityRegisterBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityRegisterBinding binding;

    //Declaration SqliteHelper
    SqliteHelper sqliteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        Button letsLogin = findViewById(R.id.btn_lets_login);
        Button buttonRegister = findViewById(R.id.btn_Register);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextUserName = findViewById(R.id.editTextUserName);

        sqliteHelper = new SqliteHelper(this);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    //Check in the database is there any user associated with  this email
                    if (!sqliteHelper.isEmailExists(Email)) {

                        //Email does not exist now add new user to database
                        sqliteHelper.addUser(new User(null, UserName, Email, Password));
                        Toast.makeText(getApplicationContext(), "User created successfully! Please Login ", Toast.LENGTH_LONG).show();
                        editTextUserName.setText("");
                        editTextEmail.setText("");
                        editTextPassword.setText("");
                } else {
                        //Email exists with email input provided so show error user already exist
                        Toast.makeText(getApplicationContext(), "User already exists with same email ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            //This method is used to validate input given by user
            public boolean validate() {
                boolean valid = false;

                //Get values from EditText fields
                String UserName = editTextUserName.getText().toString();
                String Email = editTextEmail.getText().toString();
                String Password = editTextPassword.getText().toString();

                //Handling validation for UserName field
                if (UserName.isEmpty()) {
                    valid = false;
                    editTextUserName.setError("Please enter valid username!");
                } else {
                    if (UserName.length() > 5) {
                        valid = true;
                        editTextUserName.setError(null);
                    } else {
                        valid = false;
                        editTextUserName.setError("Username is to short!");
                    }
                }

                //Handling validation for Email field
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    valid = false;
                    editTextEmail.setError("Please enter valid email!");
                } else {
                    valid = true;
                    editTextEmail.setError(null);
                }

                //Handling validation for Password field
                if (Password.isEmpty()) {
                    valid = false;
                    editTextPassword.setError("Please enter valid password!");
                } else {
                    if (Password.length() > 5) {
                        valid = true;
                        editTextPassword.setError(null);
                    } else {
                        valid = false;
                        editTextPassword.setError("Password is to short!");
                    }
                }


                return valid;
            }
        });

        letsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}