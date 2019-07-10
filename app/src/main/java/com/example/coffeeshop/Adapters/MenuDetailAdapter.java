package com.example.coffeeshop.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.coffeeshop.R;

public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.MyViewHolder> {

    AppCompatActivity activity;

    public MenuDetailAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menudetails_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img_Like;
        int a = 0;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.menulist_cardview);
            img_Like = itemView.findViewById(R.id.img_like);

            img_Like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  setImageAnimation();
                }
            });
        }


        public void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
            final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
            final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
            anim_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setImageResource(new_image);
                    anim_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }
                    });
                    v.startAnimation(anim_in);
                }
            });
            v.startAnimation(anim_out);
        }

        private void setImageAnimation(){
            if (a > 0) {
                a--;
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_outline);
            }
            else {
                a++;
                ImageViewAnimatedChange(itemView.getContext(), img_Like, R.drawable.heart_fillcolor);
            }

        }


    }

}
