package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.CloudStorageApplicationTests;
import com.udacity.jwdnd.course1.cloudstorage.WebElements.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.WebElements.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests extends CloudStorageApplicationTests {

    public static final String BEATLES_URL = "https://www.thebeatles.com/";
    public static final String MCCARTNEY_USERNAME = "mccartney";
    public static final String MCCARTNEY_PASSWORD = "mccartney_pswrd";
    public static final String RINGO_URL = "http://www.ringostarr.com/";
    public static final String RINGO_USERNAME = "ringostar";
    public static final String RINGO_PASSWORD = "ringostar_pswrd";

    @Test
    public void testCredentialCreation() {
        HomePage homePage = signUpAndLogin();
        createAndVerifyCredential(BEATLES_URL, MCCARTNEY_USERNAME, MCCARTNEY_PASSWORD, homePage);
        homePage.deleteCredential();
        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickOk();
        homePage.logout();
    }

    private void createAndVerifyCredential(String url, String username, String password, HomePage homePage) {
        createCredential(url, username, password, homePage);
        homePage.navToCredentialsTab();
        Credential credential = homePage.getFirstCredential();
        Assertions.assertEquals(url, credential.getUrl());
        Assertions.assertEquals(username, credential.getUserName());
        Assertions.assertNotEquals(password, credential.getPassword());
    }

    private void createCredential(String url, String username, String password, HomePage homePage) {
        homePage.navToCredentialsTab();
        homePage.addNewCredential();
        setCredentialFields(url, username, password, homePage);
        homePage.saveCredentialChanges();
        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickOk();
        homePage.navToCredentialsTab();
    }

    private void setCredentialFields(String url, String username, String password, HomePage homePage) {
        homePage.setCredentialUrl(url);
        homePage.setCredentialUsername(username);
        homePage.setCredentialPassword(password);
    }

    @Test
    public void testCredentialModification() {
        HomePage homePage = signUpAndLogin();
        createAndVerifyCredential(BEATLES_URL, MCCARTNEY_USERNAME, MCCARTNEY_PASSWORD, homePage);
        Credential originalCredential = homePage.getFirstCredential();
        String firstEncryptedPassword = originalCredential.getPassword();
        homePage.editCredential();
        String newUrl = RINGO_URL;
        String newCredentialUsername = RINGO_USERNAME;
        String newPassword = RINGO_PASSWORD;
        setCredentialFields(newUrl, newCredentialUsername, newPassword, homePage);
        homePage.saveCredentialChanges();
        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickOk();
        homePage.navToCredentialsTab();
        Credential modifiedCredential = homePage.getFirstCredential();
        Assertions.assertEquals(newUrl, modifiedCredential.getUrl());
        Assertions.assertEquals(newCredentialUsername, modifiedCredential.getUserName());
        String modifiedCredentialPassword = modifiedCredential.getPassword();
        Assertions.assertNotEquals(newPassword, modifiedCredentialPassword);
        Assertions.assertNotEquals(firstEncryptedPassword, modifiedCredentialPassword);
        homePage.deleteCredential();
        resultPage.clickOk();
        homePage.logout();
    }

    @Test
    public void testDeletion() {
        HomePage homePage = signUpAndLogin();
        createCredential(BEATLES_URL, MCCARTNEY_USERNAME, MCCARTNEY_PASSWORD, homePage);
        createCredential(RINGO_URL, RINGO_USERNAME, RINGO_PASSWORD, homePage);
        createCredential("http://www.johnlennon.com/", "lennon", "julia", homePage);
        Assertions.assertFalse(homePage.noCredentials(driver));
        homePage.deleteCredential();
        ResultPage resultPage = new ResultPage(driver);
        resultPage.clickOk();
        homePage.navToCredentialsTab();
        homePage.deleteCredential();
        resultPage.clickOk();
        homePage.navToCredentialsTab();
        homePage.deleteCredential();
        resultPage.clickOk();
        homePage.navToCredentialsTab();
        Assertions.assertTrue(homePage.noCredentials(driver));
        homePage.logout();
    }
}