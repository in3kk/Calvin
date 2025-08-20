package calvin.calvin.controller;


import calvin.calvin.domain.*;
import calvin.calvin.error.ErrorCode;
import calvin.calvin.service.CalvinBoardService;
import calvin.calvin.service.CalvinFileService;
import calvin.calvin.service.CalvinMemberService;
import calvin.calvin.service.CalvinSubjectService;
import exception.CustomException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class CalvinController {

    @Autowired
    private CalvinMemberService calvinMemberService;
    @Autowired
    private CalvinBoardService calvinBoardService;
    @Autowired
    private CalvinSubjectService calvinSubjectService;
    @Autowired
    private CalvinFileService calvinFileService;


    @GetMapping("/errortest")
    public String errortest(){
        throw new CustomException(ErrorCode.INVALID_PERMISSION);
    }
    //메인 화면
    @GetMapping("/")
    public String home(Model model){
        List<BoardView> notice = calvinBoardService.SelectNotice6();
        model.addAttribute("notice", notice);

        return "index2";
    }

    //회원가입 페이지
    @GetMapping("/member/join")
    public String joinPage(Model model, HttpSession httpSession){
        JoinMember jm = new JoinMember();
        httpSession.removeAttribute("member_id");
        httpSession.removeAttribute("member_type");
        model.addAttribute("member",jm);
        return "member/join";
    }

    //회원가입
    @RequestMapping(value = "/member/join", method = RequestMethod.POST)
    @ResponseBody
    public String memberJoin(JoinMember member, Model model){
        String result = "";
        Pattern id1_pattern = Pattern.compile("[A-Za-z0-9]{4,15}");
        Pattern id2_pattern = Pattern.compile("[a-z]{4,10}.(com|net|ac.kr)");
        Pattern pwd_pattern = Pattern.compile("[a-zA-Z0-9!@#$%^&\\*()_\\+]{10,25}");
        Pattern name_pattern = Pattern.compile("[가-힣A-Za-z]{2,10}");
        Pattern birth_pattern = Pattern.compile("(19[0-9][0-9]|20[0-9]{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])");
        Pattern pnum_pattern = Pattern.compile("0[0-9]{1,2}[0-9]{3,4}[0-9]{4}");
        Pattern address1_pattern = Pattern.compile("[가-힣0-9\\s-]*");
        Pattern address2_pattern = Pattern.compile("[가-힣0-9\\s-]*");
        Matcher m1 = id1_pattern.matcher(member.getId());
        Matcher m2 = id2_pattern.matcher(member.getId2());
        Matcher m3 = pwd_pattern.matcher(member.getPwd());
        Matcher m4 = name_pattern.matcher(member.getName());
        Matcher m5 = birth_pattern.matcher(member.getBirth());
        Matcher m6 = pnum_pattern.matcher(member.getPhone_number());
        Matcher m7 = address1_pattern.matcher(member.getAddress());
        Matcher m8 = address2_pattern.matcher(member.getAddress2());
        int rowCnt = 0;
        if(m1.matches()&&m2.matches()&&m3.matches()&&m4.matches()&&m5.matches()&&m6.matches()&&m7.matches()&&m8.matches()){
            rowCnt = calvinMemberService.JoinMember(member.getId()+"@"+member.getId2(),member.getPwd(),member.getName(),member.getBirth(),member.getPhone_number(),member.getAddress()+" "+member.getAddress2());
        }

        if(rowCnt == 1){
            result = "<script>alert('회원가입이 완료되었습니다.');window.location.href='/member/login'</script>";
//            result = "<script>alert('회원가입이 완료되었습니다.');window.location.href='http://localhost:8080/member/login'</script>";
        }else{
            result = "<script>alert('회원가입에 실패했습니다.');history.back();</script>";
        }
        return result;
    }

    //로그인 페이지
    @GetMapping("/member/login")
    public String loginPage(Model model, HttpSession httpSession){
        Calvin_Member cm = new Calvin_Member();
        model.addAttribute("member", cm);
        httpSession.removeAttribute("member_id");
        httpSession.removeAttribute("member_type");
        return "member/login";
    }

    //로그인
    @PostMapping("/member/login")
    @ResponseBody
    public String login(HttpSession httpSession,
                        @RequestParam(value="id") String id, @RequestParam(value="pwd")String pwd){
        String result;
        boolean check = calvinMemberService.login(id,pwd);
        if(check){
            httpSession.setAttribute("member_id", id);
//            result = "<script>window.location.href='http://localhost:8080/'</script>";//
            result = "<script>window.location.href='/'</script>";//서버
            String member_type = calvinMemberService.GetMemberType(id);
            if(member_type.equals("member")){
                httpSession.setAttribute("member_type", "mb");
            }else if(member_type.equals("developer")){
                httpSession.setAttribute("member_type", "dd");
            }else if(member_type.equals("staff")){
                httpSession.setAttribute("member_type","st");
            }else if(member_type.equals("admin")){
                httpSession.setAttribute("member_type","ai");
            }
        }else{
            result = "<script>alert('회원정보가 일치하지 않습니다.');history.back();</script>";
        }
        return result;
    }

    //로그아웃
    @GetMapping("/member/logout")
    @ResponseBody
    public String Logout(HttpSession httpSession){
        String result;
        httpSession.removeAttribute("member_id");
        httpSession.removeAttribute("member_type");
        return "<script>window.location.href='/'</script>";
//        return "<script>window.location.href='http://calvin.or.kr/'</script>";
    }

    //학점은행제 소개 페이지
    @GetMapping("/menu/subject/ACBsystem")
    public String SystemPage(Model model){
        model.addAttribute("page_type", "2.1");
        return "menu/subject/ACBsystem";
    }


//    @GetMapping("/menu/subject/list") //강의 리스트 페이지
//    public String SubjectList(@RequestParam(value = "field", required = false, defaultValue = "") String field,
//                              @RequestParam(value = "type") String type, Model model){
//        List<Calvin_subject> subject_list;
//        if(field.equals("")){
//            subject_list= calvinSubjectService.SubjectList(type);
//        }else{
//            System.out.println("result"+field);
//            subject_list= calvinSubjectService.SubjectList(field,type);
//        }
//        String result = "menu/subject/subject_list";
//        model.addAttribute("subject_list", subject_list);
//        model.addAttribute("page_type","2.2");
//        return  result;
//    }
    //강의 리스트 페이지 (학점은행제, 일반교양, 자격증/취창업)
    @GetMapping({"/menu/subject/list", "/menu/liberal_arts/list","/menu/certificate/list","/menu/special/list","/menu/language/list","/menu/ministry/list","/menu/center/list"})
    public String SubjectList(@RequestParam(value = "field", required = false, defaultValue = "") String field,
                              @RequestParam(value = "type") String type,
                              @RequestParam(value = "name", required = false, defaultValue = "")String name,Model model){
        List<Calvin_subject> subject_list;
        if(field.equals("")){
            subject_list= calvinSubjectService.SubjectList(type);
        }else if(!name.equals("")){
            subject_list = calvinSubjectService.SubjectList(field,type,name);
            model.addAttribute("subject_name", name);
        }else{
            subject_list= calvinSubjectService.SubjectList(field,type);
        }
        String result="";
        model.addAttribute("subject_list", subject_list);
        if(type.equals("학점은행제")){
            result = "menu/subject/subject_list";
            model.addAttribute("page_type","2.2");
        }else if(type.equals("일반교양")){
            result = "menu/liberal_arts/subject_list";
            model.addAttribute("page_type","3.1");
        }else if(type.equals("자격증/취창업")){
            result = "menu/certificate/subject_list";
            if(field.equals("전문자격증")){
                model.addAttribute("page_type","4.1");
            }else if(field.equals("민간자격증")){
                model.addAttribute("page_type","4.2");
            }else if(field.equals("기술자격증")){
                model.addAttribute("page_type","4.3");
            }else if(field.equals("취창업")){
                model.addAttribute("page_type","4.4");
            }
//            if (field.equals("자격증")) {
//                model.addAttribute("page_type", "4.1");
//            } else if (field.equals("취창업")) {
//                model.addAttribute("page_type", "4.2");
//            }
        }else if(type.equals("특별교육과정")){
            //용인학아카데미, 서현정치경제아카데미, 경기교육아카데미, 사모아카데미, 레이번스축구아카데미, 연예아카데미
            result = "menu/special/subject_list";
            if(field.equals("바이블")){
                model.addAttribute("page_type","5.1");
            }else if(field.equals("용인")){
                model.addAttribute("page_type","5.2");
            }else if(field.equals("골프")){
                model.addAttribute("page_type","5.3");
            }else if(field.equals("교회음향")){
                model.addAttribute("page_type","5.4");
            }else if(field.equals("AI")){
                model.addAttribute("page_type","5.5");
            } else if (field.equals("경기교육")) {
                model.addAttribute("page_type","5.6");
            } else if (field.equals("축구아카데미")) {
                model.addAttribute("page_type","5.7");
            }
        }else if(type.equals("언어")){
            result = "menu/language/subject_list";
            if(field.equals("성경고전어")){
                model.addAttribute("page_type","6.1");
            }else if(field.equals("제2외국어")){
                model.addAttribute("page_type","6.2");
            }else if(field.equals("한국어")){
                model.addAttribute("page_type","6.3");
            }
        } else if (type.equals("교육원")) {
            result = "menu/center/subject_list";
            if (field.equals("요양보호사교육원")) {
                model.addAttribute("page_type", "6.1");
            } else if (field.equals("미래금융교육원")) {
                model.addAttribute("page_type", "6.2");
            } else if (field.equals("미래교육교육원")) {
                model.addAttribute("page_type", "6.3");
            } else if (field.equals("드론전문교육원")) {
                model.addAttribute("page_type", "6.4");
            } else if (field.equals("장례지도사교육원")) {
                model.addAttribute("page_type", "6.5");
            } else if (field.equals("장로권사교육원")) {
                model.addAttribute("page_type", "6.6");
            } else if (field.equals("미래목회연구원")) {
                model.addAttribute("page_type", "6.7");
            } else if (field.equals("ESG경영연구원")) {
                model.addAttribute("page_type", "6.8");
            } else if (field.equals("사모교육원")) {
                model.addAttribute("page_type", "6.9");
            } else if (field.equals("여교역자교육원")) {
                model.addAttribute("page_type", "6.10");
            }
        } else if (type.equals("목회")) {
            result = "menu/ministry/subject_list";
            model.addAttribute("page_type", "7.1");
        }
        return  result;
    }
    //수강신청 페이지
    @GetMapping({"/menu/subject/apply", "/menu/liberal_arts/apply","/menu/certificate/apply","/menu/language/apply"})
    public String ApplyPage(@RequestParam(value = "id", required = false, defaultValue = "-1") int id, Model model){
        Calvin_subject subject = calvinSubjectService.SubjectApply(id);
        Calvin_file calvinFile;
        if(subject.getFile_code() != -1){
            calvinFile = calvinFileService.getFileOriginName(subject.getFile_code());
            model.addAttribute("file", calvinFile);
        }
        model.addAttribute("subject",subject);
        String result = "";
        if(subject.getSubject_type().equals("학점은행제")){
            model.addAttribute("page_type","2.2");
            result = "menu/subject/apply";
        }else if(subject.getSubject_type().equals("일반교양")){
            model.addAttribute("page_type","3.1");
            result = "menu/liberal_arts/apply";
        }else if(subject.getSubject_type().equals("자격증/취창업")){
            result = "menu/certificate/apply";
//            if(subject.getSubject_field().matches("%자격증%")){
//                model.addAttribute("page_type","4.1");
//            }else if(subject.getSubject_field().matches("%취창업%")){
//                model.addAttribute("page_type","4.2");
//            }
            if(subject.getSubject_field().equals("반려동물")){
                model.addAttribute("page_type","4.1");
            }else if(subject.getSubject_field().equals("사회복지")){
                model.addAttribute("page_type","4.2");
            }else if(subject.getSubject_field().equals("실용음악")){
                model.addAttribute("page_type","4.3");
            }else if(subject.getSubject_field().equals("자격증")){
                model.addAttribute("page_type","4.4");
            }else if(subject.getSubject_field().equals("취창업")){
                model.addAttribute("page_type","4.5");
            }
        }else if(subject.getSubject_type().equals("특별교육과정")){
            result = "menu/special/apply";
            if(subject.getSubject_field().equals("바이블")){
                model.addAttribute("page_type","5.1");
            }else if(subject.getSubject_field().equals("용인")){
                model.addAttribute("page_type","5.2");
            }else if(subject.getSubject_field().equals("골프")){
                model.addAttribute("page_type","5.3");
            }else if(subject.getSubject_field().equals("교회음향")){
                model.addAttribute("page_type","5.5");
            }else if(subject.getSubject_field().equals("AI")){
                model.addAttribute("page_type","5.6");
            } else if (subject.getSubject_field().equals("축구아카데미")) {
                model.addAttribute("page_type","5.7");
            }
        }else if(subject.getSubject_type().equals("언어")){
            result = "menu/language/apply";
            if(subject.getSubject_field().equals("히브리어")){
                model.addAttribute("page_type","6.1");
            }else if(subject.getSubject_field().equals("헬라어")){
                model.addAttribute("page_type","6.2");
            }else if(subject.getSubject_field().equals("라틴어")){
                model.addAttribute("page_type","6.3");
            }else if(subject.getSubject_field().equals("독일어")){
                model.addAttribute("page_type","6.4");
            }else if(subject.getSubject_field().equals("한국어")){
                model.addAttribute("page_type","6.5");
            } else if (subject.getSubject_field().equals("영어")) {
                model.addAttribute("page_type","6.6");
            }
        } else if (subject.getSubject_type().equals("교육원")) {
            result = "menu/center/apply";
            if (subject.getSubject_field().equals("요양보호사교육원")) {
                model.addAttribute("page_type", "6.1");
            } else if (subject.getSubject_field().equals("미래금융교육원")) {
                model.addAttribute("page_type", "6.2");
            } else if (subject.getSubject_field().equals("미래교육교육원")) {
                model.addAttribute("page_type", "6.3");
            } else if (subject.getSubject_field().equals("드론전문교육원")) {
                model.addAttribute("page_type", "6.4");
            } else if (subject.getSubject_field().equals("장례지도사교육원")) {
                model.addAttribute("page_type", "6.5");
            } else if (subject.getSubject_field().equals("장로권사교육원")) {
                model.addAttribute("page_type", "6.6");
            } else if (subject.getSubject_field().equals("미래목회연구원")) {
                model.addAttribute("page_type", "6.7");
            } else if (subject.getSubject_field().equals("ESG경영연구원")) {
                model.addAttribute("page_type", "6.8");
            } else if (subject.getSubject_field().equals("사모교육원")) {
                model.addAttribute("page_type", "6.9");
            } else if (subject.getSubject_field().equals("여교역자교육원")) {
                model.addAttribute("page_type", "6.10");
            }
        }
        return result;
    }
    //수강신청
    @GetMapping("/menu/subject/apply/pro")
    @ResponseBody
    public String ApplyPro(HttpSession httpSession, @RequestParam(value = "subject_code") int subject_code){
        String result = "";
        if(httpSession.getAttribute("member_id")==null){
            result = "<script>alert('로그인이 필요한 서비스 입니다..');window.location.href = '/member/login';</script>";
        }else{
            String member_id = httpSession.getAttribute("member_id").toString();
            if(calvinSubjectService.ApplyWhether(member_id,subject_code)){
                int insert_result = calvinSubjectService.ApplyPro(member_id,subject_code);
                if(insert_result == 1){
                    result = "<script>alert('수강신청이 완료되었습니다.');window.location.href = '/menu/subject/apply/done';</script>";
                }else{
                    result = "<script>alert('수강신청에 실패하였습니다.');history.go(-2);</script>";
                }
            }else{
                result = "<script>alert('이미 신청한 강의 입니다.');window.location.href = document.referrer;</script>";
            }
        }
        return result;
    }
    //수강신청 완료
    @GetMapping("/menu/subject/apply/done")
    public String ApplyDone(){
        return "menu/subject/apply_done";
    }

    //회원 권한 변경
    @PostMapping("/mypage/admin/member/grant")
    @ResponseBody
    public String AdminMemberGrant(@RequestParam(value = "member_type") String member_type,
                                   @RequestParam(value = "member_code") String member_code,
                                   HttpSession httpSession){
        String result = "";
        if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
            if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {
                int grant_result = calvinMemberService.MemberGrant(member_code,member_type);
                if(grant_result == 1){
                    result = "<script>alert('권한이 성공적으로 변경되었습니다. 변경 내용은 새로고침 후 적용됩니다.');history.go(-1);</script>";
                }else{
                    result = "<script>alert('권한변경에 실패했습니다.');history.back();</script>";
                }
            }else {
                System.out.println("에러 : "+httpSession.getAttribute("member_type"));
            }
        } else{
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return result;
    }

    //회원 정보 삭제
    @GetMapping("/mypage/admin/member/delete")
    @ResponseBody
    public String AdminMemberDelete(@RequestParam(value = "member_code") int member_code,HttpSession httpSession){

        String result = "";
        if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
            if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {
                int delete_result = calvinMemberService.DeleteMember(member_code);
                if(delete_result == 1){
                    result = "<script>alert('회원 정보가 삭제되었습니다. 변경 내용은 새로고침 후 적용됩니다.');history.go(-2);</script>";

                }else{
                    result = "<script>alert('회원 정보 삭제에 실패했습니다.');history.back();</script>";
                }
            }
        }else{
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return result;
    }
    //어드민 회원관리
    @GetMapping("/mypage/admin/member")
    public String AdminMember(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "search_word", required = false, defaultValue = "") String search_word,
                              @RequestParam(value = "search_type", required = false, defaultValue = "1") int search_type,
                              Model model,HttpSession httpSession){
        String result = "menu/mypage/admin_member";
        if(!search_word.equals("")){
            Pattern RegPattern1 = Pattern.compile("/[^(A-Za-z가-힣0-9\\s.,@)]/");
            Matcher m = RegPattern1.matcher(search_word);
            search_word = m.replaceAll(" ");
        }
        if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
            if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {
                int count;
                List<Calvin_Member> member_list;

                if(search_word.equals("")){
                    count = calvinMemberService.paging();
                    member_list = calvinMemberService.SelectAllMember(page,count);
                }else{
                    count = calvinMemberService.paging(search_type,search_word);
                    if(search_type == 1){//아이디
                        member_list = calvinMemberService.SelectById(search_word,page,count);
                    }else{// 2 일때 이름
                        member_list = calvinMemberService.SelectByName(search_word,page,count);
                    }
                }
                int begin_page;
                if(page % 10 == 0){
                    begin_page = page-9;
                }else{
                    begin_page = page/10*10+1;
                }

                int max_page;
                if(count/20 == 0 && count%20 > 0){
                    max_page = 1;
                }else if(count/20 > 0 && count%20 > 0){
                    max_page = count/20 + 1;
                }else{
                    max_page = count/20;
                }
                model.addAttribute("search_word", search_word);
                model.addAttribute("search_type", search_type);
                model.addAttribute("page", page);
                model.addAttribute("begin_page",begin_page);
                model.addAttribute("max_page", max_page);
                model.addAttribute("member_list",member_list);
                model.addAttribute("page_type","9.3");
            }
        }else{
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
        return result;
    }

    //회원정보 열람
    @GetMapping("/mypage/admin/member/view")
    public String AdminMemberView(Model model, @RequestParam(value = "member_code") int member_code,HttpSession httpSession){
        if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
            if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {
                Calvin_Member calvin_member = calvinMemberService.MemberInfo(member_code,2);
                model.addAttribute("member", calvin_member);
                model.addAttribute("page_type","9.3");
            }
        }else{
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return "menu/mypage/admin_member_view";
    }
    //게시판 페이지 search_type = 검색방법 ex) 제목, 내용 admin => 관리자용 페이지
    @GetMapping({"/menu/board","/mypage/admin/board","/menu/culture/board"})
    public String BoardPage(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "search_word", required = false, defaultValue = "") String search_word,
                            @RequestParam(value = "search_type", required = false, defaultValue = "1") int search_type,
                            @RequestParam(value = "board_type",required = false, defaultValue = "") String board_type,
                            HttpSession httpSession, Model model){

        if(!search_word.equals("")){
            Pattern RegPattern1 = Pattern.compile("/[^(A-Za-z가-힣0-9\\s.,)]/");
            Matcher m = RegPattern1.matcher(search_word);
            search_word = m.replaceAll(" ");
            search_word = calvinBoardService.WordValidationPro(search_word);
        }
        int count = 0;
        List<BoardView> board_list = new ArrayList<>();
        String result = "";
        String page_type = "8.5";
        if(search_word.equals("")){
            if(board_type.equals("")){
                if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
                    if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {
                        count = calvinBoardService.paging();
                        board_list = calvinBoardService.SelectAllBoard(page, count);
                        result = "menu/mypage/admin_board";
                        page_type = "9.3";
                    }
                }else{
                    throw new CustomException(ErrorCode.INVALID_PERMISSION);
                }

            }else {
                LocalDateTime startTime = LocalDateTime.now();
                count = calvinBoardService.paging(board_type);
                board_list = calvinBoardService.SelectAllBoard(board_type, page, count);
                if(board_type.equals("공지사항")){
                    result = "menu/board/board01";
                    page_type = "8.5";
                    LocalDateTime now = LocalDateTime.now();
                }else if(board_type.equals("사진자료실")){
                    result = "menu/info2/gallery";
                    page_type = "8.6";
                }else if(board_type.equals("서식자료실")){
                    result = "menu/info2/format";
                    page_type = "8.7";
                } else if (board_type.equals("한베문화원")) {
                    result = "menu/cultural_center/board01";
                    model.addAttribute("board_type",board_type);
                    page_type = "7.1";
                }else if (board_type.equals("한몽문화원")) {
                    result = "menu/cultural_center/board01";
                    model.addAttribute("board_type",board_type);
                    page_type = "7.2";
                }else if (board_type.equals("한우문화원")) {
                    result = "menu/cultural_center/board01";
                    model.addAttribute("board_type",board_type);
                    page_type = "7.3";
                }else if (board_type.equals("한네문화원")) {
                    result = "menu/cultural_center/board01";
                    model.addAttribute("board_type",board_type);
                    page_type = "7.4";
                }
            }

        }else{
            if(board_type.equals("")){
                if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")){
                    count = calvinBoardService.paging(search_type,search_word);
                    board_list = calvinBoardService.SelectByTitle(search_word,page, count);
                    result = "menu/mypage/admin_board";
                    page_type = "9.3";
                }else{
                    throw new CustomException(ErrorCode.INVALID_PERMISSION);
                }
            }else if(board_type.equals("공지사항")){
                count = calvinBoardService.paging(board_type,search_type,search_word);
                board_list = calvinBoardService.SelectByTitle(board_type,search_word,page, count);
                result = "menu/board/board01";
                page_type = "8.5";
            }else if(board_type.equals("사진자료실")){
                count = calvinBoardService.paging(board_type,search_type,search_word);
                board_list = calvinBoardService.SelectByTitle(board_type,search_word,page, count);
                result = "menu/info2/gallery";
                page_type = "8.6";
            }else if(board_type.equals("서식자료실")){
                count = calvinBoardService.paging(board_type);
                board_list = calvinBoardService.SelectAllBoard(board_type,page, count);
                result = "menu/info2/format";
                page_type = "8.7";
            }
        }
        int begin_page;

        if(page % 10 == 0){
            begin_page = page-9;
        }else{
            begin_page = page/10*10+1;
        }

        int max_page;
        if(count/20 == 0 && count%20 > 0){
            max_page = 1;
        }else if(count/20 > 0 && count%20 > 0){
            max_page = count/20 + 1;
        }else{
            max_page = count/20;
        }
        model.addAttribute("search_word", search_word);
        model.addAttribute("search_type", search_type);
        model.addAttribute("page", page);
        model.addAttribute("begin_page",begin_page);
        model.addAttribute("max_page", max_page);
        model.addAttribute("board_list",board_list);
        model.addAttribute("page_type",page_type);
        model.addAttribute("board_type", board_type);

        return result;
    }

    //게시글 확인 페이지 id == board_code
    @GetMapping({"/menu/board/view","/menu/culture/view"})
    public String BoardView(@RequestParam(value = "id") int id, Model model){
        BoardView boardView = calvinBoardService.SelectBoardDetail(id);
        Calvin_file calvinFile1 = calvinFileService.getFileOriginName(boardView.getFile_code1());
        Calvin_file calvinFile2 = calvinFileService.getFileOriginName(boardView.getFile_code2());
        Calvin_file calvinFile3 = calvinFileService.getFileOriginName(boardView.getFile_code3());
        Calvin_file calvinFile4 = calvinFileService.getFileOriginName(boardView.getFile_code4());
        Calvin_file calvinFile5 = calvinFileService.getFileOriginName(boardView.getFile_code5());
        model.addAttribute("boardView", boardView);
        model.addAttribute("file1",calvinFile1);
        model.addAttribute("file2",calvinFile2);
        model.addAttribute("file3",calvinFile3);
        model.addAttribute("file4",calvinFile4);
        model.addAttribute("file5",calvinFile5);
        model.addAttribute("page_type", "8.5");
        String board_type = boardView.getBoard_type();
        String result = "menu/board/board_view";
        if(board_type.equals("공지사항")){
            model.addAttribute("page_type", "8.5");
        }else if(board_type.equals("사진자료실")){
            model.addAttribute("page_type", "8.6");
        }else if(board_type.equals("서식자료실")){
            model.addAttribute("page_type", "8.7");
        }else if(board_type.equals("한베문화원")){
            result = "menu/cultural_center/board_view";
            model.addAttribute("page_type","7.1");
        }else if(board_type.equals("한몽문화원")){
            result = "menu/cultural_center/board_view";
            model.addAttribute("page_type","7.2");
        }else if(board_type.equals("한우문화원")){
            result = "menu/cultural_center/board_view";
            model.addAttribute("page_type","7.3");
        }else if(board_type.equals("한네문화원")){
            result = "menu/cultural_center/board_view";
            model.addAttribute("page_type","7.4");
        }
        return result;
    }
    //게시글 작성 페이지
    @GetMapping("/menu/board/write")
    public String BoardWrite(HttpSession httpSession){
        if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
            if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {

            }
            }else {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
        return "menu/board/board_write";
    }

    //국제문화원 게시글 작성 페이지
    @GetMapping("/menu/culture/write")
    public String cultureWritePage(@RequestParam(value = "board_type") String board_type, HttpSession httpSession, Model model) {
        if(httpSession.getAttribute("member_id") != null && httpSession.getAttribute("member_type") != null){
            if(httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")||httpSession.getAttribute("member_type").equals("ai")) {
                if(board_type.equals("한베문화원")){
                    model.addAttribute("page_type", "7.1");
                }else if(board_type.equals("한몽문화원")){
                    model.addAttribute("page_type", "7.2");
                }else if(board_type.equals("한우문화원")){
                    model.addAttribute("page_type", "7.3");
                }else if(board_type.equals("한네문화원")){
                    model.addAttribute("page_type", "7.4");
                }
            }
        }else {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
        return "menu/cultural_center/board_write";
    }

    //게시글 작성
    @PostMapping("/menu/board/write")
    public String InsertBoard(@RequestParam(value = "title") String title, @RequestParam(value = "contents") String board_contents,
                              @RequestParam(value = "member_id") String member_id,
                              @RequestParam(value = "board_type") String board_type,
                              @RequestParam(value = "file1", required = false) MultipartFile file1,
                              @RequestParam(value = "file2", required = false) MultipartFile file2,
                              @RequestParam(value = "file3", required = false) MultipartFile file3,
                              @RequestParam(value = "file4", required = false) MultipartFile file4,
                              @RequestParam(value = "file5", required = false) MultipartFile file5) {

        String result = "";
        List<MultipartFile> file_list = new ArrayList<>();
        boolean token = false;
        if (file1 != null) {
            file_list.add(file1);
            token = true;
        }
        if (file2 != null) {
            file_list.add(file2);
            token = true;
        }
        if (file3 != null) {
            file_list.add(file3);
            token = true;
        }
        if (file4 != null) {
            file_list.add(file4);
            token = true;
        }
        if (file5 != null) {
            file_list.add(file5);
            token = true;
        }
        int insert_result;
        if (token) {
            insert_result = calvinBoardService.insertBoard(title, board_contents, member_id, file_list, board_type);
        } else {
            insert_result = calvinBoardService.insertBoard(title, board_contents, member_id, board_type);
        }
        if (insert_result == 1) {
            result = "redirect:/menu/board";
        } else {
            //insert 실패시
            result = "redirect:/menu/board";
        }
        return result;
    }

    //원장 인사말 페이지 1.1
    @GetMapping("/menu/info/greeting")
    public String GreetingPage(Model model){
        model.addAttribute("page_type","1.1");
        return "menu/information/greetings";
    }

    //연혁 페이지 1.2
    @GetMapping("/menu/info/history")
    public String HistoryPage(Model model){
        model.addAttribute("page_type","1.2");
        return "menu/information/history";
    }
    //발전 계획 1.3
    @GetMapping("/menu/info/masterplan")
    public String masterPlan(Model model) {
        model.addAttribute("page_type", "1.3");
        return "menu/information/master_plan";
    }
    //조직도 페이지 1.4
    @GetMapping("/menu/info/organization")
    public String OrganizationPage(Model model){
        model.addAttribute("page_type","1.4");
        return "menu/information/organization";
    }

    //캠퍼스 안내 페이지 1.5
    @GetMapping("/menu/info/campus")
    public String CampusPage(Model model){
        model.addAttribute("page_type","1.5");
        return "menu/information/campus";
    }
    //찾아오시는 길 페이지 1.6
    @GetMapping("/menu/info/path")
    public String PathPage(Model model){
        model.addAttribute("page_type","1.6");
        return "menu/information/path";
    }

    //파일 다운로드
    @GetMapping("/download/{save_name}/{original_name}")
    public ResponseEntity DownloadFile(@PathVariable String save_name, @PathVariable String original_name){
        return calvinFileService.FileDownload(save_name,original_name);
    }

    @GetMapping("/download/document/{original_name}")
    public ResponseEntity DownloadDoc(@PathVariable String original_name){
        return calvinFileService.FileDownload(original_name);
    }

    //메인 팝업존
    @GetMapping("/popupzone")
    public String popupzone(){
        return "popupzone";
    }

    //2장로권사포럼 7.2
    @GetMapping("/menu/ministry/presbyter")
    public String presbyter(Model model){
        model.addAttribute("page_type","7.2");
        return "menu/ministry/presbyter";
    }

    //여교역자포럼 7.3
    @GetMapping("/menu/ministry/f_pastor")
    public String f_pastor(Model model){
        model.addAttribute("page_type","7.3");
        return "menu/ministry/f_pastor";
    }

    //모집안내 8.1
    @GetMapping("/menu/info2/recruit")
    public String recruit(Model model){
        model.addAttribute("page_type", "8.1");
        return "menu/info2/recruit_info";
    }

    //학사일정 8.2
    @GetMapping("/menu/info2/calendar")
    public String calendar(Model model){
        model.addAttribute("page_type","8.2");
        return "menu/info2/calendar";
    }

    //장학제도 8.3
    @GetMapping("/menu/info2/scholarship")
    public String scholarship(Model model){
        model.addAttribute("page_type","8.3");
        return "menu/info2/scholarship_system";
    }

    //수강신청안내 8.4
    @GetMapping("/menu/info2/apply_guide")
    public String apply_guide(Model model){
        model.addAttribute("page_type", "8.4");
        return "menu/info2/apply_guide";
    }

    //내 강의 9.1
    @GetMapping("/member/mypage/subject")
    public String my_subject(Model model, HttpSession httpSession){
        String result ="";
        if(httpSession.getAttribute("member_id") == null){
            result = "redirect:/member/login";
        }else{
            List<MyPageSubjectView> subject_list = calvinSubjectService.My_subject(httpSession.getAttribute("member_id").toString());
            model.addAttribute("subject_list",subject_list);
            model.addAttribute("page_type", "9.1");
            result = "menu/mypage/subject_list";
        }

        return result;
    }

    //내 정보 9.2
    @GetMapping("/member/mypage/info")
    public String my_info(Model model, HttpSession httpSession){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "redirect:/member/login";
        }else{
            Calvin_Member cm = calvinMemberService.MyInfo(httpSession.getAttribute("member_id").toString(),1);
            model.addAttribute("info", cm);
            model.addAttribute("page_type","9.2");
            result = "menu/mypage/info";
        }

        return result;
    }

    //정보변경 페이지9.2
    @GetMapping("/member/mypage/modify")
    public String my_info_modify(Model model, HttpSession httpSession){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "redirect:/member/login";
        }else{
            Calvin_Member cm = calvinMemberService.MyInfo(httpSession.getAttribute("member_id").toString(),2);
            JoinMember jm = new JoinMember();
            model.addAttribute("info", cm);
            model.addAttribute("member", jm);
            result =  "menu/mypage/modify";
        }

        return result;
    }

    //정보변경
    @RequestMapping(value = "/member/mypage/modify/pro", method = RequestMethod.POST)
    @ResponseBody
    public String my_info_modify_pro(JoinMember member, HttpSession httpSession){
        boolean update_result = false;
        String result;
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "<script>alert('로그인이 필요한 서비스 입니다.');window.location.href='/';</script>";

        }else{
            if(!member.getPwd().equals("")){
                update_result = calvinMemberService.MemberInfoUpdatePwd(member.getPwd(),httpSession.getAttribute("member_id").toString());
            }
            if(!member.getPhone_number().equals("")){
                update_result = calvinMemberService.MemberInfoUpdatePn(member.getPhone_number(),httpSession.getAttribute("member_id").toString());
            }
            if(!member.getAddress().equals("")){
                update_result = calvinMemberService.MemberInfoUpdateAddress(member.getAddress(),httpSession.getAttribute("member_id").toString());
            }

            if(update_result){
//            result = "<script>alert('회원정보가 변경되었습니다..');window.location.href='http://calvin.or.kr/member/mypage/info'</script>";//서버
//            result = "<script>alert('회원정보가 변경되었습니다.');window.location.href='http://localhost:8080/member/mypage/info'</script>";//
                result = "<script>alert('회원정보가 변경되었습니다. 변경 내용은 새로고침 후 적용됩니다.');history.go(-2);</script>";
            }else{
//            result = "<script>alert('회원정보 변경에 실패하였습니다.');window.location.href='http://calvin.or.kr/member/mypage/info'</script>";//서버
//            result = "<script>alert('회원정보 변경에 실패하였습니다.');window.location.href='http://localhost:8080/member/mypage/info'</script>";//
                result = "<script>alert('회원정보 변경에 실패하였습니다.');history.go(-2);</script>";

            }
        }

        return result;
    }

    //게시글 삭제
    @GetMapping("/menu/board/delete")
    @ResponseBody
    public String boardDelete(@RequestParam(value = "board_code") int board_code, HttpSession httpSession, Model model){
        String result;
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }else{
            if(httpSession.getAttribute("member_type").equals("dd") || httpSession.getAttribute("member_type").equals("ai") || httpSession.getAttribute("member_type").equals("st")){
                int dt_result = calvinBoardService.DeleteBoard(board_code);
                if(dt_result == 1){
                    result = "<script>alert('게시글이 삭제되었습니다.');history.back();</script>";
                }else{
                    result = "<script>alert('게시글 삭제에 실패했습니다.');history.back();</script>";
                }
            }else {
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }

        return result;
    }

    //비밀번호 인증
    @GetMapping("/member/mypage/auth")
    public String AuthPage(Model model){
        model.addAttribute("page_type","9.2");
        return "menu/mypage/auth";
    }

    //인증
    @RequestMapping(value = "/member/mypage/auth/pro", method = RequestMethod.POST)
    @ResponseBody
    public String AuthPro(HttpSession httpSession, @RequestParam(value = "o_pwd") String pwd){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "<script>alert('로그인이 필요한 서비스 입니다.');window.location.href='/member/login'</script>";//서버
        }else{
            if(calvinMemberService.login(httpSession.getAttribute("member_id").toString(), pwd)){
                result = "<script>window.location.href='/member/mypage/modify'</script>";//서버
//            result = "window.location.href='http://localhost:8080/member/mypage/modify'</script>";//
            }else{
                result = "<script>alert('비밀번호가 일치하지 않습니다.');window.location.href='/member/mypage/auth'</script>";//서버
//            result = "<script>alert('비밀번호가 일치하지 않습니다.');window.location.href='http://localhost:8080/member/mypage/auth'</script>";//
            }
        }

        return result;
    }

    //신청 관리 페이지 9.3
    @GetMapping("/mypage/admin/apply")
    public String ApplyManagePage(Model model,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(value = "search_type", required = false, defaultValue = "1") int search_type,
                                  @RequestParam(value = "search_word", required = false, defaultValue = "")String search_word,
                                  HttpSession httpSession){
        if(!search_word.equals("")){
            Pattern RegPattern1 = Pattern.compile("/[^(A-Za-z가-힣0-9\\s.,)]/");
            Matcher m = RegPattern1.matcher(search_word);
            search_word = m.replaceAll(" ");
        }
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "redirect:/member/login";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                List<MyPageSubjectView> apply = new ArrayList<>();
                int count = 0;
                if(search_word.equals("")){
                    apply = calvinSubjectService.SelectAllApply(page);
                    count = calvinSubjectService.admin_paging_apply();
                }else{
                    if(search_type == 1){//강의명
                        apply = calvinSubjectService.SelectApplyBySubjectName(page, search_word);
                        count = calvinSubjectService.admin_paging_apply(1,search_word);
                    }else if(search_type == 2){//아이디
                        apply = calvinSubjectService.SelectApplyById(page, search_word);
                        count = calvinSubjectService.admin_paging_apply(2,search_word);
                    }
                }
                int begin_page;

                if(page % 10 == 0){
                    begin_page = page-9;
                }else{
                    begin_page = page/10*10+1;
                }
                int max_page;
                if(count/20 == 0 && count%20 > 0){
                    max_page = 1;
                }else if(count/20 > 0 && count%20 > 0){
                    max_page = count/20 + 1;
                }else{
                    max_page = count/20;
                }
                model.addAttribute("search_word", search_word);
                model.addAttribute("search_type", search_type);
                model.addAttribute("page", page);
                model.addAttribute("begin_page",begin_page);
                model.addAttribute("max_page", max_page);
                model.addAttribute("apply_list" ,apply);
                model.addAttribute("page_type","9.3");
                result = "menu/mypage/admin_apply";
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return result;
    }

    //신청 취소
    @GetMapping("/mypage/admin/apply/manage")
    @ResponseBody
    public String ApplyManage(Model model, @RequestParam List<Integer> apply_list,HttpSession httpSession){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type")==null){
            result = "<script>alert('로그인이 필요한 서비스 입니다.');window.location.href = '/member/login';</script>";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                int delete_result = calvinSubjectService.ApplyManage(apply_list);
                if(apply_list.size() == delete_result){
                    result = "<script>alert('수강신청이 정상적으로 취소되었습니다.');window.location.href = document.referrer;</script>";
                }else if(apply_list.size() >= delete_result && delete_result > 0) {
                    result = "<script>alert('일부 수강신청만 취소되었습니다. 이미 취소된 수강신청이 아닌지 확인해주세요.'); window.location.href = document.referrer;</script>";
                }else if(delete_result == 0){
                    result = "<script>alert('수강신청 취소에 실패했습니다. 이미 취소된 수강신청이 아닌지 확인해주세요.'); window.location.href = document.referrer;</script>";
                }
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return result;
    }
    //결제 상태 변경
    @GetMapping("/mypage/admin/apply/pay")// 1 : 납부완료, 2 : 납부취소, 3 : 환불
    @ResponseBody
    public String PayManage(Model model, @RequestParam List<Integer> apply_list, @RequestParam int type,HttpSession httpSession){
        String result = "<script>alert('";
        String word = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "<script>alert('로그인이 필요한 서비스 입니다.');window.location.href = '/member/login';</script>";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                int payManage_result = calvinSubjectService.PayManage(apply_list,type);
                switch (type){
                    case 1:
                        word = "납부완료 처리";
                        break;
                    case 2:
                        word = "납부취소 처리";
                        break;
                    case 3:
                        word = "환불 처리";
                        break;
                }
                if(apply_list.size() == payManage_result){
                    result += "수강신청이 정상적으로 "+word+"되었습니다.";
                }else if(apply_list.size() >= payManage_result && payManage_result > 0){
                    result += "일부 수강신청만 " + word + "되었습니다.  수강신청 상태를 확인해주세요.";
                }else if(payManage_result == 0){
                    result += "수강신청이 정상적으로 "+ word + "되지 않았습니다.  수강신청 상태를 확인해주세요.";
                }
                result += "');window.location.href = document.referrer;</script>";
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }

        return result;
    }

    //개설 강의 관리
    @GetMapping("/mypage/admin/subject")
    public String SubjectManage(Model model,
                                @RequestParam(value = "search_word", required = false, defaultValue = "")String search_word,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "search_type", required = false, defaultValue = "1") int search_type,
                                HttpSession httpSession){
        if(!search_word.equals("")){
            Pattern RegPattern1 = Pattern.compile("/[^(A-Za-z가-힣0-9\\s.,)]/");
            Matcher m = RegPattern1.matcher(search_word);
            search_word = m.replaceAll(" ");
        }
        String result ="";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type")==null){
            result = "redirect:/member/login";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                List<Calvin_subject> list;
                int count = 0;
                if(search_word.equals("")){
                    list = calvinSubjectService.SelectSubject_admin(page);
                    count = calvinSubjectService.admin_paging();
                }else{
                    if(search_type == 1){
                        list = calvinSubjectService.SelectSubjectByName_admin(page,search_word);
                        count = calvinSubjectService.admin_paging(1,search_word);
                    }else if(search_type == 2){
                        list = calvinSubjectService.SelectSubjectByField_admin(page,search_word);
                        count = calvinSubjectService.admin_paging(2, search_word);
                    }else{//3일때
                        list = calvinSubjectService.SelectSubjectByType_admin(page, search_word);
                        count = calvinSubjectService.admin_paging(3, search_word);
                    }
                }
                int begin_page;

                if(page % 10 == 0){
                    begin_page = page-9;
                }else{
                    begin_page = page/10*10+1;
                }
                int max_page;
                if(count/20 == 0 && count%20 > 0){
                    max_page = 1;
                }else if(count/20 > 0 && count%20 > 0){
                    max_page = count/20 + 1;
                }else{
                    max_page = count/20;
                }
                model.addAttribute("search_word", search_word);
                model.addAttribute("search_type", search_type);
                model.addAttribute("page", page);
                model.addAttribute("begin_page",begin_page);
                model.addAttribute("max_page", max_page);
                model.addAttribute("subject_list",list);
                model.addAttribute("page_type", "9.3");
                result ="menu/mypage/admin_subject";
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }

        }

        return result;
    }

    //강의상태 변경
    @GetMapping("/mypage/admin/subject/manage")
    @ResponseBody
    public String SubjectStatManage(@RequestParam(value = "subject_code")int subject_code,
                                    @RequestParam(value = "stat")int stat, HttpSession httpSession){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type")==null){
            result = "<script>alert('로그인이 필요한 서비스 입니다.'); window.location.href = '/member/login';</script>";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                int pro_result = calvinSubjectService.SubjectStatManage(subject_code,stat);

                if(pro_result == 1){
                    result = "<script>alert('강의상태가 성공적으로 변경되었습니다.'); window.location.href = document.referrer;</script>";
                }else{
                    result = "<script>alert('강의상태 변경에 실패했습니다.'); window.location.href = document.referrer;</script>";
                }
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return result;
    }


    //강의 개설 페이지 & 강의 수정
    @GetMapping("/menu/subject/write_page")
    public String NewSubjectWritePage(Model model,@RequestParam(value = "subject_code", required = false, defaultValue = "-1") int subject_code, HttpSession httpSession){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result ="redirect:/member/login";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                Calvin_subject calvin_subject = new Calvin_subject();
                if(subject_code != -1){
                    calvin_subject = calvinSubjectService.SubjectApply(subject_code);
                    model.addAttribute("subject_code", subject_code);
                }
                model.addAttribute("subject",calvin_subject);
                List<Calvin_Member> list = calvinMemberService.ProfessorList();
                model.addAttribute("professor",list);
                model.addAttribute("page_type","9.3");
                result = "menu/subject/subject_write";
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return result;
    }

    @PostMapping("/menu/subject/write")
    @ResponseBody
    public String NewSubjectWrite(@RequestParam(value = "subject_code", required = false, defaultValue = "-1") int subject_code,
                                  @RequestParam(value = "subject_name") String subject_name,
                                  @RequestParam(value = "subject_field") String subject_field,
                                  @RequestParam(value = "subject_type") String subject_type,
                                  @RequestParam(value = "personnel") int personnel,
                                  @RequestParam(value = "lecture_time") String lecture_time,
                                  @RequestParam(value = "period") int period,
                                  @RequestParam(value = "member_code") int member_code,
                                  @RequestParam(value = "fee") int fee,
                                  @RequestParam(value = "file1", required = false) MultipartFile file,
                                  HttpSession httpSession){
        int insert_result = 0;
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "<script>alert('로그인이 필요한 서비스입니다..');window.location.href='/member/login';</script>";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                if(subject_code != -1){
                    if(file != null){
                        insert_result = calvinSubjectService.ModifySubject(subject_name,subject_field,subject_type,personnel,lecture_time,period,member_code,fee,file,subject_code);
                    }else{
                        insert_result = calvinSubjectService.ModifySubject(subject_name,subject_field,subject_type,personnel,lecture_time,period,member_code,fee, subject_code);
                    }
                }else{
                    if(file != null){
                        insert_result = calvinSubjectService.InsertSubject(subject_name,subject_field,subject_type,personnel,lecture_time,period,member_code,fee,file);
                    }else{
                        insert_result = calvinSubjectService.InsertSubject(subject_name,subject_field,subject_type,personnel,lecture_time,period,member_code,fee);
                    }
                }
                if(insert_result == 1){
                    if(subject_code != -1){
                        result = "<script>alert('강의가 성공적으로 수정되었습니다. 변경사항은 새로고침 후 적용됩니다.');history.go(-2);</script>";
                    }else{
                        result = "<script>alert('신규 강의가 성공적으로 개설되었습니다. 변경사항은 새로고침 후 적용됩니다.');history.go(-2);</script>";
                    }
                }else{
                    result = "<script>alert('신규 강의 개설 또는 수정에 실패하였습니다. 작성한 내용에 오류가 없는지 확인해주세요');history.back();</script>";
                }
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return result;
    }

    //강의 삭제
    @GetMapping("/menu/subject/delete")
    @ResponseBody
    public String DeleteSubject(HttpSession httpSession, @RequestParam(value = "subject_code") int subject_code){
        String result = "";
        if(httpSession.getAttribute("member_id") == null || httpSession.getAttribute("member_type") == null){
            result = "<script>alert('로그인이 필요한 서비스입니다..');window.location.href='/member/login';</script>";
        }else{
            if(httpSession.getAttribute("member_type").equals("ai")||httpSession.getAttribute("member_type").equals("dd")||httpSession.getAttribute("member_type").equals("st")){
                int delete_result = calvinSubjectService.DeleteSubject(subject_code);
                if(delete_result == 1) {
                    result = "<script>alert('강의가 성공적으로 삭제되었습니다. 해당 창을 닫고 새로고침시 변경사항이 적용됩니다.');window.location.href=history.go(-2);</script>";
                }else{
                    result = "<script>alert('강의 삭제에 실패했습니다.');window.location.href=document.referrer;</script>";
                }
            }else{
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
        }
        return result;
    }

    @GetMapping("/temp_popup")
    public String temp_popup(){
        return "temp_popup";
    }

}
