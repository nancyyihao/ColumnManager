package com.netease.animationtest.column.clickhandler;

import com.netease.animationtest.column.ColumnController;
import com.netease.animationtest.column.adapters.ColumnBaseAdapter;

/**
 * Created by bjwangmingxian on 2017/9/14.
 */

public class TopClickHandler implements ClickHandler {

    private ColumnController mController;

    public TopClickHandler(ColumnController controller) {
        mController = controller;
    }


    @Override
    public void onItemClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove, boolean isInEditMode) {
        if (isForbidMove) return;
        if (isInEditMode) {
            // step 1: remove top data and animation.
            // step 2: start translate animation.
            // step 3: recent deleted data insert and animation.
            mController.moveTopToRecentDeleted(holder);
        } else {
            mController.gotoColumn(ColumnController.COLUMN_TYPE_TOP, holder);
        }
    }

    @Override
    public boolean onItemLongClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove, boolean isInEditMode) {
        if (isInEditMode && !isForbidMove) {
            // start drag
            mController.startDrag(holder);
        }
        return true;
    }
}
