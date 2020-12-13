package training.journal.activities.auth

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog

import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response
import training.journal.R
import training.journal.activities.BaseActivity
import training.journal.api.Api
import training.journal.api.ApiUtils
import training.journal.api.CheckHostAsyncTask
import training.journal.dto.UserDto
import training.journal.repository.AuthRepository
import training.journal.repository.CurrentUserRepository
import training.journal.utils.logger.Logger
import java.net.HttpURLConnection

class LoginActivity : BaseActivity() {

    class LoginCheckHost(private val context: Context): CheckHostAsyncTask() {
        override fun onPostExecute(result: String)
        {
            if (result.equals("error")) {
                Toast.makeText(context, R.string.find_server_error, Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        remember_tv.setOnClickListener {
            remember_checkbox.isChecked = !remember_checkbox.isChecked
        }

        not_exist_acc_tv.setOnClickListener {
            router?.showRegistrationPage()
        }

        confirm_button.setOnClickListener {
            val token = ApiUtils.encodeEmailAndPasswordToAuthorizationHeader(email_et.text.toString(), password_et.text.toString())
            taskContainer.add(
                    Api.login(token).subscribe(
                            { onResponse(it, token) },
                            { onFail(it) }
                    )
            )
        }
        LoginCheckHost(applicationContext).execute()
    }

    private fun onResponse(response: Response<UserDto>, token: String) {
        when (response.code()) {
            HttpURLConnection.HTTP_OK -> {
                AuthRepository.doOnLogin(this, token, remember_checkbox.isChecked,
                        response.body()?.toUserInfo() ?: CurrentUserRepository.CURRENT_USER_EMPTY)
                Logger.d(this, "successfully login with code ${response.code()}")
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                MaterialDialog(this).show {
                    title(R.string.cannot_login)
                    message(R.string.incorrect_email_or_password)
                    negativeButton(R.string.close) {
                        it.cancel()
                    }
                }
                Logger.d(this, response.code())
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                MaterialDialog(this).show {
                    title(R.string.cannot_login)
                    message(R.string.server_not_available)
                    negativeButton(R.string.close) {
                        it.cancel()
                    }
                }
                Logger.d(this, response.code())
            }
            else -> {
                MaterialDialog(this).show {
                    title(R.string.cannot_login)
                    message(R.string.server_not_available)
                    negativeButton(R.string.close) {
                        it.cancel()
                    }
                }
                Logger.d(this, "unsupported code ${response.code()}")
            }
        }
    }

    private fun onFail(throwable: Throwable) {
        MaterialDialog(this).show {
            title(R.string.cannot_login)
            message(R.string.failed_login)
            negativeButton(R.string.close) {
                it.cancel()
            }
        }
        Logger.e(this, "Login failed : ${throwable.message}")
    }

    override fun getActivityLayoutId() = R.layout.activity_login
}
