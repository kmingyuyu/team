package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.entity.DeliveryInfo;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Long> {

}
