package training.journal.lifecycle

import training.journal.activities.auth.LoginActivity
import training.journal.activities.auth.RegistrationActivity
import training.journal.activities.settings.AccountSettingsActivity
import training.journal.activities.settings.SettingsActivity
import training.journal.fragments.ActiveExerciseFragment
import training.journal.fragments.BaseFragment
import training.journal.fragments.CalendarFragment
import training.journal.fragments.ExerciseFragment
import training.journal.fragments.ResultsFragment
import training.journal.fragments.TrainingViewFragment
import training.journal.fragments.WorkoutFragment
import training.journal.fragments.settings.AccountSettingsFragment
import java.io.Serializable
import kotlin.reflect.KClass

sealed class Page : Serializable {

    sealed class Activity : Page() {

        abstract val clazz: KClass<out android.app.Activity>

        object Login : Activity() {
            override val clazz = LoginActivity::class
        }

        object Settings : Activity() {
            override val clazz = SettingsActivity::class
        }

        object AccountSettings : Activity() {
            override val clazz = AccountSettingsActivity::class
        }

        object Registration : Activity() {
            override val clazz = RegistrationActivity::class
        }
    }

    sealed class Fragment : Page() {

        abstract val clazz: KClass<out BaseFragment>

        object AccountSettings : Fragment() {
            override val clazz = AccountSettingsFragment::class
        }

        object Results : Fragment() {
            override val clazz = ResultsFragment::class
        }

        object ActiveExercise : Fragment() {
            override val clazz = ActiveExerciseFragment::class
        }

        object Calendar : Fragment() {
            override val clazz = CalendarFragment::class
        }

        object Exercise : Fragment() {
            override val clazz = ExerciseFragment::class
        }

        object Workout : Fragment() {
            override val clazz = WorkoutFragment::class
        }

        object TrainingView : Fragment() {
            override val clazz = TrainingViewFragment::class
        }
    }

    companion object {
        const val PAGE_KEY = "PAGE"
        const val USER_ID_KEY = "USER_ID"
        const val WORKOUT_ID_KEY = "WORKOUT_ID"
        const val EXERCISE_ID_KEY = "EXERCISE_ID"
    }
}