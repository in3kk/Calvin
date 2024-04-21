package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Calvin_file {
    private int file_code;
    private String original_name;
    private String save_name;
    private int size;
    private String delete_yn;
    private String created_date;

    public Calvin_file(int file_code, String original_name, String save_name, int size, String delete_yn, String created_date){
        this.file_code = file_code;
        this.original_name = original_name;
        this.save_name = save_name;
        this.size = size;
        this.delete_yn = delete_yn;
        this.created_date = created_date;
    }
    public Calvin_file(){}
}
