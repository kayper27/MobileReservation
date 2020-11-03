package com.example.mobilereservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.daimajia.swipe.SwipeLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends Fragment {

    private SwipeLayout sample2;

    public SwipeFragment() {
        // Required empty public constructor
    }

    public static SwipeFragment newInstance(String param1, String param2) {
        SwipeFragment fragment = new SwipeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_swipe, container, false);
        sample2 = (SwipeLayout) root.findViewById(R.id.sample2);
        sample2.setShowMode(SwipeLayout.ShowMode.LayDown);
        sample2.addDrag(SwipeLayout.DragEdge.Right, sample2.findViewWithTag("Bottom2"));
//        sample2.setShowMode(SwipeLayout.ShowMode.PullOut);
        sample2.findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Star", Toast.LENGTH_SHORT).show();
            }
        });

        sample2.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Trash Bin", Toast.LENGTH_SHORT).show();
            }
        });

        sample2.findViewById(R.id.magnifier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Magnifier", Toast.LENGTH_SHORT).show();
            }
        });
        
        sample2.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Click on surface", Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}