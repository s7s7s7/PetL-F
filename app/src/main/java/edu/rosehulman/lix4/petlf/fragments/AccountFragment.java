package edu.rosehulman.lix4.petlf.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.rosehulman.lix4.petlf.R;

public class AccountFragment extends Fragment {

    private AFCallBack mAFCallBack;
    private Button mLogoutButton;


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button myPostsButton = (Button) view.findViewById(R.id.button_myposts);
        mLogoutButton = (Button) view.findViewById(R.id.button_logout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAFCallBack.setNavigationId(R.id.navigation_home);
                mAFCallBack.signOut();
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content, new WelcomeFragment());
//                ft.commit();
            }
        });
        Button contactUsButton = (Button) view.findViewById(R.id.button_contact_us);
        return view;
    }

    public void controlAButton(boolean b) {
        if (b) {
            mLogoutButton.setVisibility(View.VISIBLE);
        } else {
            mLogoutButton.setVisibility(View.INVISIBLE);
        }
    }

    public interface AFCallBack {
        void setNavigationId(int id);

        void signOut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AFCallBack) {
            mAFCallBack = (AFCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AFCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAFCallBack = null;
    }

}
