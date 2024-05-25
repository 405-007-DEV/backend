package sc.small.chat.member.service;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.http.HttpStatus;import org.springframework.stereotype.Component;import org.springframework.transaction.annotation.Transactional;import sc.small.chat.global.exception.SmallChatHttpException;import sc.small.chat.global.security.entity.User;import sc.small.chat.member.dto.SignUpDTO;import sc.small.chat.member.entity.Member;import sc.small.chat.member.repository.MemberRepository;import sc.small.chat.oauth.entity.OAuthProfile;import sc.small.chat.oauth.repository.OAuth2ProfileRepository;import sc.small.chat.personality.entity.Personality;import sc.small.chat.personality.reposirtory.PersonalityRepository;import sc.small.chat.profile.entiy.Profile;import sc.small.chat.profile.service.ProfileService;@Slf4j@Component@RequiredArgsConstructorpublic class MemberService {    private final MemberRepository memberRepository;    private final ProfileService profileService;    private final PersonalityRepository personalityRepository;    private final OAuth2ProfileRepository oAuth2ProfileRepository;    @Transactional    public SignUpDTO.Response signUp(SignUpDTO.Request signUpRequest) {        OAuthProfile oAuthProfile = oAuth2ProfileRepository.findByProviderId(            User.getCurrentUser().getOAuthProfileProviderId());        if (oAuthProfile == null) {            throw new SmallChatHttpException(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다.");        }        Personality personality = personalityRepository.findById(signUpRequest.getPersonalityId())            .orElseThrow(() -> new SmallChatHttpException(HttpStatus.NOT_FOUND, "회원 성향이 존재하지 않습니다."));        Profile profile = profileService.saveForSignUp(signUpRequest.getSignUpProfileRequestDTO());        Member member = Member.signUpMember(personality, oAuthProfile, profile);        memberRepository.save(member);        return SignUpDTO.Response.builder().message("회원가입이 완료되었습니다.").build();    }}