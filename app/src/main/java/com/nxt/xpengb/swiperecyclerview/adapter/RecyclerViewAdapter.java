package com.nxt.xpengb.swiperecyclerview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxt.xpengb.swiperecyclerview.Behavior;
import com.nxt.xpengb.swiperecyclerview.R;
import com.nxt.xpengb.swiperecyclerview.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create : xpengb@outlook.com
 * Date   : 2017/4/6
 * Version: V1.0
 * Desc   :
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>>{

    private static final String TAG = "RecyclerViewAdapter";
    private static final int HEADER_TYPE = 111;
    private static final int FOOTER_TYPE = 222;
    protected static final int STATUS_TYPE = 333;
    protected int mViewCount = 0;

    private boolean allowLog = true;
    private boolean hasHeader = false;
    private boolean hasFooter = false;
    public boolean isShowNoMore = false;
    public boolean loadMoreAble = false;

    protected Behavior mLoadMoreBehavior;

    private List<T> mData = new ArrayList<>();

    private View headerView;
    private View footerView;
    protected View mStatusView;
    protected LinearLayout mLoadMoreView;
    public TextView mNoMoreView;

    private Context mContext;

    public void closeLog() {
        allowLog = false;
    }

    public RecyclerViewAdapter(Context context) {
        mContext = context;
        initStatusView(context);
    }

    public RecyclerViewAdapter(Context context, T[] data) {
        this(context, Arrays.asList(data));
    }

    public RecyclerViewAdapter(Context context, List<T> data) {
        mContext = context;
        initStatusView(context);
        this.mData = data;
        mViewCount += data.size();
        notifyDataSetChanged();
    }

    public void initStatusView(Context context) {
        mStatusView = LayoutInflater.from(context).inflate(R.layout.view_status_last, null);
        mLoadMoreView = (LinearLayout) mStatusView.findViewById(R.id.load_more_view);
        mNoMoreView = (TextView)mStatusView.findViewById(R.id.no_more_view);
        mViewCount++;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER_TYPE) {
            return new BaseViewHolder<>(headerView);
        }else if(viewType == FOOTER_TYPE) {
            return new BaseViewHolder<>(footerView);
        }else if(viewType == STATUS_TYPE) {
            return new BaseViewHolder<>(mStatusView);
        }
        return onCreateBaseViewHolder(parent, viewType);
    }

    public abstract BaseViewHolder<T> onCreateBaseViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        log("onBindViewHolder()  viewCount : " + mViewCount + " position : " + position);
        if(position == 0) {
            if(mViewCount == 1 || hasHeader) {
                return;
            }else {
                holder.setData(mData.get(0));
            }
        }else if (!hasHeader && !hasFooter && position < mData.size()) { //没有Header和Footer
            holder.setData(mData.get(position));
        } else if (hasHeader && !hasFooter && position > 0 && position < mViewCount - 1) { //有Header没有Footer
            holder.setData(mData.get(position - 1));
        } else if (!hasHeader && position < mViewCount - 2) { //没有Header，有Footer
            holder.setData(mData.get(position));
        } else if (position > 0 && position < mViewCount - 2) { //都有
            holder.setData(mData.get(position - 1));
        }

        // 最后一个可见的 item 时 加载更多。解决 remove 时 bug
        int positionEnd;
        if ((hasHeader && hasFooter) || (!hasHeader && hasFooter)) {
            positionEnd = mViewCount - 3;
        } else {
            positionEnd = mViewCount - 2;
        }
        if (loadMoreAble && !isShowNoMore && position == positionEnd) {
            mLoadMoreView.setVisibility(View.VISIBLE);
            if (mLoadMoreBehavior != null) {
                mLoadMoreBehavior.onBehavior();
            }
        }
    }

    @Override
    public int getItemViewType(int postion) {
        if(hasHeader && postion == 0) { //header
            return HEADER_TYPE;
        }
        if(hasFooter && postion == mViewCount - 2) { //footer
            return FOOTER_TYPE;
        }
        if (postion == mViewCount - 1) { //the last view status
            return STATUS_TYPE;
        }
        return super.getItemViewType(postion);
    }

    @Override
    public int getItemCount() {
        return mViewCount;
    }

    public void showNoMoreBehavior() {
        isShowNoMore = true;
        mLoadMoreView.post(new Runnable() {
            @Override
            public void run() {
                mLoadMoreView.setVisibility(View.GONE);
                mNoMoreView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void openLoadMoreBehavior() {
        isShowNoMore = false;
        mLoadMoreView.post(new Runnable() {
            @Override
            public void run() {
                mLoadMoreView.setVisibility(View.VISIBLE);
                mNoMoreView.setVisibility(View.GONE);
            }
        });
    }

    public void setLoadMoreBehavior(Behavior behavior) {
        mLoadMoreBehavior = behavior;
    }

    public void add(T object) {
        if (!isShowNoMore) {
            mData.add(object);
            int position;
            if (hasFooter) {
                position = mViewCount - 2;
            } else {
                position = mViewCount - 1;
            }
            mViewCount++;
            notifyItemInserted(position);
        }
    }

    public void insert(T object, int itemPosition) {
        if (mData != null && itemPosition < mViewCount) {
            int dataPosition;
            if(hasHeader){
                dataPosition = itemPosition - 1;
            }else {
                dataPosition = itemPosition;
            }
            mData.add(dataPosition, object);
            mViewCount++;
            notifyItemInserted(itemPosition);
        }
    }

    public void addAll(List<T> data) {
        int size = data.size();
        if (!isShowNoMore && size > 0) {
            mData.addAll(data);
            int positionStart;
            if (hasFooter) {
                positionStart = mViewCount - 2;
            } else {
                positionStart = mViewCount - 1;
            }
            mViewCount += size;
            notifyItemRangeInserted(positionStart, size);
            log("addAll()  startPosition : " + positionStart + "  itemCount : " + size);
        }
    }

    public void addAll(T[] objects) {
        addAll(Arrays.asList(objects));
    }

    public void replace(T object, int itemPosition) {
        if(mData != null){
            int dataPosition;
            if(hasHeader){
                dataPosition = itemPosition - 1;
            }else {
                dataPosition = itemPosition;
            }
            if(dataPosition < mData.size()){
                mData.set(dataPosition, object);
                mViewCount++;
                notifyItemChanged(itemPosition);
            }
        }
    }

    //position start with 0
    public void remove(T object) {
        if (!mData.contains(object)) {
            Toast.makeText(getContext(),"删除失败",Toast.LENGTH_SHORT).show();
            log("remove()  without the object : " + object.getClass().getName());
            return;
        }
        int dataPosition = mData.indexOf(object);
        int itemPosition;
        if(hasHeader){
            itemPosition = dataPosition + 1;
        }else {
            itemPosition = dataPosition;
        }
        remove(itemPosition);
    }

    //positionItem start with 0
    public void remove(int itemPosition) {
        int dataPosition;
        int dataSize = mData.size();
        if (hasHeader) {
            dataPosition = itemPosition - 1;
            if (dataPosition >= 0 && dataPosition < dataSize) {
                mData.remove(dataPosition);
                notifyItemRemoved(itemPosition);
                mViewCount--;
            } else if (dataPosition >= dataSize) {
                Toast.makeText(getContext(),"删除失败", Toast.LENGTH_SHORT).show();
            } else {
                throw new IndexOutOfBoundsException("RecyclerView has header,position is should more than 0 ." +
                        "if you want remove header , pleasure user removeHeader()");
            }
        } else {
            dataPosition = itemPosition;
            if (dataPosition >= dataSize) {
                Toast.makeText(getContext(),"删除失败",Toast.LENGTH_SHORT).show();
            } else {
                mData.remove(dataPosition);
                notifyItemRemoved(itemPosition);
                mViewCount--;
            }
        }
    }

    public void clear() {
        if (mData == null) {
            log("clear() mData is null");
            return;
        }
        mData.clear();
        mViewCount = 1;
        if (hasHeader) {
            mViewCount++;
        }
        if (hasFooter) {
            mViewCount++;
        }
        isShowNoMore = false;
        mLoadMoreView.setVisibility(View.GONE);
        mNoMoreView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }


    public void setHeader(View header) {
        hasHeader = true;
        headerView = header;
        mViewCount++;
    }

    public void setHeader(@LayoutRes int res) {
        setHeader(LayoutInflater.from(mContext).inflate(res, null));
    }

    public View getHeader() {
        return headerView;
    }

    public View getFooter() {
        return footerView;
    }

    public void setFooter(View footer) {
        hasFooter = true;
        footerView = footer;
        mViewCount++;
    }

    public void setFooter(@LayoutRes int res) {
        setFooter(LayoutInflater.from(mContext).inflate(res, null));
    }

    public void removeHeader() {
        if (hasHeader) {
            hasHeader = false;
            notifyItemRemoved(0);
        }
    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
            notifyItemRemoved(mViewCount - 2);
        }
    }

    public List<T> getData() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    protected void log(String content) {
        if(allowLog) {
            Log.i(TAG, content);
        }
    }
}
