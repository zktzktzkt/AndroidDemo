package demo.zkttestdemo.jetpack

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import demo.zkttestdemo.R
import demo.zkttestdemo.databinding.FragmentHomeBinding


/**
 * Created by zkt on 20/02/10.
 * Description:
 */
class HomeFragment : BaseVMFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun initViewModel(provider: ViewModelProvider) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.button.setOnClickListener {

            val bundle = Bundle().apply {
                putString("name", "张三")
            }
            val controller = Navigation.findNavController(it)
            controller.navigate(R.id.action_homeFragment_to_detailFragment2, bundle)
        }
    }


}

