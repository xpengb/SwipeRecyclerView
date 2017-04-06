package com.nxt.xpengb.swiperecyclerview.viewholder;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create : xpengb@outlook.com
 * Date   : 2017/4/6
 * Version: V1.0
 * Desc   :
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private final String TAG = " BaseViewHolder";

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        onInitializeView();
    }

    public void onInitializeView() {

    }

    public <T extends View> T findViewById(@IdRes int resId) {
        return (T) itemView.findViewById(resId);
    }

    public void setData(final T data) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemViewClick(data);
            }
        });
    }

    public void onItemViewClick (T data) {

    }
}
