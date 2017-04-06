package com.nxt.xpengb.swiperecyclerview.viewholder;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Create : xpengb@outlook.com
 * Date   : 2017/4/6
 * Version: V1.0
 * Desc   :
 */

public class ViewHolderManager {

    private final String TAG = "ViewHolderManager";
    private int mViewType = 10;
    private Map<Class<? extends BaseViewHolder>, Integer> mHolderType;
    private Map<Integer, Class<? extends BaseViewHolder>> mTypeHolder;

    public ViewHolderManager() {
        mHolderType = new HashMap<>();
        mTypeHolder = new HashMap<>();
    }

    public void addViewHolder(Class<? extends BaseViewHolder> viewHolder) {
        if(mHolderType.containsKey(viewHolder)) {
            return;
        }
        Class dataClass = (Class) ((ParameterizedType) viewHolder.getGenericSuperclass()).getActualTypeArguments() [0];//获取ViewHolder的泛型数据class
        mViewType ++;
        mHolderType.put(viewHolder, mViewType);
        mTypeHolder.put(mViewType, viewHolder);
    }

    public int getViewType(Class<? extends BaseViewHolder> holder) {
        if (!mHolderType.containsKey(holder)){
           throw new NullPointerException("invoke addViewHolder method");
        }
        return mHolderType.get(holder);
    }

    public Class<? extends BaseViewHolder> getViewHolder(int viewType) {
        if (!mTypeHolder.containsKey(viewType)){
            throw new NullPointerException("invoke addViewHolder method");
        }
        return mTypeHolder.get(viewType);
    }
}
