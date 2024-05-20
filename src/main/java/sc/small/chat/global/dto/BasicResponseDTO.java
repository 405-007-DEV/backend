package sc.small.chat.global.dto;import com.fasterxml.jackson.annotation.JsonIgnore;import org.springframework.http.HttpStatus;public class BasicResponseDTO<T> {    private final T data;    private final Meta meta;    public BasicResponseDTO(T data, Meta meta) {        this.data = data;        this.meta = meta;    }    public static class Meta {        @JsonIgnore        private final HttpStatus httpStatus;        private final int code;        private String message;        private final String result;        public Meta(HttpStatus httpStatus, int code, String message, String result) {            this.httpStatus = httpStatus;            this.code = code;            this.message = message;            this.result = result;        }        public HttpStatus getHttpStatus() {            return httpStatus;        }        public int getCode() {            return code;        }        public String getMessage() {            return message;        }        public void setMessage(String message) {            this.message = message;        }        public String getResult() {            return result;        }    }    public T getData() {        return data;    }    public Meta getMeta() {        return meta;    }    public static <T> BasicResponseDTO<T> success(T responseData) {        return new BasicResponseDTO<>(            responseData,            new Meta(                HttpStatus.OK,                0, // 메타 코드 응답용                "",                "success"            )        );    }    public static BasicResponseDTO<Object> error(HttpStatus httpStatus, String message, String validation, Object data) {        return new BasicResponseDTO<>(            data,            new Meta(                httpStatus != null ? httpStatus : HttpStatus.BAD_REQUEST,                -1, // 메타 코드 응답용                validation != null ? message + " [" + validation + "]" : message,                "failure"            )        );    }    public static BasicResponseDTO<Object> error() {        return error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", null, null);    }    public static BasicResponseDTO<Object> error(String message) {        return error(HttpStatus.BAD_REQUEST, message, null, null);    }    public static BasicResponseDTO<Object> error(String message, String validation) {        return error(HttpStatus.BAD_REQUEST, message, validation, null);    }    public static BasicResponseDTO<Object> error(HttpStatus httpStatus, String message) {        return error(httpStatus, message, null, null);    }    public static BasicResponseDTO<Object> errorWithData(String message, Object data) {        return error(HttpStatus.BAD_REQUEST, message, null, data);    }    public static BasicResponseDTO<Object> errorWithData(String message, String validation, Object data) {        return error(HttpStatus.BAD_REQUEST, message, validation, data);    }    public static BasicResponseDTO<Object> errorWithData(HttpStatus httpStatus, String message, Object data) {        return error(httpStatus, message, null, data);    }}