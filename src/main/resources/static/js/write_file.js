

function add_file(){
    let document_name = "file";
    for(let i = 2; i <= 5; i++){
        if(document.getElementById(document_name+i).style.display == 'none'){
            document.getElementById(document_name+i+'_label').style.display = 'block';
            document.getElementById(document_name+i).style.display = 'block';
            if(i == 5){
                document.getElementById("add_file_btn").style.display = 'none';
                document.getElementById("add_file_warning").style.display = 'block';
            }
            break;
        }
    }
}

function add_img(img_code){
    // let file = document.getElementById("file"+img_code);
    // let img = document.getElementById("img_view_"+img_code)
    // img.style.display = 'block';
    // img.src = URL.createObjectURL(file.files[0]);
    document.execCommand("insertHTML", false, '<img src="' + URL.createObjectURL(file.files[0]) + '" alt="Image">');
}