package com.pai8.ke.fragment.shop;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pai8.ke.BuildConfig;
import com.pai8.ke.R;
import com.pai8.ke.adapter.ShopAdapter;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pai8.ke.app.MyApp.getMyAppHandler;

public class TabShopChildFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnLoadMoreListener {

    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;

    private LuRecyclerViewAdapter mLRvAdapter;
    private ShopAdapter mAdapter;
    private int mPosition;
    private int mPageNo;

    public static TabShopChildFragment newInstance(int position) {
        TabShopChildFragment fragment = new TabShopChildFragment();
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
        mAdapter = new ShopAdapter(getActivity());
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
    protected void initData() {
        onRefresh();
    }

    @Override
    protected void initListener() {
        srLayout.setOnRefreshListener(this);
        lrv.setOnLoadMoreListener(this);
        if(!"Tencent".equals(BuildConfig.FLAVOR)) {
            mLRvAdapter.setOnItemClickListener((view, position) -> {
                toast("该功能暂未开放，敬请期待");
            });
        }

    }

    @Override
    public void onRefresh() {
        mPageNo = 1;
        srLayout.setRefreshing(true);
        lrv.setRefreshing(true);
        List<String> a = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            a.add("");
        }
        getMyAppHandler().postDelayed(() -> {
            mAdapter.setDataList(a);
            refreshComplete();
        }, 100);
    }

    @Override
    public void onLoadMore() {
        mPageNo++;
        getMyAppHandler().postDelayed(() -> {
        }, 400);
    }

    public void refreshComplete() {
        srLayout.setRefreshing(false);
    }

}
