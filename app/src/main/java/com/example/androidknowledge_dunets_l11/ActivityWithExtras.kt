package com.example.androidknowledge_dunets_l11

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidknowledge_dunets_l11.databinding.ActivityWithExtrasBinding

private const val STRING_EXTRA_KEY = "STRING_PARAM"
private const val INT_EXTRA_KEY = "INT_VAL"

class ActivityWithExtras : AppCompatActivity() {

    private lateinit var binding: ActivityWithExtrasBinding

    companion object {
        @JvmStatic
        fun start(context: Context, strParam: String, intParam: Int) {
            val intent = Intent(context, ActivityWithExtras::class.java)
            intent.putExtra(STRING_EXTRA_KEY, strParam)
            intent.putExtra(INT_EXTRA_KEY, intParam)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupViewsFromBundle()
    }

    private fun setupBinding() {
        binding = ActivityWithExtrasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewsFromBundle() {
        intent?.also {
            binding.intContainer.text = it.getIntExtra(INT_EXTRA_KEY, 0).toString()
            binding.stringContainer.text = it.getStringExtra(STRING_EXTRA_KEY)
        }
    }
}