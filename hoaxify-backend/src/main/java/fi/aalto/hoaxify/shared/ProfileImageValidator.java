package fi.aalto.hoaxify.shared;

import java.util.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.hoaxify.file.FileService;

public class ProfileImageValidator implements ConstraintValidator<ProfileImage, String>{
	
	@Autowired
	FileService fileService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		
		byte[] decodedBytes = Base64.getDecoder().decode(value);
		String fileType = fileService.detectType(decodedBytes);
		return fileType.equalsIgnoreCase("image/png") || fileType.equalsIgnoreCase("image/jpeg");
	}

}
