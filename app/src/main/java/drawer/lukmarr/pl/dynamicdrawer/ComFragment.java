package drawer.lukmarr.pl.dynamicdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ComFragment extends Fragment {
    private String comPortName;
    public static final String TAG = ComFragment.class.getSimpleName();

    public static ComFragment newInstance(String name) {
        ComFragment fragment = new ComFragment();
        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ComFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comPortName = getArguments().getString("NAME");
        } else {
            comPortName = "COM1";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView ");
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        TextView tview = (TextView) view.findViewById(R.id.label);
        tview.setText(comPortName);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
