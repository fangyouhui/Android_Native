package com.pai8.ke.activity.takeaway.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.FoodClassifyAdapter;
import com.pai8.ke.activity.takeaway.adapter.FoodGoodAdapter;
import com.pai8.ke.activity.takeaway.adapter.RvListener;
import com.pai8.ke.activity.takeaway.contract.GoodContract;
import com.pai8.ke.activity.takeaway.entity.FoodClassifyInfo;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.ShopDataEvent;
import com.pai8.ke.activity.takeaway.presenter.GoodPresenter;
import com.pai8.ke.activity.takeaway.widget.CheckListener;
import com.pai8.ke.activity.takeaway.widget.ItemHeaderDecoration;
import com.pai8.ke.base.BaseMvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodFragment extends BaseMvpFragment<GoodPresenter> implements View.OnClickListener, CheckListener, GoodContract.View {

    private RecyclerView mRvClassify;
    private RecyclerView mRvGoods;
    private FoodClassifyAdapter mClassifyAdapter;
    private FoodGoodAdapter mGoodAdapter;
    private ItemHeaderDecoration mDecoration;
    private CheckListener checkListener;
    private LinearLayoutManager linear;
    private boolean move = false;
    private int mIndex = 0;
    private int targetPosition;//点击左边某一个具体的item的位置
    private boolean isMoved;
    private GridLayoutManager mManager;

    private List<FoodClassifyInfo> mLeftList = new ArrayList<>();

    private final List<FoodGoodInfo> mRightList = new ArrayList<>();

    private final Map<Integer, Integer> indexMap = new HashMap<>();

    @Override
    public GoodPresenter initPresenter() {
        return new GoodPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frament_good;
    }

    @Override
    protected void initView(Bundle arguments) {
        EventBus.getDefault().register(this);
        mRvClassify = mRootView.findViewById(R.id.rv_classify);
        linear = new LinearLayoutManager(getActivity());
        mRvClassify.setLayoutManager(linear);
        mRvGoods = mRootView.findViewById(R.id.rv_goods);
        setListener(this);
        mManager = new GridLayoutManager(getActivity(), 1);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mRightList.size()>0){
                    return mRightList.get(position).isTitle() ? 1 : 1;
                }
                return 0;
            }
        });
        mRvGoods.setLayoutManager(mManager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShopDataEvent event) {
        if (event.type == Constants.EVENT_TYPE_SHOP_CONTENT) {
            mLeftList = event.data.goods;
            List<FoodClassifyInfo> goods = event.data.goods;
            mClassifyAdapter.setNewData(goods);
            for (int i = 0; i < goods.size(); i++) {
                FoodGoodInfo head = new FoodGoodInfo(goods.get(i).name);
                //头部设置为true
                head.setTitle(true);
                head.name = goods.get(i).name;
                head.setTag(String.valueOf(i));
                mRightList.add(head);
                List<FoodGoodInfo> goodInfos = goods.get(i).goods;
                for (int j = 0; j < goodInfos.size(); j++) {
                    FoodGoodInfo body = new FoodGoodInfo(goodInfos.get(j).name);
                    body.setTag(String.valueOf(i));
                    String name = goodInfos.get(j).title;
                    body.name = goods.get(i).name;
                    body.sell_price = goodInfos.get(j).sell_price;
                    body.id = goodInfos.get(j).id;
                    body.cover = goodInfos.get(j).cover;
                    body.discount = goodInfos.get(j).discount;
                    body.title = name;
                    mRightList.add(body);
                }

            }
            mDecoration = new ItemHeaderDecoration(getActivity(), mRightList);
            mDecoration.setData(mRightList);
            mRvGoods.addItemDecoration(mDecoration);
            mDecoration.setCheckListener(checkListener);
        }

    }

    public void setData(int n) {
        mIndex = n;
        mRvGoods.stopScroll();
        smoothMoveToPosition(n);
    }

    public void setListener(CheckListener listener) {
        this.checkListener = listener;
    }


    private void setChecked(int position, boolean isLeft) {
        Log.d("p-------->", String.valueOf(position));
        if (isLeft) {
            mClassifyAdapter.setCheckedPosition(position);
            //此处的位置需要根据每个分类的集合来进行计算
            int count = 0;
            for (int i = 0; i < position; i++) {
                count += mLeftList.get(i).goods.size();
            }
            count += position;
            setData(count);
            ItemHeaderDecoration.setCurrentTag(String.valueOf(targetPosition));//凡是点击左边，将左边点击的位置作为当前的tag
        } else {
            if (isMoved) {
                isMoved = false;
            } else
                mClassifyAdapter.setCheckedPosition(position);
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
    protected void initData() {
        super.initData();
        mClassifyAdapter = new FoodClassifyAdapter(mLeftList);
        mRvClassify.setAdapter(mClassifyAdapter);
        mClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                isMoved = true;
                targetPosition = position;
                setChecked(position, true);
            }
        });
        mGoodAdapter = new FoodGoodAdapter(getActivity(), mRightList, new RvListener() {
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
        mRvGoods.setAdapter(mGoodAdapter);

    }


    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        Log.d("first--->", String.valueOf(firstItem));
        Log.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRvGoods.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRvGoods.getChildAt(n - firstItem).getTop();
            Log.d("top---->", String.valueOf(top));
            mRvGoods.scrollBy(0, top);
        } else {
            mRvGoods.scrollToPosition(n);
            move = true;
        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void check(int position, boolean isScroll) {
//        checkListener.check(position, isScroll);
        setChecked(position, isScroll);
    }


    @Override
    protected void initListener() {
        mRvGoods.addOnScrollListener(new RecyclerViewListener());
    }



    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < mRvGoods.getChildCount()) {
                    int top = mRvGoods.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    mRvGoods.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRvGoods.getChildCount()) {
                    int top = mRvGoods.getChildAt(n).getTop();
                    mRvGoods.scrollBy(0, top);
                }
            }
        }
    }
}
