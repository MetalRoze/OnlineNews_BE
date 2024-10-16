package com.example.onlinenews.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    //비즈니스 로직의 예외처리(Unchecked Exception 발생시 처리)
    @ExceptionHandler(BusinessException.class) // 만들어준 커스텀익셉션 발생시 처리해주는 곳
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessException ex) {
        log.error("Business Exception Error", ex);

        final ErrorResponse errorResponse = ErrorResponse.of(ex.getExceptionCode(), ex.getMessage());


        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));

    }

    //여기부턴 클라이언트 측의 잘못된 요청에 의한 에러를 처리해줌.
    @ExceptionHandler(NullPointerException.class) // nullPointerExceptiono발생시
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.NULL_POINT_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


    // @valid 유효성 검증에 실패했을 경우 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


}