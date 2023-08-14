package com.anywayclear.repository;

import com.anywayclear.entity.Member;
import com.anywayclear.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByDeal_SellerOrderByDeal_Produce_EndDate(Member member, Pageable pageable);

    Page<Review> findAllByDeal_SellerAndDeal_Produce_NameContainingOrderByDeal_Produce_EndDate(Member member, String produceName, Pageable pageable);
    Page<Review> findAllByDeal_SellerOrderByDeal_Produce_StartDate(Member member, Pageable pageable);
    Page<Review> findAllByDeal_SellerAndDeal_Produce_NameContainingOrderByDeal_Produce_StartDate(Member member, String produceName, Pageable pageable);

    Page<Review> findAllByDeal_ConsumerOrderByDeal_Produce_EndDate(Member member, Pageable pageable);
    Page<Review> findAllByDeal_ConsumerAndDeal_Produce_NameContainingOrderByDeal_Produce_EndDate(Member member, String produceName, Pageable pageable);
    Page<Review> findAllByDeal_ConsumerOrderByDeal_Produce_StartDate(Member member, Pageable pageable);
    Page<Review> findAllByDeal_ConsumerAndDeal_Produce_NameContainingOrderByDeal_Produce_StartDate(Member member, String produceName, Pageable pageable);
}
