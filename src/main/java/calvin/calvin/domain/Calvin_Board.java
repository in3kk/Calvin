package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Calvin_Board {
    private Long board_code;
    private Long member_code;
    private String title;
    private String contents;
    private String created_date;
    private Long file_code;

    public Calvin_Board(Long board_code, Long member_code, Long file_code, String title, String contents, String created_date){
        this.board_code = board_code;
        this.member_code = member_code;
        this.title = title;
        this.contents = contents;
        this.created_date = created_date;
        this.file_code = file_code;
    }

    public Calvin_Board(){}
}
