package com.example.expirydate.util

import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker

object ImageUploadUtil {

    fun uploadPhoto(fragment:Fragment){
        ImagePicker.with(fragment)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }
}