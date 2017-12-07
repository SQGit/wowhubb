package com.wowhubb.Utils;

import com.wowhubb.data.FeedItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramya on 04-02-2017.
 */
public class ItemDetailsWrapper implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<FeedItem> itemDetails;

    public ItemDetailsWrapper(List<FeedItem> items) {
        this.itemDetails = items;
    }

    public List<FeedItem> getItemDetails() {
        return itemDetails;
    }
}
