package renewal.ektour.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import renewal.ektour.util.AdminConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class WebController {

    /**
     * 리액트 라우팅 리다이렉션
     */
    @GetMapping(value = {"", "/estimate/list/**", "/notice","/list", "/introduce",
            "/bus", "/request", "/my", "estimate/my/list/**","/search/my/estimate",
            "/service-center", "/mobile", "/mobile/**"})
    public String redirect() {
        return "forward:/index.html";
    }

    /**
     * 로고 내려주기
     */
    @GetMapping("/download")
    public ResponseEntity<?> logoDownload() throws IOException {
        Path path = Paths.get(AdminConfig.LINUX_LOGO_PATH);
        String contentType = Files.probeContentType(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename("logo.png", StandardCharsets.UTF_8)
                        .build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
