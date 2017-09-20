package com.netease.animationtest.column;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.animationtest.R;
import com.netease.animationtest.column.adapters.ColumnBaseAdapter;
import com.netease.animationtest.column.adapters.ColumnOthersAdapter;
import com.netease.animationtest.column.adapters.ColumnTopAdapter;
import com.netease.animationtest.column.clickhandler.MoreClickHandler;
import com.netease.animationtest.column.clickhandler.RecentDeletedClickHandler;
import com.netease.animationtest.column.clickhandler.TopClickHandler;
import com.netease.animationtest.column.utils.ItemTouchHelperCallback;

/**
 * Created by bjwangmingxian on 2017/9/13.
 */

public class ColumnTopEditFragment extends Fragment implements ColumnController {

    private static final int SPAN_COUNT = 4;

    private static final int ANIMATION_DURATION = 300;

    private ItemTouchHelper mItemTouchHelper;
    ColumnOthersAdapter mRecentDeletedAdapter;
    ColumnOthersAdapter mMoreAdapter;
    ColumnTopAdapter mTopAdapter;

    RecyclerView mDeletedRecyclerView;
    RecyclerView mMoreRecyclerView;
    RecyclerView mTopRecyclerView;

    TextView mMyColumnsView;
    View mDeletedHintView;
    View mMoreHintView;

    Animation mAnimation;
    Bitmap mBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMyColumnsView = (TextView) view.findViewById(R.id.my_columns);

        mTopRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top);
        mTopAdapter = new ColumnTopAdapter(getContext(), new TopClickHandler(this));
        mTopRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mTopRecyclerView.setAdapter(mTopAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mTopAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mTopRecyclerView);

        mDeletedHintView = view.findViewById(R.id.recent_deleted_columns);
        mDeletedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_deleted);
        mRecentDeletedAdapter = new ColumnOthersAdapter(getContext(),
                new RecentDeletedClickHandler(this));
        mDeletedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mDeletedRecyclerView.setAdapter(mRecentDeletedAdapter);

        mMoreHintView = view.findViewById(R.id.more_columns);
        mMoreRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_more);
        mMoreAdapter = new ColumnOthersAdapter(getContext(), new MoreClickHandler(this));
        mMoreRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mMoreRecyclerView.setAdapter(mMoreAdapter);

        mMoreAdapter.startEditMode();
        mRecentDeletedAdapter.startEditMode();
        mTopAdapter.startEditMode();
    }

    @Override
    public void moveTopToRecentDeleted(ColumnBaseAdapter.ItemViewHolder holder) {

        int pos = holder.getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            moveTopToDeleted(holder, pos);
        }
    }

    @Override
    public void moveRecentDeletedToTop(ColumnBaseAdapter.ItemViewHolder holder) {

        int pos = holder.getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            moveDeletedToTop(holder, pos);
        }
    }

    @Override
    public void moveMoreToTop(ColumnBaseAdapter.ItemViewHolder holder) {
        int pos = holder.getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
            moveMoreToTop(holder, pos);
        }
    }

    @Override
    public void gotoColumn(int columnType, ColumnBaseAdapter.ItemViewHolder holder) {
        // finish Edit.
    }

    @Override
    public void startDrag(ColumnBaseAdapter.ItemViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    private void moveTopToDeleted(ColumnBaseAdapter.ItemViewHolder holder, final int pos) {

        final ViewGroup rootContentView = getRootViewGroup();
        if (rootContentView == null) return;

        final View currentView = holder.itemView;
        final View mirrorView = createMirrorView(currentView);

        currentView.setClickable(false);
        mDeletedHintView.setVisibility(View.VISIBLE);
        mDeletedRecyclerView.setVisibility(View.VISIBLE);

        int startLocation[] = new int[2];
        currentView.getLocationOnScreen(startLocation);

        int endLocation[] = getTargetLocation();

        final View mMoveView = addFloatView(rootContentView, mirrorView, currentView);

        mAnimation = new TranslateAnimation(0, endLocation[0] - startLocation[0], 0,
                endLocation[1] - startLocation[1]);
        mAnimation.setDuration(ANIMATION_DURATION);
        mAnimation.setFillEnabled(false);
        mAnimation.setFillBefore(false);
        mAnimation.setFillAfter(false);
        mMoveView.startAnimation(mAnimation);

        final String data = mTopAdapter.getItemData(pos);
        mTopAdapter.notifyRemove(pos, true);

        mAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rootContentView != null) rootContentView.removeView(mMoveView);
                if (currentView != null) currentView.setClickable(true);
                if (mRecentDeletedAdapter != null) mRecentDeletedAdapter.notifyAdd(data, false);
            }
        });
    }

    private int[] getTargetLocation() {

        int deleteSpanCount = ((GridLayoutManager) mDeletedRecyclerView.getLayoutManager())
                .getSpanCount();
        int topSpanCount = ((GridLayoutManager) mTopRecyclerView.getLayoutManager()).getSpanCount();
        int deletedChildCount = mDeletedRecyclerView.getChildCount();
        int topChildCount = mTopRecyclerView.getChildCount();
        final View fakeTargetView = mDeletedRecyclerView.getChildAt(deletedChildCount - 1);

        int targetX = 0;
        int targetY = 0;
        int location[] = new int[2];

        if (fakeTargetView == null) {
            mDeletedHintView.getLocationOnScreen(location);
            targetX = 0;
            targetY = location[1] ;
        } else {
            fakeTargetView.getLocationOnScreen(location);
            targetX = location[0] + fakeTargetView.getMeasuredWidth();
            targetY = location[1];

            if (deletedChildCount % deleteSpanCount == 0) {
                targetX = 0;
            }
            if (deletedChildCount % deleteSpanCount == 0 && topChildCount % topSpanCount != 1) {
                targetY += fakeTargetView.getMeasuredHeight();
            }
            if (topChildCount % topSpanCount == 1 && deletedChildCount % deleteSpanCount != 0) {
                targetY -= fakeTargetView.getMeasuredHeight();
            }
        }
        location[0] = targetX;
        location[1] = targetY;

        return location;
    }

    private void moveDeletedToTop(ColumnBaseAdapter.ItemViewHolder holder, final int pos) {

        final View currentView = holder.itemView;
        View mirrorView = createMirrorView(currentView);
        currentView.setClickable(false);

        int spanCount = ((GridLayoutManager) mTopRecyclerView.getLayoutManager()).getSpanCount();
        int childCount = mTopRecyclerView.getChildCount();
        View targetView = mTopRecyclerView.getChildAt(childCount - 1);
        int targetX = 0;
        int targetY = 0;
        if (targetView != null) {

            int startLocation[] = new int[2];
            currentView.getLocationOnScreen(startLocation);

            int endLocation[] = new int[2];
            targetView.getLocationOnScreen(endLocation);

            targetX = endLocation[0] + targetView.getMeasuredWidth();
            targetY = endLocation[1];

            if (childCount % spanCount == 0) {
                targetX = 0;
                targetY += targetView.getMeasuredHeight();
            }

            endLocation[0] = targetX;
            endLocation[1] = targetY;

            final ViewGroup moveViewGroup = getRootViewGroup();
            final View mMoveView = addFloatView(moveViewGroup, mirrorView, currentView);

            mAnimation = new TranslateAnimation(0, endLocation[0] - startLocation[0], 0,
                    endLocation[1] - startLocation[1]);
            mAnimation.setDuration(ANIMATION_DURATION);
            mAnimation.setFillAfter(false);

            final String data = mRecentDeletedAdapter.getItemData(pos);
            mRecentDeletedAdapter.notifyRemove(pos, true);

            mMoveView.startAnimation(mAnimation);

            mAnimation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (moveViewGroup != null) moveViewGroup.removeView(mMoveView);
                    if (currentView != null) currentView.setClickable(true);
                    if (mTopAdapter != null) mTopAdapter.notifyAdd(data, false);

                    if (mRecentDeletedAdapter != null) {
                        boolean visible = mRecentDeletedAdapter.getItemCount() > 0;
                        if (mDeletedRecyclerView != null)
                            mDeletedRecyclerView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        if (mDeletedHintView != null)
                            mDeletedHintView.setVisibility(visible ? View.VISIBLE : View.GONE);
                    }
                }
            });
        }
    }

    private void moveMoreToTop(ColumnBaseAdapter.ItemViewHolder holder, final int pos) {

        final View currentView = holder.itemView;
        currentView.setClickable(false);
        View mirrorView = createMirrorView(currentView);

        int spanCount = ((GridLayoutManager) mTopRecyclerView.getLayoutManager()).getSpanCount();
        int childCount = mTopRecyclerView.getChildCount();
        View targetView = mTopRecyclerView.getChildAt(childCount - 1);
        int targetX = 0;
        int targetY = 0;
        if (targetView != null) {

            int startLocation[] = new int[2];
            currentView.getLocationOnScreen(startLocation);

            int endLocation[] = new int[2];
            targetView.getLocationOnScreen(endLocation);

            targetX = endLocation[0] + targetView.getMeasuredWidth();
            targetY = endLocation[1];

            if (childCount % spanCount == 0) {
                targetX = 0;
                targetY += targetView.getMeasuredHeight();
            }

            endLocation[0] = targetX;
            endLocation[1] = targetY;

            final ViewGroup moveViewGroup = getRootViewGroup();
            final View mMoveView = addFloatView(moveViewGroup, mirrorView, currentView);

            mAnimation = new TranslateAnimation(0, endLocation[0] - startLocation[0], 0,
                    endLocation[1] - startLocation[1]);
            mAnimation.setDuration(ANIMATION_DURATION);
            mAnimation.setFillAfter(false);

            final String data = mMoreAdapter.getItemData(pos);
            mMoreAdapter.notifyRemove(pos, true);

            mMoveView.startAnimation(mAnimation);

            mAnimation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (moveViewGroup != null) moveViewGroup.removeView(mMoveView);
                    if (currentView != null) currentView.setClickable(true);
                    if (mTopAdapter != null) mTopAdapter.notifyAdd(data, false);

                    if (mMoreAdapter != null) {
                        boolean visible = mMoreAdapter.getItemCount() > 0;
                        if (mMoreRecyclerView != null)
                            mMoreRecyclerView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        if (mMoreHintView != null)
                            mMoreHintView.setVisibility(visible ? View.VISIBLE : View.GONE);
                    }
                }
            });
        }
    }

    private ViewGroup getRootViewGroup() {
        return (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id
                .content);
    }

    private View addFloatView(ViewGroup viewGroup, View moveView, View currentView) {

        int[] childLocation = new int[2];
        currentView.getLocationOnScreen(childLocation);

        int[] parentLocation = new int[2];
        viewGroup.getLocationOnScreen(parentLocation);

        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = childLocation[0] - parentLocation[0];
        mLayoutParams.topMargin = childLocation[1] - parentLocation[1];
        moveView.setLayoutParams(mLayoutParams);
        viewGroup.addView(moveView);
        return moveView;
    }

    private ImageView createMirrorView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = Bitmap.createBitmap(view.getDrawingCache());
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(mBitmap);
        view.setDrawingCacheEnabled(false);
        return iv;
    }
}
