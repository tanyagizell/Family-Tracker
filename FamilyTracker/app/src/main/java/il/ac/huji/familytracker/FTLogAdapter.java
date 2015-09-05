package il.ac.huji.familytracker;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Omer on 02/09/2015.
 */
public class FTLogAdapter extends ArrayAdapter<FTNotification> {
    private Context m_cntxtCurrContext;
    private ArrayAdapter<FTNotification> m_arrNotifications;

    public FTLogAdapter(Context context, ArrayAdapter<FTNotification> p_arradptNotifications) {
        super(context, R.layout.ft_log_item);
        m_arrNotifications = p_arradptNotifications;
        m_cntxtCurrContext = context;
    }
}
