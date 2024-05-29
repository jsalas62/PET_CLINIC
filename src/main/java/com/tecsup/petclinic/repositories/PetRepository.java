package com.tecsup.petclinic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tecsup.petclinic.entities.Pet;

/**
 * 
 * @author jgomezm
 *
 */
@Repository
public interface PetRepository 
	extends JpaRepository<Pet, Integer> {

	// Fetch pets by name
	List<Pet> findByName(String name);

	// Fetch pets by typeId
	List<Pet> findByTypeId(int typeId);

	// Fetch pets by ownerId
	List<Pet> findByOwnerId(int ownerId);

	// Fetch pets by mont of birthDate

	@Query("SELECT p FROM pets p WHERE MONTH(p.birthDate) = :month")
	List<Pet> findByBirthMonth(@Param("month") int month);

	@Override
	List<Pet> findAll();
}