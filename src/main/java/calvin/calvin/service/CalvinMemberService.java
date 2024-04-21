package calvin.calvin.service;

import calvin.calvin.domain.BoardView;
import calvin.calvin.domain.Calvin_Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CalvinMemberService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Calvin_Member> SelectAllMember(int page, int count){
        String sql = " SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, member_code, id, member_type, member_name, birth, phone_number FROM calvin_member, (SELECT @rownum := 0) r ) t LIMIT ?, ?";
        List<Calvin_Member> result = jdbcTemplate.query(sql,new Object[]{(page-1)*20,20}, new RowMapper<Calvin_Member>() {
            @Override
            public Calvin_Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_Member calvin_member = new Calvin_Member();
                calvin_member.setMember_code(rs.getLong("member_code"));
                calvin_member.setId(rs.getString("id"));
                String name = rs.getString("member_name");
                if(name.length() == 2){
                    name = name.substring(0,1)+"*";
                }else{
                    name = name.substring(0,1)+"*"+name.substring(2);
                }
                calvin_member.setName(name);
                calvin_member.setMember_type(rs.getString("member_type"));
                String pnum = rs.getString("phone_number");
                if(pnum.length() == 11){
                    pnum = pnum.substring(0,3)+"-"+pnum.substring(3,7)+"-"+"****";
                }else{
                    pnum = pnum.substring(0,3)+"-"+pnum.substring(3,6)+"-"+"****";
                }
                calvin_member.setPhone_number(rs.getString("phone_number"));
                return calvin_member;
            }
        });
        return result;
    }

    public int JoinMember(String id, String pwd, String name, String birth, String phone_number, String address){
        String sql = "INSERT INTO calvin_member(id, pwd, join_date, member_type, member_name, birth, phone_number, address) VALUES(?,?,SYSDATE(),?,?,?,?,?)";
        int result = jdbcTemplate.update(sql, id, pwd, "member",name,birth, phone_number,address);
        return result;
    }
    public int DeleteMember(int member_code){
        String sql = "DELETE FROM calvin_member WHERE member_code = ?";
        return jdbcTemplate.update(sql,member_code);
    }
    public boolean MemberInfoUpdatePwd(String pwd, String id){
        String sql = "UPDATE calvin_member SET pwd = ? WHERE id =?";
        boolean result = false;
        if(jdbcTemplate.update(sql,new Object[]{pwd, id})==1){
            result = true;
        }
        return result;
    }
    public boolean MemberInfoUpdatePn(String phone_number, String id){
        String sql = "UPDATE calvin_member SET phone_number = ? WHERE id =?";
        boolean result = false;
        if(jdbcTemplate.update(sql,new Object[]{phone_number, id})==1){
            result = true;
        }
        return result;
    }
    public boolean MemberInfoUpdateAddress(String address, String id){
        String sql = "UPDATE calvin_member SET address = ? WHERE id =?";
        boolean result = false;
        if(jdbcTemplate.update(sql,new Object[]{address, id})==1){
            result = true;
        }
        return result;
    }

    public boolean login(String id, String pwd){
        boolean result = false;
        String sql = "SELECT COUNT(*) FROM calvin_member WHERE id = ? AND pwd = ? ";
        int count = jdbcTemplate.queryForObject(sql,new Object[]{id, pwd}, Integer.class);
        if(count ==1){
            result = true;
        }
        return result;
    }

    public int IdDuplicateCheck(String id){
        String sql = "SELECT COUNT(*) FROM calvin_member WHERE id = ?";
        int rowCount = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return rowCount;
    }

    public String GetMemberType(String id){
        String sql = "SELECT member_type FROM calvin_member WHERE id = ?";
        String result = jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
        return result;
    }

    public Calvin_Member MyInfo(String id, int type){
        String sql = "SELECT * FROM calvin_member WHERE id = ?";
        List<Calvin_Member> list = jdbcTemplate.query(sql, new Object[]{id}, new RowMapper<Calvin_Member>() {
            @Override
            public Calvin_Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_Member cm = new Calvin_Member();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                cm.setJoin_date(sdf.format(rs.getTimestamp("join_date")));
                cm.setId(rs.getString("id"));
                cm.setName(rs.getString("member_name"));
                cm.setBirth(rs.getString("birth"));
                cm.setAddress(rs.getString("address"));
                String pnum = rs.getString("phone_number");
                if(type == 1){
                    if(pnum.length() == 11){
                        pnum = pnum.substring(0,3)+"-"+pnum.substring(3,7)+"-"+"****";
                    }else{
                        pnum = pnum.substring(0,3)+"-"+pnum.substring(3,6)+"-"+"****";
                    }
                }
                cm.setPhone_number(pnum);
                return cm;
            }
        });
        Calvin_Member result = DataAccessUtils.singleResult(list);
        return result;
    }
    public Calvin_Member MemberInfo(int member_code, int type){
        String sql = "SELECT * FROM calvin_member WHERE member_code = ?";
        List<Calvin_Member> list = jdbcTemplate.query(sql, new Object[]{member_code}, new RowMapper<Calvin_Member>() {
            @Override
            public Calvin_Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_Member cm = new Calvin_Member();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                cm.setMember_code(rs.getLong("member_code"));
                cm.setJoin_date(sdf.format(rs.getTimestamp("join_date")));
                cm.setId(rs.getString("id"));
                cm.setName(rs.getString("member_name"));
                cm.setBirth(rs.getString("birth"));
                cm.setAddress(rs.getString("address"));
                String pnum = rs.getString("phone_number");
                if(type == 1){
                    if(pnum.length() == 11){
                        pnum = pnum.substring(0,3)+"-"+pnum.substring(3,7)+"-"+"****";
                    }else{
                        pnum = pnum.substring(0,3)+"-"+pnum.substring(3,6)+"-"+"****";
                    }
                }
                cm.setPhone_number(pnum);
                cm.setMember_type(rs.getString("member_type"));
                return cm;
            }
        });
        Calvin_Member result = DataAccessUtils.singleResult(list);
        return result;
    }
    public int paging(){
        int result;
        String sql = "SELECT COUNT(*) FROM calvin_member";
        result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }
    public int paging(int search_type, String search_word){
        int result;
        search_word = ".*"+search_word+".*";
        String sql = "";
        if(search_type == 1){//아이디
            sql = "SELECT COUNT(*) FROM calvin_member WHERE id REGEXP ?";
        }else if(search_type == 2){//이름
            sql = "SELECT COUNT(*) FROM calvin_member WHERE name REGEXP ?";
        }

        result = jdbcTemplate.queryForObject(sql,new Object[]{search_word},Integer.class);
        return result;
    }

    public List<Calvin_Member> SelectById(String word, int page, int max_code){

        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, member_code, id, member_type, member_name, birth, phone_number FROM calvin_member FROM calvin_member, (SELECT @rownum := 0) r WHERE id REGEXP ?) t LIMIT ?, ?";
        //서버
        List<Calvin_Member> result = jdbcTemplate.query(sql, new Object[]{".*"+word+".*", (page-1)*20,20}, new RowMapper<Calvin_Member>() {
            @Override
            public Calvin_Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_Member calvin_member = new Calvin_Member();
                calvin_member.setMember_code(rs.getLong("member_code"));
                calvin_member.setId(rs.getString("id"));
                calvin_member.setName(rs.getString("member_name"));
                calvin_member.setMember_type(rs.getString("member_type"));
                calvin_member.setPhone_number(rs.getString("phone_number"));
                return calvin_member;
            }
        });
        return result;
    }
    public List<Calvin_Member> SelectByName(String word, int page, int max_code){

        String sql = "SELECT * FROM ( SELECT @rownum := @rownum + 1 AS num, member_code, id, member_type, member_name, birth, phone_number FROM calvin_member FROM calvin_member, (SELECT @rownum := 0) r WHERE name REGEXP ?) t LIMIT ?, ?";
        //서버
        List<Calvin_Member> result = jdbcTemplate.query(sql, new Object[]{".*"+word+".*", (page-1)*20,20}, new RowMapper<Calvin_Member>() {
            @Override
            public Calvin_Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_Member calvin_member = new Calvin_Member();
                calvin_member.setMember_code(rs.getLong("member_code"));
                calvin_member.setId(rs.getString("id"));
                calvin_member.setName(rs.getString("member_name"));
                calvin_member.setMember_type(rs.getString("member_type"));
                calvin_member.setPhone_number(rs.getString("phone_number"));
                return calvin_member;
            }
        });
        return result;
    }

    public int MemberGrant(String member_code, String grant){
        String sql = "UPDATE calvin_member SET member_type = ? WHERE member_code = ?";
        if(grant.equals("m")){
            grant = "member";
        }else if(grant.equals("d")){
            grant = "developer";
        }else if(grant.equals("a")){
            grant = "admin";
        }else if(grant.equals("p")){
            grant = "professor";
        }

        return jdbcTemplate.update(sql,grant, member_code);
    }

    //교/강사 리스트
    public List<Calvin_Member> ProfessorList(){
        String sql = "SELECT member_code, member_name, id FROM calvin_member WHERE member_type = 'professor'";
        List<Calvin_Member> result = jdbcTemplate.query(sql, new RowMapper<Calvin_Member>() {
            @Override
            public Calvin_Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Calvin_Member calvin_member = new Calvin_Member();
                calvin_member.setMember_code(rs.getLong("member_code"));
                calvin_member.setName(rs.getString("member_name"));
                calvin_member.setId(rs.getString("id"));
                return calvin_member;
            }
        });
        return result;
    }

}
