package com.sgp.gdsc_hackathon.user;


import com.sgp.gdsc_hackathon.post.Post;
import com.sgp.gdsc_hackathon.post.PostRepository;
import com.sgp.gdsc_hackathon.post.PostService;
import com.sgp.gdsc_hackathon.security.JwtTokenProvider;
import com.sgp.gdsc_hackathon.security.dto.TokenInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sgp.gdsc_hackathon.global.SecurityUtil.getLoginUsername;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostRepository postRepository;

    public TokenInfo login(String memberId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Transactional
    public String signup(SignUpDto signUpDto) {
        Member member = signUpDto.toEntity();
        member.addUserAuthority();

        member.encodePassword(passwordEncoder);

        if(memberRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID 입니다.");
        }

        Member savedMember = memberRepository.save(member);
        return savedMember.getUsername();
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findMember(String username) {
        return memberRepository.findByUsername(username).get();
    }

    public Boolean didPost() {
        Member member = memberRepository.findByUsername(getLoginUsername()).get();
        List<Post> posts = postRepository.findAllByMember(member);

        LocalDate today = LocalDate.now();

        if (!posts.isEmpty()) {
            posts.sort(Comparator.comparingLong(Post::getId));
            return posts.get(posts.size() - 1).getCreatedAt().toLocalDate().equals(today);
        }else {
            return false;
        }
    }

//    public MemberInfo getUserInfo() {
//        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
//
//        return MemberInfo.builder()
//                .member_id(member.getId())
//                .username(member.getUsername())
//                .name(member.getName())
//                .nickName(member.getNickName())
//                .grade(member.getGrade())
//                .email(member.getEmail())
//                .score(member.getScore())
//                .build();
//    }

//    public List<MemberInfo> getAllUser() {
//        List<Member> members = memberRepository.findAll();
//        List<MemberInfo> memberInfos = new ArrayList<>();
//
//        for (Member member : members) {
//            memberInfos.add(MemberInfo.builder()
//                    .member_id(member.getId())
//                    .username(member.getUsername())
//                    .name(member.getName())
//                    .nickName(member.getNickName())
//                    .grade(member.getGrade())
//                    .email(member.getEmail())
//                    .score(member.getScore())
//                    .build());
//        }
//        return memberInfos;
//    }
}
