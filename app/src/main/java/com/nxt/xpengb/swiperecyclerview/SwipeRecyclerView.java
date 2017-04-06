package com.nxt.xpengb.swiperecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nxt.xpengb.swiperecyclerview.adapter.RecyclerViewAdapter;

/**
 * Create : xpengb@outlook.com
 * Date   : 2017/4/6
 * Version: V1.0
 * Desc   :
 */
public class SwipeRecyclerView extends FrameLayout{

    private final String TAG = "SwipeRecyclerView";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private boolean swipeAble;
    private boolean loadMoreAble;

    public SwipeRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.view_swipe_recyclerview, this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeRecyclerView);
        swipeAble = typedArray.getBoolean(R.styleable.SwipeRecyclerView_swipe_able, true);
        loadMoreAble = typedArray.getBoolean(R.styleable.SwipeRecyclerView_load_more_able, true);
        mSwipeRefreshLayout.setEnabled(loadMoreAble);
    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.loadMoreAble = loadMoreAble;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.getChildCount();
        layoutManager.getItemCount();
    }

    public void setRefreshAction(final Behavior behavior) {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                behavior.onBehavior();
            }
        });
    }

    public void setLoadMoreAction(final Behavior behavior) {
        Log.i(TAG, "setLoadMoreAction");
        if (mAdapter.isShowNoMore || !loadMoreAble) {
            return;
        }
        mAdapter.loadMoreAble = true;
        mAdapter.setLoadMoreBehavior(behavior);
    }

    public void showNoMore() {
        mAdapter.showNoMoreBehavior();
    }

    public void setItemSpace(int left, int top, int right, int bottom) {
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(left, top, right, bottom));
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public TextView getNoMoreView(){
        return mAdapter.mNoMoreView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
    }

    /**
     * 8位16进制数 ARGB
     */
    public void setSwipeRefreshColors(@ColorInt int... colors) {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    public void showSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
