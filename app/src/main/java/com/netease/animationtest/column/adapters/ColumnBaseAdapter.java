package com.netease.animationtest.column.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.netease.animationtest.R;
import com.netease.animationtest.column.clickhandler.ClickHandler;
import com.netease.animationtest.column.listeners.OnItemMoveListener;
import com.netease.animationtest.column.listeners.OnItemStateChangedListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bjwangmingxian on 2017/9/12.
 */

public abstract class ColumnBaseAdapter<T> extends RecyclerView.Adapter<ColumnBaseAdapter.ItemViewHolder>
        implements OnItemMoveListener {


    protected final List<T> mItems = new ArrayList<>();

    protected final  Set<Integer> mForbidMoveItems = new HashSet<>();

    protected final ClickHandler mClickHandler;

    protected boolean isInEditMode;

    protected Context mContext;


    public ColumnBaseAdapter(Context context, ClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public final void startEditMode() {
        isInEditMode = true;
        notifyDataSetChanged();
    }

    public final void cancelEditMode() {
        isInEditMode = false;
        notifyDataSetChanged();
    }

    public void notifyAdd(T data, boolean doAnimation) {
        if (data == null) return;
        notifyAdd(mItems.size(), data, doAnimation);
    }

    public void notifyAdd(int position, T data, boolean doAnimation) {
        if (position < 0 || position > mItems.size()) return;
        mItems.add(position, data);
        if (doAnimation) {
            notifyItemInserted(position);
        } else {
            notifyDataSetChanged();
        }
    }

    public void notifyRemove(int position) {
        notifyRemove(position, false);
    }

    public void notifyRemove(int position, boolean doAnimation) {
        if (position < 0 || position >= mItems.size()) return;
        mItems.remove(position);
        if (doAnimation) {
            notifyItemRemoved(position);
        } else {
            notifyDataSetChanged();
        }
    }

    public T getItemData(int position) {
        return position < mItems.size() && position > -1 ? mItems.get(position) : null;
    }

    public boolean isForbidMovePosition(int position) {
        return mForbidMoveItems.contains(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout_item,
                parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        if (holder == null) return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickHandler != null) {
                    mClickHandler.onItemClick(
                            holder,
                            isForbidMovePosition(holder.getAdapterPosition()),
                            isInEditMode);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return mClickHandler != null
                        && mClickHandler.onItemLongClick(
                            holder,
                            isForbidMovePosition(holder.getAdapterPosition()),
                            isInEditMode);
            }
        });
        bindItemViewHolder(holder, position);
    }

    protected abstract void bindItemViewHolder(@NonNull final ItemViewHolder holder, final int position) ;

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (isForbidMovePosition(toPosition)) {
            return true;
        }
        T data = mItems.get(fromPosition);
        mItems.remove(fromPosition);
        mItems.add(toPosition, data);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            OnItemStateChangedListener {

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemSelected() {
            scaleItem(1.0f, 1.2f, 1.0f);
        }

        @Override
        public void onItemClear() {
            scaleItem(1.2f, 1.0f, 1.0f);
        }

        private void scaleItem(float start, float end, float alpha) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(itemView, "scaleX",
                    start, end);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(itemView, "scaleY",
                    start, end);
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(itemView, "alpha",
                    alpha);

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(200);
            animSet.setInterpolator(new LinearInterpolator());
            animSet.playTogether(anim1, anim2, anim3);
            animSet.start();
        }
    }
}
