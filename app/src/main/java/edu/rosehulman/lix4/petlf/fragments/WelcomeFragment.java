package edu.rosehulman.lix4.petlf.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.rosehulman.lix4.petlf.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {
    private Button signinButton;
    private Button signupButton;


    private WFCallBack mWFCallBack;


    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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
        return view;
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
