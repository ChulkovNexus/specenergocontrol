package com.specenergocontrol.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;

import com.specenergocontrol.R;

import java.util.List;

/**
 * Created by Комп on 24.07.2015.
 */
public class PagingExpandableListView  {

//    private boolean isLoading;
//    private boolean hasMoreItems;
//    private Pagingable pagingableListener;
//    private LoadingView loadingView;
//    private OnScrollListener onScrollListener;
//
//    public PagingExpandableListView(Context context) {
//        super(context);
//        init();
//    }
//
//    public PagingExpandableListView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public PagingExpandableListView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init();
//    }
//
//    public boolean isLoading() {
//        return this.isLoading;
//    }
//
//    public void setIsLoading(boolean isLoading) {
//        this.isLoading = isLoading;
//    }
//
//    public void setPagingableListener(Pagingable pagingableListener) {
//        this.pagingableListener = pagingableListener;
//    }
//
//    public void setHasMoreItems(boolean hasMoreItems) {
//        this.hasMoreItems = hasMoreItems;
//        if (!this.hasMoreItems) {
//            removeFooterView(loadingView);
//        } else if (findViewById(R.id.loading_view) == null) {
//            addFooterView(loadingView);
//            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
//            setAdapter(adapter);
//        }
//    }
//
//    public boolean hasMoreItems() {
//        return this.hasMoreItems;
//    }
//
//    public void onFinishLoading(boolean hasMoreItems, List<? extends Object> newItems) {
//        setHasMoreItems(hasMoreItems);
//        setIsLoading(false);
//        if (newItems != null && newItems.size() > 0) {
//            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
//            if (adapter instanceof PagingBaseAdapter) {
//                ((PagingBaseAdapter) adapter).addMoreItems(newItems);
//            }
//        }
//    }
//
//    private void init() {
//        isLoading = false;
//        loadingView = new LoadingView(getContext());
//        addFooterView(loadingView);
//        super.setOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                //Dispatch to child OnScrollListener
//                if (onScrollListener != null) {
//                    onScrollListener.onScrollStateChanged(view, scrollState);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                //Dispatch to child OnScrollListener
//                if (onScrollListener != null) {
//                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//                }
//
//                int lastVisibleItem = firstVisibleItem + visibleItemCount;
//                if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
//                    if (pagingableListener != null) {
//                        isLoading = true;
//                        pagingableListener.onLoadMoreItems();
//                    }
//
//                }
//            }
//        });
//    }
//
//    @Override
//    public void setOnScrollListener(OnScrollListener listener) {
//        onScrollListener = listener;
//    }
//
//    public interface Pagingable {
//        void onLoadMoreItems();
//    }
}