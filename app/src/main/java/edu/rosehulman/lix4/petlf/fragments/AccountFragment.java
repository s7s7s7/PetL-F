package edu.rosehulman.lix4.petlf.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.rosehulman.lix4.petlf.ConstantUser;
import edu.rosehulman.lix4.petlf.MyPostActivity;
import edu.rosehulman.lix4.petlf.R;
import edu.rosehulman.lix4.petlf.models.User;

public class AccountFragment extends Fragment {

    private static final String ARG_USER = "currentUser";
    private AFCallBack mAFCallBack;
    private Button mLogoutButton;

    private User mUser;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(User currentUser) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
//        args.putParcelable(ARG_USER, currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView emailTextView = (TextView) view.findViewById(R.id.email_display_text_view);
        Button myPostsButton = (Button) view.findViewById(R.id.button_myposts);
        mLogoutButton = (Button) view.findViewById(R.id.button_logout);
        if (ConstantUser.hasUser()) {
            emailTextView.setText(String.format(getResources().getString(R.string.email_diaplay_text), ConstantUser.currentUser.getEmail()));
            myPostsButton.setVisibility(View.VISIBLE);
            mLogoutButton.setVisibility(View.VISIBLE);
            myPostsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMyPostActivity();
                }
            });
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
        } else {
            emailTextView.setText(R.string.email_not_log_in);
            myPostsButton.setVisibility(View.INVISIBLE);
            mLogoutButton.setVisibility(View.INVISIBLE);
        }

        Button contactUsButton = (Button) view.findViewById(R.id.button_contact_us);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUs();
            }
        });
        return view;
    }

    private void contactUs() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "lix4@rose-hulman.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "From me");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello Li,");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void startMyPostActivity() {
        Intent myPostActivity = new Intent(getContext(), MyPostActivity.class);
        startActivity(myPostActivity);
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
