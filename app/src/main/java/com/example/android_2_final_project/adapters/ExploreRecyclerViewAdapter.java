package com.example.android_2_final_project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.android_2_final_project.R;
import com.example.android_2_final_project.models.CarModel;
import com.example.android_2_final_project.models.PostModel;

import java.util.List;

public class ExploreRecyclerViewAdapter extends RecyclerView.Adapter<ExploreRecyclerViewAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private List<PostModel> mPosts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    public ExploreRecyclerViewAdapter(Context context, List<PostModel> posts) {
        mInflater = LayoutInflater.from(context);
        mPosts = posts;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_explorer_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CarModel car = mPosts.get(position).getCar();

        Glide.with(mContext)
                .asBitmap()
                .load(car.getImagePath())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {

                        holder.image.setImageBitmap(resource);
                        Palette p = Palette.from(resource).generate();

                        holder.carModelTv.setTextColor(p.getMutedColor(mContext.getResources().getColor(android.R.color.darker_gray)));
                        holder.manufactureYearTv.setTextColor(p.getMutedColor(mContext.getResources().getColor(android.R.color.darker_gray)));
                    }
                });

        holder.carModelTv.setText(car.getCarModel());
        holder.manufactureYearTv.setText(mContext.getString(R.string.empty_string, car.getManufactureYear()));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView carModelTv;
        TextView manufactureYearTv;
        TextView descriptionTv;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.explore_cell_imageView);
            carModelTv = itemView.findViewById(R.id.car_model_tv);
            manufactureYearTv = itemView.findViewById(R.id.manufacture_year_text_view);
            descriptionTv = itemView.findViewById(R.id.description_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    public CarModel getItem(int id) {
        return mPosts.get(id).getCar();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}