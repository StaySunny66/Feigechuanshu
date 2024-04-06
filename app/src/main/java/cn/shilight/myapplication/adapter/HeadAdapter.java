package cn.shilight.myapplication.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.shilight.myapplication.R;

public class HeadAdapter extends RecyclerView.Adapter<HeadAdapter.MyViewHolder> {

    private  int[] imageList;

    private  onItemClickLinister linister;

    public interface onItemClickLinister{

        void onItemClick(View v,int which);

    }


    public HeadAdapter(int[]  meaasageList) {
        this.imageList = meaasageList;
    }


    public void setLinister(onItemClickLinister linister) {
        this.linister = linister;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageitem,parent,false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tx.setImageResource(imageList[position]);
        if(linister!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linister.onItemClick(view,position);

                }
            });



        }

    }

    @Override
    public int getItemCount() {
        return imageList.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView tx;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tx = itemView.findViewById(R.id.imageitt);



        }

    }
}
