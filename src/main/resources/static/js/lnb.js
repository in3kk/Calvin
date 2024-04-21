function initMenu() {
  $('#lnb_menu ul').hide();
  $('#lnb_menu ul').children('.current').parent().show();
  //$('#menu ul:first').show();
  $('#lnb_menu li a').click(
    function() {
      var checkElement = $(this).next();
      if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
        return false;
        }
      if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
        $('#lnb_menu ul:visible').slideUp(200);
        checkElement.slideDown(200);
        return false;
        }
      }
    );
  }
$(document).ready(function() {initMenu();});