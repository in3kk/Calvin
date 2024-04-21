package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Calvin_Member {
    private Long member_code;
    private String id;
    private String pwd;
    private String name;
    private String join_date;
    private String member_type;
    private String birth;
    private String phone_number;
    private String address;

    public Calvin_Member(Long member_code, String id, String pwd, String name, String join_date, String member_type, String birth, String phone_number, String address){
        this.member_code = member_code;
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.join_date = join_date;
        this.member_type = member_type;
        this.birth = birth;
        this.phone_number = phone_number;
        this.address = address;
    }
    public Calvin_Member(){}

}

