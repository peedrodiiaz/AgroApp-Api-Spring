package com.salesianostriana.dam.agroapp.storage;

import com.salesianostriana.dam.agroapp.error.exception.StorageException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    @Value("${storage.location:./uploads}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    @Override
    public void init() {
        rootLocation = Paths.get(storageLocation);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("No se pudo inicializar el directorio de almacenamiento", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("El fichero está vacío");
        }
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );
        String filename = generateUniqueFilename(originalFilename);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Error al almacenar el fichero: " + filename, e);
        }
        return filename;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            UrlResource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("No se puede leer el fichero: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("No se puede leer el fichero: " + filename);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("No se pudo eliminar el fichero: " + filename, e);
        }
    }

    private String generateUniqueFilename(String filename) {
        String newFilename = filename;
        while (Files.exists(rootLocation.resolve(newFilename))) {
            String extension = StringUtils.getFilenameExtension(newFilename);
            String name = extension != null
                    ? newFilename.substring(0, newFilename.length() - extension.length() - 1)
                    : newFilename;
            String suffix = String.valueOf(System.currentTimeMillis() % 1_000_000);
            newFilename = name + "_" + suffix + (extension != null ? "." + extension : "");
        }
        return newFilename;
    }
}
