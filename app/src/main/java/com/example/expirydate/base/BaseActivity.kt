package com.example.expirydate.base

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expirydate.R

open class BaseActivity : AppCompatActivity() {
    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showError(message: String) {
        showToast(message)
    }

    @SuppressLint("ShowToast")
    fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }

    private var mProgressDialog: ProgressDialog? = null

    fun showLoading() {
        mProgressDialog?.setMessage(getString(R.string.loading))
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.show()
    }

    fun hideLoading() {
        var handler: android.os.Handler? = android.os.Handler()
        handler?.postDelayed({
            mProgressDialog.let {
                mProgressDialog?.hide()
                mProgressDialog?.dismiss()
                mProgressDialog = null
            }

        }, 500)
    }

    fun backPressed() {
        this.onBackPressed()
    }
}