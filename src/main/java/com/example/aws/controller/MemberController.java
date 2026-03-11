package com.example.aws.controller;

import com.example.aws.dto.MemberRequest;
import com.example.aws.dto.MemberResponse;
import com.example.aws.entity.Member;
import com.example.aws.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public MemberResponse save(@RequestBody MemberRequest request){
        return memberService.save(request);
    }
    @GetMapping("/{memberId}")
    public MemberResponse findById(@PathVariable Long memberId) {
        return memberService.findById(memberId);
    }
}
