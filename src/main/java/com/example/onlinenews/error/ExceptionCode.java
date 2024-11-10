package com.example.onlinenews.error;

import lombok.Getter;

@Getter
public enum ExceptionCode {


    REPLY_NOT_FOUND(404, "REPLY_001", "해당되는 id의 댓글을 찾을 수 없습니다."),

    USER_NOT_FOUND(404, "USER_004", "해당 유저를 찾을 수 없습니다."),
    TOKEN_EXPIRED(401, "TOKEN_001", "토큰이 만료되었습니다. 다시 로그인 해주세요."),
    TOKEN_NOT_VALID(401, "TOKEN_002", "유효하지 않는 토큰입니다."),
    USER_CAN_NOT_BE_NULL(400, "USER_005", "사용자는 null이 될 수 없습니다."),
    USER_ID_NOT_FOUND(404, "USER_006", "해당되는 id의 사용자를 찾을 수 없습니다."),
    USER_NOT_ALLOWED(404, "USER_007", "편집장 이상만 가능한 작업입니다."),
    USER_PASSWORD_INCORRECT(401, "USER_008", "비밀번호가 틀렸습니다."),
    INVITE_CODE_MISMATCH(400, "KEY_001", "올바르지 않는 초대코드 입니다."),
    PUBLISHER_NOT_FOUND(404, "PUB_001", "해당 이름의 신문사를 찾을 수 없습니다."),
    SUB_NOT_FOUND(404, "SUB_001", "구독 정보를 찾을 수 없습니다."),

    EMAIL_CONFLICT(409, "EMAIL_009", "이미 존재하는 email입니다. "),

    HISTORY_NOT_FOUND(400,"HIS_001","존재하지 않는 검색 기록입니다."),

    REQUEST_NOT_FOUND(404, "REQUEST_001", "해당되는 id 의 요청을 찾을 수 없습니다."),
    ALREADY_APPROVED(400, "REQUEST_002", "이미 '승인' 한 상태입니다."),
    ALREADY_HOLDING(400, "REQUEST_003", "이미 '보류' 한 상태입니다."),
    ALREADY_REJECTED(400, "REQUEST_004", "이미 '거절' 한 상태입니다."),

    NOTIFICATION_NOT_FOUND(404, "NOTIFICATION_001", "해당되는 id 의 알림을 찾을 수 없습니다."),
    ARTICLE_LIKE_NOT_FOUND(404,"ARTICLE_LIKE_001", "해당되는 id의 좋아요를 찾을 수 없습니다."),
    USER_MISMATCH(400,"USER_010", "본인에게 속하지 않은 항목에 대한 작업은 허용되지 않습니다."),
    ALREADY_LIKED(400, "LIKE_001", "이미 '좋아요'를 누른 상태입니다."),

    NULL_POINT_ERROR(404, "G010", "NullPointerException 발생"),
    PASSWORD_MISMATCH(400, "PASSWORD_001", "비밀번호 확인이 틀렸습니다."),

    SUB_NOT_MATCH_USER(400, "MATCH_001", "현재 구독 사용자와 일치하지 않습니다."),
    HIS_NOT_MATCH_USER(400, "MATCH_002", "현재 검색 사용자와 일치하지 않습니다."),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G011", "Validation Exception 발생"),

    ARTICLE_NOT_FOUND(404, "ARTICLE_001", "해당되는 id 의 기사를 찾을 수 없습니다."),
    S3_UPLOAD_FAILED( 500, "ARTICLE_002", "이미지 업로드에 실패했습니다."),
    FILE_NOT_FOUND(400, "ARTICLE_003", "존재하지 않는 파일입니다."),
    FILE_DELETE_FAILED(400, "ARTICLE_004", "이미지 삭제에 실패했습니다.");





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