package com.anywayclear.service;

import com.anywayclear.dto.request.BiddingRequest;
import com.anywayclear.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@PropertySource("classpath:/jwtsecret.properties")
//@PropertySource(value = {"jwtsecret.properties"})
public class AuctionServiceTest {
    @Autowired
    private AuctionService auctionService;

    @Test
//    @WithMockUser(username = "test",roles = {"CONSUMER","SELLER"})
    @DisplayName("현재 가격이 2000원이고 동시에 2명의 2100원 입찰이 들어오는 경우")
    void Bidding() throws InterruptedException {
        // given
        final int BIDDING_PEOPLE = 2;
        AtomicInteger successCount = new AtomicInteger();
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(BIDDING_PEOPLE);

        for (int i = 0; i < BIDDING_PEOPLE; i++) {
            service.execute(() -> {
                try {
                    auctionService.Bidding(1,"a232-1111", new BiddingRequest(2100));
                    successCount.getAndIncrement();
                    System.out.println("입찰 성공");
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("충돌 감지");
                } catch (CustomException e) {
                    System.out.println(e.getExceptionCode());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        assertThat(successCount.get()).isEqualTo(1);
    }
}
