package com.birairo.blog.common.support;

import com.birairo.blog.common.ClientInformation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class HeaderInformation implements ClientInformation {
    @Override
    public String getIp(HttpServletRequest request) {

        String[] HeaderCandidates = {"X-Forwarded-For", "Proxy-Client-IP",
                "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

        for (String header : HeaderCandidates) {
            String ip = request.getHeader(header);
            if (ip != null) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
