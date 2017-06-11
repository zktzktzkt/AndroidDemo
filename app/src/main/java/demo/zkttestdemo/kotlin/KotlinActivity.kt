package demo.zkttestdemo.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import demo.zkttestdemo.R

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        var kotlinCoder = KotlinCoder("zkt", 1)

        findViewById(R.id.button2).setOnClickListener {
            var info = "name" + kotlinCoder.name + "id" + kotlinCoder.id
            toast(info)
        }
    }


    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }


}
