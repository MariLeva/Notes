package ru.geekbrains.notes.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import ru.geekbrains.notes.R;
import ru.geekbrains.notes.data.Note;
import ru.geekbrains.notes.data.NoteSource;

public class SocialNetworkAdapter extends RecyclerView.Adapter<SocialNetworkAdapter.MyViewHolder> {

    private NoteSource data;
    OnItemClickListener onItemClickListener;
    static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private int position;
    Fragment fragment;

    public SocialNetworkAdapter(NoteSource data) {
        this.data = data;
    }

    public int getPosition(){
        return position;
    }

    public void setData(NoteSource data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public SocialNetworkAdapter() {
    }

    public SocialNetworkAdapter(Fragment fragment){
        this.fragment = fragment;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SocialNetworkAdapter.MyViewHolder holder, int position) {
        holder.setDate(data.getNote(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView note;
        private TextView noteDate;
        private TextView noteText;
        private LinearLayout color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.textView_title);
            noteDate = itemView.findViewById(R.id.textView_date);
            noteText = itemView.findViewById(R.id.textView_text);
            color = itemView.findViewById(R.id.linear_color);

            color.setOnClickListener(view -> {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });

            color.setOnLongClickListener(view -> {
                position = getLayoutPosition();
                itemView.showContextMenu(10,10);
                return true;
            });

            if (fragment != null) {
                itemView.setOnLongClickListener(view -> {
                    position = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void setDate (Note date){
            note.setText(date.getNoteName());
            noteDate.setText(format.format(date.getDate()));
            noteText.setText(date.getNoteText());
            color.setBackgroundColor(date.getColor());
        }
    }
}
