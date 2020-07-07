package demo.zkttestdemo.jetpack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * Created by zkt on 20/07/05.
 * Description:
 */
abstract class BaseVMFragment<T : ViewDataBinding>(val contentLayoutId: Int) : Fragment(contentLayoutId) {

    lateinit var b: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        b = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        val provider = ViewModelProvider(this)
        b.lifecycleOwner = requireActivity()

        initViewModel(provider)

        return b.root
    }

    abstract fun initViewModel(provider: ViewModelProvider)

}