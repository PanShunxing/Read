package com.read.pan.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.read.pan.R;
import com.read.pan.activity.GenresResultActivity;
import com.read.pan.adapter.BookgenresAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.genres_recycler)
    RecyclerView storeRecycler;
    private BookgenresAdapter bookgenresAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private View rootView;
    public GenresFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenresFragment newInstance(String param1, String param2) {
        GenresFragment fragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_genres, container, false);
        ButterKnife.bind(this, rootView);
        init(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(final View view) {
        //给Recycler设置gridView布局
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        storeRecycler.setLayoutManager(gridLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        storeRecycler.setHasFixedSize(true);
        //设置Item增加、移除动画
        storeRecycler.setItemAnimator(new DefaultItemAnimator());
        bookgenresAdapter = new BookgenresAdapter(getActivity());
        storeRecycler.setAdapter(bookgenresAdapter);
        bookgenresAdapter.setOnItemClickLitener(new BookgenresAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                getActivity().startActivity(
                        new Intent(getContext(), GenresResultActivity.class)
                                .putExtra("type",BookgenresAdapter.getType(position)));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
}

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
