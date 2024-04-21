<!-- JavaScript
function change(form)
{
if (form.url.selectedIndex !=0)
parent.location = form.url.options[form.url.selectedIndex].value
}
function setCookie( name, value, expiredays )
{
        var todayDate = new Date();
        todayDate.setDate( todayDate.getDate() + expiredays );
        document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}
function getCookie( name )
{
        var nameOfCookie = name + "=";
        var x = 0;
        while ( x <= document.cookie.length )
        {
                var y = (x+nameOfCookie.length);
                if ( document.cookie.substring( x, y ) == nameOfCookie ) {
                        if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 )
                                endOfCookie = document.cookie.length;
                        return unescape( document.cookie.substring( y, endOfCookie ) );
                }
                x = document.cookie.indexOf( " ", x ) + 1;
                if ( x == 0 )
                        break;
        }
        return "";
}

// �˾�â ��� -->


//if ( getCookie( "Notice1" ) != "done" )
//{
//     noticeWindow  =  window.open('../popup/popuplist.html','Notice1','scrollbars=no, width=355, height=482, top=10, left=10');//�̺κ��� �ڱ⿡ �°� �����ϼ���
//     noticeWindow.opener = self; 
//}


//if ( getCookie( "Notice0526" ) != "done" )
//{
//     noticeWindow  =  window.open('../popup/230526.html','Notice0526','scrollbars=no, width=490, height=415, top=10, left=10');//�̺κ��� �ڱ⿡ �°� �����ϼ���
//     noticeWindow.opener = self; 
//}

//if ( getCookie( "Notice0627" ) != "done" )
//{
//     noticeWindow  =  window.open('../popup/230627.html','Notice0627','scrollbars=no, width=700, height=415, top=10, left=10');//�̺κ��� �ڱ⿡ �°� �����ϼ���
//     noticeWindow.opener = self; 
//}

//if ( getCookie( "Notice1026" ) != "done" )
//{
//     noticeWindow  =  window.open('../popup/231026.html','Notice1026','scrollbars=no, width=605, height=535, top=10, left=10');//�̺κ��� �ڱ⿡ �°� �����ϼ���
//     noticeWindow.opener = self; 
//}

 

//if ( getCookie( "Notice1109" ) != "done" )
//{
//     noticeWindow  =  window.open('../popup/231109.html','Notice1109','scrollbars=no, width=605, height=456, top=10, left=615');//�̺κ��� �ڱ⿡ �°� �����ϼ���
//     noticeWindow.opener = self;
//}

//if ( getCookie( "Notice1204" ) != "done" )
//{
//     noticeWindow  =  window.open('../popup/231204.html','Notice1204','scrollbars=no, width=605, height=463, top=10, left=10');//�̺κ��� �ڱ⿡ �°� �����ϼ���
//     noticeWindow.opener = self; 
//}






// - JavaScript - -->

//function MM_openBrWindow(theURL,winName,features) { //v2.0
//  window.open(theURL,winName,features);
//}
//-->
