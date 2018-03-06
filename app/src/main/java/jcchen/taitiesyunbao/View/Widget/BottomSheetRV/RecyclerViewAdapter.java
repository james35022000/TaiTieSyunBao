package jcchen.taitiesyunbao.View.Widget.BottomSheetRV;

import android.support.v7.widget.RecyclerView;

/**
 * Created by JCChen on 2018/2/12.
 */

public abstract class RecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private boolean AnimationState = true;

    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindViewHolder(position, holder);
        if(getAnimationState())
            displayContentAnimation(getItemViewType(position), holder, position);
    }

    public abstract void bindViewHolder(int position, VH viewHolder);

    public abstract void displayContentAnimation(int CARD, VH viewHolder, int index);

    public void setAnimationState(boolean state) {
        this.AnimationState = state;
    }

    private boolean getAnimationState() {
        return AnimationState;
    }
}
