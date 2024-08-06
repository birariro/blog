package com.birairo.blog.config.request;

import com.birairo.blog.common.ClientInformation;
import com.birairo.blog.member.domain.Member;
import com.birairo.blog.member.service.CreateIfNecessaryGuestLoad;
import com.birairo.blog.member.service.support.repository.MemberRepository;
import com.birairo.blog.vo.Client;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberOrGuestArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final ClientInformation clientInformation;
    private final CreateIfNecessaryGuestLoad createIfNecessaryGuestNicknameLoad;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Client.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {

            User user = (User) authentication.getPrincipal();
            Member member = memberRepository.findByLoginId(user.getUsername())
                    .orElseThrow(() -> new IllegalAccessException());

            return Client.of(member.getNickname(), true);
        }

        String ip = clientInformation.getIp(webRequest.getNativeRequest(HttpServletRequest.class));
        return Client.of(createIfNecessaryGuestNicknameLoad.getGuest(ip).getNickname(), false);
    }
}
