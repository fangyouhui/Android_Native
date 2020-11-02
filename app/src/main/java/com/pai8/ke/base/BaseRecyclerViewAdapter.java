package com.pai8.ke.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import com.pai8.ke.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView-BaseAdapter
 * Created by gh on 2018/7/27.
 *
 * @param <T>
 */
public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;

    protected ArrayList<T> mDataList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position, 0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setDataList(Collection<T> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 在原List上添加
     *
     * @param list
     */
    public void addAll(Collection<T> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    /**
     * 移除指定postion
     *
     * @param position
     */
    public void remove(int position) {
        if (this.mDataList.size() > 0) {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 清空
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void add(T t) {
        this.mDataList.add(t);
    }

    /**
     * 替换指定索引的数据条目
     *
     * @param location
     * @param newData
     */
    public void replace(int location, T newData) {
        mDataList.set(location, newData);
        notifyDataSetChanged();
    }

    /**
     * 替换指定数据条目
     *
     * @param oldData
     * @param newData
     */
    public void replace(T oldData, T newData) {
        replace(mDataList.indexOf(oldData), newData);
    }


    /**
     * 交换两个数据条目的位置
     *
     * @param fromPosition
     * @param toPosition
     */
    public void move(int fromPosition, int toPosition) {
        Collections.swap(mDataList, fromPosition, toPosition);
        notifyDataSetChanged();
    }

}
