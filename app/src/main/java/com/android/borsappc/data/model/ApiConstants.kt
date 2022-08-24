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

object Sort {
    const val SORT_DIRECTION_ASC = "asc";
    const val SORT_DIRECTION_DESC = "desc";
    const val SORT_BY_WORKER_NAME = "name";
    const val SORT_BY_WORKER_POSITION = "position";
    const val SORT_BY_SPK_NO = "spk_no";
    const val SORT_BY_ARTICLE_NO = "article_no";
    const val SORT_BY_DATE = "created_at";
    const val SORT_BY_DONE_AT = "done_at";
    const val SORT_BY_ASSIGNED_AT = "assigned_at";
}

object Filter {
    const val BY_CREATED_AT = "created_at";
    const val BY_DONE_AT = "done_at";
    const val BY_ASSIGNED_AT = "assigned_at";
}
