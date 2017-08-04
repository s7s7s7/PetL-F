package edu.rosehulman.lix4.petlf.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.rosehulman.lix4.petlf.R;
import edu.rosehulman.lix4.petlf.models.Post;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String SIZE = "size";
    private static final String BREED = "breed";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mDescription;
    private Post.Size mSize;
    private String mBreed;


    public InfoDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InfoDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoDetailFragment newInstance(String title, String description, String size, String breed) {
        InfoDetailFragment fragment = new InfoDetailFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(DESCRIPTION, description);
        args.putString(SIZE, size);
        args.putString(BREED, breed);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
            mBreed = getArguments().getString(BREED);
            mDescription = getArguments().getString(DESCRIPTION);
            mSize = turnToSIZE(getArguments().getString(SIZE));
        }
    }

    private Post.Size turnToSIZE(String s) {
        if (s.equals("Big")) {
            return Post.Size.Big;
        } else if (s.equals("Medium")) {
            return Post.Size.Medium;
        } else if (s.equals("Small")) {
            return Post.Size.Small;
        } else {
            Log.e("ERROR----->>", "Input String can not be turn to a Size type.");
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_detail, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.info_detail_title);
        TextView descripView = (TextView) view.findViewById(R.id.info_detail_description);
        TextView sizeView = (TextView) view.findViewById(R.id.info_detail_size);
        TextView breedView = (TextView) view.findViewById(R.id.info_detail_breed);

        titleView.setText(mTitle);
        descripView.setText(mDescription);
        sizeView.setText(turnToSTRING(mSize));
        breedView.setText(mBreed);
        return view;
    }

    private String turnToSTRING(Post.Size mSize) {
        if (mSize.equals(Post.Size.Big)) {
            return "Big";
        } else if (mSize.equals( Post.Size.Medium)) {
            return "Medium";
        } else {
            return "Small";
        }
    }

}
