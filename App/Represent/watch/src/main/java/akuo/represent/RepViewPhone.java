package akuo.represent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepViewPhone.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepViewPhone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepViewPhone extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "background";

    // TODO: Rename and change types of parameters
    private String background;

    private OnFragmentInteractionListener mListener;

    private ImageButton button;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RepViewPhone.
     */
    // TODO: Rename and change types and number of parameters
    public static RepViewPhone newInstance() {
        RepViewPhone fragment = new RepViewPhone();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RepViewPhone() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_rep_view_phone, container, false);
        button = (ImageButton) V.findViewById(R.id.view_button);
        addListenerOnButton();
        return V;
    }

    public void addListenerOnButton() {
        final Intent intent = new Intent(getActivity(), WatchToPhoneService.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().startService(intent);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onContentFragmentInteraction(Uri uri);
    }

}
