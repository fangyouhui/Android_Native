package com.lhs.library.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.R;
import com.lhs.library.databinding.ItemCommonEmptyBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context mContext;
    protected List<T> mData;

    protected final int ITEM_VIEW_TYPE_EMPTY = 2001;
    protected final int ITEM_VIEW_TYPE_NORMAL = 2002;

    protected BaseItemTouchListener<T> mListener;

    protected boolean isShowEmptyView;

    public void setListener(BaseItemTouchListener<T> listener) {
        this.mListener = listener;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this(context, list, true);
    }

    public BaseRecyclerViewAdapter(Context mContext, List<T> list, boolean isShowEmptyView) {
        this.mContext = mContext;
        this.mData = list == null ? (List<T>) new ArrayList<>() : list;
        this.isShowEmptyView = isShowEmptyView;
    }

    public void setData(List<T> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowEmptyView && (mData == null || mData.isEmpty())) {
            return ITEM_VIEW_TYPE_EMPTY;
        }
        return getItemViewTypeWithNormal(position);
    }

    public int getItemViewTypeWithNormal(int position) {
        return ITEM_VIEW_TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isShowEmptyView && viewType == ITEM_VIEW_TYPE_EMPTY) {
            return onCreateEmptyViewHolder(parent, viewType);
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonEmptyViewHolder) {
            return;
        }
        onBindNormalViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return mData == null || mData.size() == 0 ? (isShowEmptyView ? 1 : 0) : mData.size();
    }

    protected RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common_empty, parent, false);
        return new CommonEmptyViewHolder(view);
    }

    protected abstract RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position);

    protected static class CommonEmptyViewHolder extends RecyclerView.ViewHolder {
        private ItemCommonEmptyBinding binding;

        public CommonEmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonEmptyBinding.inflate(LayoutInflater.from(itemView.getContext()));
        }
    }


    public interface BaseItemTouchListener<T> {
        void onItemClick(T item, int position);

    }
}
