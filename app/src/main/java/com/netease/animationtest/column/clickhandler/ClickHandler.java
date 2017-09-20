package com.netease.animationtest.column.clickhandler;

import com.netease.animationtest.column.adapters.ColumnBaseAdapter;

/**
 * Created by bjwangmingxian on 2017/9/14.
 *
 */

public interface ClickHandler {
    /**
     * Call when item click
     * @param holder which holder that user click.
     * @param isForbidMove true if this holder can be moved, false otherwise.
     * @param isInEditMode true if Adapter in Edit Mode, false otherwise.
     */
    void onItemClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove, boolean isInEditMode);

    /**
     * Call when item long click
     * @param holder which holder that user click.
     * @param isForbidMove true if this holder can be moved, false otherwise.
     * @param isInEditMode true if Adapter in Edit Mode, false otherwise.
     */
    boolean onItemLongClick(ColumnBaseAdapter.ItemViewHolder holder, boolean isForbidMove, boolean isInEditMode);
}
