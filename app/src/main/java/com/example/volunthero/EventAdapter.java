package com.example.volunthero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventModel> eventList;

    public EventAdapter(List<EventModel> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //item_event_card.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel event = eventList.get(position);

        holder.tvTitle.setText(event.getTitle());
        //соединяем категорию и локацию (например: Technology • Yerevan)
        String categoryText = event.getCategory() + " • " + event.getLocation();
        holder.tvCategory.setText(categoryText);

        //картинок из drawable
        holder.ivImage.setImageResource(event.getImageResId());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvCategory;

        public EventViewHolder(View itemView) {
            super(itemView);
            // ID
            ivImage = itemView.findViewById(R.id.eventImage);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvCategory = itemView.findViewById(R.id.tvEventCategory);
        }
    }
}