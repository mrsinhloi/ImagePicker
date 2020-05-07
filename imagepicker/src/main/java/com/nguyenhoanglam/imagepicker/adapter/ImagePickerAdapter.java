package com.nguyenhoanglam.imagepicker.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nguyenhoanglam.imagepicker.R;
import com.nguyenhoanglam.imagepicker.listeners.OnImageClickListener;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.List;

/**
 * Created by hoanglam on 7/31/16.
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ImageViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Image> images;
    private List<Image> selectedImages;
    private OnImageClickListener itemClickListener;

    public static int getScreenWidth(Context context) {
        int widthScreen = 0;
        if (context != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                if (Build.VERSION.SDK_INT > 12) {
                    Point size = new Point();
                    display.getSize(size);
                    widthScreen = size.x;
                } else {
                    widthScreen = display.getWidth();  // Deprecated
                }
            }
        }
        return widthScreen;
    }

    private int size = 200;

    public ImagePickerAdapter(Context context, List<Image> images, List<Image> selectedImages, OnImageClickListener itemClickListener) {
        this.context = context;
        this.images = images;
        this.selectedImages = selectedImages;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(this.context);

        int screenWidth = getScreenWidth(context);
        size = screenWidth / 3;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {

        Image image = images.get(position);

        //////////////////////////////////////////////////////////////////////////
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(size, size)
                .placeholder(R.drawable.image_placeholder);

        Glide.with(context)
                .load(image.getPath())
                .apply(options)
                .into(viewHolder.imageView);
        //////////////////////////////////////////////////////////////////////////


        if (isSelected(image)) {
            viewHolder.alphaView.setAlpha(0.5f);
            ((FrameLayout) viewHolder.itemView).setForeground(ContextCompat.getDrawable(context, R.drawable.ic_done_white));
        } else {
            viewHolder.alphaView.setAlpha(0.0f);
            ((FrameLayout) viewHolder.itemView).setForeground(null);
        }

    }

    private boolean isSelected(Image image) {
        for (Image selectedImage : selectedImages) {
            if (selectedImage.getPath().equals(image.getPath())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public void setData(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void addAll(List<Image> images) {
        int startIndex = this.images.size();
        this.images.addAll(startIndex, images);
        notifyItemRangeInserted(startIndex, images.size());
    }

    public void addSelected(Image image) {
        selectedImages.add(image);
        notifyItemChanged(images.indexOf(image));
    }

    public void removeSelectedImage(Image image) {
        selectedImages.remove(image);
        notifyItemChanged(images.indexOf(image));
    }

    public void removeSelectedPosition(int position, int clickPosition) {
        selectedImages.remove(position);
        notifyItemChanged(clickPosition);
    }

    public void removeAllSelectedSingleClick() {
        selectedImages.clear();
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private View alphaView;
        private final OnImageClickListener itemClickListener;

        public ImageViewHolder(View itemView, OnImageClickListener itemClickListener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            alphaView = itemView.findViewById(R.id.view_alpha);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.setSelected(true);
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }


}
