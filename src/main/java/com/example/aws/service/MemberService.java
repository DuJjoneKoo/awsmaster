package com.example.aws.service;

import com.example.aws.dto.MemberRequest;
import com.example.aws.dto.MemberResponse;
import com.example.aws.entity.Member;
import com.example.aws.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse save(MemberRequest request) {
        //1. Builder로 Member 만들기
        Member member = Member.builder()
                .name(request.getName())
                .age(request.getAge())
                .mbti(request.getMbti())
                .build();

        //2. DB에 저장하기
        Member saved = memberRepository.save(member);

        //3. MemberResponse로 변환해서 반환
        return new MemberResponse(saved.getId(), saved.getName(), saved.getAge(), saved.getMbti());


    }
    public MemberResponse findById(Long id){
        // 1. DB에서 id로 멤버를 찾아야함
       Member found = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없어요!"));
        // 2. 요청값을 반환해야함
        return new MemberResponse(found.getId(), found.getName(), found.getAge(), found.getMbti());
    }

}
