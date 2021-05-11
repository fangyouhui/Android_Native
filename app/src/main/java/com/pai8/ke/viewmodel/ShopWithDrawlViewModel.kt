package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class ShopWithDrawlViewModel : BaseViewModel() {

    val shopCashData = MutableLiveData<List<String>>()
    fun shopCash(money: String) {
        launchOnlyResult(
            {
                RetrofitClient.getInstance().getMainService()
                    .shopCash(AccountManager.getInstance().shopId, money)
            }, {
                shopCashData.value = it
            }
        )

    }

    val shopCashListData = MutableLiveData<List<String>>()
    fun shopCashList(page: Int, month: Int) {
        launchOnlyResult(
            {
                RetrofitClient.getInstance().getMainService().shopCashList(
                    AccountManager.getInstance().shopId,
                    page.toString(),
                    month.toString()
                )
            }, {
                shopCashListData.value = it
            }
        )

    }
}