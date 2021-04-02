package com.pai8.ke.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.databinding.ItemCommentImageBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class CommentImageAdapter extends BaseRecyclerViewAdapter<LocalMedia> {

    public CommentImageAdapter(Context context, List<LocalMedia> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemCommentImageBinding binding = ItemCommentImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommentImageViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CommentImageViewHolder) {
            LocalMedia bean = getItem(position);
            CommentImageViewHolder holder = (CommentImageViewHolder) viewHolder;


        }

    }

    class CommentImageViewHolder extends BaseViewHolder<ItemCommentImageBinding> {

        public CommentImageViewHolder(@NonNull ItemCommentImageBinding viewBinding) {
            super(viewBinding);
        }
    }
}
