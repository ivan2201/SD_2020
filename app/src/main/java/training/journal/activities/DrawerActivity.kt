package training.journal.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import kotlinx.android.synthetic.main.activity_base_fragment.*
import training.journal.R
import training.journal.controllers.DrawerController
import training.journal.controllers.NavigationMenuController
import training.journal.model.UserInfo
import training.journal.navmenu.NavigationMenuListener
import training.journal.repository.AuthRepository
import training.journal.repository.CurrentUserRepository

abstract class DrawerActivity : BaseActivity() {

    companion object {
        const val SEARCH_ITEM_ID = 1L
        const val BOOKMARKS_ITEM_ID = 2L
        const val FAVOURITE_ITEM_ID = 3L
        const val SETTINGS_ITEM_ID = 4L
        const val EXIT_ITEM_ID = 5L
        const val RESULTS_ITEM_ID = 6L
        const val CALENDAR_ITEM_ID = 7L
    }

    private val profile: ProfileDrawerItem = ProfileDrawerItem()

    private var drawerController: NavigationMenuController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerController = DrawerController(root_drawer, slider)
        setupItems()
        slider.apply {
            onDrawerItemClickListener = { _, item, _ ->
                when (item.identifier) {
                    CALENDAR_ITEM_ID -> {
                        router?.showCalendarPage()
                    }
                    RESULTS_ITEM_ID -> {
                        router?.showResultsPage()
                    }
                    SEARCH_ITEM_ID -> {
                    }
                    BOOKMARKS_ITEM_ID -> {
                    }
                    FAVOURITE_ITEM_ID -> {
                    }
                    EXIT_ITEM_ID -> {
                        buildExitDialog()
                    }
                    SETTINGS_ITEM_ID -> router?.showSettingsPage()
                }
                closeNavMenu()
                true
            }
        }
        attachCurrentUserToSlider()
        CurrentUserRepository.currentUser.observe(this, Observer { attachCurrentUserToSlider() })
    }

    private fun buildExitDialog() {
        MaterialDialog(this).show {
            title(R.string.quit)
            message(R.string.quit_confirm_msg)
            positiveButton(R.string.preference_exit) {
                AuthRepository.doOnLogout(this@DrawerActivity)
            }
            negativeButton(R.string.close) {
                it.cancel()
            }
        }
    }

    private fun setupItems() {

        val calendarItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.drawer_item_calendar)
            icon = ImageHolder(R.drawable.ic_item_calendar)
            identifier = CALENDAR_ITEM_ID
            isSelectable = false
        }

        val resultsItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.drawer_item_results)
            icon = ImageHolder(R.drawable.ic_results)
            identifier = RESULTS_ITEM_ID
            isSelectable = false
        }

        val favouritesItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.drawer_item_favourites)
            // todo set icon later
//        icon = ImageHolder()
            identifier = FAVOURITE_ITEM_ID
            isSelectable = false
        }

        val settingsItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.drawer_item_settings)
            icon = ImageHolder(R.drawable.ic_settings)
            identifier = SETTINGS_ITEM_ID
            isSelectable = false
        }

        val exitItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.preference_exit)
            identifier = EXIT_ITEM_ID
            isSelectable = false
        }

        slider.addItems(
                calendarItem,
                resultsItem,
                favouritesItem,
                DividerDrawerItem(),
                settingsItem,
                exitItem
        )
    }

    override fun onBackPressed() {
        if (root_drawer.isDrawerOpen(slider)) {
            root_drawer.closeDrawer(slider)
        } else {
            super.onBackPressed()
        }
    }

    fun openNavMenu() {
        drawerController?.openMenu()
    }

    fun closeNavMenu() {
        drawerController?.closeMenu()
    }

    fun addListener(listener: NavigationMenuListener) {
        drawerController?.addMenuListener(listener)
    }

    fun removeListener(listener: NavigationMenuListener) {
        drawerController?.removeMenuListener(listener)
    }

    private fun attachCurrentUserToSlider() {
        val userInfo = CurrentUserRepository.currentUser.value
        userInfo?.let {
            when {
                it.pictureUrlStr != null -> profile.icon = ImageHolder(it.pictureUrlStr)
                it.genderType == UserInfo.GenderType.MALE -> profile.icon = ImageHolder(getDrawable(R.drawable.male_stub))
                it.genderType == UserInfo.GenderType.FEMALE -> profile.icon = ImageHolder(getDrawable(R.drawable.female_stub))
                else -> profile.icon = null
            }
            profile.name = StringHolder(it.lastName + " " + it.firstName)
            profile.description = StringHolder(it.email)
            slider.accountHeader = AccountHeaderView(this).apply {
                attachToSliderView(slider)
                addProfiles(profile)
                onAccountHeaderListener = { view, profile, current ->
                    false
                }
                selectionListEnabled = false
                onAccountHeaderProfileImageListener = { _, _, _ ->
                    router?.showAccountSettingsPage()
                    true
                }
            }
        }
    }

    protected open fun canOpenNavMenuFromToolbar(): Boolean = false
}