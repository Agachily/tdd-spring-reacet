package fi.aalto.hoaxify.hoax;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fi.aalto.hoaxify.file.FileAttachment;
import fi.aalto.hoaxify.user.User;

import lombok.Data;

@Data
@Entity
public class Hoax {
	
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Size(min = 10, max=5000)
	@Column(length = 5000)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@ManyToOne
	private User user;

	/* Set the relationship to be mapped by the "hoax" filed in the attachment object */
	@OneToOne(mappedBy="hoax", orphanRemoval = true)
	private FileAttachment attachment;

}
