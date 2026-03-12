package com.example.aws.service;

import com.example.aws.dto.MemberRequest;
import com.example.aws.dto.MemberResponse;
import com.example.aws.entity.Member;
import com.example.aws.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
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


    public MemberResponse uploadProfileImage(Long id, MultipartFile file) throws IOException {
        // 1. 멤버 찾기
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없어요!"));

        // 2. S3에 파일 업로드하고 URL 받기
        String imageUrl = s3Service.uploadFile(file);

        // 3. 멤버에 프로필 이미지 URL 저장
        member.updateProfileImage(imageUrl);
        memberRepository.save(member);

        // 4. 응답 반환
        return new MemberResponse(member.getId(), member.getName(), member.getAge(), member.getMbti());
    }

    public String getPresignedUrl(Long id) {
        // 1. 멤버 찾기
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없어요!"));

        // 2. 저장된 파일 key로 Presigned URL 생성
        return s3Service.getPresignedUrl(member.getProfileImageUrl());
    }
}
