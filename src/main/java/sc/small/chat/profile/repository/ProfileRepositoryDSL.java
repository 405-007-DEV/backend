package sc.small.chat.profile.repository;import com.querydsl.jpa.impl.JPAQueryFactory;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Repository;import sc.small.chat.profile.dto.ProfileDTO.ProfileRequestDTO;import sc.small.chat.profile.dto.ProfileDTO.ProfileResponseDTO;@Repository@RequiredArgsConstructorpublic class ProfileRepositoryDSL {    private final JPAQueryFactory jpaQueryFactory;//    public ProfileResponseDTO findProfile(Long profileId) {//        return jpaQueryFactory.select(new QPro)//    }}