package sc.small.chat.profile.dto;import io.swagger.v3.oas.annotations.media.Schema;import jakarta.validation.constraints.NotNull;@Schema(description = "프로필 공개 여부 DTO", name = "UpdateProfileDTO")public class ChangeProfileOpenDTO {    @Schema(name = "ChangeProfileOpenRequestDTO")    public record ChangeProfileOpenRequestDTO(        @NotNull(message = "프로필 공개 여부는 필수 입력 값입니다.")        @Schema(description = "프로필 공개 여부 (공개: true, 비공개: false)", type = "boolean", example = "true")        boolean isOpen    ) {    }    @Schema(name = "ChangeProfileOpenResponseDTO")    public record ChangeProfileOpenResponseDTO(        String message    ) {    }}