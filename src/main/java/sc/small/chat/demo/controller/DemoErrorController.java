package sc.small.chat.demo.controller;import io.swagger.v3.oas.annotations.tags.Tag;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.http.HttpStatus;import org.springframework.web.HttpRequestMethodNotSupportedException;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RequestBody;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RestController;import sc.small.chat.global.exception.SmallChatHttpException;@Tag(name = "DemoError")@RestController@RequiredArgsConstructor@RequestMapping(value = "/demos/errors")public class DemoErrorController {    private static final Logger logger = LoggerFactory.getLogger(DemoErrorController.class);    /**     * 커스텀 에러 발생 테스트     */    @GetMapping("custom-http-exception")    public void customHttpException() {        throw new SmallChatHttpException(HttpStatus.BAD_GATEWAY, "커스텀 에러 테스트");    }    /**     * @Valid 에러 테스트 -> MethodArgumentNotValidException     *///    @GetMapping("valid_exception")//    public void validException(@Valid @RequestBody DemoDTO demoDTO) {//        // Method body can be empty as the validation is handled by the framework//    }    /**     * 400 에러 테스트     */    @GetMapping("bad_request_exception")    public void badRequestException() throws HttpRequestMethodNotSupportedException {        throw new HttpRequestMethodNotSupportedException("400 에러 테스트");    }    /**     * 404 에러 테스트     * 그냥 없는주소 아무거나 던져보기     */    /**     * 500 에러 테스트     */    @GetMapping("server_exception")    public void serverException() {        throw new RuntimeException("서버에러 테스트");    }}