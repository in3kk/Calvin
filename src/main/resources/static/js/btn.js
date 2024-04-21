function history_back(){
    history.back();
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