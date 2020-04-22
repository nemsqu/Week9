package com.example.week9;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TheaterShowingsAdapter extends RecyclerView.Adapter<TheaterShowingsAdapter.TheaterViewHolder>{

        private List<XmlParser.Show> list = new ArrayList<XmlParser.Show>();
        private Date startTime;
        private Date endTime;
        private TheaterCollection theaters;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class TheaterViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textView;
            public TheaterViewHolder(TextView v) {
                super(v);
                textView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public TheaterShowingsAdapter(List<XmlParser.Show> shows, String start, String end, String movie, TheaterCollection theaters) {

                for (int i=0; i<shows.size(); i++) {
                    XmlParser.Show show = (XmlParser.Show) shows.get(i);

                    if (show.getMovieName().equals(movie)) {
                        list.add(show);
                    }
                }
                this.theaters = theaters;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public TheaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            com.example.week9.TheaterShowingsAdapter.TheaterViewHolder vh = new TheaterViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(com.example.week9.TheaterShowingsAdapter.TheaterViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            XmlParser.Show show = (XmlParser.Show) list.get(position);
            String name = "";
            for(Theater t : theaters.listTheaters){
                if(t.getID().equals(show.theaterID))
                    name = t.getTheaterName();
            }
            String[] startInfo = show.getStart().split("T");
            String[] date = startInfo[0].split("-");
            String dateReformed = date[2] + "." + date[1] + "." + date[0];
            String[] startTime = startInfo[1].split(":");
            String startTimeReformed = startTime[0] + ":" + startTime[1];
            String[] endInfo = show.getEnd().split("T");
            String[] endTime = endInfo[1].split(":");
            String endTimeReformed = endTime[0] + ":" + endTime[1];
            CharSequence s = name + " " + dateReformed + " " + startTimeReformed + "-" + endTimeReformed;
            holder.textView.setText(s);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return list.size();
        }

}
