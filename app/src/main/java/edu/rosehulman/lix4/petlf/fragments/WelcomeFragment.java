package edu.rosehulman.lix4.petlf.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.rosehulman.lix4.petlf.ConstantUser;
import edu.rosehulman.lix4.petlf.R;


public class WelcomeFragment extends Fragment {
    private Button signinButton;
    private Button signupButton;

    private WFCallBack mWFCallBack;


    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        signinButton = (Button) view.findViewById(R.id.signin_button);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWFCallBack.showSignInUpDialog(true);
            }
        });
        signupButton = (Button) view.findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWFCallBack.showSignInUpDialog(false);
            }
        });

        if (!ConstantUser.hasUser()) {
            controlButtons(false);
        } else {
            controlButtons(true);
        }
        return view;
    }

    public void controlButtons(boolean b) {
        if (b) {
            signupButton.setVisibility(View.INVISIBLE);
            signinButton.setVisibility(View.INVISIBLE);
        } else {
            signupButton.setVisibility(View.VISIBLE);
            signinButton.setVisibility(View.VISIBLE);
        }
    }


    public interface WFCallBack {
        void showSignInUpDialog(boolean switsh);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountFragment.AFCallBack) {
            mWFCallBack = (WFCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AFCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWFCallBack = null;
    }

}
