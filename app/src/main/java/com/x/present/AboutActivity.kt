package com.x.present

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import com.x.present.Utils.addClickScale
import com.x.present.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    private lateinit var videoView:VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        //window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.videoViewBackGround

        initBackground()

        /*window.apply {//Explode() Slide()
            enterTransition = Fade()
            exitTransition = Fade()
            reenterTransition = Fade()
            returnTransition = Fade()
        }*/

        binding.cardViewAbout.addClickScale(0.9f, 150)
        binding.cardViewGithub.addClickScale(0.9f, 150)
        binding.cardViewEmail.addClickScale(0.9f, 150)
        binding.cardViewGwent.addClickScale(0.9f, 150)

        //待项目开源后，改变此处url为新项目的地址
        val urlGithub =
            "https://github.com/Case-Closed-X/Android-Present-Application"
        val urlOutlook = "mailto:CaseClosedX@outlook.com"
        val urlGwent = "https://www.playgwent.com/en"

        binding.cardViewAbout.setOnClickListener {
            Toast.makeText(
                this,
                "猜猜是写给谁的呢？",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.cardViewGithub.setOnClickListener {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlGithub))
            startActivity(intent)
        }

        binding.cardViewEmail.setOnClickListener {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlOutlook))
            startActivity(intent)
        }

        binding.cardViewGwent.setOnClickListener {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlGwent))
            startActivity(intent)
        }
    }

    override fun onPause() {//退出或放置后台保存数据
        super.onPause()
        videoView.suspend()//释放资源，避免返回上一个activity卡顿
        videoView.setBackgroundResource(R.drawable.gwent)//避免切换回来后黑屏
    }

    /*override fun onDestroy() {
        super.onDestroy()
        videoView.suspend()//释放资源，避免返回上一个activity卡顿
    }*/

    private fun initBackground() {
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.gwent)
        videoView.setVideoURI(uri)

        videoView.setOnCompletionListener {
            videoView.start()
        }

        videoView.setOnPreparedListener {
            //videoView.background=null
            //videoView.animate().alpha(1f).setDuration(200).start()
            it.setOnInfoListener { mp, what, extra ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                    videoView.background = null//渲染第一帧时开始显示
                true
            }
            videoView.start()
        }
    }
}