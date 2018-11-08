package ch.mse.dea.donteatalone.Objects;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.mse.dea.donteatalone.R;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserValidationTest {

    /*
    @Mock
    App mMockApp;
*/
    @Mock
    App mMockApp;

    @InjectMocks
    private UserValidation uservalidation;




    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void username() {
        //too short username
        String errShort = "Length: min 4 characters.";
        when(mMockApp.getFromResource(R.string.user_validation_error_length, 4))
                .thenReturn(errShort);

        String result = UserValidation.username("Sid");

        //assertEquals(UserValidation.username("Sid"), "Length: min 4 characters.");
        //valid username
        assertNull(UserValidation.username("Sidgon"));
    }

    @Test
    public void firstname() {
        assertNull(UserValidation.firstname("David"));
    }

    @Test
    public void lastname() {
        assertNull(UserValidation.lastname("Gsponer"));
    }

    @Test
    public void email() {
        //doesnt like the android.util library
        //assertNull(UserValidation.email("david.gsponer@stud.hslu.ch"));
    }

    @Test
    public void password() {
        assertNull(UserValidation.password(mMockApp, "MyPassword1",false));
    }

    @Test
    public void checkPasswordSecurity() {
        assertTrue(UserValidation.checkPasswordSecurity(mMockApp, "MyPassword1", false));
    }

    @Test
    public void notEmpty() {
        assertNull(UserValidation.notEmpty("12345", 4));
    }
}