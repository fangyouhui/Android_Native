package com.pai8.ke.activity.message.entity.resp;

/**
 * @author Created by zzf
 * @time 2020/12/7 21:33
 * Description：
 */
public class MsgCountResp {

    /**
     * sys_msg : {"content":"","count":0}
     * agree_msg : {"content":"","count":0}
     * comment_msg : {"content":"","count":0}
     * focus_msg : {"content":"","count":0}
     * order_msg : {"content":"","count":0}
     * eve_msg : {"content":"","count":0}
     * call_msg : {"content":"","count":0}
     */

    private MsgDetailBean sys_msg;
    private MsgDetailBean agree_msg;
    private MsgDetailBean comment_msg;
    private MsgDetailBean focus_msg;
    private MsgDetailBean order_msg;
    private MsgDetailBean eve_msg;
    private MsgDetailBean call_msg;

    public MsgDetailBean getSys_msg() {
        return sys_msg;
    }

    public void setSys_msg(MsgDetailBean sys_msg) {
        this.sys_msg = sys_msg;
    }

    public MsgDetailBean getAgree_msg() {
        return agree_msg;
    }

    public void setAgree_msg(MsgDetailBean agree_msg) {
        this.agree_msg = agree_msg;
    }

    public MsgDetailBean getComment_msg() {
        return comment_msg;
    }

    public void setComment_msg(MsgDetailBean comment_msg) {
        this.comment_msg = comment_msg;
    }

    public MsgDetailBean getFocus_msg() {
        return focus_msg;
    }

    public void setFocus_msg(MsgDetailBean focus_msg) {
        this.focus_msg = focus_msg;
    }

    public MsgDetailBean getOrder_msg() {
        return order_msg;
    }

    public void setOrder_msg(MsgDetailBean order_msg) {
        this.order_msg = order_msg;
    }

    public MsgDetailBean getEve_msg() {
        return eve_msg;
    }

    public void setEve_msg(MsgDetailBean eve_msg) {
        this.eve_msg = eve_msg;
    }

    public MsgDetailBean getCall_msg() {
        return call_msg;
    }

    public void setCall_msg(MsgDetailBean call_msg) {
        this.call_msg = call_msg;
    }

    public static class MsgDetailBean {
        /**
         * content : 
         * count : 0
         */

        private String content;
        private int count;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
