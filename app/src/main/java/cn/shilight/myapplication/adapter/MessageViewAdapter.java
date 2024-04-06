package cn.shilight.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.MessageView;
import cn.shilight.myapplication.util.SettingUtil;
import cn.shilight.myapplication.util.TxUtil;


public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.MessageViewHolder> {


    private List<MessageView> messageViewList;
    Context context;



    public MessageViewAdapter(List<MessageView> messageViewList, Context context){
        this.messageViewList = messageViewList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return messageViewList.get(position).getMessageType();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MessageView.MY_MESSAGE){
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.duihuai_my,parent,false);
            MessageViewAdapter.MessageViewHolder messageViewHolder = new MessageViewAdapter.MessageViewHolder(item);
            return messageViewHolder;
        }else if(viewType==MessageView.SHE_MESSAGE){
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.duihuai_she,parent,false);
            MessageViewAdapter.MessageViewHolder messageViewHolder = new MessageViewAdapter.MessageViewHolder(item);
            return messageViewHolder;

        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.content.setText(messageViewList.get(position).getContent());

        if(messageViewList.get(position).getMessageType()==MessageView.MY_MESSAGE){

            holder.tx.setImageBitmap(TxUtil.getTxByUser(context,SettingUtil.getMydata(context).getUid()));
          //  holder.tx.setImageResource(SettingUtil.getMydata(context).getTx_id());
        } else if (messageViewList.get(position).getMessageType()==MessageView.SHE_MESSAGE) {
            Friend friend = new FriendDao(context).getUser(messageViewList.get(position).getFromUid());

            holder.tx.setImageBitmap(TxUtil.getTxByUser(context,friend.getUid()));
           // holder.tx.setImageResource(friend.getTx_id());
        }


    }

    @Override
    public int getItemCount() {
        return messageViewList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView content;
        private ImageView tx;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            tx = itemView.findViewById(R.id.tx);



        }
    }
}
