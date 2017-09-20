package com.netease.animationtest.column;

import com.netease.animationtest.column.adapters.ColumnBaseAdapter;

/**
 * Created by bjwangmingxian on 2017/9/13.
 */

public interface ColumnController {

    int COLUMN_TYPE_TOP = 1;
    int COLUMN_TYPE_RECENT_DELETED = 2;
    int COLUMN_TYPE_MORE = 3;

    void moveMoreToTop(ColumnBaseAdapter.ItemViewHolder holder);

    void gotoColumn(int columnType, ColumnBaseAdapter.ItemViewHolder holder);

    void moveRecentDeletedToTop(ColumnBaseAdapter.ItemViewHolder holder);

    void startDrag(ColumnBaseAdapter.ItemViewHolder holder);

    void moveTopToRecentDeleted(ColumnBaseAdapter.ItemViewHolder holder);
}
