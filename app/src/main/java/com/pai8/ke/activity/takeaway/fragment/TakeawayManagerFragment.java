package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.RvListener;
import com.pai8.ke.activity.takeaway.adapter.TakeawayFoodAdapter;
import com.pai8.ke.activity.takeaway.adapter.TakeawayManagerAdapter;
import com.pai8.ke.activity.takeaway.contract.TakeawayManagerContract;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.presenter.TakeawayManagerPresenter;
import com.pai8.ke.activity.takeaway.ui.AddGoodActivity;
import com.pai8.ke.activity.takeaway.utils.SoftHideKeyBoardUtil;
import com.pai8.ke.activity.takeaway.widget.CheckListener;
import com.pai8.ke.activity.takeaway.widget.ItemHeaderDecoration;
import com.pai8.ke.activity.takeaway.widget.ItemHeaderDecoration1;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.manager.AccountManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import razerdp.util.KeyboardUtils;

public class TakeawayManagerFragment extends BaseMvpFragment<TakeawayManagerPresenter> implements OnClickListener, CheckListener, TakeawayManagerContract.View {

    private EditText mEtType;

    private TakeawayManagerPresenter presenter;
    private RecyclerView mRvClassify, mRvGood;
    private TakeawayFoodAdapter mAdapter;
    private TakeawayManagerAdapter mTakeawayAdapter;
    private ItemHeaderDecoration1 mDecoration;
    private CheckListener checkListener;
    private LinearLayoutManager linear;
    private boolean move = false;
    private int mIndex = 0;
    private int targetPosition;//点击左边某一个具体的item的位置
    private boolean isMoved;
    private GridLayoutManager mManager;

    private List<ShopInfo> mLeftList = new ArrayList<>();

    private final List<AddFoodReq> mRightList = new ArrayList<>();
    public boolean isSelected;

    @Override
    public TakeawayManagerPresenter initPresenter() {
        return new TakeawayManagerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_takeaway_manager;
    }

    @Override
    protected void initView(Bundle arguments) {
        SoftHideKeyBoardUtil.assistActivity(getActivity());
        EventBus.getDefault().register(this);
        mEtType = mRootView.findViewById(R.id.et_type);
        mRvClassify = mRootView.findViewById(R.id.rv_classify);
        mRootView.findViewById(R.id.tv_add).setOnClickListener(this);
        linear = new LinearLayoutManager(getActivity());
        mRvClassify.setLayoutManager(linear);
        mRootView.findViewById(R.id.tv_add_classify).setOnClickListener(this);
        mRvGood = mRootView.findViewById(R.id.rv_goods);
        setListener(this);
        mManager = new GridLayoutManager(getActivity(), 1);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mRightList.size() > 0) {
                    return mRightList.get(position).isTitle() ? 1 : 1;
                }
                return 0;
            }
        });
        mRvGood.setLayoutManager(mManager);


        mEtType.setOnKeyListener((v, keyCode, event) -> {        // 开始搜索
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                KeyboardUtils.close(getActivity());
                //搜索逻辑
                String name = mEtType.getText().toString();
                presenter.addUpCategory(name, AccountManager.getInstance().getShopId());
                mEtType.setText("");
                mEtType.setVisibility(View.GONE);
                return true;
            }
            return false;
        });

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new TakeawayManagerPresenter(this);
        presenter.goodslist();
        mTakeawayAdapter = new TakeawayManagerAdapter(null);
        mRvClassify.setAdapter(mTakeawayAdapter);
        mTakeawayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_edit) {

                    view.findViewById(R.id.iv_edit).setFocusable(true);
                    view.findViewById(R.id.iv_edit).setFocusableInTouchMode(true);
                    view.findViewById(R.id.iv_edit).requestFocus();

                } else if (view.getId() == R.id.iv_del) {
                    presenter.deleteCategory(mTakeawayAdapter.getData().get(position).id, position);
                }
            }
        });

        mTakeawayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                isMoved = true;
