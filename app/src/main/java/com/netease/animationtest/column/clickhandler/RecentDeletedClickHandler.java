package com.netease.animationtest.column.clickhandler;

import com.netease.animationtest.column.ColumnController;
import com.netease.animationtest.column.adapters.ColumnBaseAdapter;

/**
 * Created by bjwangmingxian on 2017/9/14.
 */

public class RecentDeletedClickHandler implements ClickHandler {

    private ColumnController mController;

    public RecentDeletedClickHandler(ColumnController columnController) {
        mController = columnController;
    }

    @Override
    public void onItemClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove,
                            boolean isInEditMode) {
        if (isInEditMode) {
            mController.moveRecentDeletedToTop(holder);
        } else {
            mController.gotoColumn(ColumnController.COLUMN_TYPE_RECENT_DELETED, holder);
        }
    }

    @Override
    public boolean onItemLongClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove,
                                   boolean isInEditMode) {
        return true;
    }
}
