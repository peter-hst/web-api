package hst.shengtao.web.demo;

import hst.shengtao.web.common.Const;
import hst.shengtao.web.common.Payload;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@RestController
public class HomeRoute {

    @GetMapping("/")
    public Payload<InfoDomain> getInfo(HttpServletRequest req) throws UnknownHostException {

        return Payload.success(InfoDomain.builder().hostname(InetAddress.getLocalHost().getHostName())
                .requesterIP(req.getRemoteAddr())
                .sessionID(req.getSession().getId())
                .appVersion(Const.getAppVersion())
                .now(LocalDateTime.now())
                .build());
    }
}
