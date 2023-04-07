package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Enumeration;

@RestController
@Slf4j
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "세션이 없습니다.";
        }

        //세션은 메모리를 사용하므로 최소한의 데이터만 보관해야한다.
        //세션 타임아웃을 길게 가져가도 메모리 사용이 누적되어 위험.
        //기본 시간은 30분이다.

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name = {}, value = {}", name, session.getAttribute(name)));

        log.info("sessionId = {} ",session.getId());  // jsessionId
        log.info("getMaxInactiveInterval = {} ",session.getMaxInactiveInterval()); //세션의 유효시간 1800초 (30분)
        log.info("creationTime = {} ", new Date(session.getCreationTime())); //세션 생성일시
        log.info("lastAccessedTime = {} ", new Date(session.getLastAccessedTime())); //세션과 연결된 사용자가 최근 서버에 접근한 시간, 클라이언트 -> 서버
        log.info("isNew = {} ", session.isNew()); //새로 생성된 세션인지, 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로 요청해서 조회된 세션인지 여부

        return "세션 출력";
    }
}
