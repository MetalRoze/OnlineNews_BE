package com.example.onlinenews.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    // runtimeException 발생시 날려줄 메시지들을 enum에 작성해놓고,
    // CustomException(현재 클래스) 에서 enum 객체를 생성
    private final ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode exceptionCode){

        this.exceptionCode = exceptionCode;
    }

}
