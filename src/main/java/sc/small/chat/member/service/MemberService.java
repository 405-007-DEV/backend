package sc.small.chat.member.service;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.stereotype.Component;import sc.small.chat.member.dto.OnBoardingDTO;import sc.small.chat.member.entity.Member;import sc.small.chat.member.repository.MemberRepository;import sc.small.chat.personality.entity.Personality;import sc.small.chat.personality.enums.PersonalityType;import sc.small.chat.personality.reposirtory.PersonalityRepository;@Slf4j@Component@RequiredArgsConstructorpublic class MemberService {    private final MemberRepository memberRepository;    private final PersonalityRepository personalityRepository;}