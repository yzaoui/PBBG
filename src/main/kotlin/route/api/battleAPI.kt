package com.bitwiserain.pbbg.route.api

import com.bitwiserain.pbbg.*
import com.bitwiserain.pbbg.domain.usecase.UserUC
import io.ktor.application.call
import io.ktor.routing.*

fun Route.battleAPI(userUC: UserUC, battleUC: BattleUC) = route("/battle") {
    interceptSetUserOr401(userUC)

    route("/session") {
        get {
            val loggedInUser = call.attributes[loggedInUserKey]

            val battle = battleUC.getCurrentBattle(loggedInUser.id)

            call.respondSuccess(battle?.toJSON())
        }

        param("action") {
            /**
             * Expects query string:
             *   action = generate
             */
            post {
                val loggedInUser = call.attributes[loggedInUserKey]

                val battle = battleUC.generateBattle(loggedInUser.id)

                call.respondSuccess(battle.toJSON())
            }
        }
    }
}

fun Battle.toJSON() = BattleJSON(
    allies = allies.map { it.toJSON() },
    enemies = enemies.map { it.toJSON() }
)
