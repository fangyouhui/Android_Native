package com.pai8.ke.activity.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pai8.ke.R;
import com.pai8.ke.activity.video.VideoDetailActivity;
import com.pai8.ke.adapter.HomeAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.presenter.VideoHomePresenter;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.app.MyApp.getMyAppHandler;
import static com.pai8.ke.global.GlobalConstants.LOADMORE;
import static com.pai8.ke.global.GlobalConstants.REFRESH;

public class SearchVideoActivity extends BaseMvpActivity<VideoHomeContract.Presenter> implements VideoHomeContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private HomeAdapter mAdapter;
    private LuRecyclerViewAdapter mLRvAdapter;
    private int mPageNo = 1;
    private String keywords = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                keywords = StringUtils.getEditText(etSearch);
                onRefresh();
            }
        }
    };

    @Override
    public VideoHomeContract.Presenter initPresenter() {
        return new VideoHomePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_video;
    }

    @Override
    public void initView() {
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new HomeAdapter(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mLRvAdapter = new LuRecyclerViewAdapter(mAdapter);
        lrv.setLayoutManager(new GridLayoutManager(this, 2));
        lrv.setHasFixedSize(true);
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lrv.setFooterViewColor(R.color.colorAccent, R.color.color_light_font, R.color.white);
        lrv.setFooterViewHint("加载中...", "没有更多了", "没有网络了");
        lrv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                switch (childAdapterPosition % 2) {
                    case 0:
                        outRect.top = 30;
                        outRect.left = 30;
                        outRect.right = 15;
                        break;
                    case 1:
                        outRect.top = 30;
                        outRect.left = 15;
                        outRect.right = 30;
                        break;
                }
            }
        });
        lrv.setAdapter(mLRvAdapter);
    }

    @Override
    public void initListener() {
        srLayout.setOnRefreshListener(this);
        lrv.setOnLoadMoreListener(this);
        mLRvAdapter.setOnItemClickListener((view, position) -> {
            VideoResp videoResp = mAdapter.getDataList().get(position);
            VideoDetailActivity.launch(this, videoResp.getId(), StringUtils.getEditText(etSearch),
                    videoResp.getPageNo(), videoResp.getPosition(), 1);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mHandler.hasMessages(1)) {
                    mHandler.removeMessages(1);
                }
                mHandler.sendEmptyMessageDelayed(1, 400);
            }
        });

    }

    @Override
    public void onRefresh() {
        mPageNo = 1;
        srLayout.setRefreshing(true);
        lrv.setRefreshing(true);
        getMyAppHandler().postDelayed(() -> {
            mPresenter.videoList(keywords, mPageNo, REFRESH);
        }, 100);
    }

    @Override
    public void onLoadMore() {
        mPageNo++;
        getMyAppHandler().postDelayed(() -> {
            mPresenter.videoList(keywords, mPageNo, LOADMORE);
        }, 100);
    }

    public void refreshComplete() {
        srLayout.setRefreshing(false);
    }

    @Override
    public void videoList(List<VideoResp> data, int tag) {
        AppUtils.hideInput(this);
        if (tag == GlobalConstants.REFRESH) {
            mAdapter.setDataList(data);
        } else if (tag == GlobalConstants.LOADMORE) {
            mAdapter.addAll(data);
        }
        lrv.refreshComplete(data.size());
        mLRvAdapter.notifyDataSetChanged();
        if (data.size() == 0) {
            lrv.setNoMore(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in_search, R.anim.no_anim);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_anim, R.anim.fade_out_search);
        AppUtils.hideInput(this);
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        finish();
    }

}
