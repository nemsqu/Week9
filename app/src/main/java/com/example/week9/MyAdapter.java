package com.example.week9;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private List<XmlParser.Show> list = new ArrayList<XmlParser.Show>();
    private Date startTime;
    private Date endTime;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<XmlParser.Show> shows, String start, String end, String movie) {
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        try {
            Date startTime = formatter.parse(start);
            Date endTime = formatter.parse(end);
            for (int i = 0; i < shows.size(); i++) {
                XmlParser.Show show = (XmlParser.Show) shows.get(i);
                String[] startTimeLong = show.getStart().split("T");
                String startTimeMovie = startTimeLong[1];
                Date showStart = formatter.parse(startTimeMovie);
                System.out.println(show.getMovieName());
                if (!(showStart.compareTo(startTime) <= 0) && !(showStart.compareTo(endTime) >= 0)) {
                    list.add(show);

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        XmlParser.Show show = (XmlParser.Show) list.get(position);
        CharSequence s = show.getMovieName() + " " + show.getStart() + "-" + show.getEnd();
        holder.textView.setText(show.getMovieName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

}