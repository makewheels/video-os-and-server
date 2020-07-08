package com.eg.videoosandserver.viewlog;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewLogDao extends CrudRepository<ViewLog, Integer> {
}
