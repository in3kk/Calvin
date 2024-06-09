window.onload = function(){
    document.getElementById('submit_btn').disabled = true;
}
//  
//  
function check_value(){
    if(document.getElementById('id_check_value').value == 'pass' && document.getElementById('pn_check_value').value == 'pass' && document.getElementById('pwd_check_value').value == 'pass' && 
    document.getElementById('birth_check_value').value == 'pass' && document.getElementById('address_check_value').value == 'pass' && document.getElementById('name_check_value').value == 'pass' &&
    document.getElementById('permit').checked == true){
        document.getElementById('submit_btn').disabled = false;
    }else{
        document.getElementById('submit_btn').disabled = true;
    }
}
function check_pw() {
    const regex = /[a-zA-Z0-9!@#$%^&\*()_\+]{10,25}/;
    if(regex.test(document.getElementById('pwd').value)){
        if (document.getElementById('pwd').value != '' && document.getElementById('pwd2').value != '') {
            if (document.getElementById('pwd').value == document.getElementById('pwd2').value) {
                document.getElementById('check').innerHTML = '비밀번호가 일치합니다.'
                document.getElementById('check').style.color = 'green';
                document.getElementById('pwd_check_value').value = 'pass';
                check_value();
            } else {
                document.getElementById('check').innerHTML = '비밀번호가 일치하지 않습니다.';
                document.getElementById('check').style.color = 'red';
                document.getElementById('pwd_check_value').value = 'x';
                document.getElementById('submit_btn').disabled = true;
            }
        }
    }else{
        document.getElementById('check').innerHTML = '유효한 비밀번호 형식이 아닙니다.';
        document.getElementById('check').style.color = 'red';
        document.getElementById('pwd_check_value').value = 'x';
        document.getElementById('submit_btn').disabled = true;
    }

}

function check_pn(){
  const regex = /0\d{1,2}\d{3,4}\d{4}/;
  if(regex.test(document.getElementById('phone_number').value)){
    document.getElementById('pn_check').innerHTML = '';
    document.getElementById('pn_check_value').value = 'pass';
    check_value();
  } else {
    document.getElementById('pn_check').innerHTML = '유효한 전화번호가 아닙니다.';
    document.getElementById('pn_check').style.color = 'red';
    document.getElementById('pn_check_value').value = 'x';
    document.getElementById('submit_btn').disabled = true;
  }
}

function check_name(){
    const regex = /[가-힣A-Za-z]{2,10}/;

    if(regex.test(document.getElementById('name').value)){
        document.getElementById('name_check').innerHTML='';
        document.getElementById('name_check_value').value = 'pass';
        check_value();
    }else{
        document.getElementById('name_check').innerHTML='유효한 이름 형식이 아닙니다.';
        document.getElementById('name_check').style.color='red';
        document.getElementById('name_check_value').value = 'x';
        document.getElementById('submit_btn').disabled = true;
    }
}
function check_birth(){
    document.getElementById('birth_check_value').value='pass';
    check_value();
}
function check_address(){
    const regex = /[가-힣0-9\s-]*/;
    if(regex.test(document.getElementById('sample6_detailAddress').value)){
        document.getElementById('address_check_value').value='pass';
        check_value();
    }else{
        document.getElementById('address_check_value').value='x';
    }

}
function check_id(){
    const regex = /[A-Za-z0-9]{4,15}/;
    const regex2 = /[a-z]{4,10}.(com|net|ac.kr)/;
    if(regex.test(document.getElementById('id').value) && regex2.test(document.getElementById('id2').value)){
        document.getElementById('id_check').innerHTML='';
        $.ajax({
            url: "/id_dup_check",
            contentType: "application/json",
            data: JSON.stringify({ user_id: document.getElementById('id').value+'@'+document.getElementById('id2').value }),
            type: 'POST',
            success: function (data) {
              console.log(data);
              if (data) {
                document.getElementById('id_check').innerHTML='이미 존재하는 아이디 입니다.';
                document.getElementById('id_check').style.color = 'red';
                document.getElementById('id_check_value').value = 'x';
                document.getElementById('id').focus();
                document.getElementById('submit_btn').disabled = true;
                return;
              } else {
                document.getElementById('id_check').innerHTML='사용가능한 아이디 입니다.';
                document.getElementById('id_check').style.color = 'green';
                document.getElementById('id_check_value').value = 'pass';
                check_value();
                return;
              }
            },
            error: function(){
                alert("중복확인에 실패했습니다. 잠시후 다시 시도해 주세요.");
                document.getElementById('id_check_value').value = 'x';
                document.getElementById('id_check').innerHTML='중복 확인에 실패했습니다.';
                document.getElementById('id_check').style.color = 'red';
                document.getElementById('submit_btn').disabled = true;
            }
          });
    }else{
        document.getElementById('id_check').innerHTML = '유효한 아이디 형식이 아닙니다.';
        document.getElementById('id_check').style.color = 'red';
        document.getElementById('id_check_value').value = 'x';
        document.getElementById('submit_btn').disabled = true;
    }
}

function check_permit(){
    check_value();
}


