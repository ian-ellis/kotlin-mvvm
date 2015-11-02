package ian_ellis.androidmvvm

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ian_ellis.androidmvvm.pesentation.fragments.ToDosFragment


public class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fragmentManager
      .beginTransaction()
      .replace(R.id.layout_main, ToDosFragment.factory.create())
      .commit()

  }

}
