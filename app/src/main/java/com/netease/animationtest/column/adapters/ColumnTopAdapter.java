package com.netease.animationtest.column.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.netease.animationtest.R;
import com.netease.animationtest.column.clickhandler.ClickHandler;

import java.util.Arrays;


/**
 * Created by bjwangmingxian on 2017/9/14.
 */

public class ColumnTopAdapter extends ColumnBaseAdapter<String> {

    private final static int HEADLINE_POSITION = 0;

    public ColumnTopAdapter(Context context, ClickHandler clickHandler) {
        super(context, clickHandler);
        // 头条默认不能动
        mForbidMoveItems.add(HEADLINE_POSITION);
        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }

    @Override
    protected void bindItemViewHolder(@NonNull ItemViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();
        if (pos == RecyclerView.NO_POSITION) return;

//        if (isInEditMode) {
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.milk_column_delete);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.textView.setCompoundDrawables(drawable, null, null, null);
//        }
        holder.textView.setText(mItems.get(pos));
    }
}
