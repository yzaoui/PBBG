package com.bitwiserain.pbbg.view.page

import com.bitwiserain.pbbg.view.template.MemberPageVM
import com.bitwiserain.pbbg.view.template.MemberTemplate
import io.ktor.html.Template
import kotlinx.html.HTML
import kotlinx.html.script

fun squadPage(memberPageVM: MemberPageVM): Template<HTML> = MemberTemplate("Squad", memberPageVM).apply {
    endOfBody {
        script(src = "/js/squad.js") {}
        script(src = "/js/component/pbbg-progress-bar.js") {}
        script(src = "/js/component/pbbg-unit.js") {}
    }
}
