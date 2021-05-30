package gitHub.restAssured;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class gitHubSSHKey {

	// Declare request specification
	RequestSpecification requestSpec;
	// Declare SSH Key
	String sshKey = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIHWaIatZbW4dmBysPfelXzN1tL8PsMD195kKxuJojHvI guruprasadsawant@gmail.com";
	// Fetch SSH Key ID
	int getSSHKeyID;

	@BeforeClass
	public void setUp() {

		// Set GitHub Access Token
		PreemptiveOAuth2HeaderScheme auth = new PreemptiveOAuth2HeaderScheme();
		auth.setAccessToken("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");

		// Create request specification
		requestSpec = new RequestSpecBuilder()
				// Set content type
				.setContentType(ContentType.JSON)
				// Set Authorization Token
				.setAuth(auth)
				// Set base URL
				.setBaseUri("https://api.github.com/user/keys")
				// Build request specification
				.build();

	}

	@Test(priority = 1)
	// Test case using a DataProvider
	public void addSSHKey() {
		String reqBody = "{\"title\": \"TestAPIKey\",\"key\": \""+sshKey+"\"}";
		Response response = given().spec(requestSpec) // Use requestSpec
				.body(reqBody) // Send request body
				.when().post(); // Send POST request
		
		// Extract status from response
		getSSHKeyID = response.then().extract().path("id");
		System.out.println("The SSH Key ID is: " + getSSHKeyID);

		// Print response
		System.out.println(response.asPrettyString());

		// Assertions
		response.then().statusCode(201);
	}
	
	@Test(priority = 2)
	public void getSSHKey() {
		Response response = given().spec(requestSpec) // Use requestSpec
				.when().get(); // Send GET request

		// Print response
		System.out.println(response.asPrettyString());
		
		// Assertions
		response.then().statusCode(200);
	}
	
	@Test(priority = 3)
	public void deleteSSHKey() {
		Response response = given().spec(requestSpec) // Use requestSpec
				.pathParam("sshKeyID", getSSHKeyID) // Add path parameter
				.when().delete("/{sshKeyID}"); // Send GET request

		// Assertions
		response.then().statusCode(204);
	}

}
