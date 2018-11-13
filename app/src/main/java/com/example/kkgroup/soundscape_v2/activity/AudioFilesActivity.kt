package com.example.kkgroup.soundscape_v2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.kkgroup.soundscape_v2.model.AudioFiles
import com.example.kkgroup.soundscape_v2.model.GlobalModel
import com.example.kkgroup.soundscape_v2.R
import com.example.kkgroup.soundscape_v2.Tools.Tools
import kotlinx.android.synthetic.main.activity_audio_files_list.*
import java.io.File
import java.util.*

class AudioFilesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_files_list)

        val listView = findViewById<ListView>(R.id.sound_listView)
        listView.adapter = audioAdapter(this)

        getAudioFiles(File(Tools.getSoundScapePath()))

        // Testing code for adding options menu for all audio files
        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.my_button -> {
                    showPopup(view)
                }
            }
        }

        my_button.setOnClickListener(clickListener)
    }


    private class audioAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            this.mContext = context
        }

        //Number of rows
        override fun getCount(): Int {
            return GlobalModel.audioFiles.size
        }

        // Render Row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val audioRow = layoutInflater.inflate(R.layout.row_audio_file, viewGroup, false)

            //Setting names for each row
            val nameTextView = audioRow.findViewById<TextView>(R.id.audioName)
            val name = GlobalModel.audioFiles[position].getAudioFileName()
            nameTextView.text = name

            //Options button shit
            val optionsButton = audioRow.findViewById(R.id.audio_list_button) as ImageButton

            optionsButton.setOnClickListener {
                GlobalModel.audioFiles.removeAt(position)
                Toast.makeText(mContext, "Removed position: $position ", Toast.LENGTH_SHORT).show()
                notifyDataSetChanged()
            }

            audioRow.setOnClickListener {


                var audioFile = Tools.getSoundScapePath() + name
                val playIntent = Intent(mContext, PlayActivity::class.java)
                playIntent.putExtra("audio", audioFile)

                mContext.startActivity(playIntent)

            }
            return audioRow
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getItem(p0: Int): Any {
           return "Test"
        }

    }

    private fun showPopup(view: View) {

        var popup: PopupMenu? = null;
        popup = PopupMenu(this, view)
        popup.inflate(R.menu.audio_list_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.item1 -> {
                    Toast.makeText(this@AudioFilesActivity, item.title, Toast.LENGTH_SHORT).show();
                }
                R.id.item2 -> {
                    Toast.makeText(this@AudioFilesActivity, item.title, Toast.LENGTH_SHORT).show();
                }
                R.id.item3 -> {
                    Toast.makeText(this@AudioFilesActivity, item.title, Toast.LENGTH_SHORT).show();
                }
            }

            true
        })

        popup.show()
    }

    fun getAudioFiles(root: File) {
        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {

            GlobalModel.audioFiles.clear()      /* fixed, avoid add the same file more times when jump to this activity again */

            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".3gp")) {
                    // File absolute path
                    Log.e("downloadFilePath", currentFile.getAbsolutePath())
                    // File Name
                    Log.e("downloadFileName", currentFile.getName())
                    fileList.add(currentFile.absoluteFile)
                    var fileName = currentFile.getName()

                    GlobalModel.audioFiles.add(AudioFiles(fileName))
                }
            }
            Log.w("fileList", "" + fileList.size)
        }
    }

}



