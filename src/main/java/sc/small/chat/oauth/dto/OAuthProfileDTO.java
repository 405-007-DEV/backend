package sc.small.chat.oauth.dto;import java.util.Collection;import java.util.Map;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Getter;import lombok.Setter;import org.springframework.security.core.GrantedAuthority;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.security.oauth2.core.oidc.OidcIdToken;import org.springframework.security.oauth2.core.oidc.OidcUserInfo;import org.springframework.security.oauth2.core.oidc.user.OidcUser;import org.springframework.security.oauth2.core.user.OAuth2User;import sc.small.chat.oauth.entity.OAuthProfile;import sc.small.chat.oauth.enums.ProviderType;import sc.small.chat.oauth.util.OAuth2UserInfo;@Getter@Setter@AllArgsConstructorpublic class OAuthProfileDTO implements OAuth2User, UserDetails, OidcUser {    private final String email;    private final String name;    private final String password;    private final String picture;    private final ProviderType providerType;    private Collection<GrantedAuthority> authorities;    private Map<String, Object> attributes;    @Override    public Map<String, Object> getAttributes() {        return attributes;    }    @Override    public Collection<? extends GrantedAuthority> getAuthorities() {        return authorities;    }    @Override    public String getName() {        return email;    }    @Override    public String getUsername() {        return name;    }    @Override    public boolean isAccountNonExpired() {        return true;    }    @Override    public boolean isAccountNonLocked() {        return true;    }    @Override    public boolean isCredentialsNonExpired() {        return true;    }    @Override    public boolean isEnabled() {        return true;    }    @Override    public Map<String, Object> getClaims() {        return null;    }    @Override    public OidcUserInfo getUserInfo() {        return null;    }    @Override    public OidcIdToken getIdToken() {        return null;    }    @Builder    public OAuthProfileDTO(String email, String name, String password, String picture, ProviderType providerType) {        this.email = email;        this.name = name;        this.password = password;        this.picture = picture;        this.providerType = providerType;    }    public static OAuthProfileDTO create(OAuthProfile profile) {        return OAuthProfileDTO.builder()            .name(profile.getName())            .email(profile.getEmail())            .picture(profile.getPicture())            .providerType(profile.getProviderType())            .build();    }    public static OAuthProfileDTO create(OAuthProfile profile, Map<String, Object> attributes) {        OAuthProfileDTO userPrincipal = create(profile);        userPrincipal.setAttributes(attributes);        return userPrincipal;    }    public static OAuthProfile createOAuthProfile(OAuth2UserInfo userInfo, ProviderType providerType) {        return OAuthProfile.builder()            .name(userInfo.getName())            .email(userInfo.getEmail())            .picture(userInfo.getPicture())            .providerType(providerType)            .providerId(userInfo.getProviderId())            .build();    }    public static void updateOAuthProfile(OAuthProfile profile, OAuth2UserInfo userInfo) {        if (userInfo.getName() != null && !profile.getName().equals(userInfo.getName())) {            profile.updateProfileByAnotherProfile(userInfo);        }    }}