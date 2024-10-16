package com.example.onlinenews.error;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private List<FieldError> errors;    // 상세 에러 메시지
    private String reason;              // 에러 이유

    @Builder
    public ErrorResponse(final ExceptionCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = new ArrayList<>();
    }

    @Builder
    protected ErrorResponse(final ExceptionCode code, final String reason) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.reason = reason;
    }

    @Builder
    protected ErrorResponse(final ExceptionCode code, final List<FieldError> errors) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = message;  // 주어진 메시지를 사용합니다.
        this.errors = errors;
    }


    public static ErrorResponse of(final ExceptionCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    @Builder
    public static ErrorResponse of(final ExceptionCode code) {
        return new ErrorResponse(code);
    }

    @Builder
    public static ErrorResponse of(final ExceptionCode code, final String reason){
        return new ErrorResponse(code, reason);
    }


    /**
     * 에러를 e.getBindingResult() 형태로 전달 받는 경우 해당 내용을 상세 내용으로 변경하는 기능을 수행한다.
     */
    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        @Builder
        FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}