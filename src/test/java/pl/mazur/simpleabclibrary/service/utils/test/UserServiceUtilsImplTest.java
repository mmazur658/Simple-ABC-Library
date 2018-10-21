package pl.mazur.simpleabclibrary.service.utils.test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import pl.mazur.simpleabclibrary.entity.User;
import pl.mazur.simpleabclibrary.service.utils.UserServiceUtilsImpl;
import pl.mazur.simpleabclibrary.utils.PasswordUtilImpl;
import pl.mazur.simpleabclibrary.utils.PasswordUtils;
import pl.mazur.simpleabclibrary.utils.PeselValidator;
import pl.mazur.simpleabclibrary.utils.PeselValidatorImpl;

@PropertySource("classpath:messages.properties")
class UserServiceUtilsImplTest {

	private PasswordUtils passwordUtils = new PasswordUtilImpl();
	private Environment env;
	private PeselValidator peselValidator = new PeselValidatorImpl();
	private UserServiceUtilsImpl userServiceUtilsImpl = new UserServiceUtilsImpl(passwordUtils, peselValidator, env);
	private User tempUser;
	private User expectedUser;

	@Test
	void shouldsetAdditionalUserData() {

		expectedUser = new User();
		expectedUser.setPassword("Admin123");

		tempUser = new User();
		tempUser.setActive(true);
		tempUser.setAdmin(false);
		tempUser.setEmployee(false);

		userServiceUtilsImpl.setAdditionalUserData(expectedUser);

		assertEquals(tempUser.isActive(), expectedUser.isActive());
		assertEquals(tempUser.isAdmin(), expectedUser.isAdmin());
		assertEquals(tempUser.isEmployee(), expectedUser.isEmployee());

	}

	@Test
	void shouldUpdateUserData() {

		tempUser = new User();
		tempUser.setFirstName("Marcin");
		tempUser.setEmail("mmazur@op.pl");
		tempUser.setPesel("90060804786");
		tempUser.setLastName("Mazur");
		tempUser.setCity("Wroc³aw");
		tempUser.setSex("Kobieta");
		tempUser.setBirthday(peselValidator.getBirthDate(tempUser.getPesel()));

		expectedUser = new User();

		userServiceUtilsImpl.updateUserData(expectedUser, tempUser);

		assertEquals(tempUser.getFirstName(), expectedUser.getFirstName());
		assertEquals(tempUser.getLastName(), expectedUser.getLastName());
		assertEquals(tempUser.getEmail(), expectedUser.getEmail());
		assertEquals(tempUser.getPesel(), expectedUser.getPesel());
		assertEquals(tempUser.getCity(), expectedUser.getCity());
		assertEquals(tempUser.getSex(), expectedUser.getSex());
		assertEquals(tempUser.getBirthday(), expectedUser.getBirthday());

	}

}
