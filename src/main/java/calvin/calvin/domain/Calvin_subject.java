package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter @Setter @Component
public class Calvin_subject {
    private int subject_code;
    private int member_code;
    private String member_name;
    private int fee;
    private String begin_date;
    private String end_date;
    private int subject_stat;
    private int file_code;
    private String subject_field;
    private String lecture_time;
    private String period;
    private String subject_name;
    private int personnel;
    private String subject_type;

    public Calvin_subject(int subject_code, int member_code, String member_name, int fee, String begin_date,String end_date, int subject_stat
    ,int file_code, String subject_field, String lecture_time, String period,String subject_name,int personnel, String subject_type){
        this.subject_code = subject_code;
        this.member_code = member_code;
        this.member_name = member_name;
        this.fee = fee;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.subject_stat = subject_stat;
        this.file_code = file_code;
        this.subject_field = subject_field;
        this.lecture_time = lecture_time;
        this.period = period;
        this.subject_name = subject_name;
        this.personnel = personnel;
        this.subject_type = subject_type;
    }

    public Calvin_subject(){}
}
