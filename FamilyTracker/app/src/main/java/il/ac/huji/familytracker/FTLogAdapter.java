package il.ac.huji.familytracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Omer on 02/09/2015.
 */
public class FTLogAdapter extends ArrayAdapter<FTNotification> {
    private Context m_cntxtCurrContext;
    private ArrayList<FTNotification> m_arrNotifications;

    public FTLogAdapter(Context context, ArrayList<FTNotification> p_arradptNotifications) {
        super(context, R.layout.ft_log_item);
        m_arrNotifications = p_arradptNotifications;
        m_cntxtCurrContext = context;
    }

    @Override
    public int getCount() {
        return m_arrNotifications.size();
    }

    @Override
    public FTNotification getItem(int position) {
        return m_arrNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vwTodoTaskVisual = convertView;

        if (vwTodoTaskVisual == null) {
            LayoutInflater inflater = ((Activity) m_cntxtCurrContext).getLayoutInflater();
            vwTodoTaskVisual = inflater.inflate(R.layout.ft_log_item, parent, false);
        }

        return vwTodoTaskVisual;
    }

}
