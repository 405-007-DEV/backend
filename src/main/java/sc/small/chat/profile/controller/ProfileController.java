package sc.small.chat.profile.controller;import io.swagger.v3.oas.annotations.Operation;import io.swagger.v3.oas.annotations.media.Content;import io.swagger.v3.oas.annotations.media.Schema;import io.swagger.v3.oas.annotations.responses.ApiResponse;import io.swagger.v3.oas.annotations.responses.ApiResponses;import io.swagger.v3.oas.annotations.tags.Tag;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.data.domain.Page;import org.springframework.http.HttpStatus;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PathVariable;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestBody;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RestController;import sc.small.chat.global.annotaion.ValidateDTO;import sc.small.chat.global.exception.SmallChatHttpException;import sc.small.chat.global.security.entity.User;import sc.small.chat.global.security.oauth.entity.OAuthProfile;import sc.small.chat.global.security.oauth.repository.OAuth2ProfileRepository;import sc.small.chat.profile.dto.AllProfileDTO;import sc.small.chat.profile.dto.ChangeProfileOpenDTO.ChangeProfileOpenRequestDTO;import sc.small.chat.profile.dto.ChangeProfileOpenDTO.ChangeProfileOpenResponseDTO;import sc.small.chat.profile.dto.ProfileDTO.ProfileResponseDTO;import sc.small.chat.profile.dto.UpdateProfileDTO;import sc.small.chat.profile.service.ProfileService;@Tag(name = "Profile 프로필")@Slf4j@RestController@RequiredArgsConstructor@RequestMapping("/profiles")public class ProfileController {    private final ProfileService profileService;    private final OAuth2ProfileRepository oAuth2ProfileRepository;    @Operation(summary = "프로필 공개 여부", description = "프로필 공개 여부 API")    @PostMapping("/is-open")    public ChangeProfileOpenResponseDTO updateProfileOpen(ChangeProfileOpenRequestDTO dto) {        OAuthProfile oAuthProfile = getOAuthProfile();        return profileService.updateProfileOpen(oAuthProfile, dto.isOpen());    }    @Operation(summary = "프로필 수정", description = "프로필 수정 API")    @PostMapping("/update")    public UpdateProfileDTO.Response updateProfile(@RequestBody @ValidateDTO UpdateProfileDTO.Request request) {        return profileService.updateProfile(request);    }    @Operation(summary = "프로필 조회", description = "프로필 조회 API")    @GetMapping("/{profileId}")    public ProfileResponseDTO getProfile(@PathVariable("profileId") Long profileId) {        return profileService.getProfile(profileId);    }    @ApiResponses(value = {        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AllProfileDTO.SwaggerDTO.class))),    })    @Operation(summary = "프로필 전체 검색 조회", description = "프로필 전체 검색 조회 API")    @GetMapping("/all")    public Page<AllProfileDTO.Response> findAllProfiles(AllProfileDTO.Request request) {        return profileService.findAllProfiles(request);    }    private OAuthProfile getOAuthProfile() {        OAuthProfile oAuthProfile = oAuth2ProfileRepository.findByProviderId(            User.getCurrentUser().getOAuthProfileProviderId());        if (oAuthProfile == null) {            throw new SmallChatHttpException(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다.");        }        return oAuthProfile;    }}