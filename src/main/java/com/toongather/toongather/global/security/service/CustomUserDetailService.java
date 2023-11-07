package com.toongather.toongather.global.security.service;

import com.toongather.toongather.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userKey) throws UsernameNotFoundException {
        //userKey -> jwt 에서 넣은 user pk id
        return memberRepository.findById(Long.valueOf(userKey))
            .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

}
