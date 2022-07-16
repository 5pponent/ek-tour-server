package renewal.ektour.dto.response

data class PageTotalCountResponse(
    var totalCount: Int = 0
)

data class EstimateSimpleResponse(
    var id: Long = 0L,
    var name: String = "",
    var travelType: String = "",
    var departPlace: String = "",
    var arrivalPlace: String = "",
    var vehicleType: String = "",
    var createdDate: String = "",
)

data class EstimateListPagingResponse(
    var currentPage: Int,
    var totalPage: Int,
    var currentPageCount: Int,
    var totalCount: Int,
    var estimateList: List<EstimateSimpleResponse>,
)