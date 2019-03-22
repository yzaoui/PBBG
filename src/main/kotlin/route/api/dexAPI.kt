package com.bitwiserain.pbbg.route.api

import com.bitwiserain.pbbg.domain.usecase.DexUC
import com.bitwiserain.pbbg.domain.usecase.UserUC
import com.bitwiserain.pbbg.interceptSetUserOr401
import com.bitwiserain.pbbg.loggedInUserKey
import com.bitwiserain.pbbg.respondSuccess
import com.bitwiserain.pbbg.view.model.dex.DexItemsJSON
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.dexAPI(userUC: UserUC, dexUC: DexUC) = route("/dex") {
    interceptSetUserOr401(userUC)

    route("/items") {
        get {
            val loggedInUser = call.attributes[loggedInUserKey]

            val dex = dexUC.getDexItems(loggedInUser.id)

            call.respondSuccess(
                DexItemsJSON(
                    discoveredItems = dex.discoveredItems.associate { it.ordinal to it.toJSON() }.toSortedMap(),
                    lastItemIsDiscovered = dex.lastItemIsDiscovered
                )
            )
        }
    }
}
