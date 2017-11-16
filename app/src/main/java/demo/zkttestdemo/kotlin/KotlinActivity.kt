package demo.zkttestdemo.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import demo.zkttestdemo.R
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        var kotlinCoder = KotlinCoder("zkt", 1)

        /*findViewById(R.id.btn_toast).setOnClickListener {
            var info = "name" + kotlinCoder.name + "id" + kotlinCoder.id
            toast(info)
        }

        findViewById(R.id.btn_async).setOnClickListener {
            asyn()
        }*/
    }


    fun asyn() {
        doAsync {
            Thread.sleep(5000)
            UI {
                toast("睡了5秒，我醒了")
            }
        }
    }

}
