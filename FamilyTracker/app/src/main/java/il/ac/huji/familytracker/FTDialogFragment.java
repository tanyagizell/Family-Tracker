package il.ac.huji.familytracker;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FTDialogFragment extends DialogFragment implements
        OnItemClickListener {

    ArrayList<String> itemsList;
    ListView mylist;

    ArrayAdapter<String> adapter;



    public interface DialogListener{
        void onDialogItemSelect(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.ft_dialog, null, false);
        mylist = (ListView) view.findViewById(R.id.listView);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, itemsList);

        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        DialogListener activity = (DialogListener) getActivity();
        activity.onDialogItemSelect(position);
        dismiss();

    }

    public void setItemsList(ArrayList<String> list){
        itemsList = list;
    }

}
