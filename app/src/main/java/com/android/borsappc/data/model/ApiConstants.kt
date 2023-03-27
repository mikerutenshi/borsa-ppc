package com.android.borsappc.data.model

object Position {
    const val DRAWER = "drawer"
    const val SEWER = "sewer"
    const val ASSEMBLER = "assembler"
    const val SOLE_STITCHER = "sole_stitcher"
    const val LINING_DRAWER = "lining_drawer"
    const val INSOLE_STITCHER = "insole_stitcher"
    const val ALL = "all"
}

object Role {
    const val ADMIN_QA = "admin_qa";
    const val ADMIN_WORK = "admin_work";
    const val ADMIN_PRICE = "admin_price";
    const val SUPER_USER = "super_user";
}

object Order {
    const val DIRECTION_ASC = "asc";
    const val DIRECTION_DESC = "desc";
    const val BY_WORKER_NAME = Pair("name", "name")
    const val BY_WORKER_POSITION = Pair("position", "position")
    const val BY_SPK_NO = Pair("spk_no", "spkNo")
    const val BY_ARTICLE_NO = Pair("article_no", "articleNo")
    const val By_CREATED_AT = Pair("created_at", "createdAt")
    const val By_CODE = Pair("code", "code")
    const val By_NAME = Pair("name", "name")
    const val BY_DONE_AT = Pair("done_at", "doneAt")
    const val BY_ASSIGNED_AT = Pair("assigned_at", "assignedAt")
}

object Filter {
    const val BY_CREATED_AT = "created_at";
    const val BY_DONE_AT = "done_at";
    const val BY_ASSIGNED_AT = "assigned_at";
}

const val API_DATE_FORMAT = "yyyy-MM-dd"
