package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JoinMember {
    private String id;
    private String id2;
    private String pwd;
    private String name;
    private String birth;
    private String phone_number;
    private String address;
    private String address2;

    public JoinMember(String id, String id2, String pwd, String name, String birth, String phone_number, String address, String address2){
        this.id = id;
        this.id2 = id2;
        this.pwd = pwd;
        this.name = name;
        this.birth = birth;
        this.phone_number = phone_number;
        this.address = address;
        this.address2 = address2;
    }
    public JoinMember(){}
}
