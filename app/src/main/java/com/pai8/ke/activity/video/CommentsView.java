package com.pai8.ke.activity.video;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.entity.resp.Comments;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.CircleMovementMethod;

import java.util.List;

import androidx.annotation.Nullable;

public class CommentsView extends LinearLayout {

    private Context mContext;
    private List<Comments> mData;
    private onItemClickListener listener;

    public CommentsView(Context context) {
        this(context, null);
    }

    public CommentsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        this.mContext = context;
    }

    /**
     * 设置评论列表信息
     *
     * @param list
     */
    public void setList(List<Comments> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        if (mData == null || mData.size() <= 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10);
        for (int i = 0; i < mData.size(); i++) {
            View view = getView(i);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }
            addView(view, i, layoutParams);
        }
    }

    private View getView(final int position) {
        final Comments item = mData.get(position);
        // 回复人信息
        String replyUserId = item.getTo_user_id();
        String replyUserAvatar = item.getTo_avatar();
        String replyUserNickName = item.getTo_nickname();
        // 评论人信息
        String commentUserId = item.getFrom_user_id();
        String commentUserAvatar = item.getFrom_avatar();
        String commentUserNickName = item.getFrom_nickname();

        boolean hasReply = false; // 是否有回复
        if (StringUtils.isNotEmpty(replyUserId)) {
            hasReply = true;
        }

        TextView textView = new TextView(mContext);
        textView.setTextSize(13);
        textView.setTextColor(ResUtils.getColor(R.color.color_mid_font));
        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (hasReply) {
            builder.append(setClickableSpan(filterMe(commentUserNickName, commentUserId), commentUserId));
            builder.append(" 回复 ");
            builder.append(setClickableSpan(filterMe(replyUserNickName, replyUserId), replyUserId));
        } else {
            builder.append(setClickableSpan(filterMe(commentUserNickName, replyUserId), commentUserId));
        }
        builder.append(" : ");
        builder.append(setClickableSpanContent(item.getContent(), position));
        textView.setText(builder);
        // 设置点击背景色
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
//  textView.setHighlightColor(0xff000000);
        textView.setMovementMethod(new CircleMovementMethod(ResUtils.getColor(R.color.color_divider),
                ResUtils.getColor(R.color.color_divider)));
        textView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, item);
            }
        });

        return textView;
    }

    /**
     * 设置评论内容点击事件
     *
     * @param item
     * @param position
     * @return
     */
    public SpannableString setClickableSpanContent(final String item, final int position) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0xff686868);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    /**
     * 设置评论用户名字点击事件
     *
     * @param item
     * @param uid  用户id
     * @return
     */
    public SpannableString setClickableSpan(final String item, String uid) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                ToastUtils.showShort("" + uid);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置显示的用户名文本颜色
                ds.setColor(ResUtils.getColor(R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    /**
     * 定义一个用于回调的接口
     */
    public interface onItemClickListener {
        void onItemClick(int position, Comments bean);
    }

    private String filterMe(String name, String userId) {
        if (AccountManager.getInstance().getUid().equals(userId)) {
            return "我";
        }
        return name;
    }
}
