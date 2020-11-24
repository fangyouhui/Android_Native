package com.pai8.ke.activity.video;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.video.adapter.ShopSearchListAdapter;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.ShopList;
import com.pai8.ke.entity.resp.ShopListResp;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.SpaceItemDecoration;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.app.MyApp.getMyAppHandler;
import static com.pai8.ke.global.GlobalConstants.LOADMORE;
import static com.pai8.ke.global.GlobalConstants.REFRESH;

/**
 * 商家搜索列表
 * Created by gh on 2020/11/14.
 */
public class ShopSearchListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        OnLoadMoreListener {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.tv_btn_cancel)
    TextView tvBtnCancel;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.l_rv)
    LuRecyclerView lrv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;

    private LuRecyclerViewAdapter mLRvAdapter;
    private ShopSearchListAdapter mAdapter;

    private int mPageNo = 1;
    private String mKeywords = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_search_list;
    }

    @Override
    public void initView() {
        titleBar.setTitle("关联店铺");
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ShopSearchListAdapter(this);
        mLRvAdapter = new LuRecyclerViewAdapter(mAdapter);
        lrv.setLayoutManager(new LinearLayoutManager(this));
        lrv.addItemDecoration(new SpaceItemDecoration(2, 0, 0, 0));
        lrv.setHasFixedSize(true);
        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lrv.setFooterViewColor(R.color.colorAccent, R.color.color_light_font, R.color.white);
        lrv.setFooterViewHint("加载中...", "没有更多了", "没有网络了");
        lrv.setAdapter(mLRvAdapter);

        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
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
    }

    @Override
    public void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    public void initListener() {
        srLayout.setOnRefreshListener(this);
        lrv.setOnLoadMoreListener(this);
        mLRvAdapter.setOnItemClickListener((view, position) -> {
            ShopList shopList = mAdapter.getDataList().get(position);
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_CHOOSE_SHOP, shopList));
            finish();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string = editable.toString();
                if (StringUtils.isNotEmpty(string)) {
                    tvBtnCancel.setVisibility(View.VISIBLE);
                } else {
                    tvBtnCancel.setVisibility(View.GONE);
                    mKeywords = "";
                    onRefresh();
                }
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    mKeywords = StringUtils.getEditText(etSearch);
                    onRefresh();
                }
                return false;
            }
        });

    }

    @Override
    public void onRefresh() {
        mPageNo = 1;
        srLayout.setRefreshing(true);
        lrv.setRefreshing(true);
        getMyAppHandler().postDelayed(() -> {
            getShopList(REFRESH);
        }, 100);
    }

    @Override
    public void onLoadMore() {
        mPageNo++;
        getMyAppHandler().postDelayed(() -> {
            getShopList(LOADMORE);
        }, 400);
    }

    private void getShopList(int tag) {
        if (StringUtils.isNotEmpty(mKeywords)) {
            tvStatus.setText("搜索结果");
        } else {
            tvStatus.setText("附件店铺");
        }
        Api.getInstance().shopSelect(mPageNo, mKeywords)
                .doOnSubscribe(disposable -> {

                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<ShopList>>() {
                    @Override
                    protected void onSuccess(List<ShopList> list) {
                        refreshComplete();
                        if (tag == GlobalConstants.REFRESH) {
                            mAdapter.setDataList(list);
                        }
                        if (tag == GlobalConstants.LOADMORE) {
                            mAdapter.addAll(list);
                        }
                        lrv.refreshComplete(list.size());
                        mLRvAdapter.notifyDataSetChanged();
                        if (list.size() == 0) {
                            lrv.setNoMore(true);
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        refreshComplete();
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void refreshComplete() {
        srLayout.setRefreshing(false);
    }

    @OnClick(R.id.tv_btn_cancel)
    public void onViewClicked() {
        etSearch.setText("");
        mKeywords = "";
        onRefresh();
        AppUtils.hideInput(this);
    }
}
