package com.zubayer.zpos.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.pk.XusersPK;

/**
 * @author Zubayer Ahamed
 * @since May 22, 2024
 * CSE202401068
 */
@Repository
public interface XusersRepo extends JpaRepository<Xusers, XusersPK> {

	Optional<Xusers> findByZidAndXusername(Integer zid, Integer xusername);
}
