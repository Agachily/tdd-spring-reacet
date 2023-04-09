package fi.aalto.hoaxify.hoax;

import java.util.Date;
import java.util.List;

import fi.aalto.hoaxify.file.FileAttachmentRepository;
import fi.aalto.hoaxify.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fi.aalto.hoaxify.file.FileAttachment;
import fi.aalto.hoaxify.file.FileService;
import fi.aalto.hoaxify.user.User;

@Service
public class HoaxService {
	
	HoaxRepository hoaxRepository;
	
	UserService userService;
	
	FileAttachmentRepository fileAttachmentRepository;
	
	FileService fileService;

	public HoaxService(HoaxRepository hoaxRepository, UserService userService,
			FileAttachmentRepository fileAttachmentRepository, FileService fileService) {
		super();
		this.hoaxRepository = hoaxRepository;
		this.userService = userService;
		this.fileAttachmentRepository = fileAttachmentRepository;
		this.fileService = fileService;
	}
	
	public Hoax save(User user, Hoax hoax) {
		hoax.setTimestamp(new Date());
		hoax.setUser(user);
		if(hoax.getAttachment() != null) {
			FileAttachment inDB = fileAttachmentRepository.findById(hoax.getAttachment().getId()).get();
			inDB.setHoax(hoax);
			hoax.setAttachment(inDB);
		}
		return hoaxRepository.save(hoax);
	}

	public Page<Hoax> getAllHoaxes(Pageable pageable) {
		return hoaxRepository.findAll(pageable);
	}

	public Page<Hoax> getHoaxesOfUser(String username, Pageable pageable) {
		User inDB = userService.getByUsername(username);
		return hoaxRepository.findByUser(inDB, pageable);
	}

	public Page<Hoax> getOldHoaxes(long id, String username, Pageable pageable) {
		Specification<Hoax> spec = Specification.where(idLessThan(id));
		if(username != null) {			
			User inDB = userService.getByUsername(username);
			spec = spec.and(userIs(inDB));
		}
		return hoaxRepository.findAll(spec, pageable);
	}


	public List<Hoax> getNewHoaxes(long id, String username, Pageable pageable) {
		Specification<Hoax> spec = Specification.where(idGreaterThan(id));
		if(username != null) {			
			User inDB = userService.getByUsername(username);
			spec = spec.and(userIs(inDB));
		}
		return hoaxRepository.findAll(spec, pageable.getSort());
	}

	public long getNewHoaxesCount(long id, String username) {
		Specification<Hoax> spec = Specification.where(idGreaterThan(id));
		if(username != null) {			
			User inDB = userService.getByUsername(username);
			spec = spec.and(userIs(inDB));
		}
		return hoaxRepository.count(spec);
	}

	private Specification<Hoax> userIs(User user) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
	}

	private Specification<Hoax> idLessThan(long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("id"), id);
	}

	private Specification<Hoax> idGreaterThan(long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), id);
	}

	/**
	 * When delete a Hoax, both the Hoax and its associated attachment will be removed.
	 * @param id Specify the Hoax to be deleted
	 */
	public void deleteHoax(long id) {
		Hoax hoax = hoaxRepository.getOne(id);
		if(hoax.getAttachment() != null) {
			fileService.deleteAttachmentImage(hoax.getAttachment().getName());
		}
		hoaxRepository.deleteById(id);
	}

}
