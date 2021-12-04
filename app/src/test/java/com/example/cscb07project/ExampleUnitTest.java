package com.example.cscb07project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    LoginActivity loginView;

    @Mock
    SignupActivity signupView;

    @Mock
    Database model;

    @Test
    public void testUsernameNotFoundLogin() {
        Mockito.when(loginView.getUsername()).thenReturn("");
        MyPresenter presenter = new MyPresenter(loginView, model);

        presenter.checkLogin();
        Mockito.verify(loginView).displayMessage("Please Enter a Username");
    }

    @Test
    public void testPasswordNotFoundLogin() {
        Mockito.when(loginView.getUsername()).thenReturn("Bob");
        Mockito.when(loginView.getPassword()).thenReturn("");
        MyPresenter presenter = new MyPresenter(loginView, model);

        presenter.checkLogin();
        Mockito.verify(loginView).displayMessage("Please Enter a Password");
    }

    @Test
    public void testValidUsernamePasswordLogin() {
        Mockito.when(loginView.getUsername()).thenReturn("Bob");
        Mockito.when(loginView.getPassword()).thenReturn("Something");
        MyPresenter presenter = new MyPresenter(loginView, model);

        presenter.checkLogin();
        Mockito.verify(model).matchPass("Bob", "Something", presenter);
    }

    @Test
    public void testValidateLogin() {
        User user = new Customer();
        MyPresenter presenter = new MyPresenter(loginView, model);

        presenter.validateLogin(user);
        Mockito.verify(loginView).validateLogin(user);
    }

    @Test
    public void testInvalidateLoginIncorrectPassword() {
        MyPresenter presenter = new MyPresenter(loginView, model);

        presenter.invalidateLogin(0);
        Mockito.verify(loginView).displayMessage("Incorrect Password");
    }

    @Test
    public void testInvalidateLoginUserNotFound() {
        MyPresenter presenter = new MyPresenter(loginView, model);

        presenter.invalidateLogin(1);
        Mockito.verify(loginView).displayMessage("User Not Found");
    }

    @Test
    public void testSignupEmptyUsername() {
        Mockito.when(signupView.getUsername()).thenReturn("");
        MyPresenter presenter = new MyPresenter(signupView, model);

        presenter.checkSignup("");
        Mockito.verify(signupView).displayMessage("Username Cannot Be Empty");
    }

    @Test
    public void testSignupCustomer() {
        Mockito.when(signupView.getUsername()).thenReturn("Chicken");
        Mockito.when(signupView.getPassword()).thenReturn("Doggy");
        MyPresenter presenter = new MyPresenter(signupView, model);

        presenter.checkSignup("I am a customer.");
        Mockito.verify(model).addCustomer("Chicken", "Doggy", presenter);
    }

    @Test
    public void testSignupStoreOwner() {
        Mockito.when(signupView.getUsername()).thenReturn("Chicken");
        Mockito.when(signupView.getPassword()).thenReturn("Doggy");
        MyPresenter presenter = new MyPresenter(signupView, model);

        presenter.checkSignup("I am a Store Owner.");
        Mockito.verify(model).addStoreOwner("Chicken", "Doggy", presenter);
    }

    @Test
    public void testValidateSignup() {
        User user = new Customer();
        MyPresenter presenter = new MyPresenter(signupView, model);

        presenter.validateSignup(user);
        Mockito.verify(signupView).displayMessage("Success");
        Mockito.verify(signupView).validateSignup(user);
    }

    @Test
    public void testInvalidateSignup() {
        MyPresenter presenter = new MyPresenter(signupView, model);

        presenter.invalidateSignup();
        Mockito.verify(signupView).displayMessage("Username Taken");
    }
}
