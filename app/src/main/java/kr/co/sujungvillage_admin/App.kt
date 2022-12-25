package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.app.Application
import kr.co.sujungvillage_admin.base.Prefs

class App : Application() {
    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var prefs: Prefs
        lateinit var context: App
    }
}
