package cn.shilight.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import cn.shilight.myapplication.R;
import cn.shilight.myapplication.domain.FriendList;
import cn.shilight.myapplication.util.TxUtil;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleListHolder> {

    private List<FriendList> friendLists;
    private Context context;


    public PeopleAdapter(List<FriendList> friendListList,Context context) {
        this.friendLists = friendListList;
        this.context = context;
    }

    @NonNull
    @Override
    public PeopleAdapter.PeopleListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistline,parent,false);
        PeopleAdapter.PeopleListHolder peopleListHolder = new PeopleAdapter.PeopleListHolder(item);
        return peopleListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.PeopleListHolder holder, int position) {
        holder.name.setText(friendLists.get(position).getName());
        //holder.tx.setImageResource(friendLists.get(position).getTx());
        holder.tx.setImageBitmap(TxUtil.getTxByUser(context,friendLists.get(position).getUid()));
    }

    @Override
    public int getItemCount() {
        return friendLists.size();
    }


    class PeopleListHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView tx;


        public PeopleListHolder(@NonNull View itemView) {
            super(itemView);
            tx = itemView.findViewById(R.id.peopletx);
            name = itemView.findViewById(R.id.peoplename);


        }
    }

}
