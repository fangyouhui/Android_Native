package com.pai8.ke.fragment.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pai8.ke.R;
import com.pai8.ke.activity.home.ClassifyActivity;
import com.pai8.ke.activity.takeaway.ui.TakeawayActivity;
import com.pai8.ke.activity.video.VideoDetailActivity;
import com.pai8.ke.adapter.HomeAdapter;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.presenter.VideoHomePresenter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pai8.ke.app.MyApp.getMyAppHandler;
import static com.pai8.ke.global.GlobalConstants.LOADMORE;
import static com.pai8.ke.global.GlobalConstants.REFRESH;

public class TabHomeChildFragment extends BaseMvpFragment<VideoHomeContract.Presenter> implements VideoHomeContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;

    private LuRecyclerViewAdapter mLRvAdapter;
    private HomeAdapter mAdapter;
    private int mPosition;
    private int mPageNo = 1;
    private String keywords = "";

    public static TabHomeChildFragment newInstance(int position) {
        TabHomeChildFragment fragment = new TabHomeChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home_child;
    }

    @Override
    protected void initView(Bundle arguments) {

        mPosition = arguments.getInt("position");

        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new HomeAdapter(getActivity());
        mLRvAdapter = new LuRecyclerViewAdapter(mAdapter);
        lrv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lrv.setHasFixedSize(true);
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lrv.setFooterViewColor(R.color.colorAccent, R.color.color_light_font, R.color.white);
        lrv.setFooterViewHint("加载中...", "没有更多了", "没有网络了");
        lrv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (mPosition == 1) {
                    switch (childAdapterPosition % 2) {
                        case 1:
                            outRect.top = 30;
                            outRect.left = 30;
                            outRect.right = 15;
                            break;
                        default:
                            outRect.top = 30;
                            outRect.left = 15;
                            outRect.right = 30;
                            break;
                    }
                } else {
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

            }
        });
        lrv.setAdapter(mLRvAdapter);

        if (mPosition == 0) {
            mAdapter.setNearby(true);
        } else if (mPosition == 1) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_nearby,
                    getActivity().findViewById(android.R.id.content), false);
            view.findViewById(R.id.iv_1).setOnClickListener(v -> launch(TakeawayActivity.class));

            view.findViewById(R.id.iv_2).setOnClickListener(v -> toast("此功能暂未开放,敬请期待"));

            view.findViewById(R.id.iv_3).setOnClickListener(v -> launch(ClassifyActivity.class));

            mLRvAdapter.addHeaderView(view);
        }

    }

    @Override
    protected void initData() {
        onRefresh();
    }

    @Override
    protected void initListener() {
        srLayout.setOnRefreshListener(this);
        lrv.setOnLoadMoreListener(this);
        mLRvAdapter.setOnItemClickListener((view, position) -> {
            VideoResp videoResp = mAdapter.getDataList().get(position);
            VideoDetailActivity.launch(getActivity(), videoResp.getId());
        });

    }

    @Override
    public void onRefresh() {
        mPageNo = 1;
        srLayout.setRefreshing(true);
        lrv.setRefreshing(true);
        getMyAppHandler().postDelayed(() -> {
            switch (mPosition) {
                case 0:
                    mPresenter.nearbyVideoList(keywords, mPageNo, REFRESH);
                    break;
                case 1:
                    mPresenter.videoList(keywords, mPageNo, REFRESH);
                    break;
                case 2:
                    mPresenter.followVideoList(mPageNo, REFRESH);
                    break;
                case 3:
                    mPresenter.myVideoList(mPageNo, REFRESH);
                    break;
                case 4:
                    mPresenter.myLikeVideoList(mPageNo, REFRESH);
                    break;
            }
        }, 100);
    }

    @Override
    public void onLoadMore() {
        mPageNo++;
        getMyAppHandler().postDelayed(() -> {
            switch (mPosition) {
                case 0:
                    mPresenter.nearbyVideoList(keywords, mPageNo, LOADMORE);
                    break;
                case 1:
                    mPresenter.videoList(keywords, mPageNo, LOADMORE);
                    break;
                case 2:
                    mPresenter.followVideoList(mPageNo, LOADMORE);
                    break;
                case 3:
                    mPresenter.myVideoList(mPageNo, LOADMORE);
                    break;
                case 4:
                    mPresenter.myLikeVideoList(mPageNo, LOADMORE);
                    break;
            }
        }, 400);
    }

    @Override
    public VideoHomeContract.Presenter initPresenter() {
        return new VideoHomePresenter(this);
    }

    @Override
    public void refreshComplete() {
        srLayout.setRefreshing(false);
    }

    @Override
    public void videoList(List<VideoResp> data, int tag) {
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

}
