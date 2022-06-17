package renewal.ektour;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.service.EstimateService;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDummyData {

    private final EstimateService estimateService;

    @PostConstruct
    public void initData() {
        // 견적 더미 데이터 생성
        for (int i = 0; i < 20; i++) {
            String idx = String.valueOf(i + 1);
            EstimateRequest data = new EstimateRequest();
            data.setName("user" + idx);
            estimateService.save(data);
        }
    }
}
