package sc.small.chat.member.dto;import io.swagger.v3.oas.annotations.media.Schema;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Getter;import lombok.NoArgsConstructor;import lombok.Setter;public class OnBoardingDTO {    @Getter    @Setter    @NoArgsConstructor    @AllArgsConstructor    @Builder    @Schema(description = "성향 코드", example = "C")    public static class Request {        String code;    }//    @Getter//    @Setter//    @NoArgsConstructor//    @AllArgsConstructor//    @Builder//    public static class Response {////    }}