package com.homework.grop.group_homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homework.grop.group_homework.model.Message;
import com.homework.grop.group_homework.widget.CircleImageView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<Message>messages;
    private MyItemClickListener listener;
    public MessageAdapter(List<Message> messages, MyItemClickListener listener) {
    this.messages=messages;
    this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.im_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);
       // viewHolder.time.setText("s:"+i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView description;
        private TextView time;
        private CircleImageView pic;
        private ImageView official_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tv_title);
            description=itemView.findViewById(R.id.tv_description);
            time=itemView.findViewById(R.id.tv_time);
            pic=itemView.findViewById(R.id.iv_avatar);
            official_pic=itemView.findViewById(R.id.robot_notice);
            itemView.setOnClickListener(this);
        }
        public void bind(int position)
        {
            title.setText(messages.get(position).getTitle());
            description.setText(messages.get(position).getDescription());
            time.setText(messages.get(position).getTime());
            Message msg=messages.get(position);
            if(msg.getIcon().equals("TYPE_ROBOT")){
            pic.setImageResource(R.drawable.session_robot);
            }else if(msg.getIcon().equals("TYPE_SYSTEM"))
            {
                pic.setImageResource(R.drawable.session_system_notice);
            }else if(msg.getIcon().equals("TYPE_GAME"))
            {
                pic.setImageResource(R.drawable.icon_micro_game_comment);
            }else if(msg.getIcon().equals("TYPE_STRANGER"))
            {
                pic.setImageResource(R.drawable.session_stranger);
                official_pic.setVisibility(View.INVISIBLE);
            }
            else
            { official_pic.setVisibility(View.INVISIBLE);

            }
        }
        @Override
        public void onClick(View v)
        {
            int position =getAdapterPosition();
            if(listener!=null)
            {
                listener.onListItemClick(position);
            }
        }
    }
    public interface MyItemClickListener{
        void onListItemClick(int position);
    }
}
