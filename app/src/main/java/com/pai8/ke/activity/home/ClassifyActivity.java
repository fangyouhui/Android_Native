package com.pai8.ke.activity.home;

import android.graphics.Rect;
import android.view.View;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.BuildConfig;
import com.pai8.ke.R;
import com.pai8.ke.activity.home.adapter.ClassifyAdapter;
import com.pai8.ke.activity.takeaway.ui.TakeawayActivity;
import com.pai8.ke.base.BaseActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class ClassifyActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;

    private ClassifyAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_classify;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("全部");
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ClassifyAdapter(this);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        rv.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void initListener() {
        super.initListener();
        srLayout.setOnRefreshListener(this);
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
        mAdapter.setOnItemClickListener((view, position, tag) -> {
            switch (position) {
                case 0:
                    launch(TakeawayActivity.class);
                    break;
                case 1:
                    if(!"Tencent".equals(BuildConfig.FLAVOR)) {
                        toast("此功能暂未开放,敬请期待");
                    }
                    break;
            }
        });
    }

    @Override
    public void onRefresh() {
        srLayout.setRefreshing(true);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("外卖");
        arrayList.add("团购");
        mAdapter.setDataList(arrayList);
        srLayout.setRefreshing(false);
    }

}
