package sc.small.chat.global.dto;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Getter;import lombok.Setter;@Builder@Getter@Setter@AllArgsConstructorpublic class TokenInfoDTO {    private String accessToken;    private String refreshToken;    private Long refreshTokenExpirationTime;}