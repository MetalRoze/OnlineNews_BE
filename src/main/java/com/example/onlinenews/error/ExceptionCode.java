package com.example.onlinenews.error;

import lombok.Getter;

@Getter
public enum ExceptionCode {


    ARTICLE_NOT_FOUND(404, "ARTICLE_001", "해당되는 id 의 기사를 찾을 수 없습니다."),
    REPLY_NOT_FOUND(404, "REPLY_001", "해당되는 id의 댓글을 찾을 수 없습니다."),
    ALREADY_LIKED(400, "LIKE_001", "이미 '좋아요'를 누른 상태입니다."),


    USER_NOT_FOUND(404, "USER_004", "해당 유저를 찾을 수 없습니다."),
    TOKEN_NOT_VALID(401, "TOKEN_001", "토큰이 만료되었습니다. 다시 로그인 해주세요."),
    USER_CAN_NOT_BE_NULL(400, "USER_005", "사용자는 null이 될 수 없습니다."),
    USER_ID_NOT_FOUND(404, "USER_006", "해당되는 id의 사용자를 찾을 수 없습니다."),
    USER_NOT_ALLOWED(404, "USER_007", "편집장 이상만 가능한 작업입니다."),
    USER_PASSWORD_INCORRECT(401, "USER_008", "비밀번호가 틀렸습니다."),
    INVITE_CODE_MISMATCH(400, "KEY_001", "올바르지 않는 초대코드 입니다."),

    EMAIL_CONFLICT(409, "EMAIL_009", "이미 존재하는 email입니다. "),

    REQUEST_NOT_FOUND(404, "REQUEST_001", "해당되는 id 의 요청을 찾을 수 없습니다."),
    ALREADY_APPROVED(400, "REQUEST_002", "이미 '승인' 한 상태입니다."),
    ALREADY_HOLDING(400, "REQUEST_003", "이미 '보류' 한 상태입니다."),
    ALREADY_REJECTED(400, "REQUEST_004", "이미 '거절' 한 상태입니다."),


    NULL_POINT_ERROR(404, "G010", "NullPointerException 발생"),
    PASSWORD_MISMATCH(400, "PASSWORD_001", "비밀번호 확인이 틀렸습니다."),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G011", "Validation Exception 발생");

    // 1. status = 날려줄 상태코드
    // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
    // 3. message = 발생한 예외에 대한 설명.

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}