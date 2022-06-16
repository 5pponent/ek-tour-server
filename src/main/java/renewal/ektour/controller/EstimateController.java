package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import renewal.ektour.domain.estimate.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.response.BoolResponse;
import renewal.ektour.service.EstimateService;

import static renewal.ektour.dto.response.RestResponse.success;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/estimate")
public class EstimateController {

    private final EstimateService estimateService;

    /**
     * 견적요청 생성(저장)
     */
    @PostMapping("")
    public ResponseEntity<?> saveSimpleEstimate(@RequestBody EstimateRequest form) {
        Estimate savedEstimate = estimateService.save(form);
        return success(savedEstimate.toResponse());
    }

    /**
     * 견적요청 조회
     */
    // 하나 조회 (상세페이지용)
    @GetMapping("/{estimate_id}")
    public ResponseEntity<?> findById(@PathVariable("estimate_id") Long estimateId) {
        return success(null);
    }

    // 전체조회 (페이징)
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return success(null);
    }

    /**
     * 견적요청 삭제
     */
    // 실제 삭제하는게 아니라 visibility 를 false 로 바꾸고 사용자에게만 안 보여준다.
    // 관리자가 직접 삭제함 디비에는 데이터가 남아있다 (사용자는 삭제 X)
    @PutMapping("/{estimate_id}")
    public ResponseEntity<?> deleteById(@PathVariable("estimate_id") Long estimateId) {
        estimateService.delete(estimateId);
        return success(new BoolResponse(true));
    }
}
