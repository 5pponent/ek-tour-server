package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class EstimateListPagingResponse {
    private int currentPage;
    private int totalPage;
    private int currentPageCount;
    private int totalCount;
    private List<EstimateSimpleResponse> estimateList;
}
