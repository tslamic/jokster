package me.tadej.joker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject lateinit var factory: JokerViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = (application as App).component()
        component.inject(this)

        val model = ViewModelProviders.of(this, factory).get(JokerViewModel::class.java)
        model.data().observe(this, Observer {
            refresher.isRefreshing = false
            joke.text = it
        })

        val listener = SwipeRefreshLayout.OnRefreshListener {
            refresher.isRefreshing = true
            model.requestNewJoke()
        }

        refresher.setOnRefreshListener(listener)
        listener.onRefresh()
    }
}
