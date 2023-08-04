package com.anywayclear.repository;

import com.anywayclear.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberStatusRepository memberStatusRepository;

    @Test
    void SignUpTest() {
        Member member = new Member();
        member.setId("S000001");
        member.setNickname("flowerdonk");
//        member.setPhone_number("01085025150");
        member.setDescription("싱싱한 딸기");
//        member.setCompany_registration_number("123456");
//        member.setCompany_address("Incheon");
        Member savedMember = memberRepository.save(member);

        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMember(savedMember);
        MemberStatus savedMemberStatus = memberStatusRepository.save(memberStatus);

        System.out.println("savedMember = " + savedMember);
        System.out.println("savedMemberStatus = " + savedMemberStatus);
    }
}
