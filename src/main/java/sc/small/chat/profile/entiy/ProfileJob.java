package sc.small.chat.profile.entiy;import jakarta.persistence.Entity;import jakarta.persistence.GeneratedValue;import jakarta.persistence.GenerationType;import jakarta.persistence.Id;import jakarta.persistence.JoinColumn;import jakarta.persistence.ManyToOne;import jakarta.persistence.Table;import lombok.Builder;import lombok.Getter;import lombok.NoArgsConstructor;import org.hibernate.annotations.SQLDelete;import org.hibernate.annotations.SQLRestriction;@Entity@Table(name = "profile_jobs")@Getter@NoArgsConstructor@SQLRestriction("deleted_at IS NULL")@SQLDelete(sql = "UPDATE profile_jobs SET deleted_at = NOW() WHERE id = ?")public class ProfileJob {    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long id;    @ManyToOne    @JoinColumn(name = "profile_id", nullable = false)    private Profile profile;    @ManyToOne    @JoinColumn(name = "job_id", nullable = false)    private Job job;    @Builder    public ProfileJob(Profile profile, Job job) {        this.profile = profile;        this.job = job;    }}