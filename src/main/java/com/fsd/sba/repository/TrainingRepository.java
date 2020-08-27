package com.fsd.sba.repository;
import com.fsd.sba.domain.Training;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Training entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByUserIdAndStatus(Long userId, String status);
}
