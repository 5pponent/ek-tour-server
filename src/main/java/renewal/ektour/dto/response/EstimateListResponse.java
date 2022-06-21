package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EstimateListResponse {
    private List<EstimateSimpleResponse> estimates;
}
