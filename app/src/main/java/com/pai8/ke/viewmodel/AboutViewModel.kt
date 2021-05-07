package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.VersionResp
import com.pai8.ke.groupBuy.http.RetrofitClient

class AboutViewModel : BaseViewModel() {

    val checkUpgradeData = MutableLiveData<VersionResp>()
    fun checkUpgrade() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService()
                .checkUpgrade("pai_android", AppUtils.getAppVersionName())
        }, {
            checkUpgradeData.value = it
        }, isShowDialog = true)
    }
}