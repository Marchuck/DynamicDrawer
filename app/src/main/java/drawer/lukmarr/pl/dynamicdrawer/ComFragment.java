package drawer.lukmarr.pl.dynamicdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ComFragment extends Fragment {
    private String comPortName;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
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
