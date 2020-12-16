package com.example.yogaappwithdb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yogaappwithdb.Activities.OpenPostActivity;
import com.example.yogaappwithdb.DataModel.DataConverter;
import com.example.yogaappwithdb.DataModel.Post;
import com.example.yogaappwithdb.DataModel.PostDatabase;

import java.util.List;

public class PostRecViewAdapter extends RecyclerView.Adapter<PostRecViewAdapter.ViewHolder> {

    List<Post> data;
    PostDatabase database;
    OpenPostActivity openPostActivity;
    public Activity context;
    public PostRecViewAdapter(Activity context, List<Post> posts)
    {
        this.context = context;
        data = posts;

        notifyDataSetChanged();
    }


    //MUST USE
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }
    //MUST USE
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Post post = data.get(position);
        holder.userImage.setImageBitmap(DataConverter.convertByteArrayToImage(post.getImage()));
        holder.userName.setText(post.getName());
        holder.userTitle.setText(post.getTitle());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, posts.get(position).getName() + "Selected", Toast.LENGTH_SHORT).show();
                //Will create Intent to access PostActivity class


                Intent intent = new Intent(context, OpenPostActivity.class);

                intent.putExtra("postID", post.getId());




                //to pass the whole obj need to use parcelable
                //intent.putExtra()
                //startActivity can't use, cuz we specify the context and class and not the activity
                context.startActivity(intent);

            }
        });

        database = PostDatabase.getDBInstance(context);

        if (data.get(position).isExpanded())
        {
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);
        }
        else
        {
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.GONE);
            holder.upArrow.setVisibility(View.VISIBLE);
        }

    }

    //MUST USE
    @Override
    public int getItemCount() {
        return data.size();
    }
    //This class is responsible for holding the View items for every item in recView
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        CardView parent;
        ImageView userImage;

        ImageView downArrow, upArrow;
        RelativeLayout expandedRelLayout;
        TextView userTitle;

        ImageView deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName= itemView.findViewById(R.id.id_userName);
            parent = itemView.findViewById(R.id.parent);
            userTitle= itemView.findViewById(R.id.id_userTitle);
            userImage = itemView.findViewById(R.id.id_userImage);


            deleteButton = itemView.findViewById(R.id.id_delete_button);


            downArrow = itemView.findViewById(R.id.id_downArrow);
            upArrow = itemView.findViewById(R.id.id_upArrow);
            expandedRelLayout = itemView.findViewById(R.id.expandedRelLayout);

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = data.get(getAdapterPosition());
                    post.setExpanded(!post.isExpanded());
                    //Item cuz changing only one list items' data
                    notifyItemChanged(getAdapterPosition());
                }
            });
            upArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = data.get(getAdapterPosition());
                    post.setExpanded(!post.isExpanded());
                    //Item cuz changing only one list items' data
                    notifyItemChanged(getAdapterPosition());
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to remove this post?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if(database.postDAO().deletePost(post))
                           // {
                                Post post = data.get(getAdapterPosition());
                                Toast.makeText(context, "Post removed", Toast.LENGTH_SHORT).show();
                                 database.postDAO().deletePost(post);
                                 data.clear();
                                 data.addAll(database.postDAO().getAllPosts());
                                notifyDataSetChanged();
                           // }
                            //else {
                             //   Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show();
                          //  }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();



                    //Item cuz changing only one list items' data
                    notifyDataSetChanged();
                }
            });

        }
    }
}
