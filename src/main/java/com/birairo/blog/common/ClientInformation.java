package com.birairo.blog.common;

import jakarta.servlet.http.HttpServletRequest;

public interface ClientInformation {
    String getIp(HttpServletRequest request);
}
