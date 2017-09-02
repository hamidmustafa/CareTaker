package caretaker.caretaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fusion on 19-Nov-16.
 */

public class ListAdapter_ForPatientReports extends ArrayAdapter<String> {
    ListAdapter_ForPatientReports.customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(ListAdapter_ForPatientReports.customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;
    private ArrayList<String> NameNumber = new ArrayList<String>();

    public ListAdapter_ForPatientReports(Context context, ArrayList<String> NameNumber) {
        super(context, R.layout.child_listivew_patients_report, NameNumber);
        this.NameNumber = NameNumber;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListAdapter_ForPatientReports.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.child_listivew_patients_report, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.childTextView_rr);
            viewHolder.button = (Button) convertView
                    .findViewById(R.id.childButton_rr);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListAdapter_ForPatientReports.ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position,temp);
                }

            }
        });

        return convertView;


    }

    public class ViewHolder {
        TextView text;
        Button button;
    }

}
