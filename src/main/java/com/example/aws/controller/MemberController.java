package com.example.aws.controller;

import com.example.aws.dto.MemberRequest;
import com.example.aws.dto.MemberResponse;
import com.example.aws.entity.Member;
import com.example.aws.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// log 객체를 위해 Slf4j 어노테이션 추가함
@Slf4j
@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public MemberResponse save(@RequestBody MemberRequest request){
        log.info("[API - LOG] POST /api/members 요청");
        return memberService.save(request);
    }
    @GetMapping("/{memberId}")
    public MemberResponse findById(@PathVariable Long memberId) {
        log.info("[API - LOG] GET /api/members/{} 요청", memberId );
        return memberService.findById(memberId);
    }
}
