package com.x.present

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.x.present.databinding.ActivityMainBinding
import android.animation.Animator

import android.animation.AnimatorListenerAdapter
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.widget.VideoView
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.x.present.Utils.addClickScale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel
    private lateinit var sp: SharedPreferences//存储数据

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        videoView = binding.videoView
        //导入依赖指定版本，才能使用不同的ViewModelProvider()
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //viewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application)).get(MainViewModel::class.java)
        sp = getPreferences(Context.MODE_PRIVATE)
        val nextReserved = sp.getInt("next_reserved", 0)//首次初始化

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(nextReserved)
        ).get(MainViewModel::class.java)

        //initBackground();
        init()

    }

    override fun onPause() {//退出或放置后台保存数据
        super.onPause()
        binding.button.isEnabled=false
        Utils.animateScaleXY(binding.cardView, true)//放置后台退场
        sp.edit {
            putInt("next_reserved", viewModel.next.value!!)
        }
        //videoView.suspend()
    }

    override fun onResume() {//activity的生命周期，重新回来后
        super.onResume()
        //window.decorView.post {  }
        viewModel.refresh()//解决快速从about activity返回，视频不会重新start
    }

    private fun init() {
        //setVideo() binding.cardView.apply{   }
        binding.cardView.addClickScale(0.9f, 150)
        binding.cardView.setOnClickListener {

        }
        binding.cardView.setOnLongClickListener {
            intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            true
        }

        binding.button.addClickScale(0.9f, 150)
        binding.button.setOnClickListener {
            /*val inY = ObjectAnimator.ofFloat(binding.cardView, "scaleY", 0f)
            val inX = ObjectAnimator.ofFloat(binding.cardView, "scaleX", 0f)
            val animSet = AnimatorSet()
            animSet.play(inY).with(inX)
            animSet.duration = 400
            animSet.start()*/

            binding.button.isEnabled=false
            val animSet = Utils.animateScaleXY(binding.cardView, true)

            animSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    viewModel.update()//动画执行完毕后，更新数据
                }
            })
        }

        viewModel.next.observe(this) {//如果数据改变

            var path = "android.resource://com.x.present/"
            path += when (viewModel.next.value) {
                1 -> R.raw.awalak
                2 -> R.raw.knickers
                3 -> R.raw.musicians
                4 -> R.raw.roach
                5 -> R.raw.shupe
                else -> R.raw.shupe
            }

            val uri: Uri = Uri.parse(path)
            videoView.setVideoURI(uri)
            //videoView.requestFocus()
            //videoView.setZOrderOnTop(true)

            videoView.setOnCompletionListener {//重复播放
                binding.button.isEnabled=false
                val animSet = Utils.animateScaleXY(binding.cardView, true)

                animSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        videoView.start()//动画执行完毕后，重新播放
                    }
                })
            }

            videoView.setOnPreparedListener {//准备好视频后
                /*val inY = ObjectAnimator.ofFloat(binding.cardView, "scaleY", 0f, 1.1f, 1f)
                val inX = ObjectAnimator.ofFloat(binding.cardView, "scaleX", 0f, 1.1f, 1f)
                val animSet = AnimatorSet()
                animSet.play(inY).with(inX)
                animSet.duration = 800
                animSet.start()*/
                it.setOnInfoListener { mp, what, extra ->
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        val animSet = Utils.animateScaleXY(binding.cardView, false)//第一帧开始渲染时开始动画

                        animSet.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.button.isEnabled = true//动画执行完毕，按钮启用
                            }
                        })
                    }
                    true
                }
                videoView.start()//动画开始时即使准备好，播放视频也为黑屏
            }
        }
    }

//    private fun setVideo() {
    /*videoView.outlineProvider = object : ViewOutlineProvider() {//圆角
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 50.toFloat())
        }
    }
// 打开开关
    videoView.clipToOutline = true*/
//    }

    /*private fun initBackground() {
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.gwent)
        val videoView = binding.videoViewBackGround
        videoView.setVideoURI(uri)
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.start()
        }

        *//*videoView.setOnPreparedListener {
            videoView.background=null
            videoView.start()
            *//**//*val alpha = ObjectAnimator.ofFloat(videoView, "alpha", 0f, 1f)
            val animSet = AnimatorSet()
            animSet.play(alpha)
            animSet.duration = 1000
            animSet.start()*//**//*

            //binding.videoView1!!.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
        }*//*
    }*/
}