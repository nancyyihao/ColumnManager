package com.netease.animationtest.column.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.netease.animationtest.R;
import com.netease.animationtest.column.clickhandler.ClickHandler;
import com.netease.animationtest.column.clickhandler.MoreClickHandler;

import java.util.Arrays;


/**
 * Created by bjwangmingxian on 2017/9/14.
 *
 * 更多栏目、最近删除adapter
 */

public class ColumnOthersAdapter extends ColumnBaseAdapter<String> {

    public ColumnOthersAdapter(Context context, ClickHandler clickHandler) {
        super(context, clickHandler);
        if (clickHandler instanceof MoreClickHandler) {
            mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
        }
    }

    @Override
    protected void bindItemViewHolder(@NonNull ItemViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();
        if (pos == RecyclerView.NO_POSITION) return;

//        if (isInEditMode) {
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.milk_column_add);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.textView.setCompoundDrawables(drawable, null, null, null);
//        }
        holder.textView.setText(mItems.get(pos));
    }

}
