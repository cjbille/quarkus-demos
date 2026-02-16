package com.cjbdevlabs.quarkus.s3;

import io.quarkiverse.amazon.s3.runtime.S3Crt;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedUpload;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class UploadResourceTest {

    @InjectMock
    @S3Crt
    S3TransferManager s3TransferManager;

    @BeforeEach
     void setup() {
        var putObjectResponse = PutObjectResponse.builder().eTag("test-etag").build();
        var completedUpload = CompletedUpload.builder().response(putObjectResponse).build();
        CompletableFuture<CompletedUpload> successFuture = CompletableFuture.completedFuture(completedUpload);
        Upload mockUpload = mock(Upload.class);
        when(mockUpload.completionFuture()).thenReturn(successFuture);
        when(s3TransferManager.upload(any(UploadRequest.class))).thenReturn(mockUpload);
    }

    @Test
    void testUploadEndpoint() {
        var fileContent = "This is a test file";
        var byteArrayInputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        given()
                .header("filename", "test-file")
                .contentType(MediaType.WILDCARD)
                .body(byteArrayInputStream)
                .when()
                .post("/upload/s3")
                .then()
                .statusCode(202);
        verify(s3TransferManager, times(1)).upload(any(UploadRequest.class));
    }

    @Test
    @Disabled
     void testUploadEndpointWithLocalStack() {
        var fileContent = "Real bytes traveling over the network!";
        given()
                .header("filename", "real-test")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent)
                .when()
                .post("/upload/s3")
                .then()
                .statusCode(202);
    }
}
