package ch.mse.dea.donteatalone.objects;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.mse.dea.donteatalone.R;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserValidationTest {

    /*
    @Mock
    App mMockApp;
*/
    @Mock
    private App mMockApp;

    @Mock
    private Context mContext;

    @Mock
    private Resources mContextResources;

    @InjectMocks
    private UserValidation uservalidation;




    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        when(mContext.getResources()).thenReturn(mContextResources);
        when(mContext.getResources().getString(R.string.user_validation_error_required))
                .thenReturn("Required");
        when(mContext.getResources().getString(R.string.user_validation_error_length, 4))
                .thenReturn("Length: min 4 characters");
        when(mContext.getResources().getString(R.string.user_validation_error_valid_password))
                .thenReturn("Please enter a valid password");

        App.setApplicationContext(mContext);
    }

    @Test
    public void username() {
        //too short username and null
        String result = UserValidation.username("Sid");

        assertNull(UserValidation.username("Sidgon"));
        assertEquals("Length: min 4 characters.", result);

    }

    @Test
    public void firstname() {
        String result = UserValidation.firstname("Da");

        assertNull(UserValidation.username("David"));
        assertEquals("Length: min 4 characters.", result);
    }

    @Test
    public void lastname() {
        String result = UserValidation.firstname("Gs");

        assertNull(UserValidation.username("Gsponer"));
        assertEquals("Length: min 4 characters.", result);
    }

    @Test
    public void email() {
        String result = UserValidation.email("dav");
        assertEquals("Length: min 4 characters", result);
    }

    @Test
    public void password() {
        String result = UserValidation.password(mMockApp,"pas",false);

        assertNull(UserValidation.password(mMockApp, "MyPassword1",false));
        assertEquals("Please enter a valid password.", result);
    }

    @Test
    public void checkPasswordSecurity() {
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "nocapitalcaseletter",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "NOLOWERCASELETTER",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "NoNumber",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "1234567",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "nocapitalletterbutnumber123",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "NOLOWERCASELETTERBUTNUMBER123",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "short",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "ContainsAND123",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "ContainsNOT123",false));
        assertFalse(UserValidation.checkPasswordSecurity(mMockApp, "ContainsNOTAND123",false));
        assertTrue(UserValidation.checkPasswordSecurity(mMockApp, "MyPassword1", false));
    }

    @Test
    public void notEmpty() {
        assertNull(UserValidation.notEmpty("12345", 4));
    }
}