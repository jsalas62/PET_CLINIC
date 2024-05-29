package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.domain.PetTO;
import com.tecsup.petclinic.services.PetService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * 
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j

public class PetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	// Test busqueda por mes
	@Test
	public void testFindPetsByBirthMonth() throws Exception {
		// Datos esperados en la respuesta
		String NAME_PET1 = "Leo";
		int TYPE_ID1 = 1;
		int OWNER_ID1 = 1;
		String BIRTH_DATE1 = "2000-09-07";

		String NAME_PET2 = "Samantha";
		int TYPE_ID2 = 1;
		int OWNER_ID2 = 6;
		String BIRTH_DATE2 = "1995-09-04";

		String NAME_PET3 = "Max";
		int TYPE_ID3 = 1;
		int OWNER_ID3 = 6;
		String BIRTH_DATE3 = "1995-09-04";

		this.mockMvc.perform(get("/findByBirthMonth/9"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is(NAME_PET1)))
				.andExpect(jsonPath("$[0].typeId", is(TYPE_ID1)))
				.andExpect(jsonPath("$[0].ownerId", is(OWNER_ID1)))
				.andExpect(jsonPath("$[0].birthDate", is(BIRTH_DATE1)))
				.andExpect(jsonPath("$[1].id", is(7)))
				.andExpect(jsonPath("$[1].name", is(NAME_PET2)))
				.andExpect(jsonPath("$[1].typeId", is(TYPE_ID2)))
				.andExpect(jsonPath("$[1].ownerId", is(OWNER_ID2)))
				.andExpect(jsonPath("$[1].birthDate", is(BIRTH_DATE2)))
				.andExpect(jsonPath("$[2].id", is(8)))
				.andExpect(jsonPath("$[2].name", is(NAME_PET3)))
				.andExpect(jsonPath("$[2].typeId", is(TYPE_ID3)))
				.andExpect(jsonPath("$[2].ownerId", is(OWNER_ID3)))
				.andExpect(jsonPath("$[2].birthDate", is(BIRTH_DATE3)));
	}

	@Test
	public void testFindPetsByBirthMonth_NoContent() throws Exception {
		this.mockMvc.perform(get("/findByBirthMonth/10"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testFindAllPets() throws Exception {

		//int NRO_RECORD = 73;
		int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/pets"))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				//		    .andExpect(jsonPath("$", hasSize(NRO_RECORD)))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}
	

	/**
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testFindPetOK() throws Exception {

		String NAME_PET = "Leo";
		int TYPE_ID = 1;
		int OWNER_ID = 1;
		String BIRTH_DATE = "2000-09-07";

		mockMvc.perform(get("/pets/1"))  // Object must be BASIL
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is(NAME_PET)))
				.andExpect(jsonPath("$.typeId", is(TYPE_ID)))
				.andExpect(jsonPath("$.ownerId", is(OWNER_ID)))
				.andExpect(jsonPath("$.birthDate", is(BIRTH_DATE)));
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFindPetKO() throws Exception {

		mockMvc.perform(get("/pets/666"))
				.andExpect(status().isNotFound());

	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void testCreatePet() throws Exception {

		String NAME_PET = "Beethoven";
		int TYPE_ID = 1;
		int OWNER_ID = 1;
		String BIRTH_DATE = "2020-05-20";

		PetTO newPetTO = new PetTO();
		newPetTO.setName(NAME_PET);
		newPetTO.setTypeId(TYPE_ID);
		newPetTO.setOwnerId(OWNER_ID);
		newPetTO.setBirthDate(BIRTH_DATE);

		mockMvc.perform(post("/pets")
						.content(om.writeValueAsString(newPetTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				//.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is(NAME_PET)))
				.andExpect(jsonPath("$.typeId", is(TYPE_ID)))
				.andExpect(jsonPath("$.ownerId", is(OWNER_ID)))
				.andExpect(jsonPath("$.birthDate", is(BIRTH_DATE)));

	}


	/**
     * 
     * @throws Exception
     */
	@Test
	public void testDeletePet() throws Exception {

		String NAME_PET = "Beethoven3";
		int TYPE_ID = 1;
		int OWNER_ID = 1;
		String BIRTH_DATE = "2020-05-20";

		PetTO newPetTO = new PetTO();
		newPetTO.setName(NAME_PET);
		newPetTO.setTypeId(TYPE_ID);
		newPetTO.setOwnerId(OWNER_ID);
		newPetTO.setBirthDate(BIRTH_DATE);

		ResultActions mvcActions = mockMvc.perform(post("/pets")
						.content(om.writeValueAsString(newPetTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();

		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/pets/" + id ))
				/*.andDo(print())*/
				.andExpect(status().isOk());
	}


	@Test
	public void testDeletePetKO() throws Exception {

		mockMvc.perform(delete("/pets/" + "1000" ))
				/*.andDo(print())*/
				.andExpect(status().isNotFound());
	}
}
    