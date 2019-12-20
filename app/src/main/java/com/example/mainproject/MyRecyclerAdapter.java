package com.example.mainproject;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference postsRef = mRootReference.child("posts");
    public MyRecyclerAdapter(Context _context) {
        context = _context;
        postsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = new Post();
                post.comments = new ArrayList<>();
                post.key = dataSnapshot.getKey();
                post.id = dataSnapshot.child("id").getValue().toString();
                post.un = dataSnapshot.child("un").getValue().toString();
                post.like_list = dataSnapshot.child("like_list").getValue().toString();
                post.link = dataSnapshot.child("link").getValue().toString();
                post.pp_link = dataSnapshot.child("pp_link").getValue().toString();
                post.text = dataSnapshot.child("text").getValue().toString();
                post.likes = Integer.parseInt(dataSnapshot.child("likes").getValue().toString());
                SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String time=dataSnapshot.child("time").getValue().toString();
                Long t=Long.parseLong(time);
                post.time = localDateFormat.format(new Date(t));
                DataSnapshot commentsSnapshot = dataSnapshot.child("comments");
                for (DataSnapshot comment : commentsSnapshot.getChildren()) {
                    Comment newComment = new Comment();
                    newComment.user = comment.child("user").getValue().toString();
                    newComment.text = comment.child("text").getValue().toString();
                    post.comments.add(newComment);
                }
                postList.add(post);
                MyRecyclerAdapter.this.notifyItemInserted(postList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id=dataSnapshot.getKey().toString();
                int position=-1;
                for (int i=0; i<postList.size(); i++)
                {
                    if(postList.get(i).key.equals(id)){
                        position=i;
                        break;
                    }
                }
                if (position!=-1) {
                    postList.remove(position);
                    Post post = new Post();
                    post.comments = new ArrayList<>();
                    post.key = dataSnapshot.getKey();
                    post.id = dataSnapshot.child("id").getValue().toString();
                    post.un = dataSnapshot.child("un").getValue().toString();
                    post.like_list = dataSnapshot.child("like_list").getValue().toString();
                    post.link = dataSnapshot.child("link").getValue().toString();
                    post.pp_link = dataSnapshot.child("pp_link").getValue().toString();
                    post.text = dataSnapshot.child("text").getValue().toString();
                    post.likes = Integer.parseInt(dataSnapshot.child("likes").getValue().toString());
                    SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String time=dataSnapshot.child("time").getValue().toString();
                    Long t=Long.parseLong(time);
                    post.time = localDateFormat.format(new Date(t));
                    DataSnapshot commentsSnapshot = dataSnapshot.child("comments");
                    for (DataSnapshot comment : commentsSnapshot.getChildren()) {
                        Comment newComment = new Comment();
                        newComment.user = comment.child("user").getValue().toString();
                        newComment.text = comment.child("text").getValue().toString();
                        post.comments.add(newComment);
                    }
                    postList.add(position,post);
                    MyRecyclerAdapter.this.notifyItemChanged(position);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_pic_thumb;
        public TextView username;
        public ImageView post_image;
        public TextView post_content;
        public ImageView like;
        public TextView numLikes;
        public TextView addComment;
        public LinearLayout commentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_pic_thumb = itemView.findViewById(R.id.profile_pic_thumb);
            username = itemView.findViewById(R.id.card_username);
            post_image = itemView.findViewById(R.id.post_pic);
            post_content = itemView.findViewById(R.id.post_text);
            like = itemView.findViewById(R.id.like_button);
            numLikes = itemView.findViewById(R.id.num_likes);
            addComment = itemView.findViewById(R.id.card_comment_link);
            commentLayout = itemView.findViewById(R.id.comment_view);
        }
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_wall, parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.username.setText(postList.get(position).un);
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("userID",postList.get(position).id);
                context.startActivity(intent);
            }
        });
        if (!postList.get(position).like_list.contains(user.getUid())) {
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = postList.get(position).key.toString();
                    DatabaseReference postRef = mRootReference.child("posts/"+key);
                    postRef.child("likes").setValue(++postList.get(position).likes);
                    postRef.child("like_list").setValue(postList.get(position).like_list+user.getUid()+",");
                }
            });
        } else {
            holder.like.setImageResource(R.drawable.ic_check_mark_dark);
        }

        holder.post_content.setText(postList.get(position).text);
        holder.numLikes.setText(postList.get(position).likes+" Likes");
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new CommentDialogFragment(postList.get(position).key);
                newFragment.show(((BaseActivity) context).getSupportFragmentManager(),"comment");
            }
        });
        LayoutInflater inflater = ((BaseActivity)context).getLayoutInflater();
        for (Comment comment : postList.get(position).comments) {
            View view = inflater.inflate(R.layout.comment_card,holder.commentLayout, false);
            TextView un = view.findViewById(R.id.comment_username);
            un.setText(comment.user);
            TextView txt = view.findViewById(R.id.comment_text);
            txt.setText(comment.text);
            holder.commentLayout.addView(view);
        }
        new BaseActivity.WorkerDownloadImage(context,holder.profile_pic_thumb).execute(postList.get(position).pp_link);
        new BaseActivity.WorkerDownloadImage(context,holder.post_image).execute(postList.get(position).link);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}