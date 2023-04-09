package fi.aalto.hoaxify.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/1.0")
public class FileUploadController {
	
	@Autowired
	FileService fileService;

	/**
	 * File can be stored independently without associated with a Hoax. For file that is not associated with Hoax and is more than one
	 * hour old, it will be removed.
	 * @param file The file to be added.
	 */
	@PostMapping("/hoaxes/upload")
	FileAttachment uploadForHoax(MultipartFile file) {
		return fileService.saveAttachment(file);
	}

}
