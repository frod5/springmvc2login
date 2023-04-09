package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


//웹요청 > 서버 > 필터 > 서블릿 > 인터셉터 > 컨트롤러
//필터는 서블릿을 사용
//인터셉터는 스프링 MVC를 사용
//꼭 서블릿으로 기능을 구현해야하는 것 아니면 인터셉터를 이용하는게 낫다. 기능도 더욱 많음

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";

    @Override //컨트롤러 호출전 실행
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID,uuid); //로그를 찍기위해 afterCompletion에 로그아이디를 넘기기위해 이용

        //@RequestMapping : HandlerMethod
        //정적 리소스 : ResouceHttpRequestHandler
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;//호출할 컨트롤러 메소드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]",uuid,requestURI,handler);
        return true;  //return false를 하면 여기서 로직 중지.
    }

    @Override //컨트롤러 호출 후 실행 단, 컨트롤러에서 예외 발생시 호출되지 않는다.
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    @Override //컨트롤러 호출 후 뷰 렌더링이 된 이후 호출된다. 예외가 발생하더라도 호출.
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]",logId,requestURI,handler);

        if(ex != null) {
            log.error("afterCompletion error!!",ex);
        }
    }
}
