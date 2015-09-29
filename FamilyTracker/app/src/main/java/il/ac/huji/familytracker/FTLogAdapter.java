package il.ac.huji.familytracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Omer on 02/09/2015.
 */
public class FTLogAdapter extends ArrayAdapter<FTNotification> {
    private static final String NOTIFICATION_CONTENT_FORMAT = "%s %s %s";
    private static final String NOTIFICATION_ARRIVED_AT_SEGMENT = "arrived at";
    private static final String NOTIFICATION_LEFT_SEGMENT = "left";
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
        View vwNotificationVisual = convertView;

        if (vwNotificationVisual == null) {
            LayoutInflater inflater = ((Activity) m_cntxtCurrContext).getLayoutInflater();
            vwNotificationVisual = inflater.inflate(R.layout.ft_log_item, parent, false);
        }
        FTNotification ntfCurrentlyInserted = m_arrNotifications.get(position);
        TextView txtvwNotifContent = (TextView) vwNotificationVisual.findViewById(R.id.EntryContent);
        TextView txtvwNotifTimeStamp = (TextView) vwNotificationVisual.findViewById(R.id.EntryTimeStamp);
        txtvwNotifContent.setText(ConstructNotificationDisplay(ntfCurrentlyInserted));
        txtvwNotifTimeStamp.setText(ntfCurrentlyInserted.getTimeStamp().toString());
        return vwNotificationVisual;
    }

    private String ConstructNotificationDisplay(FTNotification p_ntfDataContainer) {
        return String.format(NOTIFICATION_CONTENT_FORMAT, p_ntfDataContainer.getSubjectName(), getNotificationActionString(p_ntfDataContainer), p_ntfDataContainer.getNotificationLocationDisplayName());
    }

    private String getNotificationActionString(FTNotification p_ntfDataContainer) {
        return p_ntfDataContainer.getType() == FTNotification.FTNotifStateENUM.ARRIVAL ? NOTIFICATION_ARRIVED_AT_SEGMENT : NOTIFICATION_LEFT_SEGMENT;
    }

    @Override
    public void add(FTNotification p_ntfNew) {
        if (p_ntfNew != null) {
            m_arrNotifications.add(p_ntfNew);
        }
        notifyDataSetChanged();
    }
}
