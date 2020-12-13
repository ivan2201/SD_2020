package training.journal.lifecycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import training.journal.activities.BaseFragmentActivity
import training.journal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import training.journal.lifecycle.Page.Companion.PAGE_KEY
import training.journal.lifecycle.Page.Companion.USER_ID_KEY
import training.journal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import training.journal.utils.logger.Logger
import kotlin.reflect.full.createInstance

class Router(private val activity: Activity) {

    fun showLoginPage() {
        showPage(Page.Activity.Login)
    }

    fun showCalendarPage() {
        showPage(Page.Fragment.Calendar)
    }

    fun showWorkoutPage(workoutId: Long) {
        val workoutIdBundle = Bundle(1)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.Workout, workoutIdBundle)
    }

    fun showTrainingViewPage() {
        showPage(Page.Fragment.TrainingView)
    }

    fun showActiveExercisePage(userId: Long, workoutId: Long) {
        val workoutIdBundle = Bundle(2)
        workoutIdBundle.putLong(USER_ID_KEY, userId)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.ActiveExercise, workoutIdBundle)
    }

    fun showExercisePage(exerciseId: Long) {
        val exerciseIdBundle = Bundle(1)
        exerciseIdBundle.putLong(EXERCISE_ID_KEY, exerciseId)
        showPage(Page.Fragment.Exercise, exerciseIdBundle)
    }

    fun showResultsPage() {
        showPage(Page.Fragment.Results)
    }

    fun showSettingsPage() {
        showPage(Page.Activity.Settings)
    }

    fun showAccountSettingsPage() {
        showPage(Page.Activity.AccountSettings)
    }

    fun showAccountSettingsSubPage() {
        showPage(Page.Fragment.AccountSettings)
    }

    fun showRegistrationPage() {
        showPage(Page.Activity.Registration)
    }

    private fun showPage(page: Page, bundle: Bundle? = null) {
        Logger.d(this, "showPage $page")
        when (page) {
            is Page.Activity -> showActivity(page, bundle)
            is Page.Fragment -> {
                if (activity is BaseFragmentActivity) {
                    replaceFragment(page, bundle)
                } else {
                    showActivityWithFragment(page, bundle)
                }
            }
        }
    }

    private fun showActivity(page: Page.Activity, bundle: Bundle?) {
        val intent = Intent(activity, page.clazz.java)
        bundle?.let { intent.putExtras(it) }
        activity.startActivity(intent)
    }

    private fun showActivityWithFragment(page: Page.Fragment, bundle: Bundle?) {
        val intent = Intent(activity, BaseFragmentActivity::class.java)
        intent.putExtra(PAGE_KEY, page)
        bundle?.let { intent.putExtras(it) }
        activity.startActivity(intent)
    }

    private fun replaceFragment(page: Page.Fragment, bundle: Bundle?) {
        bundle?.let { activity.intent.putExtras(it) }
        (activity as? BaseFragmentActivity)?.setFragment(page.clazz.createInstance())
    }

    fun goToPrevFragment() {
        (activity as? BaseFragmentActivity)?.setPrevFragment()
    }
}