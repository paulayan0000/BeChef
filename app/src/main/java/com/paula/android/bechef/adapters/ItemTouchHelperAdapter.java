package com.paula.android.bechef.adapters;

interface ItemTouchHelperAdapter {
    boolean onItemMoved(int fromPosition, int toPosition);

    void onItemSwiped(int position);

    int getItemMovementFlags();
}
