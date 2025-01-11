package com.ht.qlktx.modules.room_type;

import com.ht.qlktx.entities.RoomType;
import com.ht.qlktx.projections.RoomTypeWithRoomCountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<RoomType> findByNameContainingIgnoreCaseAndDeletedIsFalse(String keyword);

    Optional<RoomType> findByIdAndDeletedIsFalse(Long id);

    List<RoomType> findAllByDeletedIsFalse();

    @Query("""
            SELECT rt.id AS id, rt.name AS name, COUNT(r.id) AS totalRooms
            FROM RoomType rt
            LEFT JOIN Room r ON rt.id = r.type.id AND r.deleted = false
            WHERE rt.deleted = false
            GROUP BY rt.id, rt.name
    """)
    List<RoomTypeWithRoomCountView> findAllWithRoomCount();

    boolean existsByNameAndDeletedIsFalse(String name);

    Optional<RoomType> findByNameAndDeletedIsTrue(String name);
}
