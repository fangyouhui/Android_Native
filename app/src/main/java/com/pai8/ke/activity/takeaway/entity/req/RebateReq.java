package com.pai8.ke.activity.takeaway.entity.req;

import java.io.Serializable;

/**
 * Created by atian
 * on 1/14/21
 * describe:
 */

public class RebateReq implements Serializable {
    private String id;//商户id
    private String beat_rebate;//返点比例

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeat_rebate() {
        return beat_rebate;
    }

    public void setBeat_rebate(String beat_rebate) {
        this.beat_rebate = beat_rebate;
    }
}
