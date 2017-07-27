package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinishSell.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinishSell#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishSell extends Fragment {

    // UI objects
    TextView tvFinisSale;

    //
    ArrayList<SalesItem> items;

    private OnFragmentInteractionListener mListener;

    public FinishSell() {
        // Required empty public constructor

    }

    public static FinishSell newInstance(ArrayList<SalesItem> _items) {
        FinishSell fragment = new FinishSell();
        Bundle args = new Bundle();
        args.putParcelableArrayList("SalesData", _items);
        fragment.setArguments(args);
        return fragment;
    }

    private void initUI() {
        tvFinisSale = (TextView) getView().findViewById(R.id.saleText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("SalesData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_sell, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        if (items != null) {
            tvFinisSale.setText("Data retrieved successfully " + String.valueOf(items.size()) + "" +
                    " items\n" + items.get(0).barcode);
        } else {
            tvFinisSale.setText("Failed to receive data");
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
