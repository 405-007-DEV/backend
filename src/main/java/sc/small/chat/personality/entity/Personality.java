package sc.small.chat.personality.entity;import jakarta.persistence.Entity;import jakarta.persistence.GeneratedValue;import jakarta.persistence.GenerationType;import jakarta.persistence.Id;import jakarta.persistence.Table;@Entity@Table(name = "personalities")public class Personality {    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long id;}