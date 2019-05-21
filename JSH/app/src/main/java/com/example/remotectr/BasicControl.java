package com.example.remotectr;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.EOFException;
import java.io.PrintWriter;
import java.net.Socket;


public class BasicControl extends Fragment {

    /*
    public BasicControl() {
        // Required empty public constructor
    }
    */


    // TODO: Rename and change types and number of parameters
    /*
    public static BasicControl newInstance(String param1, String param2) {
        BasicControl fragment = new BasicControl();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */



    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    */
    Socket sock;
    PrintWriter out;
    String id;

    public void setfragment (Socket socket, PrintWriter out,String id){
        this.out = out;
        sock = socket;
        this.id = id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_basic_control, container, false);
        view.findViewById(R.id.prev).setOnClickListener(btnClickListener);
        view.findViewById(R.id.next).setOnClickListener(btnClickListener);


        return view;
    }


    /*
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
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

    private Button.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prev:

                    String msg="MAIN/PRE";
                    Log.w("NETWORK", id+"a/"+msg);
                    out.println(id+"a/"+msg);
                    out.flush();
                    break;
                case R.id.next:

                    String msg2="MAIN/NEXT";
                    Log.w("NETWORK", id+"a/"+msg2);
                    out.println(id+"a/"+msg2);
                    out.flush();
                    break;
                case R.id.F5:

                    String msg3="MAIN/START";
                    Log.w("NETWORK", id+"a/"+msg3);
                    out.println(id+"a/"+msg3);
                    out.flush();
                    break;
                case R.id.ESC:

                    String msg4="MAIN/ESC";
                    Log.w("NETWORK", id+"a/"+msg4);
                    out.println(id+"a/"+msg4);
                    out.flush();
                    break;
//                case R.id.back:
//                    Intent intent = new Intent();
//                    intent.putExtra("name", "inae");
//                    setResult(RESULT_OK, intent);
//                    finish();
//                    break;
            }
        }
    };
}