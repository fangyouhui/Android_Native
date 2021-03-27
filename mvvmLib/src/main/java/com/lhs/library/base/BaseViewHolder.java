package com.lhs.library.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BaseViewHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {

    public T binding;

    public BaseViewHolder(@NonNull T viewbinding) {
        super(viewbinding.getRoot());
        this.binding = viewbinding;
    }
}
