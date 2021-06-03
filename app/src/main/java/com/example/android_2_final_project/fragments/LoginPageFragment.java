package com.example.android_2_final_project.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_2_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageFragment extends Fragment {

    public interface LoginListener {
        void onLoginSuccess();
    }

    private LoginListener loginListener;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button mLoginBtn;
    private TextInputEditText mEmailEt;
    private TextInputEditText mPasswordEt;
    private ImageView mLogo;
    private Animation animSlide;
    private Animation progressBarSlideDown;
    private Button mNewUserBtn;
    private ProgressBar mProgressBar;

    private TextWatcher mEmailWatcher;
    private TextWatcher mPasswordWatcher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            loginListener = (LoginListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("Activity must implement LoginListener interface");
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBarSlideDown = AnimationUtils.loadAnimation(getContext(), R.anim.progress_slide_down);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login_page, container, false);

        mEmailEt = rootView.findViewById(R.id.login_page_username_edit_text);
        mPasswordEt = rootView.findViewById(R.id.login_page_password_edit_text);
        mLoginBtn = rootView.findViewById(R.id.login_page_login_btn);
        mLogo = rootView.findViewById(R.id.login_logo);
        mNewUserBtn = rootView.findViewById(R.id.login_page_new_user_btn);
        animSlide = AnimationUtils.loadAnimation(getContext(), R.anim.car_slide_left);
        mLogo.startAnimation(animSlide);
        mProgressBar = rootView.findViewById(R.id.login_page_progress_bar);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners(view);

        mEmailWatcher = setInputWatcher(mEmailEt);
        mPasswordWatcher = setInputWatcher(mPasswordEt);

        mEmailEt.addTextChangedListener(mEmailWatcher);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mEmailEt != null && mPasswordEt != null) {

            mEmailEt.removeTextChangedListener(mEmailWatcher);
            mEmailEt.removeTextChangedListener(mPasswordWatcher);
            mEmailEt.setText("");
            mPasswordEt.setText("");
        }
    }

    private void setListeners(View view) {

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailEt.getText().toString();
                String password = mPasswordEt.getText().toString();

                if (!email.trim().isEmpty() && !password.trim().isEmpty()) {

                    mProgressBar.startAnimation(progressBarSlideDown);
                    mProgressBar.setVisibility(View.VISIBLE);

                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            hideProgressBar();

                            if (loginListener != null && task.isSuccessful()) {
                                Snackbar.make(view, getString(R.string.login_success), Snackbar.LENGTH_SHORT).show();
                                loginListener.onLoginSuccess();
                            }
                            else if (loginListener != null) {
                                Snackbar.make(view, getString(R.string.login_failed), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
              else{
                    Snackbar.make(view, getString(R.string.provide_email_and_password), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to CreateUserFragment
            }
        });


    }

    private void hideProgressBar() {
        mProgressBar.clearAnimation();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private TextWatcher setInputWatcher(TextInputEditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    editText.setError("This field is required");

                }
                else {
                    editText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
