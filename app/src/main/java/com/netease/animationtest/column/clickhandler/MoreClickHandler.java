package com.netease.animationtest.column.clickhandler;

import com.netease.animationtest.column.ColumnController;
import com.netease.animationtest.column.adapters.ColumnBaseAdapter;

/**
 * Created by bjwangmingxian on 2017/9/14.
 */

public class MoreClickHandler implements ClickHandler {

    private ColumnController mController;

    public MoreClickHandler(ColumnController controller) {
        mController = controller;
    }

    @Override
    public void onItemClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove, boolean isInEditMode) {
        if (isInEditMode) {
            // step 1: remove more data and animation.
            // step 2: start translate animation.
            // step 3: add top data and animation.
            mController.moveMoreToTop(holder);
        } else {
            mController.gotoColumn(ColumnController.COLUMN_TYPE_MORE, holder);
        }
    }

    @Override
    public boolean onItemLongClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove, boolean isInEditMode) {
        return true;
    }
}
