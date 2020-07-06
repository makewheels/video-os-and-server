package com.eg.videoosandserver.video;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDao extends CrudRepository<Video,Integer> {
}
