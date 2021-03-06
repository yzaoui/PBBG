package com.bitwiserain.pbbg.view.page

import com.bitwiserain.pbbg.view.SiteSection
import com.bitwiserain.pbbg.view.template.MemberPageVM
import com.bitwiserain.pbbg.view.template.MemberTemplate
import io.ktor.html.Template
import kotlinx.html.*

fun homeMemberPage(memberPageVM: MemberPageVM): Template<HTML> = MemberTemplate("Home", memberPageVM, SiteSection.HOME).apply {
    endOfBody {
        script(src = "/js/home.js") {}
    }
}
