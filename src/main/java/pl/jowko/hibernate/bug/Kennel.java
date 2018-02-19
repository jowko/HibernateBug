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
public class Kennel {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	@OneToOne
	private Dog dog;
	
	public Kennel(String name, Dog dog){
		this.name = name;
		this.dog = dog;
	}
	
	public Kennel() {}
	
}
