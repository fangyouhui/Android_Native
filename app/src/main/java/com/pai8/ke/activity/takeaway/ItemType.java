package com.pai8.ke.activity.takeaway;


import androidx.annotation.IntDef;


@IntDef({ItemType.BIG_SORT,ItemType.SMALL_SORT})
public @interface ItemType {
    int BIG_SORT = 0;
    int SMALL_SORT = 1;
}
