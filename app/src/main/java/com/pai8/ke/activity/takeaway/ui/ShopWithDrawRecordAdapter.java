package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShopWithDrawRecordAdapter extends BaseRecyclerViewAdapter<String> {
    public ShopWithDrawRecordAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int position) {

    }
}
