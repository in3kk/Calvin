package calvin.calvin.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class BoardView {
    private int board_code;
    private int member_code;
    private String title;
    private String contents;
    private String created_date;
    private int file_code1;
    private int file_code2;
    private int file_code3;
    private int file_code4;
    private int file_code5;
    private String name;
    private String board_type;

    public BoardView(int board_code, int member_code, int file_code1, int file_code2,int file_code3,int file_code4,int file_code5,String title, String contents, String created_date, String name, String board_type){
        this.board_code = board_code;
        this.member_code = member_code;
        this.title = title;
        this.contents = contents;
        this.created_date = created_date;
        this.file_code1 = file_code1;
        this.file_code2 = file_code2;
        this.file_code3 = file_code3;
        this.file_code4 = file_code4;
        this.file_code5 = file_code5;
        this.name = name;
        this.board_type = board_type;
    }

    public BoardView(){}
}
