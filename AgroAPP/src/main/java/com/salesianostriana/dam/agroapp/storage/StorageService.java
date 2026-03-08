package com.salesianostriana.dam.agroapp.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Resource loadAsResource(String filename);

    void deleteFile(String filename);
}
