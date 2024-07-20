import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddNewEntry {

    private WebDriver driver;
    private final String baseUrl = "http://localhost/addressbook/";
    private Path screenshotPath = Paths.get("screenshots");

    @BeforeEach
    public void setUp() {
        // Initialize ChromeDriver
        driver = new ChromeDriver();
        // Set implicit wait time
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Create screenshot directory if it doesn't exist
        try {
            Files.createDirectories(screenshotPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTitlePage() {
        driver.navigate().to(baseUrl);
        String title = driver.getTitle();
        assertEquals("Address Book", title);
    }

    @ParameterizedTest
    @CsvSource({
        "'', '', '', '', '', '', '', '', '', '', '','',''",  // All fields empty
        "'A', 'Bikey', 'QA analyst', '2nd St', '', '', 'New York', 'NY', 'United States', '1234567890', 'a.smith@example.com', '1234567890', 'www.example.com'",  //  first name Min character
        "'Andy', 'Aedddddddddddddddddddddaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'Developer', '3rd St', '', '', 'Toronto', 'Ontario', 'Canada', '0987654321', 'andy.dev@example.com', '0987654321', 'www.example.com'",  // Long first name
        "'Bob', 'Peter', 'Tester', '4th St', '', '', 'Los Angeles', 'CA', 'United States', '1231231234', 'bob.peter@example.com', '1231231234', 'www.example.com'",  // Valid input
        "'Charlie', 'White', 'Manager', '5th St', '', '', 'Chicago', 'IL', 'United States', '3213214321', 'charlie.white@example.com', '3213214321', 'www.example.com'",  // Valid input
        "'123', 'Black', 'Consultant', '6th St', '', '', 'Houston', 'TX', 'United States', '4564564567', '123.black@example.com', '4564564567', 'www.example.com'",  // Valid input
        "'J0hn', 'Doe', 'Engineer', '7th St', '', '', 'San Francisco', 'CA', 'United States', '5675675678', 'john.doe@example.com', '5675675678', 'www.example.com'",  // Invalid first name
        "'Jane', 'Doe', 'Designer', '8th St', '', '', 'Seattle', 'WA', 'United States', '6786786789', 'jane.doeexample.com', '6786786789', 'www.example.com'"  // Invalid email
    })
    public void test_AddEntry(String firstName, String lastName, String businessName, String address1, String address2, String address3, String city, String province, 
                              String country1, String postcode, String email, String phone, String webaddress) {
        driver.get(baseUrl);

        WebElement linkAdd = driver.findElement(By.xpath("/html/body/doctype/ul/li[2]/a"));
        linkAdd.click();

        WebElement firstNameField = driver.findElement(By.xpath("//*[@id='addr_first_name']"));
        firstNameField.sendKeys(firstName);

        WebElement lastNameField = driver.findElement(By.xpath("//*[@id='addr_last_name']"));
        lastNameField.sendKeys(lastName);

        WebElement businessNameField = driver.findElement(By.xpath("//*[@id='addr_business']"));
        businessNameField.sendKeys(businessName);

        WebElement address1Field = driver.findElement(By.xpath("//*[@id='addr_addr_line_1']"));
        address1Field.sendKeys(address1);

        WebElement address2Field = driver.findElement(By.xpath("//*[@id='addr_addr_line_2']"));
        address2Field.sendKeys(address2);

        WebElement address3Field = driver.findElement(By.xpath("//*[@id='addr_addr_line_3']"));
        address3Field.sendKeys(address3);

        WebElement cityField = driver.findElement(By.xpath("//*[@id='addr_city']"));
        cityField.sendKeys(city);

        WebElement provinceField = driver.findElement(By.xpath("//*[@id='addr_region']"));
        provinceField.sendKeys(province);

        WebElement country = driver.findElement(By.xpath("//*[@id=\"addr_country\"]"));
        country.sendKeys(country1);

        WebElement pcode = driver.findElement(By.xpath("//*[@id=\"addr_post_code\"]"));
        pcode.sendKeys(postcode);

        WebElement emailField = driver.findElement(By.xpath("//*[@id='addr_email_1']"));
        emailField.sendKeys(email);

        WebElement phoneField = driver.findElement(By.xpath("//*[@id='addr_phone_1']"));
        phoneField.sendKeys(phone);

        WebElement websiteField = driver.findElement(By.xpath("//*[@id='addr_web_url_1']"));
        websiteField.sendKeys(webaddress);

        WebElement submitButton = driver.findElement(By.xpath("//*[@id='submit_button']"));
        submitButton.click();

        // Validation checks
        if (firstName.isEmpty()) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/p"));
            assertEquals("First name is required", errorMsg.getText());
        } else if (firstName.length() < 2) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("Firstname cannot be less than 2 characters", errorMsg.getText());
        } else if (firstName.length() > 50) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("First name must be less than 50 characters", errorMsg.getText());
        } else if (lastName.isEmpty()) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("Last name cannot be empty", errorMsg.getText());
        } else if (lastName.length() < 2) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("Last name cannot be less than 2 characters", errorMsg.getText());
        } else if (lastName.length() > 50) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("Last name cannot be more than 50 characters", errorMsg.getText());
        } else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("Invalid email format", errorMsg.getText());
        } else if (phone.length() != 10) {
            WebElement errorMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("Number cannot be less than or more than 10 digits", errorMsg.getText());
        } else {
            WebElement successMsg = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("The new address book entry was added successfully", successMsg.getText());
        }
    }

    @ParameterizedTest
    @CsvSource({ 
        "'Jane', 'Doe', 'Designer', '8th St', '', '', 'Seattle', 'WA', 'United States', '6786786789', 'jane.doeexample.com', '6786786789', 'www.example.com'"  // Invalid email
    })
    public void clearForm(String firstName, String lastName, String businessName, String address1, String address2, String address3, String city, String province, 
                          String country1, String postcode, String email, String phone, String webaddress) {
        driver.get(baseUrl);

        WebElement linkAdd = driver.findElement(By.xpath("/html/body/doctype/ul/li[2]/a"));
        linkAdd.click();

        WebElement firstNameField = driver.findElement(By.xpath("//*[@id='addr_first_name']"));
        firstNameField.sendKeys(firstName);

        WebElement lastNameField = driver.findElement(By.xpath("//*[@id='addr_last_name']"));
        lastNameField.sendKeys(lastName);

        WebElement businessNameField = driver.findElement(By.xpath("//*[@id='addr_business']"));
        businessNameField.sendKeys(businessName);

        WebElement address1Field = driver.findElement(By.xpath("//*[@id='addr_addr_line_1']"));
        address1Field.sendKeys(address1);

        WebElement address2Field = driver.findElement(By.xpath("//*[@id='addr_addr_line_2']"));
        address2Field.sendKeys(address2);

        WebElement address3Field = driver.findElement(By.xpath("//*[@id='addr_addr_line_3']"));
        address3Field.sendKeys(address3);

        WebElement cityField = driver.findElement(By.xpath("//*[@id='addr_city']"));
        cityField.sendKeys(city);

        WebElement provinceField = driver.findElement(By.xpath("//*[@id='addr_region']"));
        provinceField.sendKeys(province);

        WebElement country = driver.findElement(By.xpath("//*[@id=\"addr_country\"]"));
        country.sendKeys(country1);

        WebElement pcode = driver.findElement(By.xpath("//*[@id=\"addr_post_code\"]"));
        pcode.sendKeys(postcode);

        WebElement emailField = driver.findElement(By.xpath("//*[@id='addr_email_1']"));
        emailField.sendKeys(email);

        WebElement phoneField = driver.findElement(By.xpath("//*[@id='addr_phone_1']"));
        phoneField.sendKeys(phone);

        WebElement websiteField = driver.findElement(By.xpath("//*[@id='addr_web_url_1']"));
        websiteField.sendKeys(webaddress);

        WebElement clearButton = driver.findElement(By.xpath("//*[@id=\"reset_button\"]"));
        clearButton.click();
    }

    @Test 
    public void returnToMainMenu() {
        driver.get(baseUrl);
        WebElement linkAdd = driver.findElement(By.xpath("/html/body/doctype/ul/li[2]/a"));
        linkAdd.click();

        WebElement returnMain = driver.findElement(By.xpath("/html/body/form/table[2]/tbody/tr/td[3]/a"));
        returnMain.click();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                takeScreenshot();
            } catch (IOException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }

    private void takeScreenshot() throws IOException {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path destination = screenshotPath.resolve("screenshot-" + System.currentTimeMillis() + ".png");
        Files.copy(screenshotFile.toPath(), destination);
    }
}
