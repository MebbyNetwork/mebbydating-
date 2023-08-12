package com.example.domain.models

data class ReportModel(
    val userId: String,
    val reportedUserId: String,
    val reportReason: String,
    val reportDescription: String? = null,
    val reportTime: Long,
    val isChecked: Boolean = false,
    val checkedBy: String? = null,
    val checkTime: Long? = null
) {
    fun toHashMap(): HashMap<String, Any> {
        val reportMap = HashMap<String, Any>()

        reportMap["userId"] = this.userId
        reportMap["reportedUserId"] = this.reportedUserId
        reportMap["reportReason"] = this.reportReason
        reportMap["reportDescription"] = this.reportDescription ?: ""
        reportMap["reportTime"] = this.reportTime
        reportMap["isChecked"] = this.isChecked
        reportMap["checkedBy"] = this.checkedBy ?: "No checked"
        reportMap["checkTime"] = this.checkTime ?: 0
        return reportMap
    }
}
