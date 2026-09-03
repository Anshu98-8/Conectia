package com.CodingBrajmohan.connectionService.repository;


import com.CodingBrajmohan.connectionService.entity.PersonEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<PersonEntity, Long> {

    Optional<PersonEntity> findByUserId(Long userId);

    @Query("match (personA:Person) -[:CONNECTED_TO]- (personB:Person) " +
            "where personA.userId = $userId " +
            "return personB")
    List<PersonEntity> getFirstDegreeConnections(Long userId);
}
