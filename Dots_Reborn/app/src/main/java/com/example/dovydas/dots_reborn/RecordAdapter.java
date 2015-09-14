package com.example.dovydas.dots_reborn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Yngvi on 07/09/15.
 * Base code for this class is retrieved from
 *         https://github.com/yngvib/InClassListDemo/blob/master/app/src/main/java/com/example/yngvi/inclasslistdemo/RecordAdapter.java
 */
public class RecordAdapter extends ArrayAdapter<Record> {

    private final Context context;
    private final List<Record> values;

    public RecordAdapter(Context context, List<Record> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);


        TextView rankView = (TextView) rowView.findViewById(R.id.row_rank);
        rankView.setText( Integer.toString(position + 1)); /* think of the better war for displaying RANK*/

        TextView scoreView = (TextView) rowView.findViewById(R.id.row_best_score);
        scoreView.setText( values.get(position).getDBscore() );

        TextView timeView = (TextView) rowView.findViewById(R.id.row_time);
        timeView.setText( values.get(position).getDBtime() );


        return rowView;
    }
}