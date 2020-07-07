package demo.zkttestdemo.jetpack

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import demo.zkttestdemo.R
import demo.zkttestdemo.databinding.FragmentDetailBinding

class DetailFragment : BaseVMFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    lateinit var detailViewModel: DetailViewModel

    override fun initViewModel(provider: ViewModelProvider) {
        detailViewModel = provider.get(DetailViewModel::class.java)
        b.vm = detailViewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 获取传过来的值
        val string = arguments?.getString("name")
        view?.findViewById<TextView>(R.id.textView2)?.text = string

        view?.findViewById<Button>(R.id.button2)?.setOnClickListener {
            // 获取view的Controller
            val controller = Navigation.findNavController(it)
            // 关闭当前页面
            controller.navigateUp()
        }
    }
}