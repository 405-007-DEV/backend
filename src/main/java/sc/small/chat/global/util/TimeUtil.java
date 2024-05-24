package sc.small.chat.global.util;import java.sql.Date;import java.sql.Timestamp;import java.text.SimpleDateFormat;import java.time.LocalDate;import java.time.LocalDateTime;import java.time.LocalTime;import java.time.ZoneId;import java.util.ArrayList;import java.util.Calendar;import java.util.List;import sc.small.chat.global.dto.TimestampDTO;public class TimeUtil {    /**     * 현재 시간 LocalDateTime 리턴     */    private static LocalDateTime getNowDateTime() {        return LocalDateTime.now();    }    /**     * 현재 시간 Timestamp 리턴     */    public static Timestamp getNowTimestamp() {        return Timestamp.valueOf(getNowDateTime());    }    /**     * 현재 시간 Unix Timestamp 리턴     */    public static String getNowUnixTimestamp() {        double currentTimeSeconds = System.currentTimeMillis() / 1000.0;        return String.format("%.5f", currentTimeSeconds);    }    /**     * workingDate가 null이면 현재 시간 + 2시간을 기준으로 조회     */    public static Date getPurchaseWorkingDate(String workingDate) {        return (workingDate != null) ? Date.valueOf(workingDate) : getPlus2HoursDate();    }    /**     * 2시간 후의 java.sql.Date 반환     * 입고확정 부분 마감시간 때문에 사용     */    private static Date getPlus2HoursDate() {        Calendar calendar = Calendar.getInstance();        calendar.add(Calendar.HOUR_OF_DAY, 2); // 2시간 뒤        // 0시 0분 0초 0밀리초로 설정 - db 로그가 이상하게 남는현상 수정        calendar.set(Calendar.HOUR_OF_DAY, 0);        calendar.set(Calendar.MINUTE, 0);        calendar.set(Calendar.SECOND, 0);        calendar.set(Calendar.MILLISECOND, 0);        return new Date(calendar.getTimeInMillis());    }    public static String parseTimestampToFormattedDate(Timestamp timestamp) {        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        return format.format(timestamp);    }    /**     * 시작일과 종료일 사이의 날짜를 간격 기간(intervalPeriod)만큼 나누어 시작일과 종료일을 리스트로 리턴     */    public static List<TimestampDTO> generateStartEndTimestampsByIntervalPeriod(int intervalPeriod, Timestamp startTimestamp, Timestamp endTimestamp) {        Timestamp startTimestampTemp = startTimestamp;        Timestamp endTimestampTemp = new Timestamp(startTimestamp.getTime() + intervalPeriod - 1);        Timestamp lastStartTimestampTemp = endTimestampTemp;        List<TimestampDTO> result = new ArrayList<>();        while (startTimestampTemp.getTime() < endTimestamp.getTime()) {            lastStartTimestampTemp = startTimestampTemp; // 현재 시작 타임스탬프를 마지막 시작 타임스탬프로 갱신            endTimestampTemp = new Timestamp(startTimestampTemp.getTime() + intervalPeriod - 1); // 현재 시작 타임스탬프로부터 간격을 더한 종료 타임스탬프 생성            if (endTimestampTemp.getTime() <= endTimestamp.getTime()) { // 종료 타임스탬프가 주어진 종료 타임스탬프보다 이전인 경우                result.add(new TimestampDTO(startTimestampTemp, endTimestampTemp)); // 시작과 종료 타임스탬프를 리스트에 추가            }            startTimestampTemp = new Timestamp(endTimestampTemp.getTime() + 1); // 다음 시작 타임스탬프를 설정        }        if (endTimestampTemp.getTime() > endTimestamp.getTime()) {            endTimestampTemp = new Timestamp(endTimestamp.getTime() - 1); // DatePageRequest.endTimestamp에서 +1을 증가 시켜 DB 조회시 1일을 뺌.            result.add(new TimestampDTO(lastStartTimestampTemp, endTimestampTemp)); // 마지막 시작 타임스탬프부터 수정된 종료 타임스탬프까지 리스트에 추가        }        return result; // 결과 리스트 반환    }    public static Date getWorkingDate() {        LocalTime today10pm = LocalTime.of(22, 0);        LocalTime now = LocalTime.now(ZoneId.systemDefault());        LocalDate workingDate = now.isAfter(today10pm) ? LocalDate.now().plusDays(1) : LocalDate.now();        return Date.valueOf(workingDate);    }    public static Timestamp convertStringToTimestamp(String date) {        return Timestamp.valueOf(date + "-01 00:00:00");    }}