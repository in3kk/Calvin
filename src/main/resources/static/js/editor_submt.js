function editor_submit(){
    oEditors.getById["editor"].exec("UPDATE_CONTENTS_FIELD", [])
    let content = document.getElementById("editorTxt").value
  
    if(content == '') {
      alert("내용을 입력해주세요.")
      oEditors.getById["editorTxt"].exec("FOCUS")
      return
    } else {
      console.log(content)
    }
}
