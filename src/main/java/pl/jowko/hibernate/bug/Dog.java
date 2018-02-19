package pl.jowko.hibernate.bug;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Piotr on 2018-02-19.
 */
@Getter
@Setter
@Entity
public class Dog {
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	
	@OneToOne(mappedBy = "dog")
	private Kennel kennel;
	
	public Dog(String name) {
		this.name = name;
	}
	
	public Dog() {}
	
}
