package com.example.ahmadsb.lab_ex9;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    List<oneCall> List_calls;
    Context context;

    public ListAdapter(Context context, List<oneCall> call){
        this.List_calls = call;
        this.context = context;
    }
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_all_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        holder.phoneNumber.setText(List_calls.get(position).getnumberphone());
        holder.Date.setText(List_calls.get(position).getDateCall());
        holder.Type.setText(List_calls.get(position).getType());
        holder.Duration.setText(List_calls.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return List_calls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView phoneNumber,Date,Type,Duration;
        public ViewHolder(View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.txt_number_phone);
            Date = itemView.findViewById(R.id.date_id);
            Type = itemView.findViewById(R.id.type_id);
            Duration = itemView.findViewById(R.id.txt_duration);
        }
    }
}
