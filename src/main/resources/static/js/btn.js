function history_back(){
    history.back();
}

function changeField(type_value){
    const lists = {
        "학점은행제": ["사회복지학과", "사회복지현장실습", "장애아특수교사", "상담학과", "신학과", "아동학과", "실용음악학과", "교양", "행정관리사"],
        "자격증/취창업": ["등록민간자격증", "기술자격증", "취창업과정"],
        "특별교육과정": ["바이블아카데미", "용인학아카데미", "골프아카데미", "교회음향아카데미", "AI아카데미", "경기교육아카데미", "축구아카데미"],
        "일반교양": ["놀술강좌", "자기계발강좌", "생활건강", "생활교양", "생활예술", "성경고전어강좌", "라틴어강좌", "영어강좌", "독일어강좌"],
        "교육원": ["요양보호사교육원", "미래금융교육원", "미래교육교육원", "드론전문교육원", "장례지도사교육원", "장로권사교육원", "미래목회연구원", "ESG경영연구원", "사모교육원", "여교역자교육원"]
    };

    const select = document.getElementsByName("subject_field")[0];
    select.options.length = 0;

    const targetList = lists[type_value] || []; // 없는 경우 빈 배열

    targetList.forEach(field => {
        const option = document.createElement("option");
        option.value = field;
        option.textContent = field;
        select.appendChild(option);
    });
}

function delete_member_btn(member_code){
    if(confirm('해당 회원의 정보를 삭제합니다.')){
        window.location.href = '/mypage/admin/member/delete?member_code='+member_code;
    }
}
function ManagePro(type){
    const query = 'input[name="apply_code"]:checked';
    const selectedEls = document.querySelectorAll(query);
    let result = '';
    if(type == 'payY'){
        result += "/mypage/admin/apply/pay?type=1&apply_list=";
        result += selectedEls[0].value;
        for(tmp = 1; tmp < selectedEls.length; tmp++){
            result += ","+selectedEls[tmp].value;
        }
    }else if(type == 'payN'){
        result += "/mypage/admin/apply/pay?type=2&apply_list=";
        result += selectedEls[0].value;
        for(tmp = 1; tmp < selectedEls.length; tmp++){
            result += ","+selectedEls[tmp].value;
        }
    }else if(type == 'refund'){
        result += "/mypage/admin/apply/pay?type=3&apply_list=";
        result += selectedEls[0].value;
        for(tmp = 1; tmp < selectedEls.length; tmp++){
            result += ","+selectedEls[tmp].value;
        }
    }else if(type == 'apply'){
        result += "/mypage/admin/apply/manage?apply_list=";
        result += selectedEls[0].value;
        for(tmp = 1; tmp < selectedEls.length; tmp++){
            result += ","+selectedEls[tmp].value;
        }
    }
    window.location.href = result;
}
function SubjectStatManage(type,subject_code){
    const result = "/mypage/admin/subject/manage?stat="+type+"&subject_code="+subject_code;
}

function ApplyYN(subject_code){
    if(confirm('해당 강의에 수강신청하시겠습니까?')){
        window.location.href='/menu/subject/apply/pro?subject_code='+subject_code;
    }
}

function GrantAuthor(){
    return confirm('해당 회원의 권한을 변경합니다.');
}

function ChangeSubjectStat(subject_code, stat){
    if(confirm('해당 강의의 상태를 변경합니다.')){
        window.location.href='/mypage/admin/subject/manage?subject_code='+subject_code+'&stat='+stat;
    }
}
function ModifyApply(subject_code){
    if(confirm('해당 강의의 내용을 수정합니다.')){
        window.location.href='/menu/subject/write_page?subject_code='+subject_code;
    }
}

function DeleteSubject(subject_code){
    if(confirm('해당 강의를 삭제합니다.')){
        window.location.href='/menu/subject/delete?subject_code='+subject_code;
    }
}