package demo.zkttestdemo.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import demo.zkttestdemo.databinding.ActivityRoomBinding
import kotlin.concurrent.thread

/**
 * Created by Lenovo on 20/11/10.
 * Description: Room数据库操作
 */
class RoomActivity : AppCompatActivity() {

    private lateinit var bind: ActivityRoomBinding
    lateinit var wordDao: WordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initRoom()
    }


    private fun initRoom() {
        var wordDatabase = WordDatabase.getDatabase(this)
        wordDao = wordDatabase.wordDao

        var allWordsLive: LiveData<List<Word>> = wordDao.allWords
        allWordsLive.observe(this, object : Observer<List<Word>> {
            override fun onChanged(list: List<Word>) {
                var text = ""
                for (item in list) {
                    text += "${item.id}:${item.word}=${item.chineseMeaning}\n"
                }
                bind.tvData.text = text
            }
        })

        //增
        bind.btnAdd.setOnClickListener {
            thread {
                var word1 = Word("hello", "你好")
                var word2 = Word("world", "世界")
                wordDao.insertWords(word1, word2)
            }
        }

        //删
        bind.btnDelete.setOnClickListener {
            thread {
                var word = Word("", "")
                word.id = 38 //根据主键删除
                wordDao.deleteWords(word)
            }
        }

        bind.btnUpdate.setOnClickListener {
            thread {
                var word = Word("hello", "asdasdasd")
                //word.id = 64
                wordDao.updateWords(word)
            }
        }

        //查
        bind.btnQuery.setOnClickListener {
        }

        //清空
        bind.btnClear.setOnClickListener {
            thread {
                wordDao.deleteAllWords()
            }
        }
    }

}