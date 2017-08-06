package edu.rosehulman.lix4.petlf.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.lix4.petlf.MyPostsAdapter;
import edu.rosehulman.lix4.petlf.R;
import edu.rosehulman.lix4.petlf.models.Post;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MPFCallback} interface
 * to handle interaction events.
 */
public class MyPostFragment extends Fragment {

    private MPFCallback mpfCallback;
    private MyPostsAdapter mAdapter;

    public MyPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_post, container, false);
        RecyclerView view = (RecyclerView) rootView.findViewById(R.id.recycler_view_my_post);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.setHasFixedSize(true);
        mAdapter = new MyPostsAdapter(mpfCallback);
        view.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MPFCallback) {
            mpfCallback = (MPFCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MPFCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mpfCallback = null;
    }

    public void remove(Post post) {
        mAdapter.remove(post);
    }

    public void update(Post post, String title, String breed, String size, String description) {
        mAdapter.update(post, title, breed, size, description);
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
    public interface MPFCallback {
        // TODO: Update argument type and name
        void editMyPost(Post post);

        void deleteMyPost(Post post);
    }
}
