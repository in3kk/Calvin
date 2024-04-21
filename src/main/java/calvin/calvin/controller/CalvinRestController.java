package calvin.calvin.controller;

import calvin.calvin.service.CalvinMemberService;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CalvinRestController {
    @Autowired
    CalvinMemberService calvinMemberService;
    @PostMapping("/id_dup_check")
    @ResponseBody
    public ResponseEntity<Boolean> IdDuplicateCheck(@RequestBody Map<String, Object> user_id){
        String id = user_id.get("user_id").toString();
        int rowCount = calvinMemberService.IdDuplicateCheck(id);
        boolean result;
        if(rowCount == 1){
            result = true;
        }else{
            result = false;
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping(value="/uploadSummernoteImageFile", produces = "application/json")
    @ResponseBody
    public Map<String,Object> uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {

        Map<String, Object> response = new HashMap<>();

//        String fileRoot = "C:\\summernote_image\\";	//저장될 외부 파일 경로
        String fileRoot = "F:\\CalvinUploadFiles\\";
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            response.put("filename", savedFileName);
            response.put("responseCode", "success");
        } catch (IOException e) {
            System.out.println("error : "+e);
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            response.put("responseCode", "error");
            e.printStackTrace();
        }

        return response;
    }
}
