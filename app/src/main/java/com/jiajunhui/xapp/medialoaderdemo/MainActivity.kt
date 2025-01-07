package com.jiajunhui.xapp.medialoaderdemo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jiajunhui.xapp.medialoader.MediaLoader
import com.jiajunhui.xapp.medialoader.bean.AudioResult
import com.jiajunhui.xapp.medialoader.bean.FileResult
import com.jiajunhui.xapp.medialoader.bean.FileType
import com.jiajunhui.xapp.medialoader.bean.PhotoResult
import com.jiajunhui.xapp.medialoader.bean.VideoResult
import com.jiajunhui.xapp.medialoader.callback.OnAudioLoaderCallBack
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack
import com.jiajunhui.xapp.medialoader.callback.OnPhotoLoaderCallBack
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack
import com.jiajunhui.xapp.medialoader.filter.PhotoFilter
import com.jiajunhui.xapp.medialoader.inter.OnRecursionListener
import com.jiajunhui.xapp.medialoader.utils.TraversalSearchLoader
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File

class MainActivity : AppCompatActivity() {
    private var tv_photo_info: TextView? = null
    private var tv_video_info: TextView? = null
    private var tv_audio_info: TextView? = null
    private var tv_file_info: TextView? = null
    private var tv_traversal_info: TextView? = null

    private var start: Long = 0
    private var mTask: AsyncTask<*, *, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_photo_info = findViewById<View>(R.id.tv_photo_info) as TextView
        tv_video_info = findViewById<View>(R.id.tv_video_info) as TextView
        tv_audio_info = findViewById<View>(R.id.tv_audio_info) as TextView
        tv_file_info = findViewById<View>(R.id.tv_file_info) as TextView
        tv_traversal_info = findViewById<View>(R.id.tv_traversal_info) as TextView

        findViewById<View>(R.id.btn_next).setOnClickListener {
            val intent = Intent(applicationContext, MainActivity2::class.java)
            startActivity(intent)
        }

        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(
                this,
                100,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).build()
        )

        if (!isFM) {
            requestStorageManager(this)
        }
    }

    private fun recursionLoad() {
        val params = TraversalSearchLoader.LoadParams()
        //需要遍历的根目录
        params.root = Environment.getExternalStorageDirectory()
        //过滤器
        params.fileFilter = PhotoFilter()
        mTask = TraversalSearchLoader.loadAsync(params, object : OnRecursionListener {
            override fun onStart() {
                println("load_log : start---->")
            }

            override fun onItemAdd(file: File, counter: Int) {
                println("load_log : onItemAdd : " + file.absolutePath)
                tv_traversal_info!!.text = "number : " + counter + " : " + file.absolutePath
            }

            override fun onFinish(files: List<File>) {
                println("load_log : finish ***** size = " + files.size)
                tv_traversal_info!!.text = "number : " + files.size
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val hasStorage =
            EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (requestCode == 100 && hasStorage) {
            if (hasStorage) {
                start = System.currentTimeMillis()
                recursionLoad()
                startLoad()
            } else {
                Toast.makeText(this, "permission deny", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoad() {
        loadPhotos()
        loadAudios()
        loadVideos()
        val mInfos = StringBuilder()
        MediaLoader.getLoader()
            .loadFiles(this@MainActivity, object : OnFileLoaderCallBack(FileType.DOC) {
                override fun onResult(result: FileResult) {
                    mInfos.append("doc file : " + result.items.size).append("\n")
                }
            })

        MediaLoader.getLoader()
            .loadFiles(this@MainActivity, object : OnFileLoaderCallBack(FileType.ZIP) {
                override fun onResult(result: FileResult) {
                    mInfos.append("zip file : " + result.items.size).append("\n")
                }
            })

        MediaLoader.getLoader()
            .loadFiles(this@MainActivity, object : OnFileLoaderCallBack(FileType.APK) {
                override fun onResult(result: FileResult) {
                    mInfos.append("apk file : " + result.items.size).append("\n")
                    mInfos.append("consume time : " + (System.currentTimeMillis() - start))
                        .append("ms")
                    tv_file_info!!.text = mInfos.toString()
                }
            })
    }

    private fun loadPhotos() {
        MediaLoader.getLoader().loadPhotos(this, object : OnPhotoLoaderCallBack() {
            override fun onResult(result: PhotoResult) {
                tv_photo_info!!.text = "图片: " + result.items.size + " 张"
            }
        })
    }

    private fun loadAudios() {
        MediaLoader.getLoader().loadAudios(this, object : OnAudioLoaderCallBack() {
            override fun onResult(result: AudioResult) {
                tv_audio_info!!.text = "音乐: " + result.items.size + " 个"
            }
        })
    }

    private fun loadVideos() {
        MediaLoader.getLoader().loadVideos(this, object : OnVideoLoaderCallBack() {
            override fun onResult(result: VideoResult) {
                tv_video_info!!.text = "视频: " + result.items.size + " 个"
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mTask != null) {
            mTask!!.cancel(true)
        }
    }

    companion object {
        val isFM: Boolean
            get() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    return Environment.isExternalStorageManager()
                }
                return false
            }

        fun requestStorageManager(context: Context?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && context != null) {
                var itt = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                itt.setData(Uri.parse("package:" + context.packageName))
                try {
                    context.startActivity(itt)
                } catch (e: Exception) {
                    e.printStackTrace()
                    itt = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    context.startActivity(itt)
                }
            }
        }
    }
}
