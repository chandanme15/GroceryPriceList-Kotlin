package com.project.commoditiesCurrentPrice.utils

class Constants {
    companion object {
        var PAGE_COUNT = 1
        const val REQUEST_TIME_OUT : Long = 30
        const val DB_EXPIRY_TIME : Long = 40000

        const val DatabaseTime = "DatabaseTime"
        const val BASE_URL = "https://api.data.gov.in/"

        const val API_KEY = "579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b"
        const val API_RESPONSE_FORMAT = "json"
        const val NO_OF_RECORDS_PER_REQUEST = "10"
    }

    interface QueryMap {
        companion object {
            const val ATTRIBUTE_API_KEY = "api-key"
            const val ATTRIBUTE_FORMAT = "format"
            const val ATTRIBUTE_LIMIT = "limit"
            const val ATTRIBUTE_OFFSET = "offset"
        }
    }
}