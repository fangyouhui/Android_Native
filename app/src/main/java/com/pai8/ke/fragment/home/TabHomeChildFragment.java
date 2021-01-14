package com.pai8.ke.fragment.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pai8.ke.BuildConfig;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.home.ClassifyActivity;
import com.pai8.ke.activity.takeaway.ui.TakeawayActivity;
import com.pai8.ke.activity.video.tiktok.TikTokActivity;
import com.pai8.ke.adapter.HomeAdapter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.event.LoginStatusEvent;
import com.pai8.ke.entity.event.VideoItemRefreshEvent;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.presenter.VideoHomePresenter;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.PreferencesUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pai8.ke.app.MyApp.getMyAppHandler;
import static com.pai8.ke.global.EventCode.EVENT_LOGIN_STATUS;
import static com.pai8.ke.global.EventCode.EVENT_VIDEO_ITEM;
import static com.pai8.ke.global.EventCode.EVENT_VIDEO_LIST_REFRESH;
import static com.pai8.ke.global.GlobalConstants.LOADMORE;
import static com.pai8.ke.global.GlobalConstants.REFRESH;

public class TabHomeChildFragment extends BaseMvpFragment<VideoHomeContract.Presenter> implements VideoHomeContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    @BindView(R.id.layout_empty_view)
    View mStateEmpty;
    @BindView(R.id.layout_login_view)
    View mStateLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private LuRecyclerViewAdapter mLRvAdapter;
    private HomeAdapter mAdapter;
    private int mPosition;
    private int mPageNo = 1;
    private VideoItemRefreshEvent mRefreshEvent;

    public static TabHomeChildFragment newInstance(int position) {
        TabHomeChildFragment fragment = new TabHomeChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EVENT_VIDEO_LIST_REFRESH:
                onRefresh();
                break;
            case EVENT_LOGIN_STATUS:
                onRefresh();
                break;
            case EVENT_VIDEO_ITEM:
                try {
                    mRefreshEvent = (VideoItemRefreshEvent) event.getData();
                    mAdapter.getDataList().set(mRefreshEvent.getPosition(), mRefreshEvent.getVideo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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

            if(!"Tencent".equals(BuildConfig.FLAVOR)) {
                view.findViewById(R.id.iv_2).setOnClickListener(v -> toast("此功能暂未开放,敬请期待"));
            }

            view.findViewById(R.id.iv_3).setOnClickListener(v -> launch(ClassifyActivity.class));

            mLRvAdapter.addHeaderView(view);
        }

    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        onRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRefreshEvent != null) {
            LogUtils.d("视频列表刷新");
            mLRvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mRefreshEvent = null;
    }

    @Override
    protected void initData() {
        if (CollectionUtils.isNotEmpty(MyApp.getLngLat())) {
            onRefresh();
        } else {
            AMapLocationUtils.getLocation(location -> {
                PreferencesUtils.put(getActivity(), "latitude", location.getLatitude() + "");
                PreferencesUtils.put(getActivity(), "longitude", location.getLongitude() + "");
                PreferencesUtils.put(getActivity(), "address", location.getAddress());
                PreferencesUtils.put(getActivity(), "city", location.getCity());
                onRefresh();
            }, true);
        }
    }

    @Override
    protected void initListener() {
        srLayout.setOnRefreshListener(this);
        lrv.setOnLoadMoreListener(this);
        mLRvAdapter.setOnItemClickListener((view, position) -> {
            Video videoResp = mAdapter.getDataList().get(position);
            TikTokActivity.launch(getActivity(), mAdapter.getDataList(), videoResp.getPage(), position
                    , mPosition);
        });
        btnLogin.setOnClickListener(view -> {
            launch(LoginActivity.class);
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
                    mPresenter.nearby(mPageNo, REFRESH);
                    break;
                case 1:
                    mPresenter.flow(mPageNo, REFRESH);
                    break;
                case 2:
                    mPresenter.follow(mPageNo, REFRESH);
                    break;
                case 3:
                    mPresenter.myVideo(mPageNo, REFRESH);
                    break;
                case 4:
                    mPresenter.myLike(mPageNo, REFRESH);
                    break;
                case 6:
                    mPresenter.myLink(mPageNo, REFRESH);
            }
        }, 100);
    }

    @Override
    public void onLoadMore() {
        mPageNo++;
        getMyAppHandler().postDelayed(() -> {
            switch (mPosition) {
                case 0:
                    mPresenter.nearby(mPageNo, LOADMORE);
                    break;
                case 1:
                    mPresenter.flow(mPageNo, LOADMORE);
                    break;
                case 2:
                    mPresenter.follow(mPageNo, LOADMORE);
                    break;
                case 3:
                    mPresenter.myVideo(mPageNo, LOADMORE);
                    break;
                case 4:
                    mPresenter.myLike(mPageNo, LOADMORE);
                    break;
                case 6:
                    mPresenter.myLink(mPageNo, REFRESH);
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
    public void setNoMore() {
        lrv.setNoMore(true);
    }

    @Override
    public void videoList(List<Video> data, int tag) {
        if (tag == REFRESH) {
            mAdapter.setDataList(data);
        } else if (tag == LOADMORE) {
            mAdapter.addAll(data);
        }
        lrv.refreshComplete(data.size());
        mLRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteVideo(String videoId) {

    }

    @Override
    public void loginView() {
        mStateEmpty.setVisibility(View.INVISIBLE);
        mStateLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyPage() {
        mStateEmpty.setVisibility(View.VISIBLE);
        mStateLogin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSucessPage() {
        mStateEmpty.setVisibility(View.INVISIBLE);
        mStateLogin.setVisibility(View.INVISIBLE);
    }
}
