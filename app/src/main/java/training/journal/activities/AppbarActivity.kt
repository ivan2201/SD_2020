package training.journal.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import training.journal.R

abstract class AppbarActivity : DrawerActivity() {

    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar = getToolbarView()
        toolbar?.title = getToolbarTitle()
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.displayOptions = ActionBar.DISPLAY_HOME_AS_UP
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.light_blue)))
            it.setHomeAsUpIndicator(
                    if (canOpenNavMenuFromToolbar()) R.drawable.ic_gamburger_menu
                    else R.drawable.ic_home_button
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (canOpenNavMenuFromToolbar()) {
                    openNavMenu()
                } else {
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected abstract fun getToolbarView(): Toolbar

    protected open fun getToolbarTitle(): String = getString(R.string.app_name)

    override fun canOpenNavMenuFromToolbar(): Boolean = true
}