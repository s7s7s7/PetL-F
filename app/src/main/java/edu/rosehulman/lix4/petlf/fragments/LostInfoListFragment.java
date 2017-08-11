package edu.rosehulman.lix4.petlf.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.rosehulman.lix4.petlf.InfoListAdapter;
import edu.rosehulman.lix4.petlf.R;
import edu.rosehulman.lix4.petlf.models.Post;

public class LostInfoListFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String UID = "uid";

    // TODO: Rename and change types of parameters
    private String mType;
    private InfoListAdapter mAdapter;
    private String mUid;

    private LILCallback mLILCallback;
    private boolean mLoggedIn;

    public LostInfoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LostInfoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LostInfoListFragment newInstance(String type, String uid) {
        LostInfoListFragment fragment = new LostInfoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_PARAM1);
            mUid = getArguments().getString(UID);
            if (!mUid.equals("no user here")) {
                mLoggedIn = true;
            } else {
                mLoggedIn = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_lost_info_list, container, false);
        RecyclerView mInfoList = (RecyclerView) rootView.findViewById(R.id.info_recycler_view);
        mInfoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mInfoList.setHasFixedSize(true);
        mAdapter = new InfoListAdapter(mType, mLILCallback);
        mInfoList.setAdapter(mAdapter);

        final View fab = rootView.findViewById(R.id.fab);

        if (mLoggedIn) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.add_lost_info_dialog, null, false);
        final EditText titleEditView = (EditText) view.findViewById(R.id.title_edittext_view);
        final EditText breedEditView = (EditText) view.findViewById(R.id.breed_edittext_view);
        final EditText locationEditView = (EditText) view.findViewById(R.id.location_edittext_view);
        final EditText colorEditView = (EditText) view.findViewById(R.id.color_edittext_view);
        final EditText timeEditView = (EditText) view.findViewById(R.id.time_edittext_view);
        final EditText otherinfoEditView = (EditText) view.findViewById(R.id.otherinfo_edittext_view);
        final EditText sizeEditView = (EditText) view.findViewById(R.id.size_edittext_view);
        final TextView uploadText = (TextView) view.findViewById(R.id.uploadimage_text_view);
        final Button uploadButton = (Button) view.findViewById(R.id.uploadimage_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLILCallback.chooseImage();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new Pic here.");
        builder.setView(view);



        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleEditView.getText().toString();
                String breed = breedEditView.getText().toString();
                String size = sizeEditView.getText().toString();
                String descripion =
                        "Location: " + locationEditView.getText().toString()
                                + ". Color: " + colorEditView.getText().toString()
                                + ". Time: " + timeEditView.getText().toString()
                                + ". OtherInfomation: " + otherinfoEditView.getText().toString();
                int temp = 0;
                if (mType.equals("LOST")) {
                    temp = 0;
                } else {
                    temp = 1;
                }

                addPost(new Post(title, breed, size, descripion, mUid, temp));
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void addPost(Post post) {
        mAdapter.addPost(post);
    }

//    private Post.Size turnToSIZE(String s) {
//        if (s.equals("Big")) {
//            return Post.Size.Big;
//        } else if (s.equals("Medium")) {
//            return Post.Size.Medium;
//        } else if (s.equals("Small")) {
//            return Post.Size.Small;
//        } else {
//            Log.e("ERROR----->>", "Input String can not be turn to a Size type.");
//            return null;
//        }
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LILCallback) {
            mLILCallback = (LILCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LILCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLILCallback = null;
    }

    public void uploadImage(Uri file) {
        mAdapter.uploadImage(file);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface LILCallback {
        void onPostSelected(Post post, int position);

        void chooseImage();
    }

}
