package com.bitwiserain.pbbg.view.page

import com.bitwiserain.pbbg.view.SiteSection
import com.bitwiserain.pbbg.view.template.MemberPageVM
import com.bitwiserain.pbbg.view.template.MemberTemplate
import io.ktor.html.Template
import kotlinx.html.HTML
import kotlinx.html.link
import kotlinx.html.script

fun battlePage(memberPageVM: MemberPageVM): Template<HTML> = MemberTemplate("Battle", memberPageVM, SiteSection.BATTLE).apply {
    headContent {
        link(href = "/css/battle.css", rel = "stylesheet")
    }
    endOfBody {
        script(src = "/js/battle.js") {}
    }
}
