package com.anywayclear.repository;

import com.anywayclear.entity.MemberEntity;
import com.anywayclear.entity.MemberStatusEntity;
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
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId("S000001");
        memberEntity.setNickname("flowerdonk");
        memberEntity.setPhone_number("01085025150");
        memberEntity.setDesc("싱싱한 딸기");
        memberEntity.setCompany_registration_number("123456");
        memberEntity.setCompany_address("Incheon");
        MemberEntity savedMember = memberRepository.save(memberEntity);

        MemberStatusEntity memberStatusEntity = new MemberStatusEntity();
        memberStatusEntity.setMemberEntity(savedMember);
        MemberStatusEntity savedMemberStatus = memberStatusRepository.save(memberStatusEntity);

        System.out.println("savedMember = " + savedMember);
        System.out.println("savedMemberStatus = " + savedMemberStatus);
    }
}
