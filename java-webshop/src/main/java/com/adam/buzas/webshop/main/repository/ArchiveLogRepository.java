package com.adam.buzas.webshop.main.repository;

import com.adam.buzas.webshop.main.model.ArchiveLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveLogRepository extends CrudRepository<ArchiveLog, Integer> {

}
