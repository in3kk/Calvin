package calvin.calvin.service;

import calvin.calvin.domain.Calvin_Member;
import calvin.calvin.domain.Calvin_subject;
import calvin.calvin.domain.MyPageSubjectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CalvinSubjectService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private CalvinBoardService calvinBoardService;


    public List<Calvin_subject> SubjectList(String subject_type){
        String sql = "SELECT subject_type,period,subject_code, subject_field, lecture_time, fee, subject_name, subject_stat FROM calvin_subject WHERE subject_type = ? ORDER BY subject_code DESC";
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{subject_type},new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setLecture_time(rs.getString("lecture_time"));
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_stat(rs.getInt("subject_stat"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                calvin_subject.setPeriod(rs.getString("period"));
                return calvin_subject;
            }
        });
        return result;
    }
    public List<Calvin_subject> SubjectList(String field, String subject_type){
        String sql = "SELECT period,subject_code, subject_field, lecture_time, fee, subject_name, subject_stat FROM calvin_subject WHERE subject_field REGEXP ? AND subject_type = ? ORDER BY subject_code DESC";
        field = ".*"+field+".*";
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{field, subject_type}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setLecture_time(rs.getString("lecture_time"));
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_stat(rs.getInt("subject_stat"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                calvin_subject.setPeriod(rs.getString("period"));
                return calvin_subject;
            }
        });
        return result;
    }

    public List<Calvin_subject> SubjcetList(String type, String field, int ipt) {
        String sql = "SELECT period,subject_code, subject_field, lecture_time, fee, subject_name, subject_stat FROM calvin_subject WHERE subject_field REGEXP ? AND subject_type = ? AND subject_name REGEXP ? ORDER BY subject_code DESC";


        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{".*"+field+".*",type,".*1:"+ipt+".*"}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setLecture_time(rs.getString("lecture_time"));
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_stat(rs.getInt("subject_stat"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                calvin_subject.setPeriod(rs.getString("period"));
                return calvin_subject;
            }
        });
        return result;
    }

    //새 강의 개설 첨부파일 o
    @Transactional
    public int ModifySubject(String subject_name, String subject_field, String subject_type, int personnel, String lecture_time, int period,
                          int member_code, int fee, MultipartFile file,int subject_code){

        int file_code = -1;
        int result = 0;
        try {
            file_code =  calvinBoardService.SaveFile(file);
            String sql = "UPDATE calvin_subject SET fee = ?,subject_name = ?, file_code = ?,subject_field = ?,lecture_time = ?,period = ?,personnel = ?,subject_type = ?,member_code = ? WHERE subject_code = ?";
            result = jdbcTemplate.update(sql, fee, subject_name, file_code, subject_field, lecture_time, period,personnel, subject_type, member_code, subject_code);
        }catch (Exception e){
            System.out.println("에러 : "+e);
        }
        return result;
    }
    @Transactional
    public int InsertSubject(String subject_name, String subject_field, String subject_type, int personnel, String lecture_time, int period,
                             int member_code, int fee, MultipartFile file){

        int file_code = -1;
        int result = 0;
        if (subject_name.isEmpty() || subject_field.isEmpty() || subject_type.isEmpty()) {
            return result;
        }
        try {
            file_code =  calvinBoardService.SaveFile(file);
            String sql = "INSERT INTO calvin_subject(fee,subject_name, write_date, subject_stat, file_code, subject_field, lecture_time, " +
                    "period, personnel, subject_type,member_code) VALUES(?,?,SYSDATE(),1,?,?,?,?,?,?,?)";
            result = jdbcTemplate.update(sql, fee, subject_name, file_code, subject_field, lecture_time, period,personnel, subject_type, member_code);
        }catch (Exception e){
            System.out.println("에러 : "+e);
        }
        return result;
    }
    //새 강의 개설 첨부파일 x
    @Transactional
    public int ModifySubject(String subject_name, String subject_field, String subject_type, int personnel, String lecture_time, int period,
                          int member_code, int fee, int subject_code){
        String sql = "UPDATE calvin_subject SET fee = ?,subject_name = ?, subject_field = ?,lecture_time = ?,period = ?,personnel = ?,subject_type = ?,member_code = ? WHERE subject_code = ?";
        int result = jdbcTemplate.update(sql, fee,subject_name, subject_field,lecture_time,period,personnel,subject_type,member_code,subject_code);
        return result;
    }
    @Transactional
    public int InsertSubject(String subject_name, String subject_field, String subject_type, int personnel, String lecture_time, int period,
                             int member_code, int fee){
        String sql = "INSERT INTO calvin_subject(fee, subject_name, write_date, subject_stat, file_code, subject_field, lecture_time, period, personnel, subject_type, member_code) VALUES(?,?,SYSDATE(),1,-1,?,?,?,?,?,?)";
        int result = jdbcTemplate.update(sql, fee, subject_name, subject_field, lecture_time, period, personnel, subject_type, member_code);
        return result;
    }

    public int DeleteSubject(int subject_code){
        String sql = "DELETE FROM calvin_subject WHERE subject_code = ?";
        int result = jdbcTemplate.update(sql,subject_code);
        return result;
    }
    public List<Calvin_subject> SubjectList(String field, String subject_type,String name){
        String sql = "SELECT period,subject_code, subject_field, lecture_time, fee, subject_name, subject_stat FROM calvin_subject WHERE subject_field REGEXP ? AND subject_type = ? AND REPLACE(subject_name,' ','') LIKE ? ORDER BY subject_code DESC";
        field =".*"+field+".*";
        name = name.replaceAll(" ","");
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{field, subject_type,"%"+name+"%"}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setLecture_time(rs.getString("lecture_time"));
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_stat(rs.getInt("subject_stat"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                calvin_subject.setPeriod(rs.getString("period"));
                return calvin_subject;
            }
        });
        return result;
    }

    public Calvin_subject SubjectApply(int subject_code){
        String sql = "SELECT subject_type,personnel, period, lecture_time, member_name,m.member_code,subject_code, fee, subject_name, write_date, subject_stat, file_code, subject_field FROM calvin_subject s, calvin_member m WHERE s.member_code = m.member_code AND subject_code = ?";
        Calvin_subject result = jdbcTemplate.query(sql, new Object[]{subject_code}, new ResultSetExtractor<Calvin_subject>() {
            @Override
            public Calvin_subject extractData(ResultSet rs) throws SQLException, DataAccessException {
                Calvin_subject calvin_subject = new Calvin_subject();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(rs.next()){
                    calvin_subject.setSubject_code(rs.getInt("subject_code"));
                    calvin_subject.setMember_code(rs.getInt("member_code"));
                    calvin_subject.setFee(rs.getInt("fee"));
//                    calvin_subject.setwrite_date(sdf.format(rs.getTimestamp("write_date")));
                    calvin_subject.setSubject_stat(rs.getInt("subject_stat"));
                    calvin_subject.setMember_name(rs.getString("member_name"));
                    calvin_subject.setFile_code(rs.getInt("file_code"));
                    calvin_subject.setSubject_field(rs.getString("subject_field"));
                    calvin_subject.setSubject_name(rs.getString("subject_name"));
                    calvin_subject.setLecture_time(rs.getString("lecture_time"));
                    calvin_subject.setPeriod(rs.getString("period"));
                    calvin_subject.setPersonnel(rs.getInt("personnel"));
                    calvin_subject.setSubject_type(rs.getString("subject_type"));
                }
                return calvin_subject;
            }
        });
        return result;
    }

    public List<MyPageSubjectView> My_subject(String member_id){
        String sql = "SELECT s.subject_code, s.subject_name, s.subject_stat, s.subject_field, s.fee, m.pay_stat FROM calvin_subject s, member_subject m WHERE s.subject_code = m.subject_code AND m.member_code = ? ORDER BY s.subject_code DESC";
        List<MyPageSubjectView> result = jdbcTemplate.query(sql, new Object[]{member_id}, new RowMapper<MyPageSubjectView>() {
            @Override
            public MyPageSubjectView mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyPageSubjectView myPageSubjectView = new MyPageSubjectView();
                myPageSubjectView.setFee(rs.getInt("fee"));
                myPageSubjectView.setSubject_stat(rs.getInt("subject_stat"));
                myPageSubjectView.setSubject_field(rs.getString("field"));
                myPageSubjectView.setSubject_code(rs.getInt("subject_code"));
                myPageSubjectView.setPay_stat(rs.getString("pay_stat"));
                myPageSubjectView.setSubject_name(rs.getString("subject_name"));
                return myPageSubjectView;
            }
        });
        return result;
    }

    //모든 신청 검색
    public List<MyPageSubjectView> SelectAllApply(int page){//페이징 추가
        String sql = "SELECT ms.apply_code, m.member_code, s.subject_code, ms.pay_stat, m.member_name, m.id, s.subject_name, s.subject_field FROM calvin_member m, calvin_subject s, member_subject ms WHERE m.member_code = ms.member_code AND s.subject_code = ms.subject_code";
        List<MyPageSubjectView> result = jdbcTemplate.query(sql, new RowMapper<MyPageSubjectView>() {
            @Override
            public MyPageSubjectView mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyPageSubjectView myPageSubjectView = new MyPageSubjectView();
                myPageSubjectView.setId(rs.getString("id"));
                myPageSubjectView.setSubject_field(rs.getString("subject_field"));
                myPageSubjectView.setApply_code(rs.getInt("apply_code"));
                myPageSubjectView.setMember_code(rs.getInt("member_code"));
                myPageSubjectView.setSubject_code(rs.getInt("subject_code"));
                myPageSubjectView.setPay_stat(rs.getString("pay_stat"));
                myPageSubjectView.setName(rs.getString("member_name"));
                myPageSubjectView.setSubject_name(rs.getString("subject_name"));
                return myPageSubjectView;
            }
        });
        return result;
    }

    //모든 신청 아이디로 검색
    public List<MyPageSubjectView> SelectApplyById(int page, String search_word){
        search_word = ".*"+search_word+".*";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num,  ms.apply_code, m.member_code, s.subject_code, ms.pay_stat, m.member_name, m.id, s.subject_name, s.subject_field FROM calvin_member m, calvin_subject s, member_subject ms, (SELECT @rownum := 0) r WHERE m.id REGEXP ? AND  m.member_code = ms.member_code AND s.subject_code = ms.subject_code) t LIMIT ?, ?";
        List<MyPageSubjectView> result = jdbcTemplate.query(sql, new Object[]{search_word, (page - 1) * 20, 20}, new RowMapper<MyPageSubjectView>() {
            @Override
            public MyPageSubjectView mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyPageSubjectView myPageSubjectView = new MyPageSubjectView();
                myPageSubjectView.setId(rs.getString("id"));
                myPageSubjectView.setSubject_field(rs.getString("subject_field"));
                myPageSubjectView.setApply_code(rs.getInt("apply_code"));
                myPageSubjectView.setMember_code(rs.getInt("member_code"));
                myPageSubjectView.setSubject_code(rs.getInt("subject_code"));
                myPageSubjectView.setPay_stat(rs.getString("pay_stat"));
                myPageSubjectView.setName(rs.getString("member_name"));
                myPageSubjectView.setSubject_name(rs.getString("subject_name"));
                return null;
            }
        });
        return result;
    }
    //모든 신청 강의명으로 검색
    public List<MyPageSubjectView> SelectApplyBySubjectName(int page, String search_word){
        search_word = ".*"+search_word+".*";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num,  ms.apply_code, m.member_code, s.subject_code, ms.pay_stat, m.member_name, m.id, s.subject_name, s.subject_field FROM calvin_member m, calvin_subject s, member_subject ms, (SELECT @rownum := 0) r WHERE m.subject_name REGEXP ? AND  m.member_code = ms.member_code AND s.subject_code = ms.subject_code) t LIMIT ?, ?";
        List<MyPageSubjectView> result = jdbcTemplate.query(sql, new Object[]{search_word, (page - 1) * 20, 20}, new RowMapper<MyPageSubjectView>() {
            @Override
            public MyPageSubjectView mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyPageSubjectView myPageSubjectView = new MyPageSubjectView();
                myPageSubjectView.setId(rs.getString("id"));
                myPageSubjectView.setSubject_field(rs.getString("subject_field"));
                myPageSubjectView.setApply_code(rs.getInt("apply_code"));
                myPageSubjectView.setMember_code(rs.getInt("member_code"));
                myPageSubjectView.setSubject_code(rs.getInt("subject_code"));
                myPageSubjectView.setPay_stat(rs.getString("pay_stat"));
                myPageSubjectView.setName(rs.getString("member_name"));
                myPageSubjectView.setSubject_name(rs.getString("subject_name"));
                return null;
            }
        });
        return result;
    }

    @Transactional
    public int ApplyManage(List<Integer> apply_list){
        String sql = "DELETE FROM member_subject WHERE apply_code = ?";
        int result = 0;
        for(int apply_code : apply_list){
            int delete_result = jdbcTemplate.update(sql,apply_code);
            if(delete_result == 1){
                result += 1;
            }
        }
        return result;
    }

    @Transactional// 1 : 납부완료, 2 : 납부취소, 3 : 환불
    public int PayManage(List<Integer> apply_list, int type){
        String sql = "UPDATE member_subject SET pay_stat = '";
        switch (type){
            case 1:
                sql += "y' WHERE apply_code IN (:apply)";//yes
                break;
            case 2:
                sql += "n' WHERE apply_code IN (:apply)";//no
                break;
            case 3:
                sql += "r' WHERE apply_code IN (:apply) AND pay_stat != 'y'";//refund
                break;
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("apply",apply_list);
        int result = namedParameterJdbcTemplate.update(sql, parameters);
        return result;
    }
    //신청 관리 페이징
    public int admin_paging_apply(){
        int result;
        String sql = "SELECT COUNT(*) FROM calvin_subject";
        result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    public int admin_paging_apply(int search_type, String search_word){
        int result = 0;
        String sql = "";
        search_word = ".*"+search_word+".*";
        if(search_type == 1){//강의명
            sql = "SELECT COUNT(*) FROM member_subject ms, calvin_subject s WHERE ms.subject_code = s.subject_code AND subject_name REGEXP ?";
        }else if(search_type == 2){//아이디
            sql = "SELECT COUNT(*) FROM member_subject ms, calvin_member m WHERE m.member_code = ms.member_code AND id REGEXP ?";
        }
        result = jdbcTemplate.queryForObject(sql,new Object[]{search_word},Integer.class);
        return result;
    }

    //개설 강의 관리 페이징
    public int admin_paging(){
        int result;
        String sql = "SELECT COUNT(*) FROM calvin_subject";
        result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }
    public int admin_paging(int search_type, String search_word){
        int result;
        search_word = ".*"+search_word+".*";
        String sql="";
        if(search_type == 1){//강의명(subject_name)으로 검색
            sql = "SELECT COUNT(*) FROM calvin_subject WHERE subject_name REGEXP ?";
        }else if(search_type == 2){//과정명(subject_field)로 검색
            sql = "SELECT COUNT(*) FROM calvin_subject WHERE subject_field REGEXP ?";
        }else if(search_type == 3){//강의 분류(subject_type)으로 검색
            sql = "SELECT COUNT(*) FROM calvin_subject WHERE subject_type REGEXP ?";
        }
        result = jdbcTemplate.queryForObject(sql,new Object[]{search_word},Integer.class);
        return result;
    }

    //모든 개설 강의 검색
    public List<Calvin_subject> SelectSubject_admin(int page){
        String sql = " SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, subject_field, subject_code, fee, subject_name, subject_stat, subject_type FROM calvin_subject, (SELECT @rownum := 0) r ) t LIMIT ?, ?";
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{(page - 1) * 20, 20}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_type(rs.getString("subject_type"));
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setSubject_stat(rs.getInt("subject_stat"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                return calvin_subject;
            }
        });
        return result;
    }

    //모든 개설 강의 강의명으로 검색
    public List<Calvin_subject> SelectSubjectByName_admin(int page, String search_word){
        search_word = ".*"+search_word+".*";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, subject_field,subject_code, fee, subject_name, subject_stat, subject_type FROM calvin_subject, (SELECT @rownum := 0) r WHERE subject_name REGEXP ?) t LIMIT ?, ?";
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{search_word, (page - 1) * 20, 20}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setSubject_stat(rs.getShort("subject_stat"));
                calvin_subject.setSubject_type(rs.getString("subject_type"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));

                return calvin_subject;
            }
        });
        return result;
    }

    //모든 개설 강의 과정명으로 검색
    public List<Calvin_subject> SelectSubjectByField_admin(int page, String search_word){
        search_word = ".*"+search_word+".*";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, subject_field,subject_code, fee, subject_name, subject_stat, subject_type FROM calvin_subject, (SELECT @rownum := 0) r WHERE subject_field REGEXP ?) t LIMIT ?, ?";
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{search_word, (page - 1) * 20, 20}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setSubject_stat(rs.getShort("subject_stat"));
                calvin_subject.setSubject_type(rs.getString("subject_type"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                return calvin_subject;
            }
        });
        return result;
    }

    //모든 개설 강의 분류명으로 검색
    public List<Calvin_subject> SelectSubjectByType_admin(int page, String search_word){
        search_word = ".*"+search_word+".*";
        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, subject_code, fee, subject_field, subject_name, subject_stat, subject_type FROM calvin_subject, (SELECT @rownum := 0) r WHERE subject_type REGEXP ?) t LIMIT ?, ?";
        List<Calvin_subject> result = jdbcTemplate.query(sql, new Object[]{search_word, (page - 1) * 20, 20}, new RowMapper<Calvin_subject>() {
            @Override
            public Calvin_subject mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_subject calvin_subject = new Calvin_subject();
                calvin_subject.setSubject_code(rs.getInt("subject_code"));
                calvin_subject.setFee(rs.getInt("fee"));
                calvin_subject.setSubject_name(rs.getString("subject_name"));
                calvin_subject.setSubject_stat(rs.getShort("subject_stat"));
                calvin_subject.setSubject_type(rs.getString("subject_type"));
                calvin_subject.setSubject_field(rs.getString("subject_field"));
                return calvin_subject;
            }
        });
        return result;
    }

    //신청 여부 확인
    public boolean ApplyWhether(String member_id, int subject_code){
        String sql = "SELECT COUNT(*) FROM member_subject ms, calvin_member m WHERE m.member_code = ms.member_code AND m.id = ? AND subject_code = ?";
        int select_result = jdbcTemplate.queryForObject(sql,new Object[]{member_id,subject_code}, Integer.class);
        boolean result = true;
        if(select_result >= 1){
            result = false;
        }
        return result;
    }

    //수강 신청
    public int ApplyPro(String member_id, int subject_code){
        int result = 0;
        String sql = "SELECT member_code FROM calvin_member WHERE id = ?";
        int member_code = jdbcTemplate.queryForObject(sql,new Object[]{member_id},Integer.class);
        sql = "INSERT INTO member_subject(member_code, subject_code, app_date) VALUES(?,?,SYSDATE())";
        result = jdbcTemplate.update(sql, member_code,subject_code);

        return result;
    }

    //강의 상태 변경
    public int SubjectStatManage(int subject_code, int stat){
        String sql = "UPDATE calvin_subject SET subject_stat = ? WHERE subject_code = ?";
        int result = jdbcTemplate.update(sql,stat, subject_code);
        return result;
    }
}
