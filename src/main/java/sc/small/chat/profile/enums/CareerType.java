package sc.small.chat.profile.enums;import lombok.Getter;import sc.small.chat.oauth.enums.ProviderType;@Getterpublic enum CareerType {    FULL_TIME("정규직", "FULL_TIME"),    CONTRACT("계약직", "CONTRACT"),    INTERN("인턴", "INTERN"),;    private final String displayName;    private final String type;    CareerType(String displayName, String type) {        this.displayName = displayName;        this.type = type;    }    public static CareerType typeOf(String type) {        for (CareerType resultReturn : CareerType.values()) {            if (resultReturn.getType().equals(type)) {                return resultReturn;            }        }        return null;    }    public static String typeOfDisplayName(String type) {        CareerType resultReturn = typeOf(type);        return (resultReturn != null) ? resultReturn.getDisplayName() : type;    }}