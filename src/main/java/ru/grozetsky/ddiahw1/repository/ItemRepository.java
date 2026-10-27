package ru.grozetsky.ddiahw1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.grozetsky.ddiahw1.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
