package pbbg.view.template

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*
import pbbg.view.GuestPageVM

open class GuestTemplate(private val pageTitle: String, private val member: GuestPageVM) : Template<HTML> {
    val headContent = Placeholder<HEAD>()
    val content = Placeholder<MAIN>()
    val endOfBody = Placeholder<BODY>()
    override fun HTML.apply() {
        head {
            title { +pageTitle }
            link(href = "/css/common.css", rel = "stylesheet")
            insert(headContent)
        }
        body {
            div(classes = "container") {
                div(classes = "content") {
                    nav(classes = "sidebar") {
                        a(href = "/") {
                            +"Index"
                        }
                    }
                    main {
                        id = "main"
                        insert(content)
                    }
                }
                footer {
                    +"BitwiseRain © 2018"
                }
            }
            insert(endOfBody)
        }
    }
}
