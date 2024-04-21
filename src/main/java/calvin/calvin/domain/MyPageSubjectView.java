package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MyPageSubjectView {
    private int subject_code;
    private String subject_name;
    private int subject_stat;
    private String subject_field;
    private int fee;
    private String name;
    private String id;
    private int member_code;
    private String pay_stat;
    private int apply_code;
    public MyPageSubjectView(int apply_code,String name, String id, int member_code, int subject_code, String subject_name, int subject_stat, String subject_field, int fee, String pay_stat){
        this.subject_code = subject_code;
        this.subject_name = subject_name;
        this.subject_stat = subject_stat;
        this.subject_field = subject_field;
        this.fee = fee;
        this.pay_stat = pay_stat;
        this.name = name;
        this.id = id;
        this.member_code = member_code;
        this.apply_code = apply_code;
    }
    public MyPageSubjectView(){}
}
