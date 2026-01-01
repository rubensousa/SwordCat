package com.rubensousa.swordcat.domain.fixtures

import com.rubensousa.swordcat.domain.CatRequest

object CatRequestFixtures {

    fun create(
        limit: Int = 25,
        offset: Int = 0,
    ): CatRequest {
        return CatRequest(
            limit = limit,
            offset = offset
        )
    }

}
