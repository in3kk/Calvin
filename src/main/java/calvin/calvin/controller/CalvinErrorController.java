package calvin.calvin.controller;

import calvin.calvin.domain.ErrorDTO;
import calvin.calvin.error.ErrorCode;
import exception.CustomException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Controller
public class CalvinErrorController implements ErrorController {
//    @Override
//    public String getErrorPath(){
//        return null;
//    }
    @RequestMapping("/error")
    public String ErrorHandler1(HttpServletRequest request, Model model, CustomException ce, Error er,RuntimeException re, Exception e){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String msg = "";
        if(ce != null){
            msg = ce.getMessage();
            status = ce.getStatus();
            System.out.println("에러코드 : "+status);
        }else if(er != null){
            msg = er.getMessage();
        }else if(e != null){
            msg = e.getMessage();
        }
        int statusCode = Integer.valueOf(status.toString());
        if(statusCode == HttpStatus.BAD_REQUEST.value()){
            msg = "잘못된 요청입니다.";
        }else if(statusCode == HttpStatus.NOT_FOUND.value()){
            msg = "페이지를 찾을 수 없습니다.";
        }
        model.addAttribute("message",msg);
        model.addAttribute("status",statusCode);
        model.addAttribute("timestamp", LocalDateTime.now());
        return "error/500";
    }
    @ExceptionHandler(Exception.class)
    public String ErrorHandler6(HttpServletRequest request, Model model,Exception e){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        model.addAttribute("message",e.getMessage());
        model.addAttribute("status",status);

        model.addAttribute("timestamp", LocalDateTime.now());
        return "error/500";
    }

    @GetMapping("/throw-exception/1")
    public ResponseEntity<?> ExceptionHandler1(){
        ErrorCode errorCode = ErrorCode.INVALID_PERMISSION;
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorDTO(errorCode, "권한이 없습니다."));
    }
}
