package com.library.bible.memberrent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.bible.member.model.Member;
import com.library.bible.memberrent.model.MemberRent;
import com.library.bible.memberrent.service.IMemberRentService;
import com.library.bible.resolver.AuthMember;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRentController {
	private final IMemberRentService memberRentService;
	private final String MEMBER_RENT_PATH = "/rent-status";
	
	@GetMapping("/me" + MEMBER_RENT_PATH)
	public ResponseEntity<MemberRent> selectMemberRent(@AuthMember Member member) {
		MemberRent memberRent = memberRentService.selectMemberRentByMemId(member.getMemId());
		return ResponseEntity.ok(memberRent);
	}

	@GetMapping("/{memId}" + MEMBER_RENT_PATH)
	public ResponseEntity<MemberRent> selectMemberRent(@PathVariable int memId) {
		MemberRent memberRent = memberRentService.selectMemberRentByMemId(memId);
		return ResponseEntity.ok(memberRent);
	}
	
	@PutMapping("/me" + MEMBER_RENT_PATH)
	public ResponseEntity<MemberRent> updateMemberRent(@AuthMember Member member, @Valid @RequestBody MemberRent memberRent) {
		memberRent.setMemId(member.getMemId());
		memberRentService.updateMemberRent(memberRent);
		return ResponseEntity.ok(memberRent);
	}

	@PutMapping("/{memId}" + MEMBER_RENT_PATH)
	public ResponseEntity<MemberRent> updateMemberRent(@PathVariable int memId, @Valid @RequestBody MemberRent memberRent) {
		memberRent.setMemId(memId);
		memberRentService.updateMemberRent(memberRent);
		return ResponseEntity.ok(memberRent);
	}

	@DeleteMapping("/me" + MEMBER_RENT_PATH)
	public ResponseEntity<?> deleteAddress(@AuthMember Member member) {
		memberRentService.deleteMemberRent(member.getMemId());
		return ResponseEntity.noContent().build();
	}

	
	@DeleteMapping("/{memId}" + MEMBER_RENT_PATH)
	public ResponseEntity<?> deleteAddressByMemId(@PathVariable int memId) {
		memberRentService.deleteMemberRent(memId);
		return ResponseEntity.noContent().build();
	}
}
