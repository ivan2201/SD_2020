package training.journal.repository

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import training.journal.activities.BaseActivity
import training.journal.db.entity.UserEntity
import training.journal.model.UserInfo

object AuthRepository {

    const val TRAINING_PREFERENCE = "Training"
    const val USER_TOKEN = "user_token_key"
    const val USER_INFO_KEY = "user_info_key"

    fun doOnLogin(activity: BaseActivity, token: String, needRemember: Boolean, userInfo: UserInfo) {
        activity.apply {
            CurrentUserRepository.currentUser.value = userInfo
            GlobalScope.launch(Dispatchers.IO) {
                var user = database?.userDao()?.getByEmail(userInfo.email)
                if (user == null) {
                    user = UserEntity(
                            userInfo.firstName,
                            userInfo.lastName,
                            userInfo.fatherName,
                            userInfo.email,
                            userInfo.genderType.toApiStr(),
                            userInfo.pictureUrlStr
                    )
                    database?.userDao()?.insert(user)
                }
                withContext(Dispatchers.Main) {
                    if (needRemember) {
                        getSharedPreferences(TRAINING_PREFERENCE, Context.MODE_PRIVATE).edit().putString(USER_TOKEN, token).apply()
                        getSharedPreferences(TRAINING_PREFERENCE, Context.MODE_PRIVATE).edit().putString(USER_INFO_KEY, Gson().toJson(user)).apply()
                    }
                    router?.showCalendarPage()
                    finish()
                }
            }
        }
    }

    fun doOnLogout(activity: BaseActivity) {
        activity.apply {
            CurrentUserRepository.currentUser.value = null
            getSharedPreferences("Training", Context.MODE_PRIVATE).edit().remove(USER_TOKEN).apply()
            getSharedPreferences("Training", Context.MODE_PRIVATE).edit().remove(USER_INFO_KEY).apply()
            router?.showLoginPage()
            finish()
        }
    }
}