package sc.small.chat.personality.entity;import jakarta.persistence.Column;import jakarta.persistence.Entity;import jakarta.persistence.EnumType;import jakarta.persistence.Enumerated;import jakarta.persistence.FetchType;import jakarta.persistence.GeneratedValue;import jakarta.persistence.GenerationType;import jakarta.persistence.Id;import jakarta.persistence.JoinColumn;import jakarta.persistence.ManyToOne;import jakarta.persistence.Table;import lombok.Builder;import lombok.Getter;import lombok.NoArgsConstructor;import sc.small.chat.global.entity.BaseEntity;import sc.small.chat.member.entity.Member;import sc.small.chat.personality.enums.PersonalityType;@Entity@Table(name = "personalities")@Getter@NoArgsConstructorpublic class Personality extends BaseEntity {    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long id;    @Enumerated(EnumType.STRING)    @Column(name = "personality_type")    private PersonalityType personalityType;    @ManyToOne(fetch = FetchType.LAZY)    @JoinColumn(name = "member_id")    private Member member;    @Builder    public Personality(PersonalityType personalityType) {        this.personalityType = personalityType;    }}