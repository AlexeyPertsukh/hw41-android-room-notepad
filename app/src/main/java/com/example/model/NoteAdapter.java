package com.example.model;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.constants.IColor;
import com.example.notepad.R;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Serializable, IColor {
    private final static int LENGTH_SHORT_MEMO = 30;
    private final ArrayList<Note> notes;
    private OnClickItem onClickItem;

    public NoteAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvAdTitle.setText(note.getTitle());
        holder.tvAdDt.setText(note.getDt());
        String shortMemo = textToLine(note.getMemo());
        if(shortMemo.length() > LENGTH_SHORT_MEMO) {
            shortMemo = shortMemo.substring(0, LENGTH_SHORT_MEMO) + "...";
        }
        holder.tvAdShortMemo.setText(shortMemo);

        holder.itemView.setBackgroundColor(note.getColor());
    }

    @Override
    public int getItemCount() {
        if(notes == null) {
            return 0;
        } else {
            return notes.size();
        }
    }

    private static String textToLine(String text) {
        return text.replace("\n", " ");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAdTitle;
        private final TextView tvAdDt;
        private final TextView tvAdShortMemo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdTitle = itemView.findViewById(R.id.tvAdTitle);
            tvAdDt = itemView.findViewById(R.id.tvAdDt);
            tvAdShortMemo = itemView.findViewById(R.id.tvAdShortMemo);
            itemView.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            if(onClickItem != null) {
                Note note = notes.get(getAdapterPosition());
                onClickItem.onClickItem(note);
            }
        }
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public interface OnClickItem {
        void onClickItem(Note note);
    }

}
