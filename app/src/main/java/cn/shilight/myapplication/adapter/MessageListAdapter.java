package cn.shilight.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.domain.Message;
import cn.shilight.myapplication.util.TxUtil;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListHolder> {

    private List<Message> messageList;
    private Context context;


    public MessageListAdapter(List<Message> messageList , Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageline,parent,false);
        MessageListHolder messageListHolder = new MessageListHolder(item);
        return messageListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListHolder holder, int position) {
           holder.name.setText(messageList.get(position).getName());
           holder.content.setText(messageList.get(position).getContent());
           holder.time.setText(messageList.get(position).getTime());
           holder.tx.setImageBitmap(TxUtil.getTxByUser(context,messageList.get(position).getUid()));

           //holder.tx.setImageResource(messageList.get(position).getTx());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageListHolder extends RecyclerView.ViewHolder{

        private TextView name,content,time;
        private ImageView tx;


        public MessageListHolder(@NonNull View itemView) {
            super(itemView);
            tx = itemView.findViewById(R.id.mltx);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            content= itemView.findViewById(R.id.content);

        }
    }
}
