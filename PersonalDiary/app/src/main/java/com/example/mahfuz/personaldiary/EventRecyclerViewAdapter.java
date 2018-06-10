package com.example.mahfuz.personaldiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder>
            implements Filterable{

    private List<Event> events;
    private List<Event> filteredEvents;
    private String colorArray[] = {"FF0000", "FF00FF", "FF4500", "FFD700", "FF8C00",
            "5E35B1", "303F9F", "1976D2", "6D4C41", "C2185B","455A64"};
    private ActionMode mActionMode;


    public EventRecyclerViewAdapter (List<Event> events) {
        this.events = events;
        this.filteredEvents = events;
    }

    @Override
    public EventRecyclerViewAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_view_item, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewAdapter.EventViewHolder holder, int position) {
        Event event = filteredEvents.get(position);
        holder.titleTv.setText(event.getTitle());
        holder.descriptionTv.setText(event.getDescription());

        Date date = null;
        try {
            date = DateUtility.getStringToDate(event.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int randomValue = DateUtility.getRandomNumber(0, colorArray.length);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String description = event.getDescription();

        //checking length for big string it will show only 30 character
        if (description.length() >=25) {
            String ss = description.substring(0, 24);
            //set description to Text View
            holder.descriptionTv.setText(ss + "...");
        } else {
            //set description to Text View
            //Log.i("description is:", description);
            holder.descriptionTv.setText(description);
        }

        //setting date
        //Log.i("month is:", date.getMonth()+"");
        holder.dayTv.setText(cal.get(Calendar.DAY_OF_MONTH)+"");
        holder.monthTv.setText(new SimpleDateFormat("MMM")
                .format(date)+"");
        holder.yearTv.setText(cal.get(Calendar.YEAR)+"");
        //setting random color
        //Log.i("color code is:", colorArray[2]);
        holder.monthTv.setBackgroundColor(Color.parseColor("#"+colorArray[randomValue]));
        holder.leftBar.setBackgroundColor(Color.parseColor("#"+colorArray[randomValue]));
    }

    @Override
    public int getItemCount() {
        return filteredEvents.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                List<Event> tempList = new ArrayList<>();
                if (query.isEmpty()) {
                    filteredEvents = events;
                } else {
                    for (Event event : events) {
                        if (event.getDate().contains(query) ||
                                event.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            tempList.add(event);
                        }
                    }
                    filteredEvents = tempList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredEvents;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEvents = (List<Event>) filterResults.values;
                notifyDataSetChanged();//refresh recyclerview items
            }
        };
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private EventDataSource eventDataSource = new EventDataSource(itemView.getContext());
        TextView titleTv, descriptionTv;
        TextView dayTv, monthTv, yearTv;
        TextView leftBar;
        public EventViewHolder(final View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);

            dayTv = itemView.findViewById(R.id.dayTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            yearTv = itemView.findViewById(R.id.yearTv);
            leftBar = itemView.findViewById(R.id.leftBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Event eventForId = filteredEvents.get(getAdapterPosition());
                    //Event event = eventDataSource.get(eventForId.getId());
                    Intent intent = new Intent(view.getContext(),
                            ShowEventActivity.class);
                    intent.putExtra("event", eventForId);
                    view.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mActionMode != null) {
                        return false;
                    }
                    mActionMode = view.startActionMode(mActionModeCallback);
                    mActionMode.setTag(getAdapterPosition());
                    view.setSelected(true);
                    return true;
                }
            });
        }

        private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                actionMode.setTitle("Choose Option");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                Event eventForId = filteredEvents.get(getAdapterPosition());
                switch (menuItem.getItemId()) {
                    case R.id.editMenu :
                        Event event = eventDataSource.get(eventForId.getId());
                        Intent intent = new Intent(itemView.getContext(), UpdateEventActivity.class);
                        intent.putExtra("event", event);
                        itemView.getContext().startActivity(intent);
                        actionMode.finish();
                        return true;
                    case R.id.deleteMenu :
                        boolean isDeleted = eventDataSource.delete(eventForId.getId());
                        if (isDeleted) {
                            Toast.makeText(itemView.getContext(), "Deleted Succesfully!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "No Deleted!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // reload the activity
                        ((Activity)itemView.getContext()).finish();
                        itemView.getContext().startActivity(((Activity)itemView.getContext()).getIntent());
                        ((Activity) itemView.getContext()).finish();
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mActionMode = null;
            }
        };
    }


}
