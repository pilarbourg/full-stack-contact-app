package com.example.practice_full_stack_app.service;

import com.example.practice_full_stack_app.domain.Contact;
import com.example.practice_full_stack_app.repo.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.practice_full_stack_app.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class ContactService { // Defines CRUD operations for all contacts
    private final ContactRepo contactRepo;

    public ContactService(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    public Page<Contact> getAllContacts(int page, int size) {
        return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
        // Sorts contacts list in alphabetical order
    }

    public Contact getContact(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Contact createContact(Contact contact) {
        return contactRepo.save(contact);
    }

    public void deleteContact(Contact contact) {
        contactRepo.delete(contact);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        // MultipartFile is physical file found on computer that contains image
        // log.info("Saving photo for contact: {}", id);
        Contact contact = getContact(id);
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepo.save(contact);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
    // We filter the filename to find a dot or period, which indicates the end of the file which is then followed by the extension
    // Thus, the presence of the dot moves us to the last index, + 1, giving us what comes after the dot.
    // This tells us the file extension by creating a substring of the extension.

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
            // Allows for multiple inputs (String, MultipartFile) and one output (String)
        // Function which takes in two arguments and produces a result. Requires .apply()
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            // Gives specific location on computer where we can save the file we receive.
            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            // getInputStream gets us the image data, REPLACE_EXISTING replaces the image if it already exists
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();
        }catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };
}
