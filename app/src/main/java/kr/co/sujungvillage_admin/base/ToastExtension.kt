package kr.co.sujungvillage_admin.base // ktlint-disable filename // ktlint-disable package-name

import android.content.Context
import android.widget.Toast

/** 토스트 활성화 확장함수 (LENGTH_SHORT) */
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
