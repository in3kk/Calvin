package calvin.calvin.service;

import calvin.calvin.domain.Calvin_file;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@Service
public class CalvinFileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Calvin_file getFileOriginName(int file_code){
        String sql = "SELECT file_code, original_name, save_name FROM calvin_file WHERE file_code = ?";
        Calvin_file result = jdbcTemplate.query(sql, new Object[]{file_code}, new ResultSetExtractor<Calvin_file>() {
            @Override
            public Calvin_file extractData(ResultSet rs) throws SQLException, DataAccessException {
                Calvin_file calvinFile = new Calvin_file();
                if(rs.next()){
                    calvinFile.setFile_code(rs.getInt("file_code"));
                    calvinFile.setOriginal_name(rs.getString("original_name"));
                    calvinFile.setSave_name(rs.getString("save_name"));
                }
                return calvinFile;
            }
        });
        return result;
    }

    public ResponseEntity FileDownload(String save_name, String original_name){
//        String path = "F:\\CalvinUploadFiles\\"+save_name; //로컬
        String path = "/iceadmin/CalvinUploadFile/"+save_name;//서버
        try{
            UrlResource urlResource = new UrlResource("file:"+path);
            String encodedOriginalName = UriUtils.encode(original_name, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedOriginalName + "\"";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(urlResource);
        }catch(Exception e){
            // 사용자에게 오류 메시지 전송
            // 여기서는 간단히 "파일 다운로드 중 오류가 발생했습니다."라는 메시지를 반환하도록 설정
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 다운로드 중 오류가 발생했습니다.");
        }
    }
    public ResponseEntity FileDownload(String original_name){
//        String path = "F:\\DocumentFile\\"+original_name; //로컬
        String path = "/iceadmin/DocumentFile/"+original_name;//서버
        try{
            UrlResource urlResource = new UrlResource("file:"+path);
            String encodedOriginalName = UriUtils.encode(original_name, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedOriginalName + "\"";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(urlResource);
        }catch(Exception e){
            // 사용자에게 오류 메시지 전송
            // 여기서는 간단히 "파일 다운로드 중 오류가 발생했습니다."라는 메시지를 반환하도록 설정
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 다운로드 중 오류가 발생했습니다.");
        }
    }

    public String SelectFileSaveName(int file_code){
        String sql = "SELECT save_name FROM calvin_file WHERE file_code = ?";
        String fileSaveName = jdbcTemplate.queryForObject(sql, new Object[]{file_code}, String.class);
        return fileSaveName;
    }

    public boolean ExtensionValidation(String file_name){
        String[] extensions = new String[]{"jpg","jpeg","png","bmp",};
        boolean result = false;
        String file_extension = file_name.substring(file_name.lastIndexOf(".")+1);
        for(String extension : extensions){
            if(file_extension.equals(extension)){
                result = true;
                break;
            }
        }
        return result;
    }

    //파일 삭제
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public boolean DeleteFile(String FilePath, int file_code){
        File file = new File(FilePath);
        boolean result = false;
        if(file.exists()){
            file.delete();
            String sql = "UPDATE calvin_file SET delete_yn = 'y' WHERE file_code = ?";
            jdbcTemplate.update(sql, file_code);
            result = true;
        }
        return result;
    }
}
