package com.pai8.ke.activity.takeaway.contract;


import com.pai8.ke.activity.takeaway.entity.resq.CommentInfo;
import com.pai8.ke.base.BaseView;

import java.util.List;

/*
 */
public interface EvaluateContract {

    interface View extends BaseView {


        void getCommentSuccess(List<CommentInfo> data);

    }


}
