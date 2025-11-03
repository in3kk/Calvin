package calvin.calvin.service;

import calvin.calvin.configuration.WebConfig;
import calvin.calvin.domain.BoardView;
import calvin.calvin.domain.Calvin_Board;
import calvin.calvin.domain.Calvin_file;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalvinBoardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CalvinFileService calvinFileService;
    @Value("${upload.dir}")
    private String uploadDir;
    //유효성 검사 코드
    public String WordValidationPro(String word){
        String [] word_data = {"select", "insert", "delete", "update", "create", "drop", "exec", "union", "fetch", "declare", "truncate"};
        for(String data : word_data){
            word = word.replaceAll(data,"");
        }
        Pattern p = Pattern.compile("(%|#|-|>|<|=|'|\")");
        Matcher m = p.matcher(word);
        word = m.replaceAll("");
        return word;
    }
    //게시글 작성
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int insertBoard(String title, String contents, String member_id, String board_type){
        String sql = "SELECT member_code FROM calvin_member WHERE id = ?";
        int code = jdbcTemplate.queryForObject(sql, new Object[]{member_id}, Integer.class);
        sql = "INSERT INTO calvin_board(member_code,title, contents, created_date,file_code, board_type) VALUES(?,?,?,SYSDATE(),-1,?)";
        //sql = "INSERT INTO calvin_board(member_code,title, contents, created_date,file_code, board_type) VALUES(?,?,?,current_date(),-1,?)";
        System.out.println("contents size : "+contents);
        int result = jdbcTemplate.update(sql, code, title, contents,board_type);
        return result;
    }

    //게시글 작성 첨부파일
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int insertBoard(String title, String contents, String member_id, List<MultipartFile> file_list, String board_type){
        Pattern p1 = Pattern.compile("<([a-zA-Z]+)(\\s[^>]*)?>(?![\\s\\S]*<\\/\\1>)");
        Matcher m = p1.matcher(contents);
        contents = m.replaceAll("");//드래그앤드롭 이미지 입력 방지 코드
        int result = 0;
        try{
            String sql = "SELECT member_code FROM calvin_member WHERE id = ?";
            int[] file_code_list = new int[]{-1,-1,-1,-1,-1};
            String[] file_name_list = new String[]{"","","","",""};
            int current_code = 0;
            for(MultipartFile file : file_list){
                file_code_list[current_code++] = SaveFile(file);
            }
            current_code = 0;
            for(int file_code : file_code_list){
                if(file_code != -1){
                    file_name_list[current_code++] = calvinFileService.SelectFileSaveName(file_code);
                }
            }
            System.out.println("contents : "+contents);
            current_code = 0;
            for(String file_name : file_name_list){
                if(!file_name_list[current_code++].equals("")){
                    if(calvinFileService.ExtensionValidation(file_name)){
                        contents = contents.replace("&lt;&lt;&lt;"+current_code+"&gt;&gt;&gt;","<img src=\"/imgPath/"+file_name+"\" width=\"100%\" height=\"auto\"/>");
                    //<img src="F:CalvinUploadFiles\fd714363-c4b9-4fec-9951-4e1f0b293333_Selfie_20220602_인삼무_000.jpg" width="100%" height="auto">
                    }
                }
            }
            int code = jdbcTemplate.queryForObject(sql, new Object[]{member_id}, Integer.class);
            sql = "INSERT INTO calvin_board(member_code,title, contents, created_date,file_code1,file_code2, file_code3, file_code4, file_code5,board_type) VALUES(?,?,?,SYSDATE(),?,?,?,?,?,?)";
            //sql = "INSERT INTO calvin_board(member_code,title, contents, created_date,file_code1,file_code2, file_code3, file_code4, file_code5,board_type) VALUES(?,?,?,current_date(),?,?,?,?,?,?)";
            result = jdbcTemplate.update(sql, code, title, contents,file_code_list[0],file_code_list[1],file_code_list[2],file_code_list[3],file_code_list[4],board_type);
        }catch (Exception e){
            System.out.println("에러 : "+e);
        }
        return result;
    }

    //게시글 수정
    public int updateBoard(String title, String contents, int board_code){
        String sql = "UPDATE calvin_board SET title = ?, contesnts = ? WHERE board_code = ?";
        int result = jdbcTemplate.update(sql, title, contents, board_code);
        return result;
    }

    //게시글 삭제
    public int deleteBoard(int board_code){
        String sql = "DELETE calvin_board WHERE board_code = ?";
        int result = jdbcTemplate.update(sql, board_code);
        return 0;
    }

    //전체 게시글 목록(어드민용)
    public List<BoardView> SelectAllBoard(int page, int max_code){
        List<BoardView> result = new ArrayList<>();
//        String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY board_code DESC) AS num,board_code, b.member_code, title, board_type," +//
//                "contents, created_date, member_name FROM calvin_board b, calvin_member m where b.member_code = m.member_code) t WHERE num >= ? AND num <= ?";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code,board_type,b.member_code, title,contents,created_date,member_name " +
                "FROM calvin_board b, calvin_member m, (SELECT @rownum := 0) r WHERE b.member_code = m.member_code ORDER BY board_code DESC) t LIMIT ?, ?";//서버
        result = jdbcTemplate.query(sql, new Object[]{(page-1)*20,20}, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView= new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                boardView.setMember_code(rs.getInt("member_code"));
                String title = rs.getString("title");
                if(title.length() > 25){
                    title = title.substring(0,26)+"...";
                }
                boardView.setTitle(title);
                boardView.setContents(rs.getString("contents"));
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                boardView.setName(rs.getString("member_name"));
                boardView.setBoard_type(rs.getString("board_type"));
                return boardView;
            }
        });
        return result;
    }

    //전체 게시글 목록(일반용)
    public List<BoardView> SelectAllBoard(String board_type, int page, int max_code){
        List<BoardView> result = new ArrayList<>();
//        String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY board_code DESC) AS num,board_code, b.member_code, title, " +//
//                "contents, board_type,created_date, member_name FROM calvin_board b, calvin_member m where b.member_code = m.member_code) t WHERE num >= ? AND num <= ? AND board_type = ?";
//        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code,b.member_code, title,contents,created_date,member_name FROM calvin_board b, calvin_member m, (SELECT @rownum := 0) r WHERE b.member_code = m.member_code AND board_type = ? ORDER BY board_code DESC) t LIMIT ?, ? ";//서버
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code,b.member_code, title,contents,b.created_date,member_name, f.save_name FROM calvin_board b, calvin_member m,calvin_file f, (SELECT @rownum := 0) r WHERE b.member_code = m.member_code AND b.file_code1 = f.file_code AND board_type = ? ORDER BY board_code DESC) t LIMIT ?, ? ";//서버
        result = jdbcTemplate.query(sql, new Object[]{board_type,(page-1)*20,20}, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView= new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                boardView.setMember_code(rs.getInt("member_code"));
                String title = rs.getString("title");
                if(title.length() > 25){
                    title = title.substring(0,26)+"...";
                }
                boardView.setTitle(title);
                boardView.setContents(rs.getString("contents"));
                String thumbnail = rs.getString("save_name");
                if(thumbnail.equals("-1")){
                    boardView.setBoard_thumbnail("/imgPath/white.png");
                }else{
                    boardView.setBoard_thumbnail("/imgPath/"+thumbnail);
                }
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                boardView.setName(rs.getString("member_name"));
                return boardView;
            }
        });
        return result;
    }

    //메인 페이지 표시용 공지사항
    public List<BoardView> SelectNotice6(){
        List<BoardView> result = new ArrayList<>();
        String sql = "SELECT board_code, title, created_date FROM calvin_board WHERE board_type = '공지사항' ORDER BY  board_code DESC LIMIT 6";

        result = jdbcTemplate.query(sql, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView= new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                String title = rs.getString("title");
                if(title.length()>=35){
                    title = title.substring(0,36) + "...";
                }
                boardView.setTitle(title);
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                return boardView;
            }
        });
        return  result;
    }
    //페이징을 위한 게시글 수
    public int paging(){
        int result;
        String sql = "SELECT COUNT(*) FROM calvin_board";
        result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }
    public int paging(String board_type){
        int result;
        String sql = "SELECT COUNT(*) FROM calvin_board WHERE board_type = ?";
        result = jdbcTemplate.queryForObject(sql,new Object[]{board_type},Integer.class);
        return result;
    }
    public int paging(int search_type, String search_word){//search_type 1 = 제목 2 = 내용
        int result;
        search_word = ".*"+search_word+".*";
        String sql;
        if(search_type == 1){
            sql = "SELECT COUNT(*) FROM calvin_board WHERE title REGEXP ?";
        }else{
            sql = "SELECT COUNT(*) FROM calvin_board WHERE contents REGEXP ?";
        }
        result = jdbcTemplate.queryForObject(sql,new Object[]{search_word},Integer.class);
        return result;
    }
    public int paging(String board_type, int search_type, String search_word){//search_type 1 = 제목 2 = 내용
        int result;
        search_word = ".*"+search_word+".*";
        String sql;
        if(search_type == 1){
            sql = "SELECT COUNT(*) FROM calvin_board WHERE title REGEXP ? AND board_type = ?";
        }else{
            sql = "SELECT COUNT(*) FROM calvin_board WHERE contents REGEXP ? AND board_type = ?";
        }
        result = jdbcTemplate.queryForObject(sql,new Object[]{search_word,board_type},Integer.class);
        return result;
    }

    //제목으로 검색(어드민용)
    public List<BoardView> SelectByTitle(String word, int page, int max_code){
        //String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY board_code DESC) AS num, board_code, m.member_code, title, contents, created_date, m.member_name FROM calvin_board b, calvin_member m WHERE b.member_code = m.member_code AND title REGEXP ?) t WHERE num >= ? AND num <= ?";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code, m.member_code, title, contents, b.created_date, m.member_name,f.save_name FROM calvin_board b, calvin_member m, calvin_file f, (SELECT @rownum := 0) r WHERE b.member_code = m.member_code AND b.file_code1 = f.file_code AND title REGEXP ?) t LIMIT ?, ?";
        //서버
        List<BoardView> result = jdbcTemplate.query(sql, new Object[]{".*"+word+".*", (page-1)*20,20}, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView = new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                boardView.setMember_code(rs.getInt("member_code"));
                String title = rs.getString("title");
                if(title.length() > 25){
                    title = title.substring(0,26)+"...";
                }
                boardView.setTitle(title);
                boardView.setContents(rs.getString("contents"));
                boardView.setName(rs.getString("member_name"));
                String thumbnail = rs.getString("save_name");
                if(thumbnail.equals("-1")){
                    boardView.setBoard_thumbnail("/imgPath/white.png");
                }else{
                    boardView.setBoard_thumbnail("/imgPath/"+thumbnail);
                }
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                return boardView;
            }
        });
        return result;
    }
    //제목으로 검색(일반용)
    public List<BoardView> SelectByTitle(String board_type, String word, int page, int max_code){
        //String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY board_code DESC) AS num, board_code, m.member_code, title, contents, created_date, m.member_name FROM calvin_board b, calvin_member m WHERE b.member_code = m.member_code AND title REGEXP ?) t WHERE num >= ? AND num <= ? AND board_type = ?";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code, m.member_code, title, contents, b.created_date, m.member_name, f.save_name FROM calvin_board b, calvin_member m, calvin_file f,(SELECT @rownum := 0) r WHERE b.member_code = m.member_code AND f.file_code = b.file_code1 AND title REGEXP ?  AND board_type = ?) t LIMIT ?, ?";
        //서버
        List<BoardView> result = jdbcTemplate.query(sql, new Object[]{".*"+word+".*",board_type, (page-1)*20,20}, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView = new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                boardView.setMember_code(rs.getInt("member_code"));
                String title = rs.getString("title");
                if(title.length() > 25){
                    title = title.substring(0,26)+"...";
                }
                boardView.setTitle(title);
                boardView.setContents(rs.getString("contents"));
                boardView.setName(rs.getString("member_name"));
                String thumbnail = rs.getString("save_name");
                if(thumbnail.equals("-1")){
                    boardView.setBoard_thumbnail("/imgPath/white.png");
                }else{
                    boardView.setBoard_thumbnail("/imgPath/"+thumbnail);
                }
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                return boardView;
            }
        });
        return result;
    }
    //내용으로 검색(어드민용)
    public List<BoardView> SelectByContents(String word, int page, int max_code){
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code, m.member_code, title, contents, b.created_date, m.member_name,f.save_name FROM calvin_board b, calvin_member m, calvin_file f, (SELECT @rownum := 0) r WHERE b.member_code = m.member_code AND b.file_code1 = f.file_code AND contents REGEXP ?) t LIMIT ?, ?";
        //서버
        //String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY BOARD_CODE DESC) AS NUM, BOARD_CODE, M.MEMBER_CODE, TITLE, CONTENTS,B.CREATED_DATE, M.MEMBER_NAME, F.SAVE_NAME FROM CALVIN_BOARD B, CALVIN_MEMBER M, CALVIN_FILE F, (SELECT ROW_NUMBER() OVER (ORDER BY score DESC)) R WHERE B.MEMBER_CODE = M.MEMBER_CODE AND B.FILE_CODE1 = F.FILE_CODE AND CONTENTS REGEXP ?) T LIMIT ?,?";
        List<BoardView> result = jdbcTemplate.query(sql, new Object[]{".*"+word+".*", (page-1)*20,20}, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView = new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                boardView.setMember_code(rs.getInt("member_code"));
                String title = rs.getString("title");
                if(title.length() > 25){
                    title = title.substring(0,26)+"...";
                }
                boardView.setTitle(title);
                boardView.setContents(rs.getString("contents"));
                boardView.setName(rs.getString("member_name"));
                String thumbnail = rs.getString("save_name");
                if(thumbnail.equals("-1")){
                    boardView.setBoard_thumbnail("/imgPath/white.png");
                }else{
                    boardView.setBoard_thumbnail("/imgPath/"+thumbnail);
                }
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                return boardView;
            }
        });
        return result;
    }
    //내용으로 검색(일반용)
    public List<BoardView> SelectByContents(String board_type, String word, int page, int max_code){
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, board_code, m.member_code, title, contents, created_date, m.member_name,f.save_name FROM calvin_board b, calvin_member m, (SELECT @rownum := 0) r WHERE b.member_code = m.member_code AND contents REGEXP ?  AND board_type = ?) t LIMIT ?, ?";
        //서버
        List<BoardView> result = jdbcTemplate.query(sql, new Object[]{".*"+word+".*",board_type, (page-1)*20,20}, new RowMapper<BoardView>() {
            @Override
            public BoardView mapRow(ResultSet rs, int rowNum) throws SQLException {
                BoardView boardView = new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                boardView.setBoard_code(rs.getInt("board_code"));
                boardView.setMember_code(rs.getInt("member_code"));
                String title = rs.getString("title");
                if(title.length() > 25){
                    title = title.substring(0,26)+"...";
                }
                boardView.setTitle(title);
                boardView.setContents(rs.getString("contents"));
                boardView.setName(rs.getString("member_name"));
                String thumbnail = rs.getString("save_name");
                if(thumbnail.equals("-1")){
                    boardView.setBoard_thumbnail("/imgPath/white.png");
                }else{
                    boardView.setBoard_thumbnail("/imgPath/"+thumbnail);
                }
                boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                return boardView;
            }
        });
        return result;
    }

    //게시글 확인
    public BoardView SelectBoardDetail(int board_id){
        BoardView result;
        String sql = "SELECT  board_code, b.member_code, board_type, title, contents, created_date, file_code1,file_code2,file_code3,file_code4,file_code5, member_name" +
                " FROM calvin_board b, calvin_member m WHERE b.member_code = m.member_code AND" +
                " board_code = ?";
        result = jdbcTemplate.query(sql, new Object[]{board_id}, new ResultSetExtractor<BoardView>() {
            @Override
            public BoardView extractData(ResultSet rs) throws SQLException, DataAccessException {
                BoardView boardView = new BoardView();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(rs.next()){
                    boardView.setBoard_type(rs.getString("board_type"));
                    boardView.setBoard_code(rs.getInt("board_code"));
                    boardView.setMember_code(rs.getInt("member_code"));
                    boardView.setTitle(rs.getString("title"));
                    boardView.setContents(rs.getString("contents"));
                    boardView.setCreated_date(sdf.format(rs.getTimestamp("created_date")));
                    boardView.setFile_code1(rs.getInt("file_code1"));
                    boardView.setFile_code2(rs.getInt("file_code2"));
                    boardView.setFile_code3(rs.getInt("file_code3"));
                    boardView.setFile_code4(rs.getInt("file_code4"));
                    boardView.setFile_code5(rs.getInt("file_code5"));
                    boardView.setName(rs.getString("member_name"));
                }
                return boardView;
            }
        });
        return result;
    }

    //첨부파일 저장
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int SaveFile(MultipartFile file) throws Exception{
//        String path = "F:\\CalvinUploadFiles\\";//로컬
        String path = "/iceadmin/CalvinUploadFile/"; //서버
        UUID uuid = UUID.randomUUID();
        String fileName = uuid+"_"+file.getOriginalFilename();
        File saveFile = new File(path,fileName);
        file.transferTo(saveFile);
        String sql = "INSERT INTO calvin_file (original_name, save_name, size, created_date) VALUES (?,?,?,SYSDATE())";
        //String sql = "INSERT INTO calvin_file (original_name, save_name, size, created_date) VALUES (?,?,?,current_date())";
        int insertResult = jdbcTemplate.update(sql,file.getOriginalFilename(), fileName, file.getSize());
        int file_code = -1;
        if(insertResult == 1){
            sql = "SELECT file_code FROM calvin_file WHERE save_name = ?";
            file_code = jdbcTemplate.queryForObject(sql,new Object[]{fileName}, Integer.class);
        }
        return file_code;
    }


    //게시글 삭제
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int DeleteBoard(int board_code){
        int result;
        String sql = "SELECT file_code, save_name FROM calvin_file f, calvin_board b WHERE b.board_code = ? AND f.file_code != -1 AND (f.file_code = b.file_code1 OR f.file_code = b.file_code2 OR f.file_code = b.file_code3 OR f.file_code = b.file_code4 OR f.file_code = b.file_code5);";
        List<Calvin_file> list = jdbcTemplate.query(sql, new Object[]{board_code}, new RowMapper<Calvin_file>() {
            @Override
            public Calvin_file mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_file file = new Calvin_file();
                file.setSave_name(rs.getString("save_name"));
                file.setFile_code(rs.getInt("file_code"));
                return file;
            }
        });
        String path = "F:\\CalvinUploadFiles\\";//로컬
//        String path = "/iceadmin/CalvinUploadFile/"; //서버
        for(Calvin_file calvinFile : list){
            path = path+calvinFile.getSave_name();
            calvinFileService.DeleteFile(path,calvinFile.getFile_code());
        }

        sql = "DELETE FROM calvin_board WHERE board_code = ?";
        result = jdbcTemplate.update(sql, board_code);
        return result;
    }
}
