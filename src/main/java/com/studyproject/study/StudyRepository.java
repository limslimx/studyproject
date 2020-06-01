package com.studyproject.study;

import com.studyproject.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {

    boolean existsByPath(String path);

    //LOAD 타입을 이용하여 @NamedEntityGrapth에 명시한 값들은 fetch로 가져오고 나머지 값들은 기본 fetch값들로 가져오도록 함
    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Study findByPath(String path);
}
