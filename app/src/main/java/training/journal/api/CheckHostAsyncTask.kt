package training.journal.api

import android.os.AsyncTask
import android.util.Log
import okhttp3.Request
import okio.source
import java.io.InputStream
import java.net.URL


abstract class CheckHostAsyncTask: AsyncTask<String, Void, String>() {

    override
    fun doInBackground(vararg p0: String): String {
        try {
            var uRL = URL(RetrofitApiUtils.TRAINING_JOURNAL_URL)
            var connection = uRL.openConnection()
            connection.connect()
            var headers = connection.headerFields
            var nullHeader = headers[null].toString()
            var firstSpace = nullHeader.indexOf(' ')
            var secondSpace = nullHeader.indexOf(' ', firstSpace + 1)
            var statusCode = nullHeader.substring(firstSpace + 1, secondSpace)
            while (statusCode.toInt() == 301 || statusCode.toInt() == 302) {
                val url = headers["Location"].toString()
                val firstSlash = url.indexOf('/')
                var thirdSlash = url.indexOf('/', firstSlash+2)
                if (thirdSlash == -1)
                    thirdSlash = url.indexOf(']')
                val host = url.substring(1, thirdSlash)
                if (host != RetrofitApiUtils.TRAINING_JOURNAL_URL) {
                    RetrofitApiUtils.TRAINING_JOURNAL_URL = host
                } else {
                    return "ok"
                }
                uRL = URL(RetrofitApiUtils.TRAINING_JOURNAL_URL)
                connection = uRL.openConnection()
                connection.connect()
                headers = connection.headerFields
                nullHeader = headers[null].toString()
                firstSpace = nullHeader.indexOf(' ')
                secondSpace = nullHeader.indexOf(' ', firstSpace + 1)
                statusCode = nullHeader.substring(firstSpace + 1, secondSpace)
            }
            return "ok"
        } catch (e: Exception) {
            Log.e("checkHostError", e.localizedMessage, e);
            return "error"
        }
    }

    abstract override fun onPostExecute(result: String);
}