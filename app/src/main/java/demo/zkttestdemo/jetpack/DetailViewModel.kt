package demo.zkttestdemo.jetpack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

/**
 * Created by zkt on 20/07/05.
 * Description:
 */
class DetailViewModel(application: Application) : AndroidViewModel(application) {

    val beanLiveData = MutableLiveData<LoginResp>()

    init {

    }

    fun onChangeClick() {
        val resp = LoginResp().apply {
            data = LoginResp.DataBean().apply {
                user_id = "用户id -> ${Random.nextInt(100)}"
            }
        }
        beanLiveData.postValue(resp)
    }
}