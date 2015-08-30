package il.ac.huji.familytracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanyagizell on 29/08/2015.
 *
 * Dialog fragment class for family tracker app
 */
public class FTDialogFragment extends DialogFragment {

    private AdapterView.OnItemClickListener onItemClickListener;
//    private Core application;
    public static String TAG = "DialogMyListFragment";

    public static FTDialogFragment getInstance() {
        FTDialogFragment df = new FTDialogFragment();
        return df;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ListView list = new ListView(getActivity());


        List<String> elements = new ArrayList<String>();
        for(int i=0;i<10;i++)
            elements.add("item " + i);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, elements);

        list.setAdapter(adapter);
        list.setOnItemClickListener(onItemClickListener);

        builder.setView(list);

        return builder.create();

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.ft_dialog, container, false);
//
//        return v;
//    }
}