//                targetPosition = position;
//                setChecked(position, true);
            }
        });
        mAdapter = new TakeawayFoodAdapter(getActivity(), mRightList, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {
                String content = "";
                switch (id) {
                    case R.id.root:
                        content = "title";
                        break;

                }

            }
        });
        mRvGood.setAdapter(mAdapter);

    }


    public void setData(int n) {
        mIndex = n;
        mRvGood.stopScroll();
        smoothMoveToPosition(n);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyEvent event) {
        if (event.type == Constants.EVENT_TYPE_REFRESH_SHOP_GOOD) {
            mPresenter.goodslist();
        }

    }


    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        Log.d("first--->", String.valueOf(firstItem));
        Log.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRvGood.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRvGood.getChildAt(n - firstItem).getTop();
            Log.d("top---->", String.valueOf(top));
            mRvGood.scrollBy(0, top);
        } else {
            mRvGood.scrollToPosition(n);
            move = true;
        }
    }

    public void setListener(CheckListener listener) {
        this.checkListener = listener;
    }


    private void setChecked(int position, boolean isLeft) {
        Log.d("p-------->", String.valueOf(position));
        if (isLeft) {
            mTakeawayAdapter.setCheckedPosition(position);
            //此处的位置需要根据每个分类的集合来进行计算
            int count = 0;
            if (mLeftList!=null && mLeftList.size()>0 &&mLeftList.size() >= position) {
                for (int i = 0; i < position; i++) {
                    count += mLeftList.get(i).goods.size();
                }
            }
            count += position;
            setData(count);
            ItemHeaderDecoration.setCurrentTag(String.valueOf(targetPosition));//凡是点击左边，将左边点击的位置作为当前的tag
        } else {
            if (isMoved) {
                isMoved = false;
            } else
                mTakeawayAdapter.setCheckedPosition(position);
            ItemHeaderDecoration.setCurrentTag(String.valueOf(position));//如果是滑动右边联动左边，则按照右边传过来的位置作为tag

        }
        moveToCenter(position);

    }


    //将当前选中的item居中
    private void moveToCenter(int position) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        View childAt = mRvClassify.getChildAt(position - linear.findFirstVisibleItemPosition());
        if (childAt != null) {
            int y = (childAt.getTop() - mRvClassify.getHeight() / 2);
            mRvClassify.smoothScrollBy(0, y);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_add) {
            startActivity(new Intent(mActivity, AddGoodActivity.class));

        } else if (v.getId() == R.id.tv_add_classify) {  //添加分类
            mEtType.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void getCategoryListSuccess(List<ShopInfo> data) {
        mRightList.clear();
        mLeftList = data;
        mTakeawayAdapter.setNewData(data);
        for (int i = 0; i < data.size(); i++) {
            AddFoodReq head = new AddFoodReq(data.get(i).name);
            //头部设置为true
            head.setTitle(true);
            head.title = data.get(i).name;
            head.setTag(String.valueOf(i));
            mRightList.add(head);
            List<AddFoodReq> goodInfos = data.get(i).goods;
            if (goodInfos != null && goodInfos.size() > 0) {
                for (int j = 0; j < goodInfos.size(); j++) {
                    AddFoodReq body = new AddFoodReq(goodInfos.get(j).title);
                    body.setTag(String.valueOf(i));
                    String name = goodInfos.get(j).title;
//                    body.name = goodInfos.get(i).name;
                    body.sell_price = goodInfos.get(j).sell_price;
                    body.cate_id = goodInfos.get(j).cate_id;
                    body.id = goodInfos.get(j).id;
                    body.cover = goodInfos.get(j).cover;
                    body.cate_name = data.get(i).name;
                    body.discount = goodInfos.get(j).discount;
                    body.title = name;
                    mRightList.add(body);
                }

            }
        }
        mDecoration = new ItemHeaderDecoration1(getActivity(), mRightList);
        mDecoration.setData(mRightList);
        mRvGood.addItemDecoration(mDecoration);
        mDecoration.setCheckListener(checkListener);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteCategorySuccess(int position) {
        mTakeawayAdapter.remove(position);
    }

    @Override
    public void addUpCategorySuccess(ShopInfo data) {
        mTakeawayAdapter.getData().add(data);
        mTakeawayAdapter.notifyDataSetChanged();

    }

    @Override
    public void check(int position, boolean isScroll) {
        setChecked(position, isScroll);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
