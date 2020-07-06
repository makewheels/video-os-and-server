package com.eg.videoosandserver.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VideoService {
    @Autowired
    private VideoDao videoDao;
}
