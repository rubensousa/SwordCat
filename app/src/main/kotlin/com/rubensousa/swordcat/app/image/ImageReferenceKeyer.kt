package com.rubensousa.swordcat.app.image

import coil3.key.Keyer
import coil3.request.Options

class ImageReferenceKeyer : Keyer<ImageReference> {

    override fun key(
        data: ImageReference,
        options: Options
    ): String {
        return data.id
    }

}
